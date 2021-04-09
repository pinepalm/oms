/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 10:50:24
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-09 10:54:01
 * @Description: 双精度浮点数工具类
 */

/**
 * @description: 双精度浮点数工具类
 */
public final class DoubleUtil {
    public static double tryParse(String s, double defaultValue) {
        return tryParse(s, Double.valueOf(defaultValue));
    }

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
