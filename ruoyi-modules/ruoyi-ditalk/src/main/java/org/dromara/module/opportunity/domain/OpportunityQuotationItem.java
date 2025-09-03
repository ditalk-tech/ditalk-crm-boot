package org.dromara.module.opportunity.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 商机报价单明细对象 opportunity_quotation_item
 *
 * @author weidixian
 * @date 2025-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("opportunity_quotation_item")
public class OpportunityQuotationItem extends TenantEntity {

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
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 报价单ID
     */
    private Long quotationId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 商品快照ID
     */
    private Long goodsSnapshotId;

    /**
     * SKU_ID
     */
    private Long skuId;

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
     * 单价
     */
    private Long unitPrice;

    /**
     * 购买数量
     */
    private Long quantity;

    /**
     * 总价
     */
    private Long totalPrice;


}
