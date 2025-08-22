package org.dromara.module.opportunity.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.handler.IOpportunityOrderItemHandler;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemTinyBo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
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
import org.dromara.module.opportunity.domain.vo.OpportunityOrderItemVo;
import org.dromara.module.opportunity.domain.bo.OpportunityOrderItemBo;
import org.dromara.module.opportunity.service.IOpportunityOrderItemService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机商品
 *
 * @author weidixian
 * @date 2025-08-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/orderItem")
public class OpportunityOrderItemController extends BaseController {

    private final IOpportunityOrderItemService opportunityOrderItemService;
    private final IOpportunityInfoService opportunityInfoService;
    private final IOpportunityOrderItemHandler opportunityOrderItemHandler;

    /**
     * 查询商机商品列表
     */
    @SaCheckPermission("opportunity:orderItem:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityOrderItemVo> list(OpportunityOrderItemBo bo, PageQuery pageQuery) {
        if (bo.getOpportunityId() == null) {
            throw new UserException("必先指定商机");
        }
        return opportunityOrderItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机商品列表
     */
    @SaCheckPermission("opportunity:orderItem:export")
    @Log(title = "商机商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityOrderItemBo bo, HttpServletResponse response) {
        if (bo.getOpportunityId() == null) {
            throw new UserException("必先指定商机");
        }
        List<OpportunityOrderItemVo> list = opportunityOrderItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机商品", OpportunityOrderItemVo.class, response);
    }

    /**
     * 获取商机商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:orderItem:query")
    @GetMapping("/{id}")
    public R<OpportunityOrderItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        OpportunityOrderItemVo vo = opportunityOrderItemService.queryById(id);
        if (vo != null && vo.getOpportunityId() != null) {
            OpportunityInfoVo infoVo = opportunityInfoService.queryById(vo.getOpportunityId());
            if (infoVo == null) {
                return R.fail("未找到数据");
            }
        }
        return R.ok(vo);
    }

//    /**
//     * 新增商机商品
//     */
//    @SaCheckPermission("opportunity:orderItem:add")
//    @Log(title = "商机商品", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityOrderItemBo bo) {
//        return toAjax(opportunityOrderItemService.insertByBo(bo));
//    }
//
//    /**
//     * 修改商机商品
//     */
//    @SaCheckPermission("opportunity:orderItem:edit")
//    @Log(title = "商机商品", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityOrderItemBo bo) {
//        return toAjax(opportunityOrderItemService.updateByBo(bo));
//    }

    /**
     * 删除商机商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:orderItem:remove")
    @Log(title = "商机商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        for (Long id : ids) {
            OpportunityOrderItemVo vo = opportunityOrderItemService.queryById(id);
            if (vo == null || vo.getCustomerId() == null) {
                throw new UserException("删除操作失败，记录不存在");
            }
            OpportunityInfoVo infoVo = opportunityInfoService.queryById(vo.getOpportunityId());
            if (infoVo == null) {
                return R.fail("删除操作失败，未找到数据");
            }
        }
        return toAjax(opportunityOrderItemService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 新增商机商品_快捷版
     */
    @SaCheckPermission("opportunity:orderItem:add")
    @Log(title = "商机商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> addFunc(@Validated(AddGroup.class) @RequestBody OpportunityOrderItemTinyBo bo) {
        return toAjax(opportunityOrderItemHandler.add(bo));
    }

    /**
     * 修改商机商品_快捷版
     */
    @SaCheckPermission("opportunity:orderItem:edit")
    @Log(title = "商机商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> editFunc(@Validated(EditGroup.class) @RequestBody OpportunityOrderItemTinyBo bo) {
        return toAjax(opportunityOrderItemHandler.edit(bo));
    }

}
