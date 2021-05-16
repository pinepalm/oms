/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 13:51:41
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 21:58:24
 * @Description: 服务员服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.Order;
import com.buaa.oms.dao.OrderCookList;
import com.buaa.oms.dao.PersonList;
import com.buaa.oms.model.Customer;
import com.buaa.oms.model.Waiter;
import com.buaa.util.DoubleUtil;

/**
 * 服务员服务
 */
public final class WaiterService extends PersonService<Waiter> {
    // <editor-fold> 字符串常量
    private static final String RECHARGE_INPUT_ILLEGAL = "Recharge input illegal";

    private static final String MANAGE_ORDER_SUCCESS = "Manage order success";
    private static final String NO_SERVING_ORDER = "No serving order";
    private static final String ORDER_SERVE_ILLEGAL = "Order serve illegal";
    private static final String ORDER_NOT_COOKED = "Order not cooked";
    private static final String ORDER_ALREADY_CHECKOUT = "Order already checkout";
    private static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    // </editor-fold>

    // <editor-fold> 获取Order清单gl
    private final Lazy<ICommand> getOrderListCommand = new Lazy<>(() -> new StandardCommand("gl", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            printOrders();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 分配订单mo
    private final Lazy<ICommand> manageOrderCommand = new Lazy<>(() -> new StandardCommand("mo", (runtimeArgs) -> {
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            manageOrder();
        }, () -> new RunResult(MANAGE_ORDER_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 送餐并且收钱sr
    private final Lazy<ICommand> serveReceiveCommand = new Lazy<>(() -> new StandardCommand("sr", (runtimeArgs) -> {
        if (runtimeArgs.length != 2) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            serveReceive(runtimeArgs[1]);
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 充值rw
    private final Lazy<ICommand> rechargeWaiterCommand = new Lazy<>(() -> new StandardCommand("rw", (runtimeArgs) -> {
        if (runtimeArgs.length != 3) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            double money = DoubleUtil.tryParse(runtimeArgs[2], -1d);
            if (money < 100d || money >= 1000d) {
                throw new IllegalArgumentException(RECHARGE_INPUT_ILLEGAL);
            }

            Customer customer = (Customer) PersonList.instance.getAnyPersonById(runtimeArgs[1]);
            customer.setBalance(customer.getBalance() + money);
            if (customer.getBalance() >= 200d) {
                customer.setIsVIP(true);
            }
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(getOrderListCommand.getValue(),
            manageOrderCommand.getValue(), serveReceiveCommand.getValue(), rechargeWaiterCommand.getValue()));
    // </editor-fold>

    public WaiterService(Waiter person) {
        super(person);
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    public void printOrders() throws IllegalStateException {
        if (person.getOrders().isEmpty()) {
            throw new IllegalStateException(NO_SERVING_ORDER);
        }

        int i = 1;
        for (Order order : person.getOrders()) {
            System.out.println(String.format("%d. %s", i++, order));
        }
    }

    public void manageOrder() throws IllegalStateException {
        if (person.getOrders().isEmpty()) {
            throw new IllegalStateException(NO_SERVING_ORDER);
        }

        Order order = person.getOrders().poll();
        OrderCookList.instance.getUndoneOrders().offer(order);
    }

    public void serveReceive(String oid) throws IllegalStateException {
        Order order = OrderCookList.instance.getDoneOrders().get(oid);
        if (order == null) {
            for (Order undoneOrder : OrderCookList.instance.getUndoneOrders()) {
                if (oid.equals(undoneOrder.getOrderID())) {
                    if (person.getPID().equals(undoneOrder.getWaiterID())) {
                        throw new IllegalStateException(ORDER_NOT_COOKED);
                    } else {
                        throw new IllegalStateException(ORDER_SERVE_ILLEGAL);
                    }
                }
            }
            for (Order undoneOrder : person.getOrders()) {
                if (oid.equals(undoneOrder.getOrderID())) {
                    throw new IllegalStateException(ORDER_NOT_COOKED);
                }
            }
            throw new IllegalStateException(ORDER_SERVE_ILLEGAL);
        }

        if (!person.getPID().equals(order.getWaiterID())) {
            throw new IllegalStateException(ORDER_SERVE_ILLEGAL);
        }

        if (order.getIsDelivered()) {
            throw new IllegalStateException(ORDER_ALREADY_CHECKOUT);
        }
        Customer customer = (Customer) PersonList.instance.getAnyPersonById(order.getCustomerID());
        double discount = customer.getIsVIP() ? 0.8 : 1.0;
        double sum = order.sum(discount);
        double balance = customer.getBalance() - sum;
        if (balance < 0d) {
            throw new IllegalStateException(INSUFFICIENT_BALANCE);
        }
        customer.setBalance(balance);
        order.setIsDelivered(true);
        System.out.println(String.format("%s,BALANCE:%.1f", order.toSumString(discount), customer.getBalance()));
    }
}
