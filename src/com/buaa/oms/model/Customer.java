/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 20:12:04
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 22:40:13
 * @Description: 顾客类
 */
package com.buaa.oms.model;

import com.buaa.oms.dao.OrderList;
import com.buaa.oms.service.CustomerService;
import com.buaa.oms.service.PersonService;

/**
 * 顾客类
 */
public class Customer extends Person {
    private boolean isVIP;
    private double balance;
    private boolean isDining;
    private OrderList orderList;

    public Customer(String name, String sex, String phoneNum, String pid) {
        super(name, sex, phoneNum, pid);

        orderList = new OrderList(this);
    }

    public boolean getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean getIsDining() {
        return isDining;
    }

    public void setIsDining(boolean isDining) {
        this.isDining = isDining;
    }

    public OrderList getOrderList() {
        return orderList;
    }

    @Override
    public PersonService<?> createService() {
        return new CustomerService(this);
    }
}
