package org.dromara.module.contract.service;

import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 合同信息Service接口
 *
 * @author weidixian
 * @date 2025-08-17
 */
public interface IContractInfoService {

    /**
     * 查询合同信息
     *
     * @param id 主键
     * @return 合同信息
     */
    ContractInfoVo queryById(Long id);

    /**
     * 分页查询合同信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 合同信息分页列表
     */
    TableDataInfo<ContractInfoVo> queryPageList(ContractInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的合同信息列表
     *
     * @param bo 查询条件
     * @return 合同信息列表
     */
    List<ContractInfoVo> queryList(ContractInfoBo bo);

    /**
     * 新增合同信息
     *
     * @param bo 合同信息
     * @return 是否新增成功
     */
    Boolean insertByBo(ContractInfoBo bo);

    /**
     * 修改合同信息
     *
     * @param bo 合同信息
     * @return 是否修改成功
     */
    Boolean updateByBo(ContractInfoBo bo);

    /**
     * 删除合同信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除合同信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询合同信息列表
     *
     * @param bo 查询条件
     * @return 合同信息列表
     */
    List<ContractInfoVo> queryList(ContractInfoBo bo, IdPageQuery pageQuery);
}
