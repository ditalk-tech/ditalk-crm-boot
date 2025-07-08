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
import org.dromara.module.goods.domain.vo.GoodsCategoryVo;
import org.dromara.module.goods.domain.bo.GoodsCategoryBo;
import org.dromara.module.goods.service.IGoodsCategoryService;

/**
 * 商品分类
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/category")
public class GoodsCategoryController extends BaseController {

    private final IGoodsCategoryService goodsCategoryService;

    /**
     * 查询商品分类列表
     */
    @SaCheckPermission("goods:category:list")
    @GetMapping("/list")
    public R<List<GoodsCategoryVo>> list(GoodsCategoryBo bo) {
        List<GoodsCategoryVo> list = goodsCategoryService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出商品分类列表
     */
    @SaCheckPermission("goods:category:export")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsCategoryBo bo, HttpServletResponse response) {
        List<GoodsCategoryVo> list = goodsCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品分类", GoodsCategoryVo.class, response);
    }

    /**
     * 获取商品分类详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:category:query")
    @GetMapping("/{id}")
    public R<GoodsCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsCategoryService.queryById(id));
    }

    /**
     * 新增商品分类
     */
    @SaCheckPermission("goods:category:add")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsCategoryBo bo) {
        return toAjax(goodsCategoryService.insertByBo(bo));
    }

    /**
     * 修改商品分类
     */
    @SaCheckPermission("goods:category:edit")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsCategoryBo bo) {
        return toAjax(goodsCategoryService.updateByBo(bo));
    }

    /**
     * 删除商品分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:category:remove")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsCategoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
