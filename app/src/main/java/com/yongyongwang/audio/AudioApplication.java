package com.yongyongwang.audio;

import android.app.Application;

import com.yongyongwang.audio.record.AudioKit;

/**
 * @author yongyongwang 
 * 
 * @desc:
 * 
 * @// TODO: 2022/5/12
 */
public class AudioApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AudioKit.init(this);
    }
}
