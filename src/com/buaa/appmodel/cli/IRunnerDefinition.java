/*
 * @Author: Zhe Chen
 * @Date: 2021-04-05 23:45:20
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 01:21:29
 * @Description: 运行器定义
 */
package com.buaa.appmodel.cli;

import com.buaa.appmodel.core.input.ICommandContainer;

/**
 * @description: 运行器定义
 */
public interface IRunnerDefinition {
    /**
     * @description: 获取命令容器数组
     * @param {*}
     * @return {*}
     */
    ICommandContainer[] getCommandContainers();

    /**
     * @description: 获取空命令结果
     * @param {*}
     * @return {*}
     */
    RunResult getEmptyCommandResult();

    /**
     * @description: 获取未知命令结果
     * @param {*}
     * @return {*}
     */
    RunResult getUnknownCommandResult();
}
