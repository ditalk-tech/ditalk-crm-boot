package org.dromara.handler;

import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;

import java.util.List;

/**
 * 客户信息应用接口
 *
 * @author weidixian
 */
public interface ICustomerInfoHandler {

    Boolean addByBo(CustomerContactBo bo);

    Boolean editByBo(CustomerContactBo bo);

    List<CustomerInfoVo> myCustomers(CustomerInfoBo bo, PageQuery pageQuery);

    CustomerInfoVo myCustomer(Long id);
}
