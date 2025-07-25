package org.dromara.module.contact.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.module.contact.domain.ContactInfo;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 联系人信息视图对象 contact_info
 *
 * @author weidixian
 * @date 2025-07-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ContactInfo.class)
public class ContactInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 乐观锁
     */
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 客户ID
     */
    @ExcelProperty(value = "客户ID")
    private Long customerId;

    /**
     * 姓氏
     */
    @ExcelProperty(value = "姓氏")
    private String lastName;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String firstName;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private Long avatar;

    /**
     * 头像Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "avatar")
    private String avatarUrl;
    /**
     * 姓名拼音
     */
    @ExcelProperty(value = "姓名拼音")
    private String pinyin;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    private String gender;

    /**
     * 电子邮箱
     */
    @ExcelProperty(value = "电子邮箱")
    private String email;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    private String position;

    /**
     * 备注信息
     */
    @ExcelProperty(value = "备注信息")
    private String remark;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日")
    private Date birthday;

    /**
     * 户籍
     */
    @ExcelProperty(value = "户籍")
    private String placeOfOrigin;

    /**
     * 居住地
     */
    @ExcelProperty(value = "居住地")
    private String address;

    /**
     * 毕业学校
     */
    @ExcelProperty(value = "毕业学校")
    private String graduationSchool;

    /**
     * 学历
     */
    @ExcelProperty(value = "学历", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_educational_qualification")
    private String qualification;

    /**
     * 社会角色
     */
    @ExcelProperty(value = "社会角色")
    private String socialRole;

    /**
     * 最近接触时间
     */
    @ExcelProperty(value = "最近接触时间")
    private Date lastContactTime;

    /**
     * 接触频率
     */
    @ExcelProperty(value = "接触频率", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_contact_frequency")
    private String contactFrequency;

    /**
     * 微信
     */
    @ExcelProperty(value = "微信")
    private String wechat;

    /**
     * QQ
     */
    @ExcelProperty(value = "QQ")
    private String qq;

    /**
     * 钉钉
     */
    @ExcelProperty(value = "钉钉")
    private String dingTalk;

    /**
     * 飞书
     */
    @ExcelProperty(value = "飞书")
    private String lark;

    /**
     * WhatsApp
     */
    @ExcelProperty(value = "WhatsApp")
    private String whatsApp;

    /**
     * Facebook
     */
    @ExcelProperty(value = "Facebook")
    private String facebook;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ditalk_contact_state")
    private String state;

    /**
     * 分配到
     */
    @ExcelProperty(value = "分配到")
    private Long assignedTo;

    /**
     * 分配部门
     */
    @ExcelProperty(value = "分配部门")
    private Long assignedDept;

}
