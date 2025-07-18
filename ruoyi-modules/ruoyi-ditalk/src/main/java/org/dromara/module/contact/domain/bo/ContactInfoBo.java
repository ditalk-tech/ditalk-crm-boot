package org.dromara.module.contact.domain.bo;

import org.dromara.module.contact.domain.ContactInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 联系人信息业务对象 contact_info
 *
 * @author weidixian
 * @date 2025-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ContactInfo.class, reverseConvertGenerate = false)
public class ContactInfoBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 乐观锁
     */
    @NotNull(message = "乐观锁不能为空", groups = { EditGroup.class })
    private Long version;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long customerId;

    /**
     * 姓氏
     */
    private String lastName;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String state;


}
