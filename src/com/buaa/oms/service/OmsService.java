/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:31:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 19:23:38
 * @Description: Oms服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.ExactMatchCommand;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.OmsApp;

/**
 * Oms服务
 */
public final class OmsService implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String GOOD_BYE = "----- Good Bye! -----";
    private static final String ENTER_SUDO_MODE = "Enter sudo mode";
    private static final String LOGIN_SUCCESS = "Login success";
    // </editor-fold>

    // <editor-fold> 退出QUIT
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("QUIT", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            System.out.println(GOOD_BYE);
            OmsApp.getInstance().close();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 进入超级用户模式SUDO
    private final Lazy<ExactMatchCommand> sudoCommand = new Lazy<>(() -> new ExactMatchCommand("SUDO", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            SuperUserService superUserService = new SuperUserService();
            superUserService.getBindingView(true).asCurrentView();
        }, () -> new RunResult(ENTER_SUDO_MODE));
    }));
    // </editor-fold>
    // <editor-fold> 登录login
    private final Lazy<StandardCommand> loginCommand = new Lazy<>(() -> new StandardCommand("login", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-i", "-n").contains(runtimeArgs[1])) {
            return RunResult.commandNotExist;
        }

        if (runtimeArgs.length != 4) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            String user = runtimeArgs[2];
            String password = runtimeArgs[3];
            LoginMode mode = runtimeArgs[1].equals("-i") ? LoginMode.ID : LoginMode.NAME;

            UserService.login(user, password, mode);
        }, () -> new RunResult(LOGIN_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(quitCommand.getValue(), sudoCommand.getValue(), loginCommand.getValue()));
    // </editor-fold>

    public static final OmsService instance = new OmsService();

    private OmsService() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}