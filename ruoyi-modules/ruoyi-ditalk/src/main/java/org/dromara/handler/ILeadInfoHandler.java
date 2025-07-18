package org.dromara.handler;

import org.dromara.app.domain.bo.LeadContactBo;

/**
 * 客户线索信息应用接口
 *
 * @author weidixian
 */
public interface ILeadInfoHandler {

    Boolean addByBo(LeadContactBo bo);

    Boolean editByBo(LeadContactBo bo);

}
