package org.dromara.handler.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.handler.IContactInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

/**
 * 系统用户应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactInfoHandlerImpl implements IContactInfoHandler {

    private final ICustomerInfoService customerInfoService;
    private final IContactInfoService contactInfoService;

    @Override
    @DSTransactional
    public Boolean add(ContactInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("客户信息不存在");
        }
        bo.setAssignedTo(customerInfoVo.getAssignedTo());
        bo.setAssignedDept(customerInfoVo.getAssignedDept());
        return contactInfoService.insertByBo(bo);
    }
}
