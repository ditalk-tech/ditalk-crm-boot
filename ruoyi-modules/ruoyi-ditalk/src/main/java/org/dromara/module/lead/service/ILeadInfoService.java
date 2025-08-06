package org.dromara.module.lead.service;

import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 线索信息Service接口
 *
 * @author weidixian
 * @date 2025-07-17
 */
public interface ILeadInfoService {

    /**
     * 查询线索信息
     *
     * @param id 主键
     * @return 线索信息
     */
    LeadInfoVo queryById(Long id);

    /**
     * 分页查询线索信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 线索信息分页列表
     */
    TableDataInfo<LeadInfoVo> queryPageList(LeadInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的线索信息列表
     *
     * @param bo 查询条件
     * @return 线索信息列表
     */
    List<LeadInfoVo> queryList(LeadInfoBo bo);

    /**
     * 新增线索信息
     *
     * @param bo 线索信息
     * @return 是否新增成功
     */
    Boolean insertByBo(LeadInfoBo bo);

    /**
     * 修改线索信息
     *
     * @param bo 线索信息
     * @return 是否修改成功
     */
    Boolean updateByBo(LeadInfoBo bo);

    /**
     * 删除线索信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除线索信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询线索信息列表
     *
     * @param bo 查询条件
     * @return 线索信息列表
     */
    List<LeadInfoVo> queryList(LeadInfoBo bo, IdPageQuery pageQuery);

    /**
     * 根据客户ID查询线索信息，不使用缓存
     *
     * @param id 客户ID
     * @return 线索信息
     */
    LeadInfoVo queryByIdNoCache(Long id);
}
