package org.dromara.module.goods.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品品牌信息对象 goods_brand
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_brand")
public class GoodsBrand extends TenantEntity {

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
     * 名称
     */
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
    private String state;


}
