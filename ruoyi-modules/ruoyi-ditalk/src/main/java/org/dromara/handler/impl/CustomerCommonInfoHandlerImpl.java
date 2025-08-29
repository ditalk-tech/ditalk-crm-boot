package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ICustomerInfoCommonHandler;
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
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.mapper.CustomerInfoMapper;
import org.dromara.module.opportunity.domain.OpportunityInfo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.mapper.OpportunityInfoMapper;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 客户/线索 公共应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerCommonInfoHandlerImpl implements ICustomerInfoCommonHandler {

    private final IContactInfoService contactInfoService;
    private final IOpportunityInfoService opportunityInfoService;
    private final IContractInfoService contractInfoService;
    private final ContactInfoMapper contactInfoMapper;
    private final OpportunityInfoMapper opportunityInfoMapper;
    private final ContractInfoMapper contractInfoMapper;
    private final CustomerInfoMapper customerInfoMapper;

    @Override
    @DSTransactional
    public void reclaimById(Long customerId) {
        // 回收客户的联系人
        ContactInfoBo contactInfoBo = new ContactInfoBo();
        contactInfoBo.setCustomerId(customerId);
        List<ContactInfoVo> voList = DataPermissionHelper.ignore(() -> contactInfoService.queryList(contactInfoBo));
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
        List<OpportunityInfoVo> opportunityInfoVoList = DataPermissionHelper.ignore(() -> opportunityInfoService.queryList(opportunityInfoBo));
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
        List<ContractInfoVo> contractInfoVoList = DataPermissionHelper.ignore(() -> contractInfoService.queryList(contractInfoBo));
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

    @Override
    @DSTransactional
    public void transfer(Long customerId, Long userId, Long deptId) {
        DataPermissionHelper.ignore(() -> {
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
                    infoBo.setAssignedDept(deptId);
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
                    infoBo.setAssignedDept(deptId);
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
                    infoBo.setAssignedDept(deptId);
                    infoBo.setVersion(vo.getVersion());
                    Boolean flag = contractInfoService.updateByBo(infoBo);
                    if (!flag) {
                        throw new UserException("转移合同信息失败");
                    }
                });
            }
        });
    }

    @Override
    @DSTransactional
    public void claim(Long customerId, Long userId, Long deptId) {
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
    }

    @Override
    public CustomerInfoVo queryAllByIdNoCache(Long id) {
        return customerInfoMapper.selectVoById(id);
    }
}
