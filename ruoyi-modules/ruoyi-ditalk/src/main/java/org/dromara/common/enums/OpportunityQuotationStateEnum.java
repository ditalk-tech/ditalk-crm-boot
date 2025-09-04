package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定价单状态 ditalk_opportunity_quotation_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum OpportunityQuotationStateEnum {

    WAITING("WAITING", "待审批"),
    APPROVED("APPROVED", "已审批"),
    SENT("SENT", "已发送"),
    ACCEPTED("ACCEPTED", "已接受"),
    REJECTED("REJECTED", "已拒绝"),
    EXPIRED("EXPIRED", "已过期"),
    CONVERTED("CONVERTED", "已转合同"),
    CANCELED("CANCELED", "已取消"),
    ;

    private final String code;
    private final String desc;

}
