package org.dromara.module.goods.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * SKU规格对象 goods_sku_spec
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_sku_spec")
public class GoodsSkuSpec extends TenantEntity {

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
     * 类目ID
     */
    private Long categoryId;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 规格选项JSON
     */
    private String specJson;

    /**
     * 排序
     */
    private Long sortOrder;

    /**
     * 状态
     */
    private String state;


}
