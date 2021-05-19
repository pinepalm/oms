/*
 * @Author: Zhe Chen
 * @Date: 2021-04-24 12:43:03
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 22:41:14
 * @Description: 人员服务
 */
package com.buaa.oms.service;

import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.oms.model.Person;

/**
 * 人员服务
 */
public abstract class PersonService<T extends Person> implements ICommandContainer {
    protected final T person;

    protected PersonService(T person) {
        this.person = person;
    }
}
