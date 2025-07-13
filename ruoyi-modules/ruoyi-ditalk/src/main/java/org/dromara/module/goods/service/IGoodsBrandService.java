package org.dromara.module.goods.service;

import org.dromara.module.goods.domain.vo.GoodsBrandOptionVo;
import org.dromara.module.goods.domain.vo.GoodsBrandVo;
import org.dromara.module.goods.domain.bo.GoodsBrandBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品品牌信息Service接口
 *
 * @author weidixian
 * @date 2025-07-09
 */
public interface IGoodsBrandService {

    /**
     * 查询商品品牌信息
     *
     * @param id 主键
     * @return 商品品牌信息
     */
    GoodsBrandVo queryById(Long id);

    /**
     * 分页查询商品品牌信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品品牌信息分页列表
     */
    TableDataInfo<GoodsBrandVo> queryPageList(GoodsBrandBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品品牌信息列表
     *
     * @param bo 查询条件
     * @return 商品品牌信息列表
     */
    List<GoodsBrandVo> queryList(GoodsBrandBo bo);

    /**
     * 新增商品品牌信息
     *
     * @param bo 商品品牌信息
     * @return 是否新增成功
     */
    Boolean insertByBo(GoodsBrandBo bo);

    /**
     * 修改商品品牌信息
     *
     * @param bo 商品品牌信息
     * @return 是否修改成功
     */
    Boolean updateByBo(GoodsBrandBo bo);

    /**
     * 删除商品品牌信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除商品品牌信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询商品品牌信息列表
     *
     * @param bo 查询条件
     * @return 商品品牌信息列表
     */
    List<GoodsBrandVo> queryList(GoodsBrandBo bo, IdPageQuery pageQuery);

    TableDataInfo<GoodsBrandOptionVo> queryPageOptionList(GoodsBrandBo bo, PageQuery pageQuery);
}
