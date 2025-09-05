package org.dromara.handler;

import org.dromara.module.opportunity.domain.bo.OpportunityQuotationItemBo;

/**
 * 报价单明细应用接口
 *
 * @author weidixian
 */
public interface IOpportunityQuotationItemHandler {

    Boolean add(OpportunityQuotationItemBo bo);

    Boolean edit(OpportunityQuotationItemBo bo);
}
