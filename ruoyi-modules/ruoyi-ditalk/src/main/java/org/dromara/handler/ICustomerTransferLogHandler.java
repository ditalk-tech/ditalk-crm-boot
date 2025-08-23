package org.dromara.handler;

/**
 * 系统用户应用接口
 *
 * @author weidixian
 */
public interface ICustomerTransferLogHandler {

    /**
     * 记录客户转移日志<br>
     * 注意：记录日志动作会查询当前客户的归属信息，应在修改客户归属前调用
     *
     * @param customerId 客户ID
     * @param newUserId  新用户ID
     * @param newDeptId  新部门ID
     */
    void add(Long customerId, Long newUserId, Long newDeptId);
}
