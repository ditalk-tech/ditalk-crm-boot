package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知状态 ditalk_common_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum CommonStateEnum {

    ACTIVE("ACTIVE", "有效"),
    INACTIVE("INACTIVE", "无效"),
    ;

    private final String code;
    private final String desc;

}
