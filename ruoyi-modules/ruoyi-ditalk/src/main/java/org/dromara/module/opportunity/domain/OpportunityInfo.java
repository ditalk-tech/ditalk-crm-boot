package org.dromara.module.opportunity.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 商机信息对象 opportunity_info
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("opportunity_info")
public class OpportunityInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 乐观锁
     */
    @Version
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 商机标题
     */
    private String title;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 预计销售金额
     */
    private Long amount;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 预计成交日期
     */
    private Date closeDate;

    /**
     * 商机阶段
     */
    private String state;


}
