package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchScrollView;
import android.widget.TextView;

import com.dld.android.net.HttpUtil;
import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.MD5Encoder;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.UMSearchBar;
import com.dld.coupon.view.UMSearchBar.OnSearchListener;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.HuoDongResponse;
import com.dld.protocol.json.Promotion;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity {
    public static final String[] ADS;
    static final CategoryImage[] CATEGORIES;
    private final int GETHUODONGURLSUCCESS = 5;
    private DragViewGroup activities;
    private int activityCount = 0;
    private DragViewGroup ads;
    private String cityName;
    Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
            case 5:
                SearchActivity.this.initHuoUrl();
            }
        }
    };
    View.OnClickListener onClose = new View.OnClickListener() {
        public void onClick(View paramView) {
            SearchActivity.this.activities.setVisibility(8);
        }
    };
    private List<Promotion> proList;
    private SearchScrollView scroll;
    private UMSearchBar searchBar;
    private boolean showing = false;
    private Timer timer = new Timer();

    static {
        Object[] localObject = new String[4];
        localObject[0] = "http://www.dld.com/KC/WAP/mdl/mdl.html";
        localObject[1] = "http://www.dld.com/KC/WAP/yonghe/yh.html";
        localObject[2] = "http://www.dld.com/KC/WAP/zgf/zgf.html";
        localObject[3] = "http://www.dld.com/KC/WAP/hhg/hhg.html";
        ADS = (String[]) localObject;
        localObject = new CategoryImage[8];
        localObject[0] = new CategoryImage(R.drawable.cat_1, "餐饮美食", 1);
        localObject[1] = new CategoryImage(R.drawable.cat_2, "休闲娱乐", 2);
        localObject[2] = new CategoryImage(R.drawable.cat_3, "丽人", 3);
        localObject[3] = new CategoryImage(R.drawable.cat_4, "购物", 4);
        localObject[4] = new CategoryImage(R.drawable.cat_5, "摄影写真", 5);
        localObject[5] = new CategoryImage(R.drawable.cat_6, "旅游酒店", 6);
        localObject[6] = new CategoryImage(R.drawable.cat_7, "生活服务", 7);
        localObject[7] = new CategoryImage(R.drawable.cat_8, "其它", 0);
        CATEGORIES = (CategoryImage[]) localObject;
    }

    private void getHuodong() {
        Param localParam = new Param();
        localParam.append("ct", "0").append("n", "10");
        ProtocolHelper.getHuoDongRequest(ActivityManager.getCurrent(),
                localParam, false).startTrans(new HuoDongProtocolResult());
    }

    private void init() {
        this.cityName = SharePersistent.getInstance().getPerference(this,
                "city_name");
        this.searchBar = ((UMSearchBar) findViewById(R.id.search_bar));
        this.searchBar.setOnSearchListener(new UMSearchBar.OnSearchListener() {
            public void onSearch(int paramInt, String paramString) {
                Intent localIntent = new Intent();
                localIntent.setClass(SearchActivity.this,
                        SearchResultActivity.class);
                localIntent.putExtra("keyword", paramString);
                localIntent.putExtra("type",
                        SearchActivity.this.searchBar.getPosition());
                SearchActivity.this.startActivity(localIntent);
            }
        });
        initHotRange();
        initCategory();
        CityActivity.registObserver(new CityActivity.NotifyChangeObserver() {
            public void onChangeCity(String paramString1, String paramString2) {
                SearchActivity.this.cityName = paramString1;
                SearchActivity.this.initHotRange();
                if (SearchActivity.this.showing)
                    ;
                try {
                    ((TextView) SearchActivity.this
                            .findViewById(R.id.title_text)).setText("首页-"
                            + SearchActivity.this.cityName);
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", "", localException);
                }
            }
        });
        initAds();
        initActivities();
        ((TextView) findViewById(R.id.title_text)).setText("首页-"
                + MapUtil.getCity());
        ((Button) findViewById(R.id.invite_top_btn))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        if (!StringUtils.isEmpty(SharePersistent
                                .get("customer_id")))
                            Common.startActivity(InviteActivity.class);
                        else
                            Common.startActivity(LoginActivity.class);
                    }
                });
    }

    private void initActivities() {
        this.activityCount = 0;
        this.activities = ((DragViewGroup) findViewById(R.id.activities));
        this.scroll.setTopChild(this.activities);
    }

    private void initAds() {
        findViewById(R.id.activity_1).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.openBrowser(SearchActivity.ADS[0], true);
                    }
                });
        findViewById(R.id.activity_2).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.openBrowser(SearchActivity.ADS[1], true);
                    }
                });
        findViewById(R.id.activity_3).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.openBrowser(SearchActivity.ADS[2], true);
                    }
                });
        findViewById(R.id.activity_4).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.openBrowser(SearchActivity.ADS[3], true);
                    }
                });
    }

    private void initCategory() {
        LinearLayout localLinearLayout3 = (LinearLayout) findViewById(R.id.categories);
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService("layout_inflater");
        int j = (int) (0.98D * (ActivityManager.getCurrent().getWindowManager()
                .getDefaultDisplay().getWidth() / 4));
        int m = 0;
        int k = CATEGORIES.length / 4;
        while (m < k) {
            LinearLayout localLinearLayout2 = (LinearLayout) localLayoutInflater
                    .inflate(R.layout.search_linear, null);
            for (int i = 0; i < 4; i++) {
                CategoryImage localCategoryImage = CATEGORIES[(i + m * 4)];
                LinearLayout localLinearLayout1 = (LinearLayout) localLayoutInflater
                        .inflate(R.layout.search_category, null);
                ((ImageView) localLinearLayout1.findViewById(R.id.image))
                        .setImageResource(localCategoryImage.imageId);
                ((TextView) localLinearLayout1.findViewById(R.id.text))
                        .setText(localCategoryImage.text);
                setCategory(localLinearLayout1, localCategoryImage);
                localLinearLayout2.addView(localLinearLayout1,
                        new LinearLayout.LayoutParams(j, j));
            }
            localLinearLayout3.addView(localLinearLayout2);
            m++;
        }
    }

    private void initHotRange() {
    	
        List localList = GenericDAO.getInstance(ActivityManager.getCurrent())
                       .getAddresses(this.cityName, "热门商圈");
                      
        localList.remove(0);
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService("layout_inflater");
        LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.hot_addresses);
        localLinearLayout.removeAllViews();
        if (!localList.isEmpty()) {
            findViewById(R.id.no_result).setVisibility(8);
            int j = 0;
            int i = -1 + localList.size();
            while (j < 6) {
                View localView = localLayoutInflater.inflate(
                        R.layout.search_hot_address, null);
                TextView localTextView = (TextView) localView
                        .findViewById(R.id.text_left);
                if (j > i)
                    break;
                setHotAddress(localTextView, (String) localList.get(j));
                localTextView = (TextView) localView
                        .findViewById(R.id.text_right);
                if ((j != 4) || (i <= 5)) {
                    if (i > j)
                        setHotAddress(localTextView,
                                (String) localList.get(j + 1));
                } else {
                    localTextView.setText(Html.fromHtml("<u>更多>></u>"));
                    localTextView.setOnTouchListener(new OnTouchListenerImpl(
                            localTextView));
                    localTextView
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View paramView) {
                                    Activity localActivity = ActivityManager
                                            .getCurrent();
                                    Intent localIntent = new Intent();
                                    localIntent.putExtra("keyword",
                                            SearchActivity.this.searchBar
                                                    .getKeyword());
                                    localIntent.putExtra("type",
                                            SearchActivity.this.searchBar
                                                    .getPosition());
                                    localIntent.setClass(localActivity,
                                            ChooseAddressActivity.class);
                                    localActivity.startActivity(localIntent);
                                }
                            });
                }
                localLinearLayout.addView(localView);
                j += 2;
            }
        }else {
        	findViewById(R.id.no_result).setVisibility(0);
		}
    }

    private void initHuoUrl() {
        if (this.proList != null) {
            int i = 0;
            do
                try {
                    i = -1 + this.proList.size();
                    // continue;
                    Promotion localPromotion = (Promotion) this.proList.get(i);
                    if (StringUtils.equals(localPromotion.type, "1"))
                        initMyActivity(localPromotion.content,
                                localPromotion.image);
                    i--;
                } catch (Exception localException) {
                    LogUtils.log("test", "exception in huodong");
                    break;
                }
            while (i >= 0);
        }
    }

    private void initMyActivity(final String paramString1,
            final String paramString2) {
        LogUtils.log("test", paramString1);
        final View localView = ((LayoutInflater) getSystemService("layout_inflater"))
                .inflate(R.layout.activity_img, null).findViewById(
                        R.id.activity);
        localView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                SearchActivity.this.forward(paramString1, false);
            }
        });
        localView.findViewById(R.id.close).setOnClickListener(this.onClose);
        new Thread() {
            public void run() {
                try {
                    Object localObject = FileUtil
                            .getByteFromInputStream(HttpUtil
                                    .getInputStreamByUrl(paramString2,
                                            SearchActivity.this));
                    FileUtil.writehuodongCache(new ByteArrayInputStream(
                            (byte[]) localObject), MD5Encoder
                            .toHexString(paramString2.getBytes()));
                    final Bitmap bitmap = FileUtil
                            .getBitmapFromInputStream(new ByteArrayInputStream(
                                    (byte[]) localObject));
                    // ((byte[])null);
                    if (localObject != null)
                        SearchActivity.this.handler.post(new Runnable() {
                            public void run() {
                                Object localObject1 = new BitmapDrawable(
                                        SearchActivity.this.getResources(),
                                        (Bitmap) bitmap);
                                localView
                                        .setBackgroundDrawable((Drawable) localObject1);
                                SearchActivity.this.activities.setVisibility(0);
                                SearchActivity.this.activities
                                        .addView(localView);
                                localObject1 = SearchActivity.this;
                                ((SearchActivity) localObject1).activityCount = (1 + ((SearchActivity) localObject1).activityCount);
                            }
                        });
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", localException);
                }
            }
        }.start();
    }

    private void initTimer() {
        this.timer.schedule(new TimerTask() {
            public void run() {
                if (SearchActivity.this.showing)
                    SearchActivity.this.handler.post(new Runnable() {
                        public void run() {
                            long l = System.currentTimeMillis();
                            if ((SearchActivity.this.ads.rest())
                                    && (l
                                            - SearchActivity.this.ads
                                                    .getLastSnapped() >= 8000L)) {
                                int j = SearchActivity.this.ads.getFocusIndex();
                                if (j < 1)
                                    SearchActivity.this.ads.snapToScreen(j + 1);
                                else
                                    SearchActivity.this.ads.reset();
                            }
                            if ((SearchActivity.this.activityCount > 0)
                                    && (SearchActivity.this.activities.rest())
                                    && (l
                                            - SearchActivity.this.activities
                                                    .getLastSnapped() >= 8000L)) {
                                int i = SearchActivity.this.activities
                                        .getFocusIndex();
                                if (i < -1 + SearchActivity.this.activityCount)
                                    SearchActivity.this.activities
                                            .snapToScreen(i + 1);
                                else
                                    SearchActivity.this.activities.reset();
                            }
                        }
                    });
            }
        }, 10000L, 10000L);
    }

    private void setCategory(View paramView,
            final CategoryImage paramCategoryImage) {
        paramView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Activity localActivity = ActivityManager.getCurrent();
                int i = SearchActivity.this.searchBar.getPosition();
                Intent localIntent = new Intent();
                Object localObject;
                if (paramCategoryImage.value != 0)
                    localObject = CategoryActivity.class;
                else
                    localObject = SearchResultActivity.class;
                localIntent.setClass(localActivity, (Class) localObject);
                localIntent.putExtra("type", i);
                localIntent.putExtra("cat", paramCategoryImage.value);
                localIntent.putExtra("cat_name", paramCategoryImage.text);
                localActivity.startActivity(localIntent);
            }
        });
        paramView.setOnTouchListener(new OnTouchListenerImpl(paramView,
                R.drawable.category_selected));
    }

    private void setHotAddress(TextView paramTextView, String paramString) {
        paramTextView.setText(Html.fromHtml("<u>" + paramString + "</u>"));
        paramTextView
                .setOnTouchListener(new OnTouchListenerImpl(paramTextView));
        paramTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Activity localActivity = ActivityManager.getCurrent();
                Intent localIntent = new Intent();
                localIntent.setClass(localActivity, SearchResultActivity.class);
                localIntent.putExtra("keyword",
                        SearchActivity.this.searchBar.getKeyword());
                localIntent.putExtra("type",
                        SearchActivity.this.searchBar.getPosition());
                localIntent.putExtra("address", SearchActivity.this.cityName);
                localActivity.startActivity(localIntent);
            }
        });
    }

    public void forward(String paramString, boolean paramBoolean) {
        Intent localIntent = new Intent();
        localIntent.setClass(this, Browsers.class);
        localIntent.putExtra("url", paramString);
        localIntent.putExtra("backable", paramBoolean);
        startActivity(localIntent);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.search_view);
        this.showing = true;
        this.scroll = ((SearchScrollView) findViewById(R.id.scroll));
        this.ads = ((DragViewGroup) findViewById(R.id.ads));
        this.scroll.setBottomChild(this.ads);
        init();
        new Thread(new Runnable() {
            public void run() {
                SearchActivity.this.getHuodong();
            }
        }).start();
        initTimer();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.showing = false;
        this.timer.cancel();
        this.timer = null;
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return false;
    }

    protected void onPause() {
        super.onPause();
        this.showing = false;
    }

    protected void onResume() {
        super.onResume();
        this.showing = true;
    }

    static final class CategoryImage {
        int imageId;
        String text;
        int value;

        public CategoryImage(int paramInt1, String paramString, int paramInt2) {
            this.imageId = paramInt1;
            this.text = paramString;
            this.value = paramInt2;
        }
    }

    private class HuoDongProtocolResult extends Protocol.OnJsonProtocolResult {
        public HuoDongProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.log("main", "getHuodong exeception");
        }

        public void onResult(String paramString, Object paramObject) {
            HuoDongResponse localHuoDongResponse = (HuoDongResponse) paramObject;
            if ((localHuoDongResponse != null)
                    && (localHuoDongResponse.code == 0)) {
                SearchActivity.this.proList = localHuoDongResponse.promList;
                SearchActivity.this.handler.sendEmptyMessage(5);
            }
        }
    }
}
