package org.dromara.module.lead.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.lead.domain.LeadInfo;
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
 * 线索信息视图对象 customer_info
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = LeadInfo.class)
public class LeadInfoVo implements Serializable {

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
     * 客户名称
     */
    @ExcelProperty(value = "客户名称")
    private String name;

    /**
     * 客户类型
     */
    @ExcelProperty(value = "客户类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_type")
    private String type;

    /**
     * 来源渠道
     */
    @ExcelProperty(value = "来源渠道", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_source")
    private String source;

    /**
     * 所属行业
     */
    @ExcelProperty(value = "所属行业", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_industry")
    private String industry;

    /**
     * 客户级别
     */
    @ExcelProperty(value = "客户级别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_tier")
    private String tier;

    /**
     * 公司官网
     */
    @ExcelProperty(value = "公司官网")
    private String website;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 分配到
     */
    @ExcelProperty(value = "分配到")
    private Long assignedTo;

    /**
     * 分配部门
     */
    @ExcelProperty(value = "分配部门")
    private Long assignedDept;

    /**
     * 备注信息
     */
    @ExcelProperty(value = "备注信息")
    private String remark;

    /**
     * 主联系人ID
     */
    @ExcelProperty(value = "主联系人ID")
    private Long contactId;

    /**
     * 客户状态
     */
    @ExcelProperty(value = "客户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_customer_state")
    private String state;

    /**
     * 转换时间
     */
    @ExcelProperty(value = "转换时间")
    private Date convertedTime;

    /**
     * 转换人
     */
    @ExcelProperty(value = "转换人")
    private Long convertedBy;

    /**
     * 线索状态
     */
    @ExcelProperty(value = "线索状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_lead_state")
    private String leadState;

    /**
     * 主联系人信息
     */
    private ContactInfoVo contactInfo;

}
