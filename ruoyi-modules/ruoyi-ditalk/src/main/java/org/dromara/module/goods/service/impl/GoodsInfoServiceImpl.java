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
import org.dromara.module.goods.domain.GoodsInfo;
import org.dromara.module.goods.domain.bo.GoodsInfoBo;
import org.dromara.module.goods.domain.vo.GoodsInfoVo;
import org.dromara.module.goods.service.IGoodsInfoService;
import org.dromara.module.goods.mapper.GoodsInfoMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsInfoServiceImpl implements IGoodsInfoService {

    private final GoodsInfoMapper baseMapper;

    /**
     * 查询商品信息
     *
     * @param id 主键
     * @return 商品信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsInfo, key = "#id")
    public GoodsInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品信息分页列表
     */
    @Override
    public TableDataInfo<GoodsInfoVo> queryPageList(GoodsInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsInfo> lqw = buildQueryWrapper(bo);
        Page<GoodsInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品信息列表
     *
     * @param bo 查询条件
     * @return 商品信息列表
     */
    @Override
    public List<GoodsInfoVo> queryList(GoodsInfoBo bo) {
        LambdaQueryWrapper<GoodsInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsInfo> buildQueryWrapper(GoodsInfoBo bo) {
        LambdaQueryWrapper<GoodsInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsInfo> buildWrapper(GoodsInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getShopId() != null, GoodsInfo::getShopId, bo.getShopId());
        lqw.eq(bo.getCategoryId() != null, GoodsInfo::getCategoryId, bo.getCategoryId());
        lqw.eq(StringUtils.isNotBlank(bo.getSpuCode()), GoodsInfo::getSpuCode, bo.getSpuCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), GoodsInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), GoodsInfo::getBarCode, bo.getBarCode());
        lqw.eq(bo.getBrandId() != null, GoodsInfo::getBrandId, bo.getBrandId());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商品信息
     *
     * @param bo 商品信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsInfoBo bo) {
        GoodsInfo add = MapstructUtils.convert(bo, GoodsInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品信息
     *
     * @param bo 商品信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsInfo, key = "#bo.id")
    public Boolean updateByBo(GoodsInfoBo bo) {
        GoodsInfo update = MapstructUtils.convert(bo, GoodsInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商品信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商品信息信息
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
     * 通过ID分页查询商品信息列表
     *
     * @param bo 查询条件
     * @return 商品信息列表
     */
    @Override
    public List<GoodsInfoVo> queryList(GoodsInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
