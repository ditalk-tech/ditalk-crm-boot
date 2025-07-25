package org.dromara.module.customer.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
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
 * 公海客户信息
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/info/public")
public class PublicCustomerInfoController extends BaseController {

    private final ICustomerInfoService customerInfoService;
    private final IContactInfoService contactInfoService;

    /**
     * 查询公海客户信息列表
     */
    @SaCheckPermission("customer:public:list")
    @GetMapping("/list")
    public TableDataInfo<CustomerInfoVo> list(CustomerInfoBo bo, PageQuery pageQuery) {
        bo.getParams().put("isPublic", true); // 设置查询条件为公海客户
        return customerInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取公海客户信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("customer:public:query")
    @GetMapping("/{id}")
    public R<CustomerInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                               @PathVariable Long id) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(id);
        if (customerInfoVo == null && customerInfoVo.getAssignedTo() != null) {
            return R.fail("数据错误");
        }
        customerInfoVo.setContactInfo(contactInfoService.queryById(customerInfoVo.getContactId()));
        return R.ok(customerInfoVo);
    }

}
