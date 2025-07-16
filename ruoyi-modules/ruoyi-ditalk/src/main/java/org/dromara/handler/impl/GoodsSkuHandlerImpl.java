package org.dromara.handler.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CommonConstants;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.ObjectUtils;
import org.dromara.common.utils.ObjectUtil;
import org.dromara.handler.IGoodsSkuHandler;
import org.dromara.module.goods.domain.bo.GoodsInfoBo;
import org.dromara.module.goods.domain.bo.GoodsSkuBatchBo;
import org.dromara.module.goods.domain.bo.GoodsSkuBo;
import org.dromara.module.goods.domain.vo.GoodsInfoVo;
import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.service.IGoodsInfoService;
import org.dromara.module.goods.service.IGoodsSkuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GoodsSku应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsSkuHandlerImpl implements IGoodsSkuHandler {

    private final IGoodsSkuService goodsSkuService;
    private final IGoodsInfoService goodsInfoService;

    private void updateGoodsInfo(Long goodsId, Long availableStock, Long minPrice) {
        GoodsInfoVo goodsInfoVo = goodsInfoService.queryById(goodsId);
        // 更新商品信息表中的 可用库存、最低价
        GoodsInfoBo goodsInfoBo = new GoodsInfoBo();
        goodsInfoBo.setId(goodsInfoVo.getId());
        goodsInfoBo.setVersion(goodsInfoVo.getVersion());
        goodsInfoBo.setAvailableStock(availableStock);
        goodsInfoBo.setMinPrice(minPrice);
        Boolean flag = goodsInfoService.updateByBo(goodsInfoBo);
        if (!flag) {
            throw new UserException("更新商品信息失败，请检查数据内容");
        }
    }

    /**
     * 批量更新商品SKU<br>
     * 【1】如果原SKU存在于BO中，使用BO记录内容进行更新，同时移除BO该记录<br>
     * 【2】如果原SKU不存在于BO中，删除该数据<br>
     * 【3】BO中剩下的记录进行添加操作<br>
     * 【4】通过新的SKU数据更新商品信息表中的 可用库存、最低价<br>
     *
     * @param bo 商品SKU批量业务对象
     * @return 是否成功
     */
    @Override
    @DSTransactional
    public Boolean batchUpdateByBo(GoodsSkuBatchBo bo) {
        GoodsSkuBo queryBo = new GoodsSkuBo();
        queryBo.setGoodsId(bo.getGoodsId());
        queryBo.setState(CommonConstants.AVAILABLE);
        List<GoodsSkuBo> goodsSkuBos = bo.getGoodsSkuBos();
        List<GoodsSkuVo> goodsSkuVoList = goodsSkuService.queryList(queryBo);
        if (ObjectUtils.isEmpty(goodsSkuBos)) { // 如果提交为空数据集，清空现有SKU数据，结束整个处理
            goodsSkuVoList.stream()
                .map(GoodsSkuVo::getId)
                .forEach(goodsSkuService::deleteById);
            // 【4】通过新的SKU数据更新商品信息表中的 可用库存、最低价
            this.updateGoodsInfo(bo.getGoodsId(), 0L, 0L);
            return true;
        }
        Boolean flag; // SQL执行结果标志
        Boolean isDelete = true; // 是否删除原SKU数据标志，如果原SKU不存在于BO中则删除该数据
        if (ObjectUtils.isNotEmpty(goodsSkuVoList)) {
            for (GoodsSkuVo skuVo : goodsSkuVoList) {
                // 【1】如果原SKU存在于BO中，使用BO记录内容进行更新，同时移除BO该记录
                for (GoodsSkuBo skuBo : goodsSkuBos) {
                    if (ObjectUtil.compareObjects(JSON.parse(skuBo.getSpecJson()), JSON.parse(skuVo.getSpecJson()))) {
                        skuBo.setId(skuVo.getId()); // 设置ID以便更新
                        flag = goodsSkuService.updateByBo(skuBo);
                        goodsSkuBos.remove(skuBo); // 从BO中移除已更新的记录 （提高后续处理效率）
                        if (!flag) throw new UserException("更新SKU失败，请检查" + skuVo.getId() + "数据");
                        isDelete = false; // 标记不删除原SKU数据
                        break;
                    }
                }
                // 【2】如果原SKU不存在于BO中，删除该数据
                if (isDelete) {
                    flag = goodsSkuService.deleteById(skuVo.getId());
                    if (!flag) throw new UserException("删除SKU失败，请检查" + skuVo.getId() + "数据");
                } else {
                    isDelete = true; // 重置标记
                }
            }
        }
        // 【3】最后BO中剩下的记录进行添加操作
        for (GoodsSkuBo sku : goodsSkuBos) {
            sku.setShopId(bo.getShopId());
            sku.setGoodsId(bo.getGoodsId());
            flag = goodsSkuService.insertByBo(sku);
            if (!flag) throw new UserException("新增SKU失败，请检查" + sku.getSpecJson() + "的数据内容");
        }
        // 【4】通过新的SKU数据更新商品信息表中的 可用库存、最低价
        List<GoodsSkuVo> currentSkuVoList = goodsSkuService.queryList(queryBo);
        if (ObjectUtils.isNotEmpty(currentSkuVoList)) {
            Long availableStock = currentSkuVoList.stream()
                .mapToLong(GoodsSkuVo::getAvailableStock)
                .sum();
            Long minPrice = currentSkuVoList.stream()
                .map(GoodsSkuVo::getSalePrice)
                .min(Long::compareTo)
                .orElse(0L);
            this.updateGoodsInfo(bo.getGoodsId(), availableStock, minPrice);
        }
        return true;
    }

}
