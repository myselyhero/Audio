package com.yongyongwang.audio.record.util;

import android.media.MediaExtractor;
import android.media.MediaFormat;
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
     * 查找视频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectVideoTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找音频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectAudioTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取音频、视频信息 时长 微秒 us
     *
     * @param url
     * @return
     */
    public static long getDuration(String url,boolean isVideo) {
        if (TextUtils.isEmpty(url))
            return -1;
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            int ext = isVideo ? selectVideoTrack(mediaExtractor) : selectAudioTrack(mediaExtractor);
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(ext);
            long res = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;//时长
            mediaExtractor.release();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取视频时长 单位：秒
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
        return Integer.parseInt(rotation) / 1000;//转为秒
    }

    /**
     * 获取音频时长 单位：秒
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
        return Integer.parseInt(rotation) / 1000;//转为秒
    }
}
