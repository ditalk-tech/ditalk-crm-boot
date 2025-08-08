package org.dromara.module.contact.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.IContactInfoHandler;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.contact.domain.vo.ContactInfoOptionVo;
import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.contact.service.IContactInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人信息
 *
 * @author weidixian
 * @date 2025-07-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/contact/info")
public class ContactInfoController extends BaseController {

    private final IContactInfoService contactInfoService;
    private final IContactInfoHandler contactInfoHandler;

    /**
     * 查询联系人信息列表
     */
    @SaCheckPermission("contact:info:list")
    @GetMapping("/list")
    public TableDataInfo<ContactInfoVo> list(ContactInfoBo bo, PageQuery pageQuery) {
        return contactInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出联系人信息列表
     */
    @SaCheckPermission("contact:info:export")
    @Log(title = "联系人信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ContactInfoBo bo, HttpServletResponse response) {
        List<ContactInfoVo> list = contactInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "联系人信息", ContactInfoVo.class, response);
    }

    /**
     * 获取联系人信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("contact:info:query")
    @GetMapping("/{id}")
    public R<ContactInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(contactInfoService.queryByIdNoCache(id));
    }

    /**
     * 新增联系人信息
     */
    @SaCheckPermission("contact:info:add")
    @Log(title = "联系人信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ContactInfoBo bo) {
        return toAjax(contactInfoHandler.add(bo));
    }

    /**
     * 修改联系人信息
     */
    @SaCheckPermission("contact:info:edit")
    @Log(title = "联系人信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContactInfoBo bo) {
        return toAjax(contactInfoService.updateByBo(bo));
    }

    /**
     * 删除联系人信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("contact:info:remove")
    @Log(title = "联系人信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        for (Long id : ids) {
            Boolean b = contactInfoHandler.checkIsDefaultContact(id);
            if (b) {
                return R.fail("默认联系人不能删除");
            }
        }
        return toAjax(contactInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询联系人信息选项列表
     * @param customerId 客户ID
     * @return 联系人信息选项列表
     */
    @SaCheckPermission("contact:info:list")
    @GetMapping("/list/options/{customerId}")
    public R<List<ContactInfoOptionVo>> listOptions(@NotNull(message = "主键不能为空")
                                                  @PathVariable Long customerId) {
        ContactInfoBo bo = new ContactInfoBo();
        bo.setCustomerId(customerId);
        List<ContactInfoVo> voList = contactInfoService.queryList(bo);
        return R.ok(BeanUtil.copyToList(voList, ContactInfoOptionVo.class));
    }
}
