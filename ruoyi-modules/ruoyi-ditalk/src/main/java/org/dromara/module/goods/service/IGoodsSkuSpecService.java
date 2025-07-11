package org.dromara.module.goods.service;

import org.dromara.module.goods.domain.vo.GoodsSkuSpecVo;
import org.dromara.module.goods.domain.bo.GoodsSkuSpecBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * SKU规格Service接口
 *
 * @author weidixian
 * @date 2025-07-11
 */
public interface IGoodsSkuSpecService {

    /**
     * 查询SKU规格
     *
     * @param id 主键
     * @return SKU规格
     */
    GoodsSkuSpecVo queryById(Long id);

    /**
     * 分页查询SKU规格列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return SKU规格分页列表
     */
    TableDataInfo<GoodsSkuSpecVo> queryPageList(GoodsSkuSpecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的SKU规格列表
     *
     * @param bo 查询条件
     * @return SKU规格列表
     */
    List<GoodsSkuSpecVo> queryList(GoodsSkuSpecBo bo);

    /**
     * 新增SKU规格
     *
     * @param bo SKU规格
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsSkuSpecBo bo);

    /**
     * 修改SKU规格
     *
     * @param bo SKU规格
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsSkuSpecBo bo);

    /**
     * 删除SKU规格
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除SKU规格信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询SKU规格列表
     *
     * @param bo 查询条件
     * @return SKU规格列表
     */
    List<GoodsSkuSpecVo> queryList(GoodsSkuSpecBo bo, IdPageQuery pageQuery);
}
