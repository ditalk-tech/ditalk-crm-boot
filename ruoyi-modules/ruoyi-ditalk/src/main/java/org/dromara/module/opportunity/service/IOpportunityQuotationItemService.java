package org.dromara.module.opportunity.service;

import org.dromara.module.opportunity.domain.vo.OpportunityQuotationItemVo;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationItemBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商机报价单明细Service接口
 *
 * @author weidixian
 * @date 2025-09-03
 */
public interface IOpportunityQuotationItemService {

    /**
     * 查询商机报价单明细
     *
     * @param id 主键
     * @return 商机报价单明细
     */
    OpportunityQuotationItemVo queryById(Long id);

    /**
     * 分页查询商机报价单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机报价单明细分页列表
     */
    TableDataInfo<OpportunityQuotationItemVo> queryPageList(OpportunityQuotationItemBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商机报价单明细列表
     *
     * @param bo 查询条件
     * @return 商机报价单明细列表
     */
    List<OpportunityQuotationItemVo> queryList(OpportunityQuotationItemBo bo);

    /**
     * 新增商机报价单明细
     *
     * @param bo 商机报价单明细
     * @return 是否新增成功
     */
    Boolean insertByBo(OpportunityQuotationItemBo bo);

    /**
     * 修改商机报价单明细
     *
     * @param bo 商机报价单明细
     * @return 是否修改成功
     */
    Boolean updateByBo(OpportunityQuotationItemBo bo);

    /**
     * 删除商机报价单明细
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商机报价单明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商机报价单明细列表
     *
     * @param bo 查询条件
     * @return 商机报价单明细列表
     */
    List<OpportunityQuotationItemVo> queryList(OpportunityQuotationItemBo bo, IdPageQuery pageQuery);
}
