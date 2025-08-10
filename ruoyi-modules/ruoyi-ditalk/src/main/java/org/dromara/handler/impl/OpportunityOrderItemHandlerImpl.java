package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.handler.IOpportunityOrderItemHandler;
import org.dromara.module.goods.domain.vo.GoodsInfoSnapshotVo;
import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.service.IGoodsInfoSnapshotService;
import org.dromara.module.goods.service.IGoodsSkuService;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemBo;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemTinyBo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.domain.vo.OpportunityOrderItemVo;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
import org.dromara.module.opportunity.service.IOpportunityOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动信息应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityOrderItemHandlerImpl implements IOpportunityOrderItemHandler {

    private final IOpportunityInfoService opportunityInfoService;
    private final IOpportunityOrderItemService opportunityOrderItemService;
    private final IGoodsInfoSnapshotService goodsInfoSnapshotService;
    private final IGoodsSkuService goodsSkuService;

    @Override
    @DSTransactional
    public Boolean add(OpportunityOrderItemTinyBo bo) {
        // 重复数据校验：不允许添加相同的商品SKU
        OpportunityOrderItemBo orderItemBo = new OpportunityOrderItemBo();
        orderItemBo.setOpportunityId(bo.getOpportunityId());
        orderItemBo.setSkuId(bo.getSkuId());
        List<OpportunityOrderItemVo> opportunityOrderItemVoList = opportunityOrderItemService.queryList(orderItemBo);
        if (IterUtil.isNotEmpty(opportunityOrderItemVoList)) {
            throw new IllegalArgumentException("已存在相同的商品，不可重复添加");
        }
        OpportunityOrderItemBo itemBo = buildOrderItemBo(bo);
        ValidatorUtils.validate(itemBo, AddGroup.class);
        return opportunityOrderItemService.insertByBo(itemBo);
    }

    @Override
    @DSTransactional
    public Boolean edit(OpportunityOrderItemTinyBo bo) {
        // 重复数据校验：不能覆盖更新
        OpportunityOrderItemBo orderItemBo = new OpportunityOrderItemBo();
        orderItemBo.setOpportunityId(bo.getOpportunityId());
        orderItemBo.setSkuId(bo.getSkuId());
        List<OpportunityOrderItemVo> opportunityOrderItemVoList = opportunityOrderItemService.queryList(orderItemBo);
        if (IterUtil.isNotEmpty(opportunityOrderItemVoList)) {
            // 如果是编辑当前商品，则直接更新
            if (opportunityOrderItemVoList.size() == 1 && opportunityOrderItemVoList.get(0).getId().equals(bo.getId())) {
                return opportunityOrderItemService.updateByBo(buildOrderItemBo(bo));
            } else {
                // 更新已存在的商品SKU时，则抛出异常
                throw new IllegalArgumentException("已存在相同的商品，不可覆盖更新");
            }
        }
        OpportunityOrderItemBo itemBo = buildOrderItemBo(bo);
        ValidatorUtils.validate(itemBo, EditGroup.class);
        return opportunityOrderItemService.updateByBo(itemBo);
    }

    private OpportunityOrderItemBo buildOrderItemBo(OpportunityOrderItemTinyBo bo) {
        OpportunityInfoVo opportunityInfoVo = opportunityInfoService.queryById(bo.getOpportunityId());
        GoodsSkuVo goodsSkuVo = goodsSkuService.queryById(bo.getSkuId());
        GoodsInfoSnapshotVo goodsInfoSnapshotVo = goodsInfoSnapshotService.queryLastByGoodsId(goodsSkuVo.getGoodsId());
        OpportunityOrderItemBo itemBo = new OpportunityOrderItemBo();
        if (bo.getId() != null) {
            itemBo.setId(bo.getId());
        }
        if (bo.getVersion() != null) {
            itemBo.setVersion(bo.getVersion());
        }
        itemBo.setOpportunityId(bo.getOpportunityId());
        itemBo.setShopId(goodsSkuVo.getShopId());
        itemBo.setCustomerId(opportunityInfoVo.getCustomerId());
        itemBo.setGoodsSnapshotId(goodsInfoSnapshotVo.getId());
        itemBo.setSkuId(bo.getSkuId());
        itemBo.setSkuSn(goodsSkuVo.getSkuSn());
        itemBo.setMainPic(goodsSkuVo.getMainPic());
        itemBo.setSpecJson(goodsSkuVo.getSpecJson());
        itemBo.setSalePrice(goodsSkuVo.getSalePrice());
        itemBo.setOriginalPrice(goodsSkuVo.getOriginalPrice());
        itemBo.setCostPrice(goodsSkuVo.getCostPrice());
        itemBo.setWeight(goodsSkuVo.getWeight());
        itemBo.setVolume(goodsSkuVo.getVolume());
        itemBo.setUnitPrice(bo.getUnitPrice());
        itemBo.setQuantity(bo.getQuantity());
        itemBo.setTotalPrice(bo.getUnitPrice() * bo.getQuantity());
        return itemBo;
    }
}
