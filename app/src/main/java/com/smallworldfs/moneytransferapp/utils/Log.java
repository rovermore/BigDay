package com.smallworldfs.moneytransferapp.utils;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.smallworldfs.moneytransferapp.BuildConfig;


/**
 * Created by ccasanova on 15/03/2018
 */
public class Log {

    private static String clearTag(String tag) {
        if (tag == null)
            return "";
        else if (tag.length() > 23)
            return tag.substring(0, 23);
        else
            return tag;
    }

    public static void e(String tag, String msg, Throwable e) {
        FirebaseCrashlytics.getInstance().recordException(e);
        if (BuildConfig.DEBUG) {
            android.util.Log.e(clearTag(tag), msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(clearTag(tag), msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(clearTag(tag), msg);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(clearTag(tag), msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(clearTag(tag), msg);
    }
}
