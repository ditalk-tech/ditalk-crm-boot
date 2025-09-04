package org.dromara.module.goods.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品SKU对象 goods_sku
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_sku")
public class GoodsSku extends TenantEntity {

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
     * 店铺ID
     */
    private Long shopId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * SKU编码
     */
    private String skuSn;

    /**
     * 图片
     */
    private Long mainPic;

    /**
     * 规格JSON
     */
    private String specJson;

    /**
     * 单位
     */
    private String unitName;

    /**
     * 售价
     */
    private Long salePrice;

    /**
     * 原价
     */
    private Long originalPrice;

    /**
     * 成本价
     */
    private Long costPrice;

    /**
     * 重量(kg)
     */
    private Long weight;

    /**
     * 体积(m³)
     */
    private Long volume;

    /**
     * 可用库存
     */
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
    private String state;


}
