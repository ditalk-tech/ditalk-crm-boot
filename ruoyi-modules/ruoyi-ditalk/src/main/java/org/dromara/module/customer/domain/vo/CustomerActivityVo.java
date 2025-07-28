package org.dromara.module.customer.domain.vo;

import java.util.Date;
import org.dromara.module.customer.domain.CustomerActivity;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 客户活动记录视图对象 customer_activity
 *
 * @author weidixian
 * @date 2025-07-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CustomerActivity.class)
public class CustomerActivityVo implements Serializable {

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
     * 创建人ID
     */
    @ExcelProperty(value = "创建人ID")
    private Long createBy;

    /**
     * 乐观锁
     */
    @ExcelProperty(value = "乐观锁")
    private Long version;

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
     * 联系人姓名
     */
    private String contactName;

    /**
     * 商机ID
     */
    @ExcelProperty(value = "商机ID")
    private Long opportunityId;

    /**
     * 活动类型
     */
    @ExcelProperty(value = "活动类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_activity_type")
    private String type;

    /**
     * 主题
     */
    @ExcelProperty(value = "主题")
    private String subject;

    /**
     * 描述内容
     */
    @ExcelProperty(value = "描述内容")
    private String description;

    /**
     * 活动时间
     */
    @ExcelProperty(value = "活动时间")
    private Date activityTime;


}
