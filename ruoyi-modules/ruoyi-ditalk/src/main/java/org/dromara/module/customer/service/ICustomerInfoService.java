package org.dromara.module.customer.service;

import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客户信息Service接口
 *
 * @author weidixian
 * @date 2025-07-17
 */
public interface ICustomerInfoService {

    /**
     * 查询客户信息
     *
     * @param id 主键
     * @return 客户信息
     */
    CustomerInfoVo queryById(Long id);

    /**
     * 分页查询客户信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户信息分页列表
     */
    TableDataInfo<CustomerInfoVo> queryPageList(CustomerInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的客户信息列表
     *
     * @param bo 查询条件
     * @return 客户信息列表
     */
    List<CustomerInfoVo> queryList(CustomerInfoBo bo);

    /**
     * 新增客户信息
     *
     * @param bo 客户信息
     * @return 是否新增成功
     */
    Boolean insertByBo(CustomerInfoBo bo);

    /**
     * 修改客户信息
     *
     * @param bo 客户信息
     * @return 是否修改成功
     */
    Boolean updateByBo(CustomerInfoBo bo);

    /**
     * 删除客户信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除客户信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询客户信息列表
     *
     * @param bo 查询条件
     * @return 客户信息列表
     */
    List<CustomerInfoVo> queryList(CustomerInfoBo bo, IdPageQuery pageQuery);

    /**
     * 通过客户ID查询客户信息，不使用缓存
     *
     * @param id 客户ID
     * @return 客户信息
     */
    CustomerInfoVo queryByIdNoCache(Long id);

    /**
     * 通过ID查询 客户与线索 所有类型数据，不使用缓存
     *
     * @param id 客户ID
     * @return 客户信息
     */
    CustomerInfoVo queryAllByIdNoCache(Long id);
}
