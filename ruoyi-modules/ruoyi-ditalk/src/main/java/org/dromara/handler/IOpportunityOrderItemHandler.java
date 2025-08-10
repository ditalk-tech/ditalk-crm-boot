package org.dromara.handler;

import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemTinyBo;

/**
 * 客户线索信息应用接口
 *
 * @author weidixian
 */
public interface IOpportunityOrderItemHandler {

    Boolean add(OpportunityOrderItemTinyBo bo);

    Boolean edit(OpportunityOrderItemTinyBo bo);
}
