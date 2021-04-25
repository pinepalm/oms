/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 23:38:48
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:24:22
 * @Description: 完全匹配命令
 */
package com.buaa.appmodel.cli;

import java.util.function.Supplier;

import com.buaa.appmodel.core.input.CommandBase;

/**
 * 完全匹配命令
 */
public class ExactMatchCommand extends CommandBase {
    private final Supplier<RunResult> runner;

    /**
     * 指定参数构造
     * 
     * @param name   名称
     * @param runner 运行器
     */
    public ExactMatchCommand(String name, Supplier<RunResult> runner) {
        super(name);

        this.runner = runner;
    }

    /**
     * @description: 运行
     * @param {*}
     * @return {*}
     */
    public RunResult run() {
        if (runner == null)
            return RunResult.empty;

        return runner.get();
    }
}