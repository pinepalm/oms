/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 02:50:11
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:36:02
 * @Description: 命令行应用
 */
package com.buaa.appmodel.cli;

import com.buaa.appmodel.core.AppBase;

/**
 * @description: 命令行应用
 */
public abstract class CliApp extends AppBase<CliAppView> {
    @Override
    public CliAppView openNewView() {
        return new CliAppView(this);
    }

    /**
     * @description: 打开新视图
     * @param {IRunnerDefinition} definition
     * @return {*}
     */
    public CliAppView openNewView(IRunnerDefinition definition) {
        return new CliAppView(this, definition);
    }
}
