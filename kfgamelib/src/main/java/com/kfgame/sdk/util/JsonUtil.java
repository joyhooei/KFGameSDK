package com.kfgame.sdk.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * FastJson序列化工具包
 * Created by Tobin
 */
public class JsonUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * 将网络请求下来的数据用fastjson处理空的情况，并将时间戳转化为标准时间格式
     * @param result
     * @return
     */
    public static String dealResponseResult(String result) {
        result = JSONObject.toJSONString(result,
                SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteSlashAsSpecial,
                SerializerFeature.WriteTabAsSpecial);
        return result;
    }

    /**
     * 将json转换成为java对象T
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T Json2T(String json, Class<T> classOfT) {
        if (null == json || "".equals(json.trim()))
            return null;
        try {
            return JSON.parseObject(json, classOfT);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("JsonUtil.Json2T", "msg:" + json + "\nclazz:" + classOfT.getName());
            return null;
        }
    }

    /**
     * 将json转换成为java对象列表List<T>
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> List<T> Json2List(String json, Class<T> classOfT) {
        if (null == json || "".equals(json.trim())) {
            return null;
        }
        try {
            return JSON.parseArray(json, classOfT);
        } catch (Exception e) {
            Log.e("JsonUtil.Json2T", "msg:" + json + "\r\nclazz:" + classOfT.getName());
            return null;
        }
    }

    /**
     * 把JSON数据转换成较为复杂的java对象列表List<Map<String, Object>>
     *
     * @param jsonData
     *            JSON数据
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> json2MapList(String jsonData) throws Exception {
        return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * 将非集合类java对象转换成为json object
     *
     * @param o
     * @return
     */
    public static JSONObject Object2JsonObject(Object o) {
        try {
            return (JSONObject) JSON.toJSON(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将集合类java对象装换成为json array
     *
     * @param collection
     * @return
     */
    public static JSONArray Collection2JsonArray(Collection collection) {
        try {
            return (JSONArray) JSON.toJSON(collection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json String 转 JSONObject
     *
     * @param json
     * @return
     */
    public static JSONObject json2JsonObject(String json) {
        try {
            return JSON.parseObject(json);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json转换成为jsonArray对象
     *
     * @param str
     * @return
     */
    public static JSONArray Json2JsonArray(String str) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        try {
            return JSON.parseArray(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象Object转换成json格式数据
     *
     * @param o
     * @return
     */
    public static String Object2Json(Object o) {
        try {
            return JSON.toJSONString(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象Object转换成更加方便观察的json格式数据，但会增加存储空间
     *
     * @param o
     * @return
     */
    public static String Object2JsonPrettyFormat(Object o) {
        try {
            return JSON.toJSONString(o, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte[] 转 JSONObject
     *
     * @param bytes
     * @return
     */
    public static JSONObject bytes2JsonObject(byte[] bytes) {
        try {
            return JSON.parseObject(new String(bytes, CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte[] 转 java对象
     *
     * @param bytes
     * @return
     */
    public static <T> T bytes2T(byte[] bytes, Class<T> classOfT) {
        try {
            return JSON.parseObject(new String(bytes, CHARSET), classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象转换成json格式数据，使用全序列化方案
     *
     * @param o
     * @return
     */
    public static String Object2JsonSerial(Object o) {
        try {
            return JSON.toJSONString(o, SerializerFeature.WriteClassName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将列表转换成为json，使用全序列化方案
     *
     * @param l
     * @return
     */
    public static String list2JsonSerial(List l) {
        if (null == l) {
            return null;
        }
        try {
            return JSON.toJSONString(l, SerializerFeature.WriteClassName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将全序列化json转换成为java对象
     *
     * @param json
     * @return
     */
    public static Object JsonSerial2Object(String json) {
        try {
            return JSON.parse(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将全序列化json转换成为list对象
     *
     * @param json
     * @return
     */
    public static List JsonSerial2List(String json) {
        try {
            return JSON.parseArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将全序列化json转换为jsonArray对象
     *
     * @param json
     * @return
     */
    public static JSONArray JsonSerial2JsonArray(String json) {
        try {
            return JSON.parseArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
