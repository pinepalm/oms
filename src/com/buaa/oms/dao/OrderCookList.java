/*
 * @Author: Zhe Chen
 * @Date: 2021-04-25 19:08:36
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 20:45:28
 * @Description: 点菜制作列表
 */
package com.buaa.oms.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 点菜制作列表
 */
public final class OrderCookList {
    public static final OrderCookList instance = new OrderCookList();

    private Queue<Order> undoneOrders;
    private Map<String, Order> doneOrders;

    private OrderCookList() {
        undoneOrders = new LinkedList<>();
        doneOrders = new HashMap<>();
    }

    public Queue<Order> getUndoneOrders() {
        return undoneOrders;
    }

    public Map<String, Order> getDoneOrders() {
        return doneOrders;
    }
}
