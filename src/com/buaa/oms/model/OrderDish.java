/*
 * @Author: Zhe Chen
 * @Date: 2021-05-16 15:59:51
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-17 00:01:44
 * @Description: 点菜菜品
 */
package com.buaa.oms.model;

/**
 * 点菜菜品
 */
public class OrderDish {
    private Dish dish;
    private int count;

    public OrderDish(Dish dish, int count) {
        setDish(dish);
        setCount(count);
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        Dish dish = getDish();
        return String.format("DID:%s,DISH:%s,PRICE:%.1f,NUM:%d", dish.getDID(), dish.getName(), dish.getPrice(),
                getCount());
    }
}
