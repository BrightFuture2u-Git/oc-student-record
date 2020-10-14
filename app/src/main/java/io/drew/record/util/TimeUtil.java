package io.drew.record.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class TimeUtil {

    private static SimpleDateFormat yyyyMMddhhmmssFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentStringTime() {
        return format(System.currentTimeMillis(), yyyyMMddhhmmssFormatter);
    }


    private static String format(Object time, SimpleDateFormat format) {
        if (time instanceof String) {
            String s = (String) time;
            if (TextUtils.isEmpty(s)) {
                return format.format(new Date(0));
            } else {
                return format.format(new Date(Long.parseLong(s)));
            }
        } else if (time instanceof Long) {
            long l = (Long) time;
            if (l <= 0) {
                return format.format(new Date(0));
            } else {
                return format.format(new Date(l));
            }
        } else {
            return format.format(new Date(0));
        }
    }

    public static String stringForTimeHMS(long timeS, String formatStrHMS) {
        long seconds = timeS % 60;
        long minutes = timeS / 60 % 60;
        long hours = timeS / 3600;
        return new Formatter(new StringBuffer(), Locale.getDefault())
                .format(formatStrHMS, hours, minutes, seconds).toString();
    }

    public static String stringForTimeMS(long seconds) {
        String m = String.valueOf(seconds / 60);
        String s = String.valueOf(seconds % 60);
        if (m.length() < 2) {
            m = "0" + m;
        }
        if (s.length() < 2) {
            s = "0" + s;
        }
        return m + "分" + s + "秒";
    }

    public static String getVideoTime(long time) {
        String formatTime;
        long second = time / 1000;
        if (second <= 0) {
            formatTime = "00:00";
        } else if (second < 60) {
            formatTime = String.format(Locale.getDefault(), "00:%02d", second % 60);
        } else if (second < 3600) {
            formatTime = String.format(Locale.getDefault(), "%02d:%02d", second / 60, second % 60);
        } else {
            formatTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", second / 3600, second % 3600 / 60, second % 60);
        }
        return formatTime;
    }

}
