package org.dromara.module.shop.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.shop.domain.ShopInfo;
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
 * 店铺信息视图对象 shop_info
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ShopInfo.class)
public class ShopInfoVo implements Serializable {

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
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 商标
     */
    @ExcelProperty(value = "商标")
    private Long logo;

    /**
     * 商标Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "logo")
    private String logoUrl;
    /**
     * 主图
     */
    @ExcelProperty(value = "主图")
    private Long mainPic;

    /**
     * 主图Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "mainPic")
    private String mainPicUrl;
    /**
     * 店铺评分
     */
    @ExcelProperty(value = "店铺评分")
    private Long rating;

    /**
     * 店铺状态
     */
    @ExcelProperty(value = "店铺状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String state;


}
