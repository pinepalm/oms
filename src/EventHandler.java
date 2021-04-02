/*
 * @Author: Zhe Chen
 * @Date: 2021-04-02 20:39:52
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 20:56:54
 * @Description: 事件处理类
 */

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
