package org.dromara.module.opportunity.domain.bo;

import org.dromara.module.opportunity.domain.OpportunityQuotationItem;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 商机报价单明细业务对象 opportunity_quotation_item
 *
 * @author weidixian
 * @date 2025-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = OpportunityQuotationItem.class, reverseConvertGenerate = false)
public class OpportunityQuotationItemBo extends BaseEntity {

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
     * 商机ID
     */
//    @NotNull(message = "商机ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long opportunityId;

    /**
     * 报价单ID
     */
    @NotNull(message = "报价单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quotationId;

    /**
     * 店铺ID
     */
//    @NotNull(message = "店铺ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shopId;

    /**
     * 客户ID
     */
//    @NotNull(message = "客户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long customerId;

    /**
     * 商品快照ID
     */
//    @NotNull(message = "商品快照ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long goodsSnapshotId;

    /**
     * SKU_ID
     */
    @NotNull(message = "SKU_ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * SKU编码
     */
    private String skuSn;

    /**
     * 图片
     */
//    @NotNull(message = "图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long mainPic;

    /**
     * 规格JSON
     */
//    @NotBlank(message = "规格JSON不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specJson;

    /**
     * 单位
     */
    private String unitName;

    /**
     * 售价
     */
//    @NotNull(message = "售价不能为空", groups = { AddGroup.class, EditGroup.class })
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
//    @NotNull(message = "重量(kg)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long weight;

    /**
     * 体积(m³)
     */
//    @NotNull(message = "体积(m³)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long volume;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long unitPrice;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quantity;

    /**
     * 总价
     */
//    @NotNull(message = "总价不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long totalPrice;

    /**
     * 交货日期
     */
    private Date deliveryDate;

}
