package org.dromara.module.contact.service;

import org.dromara.module.contact.domain.vo.ContactInfoVo;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.IdPageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 联系人信息Service接口
 *
 * @author weidixian
 * @date 2025-07-18
 */
public interface IContactInfoService {

    /**
     * 查询联系人信息
     *
     * @param id 主键
     * @return 联系人信息
     */
    ContactInfoVo queryById(Long id);

    /**
     * 分页查询联系人信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 联系人信息分页列表
     */
    TableDataInfo<ContactInfoVo> queryPageList(ContactInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的联系人信息列表
     *
     * @param bo 查询条件
     * @return 联系人信息列表
     */
    List<ContactInfoVo> queryList(ContactInfoBo bo);

    /**
     * 新增联系人信息
     *
     * @param bo 联系人信息
     * @return 是否新增成功
     */
    Boolean insertByBo(ContactInfoBo bo);

    /**
     * 修改联系人信息
     *
     * @param bo 联系人信息
     * @return 是否修改成功
     */
    Boolean updateByBo(ContactInfoBo bo);

    /**
     * 删除联系人信息
     *
     * @param id 主键
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 校验并批量删除联系人信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过ID分页查询联系人信息列表
     *
     * @param bo 查询条件
     * @return 联系人信息列表
     */
    List<ContactInfoVo> queryList(ContactInfoBo bo, IdPageQuery pageQuery);
}
