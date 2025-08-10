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
import org.dromara.module.opportunity.domain.OpportunityOrderItem;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemBo;
import org.dromara.module.opportunity.domain.vo.OpportunityOrderItemVo;
import org.dromara.module.opportunity.service.IOpportunityOrderItemService;
import org.dromara.module.opportunity.mapper.OpportunityOrderItemMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商机商品Service业务层处理
 *
 * @author weidixian
 * @date 2025-08-10
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpportunityOrderItemServiceImpl implements IOpportunityOrderItemService {

    private final OpportunityOrderItemMapper baseMapper;

    /**
     * 查询商机商品
     *
     * @param id 主键
     * @return 商机商品
     */
    @Override
    @Cacheable(cacheNames = CacheNames.OpportunityOrderItem, key = "#id")
    public OpportunityOrderItemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商机商品列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机商品分页列表
     */
    @Override
    public TableDataInfo<OpportunityOrderItemVo> queryPageList(OpportunityOrderItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityOrderItem> lqw = buildQueryWrapper(bo);
        Page<OpportunityOrderItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商机商品列表
     *
     * @param bo 查询条件
     * @return 商机商品列表
     */
    @Override
    public List<OpportunityOrderItemVo> queryList(OpportunityOrderItemBo bo) {
        LambdaQueryWrapper<OpportunityOrderItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OpportunityOrderItem> buildQueryWrapper(OpportunityOrderItemBo bo) {
        LambdaQueryWrapper<OpportunityOrderItem> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, OpportunityOrderItem::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<OpportunityOrderItem> buildWrapper(OpportunityOrderItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpportunityOrderItem> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(OpportunityOrderItem::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            OpportunityOrderItem::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getOpportunityId() != null, OpportunityOrderItem::getOpportunityId, bo.getOpportunityId());
        lqw.eq(bo.getShopId() != null, OpportunityOrderItem::getShopId, bo.getShopId());
        lqw.eq(bo.getCustomerId() != null, OpportunityOrderItem::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getGoodsSnapshotId() != null, OpportunityOrderItem::getGoodsSnapshotId, bo.getGoodsSnapshotId());
        lqw.eq(bo.getSkuId() != null, OpportunityOrderItem::getSkuId, bo.getSkuId());
        lqw.eq(StringUtils.isNotBlank(bo.getSkuSn()), OpportunityOrderItem::getSkuSn, bo.getSkuSn());
        return lqw;
    }

    /**
     * 新增商机商品
     *
     * @param bo 商机商品
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(OpportunityOrderItemBo bo) {
        OpportunityOrderItem add = MapstructUtils.convert(bo, OpportunityOrderItem.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商机商品
     *
     * @param bo 商机商品
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityOrderItem, key = "#bo.id")
    public Boolean updateByBo(OpportunityOrderItemBo bo) {
        OpportunityOrderItem update = MapstructUtils.convert(bo, OpportunityOrderItem.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OpportunityOrderItem entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商机商品
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityOrderItem, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商机商品信息
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
     * 通过ID分页查询商机商品列表
     *
     * @param bo 查询条件
     * @return 商机商品列表
     */
    @Override
    public List<OpportunityOrderItemVo> queryList(OpportunityOrderItemBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityOrderItem> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, OpportunityOrderItem::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
