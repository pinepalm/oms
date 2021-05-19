/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 19:01:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-17 00:01:08
 * @Description: 点菜列表
 */
package com.buaa.oms.dao;

import java.util.ArrayList;
import java.util.List;

import com.buaa.oms.model.Customer;
import com.buaa.oms.model.Dish;
import com.buaa.util.ListUtil;

/**
 * 点菜列表
 */
public class OrderList {
    // <editor-fold> 字符串常量
    private static final String NO_ORDER = "No order";
    private final static String DISH_SELECTED_NOT_EXIST = "Dish selected not exist";
    private final static String DISH_SELECTED_IS_SOLD_OUT = "Dish selected is sold out";
    private final static String DISH_IS_OUT_OF_STOCK = "Dish is out of stock";
    private static final String NO_ORDER_CAN_BE_CONFIRMED = "No order can be confirmed";
    // </editor-fold>

    private final Customer customer;

    private List<Order> orders;

    public OrderList(Customer customer) {
        this.customer = customer;

        orders = new ArrayList<>();
    }

    private void addOrderDish(Dish dish, int count) throws IllegalArgumentException {
        if (dish == null) {
            throw new IllegalArgumentException(DISH_SELECTED_NOT_EXIST);
        }

        if (dish.getTotal() == 0) {
            throw new IllegalArgumentException(DISH_SELECTED_IS_SOLD_OUT);
        }

        if (dish.getTotal() - count < 0) {
            throw new IllegalArgumentException(DISH_IS_OUT_OF_STOCK);
        }

        if (isEmpty()) {
            String cid = customer.getPID();
            String oid = String.format("%s_%d", cid, orders.size() + 1);
            orders.add(new Order(oid, cid));
        }

        ListUtil.getLast(orders, 0).addOrderDish(dish, count);
    }

    public void addOrderDishById(String did, int count) throws IllegalArgumentException {
        Dish dishById = Menu.instance.getDishById(did);
        addOrderDish(dishById, count);
    }

    public void addOrderDishByName(String name, int count) throws IllegalArgumentException {
        Dish dishByName = Menu.instance.getDishByName(name);
        addOrderDish(dishByName, count);
    }

    public boolean isEmpty() {
        return orders.isEmpty() || ListUtil.getLast(orders, 0).getIsConfirmed();
    }

    public void confirmCurrent() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(NO_ORDER_CAN_BE_CONFIRMED);
        }

        ListUtil.getLast(orders, 0).confirm();
    }

    public void printCurrent() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(NO_ORDER);
        }

        ListUtil.getLast(orders, 0).print();
    }
}
