/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:01:50
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-03-28 21:15:42
 * @Description: 命令容器接口
 */

/**
 * @description: 命令容器接口
 */
public interface ICommandContainer {
    /**
     * @description: 获取命令枚举
     * @param {*}
     * @return {*}
     */
    Iterable<ICommand> getCommands();
}