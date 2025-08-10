package org.dromara.module.opportunity.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

/**
 * 商机商品业务对象 opportunity_order_item
 *
 * @author weidixian
 * @date 2025-08-10
 */
@Data
public class OpportunityOrderItemTinyBo {

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
    @NotNull(message = "商机ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long opportunityId;

    /**
     * 商品SKU_ID
     */
    @NotNull(message = "商品SKU_ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long unitPrice;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quantity;

}
