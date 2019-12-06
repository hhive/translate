package org.example.translate.commom.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created on 2016年12月30日
 *
 * @author jinyanan
 * @version 1.0
 * @date 2016年12月30日
 * @since v1.0
 */
public final class CollectionUtil {

    /**
     * 禁止实例化
     */
    private CollectionUtil() {

    }

    /**
     * 校验Collection是否为空
     *
     * @param collection 校验Collection
     * @return boolean true-为空，false-不为空
     * @author jinyanan
     */
    public static boolean isEmpty(final Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 校验List是否不为空
     *
     * @param collection 校验Collection
     * @return boolean true-不为空，false-为空
     */
    public static boolean isNotEmpty(final Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * 校验Map是否为空
     *
     * @param map map
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 校验Map是否不为空
     *
     * @param map map
     * @return true-为空，false-不为空
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
}
