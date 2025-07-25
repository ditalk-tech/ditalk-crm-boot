package org.dromara.module.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.module.customer.domain.CustomerInfo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;

import java.io.Serializable;
import java.util.List;

/**
 * 客户信息Mapper接口
 *
 * @author weidixian
 * @date 2025-07-17
 */
public interface CustomerInfoMapper extends BaseMapperPlus<CustomerInfo, CustomerInfoVo> {

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default CustomerInfoVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default <P extends IPage<CustomerInfoVo>> P selectVoPage(IPage<CustomerInfo> page, Wrapper<CustomerInfo> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default List<CustomerInfoVo> selectVoList(Wrapper<CustomerInfo> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    int updateById(@Param(Constants.ENTITY) CustomerInfo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }

}
