/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 10:38:42
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 11:39:25
 * @Description: Oms应用
 */
package com.buaa.oms;

import com.buaa.appmodel.cli.CliApp;
import com.buaa.appmodel.cli.CliAppView;
import com.buaa.appmodel.cli.IRunnerDefinition;
import com.buaa.appmodel.cli.RunResult;
import com.buaa.appmodel.core.event.EventArgs;
import com.buaa.appmodel.core.event.EventHandler;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.oms.service.MenuService;
import com.buaa.oms.service.OmsService;
import com.buaa.oms.service.PersonListService;
import com.buaa.util.Lazy;

/**
 * @description: Oms应用
 */
public final class OmsApp extends CliApp {
    public static OmsApp instance = new OmsApp();
    
    private EventHandler<EventArgs> onMainViewClosed;
    
    private OmsApp() {

    }
    
    @Override
    protected CliAppView openMainView() {
        onMainViewClosed = (sender, args) -> {
            CliAppView mainView = (CliAppView) sender;
            mainView.closed.removeEventHandler(onMainViewClosed);
            System.exit(0);
        };

        CliAppView mainView = openNewView(new MainRunnerDefinition()).asCurrentView();
        mainView.closed.addEventHandler(onMainViewClosed);
        return mainView;
    }
}

final class MainRunnerDefinition implements IRunnerDefinition {
    private final Lazy<ICommandContainer[]> containers;

    public MainRunnerDefinition() {
        containers = new Lazy<>(() -> {
            ICommandContainer[] containers = { OmsService.instance, PersonListService.instance, MenuService.instance };
            return containers;
        });
    }

    @Override
    public ICommandContainer[] getCommandContainers() {
        return containers.getValue();
    }

    @Override
    public RunResult getEmptyCommandResult() {
        return RunResult.inputIllegal;
    }

    @Override
    public RunResult getUnknownCommandResult() {
        return RunResult.commandNotExist;
    }
}