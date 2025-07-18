package org.dromara.app.domain.bo;

import jakarta.validation.Valid;
import lombok.Data;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.lead.domain.bo.LeadInfoBo;

/**
 * Lead 与 Contact 合并对象
 *
 * @author weidixian
 **/
@Data
public class LeadContactBo {

    @Valid
    private LeadInfoBo leadInfoBo;

    @Valid
    private ContactInfoBo contactInfoBo;

}
