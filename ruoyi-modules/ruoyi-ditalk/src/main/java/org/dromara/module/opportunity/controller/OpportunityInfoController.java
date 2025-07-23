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
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机信息
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/info")
public class OpportunityInfoController extends BaseController {

    private final IOpportunityInfoService opportunityInfoService;

    /**
     * 查询商机信息列表
     */
    @SaCheckPermission("opportunity:info:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityInfoVo> list(OpportunityInfoBo bo, PageQuery pageQuery) {
        return opportunityInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机信息列表
     */
    @SaCheckPermission("opportunity:info:export")
    @Log(title = "商机信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityInfoBo bo, HttpServletResponse response) {
        List<OpportunityInfoVo> list = opportunityInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机信息", OpportunityInfoVo.class, response);
    }

    /**
     * 获取商机信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:info:query")
    @GetMapping("/{id}")
    public R<OpportunityInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(opportunityInfoService.queryById(id));
    }

    /**
     * 新增商机信息
     */
    @SaCheckPermission("opportunity:info:add")
    @Log(title = "商机信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityInfoBo bo) {
        return toAjax(opportunityInfoService.insertByBo(bo));
    }

    /**
     * 修改商机信息
     */
    @SaCheckPermission("opportunity:info:edit")
    @Log(title = "商机信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityInfoBo bo) {
        return toAjax(opportunityInfoService.updateByBo(bo));
    }

    /**
     * 删除商机信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:info:remove")
    @Log(title = "商机信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(opportunityInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
