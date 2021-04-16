/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 20:22:43
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:27:28
 * @Description: 菜单类
 */
package com.buaa.oms.dao;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.buaa.oms.model.Dish;
import com.buaa.util.collections.GroupingBase;

/**
 * @description: 菜单类
 */
public final class Menu extends GroupingBase<String, Dish> {
    // <editor-fold> 字符串常量
    private static final String INPUT_ILLEGAL = "%s input illegal";
    private static final String DID_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL, "Did");
    private static final String NEW_NAME_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL, "New name");
    private static final String NEW_DISHS_ATTRIBUTES_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL,
            "New dish's attributes");

    private static final String CHANGE_DISHS_ILLEGAL = "Change dish's %s illegal";
    private static final String CHANGE_DISHS_PRICE_ILLEGAL = String.format(CHANGE_DISHS_ILLEGAL, "price");
    private static final String CHANGE_DISHS_TOTAL_ILLEGAL = String.format(CHANGE_DISHS_ILLEGAL, "total");

    private static final String NAME_REPEATED = "Name repeated";
    private static final String NEW_NAME_REPEATED = "New name repeated";
    private static final String DISH_EXISTS = "Dish exists";
    private static final String DISH_DOES_NOT_EXIST = "Dish does not exist";
    private static final String EMPTY_MENU = "Empty Menu";

    private static final String HOT = "H";
    private static final String COOL = "C";
    private static final String OTHER = "O";
    private static final String DID = "DID";
    private static final String NAME = "Name";
    // </editor-fold>

    public static final Menu instance = new Menu();

    /**
     * @description: 默认构造
     * @param {*}
     * @return {*}
     */
    private Menu() {
        Comparator<Dish> dishComparator = (d1, d2) -> d1.getDID().compareTo(d2.getDID());

        items.put(HOT, new TreeSet<Dish>(dishComparator));// 热菜
        items.put(COOL, new TreeSet<Dish>(dishComparator));// 凉菜
        items.put(OTHER, new TreeSet<Dish>(dishComparator));// 其它

        indexers.put(DID, new HashMap<Object, Dish>());
        indexers.put(NAME, new HashMap<Object, Dish>());
    }

    /**
     * @description: 通过编号取出菜品
     * @param {String} did
     * @return {*}
     */
    public Dish getDishById(String did) throws IllegalArgumentException {
        if (!Dish.checkDID(did)) {
            throw new IllegalArgumentException(DID_INPUT_ILLEGAL);
        }

        return getIndexer(DID).get(did);
    }

    /**
     * @description: 通过名称取出菜品
     * @param {String} name
     * @return {*}
     */
    public Dish getDishByName(String name) {
        return getIndexer(NAME).get(name);
    }

    /**
     * @description: 通过关键词取出菜品集合
     * @param {String} keyword
     * @return {*}
     */
    public Vector<Dish> getDishByKeyWord(String keyword) {
        Vector<Dish> res = new Vector<>();

        String lowercaseKeyword = keyword.toLowerCase();
        for (Set<Dish> set : items.values()) {
            for (Dish dish : set) {
                if (dish.getName().toLowerCase().contains(lowercaseKeyword)) {
                    res.add(dish);
                }
            }
        }

        return res;
    }

    /**
     * @description: 添加菜品
     * @param {String} did
     * @param {String} name
     * @param {double} price
     * @param {int}    total
     * @return {*}
     */
    public void addDish(String did, String name, double price, int total) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById != null) {
            throw new IllegalArgumentException(DISH_EXISTS);
        }

        if (!Dish.checkName(name) || !Dish.checkPrice(price) || !Dish.checkTotal(total)) {
            throw new IllegalArgumentException(NEW_DISHS_ATTRIBUTES_INPUT_ILLEGAL);
        }

        Dish dishByName = getDishByName(name);
        if (dishByName != null) {
            throw new IllegalArgumentException(NAME_REPEATED);
        }

        String key = did.substring(0, 1);
        Set<Dish> set = items.get(key);
        if (set != null) {
            Dish newDish = new Dish(did, name, price, total);
            set.add(newDish);
            getIndexer(DID).put(did, newDish);
            getIndexer(NAME).put(name, newDish);
        }
    }

    /**
     * @description: 修改菜品(名称)
     * @param {String} did
     * @param {String} name
     * @return {*}
     */
    public void updateDish(String did, String name) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkName(name)) {
            throw new IllegalArgumentException(NEW_NAME_INPUT_ILLEGAL);
        }

        Dish dishByName = getDishByName(name);
        if (dishByName != null) {
            throw new IllegalArgumentException(NEW_NAME_REPEATED);
        }

        String oldName = dishById.getName();
        Map<Object, Dish> dishesNameIndexer = getIndexer(NAME);
        dishById.setName(name);
        dishesNameIndexer.put(name, dishesNameIndexer.remove(oldName));
    }

    /**
     * @description: 修改菜品(价格)
     * @param {String} did
     * @param {double} price
     * @return {*}
     */
    public void updateDish(String did, double price) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkPrice(price)) {
            throw new IllegalArgumentException(CHANGE_DISHS_PRICE_ILLEGAL);
        }

        dishById.setPrice(price);
    }

    /**
     * @description: 修改菜品(总量)
     * @param {String} did
     * @param {int}    total
     * @return {*}
     */
    public void updateDish(String did, int total) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkTotal(total)) {
            throw new IllegalArgumentException(CHANGE_DISHS_TOTAL_ILLEGAL);
        }

        dishById.setTotal(total);
    }

    /**
     * @description: 打印菜单
     * @param {*}
     * @return {*}
     */
    public void print() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(EMPTY_MENU);
        }

        int i = 1;
        for (Set<Dish> set : items.values()) {
            for (Dish dish : set) {
                System.out.println(String.format("%d. %s", i++, dish));
            }
        }
    }
}
