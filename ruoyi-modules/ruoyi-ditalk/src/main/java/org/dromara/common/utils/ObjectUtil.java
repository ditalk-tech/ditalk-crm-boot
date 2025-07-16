package org.dromara.common.utils;

import java.lang.reflect.Field;
import java.util.Objects;

public class ObjectUtil {
    /**
     * 比较两个对象是否相等，忽略字段的访问权限
     *
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象相等返回 true，否则返回 false
     */
    public static boolean compareObjects(Object obj1, Object obj2) {
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null) return false;
        if (!obj1.getClass().equals(obj2.getClass())) return false;

        Class<?> clazz = obj1.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);
                if (!Objects.equals(value1, value2)) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        return true;
    }
}
