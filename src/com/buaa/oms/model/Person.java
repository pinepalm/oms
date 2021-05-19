/*
 * @Author: Zhe Chen
 * @Date: 2021-03-21 00:19:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 22:39:42
 * @Description: 人类
 */
package com.buaa.oms.model;

import java.util.regex.Pattern;

import com.buaa.oms.service.PersonService;

/**
 * 人类
 */
public class Person {
    /**
     * 检查姓名
     * 
     * @param name 姓名
     * @return 是否合法
     */
    public static boolean checkName(String name) {
        return Pattern.matches("^[0-9a-zA-Z]+$", name);
    }

    /**
     * 检查性别
     * 
     * @param sex 性别
     * @return 是否合法
     */
    public static boolean checkSex(String sex) {
        return Pattern.matches("^(M|F)$", sex);
    }

    /**
     * 检查手机号
     * 
     * @param phoneNum 手机号
     * @return 是否合法
     */
    public static boolean checkNum(String phoneNum) {
        return Pattern.matches("^[1]([3-7][0-9]|[8][0-7])[0-9]{4}[0]([3][1-9]|[4-6][0-9]|[7][0-1])(0|1)$", phoneNum);
    }

    /**
     * 检查标识序列号
     * 
     * @param pid    人员id
     * @param prefix 类型前缀，比如Cu、Cu|Wa|Co
     * @return 是否合法
     */
    public static boolean checkPID(String pid, String prefix) {
        return Pattern.matches(String.format("^(%s)[0-9]{5}$", prefix), pid);
    }

    /**
     * 检查密码
     * 
     * @param pwd 密码
     * @return 是否合法
     */
    public static boolean checkPwd(String pwd) {
        return Pattern.matches("^(?=.*?[a-zA-Z])(?=.*?[0-9])[!-~]{8,18}$", pwd);
    }

    private String name;
    private String sex;
    private String phoneNum;
    private String pid;
    private String pwd;

    public Person(String name, String sex, String phoneNum) {
        this(name, sex, phoneNum, null);
    }

    public Person(String name, String sex, String phoneNum, String pid) {
        setName(name);
        setSex(sex);
        setPhoneNum(phoneNum);
        setPID(pid);
        setPwd("oms1921SE");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPID() {
        return pid;
    }

    public void setPID(String pid) {
        this.pid = pid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public PersonService<?> createService() {
        return null;
    }

    public String toNPString() {
        return String.format("Name:%s\nSex:%s\nPhone:%s", getName(), getSex(), getPhoneNum());
    }

    @Override
    public String toString() {
        return String.format("PID:%s,Name:%s,Sex:%s,Phone:%s,PWD:%s", getPID(), getName(), getSex(), getPhoneNum(),
                getPwd());
    }
}