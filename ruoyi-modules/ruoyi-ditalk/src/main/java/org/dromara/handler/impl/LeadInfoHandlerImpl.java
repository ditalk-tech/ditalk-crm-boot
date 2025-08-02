package org.dromara.handler.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.app.domain.bo.LeadContactBo;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ILeadInfoHandler;
import org.dromara.module.contact.domain.ContactInfo;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.contact.mapper.ContactInfoMapper;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.lead.domain.LeadInfo;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.mapper.LeadInfoMapper;
import org.dromara.module.lead.service.ILeadInfoService;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 客户线索信息应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadInfoHandlerImpl implements ILeadInfoHandler {

    private final ILeadInfoService leadInfoService;
    private final IContactInfoService contactInfoService;
    private final ISysUserService sysUserService;
    private final LeadInfoMapper leadInfoMapper;
    private final ContactInfoMapper contactInfoMapper;

    @Override
    @DSTransactional
    public Boolean addByBo(LeadContactBo bo) {
        Long leadInfoId = IdUtil.getSnowflakeNextId();
        Long contactInfoId = IdUtil.getSnowflakeNextId();
        //
        LeadInfoBo leadInfoBo = bo.getLeadInfoBo();
        leadInfoBo.setAssignedTo(LoginHelper.getUserId()); // 设置分配给当前登录用户
        leadInfoBo.setAssignedDept(LoginHelper.getDeptId());
        leadInfoBo.setId(leadInfoId);
        leadInfoBo.setContactId(contactInfoId);
        Boolean f1 = leadInfoService.insertByBo(leadInfoBo);
        //
        ContactInfoBo contactInfoBo = bo.getContactInfoBo();
        contactInfoBo.setId(contactInfoId);
        contactInfoBo.setCustomerId(leadInfoBo.getId());
        Boolean f2 = contactInfoService.insertByBo(bo.getContactInfoBo());
        if (!f1 || !f2) throw new UserException("添加客户线索信息失败，请检查数据内容");
        return true;
    }

    @Override
    @DSTransactional
    public Boolean editByBo(LeadContactBo bo) {
        Boolean f1 = leadInfoService.updateByBo(bo.getLeadInfoBo());
        Boolean f2 = contactInfoService.updateByBo(bo.getContactInfoBo());
        if (!f1 || !f2) throw new UserException("更新客户线索信息失败，请检查数据内容");
        return true;
    }

    @Override
    @DSTransactional
    public Boolean reclaimById(List<Long> leadIds) {
        if (ArrayUtil.isEmpty(leadIds)) {
            throw new UserException("回收的线索不能为空");
        }
        for (Long leadId : leadIds) {
            // 回收客户到公海
            LeadInfoVo leadInfoVo = leadInfoService.queryById(leadId); // !!! 这里校验数据权限，同时获取版本号
            if (leadInfoVo == null || leadInfoVo.getAssignedTo() == null) {
                throw new UserException("线索不存在或已在线索池中");
            }
            CacheUtils.evict(CacheNames.LeadInfo, leadId); // 清除缓存
            CacheUtils.evict(CacheNames.CustomerInfo, leadId); // 清除缓存
            // 设置客户的 归属用户 与 归属部门 为空
            LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<LeadInfo>()
                .set(LeadInfo::getAssignedTo, null)
                .set(LeadInfo::getAssignedDept, null)
                .set(LeadInfo::getVersion, leadInfoVo.getVersion() + 1)
                .set(LeadInfo::getUpdateBy, LoginHelper.getUserId())
                .set(LeadInfo::getUpdateTime, new Date())
                .eq(LeadInfo::getId, leadId)
                .eq(LeadInfo::getVersion, leadInfoVo.getVersion());
            Boolean flag = leadInfoMapper.update(null, wrapper) > 0;
            if (!flag) {
                throw new UserException("回收线索信息失败");
            }
            // 回收线索的联系人
            ContactInfoBo contactInfoBo = new ContactInfoBo();
            contactInfoBo.setCustomerId(leadId);
            List<ContactInfoVo> voList = DataPermissionHelper.ignore(() -> contactInfoService.queryList(contactInfoBo)); // !!! 这里忽略数据权限校验，因为是回收操作，有线索权限就有权回收对应联系人
            if (ArrayUtil.isNotEmpty(voList.isEmpty())) {
                voList.forEach(vo -> {
                    CacheUtils.evict(CacheNames.ContactInfo, vo.getId()); // 清除联系人缓存
                    // 设置联系人信息的 归属用户 与 归属部门 为空
                    LambdaUpdateWrapper wrapperContact = new LambdaUpdateWrapper<ContactInfo>()
                        .set(ContactInfo::getAssignedTo, null)
                        .set(ContactInfo::getAssignedDept, null)
                        .set(ContactInfo::getVersion, vo.getVersion() + 1)
                        .set(ContactInfo::getUpdateBy, LoginHelper.getUserId())
                        .set(ContactInfo::getUpdateTime, new Date())
                        .eq(ContactInfo::getId, vo.getId())
                        .eq(ContactInfo::getVersion, vo.getVersion());
                    Boolean contactFlag = contactInfoMapper.update(null, wrapperContact) > 0;
                    if (!contactFlag) {
                        throw new UserException("回收联系人信息失败");
                    }
                });
            }
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean transfer(List<Long> leadIds, Long userId) {
        if (ArrayUtil.isEmpty(leadIds)) {
            throw new UserException("转移的线索不能为空");
        }
        for (Long leadId : leadIds) {
            SysUserVo sysUserVo = sysUserService.selectUserById(userId);
            if (sysUserVo == null) {
                throw new UserException("目标用户不存在");
            }
            // 转移线索
            LeadInfoVo leadInfoVo = leadInfoService.queryById(leadId); // !!! 这里校验数据权限，同时获取版本号
            if (leadInfoVo == null) {
                throw new UserException("线索信息不存在");
            }
            CacheUtils.evict(CacheNames.LeadInfo, leadId); // 清除缓存
            CacheUtils.evict(CacheNames.CustomerInfo, leadId); // 清除缓存
            // 设置线索的 归属用户 与 归属部门
            LeadInfoBo leadInfoBo = new LeadInfoBo();
            leadInfoBo.setId(leadId);
            leadInfoBo.setAssignedTo(userId);
            leadInfoBo.setAssignedDept(sysUserVo.getDeptId());
            leadInfoBo.setVersion(leadInfoVo.getVersion());
            Boolean flag = leadInfoService.updateByBo(leadInfoBo);
            if (!flag) {
                throw new UserException("转移线索信息失败");
            }
            // 转移线索的联系人
            ContactInfoBo contactInfoBo = new ContactInfoBo();
            contactInfoBo.setCustomerId(leadId);
            DataPermissionHelper.ignore(() -> {  // !!! 这里忽略数据权限校验，因为是回收操作，有线索权限就有权回收对应联系人
                List<ContactInfoVo> voList = contactInfoService.queryList(contactInfoBo);
                if (ArrayUtil.isNotEmpty(voList)) {
                    voList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContactInfo, vo.getId()); // 清除联系人缓存
                        // 设置联系人信息的 归属用户 与 归属部门
                        ContactInfoBo contactBo = new ContactInfoBo();
                        contactBo.setId(vo.getId());
                        contactBo.setAssignedTo(userId);
                        contactBo.setAssignedDept(sysUserVo.getDeptId());
                        contactBo.setVersion(vo.getVersion());
                        Boolean contactFlag = contactInfoService.updateByBo(contactBo);
                        if (!contactFlag) {
                            throw new UserException("转移联系人信息失败");
                        }
                    });
                }
            });
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean reclaimUserLead(Long userId) {
        if (userId == null) {
            throw new UserException("用户不能为空");
        }
        LeadInfoBo leadInfoBo = new LeadInfoBo();
        leadInfoBo.setAssignedTo(userId);
        List<LeadInfoVo> leadInfoVoList = leadInfoService.queryList(leadInfoBo);
        if (ArrayUtil.isEmpty(leadInfoVoList)) {
            return true; // 没有线索可回收
        } else {
            List<Long> leadIds = leadInfoVoList.stream().map(LeadInfoVo::getId).toList();
            return reclaimById(leadIds);
        }
    }

    @Override
    @DSTransactional
    public Boolean transferUserLead(Long sourceUserId, Long targetUserId) {
        if (sourceUserId == null) {
            throw new UserException("用户不能为空");
        }
        LeadInfoBo leadInfoBo = new LeadInfoBo();
        leadInfoBo.setAssignedTo(sourceUserId);
        List<LeadInfoVo> leadInfoVoList = leadInfoService.queryList(leadInfoBo);
        if (ArrayUtil.isEmpty(leadInfoVoList)) {
            return true; // 没有线索可回收
        } else {
            List<Long> leadIds = leadInfoVoList.stream().map(LeadInfoVo::getId).toList();
            return transfer(leadIds, targetUserId);
        }
    }

    @Override
    @DSTransactional
    public Boolean claim(Long userId, List<Long> leadIds) {
        Long deptId;
        if (!userId.equals(LoginHelper.getUserId())) { // 如果认领的用户不是当前登录用户，则需要校验目标用户是否存在
            SysUserVo sysUserVo = sysUserService.selectUserById(userId);
            if (sysUserVo == null) {
                throw new UserException("目标用户不存在");
            } else {
                deptId = sysUserVo.getDeptId(); // 获取目标用户的部门ID
            }
        } else {
            deptId = LoginHelper.getDeptId(); // 如果是当前登录用户，则使用当前登录用户的部门ID
        }
        if (ArrayUtil.isEmpty(leadIds)) {
            throw new UserException("认领的线索不能为空");
        }
        for (Long leadId : leadIds) {
            // 认领线索
            DataPermissionHelper.ignore(() -> {
                CacheUtils.evict(CacheNames.LeadInfo, leadId); // 清除缓存
                CacheUtils.evict(CacheNames.CustomerInfo, leadId); // 清除缓存
                LeadInfoVo leadInfoVo = leadInfoService.queryById(leadId);
                if (leadInfoVo == null || leadInfoVo.getAssignedTo() != null) {
                    throw new UserException("线索不存在或已被认领");
                }
                // 设置线索的 归属用户 与 归属部门
                LeadInfoBo leadInfoBo = new LeadInfoBo();
                leadInfoBo.setId(leadId);
                leadInfoBo.setAssignedTo(userId);
                leadInfoBo.setAssignedDept(deptId);
                Boolean flag = leadInfoService.updateByBo(leadInfoBo);
                if (!flag) {
                    throw new UserException("认领线索信息失败");
                }
                // 认领线索的联系人
                ContactInfoBo contactInfoBo = new ContactInfoBo();
                contactInfoBo.setCustomerId(leadId);
                List<ContactInfoVo> voList = contactInfoService.queryList(contactInfoBo);
                if (ArrayUtil.isNotEmpty(voList)) {
                    voList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContactInfo, vo.getId()); // 清除联系人缓存
                        // 设置联系人信息的 归属用户 与 归属部门
                        ContactInfoBo contactBo = new ContactInfoBo();
                        contactBo.setId(vo.getId());
                        contactBo.setAssignedTo(userId);
                        contactBo.setAssignedDept(deptId);
                        Boolean contactFlag = contactInfoService.updateByBo(contactBo);
                        if (!contactFlag) {
                            throw new UserException("认领联系人信息失败");
                        }
                    });
                }
            });
        }
        return true;
    }
}
