package com.tencent.weibo.utils;

public class Utils {
    public static String intToIp(int paramInt) {
        return (0xFF & paramInt >> 24) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 8) + "." + (paramInt & 0xFF);
    }
}
