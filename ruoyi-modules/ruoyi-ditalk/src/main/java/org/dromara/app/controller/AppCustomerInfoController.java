package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.app.domain.bo.CustomerContactBo;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 指派给我的 Customer 信息分页列表
     */
    @SaCheckPermission("customer:info:list" )
    @GetMapping("/myCustomers" )
    public R<List<CustomerInfoVo>> myCustomers(CustomerInfoBo bo, PageQuery pageQuery) {
        return R.ok(customerInfoHandler.myCustomers(bo, pageQuery));
    }

    /**
     * 指派给我的 Customer 信息
     */
    @SaCheckPermission("customer:info:query" )
    @GetMapping("/myCustomer/{id}" )
    public R<CustomerInfoVo> myCustomer(@NotNull(message = "ID不能为空" )
                                         @PathVariable Long id) {
        CustomerInfoVo customerInfoVo = customerInfoHandler.myCustomer(id);
        if (customerInfoVo == null) {
            return R.fail("未找到数据" );
        } else {
            if (!customerInfoVo.getAssignedTo().equals(LoginHelper.getUserId())) {
                return R.fail("权限不足" );
            } else {
                return R.ok(customerInfoVo);
            }
        }
    }

}
