package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ICustomerInfoCommonHandler;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户信息，不区分客户与线索，统一为客户 —— 注意：不走缓存
 *
 * @author weidixian
 * @date 2025-08-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/common/info")
public class CustomerCommonInfoController extends BaseController {

    private final ICustomerInfoCommonHandler customerInfoCommonHandler;

    /**
     * 获取客户信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:info:query")
    @GetMapping("/{id}")
    public R<CustomerInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(customerInfoCommonHandler.queryAllByIdNoCache(id));
    }

}
