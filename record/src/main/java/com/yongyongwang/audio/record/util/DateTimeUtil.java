package com.yongyongwang.audio.record.util;

import android.text.TextUtils;

/**
 * @author yongyongwang 
 * 
 * @desc:
 * 
 * @// TODO: 2022/5/13
 */
public class DateTimeUtil {

    private final static long second = 1000;//秒
    private final static long minute = 60 * second;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 格式化时间
     * @param date
     * @return
     */
    public static String getTime(long date){

        Long day1 = date / day;
        Long hour1 = (date - day1 * second) / hour;
        Long minute1 = (date - day1 * day - hour1 * hour) / minute;
        Long second1 = (date - day1 * day - hour1 * hour - minute1 * minute) / second;

        String str = "";
        if(day1 > 0) {
            str = day1+"天:";
        }
        if(hour1 > 0) {
            str = str + hour1+"小时:";
        }
        if(minute1 > 0) {
            if (minute1 < 10){
                str = str + "0" + minute1 + ":";
            }else {
                str = str + minute1 + ":";
            }
        }
        if(second1 > 0) {
            if (second1 < 10){
                str = str + "0" + second1;
            }else {
                str = str + second1;
            }
            if (minute1 == 0)
                str = str + "s";
        }
        return TextUtils.isEmpty(str) ? "00" : str;
    }
}
