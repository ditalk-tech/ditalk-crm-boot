package org.dromara.module.contact.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.module.contact.domain.ContactInfo;
import org.dromara.module.contact.domain.vo.ContactInfoVo;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人信息Mapper接口
 *
 * @author weidixian
 * @date 2025-07-18
 */
public interface ContactInfoMapper extends BaseMapperPlus<ContactInfo, ContactInfoVo> {

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default ContactInfoVo selectVoById(Serializable id) {
        return BaseMapperPlus.super.selectVoById(id);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default <P extends IPage<ContactInfoVo>> P selectVoPage(IPage<ContactInfo> page, Wrapper<ContactInfo> wrapper) {
        return BaseMapperPlus.super.selectVoPage(page, wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default List<ContactInfoVo> selectVoList(Wrapper<ContactInfo> wrapper) {
        return BaseMapperPlus.super.selectVoList(wrapper);
    }

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    int updateById(@Param(Constants.ENTITY) ContactInfo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "assigned_dept" ),
        @DataColumn(key = "userName", value = "assigned_to" )
    })
    default int deleteById(Serializable id) {
        return BaseMapperPlus.super.deleteById(id);
    }

}
