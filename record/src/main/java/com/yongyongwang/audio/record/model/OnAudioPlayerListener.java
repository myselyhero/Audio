package com.yongyongwang.audio.record.model;

import android.media.MediaPlayer;

/**
 * @author yongyongwang
 *
 * @desc:语音播放监听
 *
 * @// TODO: 2022/5/12
 */
public interface OnAudioPlayerListener {

    /**
     *
     * @param player
     */
    void start(MediaPlayer player);

    /**
     * 播放结束
     */
    void complete();
}
