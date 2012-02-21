package com.tencent.weibo.utils;

import java.util.HashMap;

public class Cache {
    private static HashMap<String, Object> cache = new HashMap();

    public static void clear() {
        cache.clear();
    }

    public static Object getCache(String paramString) {
        return cache.get(paramString);
    }

    public static void put(String paramString, Object paramObject) {
        cache.put(paramString, paramObject);
    }

    public static Object remove(String paramString) {
        return cache.remove(paramString);
    }
}
