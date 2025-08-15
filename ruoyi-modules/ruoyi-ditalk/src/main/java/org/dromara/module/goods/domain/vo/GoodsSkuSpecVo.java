package org.dromara.module.goods.domain.vo;

import org.dromara.module.goods.domain.GoodsSkuSpec;
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
 * SKU规格视图对象 goods_sku_spec
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsSkuSpec.class)
public class GoodsSkuSpecVo implements Serializable {

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
     * 店铺名
     */
    @ExcelProperty(value = "店铺名")
    private String shopName;

    /**
     * 类目ID
     */
    @ExcelProperty(value = "类目ID")
    private Long categoryId;

    /**
     * 类目名
     */
    @ExcelProperty(value = "类目名")
    private String categoryName;

    /**
     * 规格名称
     */
    @ExcelProperty(value = "规格名称")
    private String name;

    /**
     * 规格选项JSON
     */
    @ExcelProperty(value = "规格选项JSON")
    private String specJson;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sortOrder;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String state;


}
