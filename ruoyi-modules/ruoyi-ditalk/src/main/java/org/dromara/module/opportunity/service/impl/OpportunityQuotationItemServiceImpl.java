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
import org.dromara.module.opportunity.domain.OpportunityQuotationItem;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationItemBo;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationItemVo;
import org.dromara.module.opportunity.service.IOpportunityQuotationItemService;
import org.dromara.module.opportunity.mapper.OpportunityQuotationItemMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商机报价单明细Service业务层处理
 *
 * @author weidixian
 * @date 2025-09-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpportunityQuotationItemServiceImpl implements IOpportunityQuotationItemService {

    private final OpportunityQuotationItemMapper baseMapper;

    /**
     * 查询商机报价单明细
     *
     * @param id 主键
     * @return 商机报价单明细
     */
    @Override
    @Cacheable(cacheNames = CacheNames.OpportunityQuotationItem, key = "#id")
    public OpportunityQuotationItemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商机报价单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机报价单明细分页列表
     */
    @Override
    public TableDataInfo<OpportunityQuotationItemVo> queryPageList(OpportunityQuotationItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityQuotationItem> lqw = buildQueryWrapper(bo);
        Page<OpportunityQuotationItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商机报价单明细列表
     *
     * @param bo 查询条件
     * @return 商机报价单明细列表
     */
    @Override
    public List<OpportunityQuotationItemVo> queryList(OpportunityQuotationItemBo bo) {
        LambdaQueryWrapper<OpportunityQuotationItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OpportunityQuotationItem> buildQueryWrapper(OpportunityQuotationItemBo bo) {
        LambdaQueryWrapper<OpportunityQuotationItem> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, OpportunityQuotationItem::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<OpportunityQuotationItem> buildWrapper(OpportunityQuotationItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpportunityQuotationItem> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(OpportunityQuotationItem::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            OpportunityQuotationItem::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getOpportunityId() != null, OpportunityQuotationItem::getOpportunityId, bo.getOpportunityId());
        lqw.eq(bo.getQuotationId() != null, OpportunityQuotationItem::getQuotationId, bo.getQuotationId());
        lqw.eq(bo.getShopId() != null, OpportunityQuotationItem::getShopId, bo.getShopId());
        lqw.eq(bo.getCustomerId() != null, OpportunityQuotationItem::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getGoodsSnapshotId() != null, OpportunityQuotationItem::getGoodsSnapshotId, bo.getGoodsSnapshotId());
        lqw.eq(bo.getSkuId() != null, OpportunityQuotationItem::getSkuId, bo.getSkuId());
        lqw.like(StringUtils.isNotBlank(bo.getSkuSn()), OpportunityQuotationItem::getSkuSn, bo.getSkuSn());
        return lqw;
    }

    /**
     * 新增商机报价单明细
     *
     * @param bo 商机报价单明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(OpportunityQuotationItemBo bo) {
        OpportunityQuotationItem add = MapstructUtils.convert(bo, OpportunityQuotationItem.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商机报价单明细
     *
     * @param bo 商机报价单明细
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityQuotationItem, key = "#bo.id")
    public Boolean updateByBo(OpportunityQuotationItemBo bo) {
        OpportunityQuotationItem update = MapstructUtils.convert(bo, OpportunityQuotationItem.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OpportunityQuotationItem entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商机报价单明细
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityQuotationItem, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商机报价单明细信息
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
     * 通过ID分页查询商机报价单明细列表
     *
     * @param bo 查询条件
     * @return 商机报价单明细列表
     */
    @Override
    public List<OpportunityQuotationItemVo> queryList(OpportunityQuotationItemBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityQuotationItem> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, OpportunityQuotationItem::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
