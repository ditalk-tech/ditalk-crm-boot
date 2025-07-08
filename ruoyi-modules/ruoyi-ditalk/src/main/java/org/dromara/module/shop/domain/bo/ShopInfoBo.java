package org.dromara.module.shop.domain.bo;

import org.dromara.module.shop.domain.ShopInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 店铺信息业务对象 shop_info
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ShopInfo.class, reverseConvertGenerate = false)
public class ShopInfoBo extends BaseEntity {

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
     * 商标
     */
    private Long logo;

    /**
     * 主图
     */
    private Long mainPic;

    /**
     * 店铺评分
     */
    @NotNull(message = "店铺评分不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long rating;

    /**
     * 店铺状态
     */
    @NotBlank(message = "店铺状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
