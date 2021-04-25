/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:58:41
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 11:33:04
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
 * 应用基类
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
     * 打开新视图
     * 
     * @return 新视图
     */
    public abstract V openNewView();

    /**
     * 获取当前视图
     * 
     * @return 当前视图
     */
    public V getCurrentView() {
        return getView((view) -> view.getIsCurrent());
    }

    /**
     * 获取指定视图
     * 
     * @param predicate 判别器
     * @return 指定视图
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
     * 获取视图列表的只读映射
     * 
     * @return 视图列表的只读映射
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
