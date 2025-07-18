package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 联系人状态枚举 ditalk_contact_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum ContactStateEnum {

    INACTIVE("INACTIVE", "无效"),
    ACTIVE("ACTIVE", "有效"),
    ;

    private final String code;
    private final String desc;

}
