/*
 * @Author: Zhe Chen
 * @Date: 2021-04-24 12:41:59
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 20:33:46
 * @Description: 顾客服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.foundation.Lazy;
import com.buaa.oms.model.Customer;
import com.buaa.util.DoubleUtil;

/**
 * 顾客服务
 */
public final class CustomerService extends PersonService<Customer> {
    // <editor-fold> 字符串常量
    private static final String RECHARGE_INPUT_ILLEGAL = "Recharge input illegal";
    private static final String APPLY_VIP_SUCCESS = "Apply VIP success";
    private static final String PLEASE_RECHARGE_MORE = "Please recharge more";
    // </editor-fold>

    // <editor-fold> 增加余额rc
    private final Lazy<StandardCommand> rechargeCommand = new Lazy<>(() -> new StandardCommand("rc", (runtimeArgs) -> {
        if (runtimeArgs.length != 2) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            double money = DoubleUtil.tryParse(runtimeArgs[1], -1d);
            if (money < 100d || money >= 1000d) {
                throw new IllegalArgumentException(RECHARGE_INPUT_ILLEGAL);
            }

            person.setBalance(person.getBalance() + money);
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 查看余额gb
    private final Lazy<StandardCommand> getBalanceCommand = new Lazy<>(() -> new StandardCommand("gb", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            return person.getBalance();
        }, (balance) -> new RunResult(String.format("Balance: %.1f", balance)));
    }));
    // </editor-fold>
    // <editor-fold> VIP申请aplVIP
    private final Lazy<StandardCommand> applyVIPCommand = new Lazy<>(() -> new StandardCommand("aplVIP", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            if (person.getBalance() < 200d) {
                person.setIsVIP(false);
                throw new IllegalArgumentException(PLEASE_RECHARGE_MORE);
            }

            person.setIsVIP(true);
        }, () -> new RunResult(APPLY_VIP_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(rechargeCommand.getValue(), getBalanceCommand.getValue(), applyVIPCommand.getValue()));
    // </editor-fold>

    public CustomerService(Customer person) {
        super(person);
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}