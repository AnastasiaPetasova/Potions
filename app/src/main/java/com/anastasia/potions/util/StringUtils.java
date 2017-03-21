package com.anastasia.potions.util;

import java.util.Locale;

public class StringUtils {

    public static String intToString(int value) {
        return String.format(Locale.US, "%d", value);
    }
}
