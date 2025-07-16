package org.dromara.module.goods.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.constant.CommonConstants;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.IdPageQuery;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.module.goods.domain.GoodsSkuSpec;
import org.dromara.module.goods.domain.bo.GoodsSkuSpecBo;
import org.dromara.module.goods.domain.vo.GoodsSkuSpecVo;
import org.dromara.module.goods.service.IGoodsSkuSpecService;
import org.dromara.module.goods.mapper.GoodsSkuSpecMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SKU规格Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsSkuSpecServiceImpl implements IGoodsSkuSpecService {

    private final GoodsSkuSpecMapper baseMapper;

    /**
     * 查询SKU规格
     *
     * @param id 主键
     * @return SKU规格
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsSkuSpec, key = "#id")
    public GoodsSkuSpecVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询SKU规格列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return SKU规格分页列表
     */
    @Override
    public TableDataInfo<GoodsSkuSpecVo> queryPageList(GoodsSkuSpecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsSkuSpec> lqw = buildQueryWrapper(bo);
        Page<GoodsSkuSpecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的SKU规格列表
     *
     * @param bo 查询条件
     * @return SKU规格列表
     */
    @Override
    public List<GoodsSkuSpecVo> queryList(GoodsSkuSpecBo bo) {
        LambdaQueryWrapper<GoodsSkuSpec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsSkuSpec> buildQueryWrapper(GoodsSkuSpecBo bo) {
        LambdaQueryWrapper<GoodsSkuSpec> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsSkuSpec::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsSkuSpec> buildWrapper(GoodsSkuSpecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsSkuSpec> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsSkuSpec::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsSkuSpec::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getShopId() != null, GoodsSkuSpec::getShopId, bo.getShopId());
        lqw.eq(bo.getCategoryId() != null, GoodsSkuSpec::getCategoryId, bo.getCategoryId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), GoodsSkuSpec::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsSkuSpec::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增SKU规格
     *
     * @param bo SKU规格
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsSkuSpecBo bo) {
        GoodsSkuSpec add = MapstructUtils.convert(bo, GoodsSkuSpec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改SKU规格
     *
     * @param bo SKU规格
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsSkuSpec, key = "#bo.id")
    public Boolean updateByBo(GoodsSkuSpecBo bo) {
        GoodsSkuSpec update = MapstructUtils.convert(bo, GoodsSkuSpec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsSkuSpec entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除SKU规格
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsSkuSpec, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除SKU规格信息
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
     * 通过ID分页查询SKU规格列表
     *
     * @param bo 查询条件
     * @return SKU规格列表
     */
    @Override
    public List<GoodsSkuSpecVo> queryList(GoodsSkuSpecBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsSkuSpec> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsSkuSpec::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

    @Override
    public List<GoodsSkuSpecVo> queryByShopIdAndCategoryId(Long shopId, Long categoryId) {
        GoodsSkuSpecBo bo = new GoodsSkuSpecBo();
        bo.setShopId(shopId);
        bo.setCategoryId(categoryId);
        bo.setState(CommonConstants.AVAILABLE);
        LambdaQueryWrapper<GoodsSkuSpec> lqw = buildWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

}
