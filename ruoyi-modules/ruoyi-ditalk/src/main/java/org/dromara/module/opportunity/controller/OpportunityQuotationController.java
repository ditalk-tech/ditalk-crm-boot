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
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationBo;
import org.dromara.module.opportunity.service.IOpportunityQuotationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机报价单
 *
 * @author weidixian
 * @date 2025-09-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/quotation")
public class OpportunityQuotationController extends BaseController {

    private final IOpportunityQuotationService opportunityQuotationService;

    /**
     * 查询商机报价单列表
     */
    @SaCheckPermission("opportunity:quotation:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityQuotationVo> list(OpportunityQuotationBo bo, PageQuery pageQuery) {
        return opportunityQuotationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机报价单列表
     */
    @SaCheckPermission("opportunity:quotation:export")
    @Log(title = "商机报价单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityQuotationBo bo, HttpServletResponse response) {
        List<OpportunityQuotationVo> list = opportunityQuotationService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机报价单", OpportunityQuotationVo.class, response);
    }

    /**
     * 获取商机报价单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:quotation:query")
    @GetMapping("/{id}")
    public R<OpportunityQuotationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(opportunityQuotationService.queryById(id));
    }

    /**
     * 新增商机报价单
     */
    @SaCheckPermission("opportunity:quotation:add")
    @Log(title = "商机报价单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityQuotationBo bo) {
        return toAjax(opportunityQuotationService.insertByBo(bo));
    }

    /**
     * 修改商机报价单
     */
    @SaCheckPermission("opportunity:quotation:edit")
    @Log(title = "商机报价单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityQuotationBo bo) {
        return toAjax(opportunityQuotationService.updateByBo(bo));
    }

    /**
     * 删除商机报价单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:quotation:remove")
    @Log(title = "商机报价单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(opportunityQuotationService.deleteWithValidByIds(List.of(ids), true));
    }
}
