package com.yongyongwang.audio.record;

import android.content.Context;
import android.os.Build;

import com.yongyongwang.audio.record.util.FileUtils;

/**
 * @author yongyongwang 
 * 
 * @desc:
 * 
 * @// TODO: 2022/5/12
 */
public class AudioKit {

    private static Context appContext;

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context){
        appContext = context;

        FileUtils.initPath(context);
    }

    /**
     *
     * @return
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * Android版本大于等于29
     * @return
     */
    public static boolean isAndroidQ(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
