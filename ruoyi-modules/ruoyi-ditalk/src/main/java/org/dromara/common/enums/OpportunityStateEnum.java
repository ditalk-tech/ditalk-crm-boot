package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商机状态 ditalk_opportunity_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum OpportunityStateEnum {

    QUALIFICATION("QUALIFICATION", "潜在机会"),
    PROPOSAL("PROPOSAL", "方案报价"),
    NEGOTIATION("NEGOTIATION", "谈判协商"),
    CLOSED_WON("CLOSED_WON", "成功关闭"),
    CLOSED_LOST("CLOSED_LOST", "失败关闭"),
    ;

    private final String code;
    private final String desc;

}
