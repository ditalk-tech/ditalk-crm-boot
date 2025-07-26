package org.dromara.module.customer.domain.bo;

import org.dromara.module.customer.domain.CustomerActivity;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 客户活动记录业务对象 customer_activity
 *
 * @author weidixian
 * @date 2025-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CustomerActivity.class, reverseConvertGenerate = false)
public class CustomerActivityBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 乐观锁
     */
    @NotNull(message = "乐观锁不能为空", groups = { EditGroup.class })
    private Long version;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long customerId;

    /**
     * 联系人ID
     */
    @NotNull(message = "联系人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contactId;

    /**
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 活动类型
     */
    @NotBlank(message = "活动类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 主题
     */
    @NotBlank(message = "主题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subject;

    /**
     * 描述内容
     */
    @NotBlank(message = "描述内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 活动时间
     */
    private Date activityTime;


}
