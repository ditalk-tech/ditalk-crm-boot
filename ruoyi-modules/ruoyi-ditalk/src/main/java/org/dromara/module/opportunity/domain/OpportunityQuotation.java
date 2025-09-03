package org.dromara.module.opportunity.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 商机报价单对象 opportunity_quotation
 *
 * @author weidixian
 * @date 2025-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("opportunity_quotation")
public class OpportunityQuotation extends TenantEntity {

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
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 编号
     */
    private String code;

    /**
     * 总售价
     */
    private Long totalSalePrice;

    /**
     * 总定价
     */
    private Long totalOriginalPrice;

    /**
     * 总成本
     */
    private Long totalCostPrice;

    /**
     * 有效期到
     */
    private Date validUntil;

    /**
     * 分派给
     */
    private Long assignedTo;

    /**
     * 分派部门
     */
    private Long assignedDept;

    /**
     * 审批状态
     */
    private String approvalState;

    /**
     * 交互状态
     */
    private String quotationState;

    /**
     * 备注
     */
    private String remark;


}
