package org.dromara.handler.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

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

}
