package org.dromara.module.goods.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.module.goods.domain.bo.GoodsSkuSpecBo;
import org.dromara.module.goods.domain.vo.GoodsCategoryVo;
import org.dromara.module.goods.domain.vo.GoodsSkuSpecVo;
import org.dromara.module.goods.service.IGoodsCategoryService;
import org.dromara.module.goods.service.IGoodsSkuSpecService;
import org.dromara.module.shop.domain.vo.ShopInfoVo;
import org.dromara.module.shop.service.IShopInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SKU规格
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/skuSpec")
public class GoodsSkuSpecController extends BaseController {

    private final IGoodsSkuSpecService goodsSkuSpecService;
    private final IShopInfoService shopInfoService;
    private final IGoodsCategoryService goodsCategoryService;

    private void extendVo(GoodsSkuSpecVo vo) {
        if (vo == null) {
            return;
        }
        // 店铺信息
        ShopInfoVo shopInfoVo = shopInfoService.queryById(vo.getShopId());
        if (shopInfoVo != null) vo.setShopName(shopInfoVo.getName());
        // 商品分类
        GoodsCategoryVo goodsCategoryVo = goodsCategoryService.queryById(vo.getCategoryId());
        if (goodsCategoryVo != null) vo.setCategoryName(goodsCategoryVo.getName());
    }

    /**
     * 查询SKU规格列表
     */
    @SaCheckPermission("goods:skuSpec:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsSkuSpecVo> list(GoodsSkuSpecBo bo, PageQuery pageQuery) {
        TableDataInfo<GoodsSkuSpecVo> tableDataInfo = goodsSkuSpecService.queryPageList(bo, pageQuery);
        if (tableDataInfo.getRows() != null && !tableDataInfo.getRows().isEmpty()) {
            tableDataInfo.getRows().forEach(r -> {
                extendVo(r);
            });
        }
        return tableDataInfo;
    }

    /**
     * 导出SKU规格列表
     */
    @SaCheckPermission("goods:skuSpec:export")
    @Log(title = "SKU规格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsSkuSpecBo bo, HttpServletResponse response) {
        List<GoodsSkuSpecVo> list = goodsSkuSpecService.queryList(bo);
        ExcelUtil.exportExcel(list, "SKU规格", GoodsSkuSpecVo.class, response);
    }

    /**
     * 获取SKU规格详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:skuSpec:query")
    @GetMapping("/{id}")
    public R<GoodsSkuSpecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsSkuSpecService.queryById(id));
    }

    /**
     * 新增SKU规格
     */
    @SaCheckPermission("goods:skuSpec:add")
    @Log(title = "SKU规格", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsSkuSpecBo bo) {
        return toAjax(goodsSkuSpecService.insertByBo(bo));
    }

    /**
     * 修改SKU规格
     */
    @SaCheckPermission("goods:skuSpec:edit")
    @Log(title = "SKU规格", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsSkuSpecBo bo) {
        return toAjax(goodsSkuSpecService.updateByBo(bo));
    }

    /**
     * 删除SKU规格
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:skuSpec:remove")
    @Log(title = "SKU规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsSkuSpecService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询SKU规格列表
     *
     * @param shopId 店铺ID
     * @param categoryId 分类ID
     */
    @SaCheckPermission("goods:skuSpec:list")
    @GetMapping("/{shopId}/{categoryId}")
    public R<List<GoodsSkuSpecVo>> queryByShopIdAndCategoryId(
        @NotNull(message = "店铺不能为空") @PathVariable Long shopId,
        @NotNull(message = "分类不能为空") @PathVariable Long categoryId) {
        return R.ok(goodsSkuSpecService.queryByShopIdAndCategoryId(shopId, categoryId));
    }

}
