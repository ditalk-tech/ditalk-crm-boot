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
import org.dromara.module.customer.domain.CustomerTransferLog;
import org.dromara.module.customer.domain.bo.CustomerTransferLogBo;
import org.dromara.module.customer.domain.vo.CustomerTransferLogVo;
import org.dromara.module.customer.service.ICustomerTransferLogService;
import org.dromara.module.customer.mapper.CustomerTransferLogMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 客户转移记录Service业务层处理
 *
 * @author weidixian
 * @date 2025-08-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerTransferLogServiceImpl implements ICustomerTransferLogService {

    private final CustomerTransferLogMapper baseMapper;

    /**
     * 查询客户转移记录
     *
     * @param id 主键
     * @return 客户转移记录
     */
    @Override
    @Cacheable(cacheNames = CacheNames.CustomerTransferLog, key = "#id")
    public CustomerTransferLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客户转移记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户转移记录分页列表
     */
    @Override
    public TableDataInfo<CustomerTransferLogVo> queryPageList(CustomerTransferLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CustomerTransferLog> lqw = buildQueryWrapper(bo);
        Page<CustomerTransferLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的客户转移记录列表
     *
     * @param bo 查询条件
     * @return 客户转移记录列表
     */
    @Override
    public List<CustomerTransferLogVo> queryList(CustomerTransferLogBo bo) {
        LambdaQueryWrapper<CustomerTransferLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CustomerTransferLog> buildQueryWrapper(CustomerTransferLogBo bo) {
        LambdaQueryWrapper<CustomerTransferLog> lqw = buildWrapper(bo);
        lqw.eq(bo.getId() != null, CustomerTransferLog::getId, bo.getId());
        return lqw;
    }

    private LambdaQueryWrapper<CustomerTransferLog> buildWrapper(CustomerTransferLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CustomerTransferLog> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(CustomerTransferLog::getId);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            CustomerTransferLog::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(bo.getCustomerId() != null, CustomerTransferLog::getCustomerId, bo.getCustomerId());
        lqw.eq(bo.getOldUserId() != null, CustomerTransferLog::getOldUserId, bo.getOldUserId());
        lqw.eq(bo.getOldDeptId() != null, CustomerTransferLog::getOldDeptId, bo.getOldDeptId());
        lqw.eq(bo.getNewUserId() != null, CustomerTransferLog::getNewUserId, bo.getNewUserId());
        lqw.eq(bo.getNewDeptId() != null, CustomerTransferLog::getNewDeptId, bo.getNewDeptId());
        lqw.eq(StringUtils.isNotBlank(bo.getState()), CustomerTransferLog::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增客户转移记录
     *
     * @param bo 客户转移记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CustomerTransferLogBo bo) {
        CustomerTransferLog add = MapstructUtils.convert(bo, CustomerTransferLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户转移记录
     *
     * @param bo 客户转移记录
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerTransferLog, key = "#bo.id")
    public Boolean updateByBo(CustomerTransferLogBo bo) {
        CustomerTransferLog update = MapstructUtils.convert(bo, CustomerTransferLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CustomerTransferLog entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除客户转移记录
     *
     * @param id 主键
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = CacheNames.CustomerTransferLog, key = "#id")
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 校验并批量删除客户转移记录信息
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
     * 通过ID分页查询客户转移记录列表
     *
     * @param bo 查询条件
     * @return 客户转移记录列表
     */
    @Override
    public List<CustomerTransferLogVo> queryList(CustomerTransferLogBo bo, IdPageQuery pageQuery) {
        LambdaQueryWrapper<CustomerTransferLog> lqw = buildWrapper(bo);
        lqw.lt(pageQuery.getId() != null, CustomerTransferLog::getId, pageQuery.getId());
        return baseMapper.selectVoList(pageQuery.build(lqw));
    }

}
