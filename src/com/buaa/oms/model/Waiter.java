/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 20:16:40
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 21:39:26
 * @Description: 服务员类
 */
package com.buaa.oms.model;

import java.util.LinkedList;
import java.util.Queue;

import com.buaa.oms.dao.Order;
import com.buaa.oms.service.IPersonService;
import com.buaa.oms.service.WaiterService;

/**
 * 服务员类
 */
public class Waiter extends Person {
    private Queue<Order> orders;

    public Waiter(String name, String sex, String phoneNum, String pid) {
        super(name, sex, phoneNum, pid);

        orders = new LinkedList<>();
    }

    public Queue<Order> getOrders() {
        return orders;
    }

    @Override
    public IPersonService createService() {
        return new WaiterService(this);
    }
}
