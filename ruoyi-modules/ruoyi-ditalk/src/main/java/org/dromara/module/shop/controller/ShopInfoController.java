package org.dromara.module.shop.controller;

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
import org.dromara.module.shop.domain.vo.ShopInfoVo;
import org.dromara.module.shop.domain.bo.ShopInfoBo;
import org.dromara.module.shop.service.IShopInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 店铺信息
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/shop/info")
public class ShopInfoController extends BaseController {

    private final IShopInfoService shopInfoService;

    /**
     * 查询店铺信息列表
     */
    @SaCheckPermission("shop:info:list")
    @GetMapping("/list")
    public TableDataInfo<ShopInfoVo> list(ShopInfoBo bo, PageQuery pageQuery) {
        return shopInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出店铺信息列表
     */
    @SaCheckPermission("shop:info:export")
    @Log(title = "店铺信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ShopInfoBo bo, HttpServletResponse response) {
        List<ShopInfoVo> list = shopInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "店铺信息", ShopInfoVo.class, response);
    }

    /**
     * 获取店铺信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("shop:info:query")
    @GetMapping("/{id}")
    public R<ShopInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(shopInfoService.queryById(id));
    }

    /**
     * 新增店铺信息
     */
    @SaCheckPermission("shop:info:add")
    @Log(title = "店铺信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ShopInfoBo bo) {
        return toAjax(shopInfoService.insertByBo(bo));
    }

    /**
     * 修改店铺信息
     */
    @SaCheckPermission("shop:info:edit")
    @Log(title = "店铺信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ShopInfoBo bo) {
        return toAjax(shopInfoService.updateByBo(bo));
    }

    /**
     * 删除店铺信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("shop:info:remove")
    @Log(title = "店铺信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(shopInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
