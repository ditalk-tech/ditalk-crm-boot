package org.dromara.module.opportunity.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.handler.IOpportunityQuotationItemHandler;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;
import org.dromara.module.opportunity.service.IOpportunityQuotationService;
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
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationItemVo;
import org.dromara.module.opportunity.domain.bo.OpportunityQuotationItemBo;
import org.dromara.module.opportunity.service.IOpportunityQuotationItemService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机报价单明细
 *
 * @author weidixian
 * @date 2025-09-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/quotationItem")
public class OpportunityQuotationItemController extends BaseController {

    private final IOpportunityQuotationService opportunityQuotationService;
    private final IOpportunityQuotationItemService opportunityQuotationItemService;
    private final IOpportunityQuotationItemHandler opportunityQuotationItemHandler;

    /**
     * 查询商机报价单明细列表
     */
    @SaCheckPermission("opportunity:quotationItem:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityQuotationItemVo> list(OpportunityQuotationItemBo bo, PageQuery pageQuery) {
        if (bo.getQuotationId() == null)
            throw new UserException("须先指定报价单");
        return opportunityQuotationItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机报价单明细列表
     */
    @SaCheckPermission("opportunity:quotationItem:export")
    @Log(title = "商机报价单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityQuotationItemBo bo, HttpServletResponse response) {
        if (bo.getQuotationId() == null)
            throw new UserException("须先指定报价单");
        List<OpportunityQuotationItemVo> list = opportunityQuotationItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机报价单明细", OpportunityQuotationItemVo.class, response);
    }

    /**
     * 获取商机报价单明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:quotationItem:query")
    @GetMapping("/{id}")
    public R<OpportunityQuotationItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        OpportunityQuotationItemVo itemVo = opportunityQuotationItemService.queryById(id);
        if (itemVo != null && itemVo.getQuotationId() != null) {
            OpportunityQuotationVo quotationVo = opportunityQuotationService.queryById(itemVo.getQuotationId());
            if (quotationVo == null) {
                return R.fail("未找到对应的报价单明细");
            }
        }
        return R.ok(itemVo);
    }

    /**
     * 新增商机报价单明细
     */
    @SaCheckPermission("opportunity:quotationItem:add")
    @Log(title = "商机报价单明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityQuotationItemBo bo) {
        return toAjax(opportunityQuotationItemHandler.add(bo));
    }

    /**
     * 修改商机报价单明细
     */
    @SaCheckPermission("opportunity:quotationItem:edit")
    @Log(title = "商机报价单明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityQuotationItemBo bo) {
        return toAjax(opportunityQuotationItemHandler.edit(bo));
    }

    /**
     * 删除商机报价单明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:quotationItem:remove")
    @Log(title = "商机报价单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
//        return toAjax(opportunityQuotationItemHandler.remove(bo));
        for (Long id : ids) {
            OpportunityQuotationItemVo vo = opportunityQuotationItemService.queryById(id);
            if (vo == null || vo.getQuotationId() == null) {
                throw new UserException("删除操作失败，记录不存在");
            }
            OpportunityQuotationVo quotationVo = opportunityQuotationService.queryById(vo.getQuotationId());
            if (quotationVo == null) {
                return R.fail("删除操作失败，未找到数据");
            }
        }
        return toAjax(opportunityQuotationItemService.deleteWithValidByIds(List.of(ids), true));
    }
}
