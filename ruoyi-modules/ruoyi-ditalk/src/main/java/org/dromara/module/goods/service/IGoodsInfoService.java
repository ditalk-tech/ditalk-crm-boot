package org.dromara.module.goods.service;

import org.dromara.module.goods.domain.vo.GoodsInfoMiniVo;
import org.dromara.module.goods.domain.vo.GoodsInfoOptionVo;
import org.dromara.module.goods.domain.vo.GoodsInfoVo;
import org.dromara.module.goods.domain.bo.GoodsInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品信息Service接口
 *
 * @author weidixian
 * @date 2025-07-08
 */
public interface IGoodsInfoService {

    /**
     * 查询商品信息
     *
     * @param id 主键
     * @return 商品信息
     */
    GoodsInfoVo queryById(Long id);

    /**
     * 分页查询商品信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品信息分页列表
     */
    TableDataInfo<GoodsInfoVo> queryPageList(GoodsInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品信息列表
     *
     * @param bo 查询条件
     * @return 商品信息列表
     */
    List<GoodsInfoVo> queryList(GoodsInfoBo bo);

    /**
     * 新增商品信息
     *
     * @param bo 商品信息
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsInfoBo bo);

    /**
     * 修改商品信息
     *
     * @param bo 商品信息
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsInfoBo bo);

    /**
     * 删除商品信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商品信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商品信息列表
     *
     * @param bo 查询条件
     * @return 商品信息列表
     */
    List<GoodsInfoVo> queryList(GoodsInfoBo bo, IdPageQuery pageQuery);

    TableDataInfo<GoodsInfoMiniVo> queryMiniPageList(GoodsInfoBo bo, PageQuery pageQuery);

    TableDataInfo<GoodsInfoOptionVo> queryOptionPageList(GoodsInfoBo bo, PageQuery pageQuery);
}
