package org.dromara.module.customer.service;

import org.dromara.module.customer.domain.vo.CustomerTransferLogVo;
import org.dromara.module.customer.domain.bo.CustomerTransferLogBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客户转移记录Service接口
 *
 * @author weidixian
 * @date 2025-08-22
 */
public interface ICustomerTransferLogService {

    /**
     * 查询客户转移记录
     *
     * @param id 主键
     * @return 客户转移记录
     */
    CustomerTransferLogVo queryById(Long id);

    /**
     * 分页查询客户转移记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户转移记录分页列表
     */
    TableDataInfo<CustomerTransferLogVo> queryPageList(CustomerTransferLogBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的客户转移记录列表
     *
     * @param bo 查询条件
     * @return 客户转移记录列表
     */
    List<CustomerTransferLogVo> queryList(CustomerTransferLogBo bo);

    /**
     * 新增客户转移记录
     *
     * @param bo 客户转移记录
     * @return 是否新增成功
     */
    Boolean insertByBo(CustomerTransferLogBo bo);

    /**
     * 修改客户转移记录
     *
     * @param bo 客户转移记录
     * @return 是否修改成功
     */
    Boolean updateByBo(CustomerTransferLogBo bo);

    /**
     * 删除客户转移记录
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除客户转移记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询客户转移记录列表
     *
     * @param bo 查询条件
     * @return 客户转移记录列表
     */
    List<CustomerTransferLogVo> queryList(CustomerTransferLogBo bo, IdPageQuery pageQuery);
}
