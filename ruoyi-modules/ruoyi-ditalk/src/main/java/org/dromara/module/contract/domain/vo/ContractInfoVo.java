package org.dromara.module.contract.domain.vo;

import java.util.Date;
import org.dromara.module.contract.domain.ContractInfo;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 合同信息视图对象 contract_info
 *
 * @author weidixian
 * @date 2025-08-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ContractInfo.class)
public class ContractInfoVo implements Serializable {

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
     * 编号
     */
    @ExcelProperty(value = "编号")
    private String code;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 联系人ID
     */
    @ExcelProperty(value = "联系人ID")
    private Long contactId;

    /**
     * 商机ID
     */
    @ExcelProperty(value = "商机ID")
    private Long opportunityId;

    /**
     * 签约日期
     */
    @ExcelProperty(value = "签约日期")
    private Date signDate;

    /**
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    private Date startDate;

    /**
     * 结束日期
     */
    @ExcelProperty(value = "结束日期")
    private Date endDate;

    /**
     * 含税总额
     */
    @ExcelProperty(value = "含税总额")
    private Long totalAmount;

    /**
     * 税费金额
     */
    @ExcelProperty(value = "税费金额")
    private Long taxAmount;

    /**
     * 备注说明
     */
    @ExcelProperty(value = "备注说明")
    private String remark;

    /**
     * 指派给
     */
    @ExcelProperty(value = "指派给")
    private Long assignedTo;

    /**
     * 指派部门
     */
    @ExcelProperty(value = "指派部门")
    private Long assignedDept;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String terms;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_contract_state")
    private String state;


}
