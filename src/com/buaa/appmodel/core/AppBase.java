/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:58:41
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-18 14:24:40
 * @Description: 应用基类
 */
package com.buaa.appmodel.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.buaa.appmodel.core.event.Event;
import com.buaa.appmodel.core.event.EventArgs;
import com.buaa.foundation.IClosable;

/**
 * @description: 应用基类
 */
@SuppressWarnings("rawtypes")
public abstract class AppBase<V extends AppViewBase> implements IClosable {
    protected final List<V> views = new ArrayList<>();
    protected final List<V> readonlyViews = Collections.unmodifiableList(views);

    public final Event<EventArgs> closed;
    public final V mainView;

    protected AppBase() {
        AppHost.apps.add(this);
        mainView = openMainView();
        closed = new Event<>();
    }

    protected abstract V openMainView();

    /**
     * @description: 打开新视图
     * @param {*}
     * @return {*}
     */
    public abstract V openNewView();

    /**
     * @description: 获取当前视图
     * @param {*}
     * @return {*}
     */
    public V getCurrentView() {
        return getView((view) -> view.getIsCurrent());
    }

    /**
     * @description: 获取指定视图
     * @param {Predicate<V>} predicate
     * @return {*}
     */
    public V getView(Predicate<V> predicate) {
        if (predicate == null)
            return null;

        for (V view : views) {
            if (predicate.test(view)) {
                return view;
            }
        }

        return null;
    }

    /**
     * @description: 获取视图列表的只读映射
     * @param {*}
     * @return {*}
     */
    public List<V> getViews() {
        return readonlyViews;
    }

    @Override
    public void close() {
        if (AppHost.apps.remove(this)) {
            closed.invoke(this, null);
        }
    }
}
