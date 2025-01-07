package com.itwray.iw.common.utils;

import com.itwray.iw.common.IwException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author wray
 * @since 2024/10/25
 */
public abstract class ExceptionUtils {

    /**
     * 提取IwException对象
     *
     * @param ex RuntimeException
     * @return IwException
     */
    public static IwException extractIwException(RuntimeException ex) {
        if (ex instanceof IwException iwException) {
            return iwException;
        }

        String packageName = ex.getClass().getPackage().getName();

        // 判断是否属于Java内置异常
        if (packageName.startsWith("java.lang")) {
            return null;
        }

        // 如果异常没有原因（cause）
        if (ex.getCause() == null) {
            return null;
        }

        // 如果异常有原因（cause），则递归检查其原因
        if (ex.getCause() instanceof RuntimeException) {
            return extractIwException((RuntimeException) ex.getCause());
        }

        return null;
    }

    /**
     * 判断异常是否为内部异常对象
     *
     * @param ex RuntimeException
     * @return true -> 继承于IwException的内部异常对象
     */
    public static boolean isInternalException(RuntimeException ex) {
        // 获取异常类的包名
        String packageName = ex.getClass().getPackage().getName();

        // 判断是否属于自定义异常
        if (packageName.startsWith("com.itwray.iw")) {
            return IwException.class.isAssignableFrom(ex.getClass());
        }

        // 判断是否属于Java内置异常
        if (packageName.startsWith("java.lang")) {
            return false;
        }

        // 如果异常没有原因（cause）
        if (ex.getCause() == null) {
            return false;
        }

        // 如果异常有原因（cause），则递归检查其原因
        if (ex.getCause() instanceof RuntimeException) {
            return isInternalException((RuntimeException) ex.getCause());
        }

        return false;
    }

    /**
     * 获取异常地调用栈信息
     *
     * @param exception 异常
     * @return 异常地调用栈信息
     */
    public static String exceptionStackTraceText(Throwable exception) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            exception.printStackTrace(pw);
        } catch (Exception e) {
            //ignore
        }
        pw.flush();

        return sw.toString();
    }

    /**
     * 获取异常地调用栈信息
     *
     * @param exception 异常
     * @return 异常地调用栈信息
     */
    public static String exceptionStackTraceText(Throwable exception, int endIndex) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            exception.printStackTrace(pw);
        } catch (Exception e) {
            //ignore
        }
        pw.flush();

        String toString = sw.toString();
        if (toString.length() > endIndex) {
            return toString.substring(0, endIndex);
        }
        return toString;
    }
}
