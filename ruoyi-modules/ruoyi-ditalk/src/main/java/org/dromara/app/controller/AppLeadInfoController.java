package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.app.domain.bo.LeadContactBo;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ILeadInfoHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户线索信息
 *
 * @author weidixian
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/lead/info")
public class AppLeadInfoController extends BaseController {

    private final ILeadInfoHandler leadInfoHandler;

    /**
     * 添加 Lead 与 Contact 信息
     */
    @SaCheckPermission("lead:info:add")
    @Log(title = "线索信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/customerContact")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody @Valid LeadContactBo bo) {
        return toAjax(leadInfoHandler.addByBo(bo));
    }

    /**
     * 修改 Lead 与 Contact 信息
     */
    @SaCheckPermission("lead:info:edit")
    @Log(title = "线索信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/customerContact")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody @Valid LeadContactBo bo) {
        return toAjax(leadInfoHandler.editByBo(bo));
    }

}
