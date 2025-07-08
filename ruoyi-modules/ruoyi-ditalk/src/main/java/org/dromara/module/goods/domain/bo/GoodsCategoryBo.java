package org.dromara.module.goods.domain.bo;

import org.dromara.module.goods.domain.GoodsCategory;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品分类业务对象 goods_category
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GoodsCategory.class, reverseConvertGenerate = false)
public class GoodsCategoryBo extends BaseEntity {

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
     * 父类ID
     */
    @NotNull(message = "父类ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 祖级列表
     */
//    @NotBlank(message = "祖级列表不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ancestors;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 主图
     */
    private Long mainPic;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sortOrder;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
