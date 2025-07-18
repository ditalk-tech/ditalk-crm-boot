package org.dromara.app.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 用户信息选项视图对象 sys_user
 *
 * @author weidixian
 */
@Data
public class SysUserOptionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

}
