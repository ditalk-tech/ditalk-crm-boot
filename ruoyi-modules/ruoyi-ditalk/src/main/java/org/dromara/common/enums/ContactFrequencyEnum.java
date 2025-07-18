package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 联络频率状态 ditalk_contact_frequency
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum ContactFrequencyEnum {

    YEARLY("yearly", "每年"),
    QUARTERLY("quarterly", "每季"),
    MONTHLY("monthly", "每月"),
    WEEKLY("weekly", "每周"),
    DAILY("daily", "每天"),
    ALWAYS("always", "随时"),
    ;

    private final String code;
    private final String desc;

}
