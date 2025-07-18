package org.dromara.app.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.app.domain.vo.SysUserOptionVo;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.ISysUserHandler;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户信息
 *
 * @author weidixian
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/sys/user")
public class AppSysUserController {

    private final ISysUserHandler sysUserHandler;
    private final ISysUserService sysUserService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/listOption")
    public R<List<SysUserOptionVo>> listOption(SysUserBo bo, PageQuery pageQuery) {
        List<SysUserVo> sysUserVos = sysUserHandler.queryUserList(bo, pageQuery);
        BeanUtil.copyToList(sysUserVos, SysUserOptionVo.class);
        return R.ok(BeanUtil.copyToList(sysUserVos, SysUserOptionVo.class));
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/getMyInfo")
    public R<SysUserOptionVo> getMyInfo() {
        SysUserOptionVo vo = new SysUserOptionVo();
        vo.setUserId(LoginHelper.getUserId());
        vo.setDeptId(LoginHelper.getDeptId());
        vo.setUserName(LoginHelper.getUsername());
        return R.ok(vo);
    }

}
