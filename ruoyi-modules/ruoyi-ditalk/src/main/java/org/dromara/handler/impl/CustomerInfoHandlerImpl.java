package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.ArrayUtil;
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
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.contact.domain.ContactInfo;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.contact.mapper.ContactInfoMapper;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.contract.domain.ContractInfo;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.mapper.ContractInfoMapper;
import org.dromara.module.contract.service.IContractInfoService;
import org.dromara.module.customer.domain.CustomerInfo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.mapper.CustomerInfoMapper;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.module.opportunity.domain.OpportunityInfo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.mapper.OpportunityInfoMapper;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
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
    private final IOpportunityInfoService opportunityInfoService;
    private final IContractInfoService contractInfoService;
    private final ISysUserService sysUserService;
    private final CustomerInfoMapper customerInfoMapper;
    private final ContactInfoMapper contactInfoMapper;
    private final OpportunityInfoMapper opportunityInfoMapper;
    private final ContractInfoMapper contractInfoMapper;

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
            // 回收客户的联系人
            ContactInfoBo contactInfoBo = new ContactInfoBo();
            contactInfoBo.setCustomerId(customerId);
            List<ContactInfoVo> voList = DataPermissionHelper.ignore(() -> contactInfoService.queryList(contactInfoBo)); // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
            if (IterUtil.isNotEmpty(voList)) {
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
                    Boolean flag = contactInfoMapper.update(null, wrapperContact) > 0;
                    if (!flag) {
                        throw new UserException("回收联系人信息失败");
                    }
                });
            }
            // 回收客户的商机
            OpportunityInfoBo opportunityInfoBo = new OpportunityInfoBo();
            opportunityInfoBo.setCustomerId(customerId);
            List<OpportunityInfoVo> opportunityInfoVoList = DataPermissionHelper.ignore(() -> opportunityInfoService.queryList(opportunityInfoBo)); // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
            if (IterUtil.isNotEmpty(opportunityInfoVoList)) {
                opportunityInfoVoList.forEach(vo -> {
                    CacheUtils.evict(CacheNames.OpportunityInfo, vo.getId()); // 清除商机缓存
                    // 设置商机信息的 归属用户 与 归属部门 为空
                    LambdaUpdateWrapper wrapperContact = new LambdaUpdateWrapper<OpportunityInfo>()
                        .set(OpportunityInfo::getAssignedTo, null)
                        .set(OpportunityInfo::getAssignedDept, null)
                        .set(OpportunityInfo::getVersion, vo.getVersion() + 1)
                        .set(OpportunityInfo::getUpdateBy, LoginHelper.getUserId())
                        .set(OpportunityInfo::getUpdateTime, new Date())
                        .eq(OpportunityInfo::getId, vo.getId())
                        .eq(OpportunityInfo::getVersion, vo.getVersion());
                    Boolean flag = opportunityInfoMapper.update(null, wrapperContact) > 0;
                    if (!flag) {
                        throw new UserException("回收商机信息失败");
                    }
                });
            }
            // 回收客户的合同
            ContractInfoBo contractInfoBo = new ContractInfoBo();
            contractInfoBo.setCustomerId(customerId);
            List<ContractInfoVo> contractInfoVoList = DataPermissionHelper.ignore(() -> contractInfoService.queryList(contractInfoBo)); // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
            if (IterUtil.isNotEmpty(contractInfoVoList)) {
                contractInfoVoList.forEach(vo -> {
                    CacheUtils.evict(CacheNames.ContractInfo, vo.getId()); // 清除合同缓存
                    // 设置合同信息的 归属用户 与 归属部门 为空
                    LambdaUpdateWrapper wrapperContact = new LambdaUpdateWrapper<ContractInfo>()
                        .set(ContractInfo::getAssignedTo, null)
                        .set(ContractInfo::getAssignedDept, null)
                        .set(ContractInfo::getVersion, vo.getVersion() + 1)
                        .set(ContractInfo::getUpdateBy, LoginHelper.getUserId())
                        .set(ContractInfo::getUpdateTime, new Date())
                        .eq(ContractInfo::getId, vo.getId())
                        .eq(ContractInfo::getVersion, vo.getVersion());
                    Boolean flag = contractInfoMapper.update(null, wrapperContact) > 0;
                    if (!flag) {
                        throw new UserException("回收合同信息失败");
                    }
                });
            }
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
            DataPermissionHelper.ignore(() -> {  // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
                // 转移客户的联系人
                ContactInfoBo contactInfoBo = new ContactInfoBo();
                contactInfoBo.setCustomerId(customerId);
                List<ContactInfoVo> voList = contactInfoService.queryList(contactInfoBo);
                if (IterUtil.isNotEmpty(voList)) {
                    voList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContactInfo, vo.getId()); // 清除联系人缓存
                        // 设置联系人信息的 归属用户 与 归属部门
                        ContactInfoBo infoBo = new ContactInfoBo();
                        infoBo.setId(vo.getId());
                        infoBo.setAssignedTo(userId);
                        infoBo.setAssignedDept(sysUserVo.getDeptId());
                        infoBo.setVersion(vo.getVersion());
                        Boolean flag = contactInfoService.updateByBo(infoBo);
                        if (!flag) {
                            throw new UserException("转移联系人信息失败");
                        }
                    });
                }
                // 转移客户的商机
                OpportunityInfoBo opportunityInfoBo = new OpportunityInfoBo();
                opportunityInfoBo.setCustomerId(customerId);
                List<OpportunityInfoVo> opportunityInfoVoList = opportunityInfoService.queryList(opportunityInfoBo);
                if (IterUtil.isNotEmpty(opportunityInfoVoList)) {
                    opportunityInfoVoList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.OpportunityInfo, vo.getId()); // 清除商机缓存
                        // 设置商机信息的 归属用户 与 归属部门
                        OpportunityInfoBo infoBo = new OpportunityInfoBo();
                        infoBo.setId(vo.getId());
                        infoBo.setAssignedTo(userId);
                        infoBo.setAssignedDept(sysUserVo.getDeptId());
                        infoBo.setVersion(vo.getVersion());
                        Boolean flag = opportunityInfoService.updateByBo(infoBo);
                        if (!flag) {
                            throw new UserException("转移商机信息失败");
                        }
                    });
                }
                // 转移客户的合同
                ContractInfoBo contractInfoBo = new ContractInfoBo();
                contractInfoBo.setCustomerId(customerId);
                List<ContractInfoVo> contractInfoVoList = contractInfoService.queryList(contractInfoBo);
                if (IterUtil.isNotEmpty(contractInfoVoList)) {
                    contractInfoVoList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContractInfo, vo.getId()); // 清除合同缓存
                        // 设置合同信息的 归属用户 与 归属部门
                        ContractInfoBo infoBo = new ContractInfoBo();
                        infoBo.setId(vo.getId());
                        infoBo.setAssignedTo(userId);
                        infoBo.setAssignedDept(sysUserVo.getDeptId());
                        infoBo.setVersion(vo.getVersion());
                        Boolean flag = contractInfoService.updateByBo(infoBo);
                        if (!flag) {
                            throw new UserException("转移合同信息失败");
                        }
                    });
                }
            });
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
                // 认领客户的联系人
                ContactInfoBo contactInfoBo = new ContactInfoBo();
                contactInfoBo.setCustomerId(customerId);
                List<ContactInfoVo> contactInfoVoList = contactInfoService.queryList(contactInfoBo);
                if (IterUtil.isNotEmpty(contactInfoVoList)) {
                    contactInfoVoList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContactInfo, vo.getId()); // 清除联系人缓存
                        // 设置联系人信息的 归属用户 与 归属部门
                        ContactInfoBo contactBo = new ContactInfoBo();
                        contactBo.setId(vo.getId());
                        contactBo.setAssignedTo(userId);
                        contactBo.setAssignedDept(deptId);
                        Boolean flag = contactInfoService.updateByBo(contactBo);
                        if (!flag) {
                            throw new UserException("认领联系人信息失败");
                        }
                    });
                }
                // 认领商机
                OpportunityInfoBo opportunityInfoBo = new OpportunityInfoBo();
                opportunityInfoBo.setCustomerId(customerId);
                List<OpportunityInfoVo> opportunityInfoVoList = opportunityInfoService.queryList(opportunityInfoBo);
                if (IterUtil.isNotEmpty(opportunityInfoVoList)) {
                    opportunityInfoVoList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.OpportunityInfo, vo.getId()); // 清除商机缓存
                        // 设置商机信息的 归属用户 与 归属部门
                        OpportunityInfoBo opportunityBo = new OpportunityInfoBo();
                        opportunityBo.setId(vo.getId());
                        opportunityBo.setAssignedTo(userId);
                        opportunityBo.setAssignedDept(deptId);
                        Boolean flag = opportunityInfoService.updateByBo(opportunityBo);
                        if (!flag) {
                            throw new UserException("认领商机信息失败");
                        }
                    });
                }
                // 认领合同
                ContractInfoBo contractInfoBo = new ContractInfoBo();
                contractInfoBo.setCustomerId(customerId);
                List<ContractInfoVo> contractInfoVoList = contractInfoService.queryList(contractInfoBo);
                if (IterUtil.isNotEmpty(contractInfoVoList)) {
                    contractInfoVoList.forEach(vo -> {
                        CacheUtils.evict(CacheNames.ContractInfo, vo.getId()); // 清除合同缓存
                        // 设置合同信息的 归属用户 与 归属部门
                        ContractInfoBo contractBo = new ContractInfoBo();
                        contractBo.setId(vo.getId());
                        contractBo.setAssignedTo(userId);
                        contractBo.setAssignedDept(deptId);
                        Boolean flag = contractInfoService.updateByBo(contractBo);
                        if (!flag) {
                            throw new UserException("认领合同信息失败");
                        }
                    });
                }
            });
        }
        return true;
    }
}
