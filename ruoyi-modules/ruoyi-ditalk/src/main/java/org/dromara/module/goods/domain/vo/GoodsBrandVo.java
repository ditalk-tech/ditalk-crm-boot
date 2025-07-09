package org.dromara.module.goods.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.goods.domain.GoodsBrand;
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
 * 商品品牌信息视图对象 goods_brand
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsBrand.class)
public class GoodsBrandVo implements Serializable {

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
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 英文名称
     */
    @ExcelProperty(value = "英文名称")
    private String englishName;

    /**
     * Logo
     */
    @ExcelProperty(value = "Logo")
    private Long logo;

    /**
     * LogoUrl
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "logo")
    private String logoUrl;
    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 所属国家
     */
    @ExcelProperty(value = "所属国家")
    private String country;

    /**
     * 官网
     */
    @ExcelProperty(value = "官网")
    private String website;

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
