package org.dromara.module.customer.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查询我的客户信息列表
     */
    @SaCheckPermission("customer:my:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerInfoVo> list(CustomerInfoBo bo, PageQuery pageQuery) {
        bo.setAssignedTo(LoginHelper.getUserId());
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
        if (customerInfoVo == null && !customerInfoVo.getAssignedTo().equals(LoginHelper.getUserId())) {
            return R.fail("数据错误");
        }
        customerInfoVo.setContactInfo(contactInfoService.queryById(customerInfoVo.getContactId()));
        return R.ok(customerInfoVo);
    }
}
