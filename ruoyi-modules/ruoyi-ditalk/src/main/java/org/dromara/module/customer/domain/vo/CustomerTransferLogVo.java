package org.dromara.module.customer.domain.vo;

import org.dromara.module.customer.domain.CustomerTransferLog;
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
 * 客户转移记录视图对象 customer_transfer_log
 *
 * @author weidixian
 * @date 2025-08-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CustomerTransferLog.class)
public class CustomerTransferLogVo implements Serializable {

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
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 原用户ID
     */
    @ExcelProperty(value = "原用户ID")
    private Long oldUserId;

    /**
     * 原部门ID
     */
    @ExcelProperty(value = "原部门ID")
    private Long oldDeptId;

    /**
     * 新用户ID
     */
    @ExcelProperty(value = "新用户ID")
    private Long newUserId;

    /**
     * 新部门ID
     */
    @ExcelProperty(value = "新部门ID")
    private Long newDeptId;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String state;

}
