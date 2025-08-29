package org.dromara.handler;

import org.dromara.module.customer.domain.vo.CustomerInfoVo;

/**
 * 客户/线索 公共应用接口
 *
 * @author weidixian
 */
public interface ICustomerInfoCommonHandler {

    /**
     * 回收客户相关资源<br>
     * 注意：该方法仅回收客户相关资源，不处理客户实体<br>
     * 注意：这里忽略数据权限校验，需要有客户权限的 <b>前提校验</b>
     *
     * @param customerId 客户ID
     */
    void reclaimById(Long customerId);

    /**
     * 转移客户相关资源到指定用户<br>
     * 注意：该方法仅转移客户相关资源，不处理客户实体<br>
     * 注意：这里忽略数据权限校验，需要有客户权限的 <b>前提校验</b>
     *
     * @param customerId 客户ID
     * @param userId     目标用户ID
     * @param deptId     目标部门ID
     */
    void transfer(Long customerId, Long userId, Long deptId);

    /**
     * 认领客户相关资源<br>
     * 注意：该方法仅认领客户相关资源，不处理客户实体<br>
     *
     * @param customerId
     * @param userId
     * @param deptId
     */
    void claim(Long customerId, Long userId, Long deptId);

    /**
     * 通过ID查询 客户与线索 所有类型数据，不使用缓存
     *
     * @param id 客户ID
     * @return 客户信息
     */
    CustomerInfoVo queryAllByIdNoCache(Long id);

}
