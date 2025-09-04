package org.dromara.handler;

import org.dromara.module.opportunity.domain.bo.OpportunityQuotationBo;

/**
 * 商机报价单应用接口
 *
 * @author weidixian
 */
public interface IOpportunityQuotationHandler {

    /**
     * 新增商机报价单<br>
     * 其中 quoted 字段为 Y 时，其他同 opportunityId 的报价单都会被标记为 quoted = N
     *
     * @param bo 商机报价单
     * @return 结果
     */
    Boolean add(OpportunityQuotationBo bo);

    /**
     * 修改商机报价单<br>
     * 其中 quoted 字段为 Y 时，其他同 opportunityId 的报价单都会被标记为 quoted = N
     *
     * @param bo 商机报价单
     * @return 结果
     */
    Boolean edit(OpportunityQuotationBo bo);
}
