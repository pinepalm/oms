/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 10:17:14
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 13:07:51
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
 * 命令行应用运行器
 */
public final class CliAppRunner {
    private final Map<String, ExactMatchCommand> exactMatchCmds;
    private final Map<String, StandardCommand> standardCmds;
    private final IRunnerDefinition runnerDefinition;

    private EventHandler<EventArgs> onViewClosed;

    CliAppRunner(CliAppView view) {
        exactMatchCmds = new HashMap<String, ExactMatchCommand>();
        standardCmds = new HashMap<String, StandardCommand>();

        onViewClosed = (sender, args) -> {
            CliAppView closedView = (CliAppView) sender;
            closedView.closed.removeEventHandler(onViewClosed);
            closedView.bindingApp.runnerHost.runners.remove(closedView);
        };

        runnerDefinition = view.runnerDefinition;
        load(runnerDefinition.getCommandContainers());

        view.closed.addEventHandler(onViewClosed);
    }

    /**
     * 加载命令容器
     * 
     * @param containers 命令容器数组
     * @return 自身
     */
    public CliAppRunner load(ICommandContainer... containers) {
        for (ICommandContainer container : containers) {
            if (container != null) {
                for (ICommand command : container.getCommands()) {
                    if (command instanceof ExactMatchCommand) {
                        exactMatchCmds.put(command.getName(), (ExactMatchCommand) command);
                    } else if (command instanceof StandardCommand) {
                        standardCmds.put(command.getName(), (StandardCommand) command);
                    }
                }
            }
        }

        return this;
    }

    /**
     * 卸载命令容器
     * 
     * @param containers 命令容器数组
     * @return 自身
     */
    public CliAppRunner unload(ICommandContainer... containers) {
        for (ICommandContainer container : containers) {
            if (container != null) {
                for (ICommand command : container.getCommands()) {
                    if (command instanceof ExactMatchCommand) {
                        exactMatchCmds.remove(command.getName());
                    } else if (command instanceof StandardCommand) {
                        standardCmds.remove(command.getName());
                    }
                }
            }
        }

        return this;
    }

    /**
     * 运行命令
     * 
     * @param cmd 命令
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
