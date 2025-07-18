package org.dromara.handler;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysUserVo;

import java.util.List;

/**
 * 系统用户应用接口
 *
 * @author weidixian
 */
public interface ISysUserHandler {
    /**
     * 查询用户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户列表
     */
    List<SysUserVo> queryUserList(SysUserBo bo, PageQuery pageQuery);
}
