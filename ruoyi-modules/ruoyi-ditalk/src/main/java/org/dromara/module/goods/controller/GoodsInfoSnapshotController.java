package org.dromara.module.goods.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.module.goods.domain.vo.GoodsBrandVo;
import org.dromara.module.goods.domain.vo.GoodsCategoryVo;
import org.dromara.module.goods.service.IGoodsBrandService;
import org.dromara.module.goods.service.IGoodsCategoryService;
import org.dromara.module.shop.domain.vo.ShopInfoVo;
import org.dromara.module.shop.service.IShopInfoService;
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
import org.dromara.module.goods.domain.vo.GoodsInfoSnapshotVo;
import org.dromara.module.goods.domain.bo.GoodsInfoSnapshotBo;
import org.dromara.module.goods.service.IGoodsInfoSnapshotService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品信息快照
 *
 * @author weidixian
 * @date 2025-08-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods/infoSnapshot")
public class GoodsInfoSnapshotController extends BaseController {

    private final IGoodsInfoSnapshotService goodsInfoSnapshotService;
    private final IShopInfoService shopInfoService;
    private final IGoodsCategoryService goodsCategoryService;
    private final IGoodsBrandService goodsBrandService;

    private void extendVo(GoodsInfoSnapshotVo goodsInfoSnapshotVo) {
        if (goodsInfoSnapshotVo == null) {
            return;
        }
        // 店铺信息
        ShopInfoVo shopInfoVo = shopInfoService.queryById(goodsInfoSnapshotVo.getShopId());
        if (shopInfoVo != null) goodsInfoSnapshotVo.setShopName(shopInfoVo.getName());
        // 商品分类
        GoodsCategoryVo goodsCategoryVo = goodsCategoryService.queryById(goodsInfoSnapshotVo.getCategoryId());
        if (goodsCategoryVo != null) goodsInfoSnapshotVo.setCategoryName(goodsCategoryVo.getName());
        // 商品品牌
        if (goodsInfoSnapshotVo.getBrandId() != null && goodsInfoSnapshotVo.getBrandId() > 0) {
            GoodsBrandVo goodsBrandVo = goodsBrandService.queryById(goodsInfoSnapshotVo.getBrandId());
            if (goodsBrandVo != null) goodsInfoSnapshotVo.setBrandName(goodsBrandVo.getName());
        }
    }

    /**
     * 查询商品信息快照列表
     */
    @SaCheckPermission("goods:infoSnapshot:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsInfoSnapshotVo> list(GoodsInfoSnapshotBo bo, PageQuery pageQuery) {
        TableDataInfo<GoodsInfoSnapshotVo> tableDataInfo = goodsInfoSnapshotService.queryPageList(bo, pageQuery);
        if (tableDataInfo.getRows() != null && !tableDataInfo.getRows().isEmpty()) {
            tableDataInfo.getRows().forEach(r -> {
                r.setContent(null); // 默认不返回content字段，减少网络传输量
                extendVo(r);
            });
        }
        return tableDataInfo;
    }

    /**
     * 导出商品信息快照列表
     */
    @SaCheckPermission("goods:infoSnapshot:export")
    @Log(title = "商品信息快照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsInfoSnapshotBo bo, HttpServletResponse response) {
        List<GoodsInfoSnapshotVo> list = goodsInfoSnapshotService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品信息快照", GoodsInfoSnapshotVo.class, response);
    }

    /**
     * 获取商品信息快照详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("goods:infoSnapshot:query")
    @GetMapping("/{id}")
    public R<GoodsInfoSnapshotVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(goodsInfoSnapshotService.queryById(id));
    }

    /**
     * 新增商品信息快照
     */
    @SaCheckPermission("goods:infoSnapshot:add")
    @Log(title = "商品信息快照", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsInfoSnapshotBo bo) {
        return toAjax(goodsInfoSnapshotService.insertByBo(bo));
    }

    /**
     * 修改商品信息快照
     */
    @SaCheckPermission("goods:infoSnapshot:edit")
    @Log(title = "商品信息快照", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsInfoSnapshotBo bo) {
        return toAjax(goodsInfoSnapshotService.updateByBo(bo));
    }

    /**
     * 删除商品信息快照
     *
     * @param ids 主键串
     */
    @SaCheckPermission("goods:infoSnapshot:remove")
    @Log(title = "商品信息快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(goodsInfoSnapshotService.deleteWithValidByIds(List.of(ids), true));
    }
}
