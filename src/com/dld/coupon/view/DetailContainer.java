package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dld.android.net.Callback;
import com.dld.android.net.Http;
import com.dld.android.net.HttpConfig;
import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class DetailContainer extends LinearLayout {
    private static final int IMG_HIDE = 0;
    private static final int IMG_LOADED = 2;
    private static final int IMG_LOADING = 1;
    Handler handler;
    private int imageX;
    private int imageY;
    private boolean inFav;
    private Bitmap largeBitmap;
    private Bitmap lastBitmap;
    private int[] loadingImgs;
    private int loadingIndex = -1;
    private int state = 0;
    private Timer timer;
    private int tsize;
    private int tx;
    private int ty;
    private int x;
    private int y;

    public DetailContainer(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        int[] arrayOfInt = new int[8];
        arrayOfInt[0] = R.drawable.loading_1;
        arrayOfInt[1] = R.drawable.loading_2;
        arrayOfInt[2] = R.drawable.loading_3;
        arrayOfInt[3] = R.drawable.loading_4;
        arrayOfInt[4] = R.drawable.loading_5;
        arrayOfInt[5] = R.drawable.loading_6;
        arrayOfInt[6] = R.drawable.loading_7;
        arrayOfInt[7] = R.drawable.loading_8;
        this.loadingImgs = arrayOfInt;
        this.lastBitmap = null;
        this.largeBitmap = null;
        this.inFav = false;
        this.handler = new Handler();
    }

    private void doLoadImage(int paramInt) {
        if (!this.inFav)
            loadRemoteImage(paramInt);
        else
            loadLocalImage(paramInt);
    }

    private void hide() {
        this.state = 0;
        invalidate();
        clear();
    }

    private void imageLoaded(Bitmap paramBitmap) {
        clear();
        resize(rotate(paramBitmap));
        this.state = 2;
        postInvalidate();
    }

    private void loadLocalImage(int paramInt) {
        try {
            byte[] arrayOfByte = FileUtil.readFile(FileUtil.genFileName(
                    "http://www.dld.com/vlife/image?id=" + paramInt, 600, 600));
            imageLoaded(BitmapFactory.decodeByteArray(arrayOfByte, 0,
                    arrayOfByte.length));
            return;
        } catch (Exception localException) {
            while (true)
                loadRemoteImage(paramInt);
        }
    }

    private void loadRemoteImage(int paramInt) {
        Object localObject1 = "http://www.dld.com/vlife/image?id=" + paramInt;
        localObject1 = "http://www.dld.com/tuan/viewimage?u="
                + URLEncoder.encode((String) localObject1) + "&w=600&h=600";
        Object localObject2 = Cache.getCache((String) localObject1);
        if (localObject2 == null) {
            new Http(new ImageCallback((String) localObject1), new HttpConfig(
                    10, false)).start();
        } else {
            localObject1 = (byte[]) localObject2;
            imageLoaded(BitmapFactory.decodeByteArray((byte[]) localObject1, 0,
                    ((byte[]) localObject1).length));
        }
    }

    private void resize(Bitmap paramBitmap) {
        LogUtils.log("test", "before resize: " + paramBitmap.getWidth() + ","
                + paramBitmap.getHeight());
        int i1 = getWidth();
        int n = getHeight();
        int i = paramBitmap.getWidth();
        int k = paramBitmap.getHeight();
        int j = i;
        int m = k;
        int i3 = (int) (0.6F * i1);
        int i2 = (int) (0.6F * n);
        float f2 = k / i;
        if ((i < i3) && (k < i2)) {
            i = Math.min(i3, (int) (i2 / f2));
            k = (int) (f2 * i);
        }
        if (i > i1) {
            i = i1;
            k = (int) (f2 * i);
        }
        if (k > n) {
            k = n;
            i = (int) (k / f2);
        }
        Matrix localMatrix = new Matrix();
        float f1 = i / j;
        localMatrix.postScale(f1, f1);
        this.largeBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, j, m,
                localMatrix, false);
        this.imageX = ((i1 - i) / 2);
        this.imageY = ((n - k) / 2);
        LogUtils.log("test", "after resize: " + this.largeBitmap.getWidth()
                + "," + this.largeBitmap.getHeight());
    }

    private Bitmap rotate(Bitmap paramBitmap) {
        LogUtils.log("test", "before rotate: " + paramBitmap.getWidth() + ","
                + paramBitmap.getHeight());
        int i = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        if (i > j) {
            Matrix localMatrix = new Matrix();
            localMatrix.postRotate(90.0F);
            paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j,
                    localMatrix, true);
        }
        LogUtils.log("test", "after rotate: " + paramBitmap.getWidth() + ","
                + paramBitmap.getHeight());
        return paramBitmap;
    }

    public void clear() {
        if (this.timer != null)
            this.timer.cancel();
        if ((this.lastBitmap != null) && (!this.lastBitmap.isRecycled()))
            this.lastBitmap.recycle();
        if ((this.largeBitmap != null) && (!this.largeBitmap.isRecycled()))
            this.largeBitmap.recycle();
    }

    public void dispatchDraw(Canvas paramCanvas) {
        super.dispatchDraw(paramCanvas);
        try {
            if (this.state != 0)
                paramCanvas.drawARGB(200, 0, 0, 0);
            if ((this.state == 1) && (this.lastBitmap != null)
                    && (!this.lastBitmap.isRecycled())) {
                paramCanvas.drawBitmap(this.lastBitmap, this.x, this.y,
                        new Paint());
                Paint localPaint = new Paint(1);
                localPaint.setColor(-1);
                localPaint.setTextSize(this.tsize);
                paramCanvas.drawText("大图加载中...", this.tx, this.ty, localPaint);
            } else if ((this.state == 2) && (this.largeBitmap != null)
                    && (!this.largeBitmap.isRecycled())) {
                paramCanvas.drawBitmap(this.largeBitmap, this.imageX,
                        this.imageY, new Paint());
            }
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public void init(boolean paramBoolean) {
        this.inFav = paramBoolean;
    }

    public void loadImage(int paramInt) {
        this.state = 1;
        if (this.timer != null)
            this.timer.cancel();
        doLoadImage(paramInt);
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            public void run() {
                if (DetailContainer.this.state == 1) {
                    DetailContainer localDetailContainer = DetailContainer.this;
                    localDetailContainer.loadingIndex = (1 + localDetailContainer.loadingIndex);
                    if (DetailContainer.this.loadingIndex >= 8)
                        DetailContainer.this.loadingIndex = 0;
                    int k = DetailContainer.this.getWidth();
                    int i = DetailContainer.this.getHeight();
                    float f = k / 320;
                    int j = (int) (41.0F * f);
                    DetailContainer.this.x = ((k - j) / 2 - (int) (5.0F * f));
                    DetailContainer.this.y = ((i - j) / 2 - (int) (40.0F * f));
                    DetailContainer.this.tx = (k / 2 - (int) (47.0F * f));
                    DetailContainer.this.ty = (i / 2 + (int) (80.0F * f));
                    DetailContainer.this.tsize = (int) (26.0F * f);
                    if (DetailContainer.this.lastBitmap != null)
                        DetailContainer.this.lastBitmap.recycle();
                    Resources localResources = ActivityManager.getCurrent()
                            .getResources();
                    DetailContainer.this.lastBitmap = BitmapFactory
                            .decodeResource(
                                    localResources,
                                    DetailContainer.this.loadingImgs[DetailContainer.this.loadingIndex]);
                    DetailContainer.this.postInvalidate();
                }
            }
        }, 0L, 200L);
    }

    public boolean onHide() {
        boolean i;
        if (this.state != 0) {
            hide();
            i = true;
        } else {
            i = false;
        }
        return i;
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        return onHide();
    }

    private class ImageCallback extends Callback {
        private String url;

        public ImageCallback(String arg2) {
            this.custom = true;
            Object localObject;
            this.url = arg2;
        }

        public Context getContext() {
            return ActivityManager.getCurrent();
        }

        public Param getHeader() {
            return null;
        }

        public byte[] getPostParam() {
            return null;
        }

        public String getUrl() {
            return this.url;
        }

        public void onException(Exception paramException) {
            DetailContainer.this.handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(ActivityManager.getCurrent(), "大图加载失败。", 0)
                            .show();
                    DetailContainer.this.hide();
                }
            });
            LogUtils.e("test", "", paramException);
        }

        public void onRecieve(byte[] paramArrayOfByte) {
            try {
                Bitmap localBitmap = BitmapFactory.decodeByteArray(
                        paramArrayOfByte, 0, paramArrayOfByte.length);
                if (localBitmap == null)
                    return;
                if ((localBitmap.getWidth() > 10)
                        && (localBitmap.getHeight() > 10)) {
                    Cache.put(this.url, paramArrayOfByte);
                    DetailContainer.this.imageLoaded(localBitmap);
                }
            } catch (Exception localException) {
                onException(localException);
            }
            onException(new Exception("Image too small."));
        }
    }
}
