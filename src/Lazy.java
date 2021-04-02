/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:34:56
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-03-26 20:16:45
 * @Description: 懒加载类
 */
import java.util.function.Supplier;

/**
 * @description: 懒加载类
 */
public class Lazy<T> {
    private boolean isValueCreated;
    private Supplier<T> valueFactory;
    private T value;

    private final Object syncRoot = new Object();

    public Lazy(Supplier<T> valueFactory) {
        this.valueFactory = valueFactory;
    }

    public boolean getIsValueCreated() {
        return isValueCreated;
    }

    public T getValue() {
        if (!isValueCreated) {
            synchronized (syncRoot) {
                if (!isValueCreated) {
                    value = valueFactory != null ? valueFactory.get() : null;
                    isValueCreated = true;
                }
            }
        }

        return value;
    }
}
