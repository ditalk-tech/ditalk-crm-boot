package org.dromara.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态 ditalk_goods_state
 *
 * @author weidixian
 */
@Getter
@AllArgsConstructor
public enum GoodsStateEnum {

    PRE_SALE("pre_sale", "预售"),
    OFF_SHELF ("off_shelf", "下架"),
    ON_SALE("on_sale", "在售"),
    ;

    private final String code;
    private final String desc;

}
