package com.dld.protocol.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.dld.android.net.Callback;
import com.dld.android.net.Http;
import com.dld.android.net.HttpConfig;
import com.dld.android.net.Param;
import com.dld.android.util.Tools;
import com.dld.coupon.util.Tag;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProtocol {
    private static HashMap<String, ArrayList<Object>> penddingBitmaps = new HashMap();
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
    private String cacheKey;
    private Context context;
    Handler handler = new Handler();
    private Http http;
    private Param param;
    private String url;

    public ImageProtocol(Context paramContext, String paramString) {
        this.context = paramContext;
        this.url = paramString;
        this.cacheKey = paramString;
    }

    public ImageProtocol(Context paramContext, String paramString1,
            String paramString2) {
        this.context = paramContext;
        this.url = paramString1;
        this.cacheKey = ("img:" + paramString2);
    }

    private void fillImageHolder(Object paramObject, final Bitmap paramBitmap) {
        if ((paramObject instanceof ImageView)) {
            final ImageView localImageView = (ImageView) paramObject;
            this.handler.post(new Runnable() {
                public void run() {
                    if (localImageView.getTag() != Tag.IMG_RECYLED)
                        if (paramBitmap.getWidth() >= 10) {
                            localImageView.setImageBitmap(paramBitmap);
                            localImageView.setTag(Tag.IMG_CHANGED);
                        } else {
                            localImageView.setImageResource(R.drawable.store);
                        }
                }
            });
        }
    }

    public Param getParam() {
        if (this.param == null)
            this.param = new Param();
        return this.param;
    }

    public void startTrans(ImageView paramImageView) {
        pool.submit(new Runnable() {
            public void run() {
                Object localObject = Cache.getCache(ImageProtocol.this.url);
                if (localObject == null) {
                    localObject = (ArrayList) ImageProtocol.penddingBitmaps
                            .get(ImageProtocol.this.cacheKey);
                    if (localObject == null) {
                        localObject = new ArrayList();
                        ImageProtocol.penddingBitmaps.put(
                                ImageProtocol.this.cacheKey,
                                (ArrayList<Object>) localObject);
                        ImageProtocol.this.http = new Http(
                                new ImageProtocol.ImageCallback(),
                                new HttpConfig(10, false));
                        ImageProtocol.this.http.start();
                    }
                    ((ArrayList) localObject).add(handler);
                } else {
                    localObject = (byte[]) localObject;
                    localObject = BitmapFactory.decodeByteArray(
                            (byte[]) localObject, 0,
                            ((byte[]) localObject).length);
                    ImageProtocol.this.fillImageHolder(handler,
                            (Bitmap) localObject);
                }
            }
        });
    }

    private class ImageCallback extends Callback {
        public ImageCallback() {
            this.custom = true;
        }

        public Context getContext() {
            return ImageProtocol.this.context;
        }

        public Param getHeader() {
            return null;
        }

        public byte[] getPostParam() {
            return null;
        }

        public String getUrl() {
            return ImageProtocol.this.url;
        }

        public void onException(Exception paramException) {
            Tools.printException(paramException);
        }

        public void onRecieve(byte[] paramArrayOfByte) {
            Object localObject = null;
            Bitmap localBitmap = null;
            try {
                localBitmap = BitmapFactory.decodeByteArray(paramArrayOfByte,
                        0, paramArrayOfByte.length);
                localObject = (ArrayList) ImageProtocol.penddingBitmaps
                        .remove(ImageProtocol.this.cacheKey);
                if (localBitmap == null)
                    return;
                if ((localBitmap.getWidth() <= 10)
                        || (localBitmap.getHeight() <= 10))
                    onException(new Exception("image too small"));
            } catch (Exception localException) {
                onException(localException);
            }
            Iterator localIterator = null;
            if (localObject != null)
                localIterator = ((ArrayList) localObject).iterator();
            while (true) {
                if (!localIterator.hasNext()) {
                    Cache.put(ImageProtocol.this.url, paramArrayOfByte);
                    break;
                }
                localObject = localIterator.next();
                ImageProtocol.this.fillImageHolder(localObject, localBitmap);
            }
        }
    }
}
