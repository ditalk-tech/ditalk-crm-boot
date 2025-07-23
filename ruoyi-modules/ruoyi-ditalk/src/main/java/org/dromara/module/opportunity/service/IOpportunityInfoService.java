package org.dromara.module.opportunity.service;

import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商机信息Service接口
 *
 * @author weidixian
 * @date 2025-07-23
 */
public interface IOpportunityInfoService {

    /**
     * 查询商机信息
     *
     * @param id 主键
     * @return 商机信息
     */
    OpportunityInfoVo queryById(Long id);

    /**
     * 分页查询商机信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机信息分页列表
     */
    TableDataInfo<OpportunityInfoVo> queryPageList(OpportunityInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商机信息列表
     *
     * @param bo 查询条件
     * @return 商机信息列表
     */
    List<OpportunityInfoVo> queryList(OpportunityInfoBo bo);

    /**
     * 新增商机信息
     *
     * @param bo 商机信息
     * @return 是否新增成功
     */
    Boolean insertByBo(OpportunityInfoBo bo);

    /**
     * 修改商机信息
     *
     * @param bo 商机信息
     * @return 是否修改成功
     */
    Boolean updateByBo(OpportunityInfoBo bo);

    /**
     * 删除商机信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商机信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商机信息列表
     *
     * @param bo 查询条件
     * @return 商机信息列表
     */
    List<OpportunityInfoVo> queryList(OpportunityInfoBo bo, IdPageQuery pageQuery);
}
