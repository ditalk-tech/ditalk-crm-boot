package org.dromara.module.customer.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
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
import org.dromara.module.customer.domain.vo.CustomerActivityVo;
import org.dromara.module.customer.domain.bo.CustomerActivityBo;
import org.dromara.module.customer.service.ICustomerActivityService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 客户活动记录
 *
 * @author weidixian
 * @date 2025-07-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/activity")
public class CustomerActivityController extends BaseController {

    private final ICustomerActivityService customerActivityService;
    private final IContactInfoService contactInfoService;

    /**
     * 查询客户活动记录列表
     */
    @SaCheckPermission("customer:activity:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerActivityVo> list(CustomerActivityBo bo, PageQuery pageQuery) {
        TableDataInfo<CustomerActivityVo> tableDataInfo = customerActivityService.queryPageList(bo, pageQuery);
        if (tableDataInfo.getRows() != null) {
            tableDataInfo.getRows().forEach(row -> {
                ContactInfoVo vo = contactInfoService.queryById(row.getContactId());
                row.setContactName(vo.getLastName()+vo.getFirstName());
            });
        }
        return tableDataInfo;
    }

    /**
     * 导出客户活动记录列表
     */
    @SaCheckPermission("customer:activity:export")
    @Log(title = "客户活动记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CustomerActivityBo bo, HttpServletResponse response) {
        List<CustomerActivityVo> list = customerActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "客户活动记录", CustomerActivityVo.class, response);
    }

    /**
     * 获取客户活动记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:activity:query")
    @GetMapping("/{id}")
    public R<CustomerActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(customerActivityService.queryById(id));
    }

    /**
     * 新增客户活动记录
     */
    @SaCheckPermission("customer:activity:add")
    @Log(title = "客户活动记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CustomerActivityBo bo) {
        return toAjax(customerActivityService.insertByBo(bo));
    }

    /**
     * 修改客户活动记录
     */
    @SaCheckPermission("customer:activity:edit")
    @Log(title = "客户活动记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CustomerActivityBo bo) {
        return toAjax(customerActivityService.updateByBo(bo));
    }

    /**
     * 删除客户活动记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("customer:activity:remove")
    @Log(title = "客户活动记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(customerActivityService.deleteWithValidByIds(List.of(ids), true));
    }
}
