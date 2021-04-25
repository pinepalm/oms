/*
 * @Author: Zhe Chen
 * @Date: 2021-04-02 20:39:52
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:26:53
 * @Description: 事件处理类
 */
package com.buaa.appmodel.core.event;

/**
 * 事件处理类
 */
public interface EventHandler<T extends EventArgs> {
    /**
     * 处理
     * 
     * @param sender 发送者
     * @param args   参数
     */
    public void handle(Object sender, T args);
}
