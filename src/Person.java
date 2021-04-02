/*
 * @Author: Zhe Chen
 * @Date: 2021-03-21 00:19:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-03-25 19:39:59
 * @Description: 人类
 */
import java.util.regex.Pattern;

/**
 * @description: 人类
 */
public class Person {
    /**
     * @description: 检查性别
     * @param {char} sex
     * @return {*}
     */
    public static boolean checkSex(char sex) {
        return sex == 'M' || sex == 'F';
    }

    /**
     * @description: 检查手机号
     * @param {String} phoneNum
     * @return {*}
     */
    public static boolean checkNum(String phoneNum) {
        return Pattern.matches("^[1]([3-7][0-9]|[8][0-7])[0-9]{4}[0]([3][1-9]|[4-6][0-9]|[7][0-1])(0|1)$", phoneNum);
    }

    private String userName;
    private char sex;
    private String phoneNum;

    public Person(String userName, char sex, String phoneNum) {
        setUserName(userName);
        setSex(sex);
        setPhoneNum(phoneNum);
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public char getSex() {
        return sex;
    }
    public void setSex(char sex) {
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
        return String.format("Name:%s\nSex:%c\nPhone:%s", getUserName(), getSex(), getPhoneNum());
    }
}