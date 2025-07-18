package org.dromara.handler.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.app.domain.bo.LeadContactBo;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.handler.ILeadInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.service.ILeadInfoService;
import org.springframework.stereotype.Service;

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

    @Override
    @DSTransactional
    public Boolean addByBo(LeadContactBo bo) {
        Long leadInfoId = IdUtil.getSnowflakeNextId();
        Long contactInfoId = IdUtil.getSnowflakeNextId();
        //
        LeadInfoBo leadInfoBo = bo.getLeadInfoBo();
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

}
