/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:32:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 20:01:13
 * @Description: 人员列表类
 */
package com.buaa.oms.dao;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import com.buaa.foundation.collections.GroupingBase;
import com.buaa.oms.model.Cook;
import com.buaa.oms.model.Customer;
import com.buaa.oms.model.Person;
import com.buaa.oms.model.Waiter;

/**
 * 人员列表类
 */
@SuppressWarnings("rawtypes")
public final class PersonList extends GroupingBase<String, Person> {
    // <editor-fold> 字符串常量
    private static final String ILLEGAL = "%s illegal";
    private static final String PHONE_NUMBER_ILLEGAL = String.format(ILLEGAL, "Phone number");
    private static final String SEX_ILLEGAL = String.format(ILLEGAL, "Sex");
    // private static final String ARGUMENTS_ILLEGAL = String.format(ILLEGAL,
    // "Arguments");
    private static final String PID_ILLEGAL = String.format(ILLEGAL, "PID");
    private static final String PID_ILLEGAL_0 = String.format(ILLEGAL, "%s PID");
    private static final String D_PID_ILLEGAL = String.format(PID_ILLEGAL_0, "D-%s");
    private static final String PNAME_ILLEGAL = String.format(ILLEGAL, "Pname");

    private static final String EXISTS = "%s exists";
    private static final String PID_EXISTS = String.format(EXISTS, "%s PID");
    private static final String PHONE_NUMBER_EXISTS = String.format(EXISTS, "Phone number");

    private static final String DOESNT_EXIST = "%s doesn't exist";
    private static final String PID_DOESNT_EXIST = String.format(DOESNT_EXIST, "%s PID");
    private static final String D_PID_DOESNT_EXIST = String.format(PID_DOESNT_EXIST, "D-%s");

    private static final String PHONE_NUMBER_DOESNT_MATCH_SEX = "Phone number doesn't match sex";
    private static final String EMPTY_PERSON_LIST = "Empty person list";

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

    private Class getPersonClass(String prefix) {
        switch (prefix) {
            case CUSTOMER:
                return Customer.class;
            case WAITER:
                return Waiter.class;
            case COOK:
                return Cook.class;
            default:
                return null;
        }
    }

    private String getFullPersonTypeName(String prefix) {
        Class clazz = getPersonClass(prefix);
        if (clazz == null)
            return null;

        return clazz.getSimpleName();
    }

    /**
     * 通过编号获取任何人员
     * 
     * @param pid 人员id
     * @return 人员
     * @throws IllegalArgumentException
     */
    public Person getAnyPersonById(String pid) throws IllegalArgumentException {
        if (!Person.checkPID(pid, "Cu|Wa|Co")) {
            throw new IllegalArgumentException(PID_ILLEGAL);
        }

        return getIndexer(PID).get(pid);
    }

    /**
     * 通过姓名获取任何人员
     * 
     * @param name 姓名
     * @return 人员
     * @throws IllegalArgumentException
     */
    public Person getAnyPersonByName(String name) throws IllegalArgumentException {
        if (!Person.checkName(name)) {
            throw new IllegalArgumentException(PNAME_ILLEGAL);
        }

        return getIndexer(NAME).get(name);
    }

    /**
     * 添加人
     * 
     * @param name     姓名
     * @param sex      性别
     * @param phoneNum 手机号
     * @return 添加的人
     * @throws IllegalArgumentException
     */
    public Person addPerson(String name, String sex, String phoneNum) throws IllegalArgumentException {
        if (!Person.checkSex(sex)) {
            throw new IllegalArgumentException(SEX_ILLEGAL);
        }

        String last = phoneNum.length() > 0 ? phoneNum.substring(phoneNum.length() - 1) : "";
        if (!(Person.checkNum(phoneNum)
                && ((sex.equals("M") && last.equals("0")) || (sex.equals("F") && last.equals("1"))))) {
            throw new IllegalArgumentException(PHONE_NUMBER_ILLEGAL);
        }

        return new Person(name, sex, phoneNum);
    }

    /**
     * 添加人
     * 
     * @param name     姓名
     * @param sex      性别
     * @param phoneNum 手机号
     * @param pid      人员id
     * @param prefix   类型前缀
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public void addPerson(String name, String sex, String phoneNum, String pid, String prefix)
            throws IllegalArgumentException {
        if (!Person.checkSex(sex)) {
            throw new IllegalArgumentException(SEX_ILLEGAL);
        }

        if (!Person.checkNum(phoneNum)) {
            throw new IllegalArgumentException(PHONE_NUMBER_ILLEGAL);
        }

        String last = phoneNum.substring(phoneNum.length() - 1);
        if (!((sex.equals("M") && last.equals("0")) || (sex.equals("F") && last.equals("1")))) {
            throw new IllegalArgumentException(PHONE_NUMBER_DOESNT_MATCH_SEX);
        }

        Person personByPhoneNum = getIndexer(PHONE_NUM).get(phoneNum);
        if (personByPhoneNum != null) {
            throw new IllegalArgumentException(PHONE_NUMBER_EXISTS);
        }

        if (!Person.checkPID(pid, prefix)) {
            throw new IllegalArgumentException(String.format(PID_ILLEGAL_0, getFullPersonTypeName(prefix)));
        }

        Person personById = getIndexer(PID).get(pid);
        if (personById != null) {
            throw new IllegalArgumentException(String.format(PID_EXISTS, getFullPersonTypeName(prefix)));
        }

        Set<Person> set = items.get(prefix);
        if (set != null) {
            try {
                Class personClass = getPersonClass(prefix);
                Constructor personConstructor = personClass.getConstructor(String.class, String.class, String.class,
                        String.class);
                Person newPerson = (Person) personConstructor.newInstance(name, sex, phoneNum, pid);
                set.add(newPerson);
                getIndexer(PID).put(pid, newPerson);
                getIndexer(NAME).put(name, newPerson);
                getIndexer(PHONE_NUM).put(phoneNum, newPerson);
            } catch (Exception ex) {

            }
        }
    }

    /**
     * 删除人
     * 
     * @param pid    人员id
     * @param prefix 类型前缀
     * @throws IllegalArgumentException
     */
    public void deletePerson(String pid, String prefix) throws IllegalArgumentException {
        if (!Person.checkPID(pid, prefix)) {
            throw new IllegalArgumentException(String.format(D_PID_ILLEGAL, getFullPersonTypeName(prefix)));
        }

        Person personById = getIndexer(PID).get(pid);
        if (personById == null) {
            throw new IllegalArgumentException(String.format(D_PID_DOESNT_EXIST, getFullPersonTypeName(prefix)));
        }

        Set<Person> set = items.get(prefix);
        set.remove(personById);
        getIndexer(PID).remove(personById.getPID());
        getIndexer(NAME).remove(personById.getName());
        getIndexer(PHONE_NUM).remove(personById.getPhoneNum());
    }

    public Waiter getFirstServableWaiter() {
        Set<Person> waiters = items.get(WAITER);
        PriorityQueue<Waiter> waitersQueue = new PriorityQueue<>((w1, w2) -> {
            int countRes = Integer.compare(w1.getOrders().size(), w2.getOrders().size());
            if (countRes != 0) {
                return countRes;
            }

            return w1.getPID().compareTo(w2.getPID());
        });
        for (Person waiter : waiters) {
            waitersQueue.add((Waiter) waiter);
        }

        return waitersQueue.peek();
    }

    /**
     * 打印人员列表
     * 
     * @throws IllegalStateException
     */
    public void print() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(EMPTY_PERSON_LIST);
        }

        int i = 1;
        for (Set<Person> set : items.values()) {
            for (Person person : set) {
                System.out.println(String.format("%d.%s", i++, person));
            }
        }
    }
}
