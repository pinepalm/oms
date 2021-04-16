/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 23:32:11
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:37:07
 * @Description: 标准命令
 */
package com.buaa.appmodel.cli;

import java.util.function.Function;

import com.buaa.appmodel.core.input.CommandBase;

/**
 * @description: 标准命令
 */
public class StandardCommand extends CommandBase {
    private final Function<String[], RunResult> runner;

    /**
     * @description: 指定参数构造
     * @param {String}                                  name
     * @param {Function<String[],MultiArgsCheckResult>} runner
     * @return {*}
     */
    public StandardCommand(String name, Function<String[], RunResult> runner) {
        super(name);

        this.runner = runner;
    }

    public RunResult run(String[] args) {
        if (runner == null)
            return RunResult.empty;

        return runner.apply(args);
    }
}
