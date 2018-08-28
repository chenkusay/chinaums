package com.chinaums.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * json 转换
 * @author rachel
 */
public class JsonUtils {
    private static final Logger logger = Logger.getLogger(JsonUtils.class);

    public static String toJSONString(Object object){
        return JSON.toJSONString(object);
    }

    /**
     * 获取单一属性值
     * @param key
     * @param json
     * @return
     */
    public static String getProperty(String key,String json){
        if (!StringUtils.isBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json);
            String value = jsonObject.getString(key);
            if(StringUtils.isBlank(value)){
                return "";
            }
            return value;
        }
        return "";
    }

    /**
     * 获取属性列表
     * @param json
     * @return
     */
    public static String getPropertyList(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        StringBuffer sb = new StringBuffer();
        for(String key:jsonObject.keySet()){
            sb.append("<p>").append(key).append(":").append(jsonObject.getString(key)).append("</p>");
        }
        return sb.toString();
    }

    /**
     * 获取属性列表
     * @param json
     * @return
     */
    public static Map<String, String> getPropertyMap(String json){
        JSONObject jsonObject = JSON.parseObject(json);

        Map<String, String> maps = new HashMap<String, String>();
        for(String key:jsonObject.keySet()){
            maps.put(key, jsonObject.getString(key));
        }
        return maps;
    }

    public static JSONArray getJsonArray(String json){
        if (StringUtils.isBlank(json)){
            return JSONArray.parseArray("[]");
        }
        return JSON.parseArray(json);
    }

}
