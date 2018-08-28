package com.chinaums.utils;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class BeanUtils {


    /**
     * map集合转换成Bean
     * @param map
     * @param clas
     * @param <T>
     * @return
     */
    public static <T> T changeToBean(Map<String, Object> map, Class clas) {
        if (map == null){
            return null;
        }
        // 获取key集合  实例对象  方法集合
        Set<String> keySet = map.keySet();
        T object = (T) ClassUtils.createInstance(clas);
        Method[] methods = object.getClass().getDeclaredMethods();
        // 循环执行set方法
        for (Method method : methods){
            if (method.getName().startsWith("set")){// 判断是否是set方法
                String attrName = ClassUtils.lowerCaseFirst(method.getName().substring(3)); // 获取属性名
                if (keySet.contains(attrName)){ // 判断属性是否在结果集中
                    Class attrType = ClassUtils.getAttributeType(object, attrName);
                    Object value = map.get(attrName);
                    // 特殊类型处理
                    if ((attrType.equals(boolean.class) || attrType.equals(Boolean.class)) && (value.equals(1) || value.equals(0))){
                        value = value.equals(1) ? true : false;
                    }

                    ClassUtils.setAttributeValue(object,attrName,value);
                }
            }
        }

        return object;
    }

    /**
     * map集合转bean集合
     * @param maps
     * @param clas
     * @param <T>
     * @return
     */
    public static  <T>PageList<T> changeToBeanList(PageList<Map<String, Object>> maps, Class clas){

        if (maps == null || maps.size() <= 0){
            return null;
        }

        PageList<T> pageList = new PageList<T>(maps.getPaginator());

        for (Map<String, Object> map : maps){
            T object = changeToBean(map, clas);
            pageList.add(object);
        }

        return pageList;
    }
}
