/*
 * @Author: Zhe Chen
 * @Date: 2021-04-15 15:51:25
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 00:51:22
 * @Description: 组基类
 */
package com.buaa.foundation.collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @description: 组基类
 */
public abstract class GroupingBase<K, V> {
    protected final Map<K, Set<V>> items;
    protected final Map<String, Map<Object, V>> indexers;

    protected GroupingBase() {
        this(() -> new LinkedHashMap<>(), () -> new HashMap<>());
    }

    protected GroupingBase(Supplier<Map<K, Set<V>>> itemsBuilder,
            Supplier<Map<String, Map<Object, V>>> indexersBuilder) {
        items = itemsBuilder.get();
        indexers = indexersBuilder.get();
    }

    protected Map<Object, V> getIndexer(String name) {
        return indexers.get(name);
    }

    public boolean isEmpty() {
        for (Set<V> set : items.values()) {
            if (!set.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}