package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户状态枚举 ditalk_customer_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum CustomerStateEnum {

    INACTIVE("INACTIVE", "无效"),
    ACTIVE("ACTIVE", "有效"),
    ;

    private final String code;
    private final String desc;

}
