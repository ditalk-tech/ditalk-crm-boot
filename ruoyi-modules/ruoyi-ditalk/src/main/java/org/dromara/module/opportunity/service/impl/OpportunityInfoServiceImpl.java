package org.dromara.module.opportunity.service.impl;

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
import org.dromara.module.opportunity.domain.OpportunityInfo;
import org.dromara.module.opportunity.domain.bo.OpportunityInfoBo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.module.opportunity.service.IOpportunityInfoService;
import org.dromara.module.opportunity.mapper.OpportunityInfoMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商机信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpportunityInfoServiceImpl implements IOpportunityInfoService {

    private final OpportunityInfoMapper baseMapper;

    /**
     * 查询商机信息
     *
     * @param id 主键
     * @return 商机信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.OpportunityInfo, key = "#id")
    public OpportunityInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商机信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商机信息分页列表
     */
    @Override
    public TableDataInfo<OpportunityInfoVo> queryPageList(OpportunityInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityInfo> lqw = buildQueryWrapper(bo);
        Page<OpportunityInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商机信息列表
     *
     * @param bo 查询条件
     * @return 商机信息列表
     */
    @Override
    public List<OpportunityInfoVo> queryList(OpportunityInfoBo bo) {
        LambdaQueryWrapper<OpportunityInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OpportunityInfo> buildQueryWrapper(OpportunityInfoBo bo) {
        LambdaQueryWrapper<OpportunityInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, OpportunityInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<OpportunityInfo> buildWrapper(OpportunityInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpportunityInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(OpportunityInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            OpportunityInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), OpportunityInfo::getTitle, bo.getTitle());
        lqw.eq(bo.getCustomerId() != null, OpportunityInfo::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getAssignedTo() != null, OpportunityInfo::getAssignedTo, bo.getAssignedTo());
        lqw.eq(bo.getOrderId() != null, OpportunityInfo::getOrderId, bo.getOrderId());
        lqw.between(params.get("beginCloseDate") != null && params.get("endCloseDate") != null,
            OpportunityInfo::getCloseDate, params.get("beginCloseDate"), params.get("endCloseDate"));
        lqw.eq(StringUtils.isNotBlank(bo.getState()), OpportunityInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增商机信息
     *
     * @param bo 商机信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(OpportunityInfoBo bo) {
        OpportunityInfo add = MapstructUtils.convert(bo, OpportunityInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商机信息
     *
     * @param bo 商机信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityInfo, key = "#bo.id")
    public Boolean updateByBo(OpportunityInfoBo bo) {
        OpportunityInfo update = MapstructUtils.convert(bo, OpportunityInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OpportunityInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除商机信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.OpportunityInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除商机信息信息
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
     * 通过ID分页查询商机信息列表
     *
     * @param bo 查询条件
     * @return 商机信息列表
     */
    @Override
    public List<OpportunityInfoVo> queryList(OpportunityInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<OpportunityInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, OpportunityInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
