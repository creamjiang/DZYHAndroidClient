package com.tencent.weibo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QStr {
    public static String decode(String paramString) {
        String str = null;
        if (paramString == null)
            str = "";
        // while (true)
        {
            // return str;
            try {
                str = URLDecoder.decode(paramString, "UTF-8");
                str = str;
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
        }
        return str;
        // throw new
        // RuntimeException(localUnsupportedEncodingException.getMessage(),
        // localUnsupportedEncodingException);
    }

    public static String def(String paramString1, String paramString2) {
        if (paramString1 != null)
            paramString2 = paramString1;
        return paramString2;
    }

    public static String encode(String paramString) {
        String str = null;
        if (paramString == null)
            str = "";
        // while (true)
        {
            // return str;
            try {
                str = URLEncoder.encode(paramString, "UTF-8")
                        .replace("+", "%20").replace("*", "%2A")
                        .replace("%7E", "~").replace("#", "%23");
                str = str;
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
        }
        return str;
        // throw new
        // RuntimeException(localUnsupportedEncodingException.getMessage(),
        // localUnsupportedEncodingException);
    }

    public static String fixTime(String paramString) {
        String str1;
        String str2 = null;
        if ((paramString == null) || ("".equals(paramString)))
            str1 = "";
        // while (true)
        {
            // return str1;
            // try
            // {
            // long l1 = 1000L * Long.parseLong(paramString);
            // long l2 = System.currentTimeMillis();
            // if (l2 - l1 < 60000L)
            // {
            // str2 = "刚刚";
            // continue;
            // }
            // if (System.currentTimeMillis() - str2 < 1800000L)
            // {
            // str2 = (System.currentTimeMillis() - str2) / 1000L / 60L + "分钟前";
            // continue;
            // }
            // Calendar localCalendar2 = Calendar.getInstance();
            // Calendar localCalendar1 = Calendar.getInstance();
            // localCalendar1.setTimeInMillis(str2);
            // if ((localCalendar1.get(1) == localCalendar2.get(1)) &&
            // (localCalendar1.get(2) == localCalendar2.get(2)) &&
            // (localCalendar1.get(5) == localCalendar2.get(5)))
            // {
            // str2 = new
            // SimpleDateFormat("今天 HH:mm").format(localCalendar1.getTime());
            // continue;
            // }
            // if ((localCalendar1.get(1) == localCalendar2.get(1)) &&
            // (localCalendar1.get(2) == localCalendar2.get(2)) &&
            // (localCalendar1.get(5) == -1 + localCalendar2.get(5)))
            // {
            // str2 = new
            // SimpleDateFormat("昨天 HH:mm").format(localCalendar1.getTime());
            // continue;
            // }
            // if (localCalendar1.get(1) == localCalendar2.get(1))
            // {
            // str2 = new
            // SimpleDateFormat("M月d日 HH:mm:ss").format(localCalendar1.getTime());
            // continue;
            // }
            // String str2 = new
            // SimpleDateFormat("yyyy年M月d日 HH:mm:ss").format(localCalendar1.getTime());
            // str2 = str2;
            // }
            // catch (Exception str3)
            // {
            // str3.printStackTrace();
            // // String str3 = "";
            // }
        }
        return str2;
    }
}
