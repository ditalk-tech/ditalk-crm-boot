package org.dromara.module.customer.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 客户信息选项视图对象 customer_info
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Data
public class CustomerInfoOptionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户类型
     */
    private String type;

    /**
     * 客户状态
     */
    private String state;

    /**
     * 主联系人ID
     */
    private Long contactId;

}
