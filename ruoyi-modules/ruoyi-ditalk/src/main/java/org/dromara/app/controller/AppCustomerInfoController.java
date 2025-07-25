package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ICustomerInfoHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户信息
 *
 * @author weidixian
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/customer/info")
public class AppCustomerInfoController extends BaseController {

    private final ICustomerInfoHandler customerInfoHandler;

    /**
     * 添加 Customer 与 Contact 信息
     */
    @SaCheckPermission("customer:info:add")
    @Log(title = "线索信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody @Valid CustomerContactBo bo) {
        return toAjax(customerInfoHandler.addByBo(bo));
    }

    /**
     * 修改 Customer 与 Contact 信息
     */
    @SaCheckPermission("customer:info:edit")
    @Log(title = "线索信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody @Valid CustomerContactBo bo) {
        return toAjax(customerInfoHandler.editByBo(bo));
    }

}
