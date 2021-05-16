/*
 * @Author: Zhe Chen
 * @Date: 2021-04-24 12:41:59
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 20:07:01
 * @Description: 顾客服务
 */
package com.buaa.oms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final String ORDER_CONFIRMED = "Order Confirmed";
    // </editor-fold>

    // <editor-fold> 增加余额rc
    private final Lazy<ICommand> rechargeCommand = new Lazy<>(() -> new StandardCommand("rc", (runtimeArgs) -> {
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
    private final Lazy<ICommand> getBalanceCommand = new Lazy<>(() -> new StandardCommand("gb", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            return person.getBalance();
        }, (balance) -> new RunResult(String.format("Balance: %.1f", balance)));
    }));
    // </editor-fold>
    // <editor-fold> VIP申请aplVIP
    private final Lazy<ICommand> applyVIPCommand = new Lazy<>(() -> new StandardCommand("aplVIP", (runtimeArgs) -> {
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
    // <editor-fold> 进入点餐环境order
    private final Lazy<ICommand> orderCommand = new Lazy<>(() -> new StandardCommand("order", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            CustomerOrderService customerOrderService = new CustomerOrderService(person);
            customerOrderService.open();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 查看已点菜品co
    private final Lazy<ICommand> checkOrderCommand = new Lazy<>(() -> new StandardCommand("co", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            person.getOrderList().printCurrent();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 提交菜单confirm
    private final Lazy<ICommand> confirmCommand = new Lazy<>(() -> new StandardCommand("confirm", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            person.getOrderList().confirmCurrent();
        }, () -> new RunResult(ORDER_CONFIRMED));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> {
        List<ICommand> cmds = new ArrayList<>();
        List<String> externalCmdNames = Arrays.asList("gd", "pm");

        cmds.add(rechargeCommand.getValue());
        cmds.add(getBalanceCommand.getValue());
        cmds.add(applyVIPCommand.getValue());
        cmds.add(orderCommand.getValue());
        cmds.add(checkOrderCommand.getValue());
        cmds.add(confirmCommand.getValue());
        for (ICommand cmd : MenuService.instance.getCommands()) {
            if (externalCmdNames.contains(cmd.getName())) {
                cmds.add(cmd);
            }
        }

        return cmds;
    });
    // </editor-fold>

    public CustomerService(Customer person) {
        super(person);
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}