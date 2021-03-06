/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:54:13
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:23:37
 * @Description: 应用命令行视图
 */
package com.buaa.appmodel.cli;

import com.buaa.appmodel.core.AppViewBase;

/**
 * 应用命令行视图
 */
public final class CliAppView extends AppViewBase<CliAppView, CliApp> {
    public final IRunnerDefinition runnerDefinition;

    CliAppView(CliApp bindingApp) {
        this(bindingApp, null);
    }

    CliAppView(CliApp bindingApp, IRunnerDefinition definition) {
        super(bindingApp);
        runnerDefinition = definition;
    }
}
