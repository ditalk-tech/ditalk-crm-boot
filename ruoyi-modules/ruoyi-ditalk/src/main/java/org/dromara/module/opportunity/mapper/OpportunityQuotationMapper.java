package org.dromara.module.opportunity.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.module.opportunity.domain.OpportunityQuotation;
import org.dromara.module.opportunity.domain.vo.OpportunityQuotationVo;

import java.io.Serializable;
import java.util.List;

/**
 * 商机报价单Mapper接口
 *
 * @author weidixian
 * @date 2025-09-02
 */
public interface OpportunityQuotationMapper extends BaseMapperPlus<OpportunityQuotation, OpportunityQuotationVo> {
    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept"),
        @DataColumn(key = "userName", value = "assigned_to")
    })
    default OpportunityQuotationVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept"),
        @DataColumn(key = "userName", value = "assigned_to")
    })
    default <P extends IPage<OpportunityQuotationVo>> P selectVoPage(IPage<OpportunityQuotation> page, Wrapper<OpportunityQuotation> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept"),
        @DataColumn(key = "userName", value = "assigned_to")
    })
    default List<OpportunityQuotationVo> selectVoList(Wrapper<OpportunityQuotation> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept"),
        @DataColumn(key = "userName", value = "assigned_to")
    })
    int updateById(@Param(Constants.ENTITY) OpportunityQuotation entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept"),
        @DataColumn(key = "userName", value = "assigned_to")
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }
}
