/*
 * @Author: Zhe Chen
 * @Date: 2021-04-02 20:41:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:26:07
 * @Description: 事件类
 */
package com.buaa.appmodel.core.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件类
 */
public class Event<T extends EventArgs> {
    private List<EventHandler<T>> handlers = new ArrayList<>();

    public void addEventHandler(EventHandler<T> handler) {
        handlers.add(handler);
    }

    public void removeEventHandler(EventHandler<T> handler) {
        handlers.remove(handler);
    }

    public void invoke(Object sender, T args) {
        for (int i = handlers.size() - 1; i >= 0; i--) {
            handlers.get(i).handle(sender, args);
        }
    }
}
