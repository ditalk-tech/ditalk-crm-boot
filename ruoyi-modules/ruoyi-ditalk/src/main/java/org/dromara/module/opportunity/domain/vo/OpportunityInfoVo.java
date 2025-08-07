package org.dromara.module.opportunity.domain.vo;

import java.util.Date;
import org.dromara.module.opportunity.domain.OpportunityInfo;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 商机信息视图对象 opportunity_info
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = OpportunityInfo.class)
public class OpportunityInfoVo implements Serializable {

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
     * 商机标题
     */
    @ExcelProperty(value = "商机标题")
    private String title;

    /**
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 预计销售金额
     */
    @ExcelProperty(value = "预计销售金额")
    private Long amount;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 备注信息
     */
    @ExcelProperty(value = "备注信息")
    private String remark;

    /**
     * 预计成交日期
     */
    @ExcelProperty(value = "预计成交日期")
    private Date closeDate;

    /**
     * 商机阶段
     */
    @ExcelProperty(value = "商机阶段", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_opportunity_state")
    private String state;


}
