package org.dromara.module.goods.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.IdPageQuery;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.module.goods.domain.GoodsSku;
import org.dromara.module.goods.domain.bo.GoodsSkuBo;
import org.dromara.module.goods.domain.vo.GoodsSkuVo;
import org.dromara.module.goods.service.IGoodsSkuService;
import org.dromara.module.goods.mapper.GoodsSkuMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品SKUService业务层处理
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsSkuServiceImpl implements IGoodsSkuService {

    private final GoodsSkuMapper baseMapper;

    /**
     * 查询商品SKU
     *
     * @param id 主键
     * @return 商品SKU
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsSku, key = "#id")
    public GoodsSkuVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品SKU列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品SKU分页列表
     */
    @Override
    public TableDataInfo<GoodsSkuVo> queryPageList(GoodsSkuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsSku> lqw = buildQueryWrapper(bo);
        Page<GoodsSkuVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品SKU列表
     *
     * @param bo 查询条件
     * @return 商品SKU列表
     */
    @Override
    public List<GoodsSkuVo> queryList(GoodsSkuBo bo) {
        LambdaQueryWrapper<GoodsSku> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsSku> buildQueryWrapper(GoodsSkuBo bo) {
        LambdaQueryWrapper<GoodsSku> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsSku::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsSku> buildWrapper(GoodsSkuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsSku> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsSku::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsSku::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getShopId() != null, GoodsSku::getShopId, bo.getShopId());
        lqw.eq(bo.getGoodsId() != null, GoodsSku::getGoodsId, bo.getGoodsId());
        lqw.eq(StringUtils.isNotBlank(bo.getSkuSn()), GoodsSku::getSkuSn, bo.getSkuSn());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsSku::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商品SKU
     *
     * @param bo 商品SKU
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsSkuBo bo) {
        GoodsSku add = MapstructUtils.convert(bo, GoodsSku.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品SKU
     *
     * @param bo 商品SKU
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsSku, key = "#bo.id")
    public Boolean updateByBo(GoodsSkuBo bo) {
        GoodsSku update = MapstructUtils.convert(bo, GoodsSku.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsSku entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商品SKU
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsSku, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商品SKU信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @DSTransactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        Boolean flag = true;
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        for (Long id : ids) {
            flag = flag && SpringUtils.getAopProxy(this).deleteById(id);
        }
        return flag;
    }

    /**
     * 通过ID分页查询商品SKU列表
     *
     * @param bo 查询条件
     * @return 商品SKU列表
     */
    @Override
    public List<GoodsSkuVo> queryList(GoodsSkuBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsSku> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsSku::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
