package org.dromara.handler;

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
}
