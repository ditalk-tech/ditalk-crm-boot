package org.dromara.module.shop.service;

import org.dromara.module.shop.domain.vo.ShopInfoVo;
import org.dromara.module.shop.domain.bo.ShopInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 店铺信息Service接口
 *
 * @author weidixian
 * @date 2025-07-08
 */
public interface IShopInfoService {

    /**
     * 查询店铺信息
     *
     * @param id 主键
     * @return 店铺信息
     */
    ShopInfoVo queryById(Long id);

    /**
     * 分页查询店铺信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 店铺信息分页列表
     */
    TableDataInfo<ShopInfoVo> queryPageList(ShopInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的店铺信息列表
     *
     * @param bo 查询条件
     * @return 店铺信息列表
     */
    List<ShopInfoVo> queryList(ShopInfoBo bo);

    /**
     * 新增店铺信息
     *
     * @param bo 店铺信息
     * @return 是否新增成功
     */
    Boolean insertByBo(ShopInfoBo bo);

    /**
     * 修改店铺信息
     *
     * @param bo 店铺信息
     * @return 是否修改成功
     */
    Boolean updateByBo(ShopInfoBo bo);

    /**
     * 删除店铺信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除店铺信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询店铺信息列表
     *
     * @param bo 查询条件
     * @return 店铺信息列表
     */
    List<ShopInfoVo> queryList(ShopInfoBo bo, IdPageQuery pageQuery);
}
