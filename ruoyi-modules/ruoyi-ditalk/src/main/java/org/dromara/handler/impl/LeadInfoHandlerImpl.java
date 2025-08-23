package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
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
import org.dromara.handler.ICustomerInfoCommonHandler;
import org.dromara.handler.ICustomerTransferLogHandler;
import org.dromara.handler.ILeadInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
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
    private final ICustomerInfoCommonHandler customerInfoCommonHandler;
    private final ICustomerTransferLogHandler customerTransferLogHandler;

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
        if (IterUtil.isEmpty(leadIds)) {
            throw new UserException("回收的线索不能为空");
        }
        for (Long leadId : leadIds) {
            // 回收客户到公海
            LeadInfoVo leadInfoVo = leadInfoService.queryByIdNoCache(leadId); // !!! 这里校验数据权限，同时获取版本号
            if (leadInfoVo == null || leadInfoVo.getAssignedTo() == null) {
                throw new UserException("线索不存在或已在线索池中");
            }
            // 记录客户转移日志
            customerTransferLogHandler.add(leadId, null, null);
            // 设置客户的 归属用户 与 归属部门 为空
            LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<LeadInfo>()
                .set(LeadInfo::getAssignedTo, null)
                .set(LeadInfo::getAssignedDept, null)
                .set(LeadInfo::getVersion, leadInfoVo.getVersion() + 1)
                .set(LeadInfo::getUpdateBy, LoginHelper.getUserId())
                .set(LeadInfo::getUpdateTime, new Date())
                .eq(LeadInfo::getId, leadId)
                .eq(LeadInfo::getVersion, leadInfoVo.getVersion());
            Boolean updateFlag = leadInfoMapper.update(null, wrapper) > 0;
            if (!updateFlag) {
                throw new UserException("回收线索信息失败");
            }
            // 同时回收所在相关数据
            customerInfoCommonHandler.reclaimById(leadId);
            // 清除缓存
            CacheUtils.evict(CacheNames.LeadInfo, leadId);
            CacheUtils.evict(CacheNames.CustomerInfo, leadId);
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean transfer(List<Long> leadIds, Long userId) {
        if (IterUtil.isEmpty(leadIds)) {
            throw new UserException("转移的线索不能为空");
        }
        for (Long leadId : leadIds) {
            SysUserVo sysUserVo = sysUserService.selectUserById(userId);
            if (sysUserVo == null) {
                throw new UserException("目标用户不存在");
            }
            // 转移线索
            LeadInfoVo leadInfoVo = leadInfoService.queryByIdNoCache(leadId); // !!! 这里校验数据权限，同时获取版本号
            if (leadInfoVo == null) {
                throw new UserException("线索信息不存在");
            }
            // 记录客户转移日志
            customerTransferLogHandler.add(leadId, userId, sysUserVo.getDeptId());
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
            // 转移客户相关资源到指定用户
            customerInfoCommonHandler.transfer(leadId, userId, sysUserVo.getDeptId());
            // 清除缓存
            CacheUtils.evict(CacheNames.LeadInfo, leadId);
            CacheUtils.evict(CacheNames.CustomerInfo, leadId);
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
        if (IterUtil.isEmpty(leadInfoVoList)) {
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
        if (IterUtil.isEmpty(leadInfoVoList)) {
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
        if (IterUtil.isEmpty(leadIds)) {
            throw new UserException("认领的线索不能为空");
        }
        for (Long leadId : leadIds) {
            // 记录客户转移日志
            customerTransferLogHandler.add(leadId, userId, deptId);
            // 认领线索
            DataPermissionHelper.ignore(() -> {
                LeadInfoVo leadInfoVo = leadInfoService.queryById(leadId);
                if (leadInfoVo == null || leadInfoVo.getAssignedTo() != null) {
                    throw new UserException("线索不存在或已被认领");
                }
                // 设置线索的 归属用户 与 归属部门
                LeadInfoBo leadInfoBo = new LeadInfoBo();
                leadInfoBo.setId(leadId);
                leadInfoBo.setAssignedTo(userId);
                leadInfoBo.setAssignedDept(deptId);
                Boolean updateFlag = leadInfoService.updateByBo(leadInfoBo);
                if (!updateFlag) {
                    throw new UserException("认领线索信息失败");
                }
                // 认领客户相关资源
                customerInfoCommonHandler.claim(leadId, userId, deptId);
            });
            // 清除缓存
            CacheUtils.evict(CacheNames.LeadInfo, leadId);
            CacheUtils.evict(CacheNames.CustomerInfo, leadId);
        }
        return true;
    }
}
