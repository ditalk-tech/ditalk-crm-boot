package org.dromara.handler;

import org.dromara.module.contact.domain.bo.ContactInfoBo;

/**
 * 联系人信息应用接口
 *
 * @author weidixian
 */
public interface IContactInfoHandler {

    /**
     * 添加联系人信息
     *
     * @param bo 联系人业务对象
     * @return 是否添加成功
     */
    Boolean add(ContactInfoBo bo);

    /**
     * 是否是默认联系人
     *
     * @param id 联系人ID
     * @return 是否是默认联系人
     */
    Boolean checkIsDefaultContact(Long id);
}
