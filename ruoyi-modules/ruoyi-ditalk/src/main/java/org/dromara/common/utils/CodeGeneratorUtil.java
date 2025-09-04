package org.dromara.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;

import java.util.Date;

/**
 * 编号生成器
 *
 * @author weidixian
 */
public class CodeGeneratorUtil
{
    private static final String COUNTER_KEY = "CodeGenerator:FourDigit";
    private static final long MIN = 1000;
    private static final long MAX = 9000;

    public static Long next4digit() {
        if (!RedisUtils.isExistsObject(COUNTER_KEY)) {
            RedisUtils.setAtomicValue(COUNTER_KEY, MIN);
        }
        long current = RedisUtils.incrAtomicValue(COUNTER_KEY);
        if (current >= MAX) {
            RedisUtils.setAtomicValue(COUNTER_KEY, MIN);
        }
        return current;
    }

    /**
     * 生成业务编号<br>
     * 如果 separator = '-'，则生成编号格式为：prefix-date-serial-NumberConverterUtil.numToRadix(id)
     *
     * @param prefix      前缀，为空则不添加
     * @param datePattern 日期格式，为空则不添加
     * @param separator   分隔符，不应为空
     * @param serial      自定义流水号，为空则不添加
     * @param snowflakeId id，为空则使用雪花ID
     * @return 业务编号
     */
    public static String businessCode(String prefix, String datePattern, String separator, String serial, Long snowflakeId) {
        StringBuilder codeBuilder = new StringBuilder();
        // 添加前缀
        if (StringUtils.isNotBlank(prefix))
            codeBuilder.append(prefix).append(separator);
        // 加入日期
        if (StringUtils.isNotBlank(datePattern))
            codeBuilder.append(DateUtil.format(new Date(), datePattern)).append(separator);
        // 加入自定义的流水号
        if (StringUtils.isNotBlank(serial))
            codeBuilder.append(serial).append(separator);
        // 加入雪花ID防重复（必存在）
        String uniqueCode = CodeGeneratorUtil.businessCode(snowflakeId);
        codeBuilder.append(uniqueCode);
        return codeBuilder.toString();
    }

    /**
     * 生成业务编号，值为 NumberConverterUtil.numToRadix(id)
     *
     * @param snowflakeId id，为空则使用雪花ID
     * @return 业务编号
     */
    public static String businessCode(Long snowflakeId) {
        // 加入雪花ID防重复
        Long id;
        if (snowflakeId != null) {
            id = snowflakeId;
        } else {
            id = IdUtil.getSnowflakeNextId();
        }
        return NumberConverterUtil.numToRadix(id.toString(), 36);
    }
}
