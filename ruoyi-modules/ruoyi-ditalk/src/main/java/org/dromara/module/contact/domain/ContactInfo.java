package org.dromara.module.contact.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 联系人信息对象 contact_info
 *
 * @author weidixian
 * @date 2025-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contact_info")
public class ContactInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 乐观锁
     */
    @Version
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 姓氏
     */
    private String lastName;

    /**
     * 名称
     */
    private String firstName;

    /**
     * 头像
     */
    private Long avatar;

    /**
     * 姓名拼音
     */
    private String pinyin;

    /**
     * 性别
     */
    private String gender;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 职位
     */
    private String position;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 户籍
     */
    private String placeOfOrigin;

    /**
     * 居住地
     */
    private String address;

    /**
     * 毕业学校
     */
    private String graduationSchool;

    /**
     * 学历
     */
    private String qualification;

    /**
     * 社会角色
     */
    private String socialRole;

    /**
     * 最近接触时间
     */
    private Date lastContactTime;

    /**
     * 接触频率
     */
    private String contactFrequency;

    /**
     * 微信
     */
    private String wechat;

    /**
     * QQ
     */
    private String qq;

    /**
     * 钉钉
     */
    private String dingTalk;

    /**
     * 飞书
     */
    private String lark;

    /**
     * WhatsApp
     */
    private String whatsApp;

    /**
     * Facebook
     */
    private String facebook;

    /**
     * 状态
     */
    private String state;

    /**
     * 分配到
     */
    private Long assignedTo;

    /**
     * 分配部门
     */
    private Long assignedDept;

}
