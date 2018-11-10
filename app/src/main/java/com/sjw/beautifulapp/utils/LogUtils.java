package com.sjw.beautifulapp.utils;

import android.util.Log;

/**
 * Created by pc on 2018/9/3.
 */

public class LogUtils {


    public static void logE(String tag, String text) {
        boolean flag = true;
        if (flag) {

            Log.e(tag, text);
        }

    }
}
