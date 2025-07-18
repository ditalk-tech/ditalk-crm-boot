package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户活动记录类型 ditalk_customer_activity_type
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum CustomerActivityTypeEnum {

    IM("IM", "即时通讯"),
    TASK("TASK", "任务"),
    NOTE("NOTE", "笔记"),
    MEETING("MEETING", "会议"),
    EMAIL("EMAIL", "邮件"),
    CALL("CALL", "电话"),
    ;

    private final String code;
    private final String desc;

}
