package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.validate.BatchGroup;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.IGoodsSkuHandler;
import org.dromara.module.goods.domain.bo.GoodsSkuBatchBo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品SKU
 *
 * @author weidixian
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/goods/sku")
public class AppGoodsSkuController extends BaseController {

    private final IGoodsSkuHandler goodsSkuHandler;

    /**
     * 批量添加、修改商品SKU
     */
    @SaCheckPermission("goods:sku:edit")
    @Log(title = "商品SKU", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> save(@Validated(BatchGroup.class) @RequestBody GoodsSkuBatchBo bo) {
        return toAjax(goodsSkuHandler.batchUpdateByBo(bo));
    }
}
