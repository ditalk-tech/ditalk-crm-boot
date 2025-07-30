package org.dromara.handler;

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
     * @param customerId 主键
     * @return 是否回收成功
     */
    Boolean reclaimById(Long customerId);

    /**
     * 转移客户到指定用户
     *
     * @param customerId
     * @param userId
     * @return 是否转移成功
     */
    Boolean transfer(Long customerId, Long userId);
}
