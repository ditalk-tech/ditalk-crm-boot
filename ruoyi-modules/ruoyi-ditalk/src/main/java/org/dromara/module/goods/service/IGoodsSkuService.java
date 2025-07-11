package org.dromara.module.goods.service;

import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.domain.bo.GoodsSkuBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品SKUService接口
 *
 * @author weidixian
 * @date 2025-07-11
 */
public interface IGoodsSkuService {

    /**
     * 查询商品SKU
     *
     * @param id 主键
     * @return 商品SKU
     */
    GoodsSkuVo queryById(Long id);

    /**
     * 分页查询商品SKU列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品SKU分页列表
     */
    TableDataInfo<GoodsSkuVo> queryPageList(GoodsSkuBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品SKU列表
     *
     * @param bo 查询条件
     * @return 商品SKU列表
     */
    List<GoodsSkuVo> queryList(GoodsSkuBo bo);

    /**
     * 新增商品SKU
     *
     * @param bo 商品SKU
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsSkuBo bo);

    /**
     * 修改商品SKU
     *
     * @param bo 商品SKU
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsSkuBo bo);

    /**
     * 删除商品SKU
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商品SKU信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商品SKU列表
     *
     * @param bo 查询条件
     * @return 商品SKU列表
     */
    List<GoodsSkuVo> queryList(GoodsSkuBo bo, IdPageQuery pageQuery);
}
