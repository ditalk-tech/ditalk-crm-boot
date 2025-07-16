package org.dromara.handler;

import org.dromara.module.goods.domain.bo.GoodsSkuBatchBo;

/**
 * GoodsSku应用接口
 *
 * @author weidixian
 */
public interface IGoodsSkuHandler {

    Boolean batchUpdateByBo(GoodsSkuBatchBo bo);
}
