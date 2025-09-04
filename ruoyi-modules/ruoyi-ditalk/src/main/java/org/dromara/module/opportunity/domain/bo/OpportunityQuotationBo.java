package org.dromara.module.opportunity.domain.bo;

import org.dromara.module.opportunity.domain.OpportunityQuotation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 商机报价单业务对象 opportunity_quotation
 *
 * @author weidixian
 * @date 2025-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = OpportunityQuotation.class, reverseConvertGenerate = false)
public class OpportunityQuotationBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 乐观锁
     */
    @NotNull(message = "乐观锁不能为空", groups = { EditGroup.class })
    private Long version;

    /**
     * 商机ID
     */
    @NotNull(message = "商机ID不能为空", groups = { AddGroup.class })
    private Long opportunityId;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空", groups = { AddGroup.class })
    private Long customerId;

    /**
     * 联系人ID
     */
    @NotNull(message = "联系人ID不能为空", groups = { AddGroup.class })
    private Long contactId;

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
    @NotNull(message = "有效期到不能为空", groups = { AddGroup.class })
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

    /**
     * 引用状态
     */
    private String quoted;
}
