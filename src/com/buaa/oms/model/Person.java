/*
 * @Author: Zhe Chen
 * @Date: 2021-03-21 00:19:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 10:48:43
 * @Description: 人类
 */
package com.buaa.oms.model;

import java.util.regex.Pattern;

/**
 * @description: 人类
 */
public class Person {
    /**
     * @description: 检查性别
     * @param {String} sex
     * @return {*}
     */
    public static boolean checkSex(String sex) {
        return Pattern.matches("^(M|F)$", sex);
    }

    /**
     * @description: 检查手机号
     * @param {String} phoneNum
     * @return {*}
     */
    public static boolean checkNum(String phoneNum) {
        return Pattern.matches("^[1]([3-7][0-9]|[8][0-7])[0-9]{4}[0]([3][1-9]|[4-6][0-9]|[7][0-1])(0|1)$", phoneNum);
    }

    /**
     * @description: 检查标识序列号
     * @param {String} pid
     * @return {*}
     */
    public static boolean checkPID(String pid) {
        return Pattern.matches("^(Cu|Wa|Bo|Co)[0-9]{5}$", pid);
    }

    /**
     * @description: 检查密码检查标识序列号
     * @param {String} pwd
     * @return {*}
     */
    public static boolean checkPwd(String pwd) {
        return Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[\\x21-\\x7e]{8,18}$", pwd);
    }

    private String pid;
    private String pwd;
    private String name;
    private String sex;
    private String phoneNum;

    public Person(String name, String sex, String phoneNum) {
        setName(name);
        setSex(sex);
        setPhoneNum(phoneNum);
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

    @Override
    public String toString() {
        return String.format("Name:%s\nSex:%s\nPhone:%s", getName(), getSex(), getPhoneNum());
    }
}