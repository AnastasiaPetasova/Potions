package com.anastasia.potions.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiSet<ValueType> implements Iterable<Map.Entry<ValueType, Integer>> {

    final Map<ValueType, Integer> counts;
    int size;

    public MultiSet() {
        this.counts = new HashMap<>();
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public int getCount(ValueType value) {
        Integer count = counts.get(value);
        return (null == count ? 0 : count);
    }

    public void add(ValueType value) {
        add(value, 1);
    }

    public void add(ValueType value, int addCount) {
        Integer curCount = getCount(value);
        counts.put(value, curCount + addCount);
        size += addCount;
    }

    public void remove(ValueType value) {
        remove(value, 1);
    }

    public void remove(ValueType value, int removeCount) {
        Integer curCount = getCount(value);

        if (curCount > 0) {
            if (curCount <= removeCount) {
                counts.remove(value);
                size -= curCount;
            } else {
                counts.put(value, curCount - removeCount);
                size -= removeCount;
            }
        }
    }

    @Override
    public Iterator<Map.Entry<ValueType, Integer>> iterator() {
        return counts.entrySet().iterator();
    }
}
