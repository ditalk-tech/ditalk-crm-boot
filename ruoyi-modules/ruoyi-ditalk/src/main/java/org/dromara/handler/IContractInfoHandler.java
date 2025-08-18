package org.dromara.handler;

import org.dromara.module.contract.domain.bo.ContractInfoBo;

/**
 * 合同信息应用接口
 *
 * @author weidixian
 */
public interface IContractInfoHandler {

    /**
     * 新增合同信息
     * @param bo
     * @return
     */
    Boolean add(ContractInfoBo bo);

    /**
     * 修改合同信息
     * @param bo
     * @return
     */
    Boolean edit(ContractInfoBo bo);
}
