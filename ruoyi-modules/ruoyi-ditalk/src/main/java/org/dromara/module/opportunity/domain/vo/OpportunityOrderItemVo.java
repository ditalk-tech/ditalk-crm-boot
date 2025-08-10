package org.dromara.module.opportunity.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.opportunity.domain.OpportunityOrderItem;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 商机商品视图对象 opportunity_order_item
 *
 * @author weidixian
 * @date 2025-08-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = OpportunityOrderItem.class)
public class OpportunityOrderItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 乐观锁
     */
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 商机ID
     */
    @ExcelProperty(value = "商机ID")
    private Long opportunityId;

    /**
     * 店铺ID
     */
    @ExcelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 商品快照ID
     */
    @ExcelProperty(value = "商品快照ID")
    private Long goodsSnapshotId;

    /**
     * 商品快照ID
     */
    @ExcelProperty(value = "商品快照ID")
    private Long skuId;

    /**
     * SKU编码
     */
    @ExcelProperty(value = "SKU编码")
    private String skuSn;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private Long mainPic;

    /**
     * 图片Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "mainPic")
    private String mainPicUrl;
    /**
     * 规格JSON
     */
    @ExcelProperty(value = "规格JSON")
    private String specJson;

    /**
     * 售价
     */
    @ExcelProperty(value = "售价")
    private Long salePrice;

    /**
     * 原价
     */
    @ExcelProperty(value = "原价")
    private Long originalPrice;

    /**
     * 成本价
     */
    @ExcelProperty(value = "成本价")
    private Long costPrice;

    /**
     * 重量(kg)
     */
    @ExcelProperty(value = "重量(kg)")
    private Long weight;

    /**
     * 体积(m³)
     */
    @ExcelProperty(value = "体积(m³)")
    private Long volume;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private Long unitPrice;

    /**
     * 购买数量
     */
    @ExcelProperty(value = "购买数量")
    private Long quantity;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private Long totalPrice;


}
