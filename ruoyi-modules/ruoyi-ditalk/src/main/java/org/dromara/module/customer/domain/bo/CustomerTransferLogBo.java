package org.dromara.module.customer.domain.bo;

import org.dromara.module.customer.domain.CustomerTransferLog;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 客户转移记录业务对象 customer_transfer_log
 *
 * @author weidixian
 * @date 2025-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CustomerTransferLog.class, reverseConvertGenerate = false)
public class CustomerTransferLogBo extends BaseEntity {

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
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
