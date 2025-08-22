package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
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
        // 校验商机信息，同时也是权限的校验
        OpportunityInfoVo infoVo = opportunityInfoService.queryById(bo.getOpportunityId());
        if (infoVo == null) throw new UserException("商机信息错误");
        // 查询商品SKU是否已存在，校验更新合法性
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
        // 校验商机信息，同时也是权限的校验
        OpportunityInfoVo infoVo = opportunityInfoService.queryById(bo.getOpportunityId());
        if (infoVo == null) throw new UserException("商机信息错误");
        // 查询商品SKU是否已存在，校验更新合法性
        OpportunityOrderItemBo orderItemBo = new OpportunityOrderItemBo();
        orderItemBo.setOpportunityId(bo.getOpportunityId());
        orderItemBo.setSkuId(bo.getSkuId());
        List<OpportunityOrderItemVo> opportunityOrderItemVoList = opportunityOrderItemService.queryList(orderItemBo);
        if (IterUtil.isNotEmpty(opportunityOrderItemVoList)) {
            // 如果存在多个相同SKU的商品项，抛出异常
            if (opportunityOrderItemVoList.size() > 1) {
                throw new IllegalArgumentException("已存多个相同的商品，数据异常");
            }
            // 如果更新的不是当前商品，即更新了已存在的商品SKU，发生了覆盖更新，抛出异常
            if (!opportunityOrderItemVoList.get(0).getId().equals(bo.getId())) {
                throw new IllegalArgumentException("已存在相同的商品，不可覆盖更新");
            }
        }
        // 构建商机商品项BO对象
        OpportunityOrderItemBo itemBo = buildOrderItemBo(bo);
        ValidatorUtils.validate(itemBo, EditGroup.class);
        return opportunityOrderItemService.updateByBo(itemBo);
    }

    private OpportunityOrderItemBo buildOrderItemBo(OpportunityOrderItemTinyBo bo) {
        GoodsSkuVo goodsSkuVo = goodsSkuService.queryById(bo.getSkuId());
        if (goodsSkuVo == null) throw new UserException("商品SKU不存在");
        OpportunityInfoVo opportunityInfoVo = opportunityInfoService.queryById(bo.getOpportunityId());
        if (opportunityInfoVo == null) throw new UserException("商机信息不存在");
        GoodsInfoSnapshotVo goodsInfoSnapshotVo = goodsInfoSnapshotService.queryLastByGoodsId(goodsSkuVo.getGoodsId());
        if (goodsInfoSnapshotVo == null) throw new UserException("商品信息不存在");
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
