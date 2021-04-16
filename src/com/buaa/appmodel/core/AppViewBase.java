/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:51:26
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 09:50:03
 * @Description: 应用视图基类
 */
package com.buaa.appmodel.core;

import com.buaa.appmodel.core.event.Event;
import com.buaa.appmodel.core.event.EventArgs;

/**
 * @description: 应用视图基类
 */
@SuppressWarnings("rawtypes")
public abstract class AppViewBase<L extends AppViewBase, A extends AppBase> {
    public final Event<EventArgs> closed;

    public final A bindingApp;

    private boolean isCurrent;
    private Object tag;

    @SuppressWarnings("unchecked") 
    protected AppViewBase(A bindingApp) {
        this.bindingApp = bindingApp;
        this.bindingApp.views.add(this);
        closed = new Event<>();
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    private void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * @description: 作为当前视图
     * @param {*}
     * @return {*}
     */
    @SuppressWarnings("unchecked")
    public L asCurrentView() {
        if (!getIsCurrent()) {
            AppViewBase currentView = bindingApp.getCurrentView();
            if (currentView != null) {
                currentView.setIsCurrent(false);
            }

            setIsCurrent(true);
        }

        return (L) this;
    }

    /**
     * @description: 关闭
     * @param {*}
     * @return {*}
     */
    public void close() {
        if (bindingApp.views.remove(this)) {
            closed.invoke(this, null);
        }
    }
}
