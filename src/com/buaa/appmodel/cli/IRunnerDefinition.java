/*
 * @Author: Zhe Chen
 * @Date: 2021-04-05 23:45:20
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:25:14
 * @Description: 运行器定义
 */
package com.buaa.appmodel.cli;

import com.buaa.appmodel.core.input.ICommandContainer;

/**
 * @description: 运行器定义
 */
public interface IRunnerDefinition {
    /**
     * 获取命令容器数组
     * 
     * @return 命令容器数组
     */
    ICommandContainer[] getCommandContainers();

    /**
     * 获取空命令结果
     * 
     * @return 空命令结果
     */
    RunResult getEmptyCommandResult();

    /**
     * 获取未知命令结果
     * 
     * @return 未知命令结果
     */
    RunResult getUnknownCommandResult();
}
