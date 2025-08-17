package org.dromara.module.contract.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 合同信息对象 contract_info
 *
 * @author weidixian
 * @date 2025-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_info")
public class ContractInfo extends TenantEntity {

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
     * 编号
     */
    private String code;

    /**
     * 标题
     */
    private String title;

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
     * 签约日期
     */
    private Date signDate;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 含税总额
     */
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
    private String state;


}
