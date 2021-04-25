/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:51:26
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:34:53
 * @Description: 应用视图基类
 */
package com.buaa.appmodel.core;

import com.buaa.appmodel.core.event.Event;
import com.buaa.appmodel.core.event.EventArgs;
import com.buaa.foundation.IClosable;

/**
 * 应用视图基类
 */
@SuppressWarnings("rawtypes")
public abstract class AppViewBase<L extends AppViewBase, A extends AppBase> implements IClosable {
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
     * 作为当前视图
     * 
     * @return 自身
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

    @Override
    public void close() {
        if (bindingApp.views.remove(this)) {
            closed.invoke(this, null);
        }
    }
}
