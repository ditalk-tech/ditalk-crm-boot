package org.dromara.module.opportunity.domain.vo;

import java.util.Date;
import org.dromara.module.opportunity.domain.OpportunityQuotation;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 商机报价单视图对象 opportunity_quotation
 *
 * @author weidixian
 * @date 2025-09-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = OpportunityQuotation.class)
public class OpportunityQuotationVo implements Serializable {

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
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private String code;

    /**
     * 总售价
     */
    @ExcelProperty(value = "总售价")
    private Long totalSalePrice;

    /**
     * 总定价
     */
    @ExcelProperty(value = "总定价")
    private Long totalOriginalPrice;

    /**
     * 总成本
     */
    @ExcelProperty(value = "总成本")
    private Long totalCostPrice;

    /**
     * 有效期到
     */
    @ExcelProperty(value = "有效期到")
    private Date validUntil;

    /**
     * 分派给
     */
    @ExcelProperty(value = "分派给")
    private Long assignedTo;

    /**
     * 分派部门
     */
    @ExcelProperty(value = "分派部门")
    private Long assignedDept;

    /**
     * 审批状态
     */
    @ExcelProperty(value = "审批状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "wf_business_status")
    private String approvalState;

    /**
     * 交互状态
     */
    @ExcelProperty(value = "交互状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_opportunity_quotation_state")
    private String quotationState;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
