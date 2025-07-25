package org.dromara.module.lead.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.module.lead.domain.LeadInfo;
import org.dromara.module.lead.domain.vo.LeadInfoVo;

import java.io.Serializable;
import java.util.List;

/**
 * 线索信息Mapper接口
 *
 * @author weidixian
 * @date 2025-07-17
 */
public interface LeadInfoMapper extends BaseMapperPlus<LeadInfo, LeadInfoVo> {


    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default LeadInfoVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default <P extends IPage<LeadInfoVo>> P selectVoPage(IPage<LeadInfo> page, Wrapper<LeadInfo> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default List<LeadInfoVo> selectVoList(Wrapper<LeadInfo> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    int updateById(@Param(Constants.ENTITY) LeadInfo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }

}
