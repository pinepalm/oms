/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 10:17:14
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:36:19
 * @Description: 命令行应用运行器
 */
package com.buaa.appmodel.cli;

import java.util.HashMap;
import java.util.Map;

import com.buaa.appmodel.core.event.EventArgs;
import com.buaa.appmodel.core.event.EventHandler;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;

/**
 * @description: 命令行应用运行器
 */
public final class CliAppRunner {
    private static final Map<CliApp, Map<CliAppView, CliAppRunner>> runners = new HashMap<>();

    /**
     * @description: 获取与当前视图关联的 CliAppRunner 对象
     * @param {*}
     * @return {*}
     */
    public static CliAppRunner getForCurrentView(CliApp app) {
        CliAppView view = app.getCurrentView();
        if (view == null)
            return null;

        Map<CliAppView, CliAppRunner> viewRunners = runners.get(app);
        if (viewRunners == null) {
            viewRunners = new HashMap<CliAppView, CliAppRunner>();
            runners.put(app, viewRunners);
        }

        CliAppRunner runner = viewRunners.get(view);
        if (runner == null) {
            runner = new CliAppRunner(view);
            viewRunners.put(view, runner);
        }

        return runner;
    }

    private final Map<String, ExactMatchCommand> exactMatchCmds;
    private final Map<String, StandardCommand> standardCmds;
    private final IRunnerDefinition runnerDefinition;

    private EventHandler<EventArgs> onViewClosed;

    private CliAppRunner(CliAppView view) {
        exactMatchCmds = new HashMap<String, ExactMatchCommand>();
        standardCmds = new HashMap<String, StandardCommand>();

        onViewClosed = (sender, args) -> {
            CliAppView closedView = (CliAppView) sender;
            closedView.closed.removeEventHandler(onViewClosed);
            
            Map<CliAppView, CliAppRunner> viewRunners = runners.get(closedView.bindingApp);
            if (viewRunners != null) {
                viewRunners.remove(closedView);
            }
        };

        runnerDefinition = view.runnerDefinition;
        load(runnerDefinition.getCommandContainers());

        view.closed.addEventHandler(onViewClosed);
    }

    /**
     * @description: 加载命令容器
     * @param {ICommandContainer...} containers
     * @return {*}
     */
    public CliAppRunner load(ICommandContainer... containers) {
        for (ICommandContainer container : containers) {
            for (ICommand command : container.getCommands()) {
                if (command instanceof ExactMatchCommand) {
                    exactMatchCmds.put(command.getName(), (ExactMatchCommand) command);
                } else if (command instanceof StandardCommand) {
                    standardCmds.put(command.getName(), (StandardCommand) command);
                }
            }
        }

        return this;
    }

    /**
     * @description: 卸载命令容器
     * @param {ICommandContainer...} containers
     * @return {*}
     */
    public CliAppRunner unload(ICommandContainer... containers) {
        for (ICommandContainer container : containers) {
            for (ICommand command : container.getCommands()) {
                if (command instanceof ExactMatchCommand) {
                    exactMatchCmds.remove(command.getName());
                } else if (command instanceof StandardCommand) {
                    standardCmds.remove(command.getName());
                }
            }
        }

        return this;
    }

    /**
     * @description: 运行命令
     * @param {String} cmd
     * @return {*}
     */
    public void run(String cmd) {
        ExactMatchCommand exactMatchCmd = exactMatchCmds.get(cmd);
        RunResult result;

        if (exactMatchCmd != null) {
            result = exactMatchCmd.run();
        } else {
            String[] runtimeArgs = cmd.split("\\s+");
            if (runtimeArgs.length > 0) {
                StandardCommand standardCmd = standardCmds.get(runtimeArgs[0]);
                result = standardCmd != null ? standardCmd.run(runtimeArgs)
                        : runnerDefinition.getUnknownCommandResult();
            } else {
                result = runnerDefinition.getEmptyCommandResult();
            }
        }

        if (result != null)
            result.print();
    }
}
