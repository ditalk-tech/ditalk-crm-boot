package org.dromara.module.opportunity.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 商机信息视图对象 opportunity_info
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Data
public class OpportunityInfoOptionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 商机标题
     */
    private String title;

    /**
     * 商机阶段
     */
    private String state;

}
