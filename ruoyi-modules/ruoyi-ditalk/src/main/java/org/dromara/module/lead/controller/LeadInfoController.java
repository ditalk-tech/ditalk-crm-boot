package org.dromara.module.lead.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.module.contact.service.IContactInfoService;
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
import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.service.ILeadInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 线索信息
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lead/info")
public class LeadInfoController extends BaseController {

    private final ILeadInfoService leadInfoService;
    private final IContactInfoService contactInfoService;

    /**
     * 查询线索信息列表
     */
    @SaCheckPermission("lead:info:list")
    @GetMapping("/list")
    public TableDataInfo<LeadInfoVo> list(LeadInfoBo bo, PageQuery pageQuery) {
        return leadInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出线索信息列表
     */
    @SaCheckPermission("lead:info:export")
    @Log(title = "线索信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LeadInfoBo bo, HttpServletResponse response) {
        List<LeadInfoVo> list = leadInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "线索信息", LeadInfoVo.class, response);
    }

    /**
     * 获取线索信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lead:info:query")
    @GetMapping("/{id}")
    public R<LeadInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        LeadInfoVo leadInfoVo = leadInfoService.queryById(id);
        leadInfoVo.setContactInfo(contactInfoService.queryById(leadInfoVo.getContactId()));
        return R.ok(leadInfoVo);
    }

    /**
     * 新增线索信息
     */
    @SaCheckPermission("lead:info:add")
    @Log(title = "线索信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LeadInfoBo bo) {
        return toAjax(leadInfoService.insertByBo(bo));
    }

    /**
     * 修改线索信息
     */
    @SaCheckPermission("lead:info:edit")
    @Log(title = "线索信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LeadInfoBo bo) {
        return toAjax(leadInfoService.updateByBo(bo));
    }

    /**
     * 删除线索信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lead:info:remove")
    @Log(title = "线索信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(leadInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
