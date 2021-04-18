/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 11:19:01
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 14:58:07
 * @Description: 菜单服务
 */
package com.buaa.oms.service;

import java.util.Arrays;
import java.util.Vector;

import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.cli.StandardCommand;
import com.buaa.appmodel.cli.util.RunRequestUtil;
import com.buaa.appmodel.core.input.ICommand;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.Lazy;
import com.buaa.oms.dao.Menu;
import com.buaa.oms.dao.MenuViewer;
import com.buaa.oms.model.Dish;
import com.buaa.util.DoubleUtil;
import com.buaa.util.IntegerUtil;

/**
 * @description: 菜单服务
 */
public final class MenuService implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String UPDATE_DISHS_SUCCESS = "Update dish's %s success";
    private static final String UPDATE_DISHS_NAME_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "name");
    private static final String UPDATE_DISHS_PRICE_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "price");
    private static final String UPDATE_DISHS_TOTAL_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "total");

    private static final String ADD_DISH_SUCCESS = "Add dish success";
    private static final String DISH_DOES_NOT_EXIST = "Dish does not exist";
    // </editor-fold>

    // <editor-fold> 打印菜单pm
    private final Lazy<StandardCommand> printMenuCommand = new Lazy<>(() -> new StandardCommand("pm", (runtimeArgs) -> {
        switch (runtimeArgs.length) {
            case 1:
                return RunRequestUtil.handleRunRequest(() -> {
                    Menu.instance.print();
                }, () -> RunResult.empty);
            case 3:
                return RunRequestUtil.handleRunRequest(() -> {
                    Integer pageIndex = IntegerUtil.tryParse(runtimeArgs[1], null);
                    Integer pageSize = IntegerUtil.tryParse(runtimeArgs[2], null);

                    MenuViewer viewer = new MenuViewer(Menu.instance, pageIndex, pageSize);
                    viewer.build().printCurrent();

                    MenuViewerService viewerService = new MenuViewerService(viewer);
                    viewerService.getBindingView(true).asCurrentView();
                }, () -> RunResult.empty);
            default:
                return RunResult.paramsCountIllegal;
        }
    }));
    // </editor-fold>
    // <editor-fold> 添加菜品nd
    private final Lazy<StandardCommand> newDishCommand = new Lazy<>(() -> new StandardCommand("nd", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        return RunRequestUtil.handleRunRequest(() -> {
            String did = runtimeArgs[1];
            String name = runtimeArgs[2];
            double price = DoubleUtil.tryParse(runtimeArgs[3], -1d);
            int total = IntegerUtil.tryParse(runtimeArgs[4], -1);

            Menu.instance.addDish(did, name, price, total);
        }, () -> new RunResult(ADD_DISH_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 获取菜品gd
    private final Lazy<StandardCommand> getDishCommand = new Lazy<>(() -> new StandardCommand("gd", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-id", "-key").contains(runtimeArgs[1])) {
            return RunResult.commandNotExist;
        }

        if (runtimeArgs.length != 3 && runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        switch (runtimeArgs[1]) {
            case "-id":
                return RunRequestUtil.handleRunRequest(() -> {
                    Dish dishById = Menu.instance.getDishById(runtimeArgs[2]);
                    if (dishById == null) {
                        throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
                    }

                    return dishById;
                }, (dishById) -> new RunResult(dishById.toString()));
            case "-key":
                switch (runtimeArgs.length) {
                    case 3:
                        return RunRequestUtil.handleRunRequest(() -> {
                            Vector<Dish> list = Menu.instance.getDishByKeyWord(runtimeArgs[2]);
                            if (list.isEmpty()) {
                                throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
                            }

                            int i = 1;
                            for (Dish dish : list) {
                                System.out.println(String.format("%d. %s", i++, dish));
                            }
                        }, () -> RunResult.empty);
                    case 5:
                        return RunRequestUtil.handleRunRequest(() -> {
                            Integer pageIndex = IntegerUtil.tryParse(runtimeArgs[3], null);
                            Integer pageSize = IntegerUtil.tryParse(runtimeArgs[4], null);

                            MenuViewer viewer = new MenuViewer(Menu.instance, runtimeArgs[2], pageIndex, pageSize);
                            viewer.build().printCurrent();

                            MenuViewerService viewerService = new MenuViewerService(viewer);
                            viewerService.getBindingView(true).asCurrentView();
                        }, () -> RunResult.empty);
                    default:
                        break;
                }

                break;
            default:
                break;
        }

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 修改菜品udd
    private final Lazy<StandardCommand> updateDishCommand = new Lazy<>(
            () -> new StandardCommand("udd", (runtimeArgs) -> {
                if (runtimeArgs.length < 2 || !Arrays.asList("-n", "-t", "-p").contains(runtimeArgs[1])) {
                    return RunResult.commandNotExist;
                }

                if (runtimeArgs.length != 4) {
                    return RunResult.paramsCountIllegal;
                }

                String did = runtimeArgs[2];

                switch (runtimeArgs[1]) {
                    case "-n":
                        return RunRequestUtil.handleRunRequest(() -> {
                            String name = runtimeArgs[3];
                            Menu.instance.updateDish(did, name);
                        }, () -> new RunResult(UPDATE_DISHS_NAME_SUCCESS));
                    case "-t":
                        return RunRequestUtil.handleRunRequest(() -> {
                            int total = IntegerUtil.tryParse(runtimeArgs[3], -1);
                            Menu.instance.updateDish(did, total);
                        }, () -> new RunResult(UPDATE_DISHS_TOTAL_SUCCESS));
                    case "-p":
                        return RunRequestUtil.handleRunRequest(() -> {
                            double price = DoubleUtil.tryParse(runtimeArgs[3], -1d);
                            Menu.instance.updateDish(did, price);
                        }, () -> new RunResult(UPDATE_DISHS_PRICE_SUCCESS));
                    default:
                        break;
                }

                return RunResult.empty;
            }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(printMenuCommand.getValue(),
            newDishCommand.getValue(), getDishCommand.getValue(), updateDishCommand.getValue()));
    // </editor-fold>

    public static final MenuService instance = new MenuService();

    private MenuService() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}
