/*
 * @Author: Zhe Chen
 * @Date: 2021-04-25 16:04:21
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-05-16 14:28:06
 * @Description: Oms内嵌环境服务
 */
package com.buaa.oms.service;

import com.buaa.appmodel.cli.CliAppView;
import com.buaa.appmodel.cli.IRunnerDefinition;
import com.buaa.appmodel.core.input.ICommandContainer;
import com.buaa.foundation.IClosable;
import com.buaa.oms.OmsApp;

/**
 * Oms内嵌环境服务
 */
public abstract class OmsEmbeddedEnvService implements ICommandContainer, IClosable {
    private CliAppView parentView;
    private CliAppView bindingView;

    protected abstract IRunnerDefinition createRunnerDefinitionInternal();

    /**
     * 获取绑定的视图
     * 
     * @param createIfNotExist 当视图不存在时是否创建
     * @return 绑定的视图
     */
    private CliAppView getBindingView(boolean createIfNotExist) {
        if (bindingView != null)
            return bindingView;

        if (createIfNotExist) {
            OmsApp app = OmsApp.getInstance();
            parentView = app.getCurrentView();
            bindingView = app.openNewView(createRunnerDefinitionInternal());
        }

        return bindingView;
    }

    /**
     * 打开
     */
    public void open() {
        getBindingView(true).asCurrentView();
    }

    @Override
    public void close() {
        CliAppView bindingView = getBindingView(false);
        if (bindingView != null) {
            bindingView.close();
            parentView.asCurrentView();
        }
    }
}