package org.dromara.module.contract.controller;

import java.util.List;

import cn.hutool.core.collection.IterUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.domain.dto.OssDTO;
import org.dromara.common.core.service.OssService;
import org.dromara.common.utils.CodeGeneratorUtil;
import org.dromara.handler.IContractInfoHandler;
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
import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.module.contract.service.IContractInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 合同信息
 *
 * @author weidixian
 * @date 2025-08-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/info")
public class ContractInfoController extends BaseController {

    private final IContractInfoService contractInfoService;
    private final IContractInfoHandler contractInfoHandler;
    private final OssService ossService;

    /**
     * 查询合同信息列表
     */
    @SaCheckPermission("contract:info:list")
    @GetMapping("/list")
    public TableDataInfo<ContractInfoVo> list(ContractInfoBo bo, PageQuery pageQuery) {
        TableDataInfo<ContractInfoVo> tableDataInfo = contractInfoService.queryPageList(bo, pageQuery);
        if (IterUtil.isNotEmpty(tableDataInfo.getRows())) {
            tableDataInfo.getRows().forEach(row -> {
                List<OssDTO> ossDtos = ossService.selectByIds(row.getTerms());
                row.setTermOss(ossDtos);
            });
        }
        return tableDataInfo;
    }

    /**
     * 导出合同信息列表
     */
    @SaCheckPermission("contract:info:export")
    @Log(title = "合同信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ContractInfoBo bo, HttpServletResponse response) {
        List<ContractInfoVo> list = contractInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "合同信息", ContractInfoVo.class, response);
    }

    /**
     * 获取合同信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("contract:info:query")
    @GetMapping("/{id}")
    public R<ContractInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(contractInfoService.queryById(id));
    }

    /**
     * 新增合同信息
     */
    @SaCheckPermission("contract:info:add")
    @Log(title = "合同信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ContractInfoBo bo) {
        bo.setCode(CodeGeneratorUtil.businessCode("HT", "yyyyMMdd", "-", null, null));
        return toAjax(contractInfoHandler.add(bo));
    }

    /**
     * 修改合同信息
     */
    @SaCheckPermission("contract:info:edit")
    @Log(title = "合同信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContractInfoBo bo) {
        return toAjax(contractInfoHandler.edit(bo));
    }

    /**
     * 删除合同信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("contract:info:remove")
    @Log(title = "合同信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(contractInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
