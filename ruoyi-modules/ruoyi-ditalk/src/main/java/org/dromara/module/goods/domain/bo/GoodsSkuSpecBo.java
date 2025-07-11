package org.dromara.module.goods.domain.bo;

import org.dromara.module.goods.domain.GoodsSkuSpec;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * SKU规格业务对象 goods_sku_spec
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GoodsSkuSpec.class, reverseConvertGenerate = false)
public class GoodsSkuSpecBo extends BaseEntity {

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
     * 类目ID
     */
    @NotNull(message = "类目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
