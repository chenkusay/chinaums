package com.chinaums.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * 
 * @author rachel.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogAnnotation {

	String value() default "";
}
