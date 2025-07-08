package org.dromara.module.goods.service;

import cn.hutool.core.lang.tree.Tree;
import org.dromara.common.mybatis.core.page.IdPageQuery;
import org.dromara.module.goods.domain.vo.GoodsCategoryVo;
import org.dromara.module.goods.domain.bo.GoodsCategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 商品分类Service接口
 *
 * @author weidixian
 * @date 2025-07-08
 */
public interface IGoodsCategoryService {

    /**
     * 查询商品分类
     *
     * @param id 主键
     * @return 商品分类
     */
    GoodsCategoryVo queryById(Long id);


    /**
     * 查询符合条件的商品分类列表
     *
     * @param bo 查询条件
     * @return 商品分类列表
     */
    List<GoodsCategoryVo> queryList(GoodsCategoryBo bo);

    /**
     * 新增商品分类
     *
     * @param bo 商品分类
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsCategoryBo bo);

    /**
     * 修改商品分类
     *
     * @param bo 商品分类
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsCategoryBo bo);

    /**
     * 删除商品分类
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商品分类信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商品分类列表
     *
     * @param bo 查询条件
     * @return 商品分类列表
     */
    List<GoodsCategoryVo> queryList(GoodsCategoryBo bo, IdPageQuery pageQuery);

    List<Tree<Long>> queryTreeList(GoodsCategoryBo bo);
}
