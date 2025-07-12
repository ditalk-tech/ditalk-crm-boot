package org.dromara.module.goods.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品信息对象 goods_info
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_info")
public class GoodsInfo extends TenantEntity {

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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 主图ID
     */
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
    private Long minPrice;

    /**
     * 总销量
     */
    private Long totalSales;

    /**
     * 可用库存
     */
    private Long availableStock;

    /**
     * 综合评分
     */
    private Long overallScore;

    /**
     * 排序
     */
    private Long sortOrder;

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
    private String state;

}
