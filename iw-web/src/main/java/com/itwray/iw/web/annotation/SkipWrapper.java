package com.itwray.iw.web.annotation;

import com.itwray.iw.web.core.webmvc.GeneralResponseWrapperAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跳过响应对象包装器
 *
 * @author wray
 * @see GeneralResponseWrapperAdvice
 * @since 2024/9/28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipWrapper {
}
