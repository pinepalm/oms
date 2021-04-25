/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 02:50:11
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 12:02:23
 * @Description: 命令行应用
 */
package com.buaa.appmodel.cli;

import com.buaa.appmodel.core.AppBase;

/**
 * 命令行应用
 */
public abstract class CliApp extends AppBase<CliAppView> {
    public final CliAppRunnerHost runnerHost = new CliAppRunnerHost(this);

    @Override
    public CliAppView openNewView() {
        return new CliAppView(this);
    }

    /**
     * 打开新视图
     * 
     * @param definition 运行器定义
     * @return 新视图
     */
    public CliAppView openNewView(IRunnerDefinition definition) {
        return new CliAppView(this, definition);
    }
}
