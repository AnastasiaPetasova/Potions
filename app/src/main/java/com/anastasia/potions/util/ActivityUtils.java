package com.anastasia.potions.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtils {

    public static void createActivity(Activity activity, Class<?> activityClass) {
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
    }

    public static void createActivity(Activity activity, Class<?> activityClass, Bundle bundle) {

    }
}
