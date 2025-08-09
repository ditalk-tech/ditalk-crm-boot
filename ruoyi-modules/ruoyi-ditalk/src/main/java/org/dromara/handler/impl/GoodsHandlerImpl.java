package org.dromara.handler.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.enums.GoodsStateEnum;
import org.dromara.handler.IGoodsInfoHandler;
import org.dromara.module.goods.domain.bo.GoodsInfoBo;
import org.dromara.module.goods.domain.bo.GoodsInfoSnapshotBo;
import org.dromara.module.goods.domain.vo.GoodsInfoVo;
import org.dromara.module.goods.service.IGoodsInfoService;
import org.dromara.module.goods.service.IGoodsInfoSnapshotService;
import org.springframework.stereotype.Service;

/**
 * 商品信息应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsHandlerImpl implements IGoodsInfoHandler {

    private final IGoodsInfoService goodsInfoService;
    private final IGoodsInfoSnapshotService goodsInfoSnapshotService;

    /**
     * 获取商品信息构建快照BO
     *
     * @param goodsId 商品ID
     * @return 商品信息快照BO
     */
    private GoodsInfoSnapshotBo buildSnapshotBo(Long goodsId) {
        GoodsInfoVo vo = goodsInfoService.queryById(goodsId);
        GoodsInfoSnapshotBo snapshotBo = new GoodsInfoSnapshotBo();
        BeanUtil.copyProperties(vo, snapshotBo);
        snapshotBo.setId(null);
        snapshotBo.setGoodsId(vo.getId());
        return snapshotBo;
    }

    /**
     * 新增商品信息快照，仅在上架状态时记录快照
     *
     * @param snapshotBo 商品信息快照BO
     * @return 是否新增成功
     */
    private Boolean addSnapshot(GoodsInfoSnapshotBo snapshotBo) {
        if (snapshotBo.getState().equals(GoodsStateEnum.ON_SALE.getCode())) { // 仅在上架状态时记录快照
            Boolean flag = goodsInfoSnapshotService.insertByBo(snapshotBo);
            if (!flag) {
                throw new UserException("新增商品信息快照失败");
            }
        }
        return true;
    }

    @Override
    @DSTransactional
    public Boolean add(GoodsInfoBo bo) {
        Boolean flag1 = goodsInfoService.insertByBo(bo);
        if (!flag1) {
            throw new UserException("新增商品信息失败");
        }
        this.addSnapshot(this.buildSnapshotBo(bo.getId()));
        return true;
    }

    @Override
    @DSTransactional
    public Boolean edit(GoodsInfoBo bo) {
        Boolean flag1 = goodsInfoService.updateByBo(bo);
        if (!flag1) {
            throw new UserException("修改商品信息失败");
        }
        this.addSnapshot(this.buildSnapshotBo(bo.getId()));
        return true;
    }

}
