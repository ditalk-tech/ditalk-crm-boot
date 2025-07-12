package org.dromara.module.goods.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.module.goods.domain.GoodsInfo;

import java.io.Serial;
import java.io.Serializable;


/**
 * 商品信息选项对象
 *
 * @author weidixian
 * @date 2025-07-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsInfo.class)
public class GoodsInfoOptionVo implements Serializable {

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

}
