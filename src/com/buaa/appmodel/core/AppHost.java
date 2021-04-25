/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 10:10:24
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:34:23
 * @Description: 应用主机
 */
package com.buaa.appmodel.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 应用主机
 */
@SuppressWarnings("rawtypes")
public final class AppHost {
    static final List<AppBase> apps = new ArrayList<>();
    static final List<AppBase> readonlyApps = Collections.unmodifiableList(apps);

    public static List<AppBase> getApps() {
        return readonlyApps;
    }

    private AppHost() {

    }
}
