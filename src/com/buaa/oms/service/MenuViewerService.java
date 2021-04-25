/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 14:25:59
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 16:29:47
 * @Description: 菜单查看器服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.ExactMatchCommand;
import com.buaa.appmodel.cli.IRunnerDefinition;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.MenuViewer;

/**
 * 菜单查看器服务
 */
public final class MenuViewerService extends OmsEmbeddedEnvService {
    // <editor-fold> 字符串常量
    private static final String EXIT_PAGE_CHECK_MODE = "Exit page check mode";
    private static final String CALL_INNER_METHOD_ILLEGAL = "Call inner method illegal";
    // </editor-fold>

    private MenuViewer viewer;

    // <editor-fold> 下一页n
    private final Lazy<ExactMatchCommand> nextCommand = new Lazy<>(() -> new ExactMatchCommand("n", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            viewer.next();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 上一页l
    private final Lazy<ExactMatchCommand> lastCommand = new Lazy<>(() -> new ExactMatchCommand("l", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            viewer.last();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 第一页f
    private final Lazy<ExactMatchCommand> firstCommand = new Lazy<>(() -> new ExactMatchCommand("f", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            viewer.first();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 退出q
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("q", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            close();
        }, () -> new RunResult(EXIT_PAGE_CHECK_MODE));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(nextCommand.getValue(),
            lastCommand.getValue(), firstCommand.getValue(), quitCommand.getValue()));
    // </editor-fold>

    public MenuViewerService(MenuViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    @Override
    protected IRunnerDefinition createRunnerDefinitionInternal() {
        return new MenuViewerRunnerDefinition();
    }

    @Override
    public void close() {
        super.close();

        viewer = null;
    }

    private final class MenuViewerRunnerDefinition implements IRunnerDefinition {
        private final Lazy<ICommandContainer[]> containers;
    
        public MenuViewerRunnerDefinition() {
            containers = new Lazy<>(() -> {
                ICommandContainer[] containers = { MenuViewerService.this };
                return containers;
            });
        }
    
        @Override
        public ICommandContainer[] getCommandContainers() {
            return containers.getValue();
        }
    
        @Override
        public RunResult getEmptyCommandResult() {
            return RunResult.inputIllegal;
        }
    
        @Override
        public RunResult getUnknownCommandResult() {
            return new RunResult(CALL_INNER_METHOD_ILLEGAL);
        }
    }
}
