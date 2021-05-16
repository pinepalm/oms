/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 19:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 19:15:06
 * @Description: file content
 */
package com.buaa.util;

import java.util.List;

public final class ListUtil {
    public static <T> T getLast(List<T> list, int index) {
        return list.get(list.size() - index - 1);
    }

    private ListUtil() {

    }
}
