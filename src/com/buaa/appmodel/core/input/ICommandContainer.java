/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:01:50
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:27:44
 * @Description: 命令容器接口
 */
package com.buaa.appmodel.core.input;

/**
 * 命令容器接口
 */
public interface ICommandContainer {
    /**
     * 获取命令枚举
     * 
     * @return 命令枚举
     */
    Iterable<ICommand> getCommands();
}