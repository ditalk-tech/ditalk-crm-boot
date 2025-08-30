package org.dromara.handler;

import org.dromara.module.contract.domain.bo.ContractInfoBo;

/**
 * 合同信息应用接口
 *
 * @author weidixian
 */
public interface IContractInfoHandler {

    /**
     * 生成合同编号
     * @param prefix 前缀
     * @param datePattern 日期格式
     * @param separator 分隔符
     * @param serial 序列号
     * @return 合同编号
     */
    String generateContractCode(String prefix, String datePattern, String separator, String serial);

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
