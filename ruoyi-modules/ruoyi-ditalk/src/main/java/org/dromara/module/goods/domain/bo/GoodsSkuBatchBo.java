package org.dromara.module.goods.domain.bo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.validate.BatchGroup;

import java.util.List;

/**
 * 商品SKU批量业务管理对象
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
public class GoodsSkuBatchBo {

    /**
     * 店铺ID
     */
    @NotNull(message = "店铺ID不能为空", groups = { BatchGroup.class })
    private Long shopId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { BatchGroup.class })
    private Long goodsId;

    /**
     * SKU集合
     */
    @Valid
    private List<GoodsSkuBo> goodsSkuBos;
}
