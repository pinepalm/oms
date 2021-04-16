/*
 * @Author: Zhe Chen
 * @Date: 2021-03-28 11:31:50
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 14:57:09
 * @Description: 菜单查看器
 */
package com.buaa.oms.dao;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.buaa.oms.model.Dish;

/**
 * @description: 菜单查看器
 */
public final class MenuViewer {
    // <editor-fold> 字符串常量
    private static final String THIS_IS_THE_PAGE = "This is the %s page";
    private static final String THIS_IS_THE_FIRST_PAGE = String.format(THIS_IS_THE_PAGE, "first");
    private static final String THIS_IS_THE_LAST_PAGE = String.format(THIS_IS_THE_PAGE, "last");

    private static final String PAGE = "Page: %d";
    private static final String DISH_DOES_NOT_EXIST = "Dish does not exist";
    private static final String MENU_IS_EMPTY_EXIT_PAGE_CHECK_MODE = "Menu is empty, exit page check mode";
    private static final String NEXT_LAST_FIRST_QUIT = "n-next page,l-last page,f-first page,q-quit";
    private static final String PAGE_SLICE_METHODS_PARAMS_INPUT_ILLEGAL = "Page slice method's params input illegal";
    // </editor-fold>

    private final Menu menu;
    private final String keyword;
    // 为什么这里要用Integer呢?这就要怪坑爹的输出逻辑!
    private final Integer pageIndex;
    private final Integer pageSize;

    private Vector<Dish> dishList;
    private int pagesCount;
    private int current;

    public MenuViewer(Menu menu, Integer pageIndex, Integer pageSize) {
        this(menu, null, pageIndex, pageSize);
    }

    public MenuViewer(Menu menu, String keyword, Integer pageIndex, Integer pageSize) {
        this.menu = menu;
        this.keyword = keyword;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * @description: 构建
     * @param {*}
     * @return {*}
     */
    public MenuViewer build() throws IllegalArgumentException {
        if (menu == null || menu.isEmpty()) {
            throw new IllegalArgumentException(MENU_IS_EMPTY_EXIT_PAGE_CHECK_MODE);
        }

        dishList = menu.getDishByKeyWord(keyword != null ? keyword : "");
        if (dishList.isEmpty()) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (pageIndex == null || pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException(PAGE_SLICE_METHODS_PARAMS_INPUT_ILLEGAL);
        }

        pagesCount = (int) Math.ceil((double) dishList.size() / pageSize);
        current = Math.min(Math.max(pageIndex, 1), pagesCount);

        return this;
    }

    public void printCurrent() {
        if (dishList == null)
            return;

        int i = 1;
        int index = current;
        List<Dish> list = dishList.stream().skip((index - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        System.out.println(String.format(PAGE, index));
        for (Dish dish : list) {
            System.out.println(String.format("%d. %s", i++, dish));
        }
        System.out.println(NEXT_LAST_FIRST_QUIT);
    }

    public void next() throws IllegalStateException {
        int index = current + 1;
        if (index > pagesCount) {
            throw new IllegalStateException(THIS_IS_THE_LAST_PAGE);
        }

        current = index;
        printCurrent();
    }

    public void last() throws IllegalStateException {
        int index = current - 1;
        if (index < 1) {
            throw new IllegalStateException(THIS_IS_THE_FIRST_PAGE);
        }

        current = index;
        printCurrent();
    }

    public void first() {
        current = 1;
        printCurrent();
    }
}
