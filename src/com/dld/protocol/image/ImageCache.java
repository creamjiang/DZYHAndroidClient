package com.dld.protocol.image;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class ImageCache {
    private static ImageCache imageCache;
    public static HashMap<String, SoftReference<Bitmap>> mSoftCache;

    public ImageCache() {
        mSoftCache = new HashMap();
    }

    public static ImageCache getInstance() {
        if (imageCache == null)
            imageCache = new ImageCache();
        return imageCache;
    }

    public void flush() {
        mSoftCache.clear();
    }

    public Bitmap get(String paramString) {
        Object localObject = (SoftReference) mSoftCache.get(paramString);
        if (localObject != null) {
            localObject = (Bitmap) ((SoftReference) localObject).get();
            if (localObject == null)
                mSoftCache.remove(paramString);
        } else {
            localObject = null;
        }
        return (Bitmap) localObject;
    }

    public void put(String paramString, Bitmap paramBitmap) {
        mSoftCache.put(paramString, new SoftReference(paramBitmap));
    }
}
