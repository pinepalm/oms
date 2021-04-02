/*
 * @Author: Zhe Chen
 * @Date: 2021-04-02 20:41:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 20:42:28
 * @Description: 事件类
 */
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 事件类
 */
public class Event<T extends EventArgs> {
    private Set<EventHandler<T>> handlers = new HashSet<>();

    public void addEventHandler(EventHandler<T> handler) {
        handlers.add(handler);
    }

    public void removeEventHandler(EventHandler<T> handler) {
        handlers.remove(handler);
    }

    public void invoke(Object sender, T args) {
        for (EventHandler<T> handler : handlers) {
            handler.handle(sender, args);
        }
    }
}
