/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 11:07:44
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 17:37:26
 * @Description: 人员列表服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.PersonList;

/**
 * 人员列表服务
 */
public final class PersonListService implements ICommandContainer {
    // <editor-fold> 添加人np
    private final Lazy<ICommand> newPersonCommand = new Lazy<>(() -> new StandardCommand("np", (runtimeArgs) -> {
        if (runtimeArgs.length != 4) {
            // return new RunResult(ARGUMENTS_ILLEGAL);
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            return PersonList.instance.addPerson(runtimeArgs[1], runtimeArgs[2], runtimeArgs[3]);
        }, (person) -> new RunResult(person.toNPString()));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(newPersonCommand.getValue()));
    // </editor-fold>
    
    public static final PersonListService instance = new PersonListService();

    private PersonListService() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}
