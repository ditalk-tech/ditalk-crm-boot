package org.dromara.handler;

import org.dromara.module.goods.domain.bo.GoodsInfoBo;

/**
 * 商品信息应用接口
 *
 * @author weidixian
 */
public interface IGoodsInfoHandler {

    /**
     * 新增商品信息
     *
     * @param bo 商品信息
     * @return 是否新增成功
     */
    Boolean add(GoodsInfoBo bo);

    /**
     * 修改商品信息
     *
     * @param bo 商品信息
     * @return 是否修改成功
     */
    Boolean edit(GoodsInfoBo bo);
}
