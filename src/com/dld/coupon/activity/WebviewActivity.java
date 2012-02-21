package com.dld.coupon.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.GroupDetail;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.net.URLEncoder;

public class WebviewActivity extends BaseActivity {
    private Boolean inFav;
    ProgressDialog pd;

    private void gotoOrderActivity(final GroupDetail paramGroupDetail) {
        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent;
                if (!StringUtils.isEmpty(SharePersistent.getInstance()
                        .getPerference(ActivityManager.getCurrent(),
                                "customer_id"))) {
                    LogUtils.log("GroupDetailView", paramGroupDetail.id);
                    Cache.put("website", paramGroupDetail.website);
                    Cache.put("amount", Double.valueOf(paramGroupDetail.price));
                    localIntent = new Intent();
                    localIntent.putExtra("groupdetail", paramGroupDetail);
                    localIntent.setClass(ActivityManager.getCurrent(),
                            OrderInfoActivity.class);
                    ActivityManager.getCurrent().startActivity(localIntent);
                } else {
                    localIntent = new Intent(ActivityManager.getCurrent(),
                            LoginActivity.class);
                    ActivityManager.getCurrent().startActivityForResult(
                            localIntent, 0);
                }
            }
        });
    }

    private void initBuy(GroupDetail paramGroupDetail) {
        if (paramGroupDetail.id <= 0)
            findViewById(R.id.buy).setVisibility(View.GONE);
        else
            gotoOrderActivity(paramGroupDetail);
    }

    private void initImage(GroupDetail paramGroupDetail) {
        ImageView localImageView = (ImageView) findViewById(R.id.image);
        String str = paramGroupDetail.image;
        if (str != null)
            if (!this.inFav.booleanValue())
                new ImageProtocol(ActivityManager.getCurrent(),
                        "http://www.dld.com/tuan/viewimage?u="
                                + URLEncoder.encode(str) + "&w=400&h=400",
                        paramGroupDetail.dealUrl).startTrans(localImageView);
            else
                FileUtil.readImage(localImageView, str, 400, 400);
    }

    private void initWebView(GroupDetail paramGroupDetail) {
        String str = "http://www.dld.com/tc/?fs=14&psize=500&u="
                + URLEncoder.encode(paramGroupDetail.dealUrl);
        WebView localWebView = (WebView) findViewById(R.id.webview);
        localWebView.getSettings().setJavaScriptEnabled(true);
        localWebView.setHorizontalScrollBarEnabled(false);
        localWebView.setVerticalScrollBarEnabled(false);
        WebView.enablePlatformNotifications();
        localWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView paramWebView, int paramInt) {
                if (paramInt == 100)
                    WebviewActivity.this.pd.dismiss();
            }
        });
        localWebView.loadUrl(str);
        localWebView.setBackgroundColor(0);
    }

    private void updateTitle(GroupDetail paramGroupDetail) {
        ((TextView) findViewById(R.id.title_text)).setText("团购详细信息");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        GroupDetail localGroupDetail = (GroupDetail) Cache.remove("detail");
        this.inFav = ((Boolean) Cache.remove("inFav"));
        this.pd = DialogHelper.getProgressBar("团购详情加载中，请稍候...");
        setTheme(16973836);
        setContentView(R.layout.webview);
        initSegment();
        initWebView(localGroupDetail);
        initBuy(localGroupDetail);
        initImage(localGroupDetail);
        updateTitle(localGroupDetail);
    }
}
