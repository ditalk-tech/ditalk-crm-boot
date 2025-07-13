package org.dromara.module.goods.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.EditGroup;

/**
 * 商品信息业务对象 content
 *
 * @author weidixian
 */
@Data
public class GoodsInfoContentBo {

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
     * 商品说明
     */
    private String content;

}
