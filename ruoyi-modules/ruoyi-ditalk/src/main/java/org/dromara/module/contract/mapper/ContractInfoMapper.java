package org.dromara.module.contract.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.module.contract.domain.ContractInfo;
import org.dromara.module.contract.domain.vo.ContractInfoVo;

import java.io.Serializable;
import java.util.List;

/**
 * 合同信息Mapper接口
 *
 * @author weidixian
 * @date 2025-08-17
 */
public interface ContractInfoMapper extends BaseMapperPlus<ContractInfo, ContractInfoVo> {

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default ContractInfoVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default <P extends IPage<ContractInfoVo>> P selectVoPage(IPage<ContractInfo> page, Wrapper<ContractInfo> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default List<ContractInfoVo> selectVoList(Wrapper<ContractInfo> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    int updateById(@Param(Constants.ENTITY) ContractInfo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }
}
