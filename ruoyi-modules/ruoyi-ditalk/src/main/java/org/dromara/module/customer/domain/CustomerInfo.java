package org.dromara.module.customer.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 客户信息对象 customer_info
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_info")
public class CustomerInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 乐观锁
     */
    @Version
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户类型
     */
    private String type;

    /**
     * 来源渠道
     */
    private String source;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 客户级别
     */
    private String tier;

    /**
     * 公司官网
     */
    private String website;

    /**
     * 地址
     */
    private String address;

    /**
     * 分配到
     */
    private Long assignedTo;

    /**
     * 分配部门
     */
    private Long assignedDept;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 主联系人ID
     */
    private Long contactId;

    /**
     * 客户状态
     */
    private String state;

    /**
     * 转换时间
     */
    private Date convertedTime;

    /**
     * 转换人
     */
    private Long convertedBy;

    /**
     * 线索状态
     */
    private String leadState;

}
