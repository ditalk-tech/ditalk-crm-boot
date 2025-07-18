package org.dromara.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.handler.ISysUserHandler;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户应用接口
 *
 * @author weidixian
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserHandlerImpl implements ISysUserHandler {

    private final ISysUserService sysUserService;

    @Override
    public List<SysUserVo> queryUserList(SysUserBo bo, PageQuery pageQuery) {
        TableDataInfo<SysUserVo> tableDataInfo = sysUserService.selectPageUserList(bo, pageQuery);
        return tableDataInfo.getRows();
    }
}
