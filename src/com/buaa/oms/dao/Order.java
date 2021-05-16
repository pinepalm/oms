/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 13:42:08
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 21:48:05
 * @Description: 点菜类
 */
package com.buaa.oms.dao;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

import com.buaa.foundation.collections.GroupingBase;
import com.buaa.oms.model.Dish;
import com.buaa.oms.model.OrderDish;
import com.buaa.oms.model.Waiter;

/**
 * 点菜类
 */
public class Order extends GroupingBase<String, OrderDish> {
    // <editor-fold> 字符串常量
    private static final String NO_ORDER = "No order";

    private static final String HOT = "H";
    private static final String COOL = "C";
    private static final String OTHER = "O";

    private static final String DID = "DID";
    // </editor-fold>

    private String orderID;
    private String customerID;
    private String waiterID;
    private boolean isConfirmed;
    private boolean isDelivered;

    public Order(String orderID, String customerID) {
        setOrderID(orderID);
        setCustomerID(customerID);

        Comparator<OrderDish> dishComparator = (od1, od2) -> od1.getDish().getDID().compareTo(od2.getDish().getDID());

        items.put(HOT, new TreeSet<OrderDish>(dishComparator));// 热菜
        items.put(COOL, new TreeSet<OrderDish>(dishComparator));// 凉菜
        items.put(OTHER, new TreeSet<OrderDish>(dishComparator));// 其它

        indexers.put(DID, new HashMap<Object, OrderDish>());
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(String waiterID) {
        this.waiterID = waiterID;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public void addOrderDish(Dish dish, int count) {
        String key = dish.getDID().substring(0, 1);
        Set<OrderDish> set = items.get(key);
        if (set != null) {
            Map<Object, OrderDish> dishesDIDIndexer = getIndexer(DID);
            OrderDish orderDish = dishesDIDIndexer.get(dish.getDID());
            if (orderDish == null) {
                orderDish = new OrderDish(dish, count);
                set.add(orderDish);
                dishesDIDIndexer.put(dish.getDID(), orderDish);
            } else {
                orderDish.setCount(orderDish.getCount() + count);
            }

            dish.setTotal(dish.getTotal() - count);
        }
    }

    public void confirm() {
        Waiter waiter = PersonList.instance.getFirstServableWaiter();
        waiter.getOrders().offer(this);
        setWaiterID(waiter.getPID());
        setIsConfirmed(true);
    }

    public double sum(double discount) {
        double sum = 0d;
        for (Set<OrderDish> set : items.values()) {
            for (OrderDish orderDish : set) {
                double money = orderDish.getCount() * orderDish.getDish().getPrice();
                sum += money;
            }
        }

        return sum * discount;
    }

    public void print() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(NO_ORDER);
        }

        int i = 1;
        double sum = 0d;
        for (Set<OrderDish> set : items.values()) {
            for (OrderDish orderDish : set) {
                double money = orderDish.getCount() * orderDish.getDish().getPrice();
                sum += money;
                System.out.println(String.format("%d.%s", i++, orderDish));
            }
        }
        System.out.println("|");
        System.out.println(String.format("SUM:%.1f", sum));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        for (Set<OrderDish> set : items.values()) {
            for (OrderDish orderDish : set) {
                joiner.add(String.format("%d %s", orderDish.getCount(), orderDish.getDish().getName()));
            }
        }

        return String.format("OID:%s,DISH:[%s]", getOrderID(), joiner);
    }

    public String toSumString(double discount) {
        double sum = 0d;
        StringJoiner joiner = new StringJoiner(",");
        for (Set<OrderDish> set : items.values()) {
            for (OrderDish orderDish : set) {
                double money = orderDish.getCount() * orderDish.getDish().getPrice();
                sum += money;
                joiner.add(String.format("%s %.1f", orderDish.getDish().getName(), money));
            }
        }

        return String.format("OID:%s,DISH:[%s],TOTAL:%.1f", getOrderID(), joiner, sum * discount);
    }
}