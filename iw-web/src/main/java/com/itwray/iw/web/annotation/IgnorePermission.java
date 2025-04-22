package com.itwray.iw.web.annotation;

import org.apache.ibatis.annotations.Mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略权限
 *
 * @author wray
 * @since 2025/1/15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnorePermission {

    /**
     * 用户数据权限
     * <p>基于{@link Mapper}注解标注的类实现</p>
     * <p>true -> 忽略</p>
     */
    boolean userDataPermission() default true;

    /**
     * http请求权限
     * <p>true -> 忽略</p>
     */
    boolean requestPermission() default true;
}
