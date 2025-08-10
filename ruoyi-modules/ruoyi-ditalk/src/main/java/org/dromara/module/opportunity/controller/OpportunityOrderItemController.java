package org.dromara.module.opportunity.controller;

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
import org.dromara.module.opportunity.domain.vo.OpportunityOrderItemVo;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemBo;
import org.dromara.module.opportunity.service.IOpportunityOrderItemService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机商品
 *
 * @author weidixian
 * @date 2025-08-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/orderItem")
public class OpportunityOrderItemController extends BaseController {

    private final IOpportunityOrderItemService opportunityOrderItemService;

    /**
     * 查询商机商品列表
     */
    @SaCheckPermission("opportunity:orderItem:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityOrderItemVo> list(OpportunityOrderItemBo bo, PageQuery pageQuery) {
        return opportunityOrderItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机商品列表
     */
    @SaCheckPermission("opportunity:orderItem:export")
    @Log(title = "商机商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityOrderItemBo bo, HttpServletResponse response) {
        List<OpportunityOrderItemVo> list = opportunityOrderItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机商品", OpportunityOrderItemVo.class, response);
    }

    /**
     * 获取商机商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:orderItem:query")
    @GetMapping("/{id}")
    public R<OpportunityOrderItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(opportunityOrderItemService.queryById(id));
    }

    /**
     * 新增商机商品
     */
    @SaCheckPermission("opportunity:orderItem:add")
    @Log(title = "商机商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityOrderItemBo bo) {
        return toAjax(opportunityOrderItemService.insertByBo(bo));
    }

    /**
     * 修改商机商品
     */
    @SaCheckPermission("opportunity:orderItem:edit")
    @Log(title = "商机商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityOrderItemBo bo) {
        return toAjax(opportunityOrderItemService.updateByBo(bo));
    }

    /**
     * 删除商机商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:orderItem:remove")
    @Log(title = "商机商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(opportunityOrderItemService.deleteWithValidByIds(List.of(ids), true));
    }
}
