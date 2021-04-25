/*
 * @Author: Zhe Chen
 * @Date: 2021-04-18 14:44:44
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:23:28
 * @Description: 命令行应用运行器主机
 */
package com.buaa.appmodel.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令行应用运行器主机
 */
public final class CliAppRunnerHost {
    final Map<CliAppView, CliAppRunner> runners = new HashMap<>();

    /**
     * 获取与当前视图关联的 CliAppRunner 对象
     * 
     * @return 与当前视图关联的 CliAppRunner 对象
     */
    public CliAppRunner getRunnerForCurrentView() {
        CliAppView view = bindingApp.getCurrentView();
        if (view == null)
            return null;

        CliAppRunner runner = runners.get(view);
        if (runner == null) {
            runner = new CliAppRunner(view);
            runners.put(view, runner);
        }

        return runner;
    }

    public final CliApp bindingApp;

    CliAppRunnerHost(CliApp bindingApp) {
        this.bindingApp = bindingApp;
    }
}
