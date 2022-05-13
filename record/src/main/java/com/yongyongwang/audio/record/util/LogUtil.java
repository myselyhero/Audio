package com.yongyongwang.audio.record.util;

import android.util.Log;

import com.yongyongwang.audio.record.BuildConfig;

/**
 * @author yongyongwang 
 * 
 * @desc:
 * 
 * @// TODO: 2022/5/13
 */
public class LogUtil {

    private static final String TAG = "AudioRecord";

    /**
     *
     * @param o
     * @return
     */
    private static String getTag(Object o){
        if (o instanceof Class){
            return o.getClass().getSimpleName();
        }else {
            return o.toString();
        }
    }

    /**
     *
     * @param i
     */
    public static void i(Object i){
        if (!BuildConfig.DEBUG)
            return;
        Log.i(TAG, i.toString());
    }

    /**
     *
     * @param tag
     * @param i
     */
    public static void i(Object tag,Object i){
        if (!BuildConfig.DEBUG)
            return;
        Log.i(getTag(tag), i.toString());
    }

    /**
     *
     * @param w
     */
    public static void w(Object w){
        if (!BuildConfig.DEBUG)
            return;
        Log.w(TAG, w.toString());
    }

    /**
     *
     * @param tag
     * @param w
     */
    public static void w(Object tag,Object w){
        if (!BuildConfig.DEBUG)
            return;
        Log.w(getTag(tag), w.toString());
    }

    /**
     *
     * @param e
     * @return
     */
    public static void e(Object e){
        if (!BuildConfig.DEBUG)
            return;
        Log.e(TAG, e.toString());
    }

    /**
     *
     * @param tag
     * @param e
     */
    public static void e(Object tag,Object e){
        if (!BuildConfig.DEBUG)
            return;
        Log.e(getTag(tag), e.toString());
    }
}
