/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 20:07:27
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 10:47:09
 * @Description: 菜品类
 */
package com.buaa.oms.model;

import java.util.regex.Pattern;

/**
 * @description: 菜品类
 */
public class Dish {
    /**
     * @description: 检查编号
     * @param {String} did
     * @return {*}
     */
    public static boolean checkDID(String did) {
        return Pattern.matches("^(H|C|O)[0-9]{6}$", did);
    }

    /**
     * @description: 检查名称
     * @param {String} name
     * @return {*}
     */
    public static boolean checkName(String name) {
        return Pattern.matches("^[0-9a-zA-Z]+$", name);
    }

    /**
     * @description: 检查价格
     * @param {double} price
     * @return {*}
     */
    public static boolean checkPrice(double price) {
        return price >= 0d;
    }

    /**
     * @description: 检查总量
     * @param {int} total
     * @return {*}
     */
    public static boolean checkTotal(int total) {
        return total >= 0;
    }

    private String did;
    private String name;
    private double price;
    private int total;

    /**
     * @description: 默认构造
     * @param {*}
     * @return {*}
     */
    public Dish() {

    }

    /**
     * @description: 指定参数构造
     * @param {String} did
     * @param {String} name
     * @param {double} price
     * @param {int} total
     * @return {*}
     */
    public Dish(String did, String name, double price, int total) {
        setDID(did);
        setName(name);
        setPrice(price);
        setTotal(total);
    }

    public String getDID() {
        return did;
    }
    public void setDID(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return String.format("DID:%s,DISH:%s,PRICE:%.1f,TOTAL:%d", getDID(), getName(), getPrice(), getTotal());
    }
}
