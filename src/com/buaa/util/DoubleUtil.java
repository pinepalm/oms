/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 10:50:24
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 22:38:37
 * @Description: 双精度浮点数工具类
 */
package com.buaa.util;

/**
 * 双精度浮点数工具类
 */
public final class DoubleUtil {
    /**
     * 尝试解析
     * 
     * @param s            字符串
     * @param defaultValue 默认值
     * @return 解析数值
     */
    public static Double tryParse(String s, Double defaultValue) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private DoubleUtil() {

    }
}
