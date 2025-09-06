package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.handler.IOpportunityQuotationItemHandler;
import org.dromara.module.goods.domain.vo.GoodsInfoSnapshotVo;
import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.service.IGoodsInfoSnapshotService;
import org.dromara.module.goods.service.IGoodsSkuService;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationBo;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationItemBo;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationItemVo;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;
import org.dromara.module.opportunity.service.IOpportunityQuotationItemService;
import org.dromara.module.opportunity.service.IOpportunityQuotationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商机报价单应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityQuotationItemHandlerImpl implements IOpportunityQuotationItemHandler {

    private final IOpportunityQuotationService opportunityQuotationService;
    private final IOpportunityQuotationItemService opportunityQuotationItemService;
    private final IGoodsInfoSnapshotService goodsInfoSnapshotService;
    private final IGoodsSkuService goodsSkuService;

    @Override
    @DSTransactional
    public Boolean add(OpportunityQuotationItemBo bo) {
        // 校验报价单信息，同时也是权限的校验
        OpportunityQuotationVo infoVo = opportunityQuotationService.queryById(bo.getQuotationId());
        if (infoVo == null) throw new UserException("报价单信息错误");
        // 查询商品SKU是否已存在，校验更新合法性
        OpportunityQuotationItemBo orderItemBo = new OpportunityQuotationItemBo();
        orderItemBo.setQuotationId(bo.getQuotationId());
        orderItemBo.setSkuId(bo.getSkuId());
        List<OpportunityQuotationItemVo> voList = opportunityQuotationItemService.queryList(orderItemBo);
        if (IterUtil.isNotEmpty(voList)) {
            throw new IllegalArgumentException("已存在相同的商品，不可重复添加");
        }
        OpportunityQuotationItemBo addBo = buildOrderItemBo(bo);
        ValidatorUtils.validate(addBo, AddGroup.class);
        boolean flag = opportunityQuotationItemService.insertByBo(addBo);
        if (!flag) throw new UserException("添加失败");
        // 更新报价单价格
        return updateQuotationPrice(bo.getQuotationId());
    }

    @Override
    @DSTransactional
    public Boolean edit(OpportunityQuotationItemBo bo) {
        // 校验报价单信息，同时也是权限的校验
        OpportunityQuotationVo infoVo = opportunityQuotationService.queryById(bo.getQuotationId());
        if (infoVo == null) throw new UserException("报价单信息错误");
        // 查询商品SKU是否已存在，校验更新合法性
        OpportunityQuotationItemBo orderItemBo = new OpportunityQuotationItemBo();
        orderItemBo.setQuotationId(bo.getQuotationId());
        orderItemBo.setSkuId(bo.getSkuId());
        List<OpportunityQuotationItemVo> voList = opportunityQuotationItemService.queryList(orderItemBo);
        if (IterUtil.isNotEmpty(voList)) {
            // 如果存在多个相同SKU的商品项，抛出异常
            if (voList.size() > 1) {
                throw new IllegalArgumentException("存在多个相同的商品，数据异常");
            }
            // 如果更新的不是当前商品，即更新了已存在的商品SKU，发生了覆盖更新，抛出异常
            if (!voList.get(0).getId().equals(bo.getId())) {
                throw new IllegalArgumentException("已存在相同的商品，不可覆盖更新");
            }
        }
        // 构建商机商品项BO对象
        OpportunityQuotationItemBo editBo = buildOrderItemBo(bo);
        ValidatorUtils.validate(editBo, EditGroup.class);
        Boolean flag = opportunityQuotationItemService.updateByBo(editBo);
        if (!flag) throw new UserException("更新失败");
        // 更新报价单价格
        return updateQuotationPrice(bo.getQuotationId());
    }

    @Override
    @DSTransactional
    public Boolean remove(List<Long> ids) {
        List<Long> quotationIdList = new ArrayList<>();
        for (Long id : ids) {
            OpportunityQuotationItemVo vo = opportunityQuotationItemService.queryById(id);
            if (vo == null || vo.getQuotationId() == null) {
                throw new UserException("删除操作失败，记录不存在");
            }
            OpportunityQuotationVo quotationVo = opportunityQuotationService.queryById(vo.getQuotationId());
            if (quotationVo == null) {
                throw new UserException("删除操作失败，未找到数据");
            }
            quotationIdList.add(vo.getQuotationId());
        }
        Boolean flag = opportunityQuotationItemService.deleteWithValidByIds(ids, true);
        if (!flag) throw new UserException("删除操作失败");
        //
        quotationIdList.forEach(quotationId -> updateQuotationPrice(quotationId));
        return true;
    }

    @DSTransactional
    private OpportunityQuotationItemBo buildOrderItemBo(OpportunityQuotationItemBo bo) {
        OpportunityQuotationVo quotationVo = opportunityQuotationService.queryById(bo.getQuotationId());
        if (quotationVo == null) throw new UserException("报价单信息错误");
        GoodsSkuVo goodsSkuVo = goodsSkuService.queryById(bo.getSkuId());
        if (goodsSkuVo == null) throw new UserException("商品SKU不存在");
        GoodsInfoSnapshotVo goodsInfoSnapshotVo = goodsInfoSnapshotService.queryLastByGoodsId(goodsSkuVo.getGoodsId());
        if (goodsInfoSnapshotVo == null) throw new UserException("商品信息不存在");
        OpportunityQuotationItemBo itemBo = new OpportunityQuotationItemBo();
        if (bo.getId() != null) {
            if (bo.getVersion() == null) throw new UserException("数据版本号不能为空");
            itemBo.setId(bo.getId());
            itemBo.setVersion(bo.getVersion());
        }
        itemBo.setOpportunityId(quotationVo.getOpportunityId());
        itemBo.setQuotationId(bo.getQuotationId());
        itemBo.setShopId(goodsSkuVo.getShopId());
        itemBo.setCustomerId(quotationVo.getCustomerId());
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
        itemBo.setUnitName(goodsSkuVo.getUnitName());
        itemBo.setUnitPrice(bo.getUnitPrice());
        itemBo.setQuantity(bo.getQuantity());
        itemBo.setTotalPrice(bo.getUnitPrice() * bo.getQuantity());
        itemBo.setDeliveryDate(bo.getDeliveryDate());
        return itemBo;
    }

    /**
     * 根据商品明细重新统计报价单价格信息
     */
    @DSTransactional
    private Boolean updateQuotationPrice(Long quotationId) {
        // 查询商品明细
        OpportunityQuotationItemBo itemBo = new OpportunityQuotationItemBo();
        itemBo.setQuotationId(quotationId);
        List<OpportunityQuotationItemVo> itemVoList = opportunityQuotationItemService.queryList(itemBo);
        if (IterUtil.isNotEmpty(itemVoList)) {
            OpportunityQuotationBo quotationBo = new OpportunityQuotationBo();
            quotationBo.setId(quotationId);
            quotationBo.setTotalSalePrice(0L);
            quotationBo.setTotalCostPrice(0L);
            quotationBo.setTotalOriginalPrice(0L);
            itemVoList.forEach(itemVo -> {
                quotationBo.setTotalSalePrice(quotationBo.getTotalSalePrice() + itemVo.getTotalPrice());
                quotationBo.setTotalCostPrice(quotationBo.getTotalCostPrice() + itemVo.getCostPrice() * itemVo.getQuantity());
                quotationBo.setTotalOriginalPrice(quotationBo.getTotalOriginalPrice() + itemVo.getOriginalPrice() * itemVo.getQuantity());
            });
            Boolean flag = opportunityQuotationService.updateByBo(quotationBo);
            if (!flag) throw new UserException("更新失败");
        }
        return true;
    }
}
