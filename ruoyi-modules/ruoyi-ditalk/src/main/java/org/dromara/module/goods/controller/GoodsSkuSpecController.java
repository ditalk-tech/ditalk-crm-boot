package org.dromara.module.goods.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.dromara.module.goods.domain.vo.GoodsSkuSpecVo;
import org.dromara.module.goods.domain.bo.GoodsSkuSpecBo;
import org.dromara.module.goods.service.IGoodsSkuSpecService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

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

    /**
     * 查询SKU规格列表
     */
    @SaCheckPermission("goods:skuSpec:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsSkuSpecVo> list(GoodsSkuSpecBo bo, PageQuery pageQuery) {
        return goodsSkuSpecService.queryPageList(bo, pageQuery);
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
}
