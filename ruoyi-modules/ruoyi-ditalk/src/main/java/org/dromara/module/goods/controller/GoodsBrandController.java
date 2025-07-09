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
import org.dromara.module.goods.domain.vo.GoodsBrandVo;
import org.dromara.module.goods.domain.bo.GoodsBrandBo;
import org.dromara.module.goods.service.IGoodsBrandService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品品牌信息
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/brand")
public class GoodsBrandController extends BaseController {

    private final IGoodsBrandService goodsBrandService;

    /**
     * 查询商品品牌信息列表
     */
    @SaCheckPermission("goods:brand:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsBrandVo> list(GoodsBrandBo bo, PageQuery pageQuery) {
        return goodsBrandService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品品牌信息列表
     */
    @SaCheckPermission("goods:brand:export")
    @Log(title = "商品品牌信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsBrandBo bo, HttpServletResponse response) {
        List<GoodsBrandVo> list = goodsBrandService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品品牌信息", GoodsBrandVo.class, response);
    }

    /**
     * 获取商品品牌信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:brand:query")
    @GetMapping("/{id}")
    public R<GoodsBrandVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsBrandService.queryById(id));
    }

    /**
     * 新增商品品牌信息
     */
    @SaCheckPermission("goods:brand:add")
    @Log(title = "商品品牌信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsBrandBo bo) {
        return toAjax(goodsBrandService.insertByBo(bo));
    }

    /**
     * 修改商品品牌信息
     */
    @SaCheckPermission("goods:brand:edit")
    @Log(title = "商品品牌信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsBrandBo bo) {
        return toAjax(goodsBrandService.updateByBo(bo));
    }

    /**
     * 删除商品品牌信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:brand:remove")
    @Log(title = "商品品牌信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsBrandService.deleteWithValidByIds(List.of(ids), true));
    }
}
