package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户类型 ditalk_customer_type
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {

    INDIVIDUAL("INDIVIDUAL", "个人"),
    COMPANY("COMPANY", "企业"),
    ;

    private final String code;
    private final String desc;

}
