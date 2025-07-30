package org.dromara.handler;

import jakarta.validation.constraints.NotNull;
import org.dromara.app.domain.bo.CustomerContactBo;

/**
 * 客户信息应用接口
 *
 * @author weidixian
 */
public interface ICustomerInfoHandler {

    Boolean addByBo(CustomerContactBo bo);

    Boolean editByBo(CustomerContactBo bo);

    /**
     * 通过ID回收客户到公海
     *
     * @param id 主键
     */
    Boolean reclaimById(Long id);

    /**
     * 转移客户到指定用户
     * @param customerId
     * @param userId
     * @return
     */
    Boolean transfer(Long customerId, Long userId);
}
