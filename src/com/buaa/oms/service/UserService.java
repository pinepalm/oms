/*
 * @Author: Zhe Chen
 * @Date: 2021-04-14 23:38:55
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-25 19:47:12
 * @Description: 用户服务
 */
package com.buaa.oms.service;

import java.util.Arrays;

import com.buaa.appmodel.cli.CliAppRunner;
import com.buaa.appmodel.cli.ExactMatchCommand;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.OmsApp;
import com.buaa.oms.dao.PersonList;
import com.buaa.oms.model.Person;

/**
 * 用户服务
 */
public final class UserService implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String SUCCESS = "%s success";
    private static final String LOGOUT_SUCCESS = String.format(SUCCESS, "Logout");
    private static final String CHANGE_PASSWORD_SUCCESS = String.format(SUCCESS, "Change password");

    private static final String ILLEGAL = "%s illegal";
    private static final String NEW_PASSWORD_ILLEGAL = String.format(ILLEGAL, "New password");

    private static final String NOT_EXIST = "%s not exist";
    private static final String PID_NOT_EXIST = String.format(NOT_EXIST, "Pid");
    private static final String PNAME_NOT_EXIST = String.format(NOT_EXIST, "Pname");

    private static final String NOT_MATCH = "Not match";
    private static final String PASSWORD_NOT_MATCH = "Password not match";
    // </editor-fold>

    /**
     * 登录
     * 
     * @param user     用户
     * @param password 密码
     * @param mode     模式
     * @return 用户服务
     * @throws IllegalArgumentException
     */
    public static UserService login(String user, String password, LoginMode mode) throws IllegalArgumentException {
        Person person = null;

        switch (mode) {
            case ID:
                person = PersonList.instance.getAnyPersonById(user);
                if (person == null) {
                    throw new IllegalArgumentException(PID_NOT_EXIST);
                }

                break;
            case NAME:
                person = PersonList.instance.getAnyPersonByName(user);
                if (person == null) {
                    throw new IllegalArgumentException(PNAME_NOT_EXIST);
                }

                break;
            default:
                break;
        }
        if (!password.equals(person.getPwd())) {
            throw new IllegalArgumentException(PASSWORD_NOT_MATCH);
        }

        return new UserService(person).activate();
    }

    // <editor-fold> 修改密码chgpw
    private final Lazy<StandardCommand> chgpwCommand = new Lazy<>(() -> new StandardCommand("chgpw", (runtimeArgs) -> {
        if (runtimeArgs.length != 3) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            changePassword(runtimeArgs[1], runtimeArgs[2]);
        }, () -> new RunResult(CHANGE_PASSWORD_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 我的信息myinfo
    private final Lazy<ExactMatchCommand> myinfoCommand = new Lazy<>(() -> new ExactMatchCommand("myinfo", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            printInfo();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 退出登录back
    private final Lazy<ExactMatchCommand> backCommand = new Lazy<>(() -> new ExactMatchCommand("back", () -> {
        return RunRequestUtil.handleRunRequest(() -> {
            logout();
        }, () -> new RunResult(LOGOUT_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(chgpwCommand.getValue(), myinfoCommand.getValue(), backCommand.getValue()));
    // </editor-fold>

    private final Person person;
    private final IPersonService personService;

    private CliAppRunner runner;

    private UserService(Person person) {
        this.person = person;
        this.personService = person.createService();
    }

    private UserService activate() {
        runner = OmsApp.getInstance().runnerHost.getRunnerForCurrentView();
        if (runner != null) {
            runner.load(this, personService);
        }

        return this;
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    /**
     * 修改密码
     * 
     * @param password 密码
     * @param confirm  确认密码
     * @throws IllegalArgumentException
     */
    public void changePassword(String password, String confirm) throws IllegalArgumentException {
        if (!Person.checkPwd(password)) {
            throw new IllegalArgumentException(NEW_PASSWORD_ILLEGAL);
        }

        if (!password.equals(confirm)) {
            throw new IllegalArgumentException(NOT_MATCH);
        }

        person.setPwd(password);
    }

    /**
     * 打印信息
     */
    public void printInfo() {
        System.out.println("[info]\n" + "| name:\t" + person.getName() + "\n" + "| Sex:\t" + person.getSex() + "\n"
                + "| Pho:\t" + person.getPhoneNum() + "\n" + "| PID:\t" + person.getPID() + "\n" + "| Pwd:\t"
                + person.getPwd() + "\n" + "| Type:\t" + person.getClass().getSimpleName());
    }

    /**
     * 退出登录
     */
    public void logout() {
        if (runner != null) {
            runner.unload(this, personService);
        }
    }
}
