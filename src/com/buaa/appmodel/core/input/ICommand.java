/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 18:59:44
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:27:25
 * @Description: 命令接口
 */
package com.buaa.appmodel.core.input;

/**
 * 命令接口
 */
public interface ICommand {
    /**
     * 获取命令名称
     * 
     * @return 命令名称
     */
    String getName();
}