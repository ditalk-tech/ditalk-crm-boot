package org.dromara.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Long类型计算工具类 arithmetic
 *
 * @author weidixian
 */
public class LongUtil {

//    /**
//     * 加法
//     */
//    public static long add(long a, long b) {
//        return a + b;
//    }
//
//    /**
//     * 减法 subtract
//     */
//    public static long sub(long a, long b) {
//        return a - b;
//    }
//
//    /**
//     * 乘法 multiply
//     */
//    public static long mul(long a, long b) {
//        return a * b;
//    }

    /**
     * 除法（银行家舍入法）
     * 结果将保留到最接近的整数，使用 RoundingMode.HALF_EVEN
     */
    public static long divide(long a, long b) {
        if (b == 0) {
            throw new ArithmeticException("除数不能为0");
        }
        BigDecimal dividend = BigDecimal.valueOf(a);
        BigDecimal divisor = BigDecimal.valueOf(b);
        BigDecimal result = dividend.divide(divisor, 0, RoundingMode.HALF_EVEN); // 银行家舍入到0位小数
        return result.longValue();
    }

}
