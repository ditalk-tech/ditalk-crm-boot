package org.dromara.handler.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.contact.domain.ContactInfo;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.contact.mapper.ContactInfoMapper;
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
    private final ContactInfoMapper contactInfoMapper;

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
    public Boolean reclaimById(Long customerId) {
        // 回收客户到公海
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(customerId); // !!! 这里校验数据权限，同时获取版本号
        if (customerInfoVo == null || customerInfoVo.getAssignedTo() == null) {
            throw new UserException("客户不存在或已是公海客户");
        }
        // 设置客户的 归属用户 与 归属部门 为空
        LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<CustomerInfo>()
            .set(CustomerInfo::getAssignedTo, null)
            .set(CustomerInfo::getAssignedDept, null)
            .set(CustomerInfo::getVersion, customerInfoVo.getVersion() + 1)
            .set(CustomerInfo::getUpdateBy, LoginHelper.getUserId())
            .set(CustomerInfo::getUpdateTime, new Date())
            .eq(CustomerInfo::getId, customerId)
            .eq(CustomerInfo::getVersion, customerInfoVo.getVersion());
        Boolean flag = customerInfoMapper.update(null, wrapper) > 0;
        if (!flag) {
            throw new UserException("回收客户信息失败");
        }
        // 回收客户的联系人
        ContactInfoBo contactInfoBo = new ContactInfoBo();
        contactInfoBo.setCustomerId(customerId);
        List<ContactInfoVo> voList = DataPermissionHelper.ignore(() -> contactInfoService.queryList(contactInfoBo)); // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
        if (ArrayUtil.isNotEmpty(voList.isEmpty())) {
            voList.forEach(vo -> {
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
        return true;
    }

    @Override
    @DSTransactional
    public Boolean transfer(Long customerId, Long userId) {
        SysUserVo sysUserVo = sysUserService.selectUserById(userId);
        if (sysUserVo == null) {
            throw new UserException("目标用户不存在");
        }
        // 转移客户
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(customerId); // !!! 这里校验数据权限，同时获取版本号
        if (customerInfoVo == null) {
            throw new UserException("客户信息不存在");
        }
        // 设置客户的 归属用户 与 归属部门
        CustomerInfoBo customerInfoBo = new CustomerInfoBo();
        customerInfoBo.setId(customerId);
        customerInfoBo.setAssignedTo(userId);
        customerInfoBo.setAssignedDept(sysUserVo.getDeptId());
        customerInfoBo.setVersion(customerInfoVo.getVersion());
        Boolean flag = customerInfoService.updateByBo(customerInfoBo);
        if (!flag) {
            throw new UserException("转移客户信息失败");
        }
        // 转移客户的联系人
        ContactInfoBo contactInfoBo = new ContactInfoBo();
        contactInfoBo.setCustomerId(customerId);
        DataPermissionHelper.ignore(() -> {  // !!! 这里忽略数据权限校验，因为是回收操作，有客户权限就有权回收对应联系人
            List<ContactInfoVo> voList = contactInfoService.queryList(contactInfoBo);
            if (ArrayUtil.isNotEmpty(voList)) {
                voList.forEach(vo -> {
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
        return true;
    }

}
