/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 16:24:42
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-17 00:02:14
 * @Description: 厨师服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.Order;
import com.buaa.oms.dao.OrderCookList;
import com.buaa.oms.model.Cook;

/**
 * 厨师服务
 */
public final class CookService extends PersonService<Cook> {
    // <editor-fold> 做菜cook
    private final Lazy<ICommand> cookCommand = new Lazy<>(() -> new StandardCommand("cook", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            Order order = OrderCookList.instance.getUndoneOrders().poll();
            if (order != null) {
                OrderCookList.instance.getDoneOrders().put(order.getOrderID(), order);
                return order.getOrderID();
            }

            return null;
        }, (oid) -> oid != null ? new RunResult(String.format("Finish order:%s", oid)) : RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(cookCommand.getValue()));
    // </editor-fold>

    public CookService(Cook person) {
        super(person);
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}
