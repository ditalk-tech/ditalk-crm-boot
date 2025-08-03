package org.dromara.handler;

import org.dromara.app.domain.bo.CustomerContactBo;

import java.util.List;

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
     * @param customerIds 主键
     * @return 是否回收成功
     */
    Boolean reclaimById(List<Long> customerIds);

    /**
     * 转移客户到指定用户
     *
     * @param customerIds
     * @param userId
     * @return 是否转移成功
     */
    Boolean transfer(List<Long> customerIds, Long userId);

    /**
     * 回收指定用户的所有客户到公海
     *
     * @param userId 用户ID
     * @return 是否回收成功
     */
    Boolean reclaimUserCustomer(Long userId);

    /**
     * 转移指定用户的客户到另一个用户
     *
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID
     * @return 是否转移成功
     */
    Boolean transferUserCustomer(Long sourceUserId, Long targetUserId);

    /**
     * 认领客户到指定用户
     *
     * @param userId      用户ID
     * @param customerIds 客户ID列表
     * @return 是否认领成功
     */
    Boolean claim(Long userId, List<Long> customerIds);
}
