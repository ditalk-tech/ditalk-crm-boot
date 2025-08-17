package org.dromara.module.contract.service.impl;

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
import org.dromara.module.contract.domain.ContractInfo;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.service.IContractInfoService;
import org.dromara.module.contract.mapper.ContractInfoMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 合同信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-08-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ContractInfoServiceImpl implements IContractInfoService {

    private final ContractInfoMapper baseMapper;

    /**
     * 查询合同信息
     *
     * @param id 主键
     * @return 合同信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.ContractInfo, key = "#id")
    public ContractInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询合同信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 合同信息分页列表
     */
    @Override
    public TableDataInfo<ContractInfoVo> queryPageList(ContractInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ContractInfo> lqw = buildQueryWrapper(bo);
        Page<ContractInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的合同信息列表
     *
     * @param bo 查询条件
     * @return 合同信息列表
     */
    @Override
    public List<ContractInfoVo> queryList(ContractInfoBo bo) {
        LambdaQueryWrapper<ContractInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ContractInfo> buildQueryWrapper(ContractInfoBo bo) {
        LambdaQueryWrapper<ContractInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, ContractInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<ContractInfo> buildWrapper(ContractInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ContractInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(ContractInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            ContractInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), ContractInfo::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), ContractInfo::getTitle, bo.getTitle());
        lqw.eq(bo.getCustomerId() != null, ContractInfo::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getContactId() != null, ContractInfo::getContactId, bo.getContactId());
        lqw.eq(bo.getOpportunityId() != null, ContractInfo::getOpportunityId, bo.getOpportunityId());
        lqw.between(params.get("beginSignDate") != null && params.get("endSignDate") != null,
            ContractInfo::getSignDate, params.get("beginSignDate"), params.get("endSignDate"));
        lqw.between(params.get("beginStartDate") != null && params.get("endStartDate") != null,
            ContractInfo::getStartDate, params.get("beginStartDate"), params.get("endStartDate"));
        lqw.between(params.get("beginEndDate") != null && params.get("endEndDate") != null,
            ContractInfo::getEndDate, params.get("beginEndDate"), params.get("endEndDate"));
        lqw.eq(bo.getAssignedTo() != null, ContractInfo::getAssignedTo, bo.getAssignedTo());
        lqw.eq(bo.getAssignedDept() != null, ContractInfo::getAssignedDept, bo.getAssignedDept());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), ContractInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增合同信息
     *
     * @param bo 合同信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ContractInfoBo bo) {
        ContractInfo add = MapstructUtils.convert(bo, ContractInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改合同信息
     *
     * @param bo 合同信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.ContractInfo, key = "#bo.id")
    public Boolean updateByBo(ContractInfoBo bo) {
        ContractInfo update = MapstructUtils.convert(bo, ContractInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ContractInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除合同信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.ContractInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除合同信息信息
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
     * 通过ID分页查询合同信息列表
     *
     * @param bo 查询条件
     * @return 合同信息列表
     */
    @Override
    public List<ContractInfoVo> queryList(ContractInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<ContractInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, ContractInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
