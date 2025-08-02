package org.dromara.module.lead.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ILeadInfoHandler;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.service.ILeadInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公共线索信息（线索池）
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lead/info/public")
public class PublicLeadInfoController extends BaseController {

    private final ILeadInfoService leadInfoService;
    private final IContactInfoService contactInfoService;
    private final ILeadInfoHandler leadInfoHandler;

    /**
     * 查询线索信息列表
     */
    @SaCheckPermission("lead:public:list")
    @GetMapping("/list")
    public TableDataInfo<LeadInfoVo> list(LeadInfoBo bo, PageQuery pageQuery) {
        bo.getParams().put("isPublic", true); // 设置查询条件为公海客户
        TableDataInfo<LeadInfoVo> tableDataInfo = DataPermissionHelper.ignore(() ->
            leadInfoService.queryPageList(bo, pageQuery)
        );
        return tableDataInfo;
    }

    /**
     * 获取线索信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lead:public:query")
    @GetMapping("/{id}")
    public R<LeadInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        LeadInfoVo leadInfoVo = DataPermissionHelper.ignore(() -> leadInfoService.queryById(id));
        if (leadInfoVo == null && leadInfoVo.getAssignedTo() != null) {
            return R.fail("数据不存在");
        }
        leadInfoVo.setContactInfo(DataPermissionHelper.ignore(() -> contactInfoService.queryById(leadInfoVo.getContactId())));
        return R.ok(leadInfoVo);
    }

    /**
     * 领取线索到指定用户
     */
    @SaCheckPermission("lead:public:claim")
    @PutMapping("/claim/{userId}/{leadIds}")
    public R<Void> claim(@NotNull(message = "用户ID不能为空") @PathVariable Long userId,
                         @NotNull(message = "线索ID不能为空") @PathVariable Long[] leadIds) {
        return toAjax(leadInfoHandler.claim(userId, List.of(leadIds)));
    }

}
