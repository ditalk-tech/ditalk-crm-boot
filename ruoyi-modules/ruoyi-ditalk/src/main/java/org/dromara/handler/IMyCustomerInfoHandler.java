package org.dromara.handler;

import org.dromara.app.domain.bo.CustomerContactBo;

/**
 * 我的客户信息应用接口
 *
 * @author weidixian
 */
public interface IMyCustomerInfoHandler {

    Boolean addByBo(CustomerContactBo bo);

    Boolean editByBo(CustomerContactBo bo);

}
