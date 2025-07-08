package org.dromara.module.goods.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CacheNames;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.utils.*;
import org.dromara.common.mybatis.core.page.IdPageQuery;
import org.dromara.module.goods.domain.GoodsCategory;
import org.dromara.module.goods.domain.bo.GoodsCategoryBo;
import org.dromara.module.goods.domain.vo.GoodsCategoryVo;
import org.dromara.module.goods.mapper.GoodsCategoryMapper;
import org.dromara.module.goods.service.IGoodsCategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsCategoryServiceImpl implements IGoodsCategoryService {

    private final GoodsCategoryMapper baseMapper;

    /**
     * 查询商品分类
     *
     * @param id 主键
     * @return 商品分类
     */
    @Override
    @Cacheable(cacheNames = CacheNames.GoodsCategory, key = "#id")
    public GoodsCategoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询符合条件的商品分类列表
     *
     * @param bo 查询条件
     * @return 商品分类列表
     */
    @Override
    public List<GoodsCategoryVo> queryList(GoodsCategoryBo bo) {
        LambdaQueryWrapper<GoodsCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsCategory> buildQueryWrapper(GoodsCategoryBo bo) {
        LambdaQueryWrapper<GoodsCategory> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, GoodsCategory::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<GoodsCategory> buildWrapper(GoodsCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsCategory> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(GoodsCategory::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            GoodsCategory::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getShopId() != null, GoodsCategory::getShopId, bo.getShopId());
        lqw.eq(bo.getParentId() != null, GoodsCategory::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getAncestors()), GoodsCategory::getAncestors, bo.getAncestors());
        lqw.like(StringUtils.isNotBlank(bo.getName()), GoodsCategory::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), GoodsCategory::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商品分类
     *
     * @param bo 商品分类
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(GoodsCategoryBo bo) {
        GoodsCategory add = MapstructUtils.convert(bo, GoodsCategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品分类
     *
     * @param bo 商品分类
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsCategory, key = "#bo.id")
    public Boolean updateByBo(GoodsCategoryBo bo) {
        GoodsCategory update = MapstructUtils.convert(bo, GoodsCategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsCategory entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商品分类
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.GoodsCategory, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商品分类信息
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
     * 通过ID分页查询商品分类列表
     *
     * @param bo 查询条件
     * @return 商品分类列表
     */
    @Override
    public List<GoodsCategoryVo> queryList(GoodsCategoryBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<GoodsCategory> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, GoodsCategory::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

    /**
     * 查询树结构信息
     *
     * @param bo
     * @return 树信息集合
     */
    @Override
    public List<Tree<Long>> queryTreeList(GoodsCategoryBo bo) {
        LambdaQueryWrapper<GoodsCategory> lqw = buildQueryWrapper(bo);
        List<GoodsCategoryVo> categoryVos = baseMapper.selectVoList(lqw);
        return buildTreeSelect(categoryVos);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param categoryVos
     * @return 下拉树结构列表
     */
    public List<Tree<Long>> buildTreeSelect(List<GoodsCategoryVo> categoryVos) {
        if (CollUtil.isEmpty(categoryVos)) {
            return CollUtil.newArrayList();
        }
        // 获取当前列表中每一个节点的parentId，然后在列表中查找是否有id与其parentId对应，若无对应，则表明此时节点列表中，该节点在当前列表中属于顶级节点
        List<Tree<Long>> treeList = CollUtil.newArrayList();
        for (GoodsCategoryVo vo : categoryVos) {
            Long parentId = vo.getParentId();
            GoodsCategoryVo goodsCategoryVo = StreamUtils.findFirst(categoryVos, it -> it.getId().longValue() == parentId);
            if (ObjectUtil.isNull(goodsCategoryVo)) {
                List<Tree<Long>> trees = TreeBuildUtils.build(categoryVos, parentId, (categoryVo, tree) ->
                    tree.setId(categoryVo.getId())
                        .setParentId(categoryVo.getParentId())
                        .setName(categoryVo.getName())
                        .setWeight(categoryVo.getSortOrder())
                        .putExtra("disabled", SystemConstants.DISABLE.equals(categoryVo.getState())));
                Tree<Long> tree = StreamUtils.findFirst(trees, it -> it.getId().longValue() == vo.getId());
                treeList.add(tree);
            }
        }
        return treeList;
    }

}
