package org.dromara.module.goods.domain.bo;

import org.dromara.common.validate.BatchGroup;
import org.dromara.module.goods.domain.GoodsSku;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品SKU业务对象 goods_sku
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GoodsSku.class, reverseConvertGenerate = false)
public class GoodsSkuBo extends BaseEntity {

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
     * 店铺ID
     */
    @NotNull(message = "店铺ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shopId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long goodsId;

    /**
     * SKU编码
     */
    private String skuSn;

    /**
     * 图片
     */
    @NotNull(message = "图片不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long mainPic;

    /**
     * 规格JSON
     */
    @NotNull(message = "规格不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private String specJson;

    /**
     * 售价
     */
    @NotNull(message = "售价不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long salePrice;

    /**
     * 原价
     */
//    @NotNull(message = "原价不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long originalPrice;

    /**
     * 成本价
     */
//    @NotNull(message = "成本价不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long costPrice;

    /**
     * 重量(kg)
     */
//    @NotNull(message = "重量(kg)不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long weight;

    /**
     * 体积(m³)
     */
//    @NotNull(message = "体积(m³)不能为空", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long volume;

    /**
     * 可用库存
     */
    @NotNull(message = "可用库存", groups = { AddGroup.class, EditGroup.class, BatchGroup.class })
    private Long availableStock;

    /**
     * 锁定库存
     */
    private Long reservedStock;

    /**
     * 已占库存
     */
    private Long allocatedStock;

    /**
     * 不可用库存
     */
    private Long unavailableStock;

    /**
     * 总库存
     */
    private Long totalStock;

    /**
     * 总销量
     */
    private Long totalSales;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
