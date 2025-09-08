package org.dromara.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.enums.OpportunityStateEnum;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ICustomerInfoCommonHandler;
import org.dromara.handler.IOpportunityInfoHandler;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.service.impl.OpportunityInfoServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商机应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityInfoHandlerImpl implements IOpportunityInfoHandler {

    private final ICustomerInfoCommonHandler customerInfoCommonHandler;
    private final OpportunityInfoServiceImpl opportunityInfoService;

    @Override
    public Boolean add(OpportunityInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoCommonHandler.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("新增操作失败，客户不存在");
        }
        OpportunityInfoBo add = new OpportunityInfoBo();
        add.setTitle(bo.getTitle());
        add.setCustomerId(bo.getCustomerId());
        add.setAmount(bo.getAmount());
        add.setRemark(bo.getRemark());
        add.setCloseDate(bo.getCloseDate());
        add.setAssignedDept(LoginHelper.getDeptId());
        add.setAssignedTo(LoginHelper.getUserId());
        add.setState(OpportunityStateEnum.QUALIFICATION.getCode());
        return opportunityInfoService.insertByBo(add);
    }

    @Override
    public Boolean edit(OpportunityInfoBo bo) {
        OpportunityInfoBo update = new OpportunityInfoBo();
        update.setId(bo.getId());
        update.setVersion(bo.getVersion());
        update.setTitle(bo.getTitle());
        update.setAmount(bo.getAmount());
        update.setRemark(bo.getRemark());
        update.setCloseDate(bo.getCloseDate());
        update.setState(bo.getState());
        return opportunityInfoService.updateByBo(update);
    }
}
