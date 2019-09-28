package com.orient.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的辅助工具类
 */
public class DateUtils {

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 日期转yyyy-MM-dd格式
     * @param date 日期
     * @return String
     */
    public static String date2SDayFormat(Date date){
        return dayFormat.format(date);
    }
}
