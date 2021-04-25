/*
 * @Author: Zhe Chen
 * @Date: 2021-04-15 00:58:25
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 19:46:41
 * @Description: 超级用户服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.ExactMatchCommand;
import com.buaa.appmodel.cli.IRunnerDefinition;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.PersonList;

/**
 * 超级用户服务
 */
public final class SuperUserService extends OmsEmbeddedEnvService {
    // <editor-fold> 字符串常量
    private static final String SUCCESS = "%s success";
    private static final String ADD_NEW_SUCCESS = String.format(SUCCESS, "Add new %s");
    private static final String ADD_NEW_CUSTOMER_SUCCESS = String.format(ADD_NEW_SUCCESS, "customer");
    private static final String ADD_NEW_WAITER_SUCCESS = String.format(ADD_NEW_SUCCESS, "waiter");
    private static final String ADD_NEW_COOK_SUCCESS = String.format(ADD_NEW_SUCCESS, "cook");
    private static final String DELETE_NEW_SUCCESS = String.format(SUCCESS, "Delete %s");
    private static final String DELETE_CUSTOMER_SUCCESS = String.format(DELETE_NEW_SUCCESS, "customer");
    private static final String DELETE_WAITER_SUCCESS = String.format(DELETE_NEW_SUCCESS, "waiter");
    private static final String DELETE_COOK_SUCCESS = String.format(DELETE_NEW_SUCCESS, "cook");

    private static final String QUIT_SUDO_MODE = "Quit sudo mode";
    private static final String CALL_SUDO_METHOD_ILLEGAL = "Call sudo method illegal";
    // </editor-fold>

    // <editor-fold> 新增顾客sncu
    private final Lazy<StandardCommand> sncuCommand = new Lazy<>(() -> new StandardCommand("sncu", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.addPerson(runtimeArgs[1], runtimeArgs[2], runtimeArgs[3], runtimeArgs[4], "Cu");
        }, () -> new RunResult(ADD_NEW_CUSTOMER_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 新增服务员snwa
    private final Lazy<StandardCommand> snwaCommand = new Lazy<>(() -> new StandardCommand("snwa", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.addPerson(runtimeArgs[1], runtimeArgs[2], runtimeArgs[3], runtimeArgs[4], "Wa");
        }, () -> new RunResult(ADD_NEW_WAITER_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 新增厨师snco
    private final Lazy<StandardCommand> sncoCommand = new Lazy<>(() -> new StandardCommand("snco", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.addPerson(runtimeArgs[1], runtimeArgs[2], runtimeArgs[3], runtimeArgs[4], "Co");
        }, () -> new RunResult(ADD_NEW_COOK_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 删除顾客dcu
    private final Lazy<StandardCommand> dcuCommand = new Lazy<>(() -> new StandardCommand("dcu", (runtimeArgs) -> {
        if (runtimeArgs.length != 2) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.deletePerson(runtimeArgs[1], "Cu");
        }, () -> new RunResult(DELETE_CUSTOMER_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 删除服务员dwa
    private final Lazy<StandardCommand> dwaCommand = new Lazy<>(() -> new StandardCommand("dwa", (runtimeArgs) -> {
        if (runtimeArgs.length != 2) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.deletePerson(runtimeArgs[1], "Wa");
        }, () -> new RunResult(DELETE_WAITER_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 删除厨师dco
    private final Lazy<StandardCommand> dcoCommand = new Lazy<>(() -> new StandardCommand("dco", (runtimeArgs) -> {
        if (runtimeArgs.length != 2) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.deletePerson(runtimeArgs[1], "Co");
        }, () -> new RunResult(DELETE_COOK_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 打印人员列表pp
    private final Lazy<StandardCommand> ppCommand = new Lazy<>(() -> new StandardCommand("pp", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            PersonList.instance.print();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 退出超级用户状态SQ
    private final Lazy<ExactMatchCommand> sqCommand = new Lazy<>(() -> new ExactMatchCommand("SQ", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            close();
        }, () -> new RunResult(QUIT_SUDO_MODE));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(sncuCommand.getValue(),
            snwaCommand.getValue(), sncoCommand.getValue(), dcuCommand.getValue(), dwaCommand.getValue(),
            dcoCommand.getValue(), ppCommand.getValue(), sqCommand.getValue()));
    // </editor-fold>

    public SuperUserService() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    @Override
    protected IRunnerDefinition createRunnerDefinitionInternal() {
        return new SuperUserRunnerDefinition();
    }

    private final class SuperUserRunnerDefinition implements IRunnerDefinition {
        private final Lazy<ICommandContainer[]> containers;

        public SuperUserRunnerDefinition() {
            containers = new Lazy<>(() -> {
                ICommandContainer[] containers = { SuperUserService.this };
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
            return new RunResult(CALL_SUDO_METHOD_ILLEGAL);
        }
    }
}
