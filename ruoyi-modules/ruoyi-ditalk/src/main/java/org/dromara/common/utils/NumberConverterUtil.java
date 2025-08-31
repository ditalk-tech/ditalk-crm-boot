package org.dromara.common.utils;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * 数字进制转换工具类，支持2-62进制转换
 *
 * @author weidixian
 */
public class NumberConverterUtil {
    private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] DIGIT_CHARS = DIGITS.toCharArray();
    private static final Map<Character, Integer> CHAR_TO_VALUE = new HashMap<>();
    static {
        for (int i = 0; i < DIGIT_CHARS.length; i++) {
            CHAR_TO_VALUE.put(DIGIT_CHARS[i], i);
        }
    }

    public static String numToRadix(String number, int radix) {
        if (radix <= 1 || radix > DIGITS.length()) {
            radix = DIGITS.length();
        }
        BigInteger bigNumber = new BigInteger(number);
        BigInteger bigRadix = BigInteger.valueOf(radix);

        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();
        while (!bigNumber.equals(BigInteger.ZERO)) {
            stack.add(DIGIT_CHARS[bigNumber.remainder(bigRadix).intValue()]);
            bigNumber = bigNumber.divide(bigRadix);
        }
        while (!stack.isEmpty()) {
            result.append(stack.pollLast());
        }
        return result.length() == 0 ? "0" : result.toString();
    }

    public static String radixToNum(String number, int radix) {
        if (radix <= 1 || radix > DIGITS.length()) {
            radix = DIGITS.length();
        }
        if (radix == 10) {
            return number;
        }
        char[] ch = number.toCharArray();
        int len = ch.length;
        BigInteger bigRadix = BigInteger.valueOf(radix);
        BigInteger result = BigInteger.ZERO;
        BigInteger base = BigInteger.ONE;

        for (int i = len - 1; i >= 0; i--) {
            Integer index = CHAR_TO_VALUE.get(ch[i]);
            if (index == null || index >= radix) {
                throw new IllegalArgumentException("Invalid character for radix: " + ch[i]);
            }
            result = result.add(BigInteger.valueOf(index).multiply(base));
            base = base.multiply(bigRadix);
        }
        return result.toString();
    }

    public static String transRadix(String num, int fromRadix, int toRadix) {
        return numToRadix(radixToNum(num, fromRadix), toRadix);
    }
}
