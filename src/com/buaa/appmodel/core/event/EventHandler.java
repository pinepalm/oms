/*
 * @Author: Zhe Chen
 * @Date: 2021-04-02 20:39:52
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 01:20:06
 * @Description: 事件处理类
 */
package com.buaa.appmodel.core.event;

/**
 * @description: 事件处理类
 */
public interface EventHandler<T extends EventArgs> {
    /**
     * @description: 处理
     * @param {Object} sender
     * @param {T} args
     * @return {*}
     */
    public void handle(Object sender, T args);
}
