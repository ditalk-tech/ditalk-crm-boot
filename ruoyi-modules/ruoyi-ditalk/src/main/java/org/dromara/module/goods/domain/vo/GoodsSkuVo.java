package org.dromara.module.goods.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.goods.domain.GoodsSku;
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
 * 商品SKU视图对象 goods_sku
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsSku.class)
public class GoodsSkuVo implements Serializable {

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
    private Long version;

    /**
     * 店铺ID
     */
    @ExcelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long goodsId;

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
     * 可用库存
     */
    @ExcelProperty(value = "可用库存")
    private Long availableStock;

    /**
     * 锁定库存
     */
    @ExcelProperty(value = "锁定库存")
    private Long reservedStock;

    /**
     * 已占库存
     */
    @ExcelProperty(value = "已占库存")
    private Long allocatedStock;

    /**
     * 不可用库存
     */
    @ExcelProperty(value = "不可用库存")
    private Long unavailableStock;

    /**
     * 总库存
     */
    @ExcelProperty(value = "总库存")
    private Long totalStock;

    /**
     * 总销量
     */
    @ExcelProperty(value = "总销量")
    private Long totalSales;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String state;


}
