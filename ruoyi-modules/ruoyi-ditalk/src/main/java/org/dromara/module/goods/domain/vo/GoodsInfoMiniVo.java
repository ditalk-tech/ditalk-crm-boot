package org.dromara.module.goods.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.goods.domain.GoodsInfo;

import java.io.Serial;
import java.io.Serializable;


/**
 * 商品信息列表对象
 *
 * @author weidixian
 * @date 2025-07-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsInfo.class)
public class GoodsInfoMiniVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

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
     * 最低价
     */
    @ExcelProperty(value = "最低价")
    private Long minPrice;

    /**
     * 总销量
     */
    @ExcelProperty(value = "总销量")
    private Long totalSales;

    /**
     * 可用库存
     */
    @ExcelProperty(value = "可用库存")
    private Long availableStock;

    /**
     * 综合评分
     */
    @ExcelProperty(value = "综合评分")
    private Long overallScore;

}
