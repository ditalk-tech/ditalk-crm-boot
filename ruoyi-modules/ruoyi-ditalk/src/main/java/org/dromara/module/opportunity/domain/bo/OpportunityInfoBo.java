package org.dromara.module.opportunity.domain.bo;

import org.dromara.module.opportunity.domain.OpportunityInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 商机信息业务对象 opportunity_info
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = OpportunityInfo.class, reverseConvertGenerate = false)
public class OpportunityInfoBo extends BaseEntity {

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
     * 商机标题
     */
    @NotBlank(message = "商机标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "商机阶段不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;

    /**
     * 分配到
     */
    private Long assignedTo;

    /**
     * 分配部门
     */
    private Long assignedDept;

}
