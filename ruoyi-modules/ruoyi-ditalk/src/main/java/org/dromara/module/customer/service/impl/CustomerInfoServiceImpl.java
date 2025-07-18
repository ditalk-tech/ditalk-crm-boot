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
import org.dromara.module.customer.domain.CustomerInfo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.dromara.module.customer.mapper.CustomerInfoMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户信息Service业务层处理
 *
 * @author weidixian
 * @date 2025-07-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerInfoServiceImpl implements ICustomerInfoService {

    private final CustomerInfoMapper baseMapper;

    /**
     * 查询客户信息
     *
     * @param id 主键
     * @return 客户信息
     */
    @Override
    @Cacheable(cacheNames = CacheNames.CustomerInfo, key = "#id")
    public CustomerInfoVo queryById(Long id) {
        CustomerInfoVo customerInfoVo = baseMapper.selectVoById(id);
        if (customerInfoVo != null && customerInfoVo.getConvertedTime() != null) {
            return customerInfoVo;
        } else {
            return null;
        }
    }

    /**
     * 分页查询客户信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户信息分页列表
     */
    @Override
    public TableDataInfo<CustomerInfoVo> queryPageList(CustomerInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CustomerInfo> lqw = buildQueryWrapper(bo);
        Page<CustomerInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的客户信息列表
     *
     * @param bo 查询条件
     * @return 客户信息列表
     */
    @Override
    public List<CustomerInfoVo> queryList(CustomerInfoBo bo) {
        LambdaQueryWrapper<CustomerInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CustomerInfo> buildQueryWrapper(CustomerInfoBo bo) {
        LambdaQueryWrapper<CustomerInfo> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, CustomerInfo::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<CustomerInfo> buildWrapper(CustomerInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CustomerInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(CustomerInfo::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            CustomerInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getName()), CustomerInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), CustomerInfo::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), CustomerInfo::getSource, bo.getSource());
        lqw.eq(StringUtils.isNotBlank(bo.getIndustry()), CustomerInfo::getIndustry, bo.getIndustry());
        lqw.eq(StringUtils.isNotBlank(bo.getTier()), CustomerInfo::getTier, bo.getTier());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), CustomerInfo::getAddress, bo.getAddress());
        lqw.eq(bo.getAssignedTo() != null, CustomerInfo::getAssignedTo, bo.getAssignedTo());
        lqw.eq(bo.getContactId() != null, CustomerInfo::getContactId, bo.getContactId());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), CustomerInfo::getState, bo.getState());
        lqw.eq(bo.getConvertedBy() != null, CustomerInfo::getConvertedBy, bo.getConvertedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getLeadState()), CustomerInfo::getLeadState, bo.getLeadState());
        // lqw.eq(bo.getConvertedTime() != null, CustomerInfo::getConvertedTime, bo.getConvertedTime());
        lqw.isNotNull(CustomerInfo::getConvertedTime); // 已转化的客户
        return lqw;
    }

    /**
     * 新增客户信息
     *
     * @param bo 客户信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CustomerInfoBo bo) {
        if (bo.getConvertedTime() == null) {
            bo.setConvertedTime(new Date()); // 客户转化时间不能为空
        }
        CustomerInfo add = MapstructUtils.convert(bo, CustomerInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户信息
     *
     * @param bo 客户信息
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerInfo, key = "#bo.id")
    public Boolean updateByBo(CustomerInfoBo bo) {
        CustomerInfo update = MapstructUtils.convert(bo, CustomerInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CustomerInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除客户信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerInfo, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除客户信息信息
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
     * 通过ID分页查询客户信息列表
     *
     * @param bo 查询条件
     * @return 客户信息列表
     */
    @Override
    public List<CustomerInfoVo> queryList(CustomerInfoBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<CustomerInfo> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, CustomerInfo::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
