package com.yongyongwang.audio.record.util;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

/**
 * @author yongyongwang
 * 
 * @desc:音视频工具类
 * 
 * @// TODO: 2022/5/17
 */
public class AudioUtils {

    /**
     * 获取视频时长
     *
     * @param videoPath
     * @return
     */
    public static int getVideoDuration(String videoPath) {
        if (TextUtils.isEmpty(videoPath))
            return -1;
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 视频时长 毫秒
        retr.release();
        return Integer.parseInt(rotation);
    }

    /**
     * 获取音频时长
     * @param path
     * @return
     */
    public static long getAudioDuration(String path) {
        if (TextUtils.isEmpty(path))
            return -1;
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(path);
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 视频时长 毫秒
        retr.release();
        return Integer.parseInt(rotation);
    }
}
