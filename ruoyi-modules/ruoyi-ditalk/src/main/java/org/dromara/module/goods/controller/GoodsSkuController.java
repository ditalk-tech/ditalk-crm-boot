package org.dromara.module.goods.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.validate.BatchGroup;
import org.dromara.handler.IGoodsSkuHandler;
import org.dromara.module.goods.domain.bo.GoodsSkuBatchBo;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.domain.bo.GoodsSkuBo;
import org.dromara.module.goods.service.IGoodsSkuService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品SKU
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/sku")
public class GoodsSkuController extends BaseController {

    private final IGoodsSkuService goodsSkuService;
    private final IGoodsSkuHandler goodsSkuHandler;

    /**
     * 查询商品SKU列表
     */
    @SaCheckPermission("goods:sku:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsSkuVo> list(GoodsSkuBo bo, PageQuery pageQuery) {
        return goodsSkuService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品SKU列表
     */
    @SaCheckPermission("goods:sku:export")
    @Log(title = "商品SKU", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsSkuBo bo, HttpServletResponse response) {
        List<GoodsSkuVo> list = goodsSkuService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品SKU", GoodsSkuVo.class, response);
    }

    /**
     * 获取商品SKU详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:sku:query")
    @GetMapping("/{id}")
    public R<GoodsSkuVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsSkuService.queryById(id));
    }

    /**
     * 新增商品SKU
     */
    @SaCheckPermission("goods:sku:add")
    @Log(title = "商品SKU", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsSkuBo bo) {
        return toAjax(goodsSkuService.insertByBo(bo));
    }

    /**
     * 修改商品SKU
     */
    @SaCheckPermission("goods:sku:edit")
    @Log(title = "商品SKU", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsSkuBo bo) {
        return toAjax(goodsSkuService.updateByBo(bo));
    }

    /**
     * 删除商品SKU
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:sku:remove")
    @Log(title = "商品SKU", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsSkuService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 批量添加、修改商品SKU
     */
    @SaCheckPermission("goods:sku:edit")
    @Log(title = "商品SKU", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/batch")
    public R<Void> batchEdit(@Validated(BatchGroup.class) @RequestBody GoodsSkuBatchBo bo) {
        return toAjax(goodsSkuHandler.batchUpdateByBo(bo));
    }
}
