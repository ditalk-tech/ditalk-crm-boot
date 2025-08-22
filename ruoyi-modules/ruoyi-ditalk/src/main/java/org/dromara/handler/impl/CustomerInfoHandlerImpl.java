package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ICustomerInfoCommonHandler;
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.customer.domain.CustomerInfo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.mapper.CustomerInfoMapper;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 客户信息应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInfoHandlerImpl implements ICustomerInfoHandler {

    private final ICustomerInfoService customerInfoService;
    private final IContactInfoService contactInfoService;
    private final ISysUserService sysUserService;
    private final CustomerInfoMapper customerInfoMapper;
    private final ICustomerInfoCommonHandler customerInfoCommonHandler;

    @Override
    @DSTransactional
    public Boolean addByBo(CustomerContactBo bo) {
        // 生成主键ID
        Long customerInfoId = IdUtil.getSnowflakeNextId();
        Long contactInfoId = IdUtil.getSnowflakeNextId();
        //
        CustomerInfoBo customerInfoBo = bo.getCustomerInfoBo();
        customerInfoBo.setAssignedTo(LoginHelper.getUserId()); // 设置分配给当前登录用户
        customerInfoBo.setAssignedDept(LoginHelper.getDeptId());
        customerInfoBo.setId(customerInfoId);
        customerInfoBo.setContactId(contactInfoId);
        Boolean f1 = customerInfoService.insertByBo(customerInfoBo);
        //
        ContactInfoBo contactInfoBo = bo.getContactInfoBo();
        contactInfoBo.setId(contactInfoId);
        contactInfoBo.setCustomerId(customerInfoBo.getId());
        Boolean f2 = contactInfoService.insertByBo(bo.getContactInfoBo());
        if (!f1 || !f2) throw new UserException("添加客户信息失败，请检查数据内容");
        return true;
    }

    @Override
    @DSTransactional
    public Boolean editByBo(CustomerContactBo bo) {
        Boolean f1 = customerInfoService.updateByBo(bo.getCustomerInfoBo());
        Boolean f2 = contactInfoService.updateByBo(bo.getContactInfoBo());
        if (!f1 || !f2) throw new UserException("更新客户信息失败，请检查数据内容");
        return true;
    }

    @Override
    @DSTransactional
    public Boolean reclaimById(List<Long> customerIds) {
        if (IterUtil.isEmpty(customerIds)) {
            throw new UserException("回收的客户不能为空");
        }
        for (Long customerId : customerIds) {
            // 回收客户到公海
            CustomerInfoVo customerInfoVo = customerInfoService.queryByIdNoCache(customerId); // !!! 这里校验数据权限，同时获取版本号
            if (customerInfoVo == null || customerInfoVo.getAssignedTo() == null) {
                throw new UserException("客户不存在或已在客户公海中");
            }
            CacheUtils.evict(CacheNames.CustomerInfo, customerId); // 清除缓存
            CacheUtils.evict(CacheNames.LeadInfo, customerId); // 清除缓存
            // 设置客户的 归属用户 与 归属部门 为空
            LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<CustomerInfo>()
                .set(CustomerInfo::getAssignedTo, null)
                .set(CustomerInfo::getAssignedDept, null)
                .set(CustomerInfo::getVersion, customerInfoVo.getVersion() + 1)
                .set(CustomerInfo::getUpdateBy, LoginHelper.getUserId())
                .set(CustomerInfo::getUpdateTime, new Date())
                .eq(CustomerInfo::getId, customerId)
                .eq(CustomerInfo::getVersion, customerInfoVo.getVersion());
            Boolean updateFlag = customerInfoMapper.update(null, wrapper) > 0;
            if (!updateFlag) {
                throw new UserException("回收客户信息失败");
            }
            // 回收客户相关资源
            customerInfoCommonHandler.reclaimById(customerId);
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean transfer(List<Long> customerIds, Long userId) {
        if (IterUtil.isEmpty(customerIds)) {
            throw new UserException("转移的客户不能为空");
        }
        for (Long customerId : customerIds) {
            SysUserVo sysUserVo = sysUserService.selectUserById(userId);
            if (sysUserVo == null) {
                throw new UserException("目标用户不存在");
            }
            // 转移客户
            CustomerInfoVo customerInfoVo = customerInfoService.queryByIdNoCache(customerId); // !!! 这里校验数据权限，同时获取版本号
            if (customerInfoVo == null) {
                throw new UserException("客户信息不存在");
            }
            CacheUtils.evict(CacheNames.CustomerInfo, customerId); // 清除缓存
            CacheUtils.evict(CacheNames.LeadInfo, customerId); // 清除缓存
            // 设置客户的 归属用户 与 归属部门
            CustomerInfoBo customerInfoBo = new CustomerInfoBo();
            customerInfoBo.setId(customerId);
            customerInfoBo.setAssignedTo(userId);
            customerInfoBo.setAssignedDept(sysUserVo.getDeptId());
            customerInfoBo.setVersion(customerInfoVo.getVersion());
            Boolean updateFlag = customerInfoService.updateByBo(customerInfoBo);
            if (!updateFlag) {
                throw new UserException("转移客户信息失败");
            }
            // 转移客户相关资源到指定用户
            customerInfoCommonHandler.transfer(customerId, userId, sysUserVo.getDeptId());
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean reclaimUserCustomer(Long userId) {
        if (userId == null) {
            throw new UserException("用户不能为空");
        }
        CustomerInfoBo customerInfoBo = new CustomerInfoBo();
        customerInfoBo.setAssignedTo(userId);
        List<CustomerInfoVo> customerInfoVoList = customerInfoService.queryList(customerInfoBo);
        if (IterUtil.isEmpty(customerInfoVoList)) {
            return true; // 没有客户可回收
        } else {
            List<Long> customerIds = customerInfoVoList.stream().map(CustomerInfoVo::getId).toList();
            return reclaimById(customerIds);
        }
    }

    @Override
    @DSTransactional
    public Boolean transferUserCustomer(Long sourceUserId, Long targetUserId) {
        if (sourceUserId == null) {
            throw new UserException("用户不能为空");
        }
        CustomerInfoBo customerInfoBo = new CustomerInfoBo();
        customerInfoBo.setAssignedTo(sourceUserId);
        List<CustomerInfoVo> customerInfoVoList = customerInfoService.queryList(customerInfoBo);
        if (IterUtil.isEmpty(customerInfoVoList)) {
            return true; // 没有客户可回收
        } else {
            List<Long> customerIds = customerInfoVoList.stream().map(CustomerInfoVo::getId).toList();
            return transfer(customerIds, targetUserId);
        }
    }

    @Override
    @DSTransactional
    public Boolean claim(Long userId, List<Long> customerIds) {
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
        if (IterUtil.isEmpty(customerIds)) {
            throw new UserException("认领的客户不能为空");
        }
        for (Long customerId : customerIds) {
            // 认领客户
            DataPermissionHelper.ignore(() -> {
                CacheUtils.evict(CacheNames.CustomerInfo, customerId); // 清除缓存
                CacheUtils.evict(CacheNames.LeadInfo, customerId); // 清除缓存
                CustomerInfoVo customerInfoVo = customerInfoService.queryById(customerId);
                if (customerInfoVo == null || customerInfoVo.getAssignedTo() != null) {
                    throw new UserException("客户不存在或已被认领");
                }
                // 设置客户的 归属用户 与 归属部门
                CustomerInfoBo customerInfoBo = new CustomerInfoBo();
                customerInfoBo.setId(customerId);
                customerInfoBo.setAssignedTo(userId);
                customerInfoBo.setAssignedDept(deptId);
                Boolean updateFlag = customerInfoService.updateByBo(customerInfoBo);
                if (!updateFlag) {
                    throw new UserException("认领客户信息失败");
                }
                // 认领客户相关资源
                customerInfoCommonHandler.claim(customerId, userId, deptId);
            });
        }
        return true;
    }
}
