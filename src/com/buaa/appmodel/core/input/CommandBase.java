/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 18:52:54
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:27:08
 * @Description: 命令基类
 */
package com.buaa.appmodel.core.input;

/**
 * 命令基类
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