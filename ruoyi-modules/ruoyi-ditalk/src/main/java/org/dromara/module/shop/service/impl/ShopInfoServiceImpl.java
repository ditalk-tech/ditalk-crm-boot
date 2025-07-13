package org.dromara.module.shop.service.impl;

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
import org.dromara.module.shop.domain.ShopInfo;
import org.dromara.module.shop.domain.bo.ShopInfoBo;
import org.dromara.module.shop.domain.vo.ShopInfoOptionVo;
import org.dromara.module.shop.domain.vo.ShopInfoVo;
import org.dromara.module.shop.mapper.ShopInfoMapper;
import org.dromara.module.shop.mapper.ShopInfoOptionMapper;
import org.dromara.module.shop.service.IShopInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 店铺信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ShopInfoServiceImpl implements IShopInfoService {

    private final ShopInfoMapper baseMapper;
    private final ShopInfoOptionMapper baseOptionMapper;

    /**
     * 查询店铺信息
     *
     * @param id 主键
     * @return 店铺信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.ShopInfo, key = "#id")
    public ShopInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询店铺信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 店铺信息分页列表
     */
    @Override
    public TableDataInfo<ShopInfoVo> queryPageList(ShopInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShopInfo> lqw = buildQueryWrapper(bo);
        Page<ShopInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的店铺信息列表
     *
     * @param bo 查询条件
     * @return 店铺信息列表
     */
    @Override
    public List<ShopInfoVo> queryList(ShopInfoBo bo) {
        LambdaQueryWrapper<ShopInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShopInfo> buildQueryWrapper(ShopInfoBo bo) {
        LambdaQueryWrapper<ShopInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, ShopInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<ShopInfo> buildWrapper(ShopInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ShopInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(ShopInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            ShopInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getName()), ShopInfo::getName, bo.getName());
        lqw.between(params.get("beginRating") != null && params.get("endRating") != null,
            ShopInfo::getRating, params.get("beginRating"), params.get("endRating"));
        lqw.eq(StringUtils.isNotBlank(bo.getState()), ShopInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增店铺信息
     *
     * @param bo 店铺信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ShopInfoBo bo) {
        ShopInfo add = MapstructUtils.convert(bo, ShopInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改店铺信息
     *
     * @param bo 店铺信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.ShopInfo, key = "#bo.id")
    public Boolean updateByBo(ShopInfoBo bo) {
        ShopInfo update = MapstructUtils.convert(bo, ShopInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ShopInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除店铺信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.ShopInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除店铺信息信息
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
     * 通过ID分页查询店铺信息列表
     *
     * @param bo 查询条件
     * @return 店铺信息列表
     */
    @Override
    public List<ShopInfoVo> queryList(ShopInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<ShopInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, ShopInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

    @Override
    public TableDataInfo<ShopInfoOptionVo> queryPageOptionList(ShopInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShopInfo> lqw = buildQueryWrapper(bo);
        Page<ShopInfoOptionVo> result = baseOptionMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

}
