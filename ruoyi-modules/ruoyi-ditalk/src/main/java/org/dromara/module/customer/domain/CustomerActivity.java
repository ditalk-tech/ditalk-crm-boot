package org.dromara.module.customer.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 客户活动记录对象 customer_activity
 *
 * @author weidixian
 * @date 2025-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_activity")
public class CustomerActivity extends TenantEntity {

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
     * 联系人ID
     */
    private Long contactId;

    /**
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 主题
     */
    private String subject;

    /**
     * 描述内容
     */
    private String description;

    /**
     * 活动时间
     */
    private Date activityTime;


}
