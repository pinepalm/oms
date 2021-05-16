/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 14:58:01
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 19:35:27
 * @Description: 顾客点菜服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.IRunnerDefinition;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.model.Customer;
import com.buaa.util.IntegerUtil;

/**
 * 顾客点菜服务
 */
public final class CustomerOrderService extends OmsEmbeddedEnvService {
    // <editor-fold> 字符串常量
    private final static String SELECT_AT_LEAST_ONE_DISH = "Please select at least one dish to your order";
    // </editor-fold>

    private Customer customer;

    // <editor-fold> 点菜add
    private final Lazy<ICommand> addCommand = new Lazy<>(() -> new StandardCommand("add", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-i", "-n").contains(runtimeArgs[1])) {
            return RunResult.commandNotExist;
        }

        if (runtimeArgs.length != 4) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            int count = IntegerUtil.tryParse(runtimeArgs[3], 0);
            switch (runtimeArgs[1]) {
                case "-i":
                    customer.getOrderList().addOrderDishById(runtimeArgs[2], count);
                    break;
                case "-n":
                    customer.getOrderList().addOrderDishByName(runtimeArgs[2], count);
                    break;
                default:
                    break;
            }
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 结束选菜finish
    private final Lazy<ICommand> finishCommand = new Lazy<>(() -> new StandardCommand("finish", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            if (customer.getOrderList().isEmpty()) {
                throw new IllegalStateException(SELECT_AT_LEAST_ONE_DISH);
            }

            close();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(addCommand.getValue(), finishCommand.getValue()));
    // </editor-fold>

    public CustomerOrderService(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    @Override
    protected IRunnerDefinition createRunnerDefinitionInternal() {
        return new CustomerOrderRunnerDefinition();
    }

    @Override
    public void close() {
        super.close();

        customer = null;
    }

    private final class CustomerOrderRunnerDefinition implements IRunnerDefinition {
        private final Lazy<ICommandContainer[]> containers;

        public CustomerOrderRunnerDefinition() {
            containers = new Lazy<>(() -> {
                ICommandContainer[] containers = { CustomerOrderService.this };
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
            return RunResult.commandNotExist;
        }
    }
}
