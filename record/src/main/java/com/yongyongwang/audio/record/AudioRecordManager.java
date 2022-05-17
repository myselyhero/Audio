package com.yongyongwang.audio.record;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import com.yongyongwang.audio.record.model.OnAudioPlayerListener;
import com.yongyongwang.audio.record.model.OnAudioRecordListener;
import com.yongyongwang.audio.record.util.FileUtils;
import com.yongyongwang.audio.record.util.LogUtil;

import java.io.File;

/**
 * @author yongyongwang 
 * 
 * @desc:
 * 
 * @// TODO: 2022/5/12
 */
public class AudioRecordManager {

    private static AudioRecordManager instance;

    public static final String FILE_SUFFIX = ".m4a";

    /* 播放器 */
    private MediaPlayer mediaPlayer;
    /*  */
    private MediaRecorder mMediaRecorder;
    /* 是否正在录制 */
    private volatile Boolean isRecord = false;
    private AudioRecordThread recordThread;
    /* 是否正在播放 */
    private boolean isPlayer;
    /* 时间 */
    private long startTime,endTime;
    /* 保存路径 */
    private String filePath;
    /* 监听 */
    private OnAudioRecordListener recordListener;
    private OnAudioPlayerListener playerListener;

    public static AudioRecordManager getInstance() {
        if (instance == null){
            synchronized (AudioRecordManager.class){
                instance = new AudioRecordManager();
            }
        }
        return instance;
    }

    /**
     *
     * @return
     */
    public String getFilePath(){
        return filePath;
    }

    /**
     *
     * @return
     */
    public boolean isRecord(){
        return isRecord;
    }

    /**
     * 开始录音
     * @param path
     * @param listener
     */
    public void startRecord(String path, OnAudioRecordListener listener){
        synchronized (isRecord){
            if (isRecord)
                return;
            isRecord = true;
            filePath = path;
            recordListener = listener;
            recordThread = new AudioRecordThread();
            recordThread.start();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord(){
        synchronized (isRecord){
            if (!isRecord)
                return;
            /* 释放 */
            if (mMediaRecorder != null) {
                try {
                    isRecord = false;
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /* 回调 */
            endTime = System.currentTimeMillis();
            if (recordListener != null)
                recordListener.complete(filePath,endTime - startTime);
            /* 销毁线程 */
            if (recordThread != null)
                recordThread.stop();
        }
    }

    /**
     *
     * @return
     */
    private boolean isPlayer(){
        return isPlayer;
    }

    /**
     * 播放
     * @param path
     * @param listener
     */
    public void startPlayer(String path, OnAudioPlayerListener listener){
        if (isPlayer || TextUtils.isEmpty(path))
            return;
        playerListener = listener;
        new PlayerRecordThread(path).start();
    }

    /**
     *
     */
    public void stopPlayer(){
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            isPlayer = false;
            playerListener = null;
        }
    }

    /* 内部API */


    /**
     *录音
     */
    private class AudioRecordThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (TextUtils.isEmpty(filePath)){
                filePath = FileUtils.AUDIO_PATH + File.separator + System.currentTimeMillis() + FILE_SUFFIX;
            }
            try {
                mMediaRecorder = new MediaRecorder();
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //RAW_AMR虽然被高版本废弃，但它兼容低版本还是可以用的
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                mMediaRecorder.setOutputFile(filePath);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mMediaRecorder.setOnErrorListener((mediaRecorder, i, i1) -> {
                    LogUtil.e("录音出现了问题："+i+"i1:"+i1);
                    stopRecord();
                });
                startTime = System.currentTimeMillis();
                synchronized (isRecord) {
                    if (!isRecord)
                        return;
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    if (recordListener != null)
                        recordListener.start(filePath);
                }
                new Thread() {
                    @Override
                    public void run() {
                        while (isRecord) {
                            try {
                                AudioRecordThread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (recordListener != null){
                                recordListener.recordContinue(System.currentTimeMillis() - startTime);
                            }
                        }
                    }
                }.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放
     */
    private class PlayerRecordThread extends Thread {

        private String mPath;

        public PlayerRecordThread(String path){
            mPath = path;
        }

        @Override
        public void run() {
            super.run();
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(mPath);
                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                    isPlayer = false;
                    if (playerListener != null)
                        playerListener.complete();
                });
                mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
                    mediaPlayer.start();
                    isPlayer = true;
                    if (playerListener != null){
                        playerListener.start(mediaPlayer1);
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "run: 语音文件已损坏或不存在");
                e.printStackTrace();
                isPlayer = false;
                if (playerListener != null)
                    playerListener.complete();
            }
        }
    }
}
