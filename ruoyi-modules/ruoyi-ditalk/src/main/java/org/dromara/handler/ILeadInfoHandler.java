package org.dromara.handler;

import jakarta.validation.constraints.NotNull;
import org.dromara.app.domain.bo.LeadContactBo;

import java.util.List;

/**
 * 客户线索信息应用接口
 *
 * @author weidixian
 */
public interface ILeadInfoHandler {

    Boolean addByBo(LeadContactBo bo);

    Boolean editByBo(LeadContactBo bo);


    /**
     * 通过ID回收线索到公海
     *
     * @param leadIds 主键
     * @return 是否回收成功
     */
    Boolean reclaimById(List<Long> leadIds);

    /**
     * 转移线索到指定用户
     *
     * @param leadIds
     * @param userId
     * @return 是否转移成功
     */
    Boolean transfer(List<Long> leadIds, Long userId);

    /**
     * 回收指定用户的所有线索到公海
     *
     * @param userId 用户ID
     * @return 是否回收成功
     */
    Boolean reclaimUserLead(Long userId);

    /**
     * 转移指定用户的线索到另一个用户
     *
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID
     * @return 是否转移成功
     */
    Boolean transferUserLead(Long sourceUserId, Long targetUserId);

    /**
     * 认领线索到指定用户
     *
     * @param userId 用户ID
     * @param leadIds 线索ID列表
     * @return 是否认领成功
     */
    Boolean claim(Long userId, List<Long> leadIds);
}
