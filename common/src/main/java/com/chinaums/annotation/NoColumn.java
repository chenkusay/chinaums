package com.chinaums.annotation;

import java.lang.annotation.*;

/**
 * 数据库对象不映射注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoColumn {

}
