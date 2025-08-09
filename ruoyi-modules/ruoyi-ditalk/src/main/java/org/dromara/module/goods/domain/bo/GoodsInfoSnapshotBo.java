package org.dromara.module.goods.domain.bo;

import org.dromara.module.goods.domain.GoodsInfoSnapshot;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 商品信息快照业务对象 goods_info_snapshot
 *
 * @author weidixian
 * @date 2025-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GoodsInfoSnapshot.class, reverseConvertGenerate = false)
public class GoodsInfoSnapshotBo extends BaseEntity {

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
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long goodsId;

    /**
     * 店铺ID
     */
    @NotNull(message = "店铺ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shopId;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 主图ID
     */
    @NotNull(message = "主图ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long mainPic;

    /**
     * 轮播图ID组
     */
    private String subPics;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 商品说明
     */
    private String content;

    /**
     * 最低价
     */
    @NotNull(message = "最低价不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long minPrice;

    /**
     * 综合评分（满10分）
     */
    @NotNull(message = "综合评分（满10分）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long overallScore;

    /**
     * 属性JSON
     */
    private String attrJson;

    /**
     * 规格JSON
     */
    private String specJson;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
