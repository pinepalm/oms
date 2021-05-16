/*
 * @Author: Zhe Chen
 * @Date: 2021-04-09 20:17:52
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 21:39:45
 * @Description: 厨师类
 */
package com.buaa.oms.model;

import com.buaa.oms.service.CookService;
import com.buaa.oms.service.IPersonService;

/**
 * 厨师类
 */
public class Cook extends Person {
    public Cook(String name, String sex, String phoneNum, String pid) {
        super(name, sex, phoneNum, pid);
    }

    @Override
    public IPersonService createService() {
        return new CookService(this);
    }
}