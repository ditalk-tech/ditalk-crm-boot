package org.dromara.module.goods.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.goods.domain.GoodsInfoSnapshot;
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
 * 商品信息快照视图对象 goods_info_snapshot
 *
 * @author weidixian
 * @date 2025-08-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsInfoSnapshot.class)
public class GoodsInfoSnapshotVo implements Serializable {

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
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 店铺ID
     */
    @ExcelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 分类ID
     */
    @ExcelProperty(value = "分类ID")
    private Long categoryId;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private String spuCode;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 副标题
     */
    @ExcelProperty(value = "副标题")
    private String subtitle;

    /**
     * 主图ID
     */
    @ExcelProperty(value = "主图ID")
    private Long mainPic;

    /**
     * 主图IDUrl
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "mainPic")
    private String mainPicUrl;
    /**
     * 轮播图ID组
     */
    @ExcelProperty(value = "轮播图ID组")
    private String subPics;

    /**
     * 轮播图ID组Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "subPics")
    private String subPicsUrl;
    /**
     * 条形码
     */
    @ExcelProperty(value = "条形码")
    private String barCode;

    /**
     * 品牌ID
     */
    @ExcelProperty(value = "品牌ID")
    private Long brandId;

    /**
     * 商品说明
     */
    @ExcelProperty(value = "商品说明")
    private String content;

    /**
     * 最低价
     */
    @ExcelProperty(value = "最低价")
    private Long minPrice;

    /**
     * 综合评分（满10分）
     */
    @ExcelProperty(value = "综合评分", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "满=10分")
    private Long overallScore;

    /**
     * 属性JSON
     */
    @ExcelProperty(value = "属性JSON")
    private String attrJson;

    /**
     * 规格JSON
     */
    @ExcelProperty(value = "规格JSON")
    private String specJson;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_goods_state")
    private String state;


}
