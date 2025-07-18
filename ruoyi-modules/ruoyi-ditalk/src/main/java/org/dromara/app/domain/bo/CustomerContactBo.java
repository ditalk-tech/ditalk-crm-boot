package org.dromara.app.domain.bo;

import jakarta.validation.Valid;
import lombok.Data;
import org.dromara.module.contact.domain.bo.ContactInfoBo;
import org.dromara.module.customer.domain.bo.CustomerInfoBo;

/**
 * Customer 与 Contact 合并对象
 *
 * @author weidixian
 **/
@Data
public class CustomerContactBo {

    @Valid
    private CustomerInfoBo customerInfoBo;

    @Valid
    private ContactInfoBo contactInfoBo;

}
