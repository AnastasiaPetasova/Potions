package com.anastasia.potions.util;

import java.util.Map;

public class MapUtils {

    public static <ValueType> int getCount(Map<ValueType, Integer> counts, ValueType value) {
        Integer count = counts.get(value);
        return (null == count ? 0 : count);
    }

    public static <ValueType> void increase(Map<ValueType, Integer> counts, ValueType value) {
        Integer count = getCount(counts, value);
        counts.put(value, count + 1);
    }

    public static <ValueType> void decrease(Map<ValueType, Integer> counts, ValueType value) {
        Integer count = getCount(counts, value);
        if (count == 1) {
            counts.remove(value);
        } else if (count > 1) {
            counts.put(value, count - 1);
        }
    }
}
