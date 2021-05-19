/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 10:29:45
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 22:37:09
 * @Description: 整型工具类
 */
package com.buaa.util;

/**
 * 整型工具类
 */
public final class IntegerUtil {
    /**
     * 尝试解析
     * 
     * @param s            字符串
     * @param defaultValue 默认值
     * @return 解析数值
     */
    public static Integer tryParse(String s, Integer defaultValue) {
        return tryParse(s, 10, defaultValue);
    }

    /**
     * 尝试解析
     * 
     * @param s            字符串
     * @param radix        进制
     * @param defaultValue 默认值
     * @return 解析数值
     */
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