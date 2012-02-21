package com.sina;

import android.content.Context;
import java.io.IOException;

public class AsyncWeiboRunner {
    private Weibo mWeibo;

    public AsyncWeiboRunner(Weibo paramWeibo) {
        this.mWeibo = paramWeibo;
    }

    public void request(Context paramContext, String paramString1,
            WeiboParameters paramWeiboParameters, String paramString2,
            RequestListener paramRequestListener) {
        new Thread()
        // {
        // public void run()
        // {
        // try
        // {
        // String str = AsyncWeiboRunner.this.mWeibo.request(this.val$context,
        // this.val$url, this.val$params, this.val$httpMethod,
        // AsyncWeiboRunner.this.mWeibo.getAccessToken());
        // if (this.val$listener != null)
        // this.val$listener.onComplete(str);
        // return;
        // }
        // catch (WeiboException localWeiboException)
        // {
        // while (true)
        // {
        // if (this.val$listener == null)
        // continue;
        // this.val$listener.onError(localWeiboException);
        // }
        // }
        // }
        // }
                .run();
    }

    public static abstract interface RequestListener {
        public abstract void onComplete(String paramString);

        public abstract void onError(WeiboException paramWeiboException);

        public abstract void onIOException(IOException paramIOException);
    }
}
