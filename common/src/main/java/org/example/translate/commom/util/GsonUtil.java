package org.example.translate.commom.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Gson帮助类
 *
 * @author hsjry
 * @version 1.0
 * @date 2017年10月5日
 * @since v1.0
 */
public final class GsonUtil {

    private static Logger log = Logger.getLogger(GsonUtil.class);

    /** 默认时间格式 */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Gson gson = new GsonBuilder().serializeNulls()
        .setDateFormat(DEFAULT_DATE_FORMAT)
        .create();

    /** 大写字母 */
    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");
    /** 小写字母 */
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");

    /**
     * 禁止实例化
     */
    private GsonUtil() {}

    /**
     * 获取默认的时间格式
     *
     * @return 时间格式
     */
    public static String getDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }

    /**
     * 实体转json
     *
     * @param obj 要转的实体
     * @return String json
     */
    public static String obj2Json(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * jsonString转对象
     *
     * @param <T> 泛型
     * @param jsonStr json
     * @param type 对象类型
     * @return 对象
     */
    public static <T> T json2Obj(String jsonStr, Class<T> type) {
        return gson.fromJson(jsonStr, type);
    }

    /**
     * 对象转jsonString针对于有Expose注解的.
     *
     * @param object 对象
     * @return json字符串
     */
    public static String objToStrForExpose(Object object) {
        if (object == null) {
            return null;
        }
        return gson.toJson(object);
    }

    /**
     * 对象转jsonString
     *
     * @param object 对象
     * @return 字符串
     */
    public static String objectToJsonString(Object object) {
        if (object == null) {
            return null;
        }
        String jsonString;
        jsonString = gson.toJson(object);
        return jsonString;
    }

    /**
     * jsonString转对象
     *
     * @param jsonStr 字符串
     * @return 对象
     */
    public static <T> T jsonStringToObject(String jsonStr, Class<T> type) {
        return gson.fromJson(jsonStr, type);
    }

    /**
     * json转list
     *
     * @param str 字符串
     * @param tClass 类类型
     * @return list
     */
    public static <T> List<T> jsonToList(String str, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(str)
            .getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(new Gson().fromJson(elem, tClass));
        }
        return list;
    }

    /**
     * json转list
     *
     * @param str json
     * @param typeOfT 类型
     * @return list
     */
    public static <T> List<T> jsonToList(String str, Type typeOfT) {
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(str)
            .getAsJsonArray();
        for (JsonElement jsonElement : array) {
            list.add(new Gson().fromJson(jsonElement, typeOfT));
        }
        return list;
    }

    public static <T> T jsonStringToObject(String jsonStr, Type typeOfT) {
        try {
            return gson.fromJson(jsonStr, typeOfT);
        } catch (Exception e) {
            log.error("json转换异常", e);
            return null;
        }
    }

    /**
     * 根据key 查询String 类型的value 只会查最上层的
     *
     * @param jsonStr json字符串
     * @param key key
     * @return value值
     */
    public static String getStringByKey(String jsonStr, String key) {
        JsonObject jo = new JsonParser().parse(jsonStr)
            .getAsJsonObject();
        JsonElement je = jo.get(key);
        if (je == null) {
            return null;
        }
        return je.getAsString();
    }

    /**
     * 根据key 查询String 类型的value 可查深层
     *
     * @param jsonStr json字符串
     * @param key key
     * @return value值
     */
    public static String getStringByKeyFore(String jsonStr, String key) {
        String reStr = null;
        JsonObject jo;
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            jo = new JsonParser().parse(jsonStr)
                .getAsJsonObject();

        } catch (Exception e) {
            log.error("json转换异常", e);
            return null;
        }
        JsonElement subJe = jo.get(key);
        if (subJe != null) {
            try {
                return subJe.getAsString();
            } catch (Exception e) {
                return gson.toJson(subJe);
            }

        }
        Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
        for (Map.Entry<String, JsonElement> entry : set) {
            JsonElement je = entry.getValue();
            String str = gson.toJson(je);
            reStr = GsonUtil.getStringByKeyFore(str, key);
            if (StringUtils.isNotBlank(reStr)) {
                break;
            }
        }
        return reStr;
    }

    /**
     * JSON key 对象格式转元数据格式
     *
     * @param jsonStr json字符串
     * @return 元数据格式
     */
    public static String toMetadataJson(String jsonStr) {
        JsonObject jo;
        try {
            jo = new JsonParser().parse(jsonStr)
                .getAsJsonObject();
        } catch (Exception e) {
            log.error("json转换异常", e);
            return null;
        }
        Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
        if (!set.isEmpty()) {
            for (Map.Entry<String, JsonElement> entry : set) {
                JsonElement je = entry.getValue();
                String str = gson.toJson(je);
                String oldStr = str;
                str = toMetadataJson(str);
                if (str != null) {
                    jsonStr = jsonStr.replace(oldStr, str);
                }
                jsonStr = toMetadataJson(entry, jsonStr);
            }
        }
        return jsonStr;
    }

    private static String toMetadataJson(Map.Entry<String, JsonElement> entry, String jsonStr) {
        String key = entry.getKey();
        String oldKey = key;
        // 处理key
        Matcher matcher = UPPER_CASE.matcher(key);
        while (matcher.find()) {
            String oldStr = matcher.group();
            String newStr = "_" + oldStr.toLowerCase();
            key = key.replace(oldStr, newStr);
        }

        return jsonStr.replace(oldKey, key);
    }

    public static String toObjectJson(String jsonStr) {
        JsonObject jo;
        try {
            jo = new JsonParser().parse(jsonStr)
                .getAsJsonObject();
        } catch (Exception e) {
            log.error("json转换异常", e);
            return null;
        }
        Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
        if (!set.isEmpty()) {
            for (Map.Entry<String, JsonElement> entry : set) {
                JsonElement je = entry.getValue();
                String str = gson.toJson(je);
                String oldStr = str;
                str = toObjectJson(str);
                if (str != null) {
                    jsonStr = jsonStr.replace(oldStr, str);
                }

                jsonStr = toObjectJson(entry, jsonStr);
            }
        }
        return jsonStr;
    }

    private static String toObjectJson(Map.Entry<String, JsonElement> entry, String jsonStr) {
        String key = entry.getKey();
        String oldKey = key;
        // 处理key
        Matcher matcher = LOWER_CASE.matcher(key);
        while (matcher.find()) {
            String oldStr = matcher.group();
            String newStr = oldStr.toUpperCase()
                .split("_")[1];
            key = key.replace(oldStr, newStr);
        }

        return jsonStr.replace(oldKey, key);
    }

    /**
     * 替换简单value String
     *
     * @param jsonStr json字符串
     * @param key key
     * @param value 新value
     * @return 新json
     */
    public static String replaceJsonValue(String jsonStr, String key, Object value) {
        JsonObject jo = null;
        try {
            jo = new JsonParser().parse(jsonStr)
                .getAsJsonObject();
        } catch (Exception e) {
            return gson.fromJson(jo, String.class);
        }
        JsonElement je1 = jo.get(key);
        if (je1 != null) {
            jo.remove(key);
            JsonElement je = gson.fromJson(gson.toJson(value), JsonElement.class);
            jo.add(key, je);

            return gson.toJson(jo);
        }
        Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
        for (Map.Entry<String, JsonElement> entry : set) {
            JsonElement curEntry = entry.getValue();
            String str = gson.toJson(curEntry);
            str = replaceJsonValue(str, key, value);
            jo.remove(entry.getKey());
            jo.add(entry.getKey(), gson.fromJson(str, JsonElement.class));
        }
        return gson.toJson(jo);
    }

    /**
     * 对象转jsonString 用于日志打印，排除不想打印的属性
     *
     * @param object 对象
     * @return 字符串
     */
    public static String objToStrForLog(Object object) {
        if (object == null) {
            return null;
        }
        return gson.toJson(object);
    }
}
