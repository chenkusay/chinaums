package com.chinaums.common;

import org.springframework.stereotype.Component;

@Component
public class ParamConstants {

    /**
     * 数据库表名前缀
     */
    public static final String DATABASE_TABLE_PREFIX = "";

    /**
     * 数据库表名格式
     * 1. 和类名一致
     * 2. 下划线格式
     */
    public static final Integer DATABASE_TABLE_FORMAT = 2;

    /**
     * 数据库属性名格式
     * 1. 和类属性一致驼峰格式
     * 2. 下划线格式
     */
    public static final Integer DATABASE_ATTRIBUTE_FORMAT = 2;

    /**
     * 数据库实体包路径
     */
//    public static final String ENTITY_PACKAGE = "com.chinaums.entity";

    /**
     * 主键名称
     */
    public static final String PRIMARY_KEY = "id";

    /**
     * 业务层实现类后缀
     */
    public static final String SERVICE_PREFIX = "Service";

    /**
     * service层包名(反射生成entity需要用到)
     */
    public static final String SERVICE_PACKAGE_NAME = "service";
    /**
     * entity层包名(反射生成entity需要用到)
     */
    public static final String ENTITY_PACKAGE_NAME = "entity";
}
