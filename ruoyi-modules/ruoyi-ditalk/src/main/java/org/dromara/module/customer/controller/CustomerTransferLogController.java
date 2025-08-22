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
import org.dromara.module.customer.domain.vo.CustomerTransferLogVo;
import org.dromara.module.customer.domain.bo.CustomerTransferLogBo;
import org.dromara.module.customer.service.ICustomerTransferLogService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 客户转移记录
 *
 * @author weidixian
 * @date 2025-08-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/transferLog")
public class CustomerTransferLogController extends BaseController {

    private final ICustomerTransferLogService customerTransferLogService;

    /**
     * 查询客户转移记录列表
     */
    @SaCheckPermission("customer:transferLog:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerTransferLogVo> list(CustomerTransferLogBo bo, PageQuery pageQuery) {
        return customerTransferLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出客户转移记录列表
     */
    @SaCheckPermission("customer:transferLog:export")
    @Log(title = "客户转移记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CustomerTransferLogBo bo, HttpServletResponse response) {
        List<CustomerTransferLogVo> list = customerTransferLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "客户转移记录", CustomerTransferLogVo.class, response);
    }

    /**
     * 获取客户转移记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:transferLog:query")
    @GetMapping("/{id}")
    public R<CustomerTransferLogVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(customerTransferLogService.queryById(id));
    }

    /**
     * 新增客户转移记录
     */
    @SaCheckPermission("customer:transferLog:add")
    @Log(title = "客户转移记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CustomerTransferLogBo bo) {
        return toAjax(customerTransferLogService.insertByBo(bo));
    }

    /**
     * 修改客户转移记录
     */
    @SaCheckPermission("customer:transferLog:edit")
    @Log(title = "客户转移记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CustomerTransferLogBo bo) {
        return toAjax(customerTransferLogService.updateByBo(bo));
    }

    /**
     * 删除客户转移记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("customer:transferLog:remove")
    @Log(title = "客户转移记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(customerTransferLogService.deleteWithValidByIds(List.of(ids), true));
    }
}
