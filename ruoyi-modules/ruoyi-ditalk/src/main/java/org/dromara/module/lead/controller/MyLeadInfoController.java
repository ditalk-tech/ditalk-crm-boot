package org.dromara.module.lead.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.app.domain.bo.LeadContactBo;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.handler.ILeadInfoHandler;
import org.dromara.module.contact.service.IContactInfoService;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.domain.vo.LeadInfoOptionVo;
import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.service.ILeadInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 我的线索信息
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lead/info/my")
public class MyLeadInfoController extends BaseController {

    private final ILeadInfoService leadInfoService;
    private final IContactInfoService contactInfoService;
    private final ILeadInfoHandler leadInfoHandler;

    /**
     * 查询线索信息列表
     */
    @SaCheckPermission("lead:my:list")
    @GetMapping("/list")
    public TableDataInfo<LeadInfoVo> list(LeadInfoBo bo, PageQuery pageQuery) {
        bo.getParams().put("isPublic", false);
        return leadInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取线索信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lead:my:query")
    @GetMapping("/{id}")
    public R<LeadInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long id) {
        LeadInfoVo leadInfoVo = leadInfoService.queryByIdNoCache(id);
        if (leadInfoVo == null) {
            return R.fail("数据错误");
        }
        leadInfoVo.setContactInfo(contactInfoService.queryById(leadInfoVo.getContactId()));
        return R.ok(leadInfoVo);
    }

    /**
     * 添加 Lead 与 Contact 信息
     */
    @SaCheckPermission("lead:my:add")
    @Log(title = "线索信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody @Valid LeadContactBo bo) {
        return toAjax(leadInfoHandler.addByBo(bo));
    }

    /**
     * 修改 Lead 与 Contact 信息
     */
    @SaCheckPermission("lead:my:edit")
    @Log(title = "线索信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody @Valid LeadContactBo bo) {
        return toAjax(leadInfoHandler.editByBo(bo));
    }

    /**
     * 回收线索到线索池
     *
     * @param leadIds 主键
     */
    @SaCheckPermission("lead:my:reclaim")
    @PutMapping("/reclaim/{leadIds}")
    public R<Void> reclaim(@NotNull(message = "线索不能为空")
                           @PathVariable Long[] leadIds) {
        return toAjax(leadInfoHandler.reclaimById(List.of(leadIds)));
    }

    /**
     * 单个或批量转移线索到指定用户
     */
    @SaCheckPermission("lead:my:transfer")
    @PutMapping("/transfer/{leadIds}/{userId}")
    public R<Void> transfer(@NotNull(message = "线索不能为空")
                            @PathVariable Long[] leadIds,
                            @NotNull(message = "目标用户不能为空")
                            @PathVariable Long userId) {
        return toAjax(leadInfoHandler.transfer(List.of(leadIds), userId));
    }

    /**
     * 回收用户所有线索到线索池
     */
    @SaCheckPermission("lead:my:reclaim")
    @PutMapping("/reclaim/user/{userId}")
    public R<Void> reclaimUserLead(@NotNull(message = "用户不能为空")
                               @PathVariable Long userId) {
        return toAjax(leadInfoHandler.reclaimUserLead(userId));
    }

    /**
     * 转移指定用户的线索到另一个用户
     */
    @SaCheckPermission("lead:my:transfer")
    @PutMapping("/transfer/user/{sourceUserId}/{targetUserId}")
    public R<Void> transferUserLead(@NotNull(message = "源用户不能为空")
                                @PathVariable Long sourceUserId,
                                @NotNull(message = "目标用户不能为空")
                                @PathVariable Long targetUserId) {
        return toAjax(leadInfoHandler.transferUserLead(sourceUserId, targetUserId));
    }

    /**
     * 查询我的线索信息选项列表
     */
    @SaCheckPermission("lead:my:list")
    @GetMapping("/list/option")
    public R<List<LeadInfoOptionVo>> listOption(LeadInfoBo bo) {
        bo.getParams().put("isPublic", false);
        List<LeadInfoVo> voList = leadInfoService.queryList(bo);
        return R.ok(BeanUtil.copyToList(voList, LeadInfoOptionVo.class));
    }

    /**
     * 分页查询我的线索信息选项列表
     */
    @SaCheckPermission("lead:my:list")
    @GetMapping("/list/page/option")
    public R<List<LeadInfoOptionVo>> listPageOption(LeadInfoBo bo, PageQuery pageQuery) {
        bo.getParams().put("isPublic", false);
        TableDataInfo<LeadInfoVo> tableDataInfo = leadInfoService.queryPageList(bo, pageQuery);
        List<LeadInfoVo> voList = tableDataInfo.getRows();
        return R.ok(BeanUtil.copyToList(voList, LeadInfoOptionVo.class));
    }

}
