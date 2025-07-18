package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 合同状态 ditalk_contract_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum ContractStateEnum {

    TERMINATED("TERMINATED", "结束"),
    EXPIRED("EXPIRED", "过期"),
    ACTIVE("ACTIVE", "有效"),
    DRAFT("DRAFT", "草稿"),
    ;

    private final String code;
    private final String desc;

}
