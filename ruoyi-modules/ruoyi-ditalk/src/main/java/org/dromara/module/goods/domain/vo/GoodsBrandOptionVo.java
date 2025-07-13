package org.dromara.module.goods.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.module.goods.domain.GoodsBrand;

import java.io.Serial;
import java.io.Serializable;


/**
 * 商品品牌信息Option视图对象
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GoodsBrand.class)
public class GoodsBrandOptionVo implements Serializable {

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
     * 英文名称
     */
    @ExcelProperty(value = "英文名称")
    private String englishName;

}
