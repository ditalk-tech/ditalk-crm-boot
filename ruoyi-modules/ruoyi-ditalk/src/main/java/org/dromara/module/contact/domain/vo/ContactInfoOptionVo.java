package org.dromara.module.contact.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 联系人信息选项视图对象 contact_info
 *
 * @author weidixian
 * @date 2025-07-18
 */
@Data
public class ContactInfoOptionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 姓氏
     */
    @ExcelProperty(value = "姓氏")
    private String lastName;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String firstName;

}
