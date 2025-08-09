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
import org.dromara.module.goods.domain.GoodsInfoSnapshot;
import org.dromara.module.goods.domain.bo.GoodsInfoSnapshotBo;
import org.dromara.module.goods.domain.vo.GoodsInfoSnapshotVo;
import org.dromara.module.goods.service.IGoodsInfoSnapshotService;
import org.dromara.module.goods.mapper.GoodsInfoSnapshotMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品信息快照Service业务层处理
 *
 * @author weidixian
 * @date 2025-08-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsInfoSnapshotServiceImpl implements IGoodsInfoSnapshotService {

    private final GoodsInfoSnapshotMapper baseMapper;

    /**
     * 查询商品信息快照
     *
     * @param id 主键
     * @return 商品信息快照
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsInfoSnapshot, key = "#id")
    public GoodsInfoSnapshotVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品信息快照列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品信息快照分页列表
     */
    @Override
    public TableDataInfo<GoodsInfoSnapshotVo> queryPageList(GoodsInfoSnapshotBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsInfoSnapshot> lqw = buildQueryWrapper(bo);
        Page<GoodsInfoSnapshotVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品信息快照列表
     *
     * @param bo 查询条件
     * @return 商品信息快照列表
     */
    @Override
    public List<GoodsInfoSnapshotVo> queryList(GoodsInfoSnapshotBo bo) {
        LambdaQueryWrapper<GoodsInfoSnapshot> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsInfoSnapshot> buildQueryWrapper(GoodsInfoSnapshotBo bo) {
        LambdaQueryWrapper<GoodsInfoSnapshot> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsInfoSnapshot::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsInfoSnapshot> buildWrapper(GoodsInfoSnapshotBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsInfoSnapshot> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsInfoSnapshot::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsInfoSnapshot::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getGoodsId() != null, GoodsInfoSnapshot::getGoodsId, bo.getGoodsId());
        lqw.eq(bo.getShopId() != null, GoodsInfoSnapshot::getShopId, bo.getShopId());
        lqw.eq(bo.getCategoryId() != null, GoodsInfoSnapshot::getCategoryId, bo.getCategoryId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuCode()), GoodsInfoSnapshot::getSpuCode, bo.getSpuCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), GoodsInfoSnapshot::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getSubtitle()), GoodsInfoSnapshot::getSubtitle, bo.getSubtitle());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), GoodsInfoSnapshot::getBarCode, bo.getBarCode());
        lqw.eq(bo.getBrandId() != null, GoodsInfoSnapshot::getBrandId, bo.getBrandId());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsInfoSnapshot::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商品信息快照
     *
     * @param bo 商品信息快照
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsInfoSnapshotBo bo) {
        GoodsInfoSnapshot add = MapstructUtils.convert(bo, GoodsInfoSnapshot.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品信息快照
     *
     * @param bo 商品信息快照
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsInfoSnapshot, key = "#bo.id")
    public Boolean updateByBo(GoodsInfoSnapshotBo bo) {
        GoodsInfoSnapshot update = MapstructUtils.convert(bo, GoodsInfoSnapshot.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsInfoSnapshot entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商品信息快照
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsInfoSnapshot, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商品信息快照信息
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
     * 通过ID分页查询商品信息快照列表
     *
     * @param bo 查询条件
     * @return 商品信息快照列表
     */
    @Override
    public List<GoodsInfoSnapshotVo> queryList(GoodsInfoSnapshotBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsInfoSnapshot> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsInfoSnapshot::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
