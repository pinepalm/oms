/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 18:52:54
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 01:20:18
 * @Description: 命令基类
 */
package com.buaa.appmodel.core.input;

/**
 * @description: 命令基类
 */
public abstract class CommandBase implements ICommand {
    private final String name;

    protected CommandBase(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}