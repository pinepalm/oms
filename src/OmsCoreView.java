/*
 * @Author: Zhe Chen
 * @Date: 2021-03-28 00:36:24
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 21:00:40
 * @Description: Oms核心视图
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @description: Oms核心视图
 */
public final class OmsCoreView {
    private static List<OmsCoreView> views = new ArrayList<>();
    private static List<OmsCoreView> readonlyViews = Collections.unmodifiableList(views);

    /**
     * @description: 打开新视图
     * @param {ICommandContainer...} containers
     * @return {*}
     */
    public static OmsCoreView openNewView(ICommandContainer... containers) {
        OmsCoreView view = new OmsCoreView(containers);
        views.add(view);
        return view;
    }

    /**
     * @description: 获取当前视图
     * @param {*}
     * @return {*}
     */
    public static OmsCoreView getCurrentView() {
        return getView((view) -> view.getIsCurrent());
    }

    /**
     * @description: 获取指定视图
     * @param {Predicate<OmsCoreView>} predicate
     * @return {*}
     */
    public static OmsCoreView getView(Predicate<OmsCoreView> predicate) {
        if (predicate == null)
            return null;

        for (OmsCoreView view : views) {
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
    public static List<OmsCoreView> getViews() {
        return readonlyViews;
    }

    /**
     * @description: 命令容器数组
     */
    public final ICommandContainer[] commandContainers;
    public final Event<EventArgs> closed;

    private OmsCoreView parent;
    private OmsCoreView child;
    private boolean isCurrent;
    private Object tag;

    private OmsCoreView(ICommandContainer[] containers) {
        commandContainers = containers;
        closed = new Event<>();
    }

    public OmsCoreView getParent() {
        return parent;
    }

    private void setParent(OmsCoreView parent) {
        this.parent = parent;
    }

    public OmsCoreView getChild() {
        return child;
    }

    private void setChild(OmsCoreView child) {
        this.child = child;
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
    public OmsCoreView asCurrentView() {
        if (!getIsCurrent()) {
            OmsCoreView currentView = getCurrentView();
            if (currentView != null) {
                currentView.setIsCurrent(false);
            }

            setIsCurrent(true);
        }

        return this;
    }

    /**
     * @description: 打开子视图
     * @param {ICommandContainer...} containers
     * @return {*} 若不存在则打开新视图并返回，否则直接返回现有子视图
     */
    public OmsCoreView openChildView(ICommandContainer... containers) {
        if (getChild() == null) {
            OmsCoreView view = openNewView(containers);
            view.setParent(this);
            setChild(view);
        }

        return getChild();
    }

    /**
     * @description: 关闭子视图
     * @param {*}
     * @return {*}
     */
    public void closeChildView() {
        OmsCoreView view = getChild();
        if (view == null)
            return;

        view.close();
    }

    /**
     * @description: 关闭
     * @param {*}
     * @return {*}
     */
    public void close() {
        OmsCoreView view = getParent();
        if (view != null) {
            view.setChild(null);
            setParent(null);
        }

        views.remove(this);
        closed.invoke(this, null);
        
        closeChildView();
    }
}