package org.dromara.module.goods.domain.bo;

import org.dromara.module.goods.domain.GoodsBrand;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品品牌信息业务对象 goods_brand
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GoodsBrand.class, reverseConvertGenerate = false)
public class GoodsBrandBo extends BaseEntity {

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
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * Logo
     */
    private Long logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 官网
     */
    private String website;

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
