package org.dromara.module.opportunity.service;

import org.dromara.module.opportunity.domain.vo.OpportunityOrderItemVo;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商机商品Service接口
 *
 * @author weidixian
 * @date 2025-08-10
 */
public interface IOpportunityOrderItemService {

    /**
     * 查询商机商品
     *
     * @param id 主键
     * @return 商机商品
     */
    OpportunityOrderItemVo queryById(Long id);

    /**
     * 分页查询商机商品列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机商品分页列表
     */
    TableDataInfo<OpportunityOrderItemVo> queryPageList(OpportunityOrderItemBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商机商品列表
     *
     * @param bo 查询条件
     * @return 商机商品列表
     */
    List<OpportunityOrderItemVo> queryList(OpportunityOrderItemBo bo);

    /**
     * 新增商机商品
     *
     * @param bo 商机商品
     * @return 是否新增成功
     */
    Boolean insertByBo(OpportunityOrderItemBo bo);

    /**
     * 修改商机商品
     *
     * @param bo 商机商品
     * @return 是否修改成功
     */
    Boolean updateByBo(OpportunityOrderItemBo bo);

    /**
     * 删除商机商品
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商机商品信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商机商品列表
     *
     * @param bo 查询条件
     * @return 商机商品列表
     */
    List<OpportunityOrderItemVo> queryList(OpportunityOrderItemBo bo, IdPageQuery pageQuery);
}
