/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 20:07:27
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 12:01:51
 * @Description: 菜品类
 */
package com.buaa.oms.model;

import java.util.regex.Pattern;

/**
 * 菜品类
 */
public class Dish {
    /**
     * 检查编号
     * 
     * @param did 菜品id
     * @return 是否合法
     */
    public static boolean checkDID(String did) {
        return Pattern.matches("^(H|C|O)[0-9]{6}$", did);
    }

    /**
     * 检查名称
     * 
     * @param name 名称
     * @return 是否合法
     */
    public static boolean checkName(String name) {
        return Pattern.matches("^[0-9a-zA-Z]+$", name);
    }

    /**
     * 检查价格
     * 
     * @param price 价格
     * @return 是否合法
     */
    public static boolean checkPrice(double price) {
        return price >= 0d;
    }

    /**
     * 检查总量
     * 
     * @param total 总量
     * @return 是否合法
     */
    public static boolean checkTotal(int total) {
        return total >= 0;
    }

    private String did;
    private String name;
    private double price;
    private int total;

    /**
     * 默认构造
     */
    public Dish() {

    }

    /**
     * 指定参数构造
     * 
     * @param did   菜品id
     * @param name  名称
     * @param price 价格
     * @param total 总量
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
