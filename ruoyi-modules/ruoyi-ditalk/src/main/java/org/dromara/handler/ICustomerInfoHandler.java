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

}
