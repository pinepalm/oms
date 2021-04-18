/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:32:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:17:35
 * @Description: 人员列表类
 */
package com.buaa.oms.dao;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import com.buaa.foundation.collections.GroupingBase;
import com.buaa.oms.model.Person;

/**
 * @description: 人员列表类
 */
public final class PersonList extends GroupingBase<String, Person> {
    // <editor-fold> 字符串常量
    private static final String ILLEGAL = "%s illegal";
    private static final String PHONE_NUMBER_ILLEGAL = String.format(ILLEGAL, "Phone number");
    private static final String SEX_ILLEGAL = String.format(ILLEGAL, "Sex");
    // private static final String ARGUMENTS_ILLEGAL = String.format(ILLEGAL,
    // "Arguments");

    private static final String CUSTOMER = "Cu";
    private static final String WAITER = "Wa";
    private static final String COOK = "Co";
    private static final String PID = "PID";
    private static final String NAME = "Name";
    private static final String PHONE_NUM = "PhoneNum";
    // </editor-fold>

    public static final PersonList instance = new PersonList();

    private PersonList() {
        Comparator<Person> personComparator = (p1, p2) -> p1.getPID().compareTo(p2.getPID());

        items.put(CUSTOMER, new TreeSet<Person>(personComparator));// 顾客
        items.put(WAITER, new TreeSet<Person>(personComparator));// 服务员
        items.put(COOK, new TreeSet<Person>(personComparator));// 厨师

        indexers.put(PID, new HashMap<Object, Person>());
        indexers.put(NAME, new HashMap<Object, Person>());
        indexers.put(PHONE_NUM, new HashMap<Object, Person>());
    }

    /**
     * @description: 添加人
     * @param {String} name
     * @param {String} sex
     * @param {String} phoneNum
     * @return {*}
     */
    public Person addPerson(String name, String sex, String phoneNum) throws IllegalArgumentException {
        if (!Person.checkSex(sex)) {
            throw new IllegalArgumentException(SEX_ILLEGAL);
        }

        String last = phoneNum.length() > 0 ? phoneNum.substring(phoneNum.length() - 1) : "";
        if (!(Person.checkNum(phoneNum) && ((sex.equals("M") && last.equals("0")) || (sex.equals("F") && last.equals("1"))))) {
            throw new IllegalArgumentException(PHONE_NUMBER_ILLEGAL);
        }

        return new Person(name, sex, phoneNum);
    }

    public void addPerson(String name, String sex, String phoneNum, String pid) {

    }

    public void deletePerson(String pid) {

    }

    public void print() {

    }
}
