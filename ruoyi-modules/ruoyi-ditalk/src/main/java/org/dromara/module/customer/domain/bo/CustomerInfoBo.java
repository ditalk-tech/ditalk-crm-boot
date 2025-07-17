package org.dromara.module.customer.domain.bo;

import org.dromara.module.customer.domain.CustomerInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 客户信息业务对象 customer_info
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CustomerInfo.class, reverseConvertGenerate = false)
public class CustomerInfoBo extends BaseEntity {

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
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 客户类型
     */
    @NotBlank(message = "客户类型不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 备注信息
     */
    private String remark;

    /**
     * 主联系人ID
     */
    @NotNull(message = "主联系人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contactId;

    /**
     * 客户状态
     */
    @NotBlank(message = "客户状态不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "线索状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String leadState;


}
