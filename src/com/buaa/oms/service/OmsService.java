/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:31:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:41:02
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
import com.buaa.oms.OmsApp;
import com.buaa.util.Lazy;

/**
 * @description: Oms服务
 */
public final class OmsService implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String GOOD_BYE = "----- Good Bye! -----";
    // </editor-fold>

    // <editor-fold> 退出QUIT
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("QUIT", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            System.out.println(GOOD_BYE);
            OmsApp.instance.mainView.close();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 进入超级用户模式SUDO
    private final Lazy<ExactMatchCommand> sudoCommand = new Lazy<>(() -> new ExactMatchCommand("SUDO", () -> {

        return RunResult.empty;
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

        switch (runtimeArgs[1]) {
            case "-i":

                break;
            case "-n":

                break;
            default:
                break;
        }

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(quitCommand.getValue()));
    // </editor-fold>

    public static final OmsService instance = new OmsService();

    private OmsService() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}