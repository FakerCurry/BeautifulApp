package com.sjw.beautifulapp.utils;


/**
 * app完全退出
 */

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class ActivityListUtil {
    private List<Activity> activitys = null;
    private static ActivityListUtil instance;

    private ActivityListUtil() {
        activitys = new LinkedList<Activity>();
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static ActivityListUtil getInstance() {
        if (null == instance) {
            instance = new ActivityListUtil();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        } else {
            activitys.add(activity);
        }

    }

    // 遍历所有Activity并finish,然后退出
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    // 移除当前的activity
    public void removeActivity(Activity activity) {
//        if (activitys != null && activitys.size() > 0) {
//            if (activitys.contains(activity)) {

        activitys.remove(activity);
//            }
//        }
    }

}
