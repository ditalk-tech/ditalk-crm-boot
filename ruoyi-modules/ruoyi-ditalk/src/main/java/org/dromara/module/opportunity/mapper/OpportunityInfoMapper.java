package org.dromara.module.opportunity.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.module.opportunity.domain.OpportunityInfo;
import org.dromara.module.opportunity.domain.vo.OpportunityInfoVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.io.Serializable;
import java.util.List;

/**
 * 商机信息Mapper接口
 *
 * @author weidixian
 * @date 2025-07-23
 */
public interface OpportunityInfoMapper extends BaseMapperPlus<OpportunityInfo, OpportunityInfoVo> {
    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default OpportunityInfoVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default <P extends IPage<OpportunityInfoVo>> P selectVoPage(IPage<OpportunityInfo> page, Wrapper<OpportunityInfo> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default List<OpportunityInfoVo> selectVoList(Wrapper<OpportunityInfo> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    int updateById(@Param(Constants.ENTITY) OpportunityInfo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }

}
