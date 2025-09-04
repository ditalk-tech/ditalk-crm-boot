package org.dromara.handler.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.lock.annotation.Lock4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CommonConstants;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.enums.OpportunityQuotationStateEnum;
import org.dromara.common.enums.WorkflowBusinessStatusEnum;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.utils.CodeGeneratorUtil;
import org.dromara.handler.IOpportunityQuotationHandler;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationBo;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;
import org.dromara.module.opportunity.service.IOpportunityQuotationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商机报价单应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityQuotationHandlerImpl implements IOpportunityQuotationHandler {

    private final IOpportunityQuotationService opportunityQuotationService;

    /**
     * 设置商机报价单为未引用
     * @param opportunityId
     * @return
     */
    @DSTransactional
    private Boolean setUnQuoted(Long opportunityId) {
        OpportunityQuotationBo query = new OpportunityQuotationBo();
        query.setOpportunityId(opportunityId);
        query.setQuoted(CommonConstants.YES);
        List<OpportunityQuotationVo> quotationVoList = opportunityQuotationService.queryList(query);
        if (quotationVoList.size() > 0) {
            quotationVoList.forEach(quotationVo -> {
                if (!quotationVo.getId().equals(opportunityId)) {
                    OpportunityQuotationBo update = new OpportunityQuotationBo();
                    update.setId(quotationVo.getId());
                    update.setVersion(quotationVo.getVersion());
                    update.setQuoted(CommonConstants.NO);
                    Boolean flag = opportunityQuotationService.updateByBo(update);
                    if (!flag) {
                        throw new UserException("设置商机报价单为未引用失败");
                    }
                }
            });
        }
        return true;
    }

    @Override
    @DSTransactional
    @Lock4j(name="OpportunityQuotation", keys = {"#bo.opportunityId"}, expire = 5000L)
    public Boolean add(OpportunityQuotationBo bo) {
        if (bo.getQuoted().equals(CommonConstants.YES)) {
            this.setUnQuoted(bo.getOpportunityId());
        }
        Long id = IdUtil.getSnowflakeNextId();
        bo.setId(id);
        bo.setCode(CodeGeneratorUtil.businessCode(id));
        bo.setTotalSalePrice(0L);
        bo.setTotalOriginalPrice(0L);
        bo.setTotalCostPrice(0L);
        bo.setAssignedTo(LoginHelper.getUserId());
        bo.setAssignedDept(LoginHelper.getDeptId());
        bo.setApprovalState(WorkflowBusinessStatusEnum.draft.getCode());
        bo.setQuotationState(OpportunityQuotationStateEnum.WAITING.getCode());
        return opportunityQuotationService.insertByBo(bo);
    }

    @Override
    @DSTransactional
    @Lock4j(name="OpportunityQuotation", keys = {"#bo.opportunityId"}, expire = 5000L)
    public Boolean edit(OpportunityQuotationBo bo) {
        if (bo.getQuoted().equals(CommonConstants.YES)) {
            this.setUnQuoted(bo.getOpportunityId());
        }
        OpportunityQuotationBo update = new OpportunityQuotationBo();
        update.setId(bo.getId());
        update.setVersion(bo.getVersion());
        update.setValidUntil(bo.getValidUntil());
        update.setRemark(bo.getRemark());
        update.setQuoted(bo.getQuoted());
        return opportunityQuotationService.updateByBo(update);
    }
}
