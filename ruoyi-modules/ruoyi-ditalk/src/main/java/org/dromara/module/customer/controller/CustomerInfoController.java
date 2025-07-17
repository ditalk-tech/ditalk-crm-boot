package org.dromara.module.customer.controller;

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
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 客户信息
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/info")
public class CustomerInfoController extends BaseController {

    private final ICustomerInfoService customerInfoService;

    /**
     * 查询客户信息列表
     */
    @SaCheckPermission("customer:info:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerInfoVo> list(CustomerInfoBo bo, PageQuery pageQuery) {
        return customerInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出客户信息列表
     */
    @SaCheckPermission("customer:info:export")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CustomerInfoBo bo, HttpServletResponse response) {
        List<CustomerInfoVo> list = customerInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "客户信息", CustomerInfoVo.class, response);
    }

    /**
     * 获取客户信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:info:query")
    @GetMapping("/{id}")
    public R<CustomerInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(customerInfoService.queryById(id));
    }

    /**
     * 新增客户信息
     */
    @SaCheckPermission("customer:info:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CustomerInfoBo bo) {
        return toAjax(customerInfoService.insertByBo(bo));
    }

    /**
     * 修改客户信息
     */
    @SaCheckPermission("customer:info:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CustomerInfoBo bo) {
        return toAjax(customerInfoService.updateByBo(bo));
    }

    /**
     * 删除客户信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("customer:info:remove")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(customerInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
