package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工作流业务状态 wf_business_status
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum WorkflowBusinessStatusEnum {

    cancel("cancel", "已撤销"),
    draft("draft", "草稿"),
    waiting("waiting", "待审核"),
    finish("finish", "已完成"),
    invalid("invalid", "已作废"),
    back("back", "已退回"),
    termination("termination", "已终止"),
    ;

    private final String code;
    private final String desc;

}
