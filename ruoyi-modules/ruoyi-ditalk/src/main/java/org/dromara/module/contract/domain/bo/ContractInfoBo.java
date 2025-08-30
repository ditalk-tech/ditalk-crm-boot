package org.dromara.module.contract.domain.bo;

import org.dromara.module.contract.domain.ContractInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 合同信息业务对象 contract_info
 *
 * @author weidixian
 * @date 2025-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ContractInfo.class, reverseConvertGenerate = false)
public class ContractInfoBo extends BaseEntity {

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
     * 编号
     */
    private String code;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

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
    @NotNull(message = "商机ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long opportunityId;

    /**
     * 签约日期
     */
    private Date signDate;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endDate;

    /**
     * 含税总额
     */
    @NotNull(message = "含税总额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long totalAmount;

    /**
     * 税费金额
     */
    private Long taxAmount;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 指派给
     */
    private Long assignedTo;

    /**
     * 指派部门
     */
    private Long assignedDept;

    /**
     * 附件
     */
    private String terms;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;

}
