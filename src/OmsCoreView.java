/*
 * @Author: Zhe Chen
 * @Date: 2021-03-28 00:36:24
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 00:35:44
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
    public static final String MAIN_TAG = "Main";

    private static List<OmsCoreView> views = new ArrayList<>();
    private static List<OmsCoreView> readonlyViews = Collections.unmodifiableList(views);

    /**
     * @description: 打开新视图
     * @param {IRunnerDefinition} definition
     * @return {*}
     */
    public static OmsCoreView openNewView(IRunnerDefinition definition) {
        OmsCoreView view = new OmsCoreView(definition);
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
     * @description: 运行器定义
     */
    public final IRunnerDefinition runnerDefinition;
    public final Event<EventArgs> closed;

    private boolean isCurrent;
    private Object tag;

    private OmsCoreView(IRunnerDefinition definition) {
        runnerDefinition = definition;
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
     * @description: 关闭
     * @param {*}
     * @return {*}
     */
    public void close() {
        if (views.remove(this)) {
            closed.invoke(this, null);
        }
    }
}