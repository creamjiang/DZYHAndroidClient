package com.dld.coupon.activity;

import android.app.Activity;

import com.dld.android.util.LogUtils;

import java.util.Stack;

public class ActivityManager {
    private static Stack<Activity> activities = new Stack();
    private static Activity current;

    public static void back() {
        try {
            while ((!(current instanceof MainActivity))
                    && (!(current.getParent() instanceof MainActivity)))
                current.finish();
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public static Activity getCurrent() {
        return current;
    }

    public static void pop() {
        if (!activities.isEmpty()) {
            activities.pop();
            if (!activities.isEmpty())
                current = (Activity) activities.peek();
            else
                current = null;
        }
    }

    public static void push(Activity paramActivity) {
        if (current != paramActivity)
            current = (Activity) activities.push(paramActivity);
    }
}
