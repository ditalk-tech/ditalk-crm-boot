package org.dromara.module.customer.controller;

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
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ICustomerInfoHandler;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 我的客户信息
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/info/my")
public class MyCustomerInfoController extends BaseController {

    private final ICustomerInfoService customerInfoService;
    private final IContactInfoService contactInfoService;
    private final ICustomerInfoHandler customerInfoHandler;

    /**
     * 查询我的客户信息列表
     */
    @SaCheckPermission("customer:my:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerInfoVo> list(CustomerInfoBo bo, PageQuery pageQuery) {
        return customerInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取我的客户信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:my:query")
    @GetMapping("/{id}")
    public R<CustomerInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(id);
        if (customerInfoVo == null) {
            return R.fail("数据不存在");
        }
        customerInfoVo.setContactInfo(contactInfoService.queryById(customerInfoVo.getContactId()));
        return R.ok(customerInfoVo);
    }

    /**
     * 添加 Customer 与 Contact 信息
     */
    @SaCheckPermission("customer:my:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody @Valid CustomerContactBo bo) {
        return toAjax(customerInfoHandler.addByBo(bo));
    }

    /**
     * 修改 Customer 与 Contact 信息
     */
    @SaCheckPermission("customer:my:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody @Valid CustomerContactBo bo) {
        return toAjax(customerInfoHandler.editByBo(bo));
    }

    /**
     * 回收客户到公海
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:my:reclaim")
    @PutMapping("/reclaim/{id}")
    public R<Void> reclaim(@NotNull(message = "主键不能为空")
                           @PathVariable Long id) {
        return toAjax(customerInfoHandler.reclaimById(id));
    }

    /**
     * 单个客户转移到其他用户
     */
    @SaCheckPermission("customer:my:transfer")
    @PutMapping("/transfer/{id}/{userId}")
    public R<Void> transfer(@NotNull(message = "主键不能为空")
                            @PathVariable Long id,
                            @NotNull(message = "目标用户不能为空")
                            @PathVariable Long userId) {
        return toAjax(customerInfoHandler.transfer(id, userId));
    }

}
