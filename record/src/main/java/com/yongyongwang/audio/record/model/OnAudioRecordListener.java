package com.yongyongwang.audio.record.model;

/**
 * @author yongyongwang 
 * 
 * @desc:语音录制监听
 * 
 * @// TODO: 2022/5/12
 */
public interface OnAudioRecordListener {

    /**
     *
     * @param path
     */
    void start(String path);

    /**
     * 完成
     * @param path 保存路径
     * @param duration 总时长
     */
    void complete(String path,long duration);

    /**
     * 持续回调
     * @param duration 已录制时长
     */
    void recordContinue(long duration);
}
