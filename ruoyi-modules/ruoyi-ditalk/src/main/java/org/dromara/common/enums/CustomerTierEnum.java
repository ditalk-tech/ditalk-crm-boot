package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户级别 ditalk_customer_tier
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum CustomerTierEnum {

    Lost("Lost","流失客户"),
    Prospect("Prospect","潜在客户"),
    LowEngagement("LowEngagement","低活跃客户"),
    Normal("Normal","普通客户"),
    Important("Important","重要客户"),
    Strategic("Strategic","重点客户"),
    ;

    private final String code;
    private final String desc;

}
