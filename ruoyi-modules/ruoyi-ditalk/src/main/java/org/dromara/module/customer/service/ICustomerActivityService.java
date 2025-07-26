package org.dromara.module.customer.service;

import org.dromara.module.customer.domain.vo.CustomerActivityVo;
import org.dromara.module.customer.domain.bo.CustomerActivityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客户活动记录Service接口
 *
 * @author weidixian
 * @date 2025-07-26
 */
public interface ICustomerActivityService {

    /**
     * 查询客户活动记录
     *
     * @param id 主键
     * @return 客户活动记录
     */
    CustomerActivityVo queryById(Long id);

    /**
     * 分页查询客户活动记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户活动记录分页列表
     */
    TableDataInfo<CustomerActivityVo> queryPageList(CustomerActivityBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的客户活动记录列表
     *
     * @param bo 查询条件
     * @return 客户活动记录列表
     */
    List<CustomerActivityVo> queryList(CustomerActivityBo bo);

    /**
     * 新增客户活动记录
     *
     * @param bo 客户活动记录
     * @return 是否新增成功
     */
    Boolean insertByBo(CustomerActivityBo bo);

    /**
     * 修改客户活动记录
     *
     * @param bo 客户活动记录
     * @return 是否修改成功
     */
    Boolean updateByBo(CustomerActivityBo bo);

    /**
     * 删除客户活动记录
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除客户活动记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询客户活动记录列表
     *
     * @param bo 查询条件
     * @return 客户活动记录列表
     */
    List<CustomerActivityVo> queryList(CustomerActivityBo bo, IdPageQuery pageQuery);
}
