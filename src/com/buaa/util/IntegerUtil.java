/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 10:29:45
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-09 10:54:38
 * @Description: 整型工具类
 */
package com.buaa.util;

/**
 * 整型工具类
 */
public final class IntegerUtil {
    public static int tryParse(String s, int defaultValue) {
        return tryParse(s, Integer.valueOf(defaultValue));
    }

    public static int tryParse(String s, int radix, int defaultValue) {
        return tryParse(s, radix, Integer.valueOf(defaultValue));
    }

    public static Integer tryParse(String s, Integer defaultValue) {
        return tryParse(s, 10, defaultValue);
    }

    public static Integer tryParse(String s, int radix, Integer defaultValue) {
        try {
            return Integer.parseInt(s, radix);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private IntegerUtil() {

    }
}