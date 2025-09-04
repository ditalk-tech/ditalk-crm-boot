package org.dromara.module.opportunity.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.IdPageQuery;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.module.opportunity.domain.OpportunityQuotation;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationBo;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;
import org.dromara.module.opportunity.service.IOpportunityQuotationService;
import org.dromara.module.opportunity.mapper.OpportunityQuotationMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商机报价单Service业务层处理
 *
 * @author weidixian
 * @date 2025-09-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpportunityQuotationServiceImpl implements IOpportunityQuotationService {

    private final OpportunityQuotationMapper baseMapper;

    /**
     * 查询商机报价单
     *
     * @param id 主键
     * @return 商机报价单
     */
    @Override
    @Cacheable(cacheNames = CacheNames.OpportunityQuotation, key = "#id")
    public OpportunityQuotationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商机报价单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机报价单分页列表
     */
    @Override
    public TableDataInfo<OpportunityQuotationVo> queryPageList(OpportunityQuotationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityQuotation> lqw = buildQueryWrapper(bo);
        Page<OpportunityQuotationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商机报价单列表
     *
     * @param bo 查询条件
     * @return 商机报价单列表
     */
    @Override
    public List<OpportunityQuotationVo> queryList(OpportunityQuotationBo bo) {
        LambdaQueryWrapper<OpportunityQuotation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OpportunityQuotation> buildQueryWrapper(OpportunityQuotationBo bo) {
        LambdaQueryWrapper<OpportunityQuotation> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, OpportunityQuotation::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<OpportunityQuotation> buildWrapper(OpportunityQuotationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpportunityQuotation> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(OpportunityQuotation::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            OpportunityQuotation::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getOpportunityId() != null, OpportunityQuotation::getOpportunityId, bo.getOpportunityId());
        lqw.eq(bo.getCustomerId() != null, OpportunityQuotation::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getContactId() != null, OpportunityQuotation::getContactId, bo.getContactId());
        lqw.like(StringUtils.isNotBlank(bo.getCode()), OpportunityQuotation::getCode, bo.getCode());
        lqw.between(params.get("beginValidUntil") != null && params.get("endValidUntil") != null,
            OpportunityQuotation::getValidUntil, params.get("beginValidUntil"), params.get("endValidUntil"));
        lqw.eq(bo.getAssignedTo() != null, OpportunityQuotation::getAssignedTo, bo.getAssignedTo());
        lqw.eq(bo.getAssignedDept() != null, OpportunityQuotation::getAssignedDept, bo.getAssignedDept());
        lqw.eq(StringUtils.isNotBlank(bo.getApprovalState()), OpportunityQuotation::getApprovalState, bo.getApprovalState());
        lqw.eq(StringUtils.isNotBlank(bo.getQuotationState()), OpportunityQuotation::getQuotationState, bo.getQuotationState());
        lqw.eq(StringUtils.isNotBlank(bo.getQuoted()), OpportunityQuotation::getQuoted, bo.getQuoted());
        return lqw;
    }

    /**
     * 新增商机报价单
     *
     * @param bo 商机报价单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(OpportunityQuotationBo bo) {
        OpportunityQuotation add = MapstructUtils.convert(bo, OpportunityQuotation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商机报价单
     *
     * @param bo 商机报价单
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityQuotation, key = "#bo.id")
    public Boolean updateByBo(OpportunityQuotationBo bo) {
        OpportunityQuotation update = MapstructUtils.convert(bo, OpportunityQuotation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OpportunityQuotation entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商机报价单
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityQuotation, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商机报价单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @DSTransactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        Boolean flag = true;
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        for (Long id : ids) {
            flag = flag && SpringUtils.getAopProxy(this).deleteById(id);
        }
        return flag;
    }

    /**
     * 通过ID分页查询商机报价单列表
     *
     * @param bo 查询条件
     * @return 商机报价单列表
     */
    @Override
    public List<OpportunityQuotationVo> queryList(OpportunityQuotationBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityQuotation> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, OpportunityQuotation::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
