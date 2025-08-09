package org.dromara.module.goods.service;

import org.dromara.module.goods.domain.vo.GoodsInfoSnapshotVo;
import org.dromara.module.goods.domain.bo.GoodsInfoSnapshotBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品信息快照Service接口
 *
 * @author weidixian
 * @date 2025-08-08
 */
public interface IGoodsInfoSnapshotService {

    /**
     * 查询商品信息快照
     *
     * @param id 主键
     * @return 商品信息快照
     */
    GoodsInfoSnapshotVo queryById(Long id);

    /**
     * 分页查询商品信息快照列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品信息快照分页列表
     */
    TableDataInfo<GoodsInfoSnapshotVo> queryPageList(GoodsInfoSnapshotBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品信息快照列表
     *
     * @param bo 查询条件
     * @return 商品信息快照列表
     */
    List<GoodsInfoSnapshotVo> queryList(GoodsInfoSnapshotBo bo);

    /**
     * 新增商品信息快照
     *
     * @param bo 商品信息快照
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsInfoSnapshotBo bo);

    /**
     * 修改商品信息快照
     *
     * @param bo 商品信息快照
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsInfoSnapshotBo bo);

    /**
     * 删除商品信息快照
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商品信息快照信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商品信息快照列表
     *
     * @param bo 查询条件
     * @return 商品信息快照列表
     */
    List<GoodsInfoSnapshotVo> queryList(GoodsInfoSnapshotBo bo, IdPageQuery pageQuery);

    /**
     * 查询最新的商品信息快照
     *
     * @param goodsId 商品信息ID
     * @return 最新的商品信息快照
     */
    GoodsInfoSnapshotVo queryLastByGoodsId(Long goodsId);
}
