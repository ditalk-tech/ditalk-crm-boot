package org.dromara.handler;

import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;

/**
 * 商机应用接口
 *
 * @author weidixian
 */
public interface IOpportunityInfoHandler {

    Boolean add(OpportunityInfoBo bo);

    Boolean edit(OpportunityInfoBo bo);
}
