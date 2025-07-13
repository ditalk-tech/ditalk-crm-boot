package org.dromara.module.goods.controller;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.module.goods.domain.bo.GoodsInfoContentBo;
import org.dromara.module.goods.domain.vo.GoodsInfoMiniVo;
import org.dromara.module.goods.domain.vo.GoodsInfoOptionVo;
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
import org.dromara.module.goods.domain.vo.GoodsInfoVo;
import org.dromara.module.goods.domain.bo.GoodsInfoBo;
import org.dromara.module.goods.service.IGoodsInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品信息
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/info")
public class GoodsInfoController extends BaseController {

    private final IGoodsInfoService goodsInfoService;

    /**
     * 查询商品mini信息列表
     */
    @SaCheckPermission("goods:info:list")
    @GetMapping("/list/mini")
    public TableDataInfo<GoodsInfoMiniVo> listMini(GoodsInfoBo bo, PageQuery pageQuery) {
        return goodsInfoService.queryMiniPageList(bo, pageQuery);
    }

    /**
     * 查询商品option信息列表
     */
    @SaCheckPermission("goods:info:list")
    @GetMapping("/list/option")
    public TableDataInfo<GoodsInfoOptionVo> listOption(GoodsInfoBo bo, PageQuery pageQuery) {
        return goodsInfoService.queryOptionPageList(bo, pageQuery);
    }

    /**
     * 查询商品信息列表
     */
    @SaCheckPermission("goods:info:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsInfoVo> list(GoodsInfoBo bo, PageQuery pageQuery) {
        TableDataInfo<GoodsInfoVo> tableDataInfo = goodsInfoService.queryPageList(bo, pageQuery);
        tableDataInfo.getRows().forEach(r -> r.setContent(null)); // 默认不返回content字段，减少网络传输量
        return goodsInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品信息列表
     */
    @SaCheckPermission("goods:info:export")
    @Log(title = "商品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsInfoBo bo, HttpServletResponse response) {
        List<GoodsInfoVo> list = goodsInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品信息", GoodsInfoVo.class, response);
    }

    /**
     * 获取商品信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:info:query")
    @GetMapping("/{id}")
    public R<GoodsInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsInfoService.queryById(id));
    }

    /**
     * 新增商品信息
     */
    @SaCheckPermission("goods:info:add")
    @Log(title = "商品信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<GoodsInfoVo> add(@Validated(AddGroup.class) @RequestBody GoodsInfoBo bo) {
        Boolean flag = goodsInfoService.insertByBo(bo);
        if (!flag) {
            return R.fail();
        }
        GoodsInfoVo goodsInfoVo = goodsInfoService.queryById(bo.getId());
        return R.ok(goodsInfoVo);
    }

    /**
     * 修改商品信息
     */
    @SaCheckPermission("goods:info:edit")
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<GoodsInfoVo> edit(@Validated(EditGroup.class) @RequestBody GoodsInfoBo bo) {
        Boolean flag = goodsInfoService.updateByBo(bo);
        if (!flag) {
            return R.fail();
        }
        GoodsInfoVo goodsInfoVo = goodsInfoService.queryById(bo.getId());
        return R.ok(goodsInfoVo);
    }

    /**
     * 删除商品信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:info:remove")
    @Log(title = "商品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 修改商品信息Content
     */
    @SaCheckPermission("goods:info:edit")
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/content")
    public R<GoodsInfoVo> editContent(@Validated(EditGroup.class) @RequestBody GoodsInfoContentBo bo) {
        GoodsInfoBo goodsInfoBo = BeanUtil.copyProperties(bo, GoodsInfoBo.class);
        Boolean flag = goodsInfoService.updateByBo(goodsInfoBo);
        if (!flag) {
            return R.fail();
        }
        GoodsInfoVo goodsInfoVo = goodsInfoService.queryById(goodsInfoBo.getId());
        return R.ok(goodsInfoVo);
    }
}
