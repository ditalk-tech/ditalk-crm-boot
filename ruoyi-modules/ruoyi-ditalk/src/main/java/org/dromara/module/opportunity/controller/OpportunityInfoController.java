package org.dromara.module.opportunity.controller;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoOptionVo;
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
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商机信息
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/opportunity/info")
public class OpportunityInfoController extends BaseController {

    private final IOpportunityInfoService opportunityInfoService;
    private final ICustomerInfoService customerInfoService;

    /**
     * 商机信息选项列表
     */
    @SaCheckPermission("opportunity:info:list")
    @GetMapping("/list/option")
    public R<List<OpportunityInfoOptionVo>> listOption(OpportunityInfoBo bo) {
        if (bo.getCustomerId() == null) {
            throw new UserException("请先指定客户");
        }
        CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("活动记录为空，无数据导出");
        }
        List<OpportunityInfoVo> infoVos = opportunityInfoService.queryList(bo);
        return R.ok(BeanUtil.copyToList(infoVos, OpportunityInfoOptionVo.class));
    }

    /**
     * 查询商机信息列表
     */
    @SaCheckPermission("opportunity:info:list")
    @GetMapping("/list")
    public TableDataInfo<OpportunityInfoVo> list(OpportunityInfoBo bo, PageQuery pageQuery) {
        if (bo.getCustomerId() == null) {
            throw new UserException("请先指定客户");
        }
        CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            TableDataInfo<OpportunityInfoVo> build = TableDataInfo.build();
            build.setRows(List.of());
            return build;
        }
        return opportunityInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商机信息列表
     */
    @SaCheckPermission("opportunity:info:export")
    @Log(title = "商机信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpportunityInfoBo bo, HttpServletResponse response) {
        if (bo.getCustomerId() == null) {
            throw new UserException("请先指定客户");
        }
        CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("活动记录为空，无数据导出");
        }
        List<OpportunityInfoVo> list = opportunityInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "商机信息", OpportunityInfoVo.class, response);
    }

    /**
     * 获取商机信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("opportunity:info:query")
    @GetMapping("/{id}")
    public R<OpportunityInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        OpportunityInfoVo opportunityInfoVo = opportunityInfoService.queryById(id);
        if (opportunityInfoVo != null && opportunityInfoVo.getCustomerId() != null) {
            CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(opportunityInfoVo.getCustomerId());
            if (customerInfoVo == null) {
                return R.fail("未找到对应的客户活动记录");
            }
        }
        return R.ok(opportunityInfoVo);
    }

    /**
     * 新增商机信息
     */
    @SaCheckPermission("opportunity:info:add")
    @Log(title = "商机信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpportunityInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("新增操作失败，客户不存在");
        }
        return toAjax(opportunityInfoService.insertByBo(bo));
    }

    /**
     * 修改商机信息
     */
    @SaCheckPermission("opportunity:info:edit")
    @Log(title = "商机信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpportunityInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(bo.getCustomerId());
        if (customerInfoVo == null) {
            throw new UserException("修改操作失败，客户不存在");
        }
        return toAjax(opportunityInfoService.updateByBo(bo));
    }

    /**
     * 删除商机信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("opportunity:info:remove")
    @Log(title = "商机信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return R.fail("暂不支持删除商机信息，请联系管理员进行处理");
//        for (Long id : ids) {
//            OpportunityInfoVo opportunityInfoVo = opportunityInfoService.queryById(id);
//            if (opportunityInfoVo == null || opportunityInfoVo.getCustomerId() == null) {
//                throw new UserException("删除操作失败，记录不存在");
//            }
//            CustomerInfoVo customerInfoVo = customerInfoService.queryAllByIdNoCache(opportunityInfoVo.getCustomerId());
//            if (customerInfoVo == null) {
//                throw new UserException("删除操作失败，客户不存在");
//            }
//        }
//        return toAjax(opportunityInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
