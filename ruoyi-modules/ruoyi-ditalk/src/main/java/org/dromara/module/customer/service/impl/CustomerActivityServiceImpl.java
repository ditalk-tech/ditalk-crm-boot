package org.dromara.module.customer.service.impl;

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
import org.dromara.module.customer.domain.CustomerActivity;
import org.dromara.module.customer.domain.bo.CustomerActivityBo;
import org.dromara.module.customer.domain.vo.CustomerActivityVo;
import org.dromara.module.customer.service.ICustomerActivityService;
import org.dromara.module.customer.mapper.CustomerActivityMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 客户活动记录Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerActivityServiceImpl implements ICustomerActivityService {

    private final CustomerActivityMapper baseMapper;

    /**
     * 查询客户活动记录
     *
     * @param id 主键
     * @return 客户活动记录
     */
    @Override
    @Cacheable(cacheNames = CacheNames.CustomerActivity, key = "#id")
    public CustomerActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客户活动记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户活动记录分页列表
     */
    @Override
    public TableDataInfo<CustomerActivityVo> queryPageList(CustomerActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CustomerActivity> lqw = buildQueryWrapper(bo);
        Page<CustomerActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的客户活动记录列表
     *
     * @param bo 查询条件
     * @return 客户活动记录列表
     */
    @Override
    public List<CustomerActivityVo> queryList(CustomerActivityBo bo) {
        LambdaQueryWrapper<CustomerActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CustomerActivity> buildQueryWrapper(CustomerActivityBo bo) {
        LambdaQueryWrapper<CustomerActivity> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, CustomerActivity::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<CustomerActivity> buildWrapper(CustomerActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CustomerActivity> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(CustomerActivity::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            CustomerActivity::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getCustomerId() != null, CustomerActivity::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getContactId() != null, CustomerActivity::getContactId, bo.getContactId());
        lqw.eq(bo.getOpportunityId() != null, CustomerActivity::getOpportunityId, bo.getOpportunityId());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), CustomerActivity::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getSubject()), CustomerActivity::getSubject, bo.getSubject());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), CustomerActivity::getDescription, bo.getDescription());
        lqw.between(params.get("beginActivityTime") != null && params.get("endActivityTime") != null,
            CustomerActivity::getActivityTime, params.get("beginActivityTime"), params.get("endActivityTime"));
        return lqw;
    }

    /**
     * 新增客户活动记录
     *
     * @param bo 客户活动记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CustomerActivityBo bo) {
        CustomerActivity add = MapstructUtils.convert(bo, CustomerActivity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户活动记录
     *
     * @param bo 客户活动记录
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerActivity, key = "#bo.id")
    public Boolean updateByBo(CustomerActivityBo bo) {
        CustomerActivity update = MapstructUtils.convert(bo, CustomerActivity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CustomerActivity entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除客户活动记录
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerActivity, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除客户活动记录信息
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
     * 通过ID分页查询客户活动记录列表
     *
     * @param bo 查询条件
     * @return 客户活动记录列表
     */
    @Override
    public List<CustomerActivityVo> queryList(CustomerActivityBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<CustomerActivity> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, CustomerActivity::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
