package org.dromara.module.shop.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 店铺信息对象 shop_info
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shop_info")
public class ShopInfo extends TenantEntity {

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
    private Long rating;

    /**
     * 店铺状态
     */
    private String state;


}
