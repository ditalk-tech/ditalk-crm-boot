package org.dromara.module.customer.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户转移记录对象 customer_transfer_log
 *
 * @author weidixian
 * @date 2025-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_transfer_log")
public class CustomerTransferLog extends TenantEntity {

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
     * 客户ID
     */
    private Long customerId;

    /**
     * 原用户ID
     */
    private Long oldUserId;

    /**
     * 原部门ID
     */
    private Long oldDeptId;

    /**
     * 新用户ID
     */
    private Long newUserId;

    /**
     * 新部门ID
     */
    private Long newDeptId;

    /**
     * 状态
     */
    private String state;


}
