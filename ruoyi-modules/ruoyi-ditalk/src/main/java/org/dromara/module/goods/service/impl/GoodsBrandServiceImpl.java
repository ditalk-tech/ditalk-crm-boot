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
import org.dromara.module.goods.domain.GoodsBrand;
import org.dromara.module.goods.domain.bo.GoodsBrandBo;
import org.dromara.module.goods.domain.vo.GoodsBrandVo;
import org.dromara.module.goods.service.IGoodsBrandService;
import org.dromara.module.goods.mapper.GoodsBrandMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品品牌信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-09
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsBrandServiceImpl implements IGoodsBrandService {

    private final GoodsBrandMapper baseMapper;

    /**
     * 查询商品品牌信息
     *
     * @param id 主键
     * @return 商品品牌信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsBrand, key = "#id")
    public GoodsBrandVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品品牌信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品品牌信息分页列表
     */
    @Override
    public TableDataInfo<GoodsBrandVo> queryPageList(GoodsBrandBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsBrand> lqw = buildQueryWrapper(bo);
        Page<GoodsBrandVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品品牌信息列表
     *
     * @param bo 查询条件
     * @return 商品品牌信息列表
     */
    @Override
    public List<GoodsBrandVo> queryList(GoodsBrandBo bo) {
        LambdaQueryWrapper<GoodsBrand> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsBrand> buildQueryWrapper(GoodsBrandBo bo) {
        LambdaQueryWrapper<GoodsBrand> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsBrand::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsBrand> buildWrapper(GoodsBrandBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsBrand> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsBrand::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsBrand::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getName()), GoodsBrand::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getEnglishName()), GoodsBrand::getEnglishName, bo.getEnglishName());
        lqw.eq(StringUtils.isNotBlank(bo.getCountry()), GoodsBrand::getCountry, bo.getCountry());
        lqw.like(StringUtils.isNotBlank(bo.getWebsite()), GoodsBrand::getWebsite, bo.getWebsite());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsBrand::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商品品牌信息
     *
     * @param bo 商品品牌信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsBrandBo bo) {
        GoodsBrand add = MapstructUtils.convert(bo, GoodsBrand.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品品牌信息
     *
     * @param bo 商品品牌信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsBrand, key = "#bo.id")
    public Boolean updateByBo(GoodsBrandBo bo) {
        GoodsBrand update = MapstructUtils.convert(bo, GoodsBrand.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsBrand entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商品品牌信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsBrand, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商品品牌信息信息
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
     * 通过ID分页查询商品品牌信息列表
     *
     * @param bo 查询条件
     * @return 商品品牌信息列表
     */
    @Override
    public List<GoodsBrandVo> queryList(GoodsBrandBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsBrand> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsBrand::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
