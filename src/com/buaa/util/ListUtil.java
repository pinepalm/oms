/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 19:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-19 14:21:14
 * @Description: 列表工具类
 */
package com.buaa.util;

import java.util.List;

/**
 * 列表工具类
 */
public final class ListUtil {
    /**
     * 从列表最后获取元素
     * 
     * @param <T>   元素类型
     * @param list  列表
     * @param index 索引
     * @return 元素
     */
    public static <T> T getLast(List<T> list, int index) {
        return list.get(list.size() - index - 1);
    }

    private ListUtil() {

    }
}
