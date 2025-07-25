package org.dromara.module.lead.service.impl;

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
import org.dromara.module.lead.domain.LeadInfo;
import org.dromara.module.lead.domain.bo.LeadInfoBo;
import org.dromara.module.lead.domain.vo.LeadInfoVo;
import org.dromara.module.lead.mapper.LeadInfoMapper;
import org.dromara.module.lead.service.ILeadInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 线索信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LeadInfoServiceImpl implements ILeadInfoService {

    private final LeadInfoMapper baseMapper;

    /**
     * 查询线索信息
     *
     * @param id 主键
     * @return 线索信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.LeadInfo, key = "#id")
    public LeadInfoVo queryById(Long id) {
        LeadInfoVo leadInfoVo = baseMapper.selectVoById(id);
        if (leadInfoVo != null && leadInfoVo.getConvertedTime() == null) {
            return leadInfoVo;
        } else {
            return null;
        }
    }

    /**
     * 分页查询线索信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 线索信息分页列表
     */
    @Override
    public TableDataInfo<LeadInfoVo> queryPageList(LeadInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LeadInfo> lqw = buildQueryWrapper(bo);
        Page<LeadInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的线索信息列表
     *
     * @param bo 查询条件
     * @return 线索信息列表
     */
    @Override
    public List<LeadInfoVo> queryList(LeadInfoBo bo) {
        LambdaQueryWrapper<LeadInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LeadInfo> buildQueryWrapper(LeadInfoBo bo) {
        LambdaQueryWrapper<LeadInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, LeadInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<LeadInfo> buildWrapper(LeadInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LeadInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(LeadInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            LeadInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getName()), LeadInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), LeadInfo::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), LeadInfo::getSource, bo.getSource());
        lqw.eq(StringUtils.isNotBlank(bo.getIndustry()), LeadInfo::getIndustry, bo.getIndustry());
        lqw.eq(StringUtils.isNotBlank(bo.getTier()), LeadInfo::getTier, bo.getTier());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), LeadInfo::getAddress, bo.getAddress());
        lqw.eq(bo.getAssignedTo() != null, LeadInfo::getAssignedTo, bo.getAssignedTo());
        lqw.eq(bo.getAssignedDept() != null, LeadInfo::getAssignedDept, bo.getAssignedDept());
        lqw.eq(bo.getContactId() != null, LeadInfo::getContactId, bo.getContactId());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), LeadInfo::getState, bo.getState());
        lqw.eq(bo.getConvertedBy() != null, LeadInfo::getConvertedBy, bo.getConvertedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getLeadState()), LeadInfo::getLeadState, bo.getLeadState());
        lqw.between(params.get("beginConvertedTime") != null && params.get("endConvertedTime") != null,
            LeadInfo::getConvertedTime, params.get("beginConvertedTime"), params.get("endConvertedTime"));
        lqw.isNull(LeadInfo::getConvertedTime); // 未转化的客户
        lqw.isNull(params.get("isPublic") != null && (Boolean)params.get("isPublic"), LeadInfo::getAssignedTo); // 只查询未指派的客户，即公海客户
        return lqw;
    }

    /**
     * 新增线索信息
     *
     * @param bo 线索信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(LeadInfoBo bo) {
        bo.setConvertedTime(null); // 新增时不设置转化时间
        LeadInfo add = MapstructUtils.convert(bo, LeadInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改线索信息
     *
     * @param bo 线索信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.LeadInfo, key = "#bo.id")
    public Boolean updateByBo(LeadInfoBo bo) {
        bo.setConvertedTime(null); // 修改时不设置转化时间
        LeadInfo update = MapstructUtils.convert(bo, LeadInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(LeadInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除线索信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.LeadInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除线索信息信息
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
     * 通过ID分页查询线索信息列表
     *
     * @param bo 查询条件
     * @return 线索信息列表
     */
    @Override
    public List<LeadInfoVo> queryList(LeadInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<LeadInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, LeadInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
