package com.dld.coupon.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dld.android.util.LogUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.coupon.activity.R;

public class Browsers extends BaseActivity {
    private boolean backable;
    private ProgressDialog pd;
    private String url;
    private WebView webview;

    private void initWebView(String paramString) {
        this.webview = ((WebView) findViewById(R.id.webview));
        this.webview.getSettings().setJavaScriptEnabled(true);
        WebView.enablePlatformNotifications();
        this.webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView paramWebView, int paramInt) {
                if (paramInt == 100)
                    ;
                try {
                    if (Browsers.this.pd != null) {
                        Browsers.this.pd.dismiss();
                        Browsers.this.pd = null;
                    }
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", localException);
                }
            }
        });
        this.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView paramWebView,
                    String paramString) {
                try {
                    if (Browsers.this.pd != null)
                        Browsers.this.pd.dismiss();
                    Browsers.this.resetProgressDialog();
                    paramWebView.loadUrl(paramString);
                    return false;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", localException);
                }
            }
        });
        this.webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramString1,
                    String paramString2, String paramString3,
                    String paramString4, long paramLong) {
                Intent localIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse(paramString1));
                Browsers.this.startActivity(localIntent);
            }
        });
        this.webview.setBackgroundColor(0);
        WebSettings localWebSettings = this.webview.getSettings();
        localWebSettings.setLoadWithOverviewMode(true);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setBuiltInZoomControls(true);
        this.webview.getZoomControls().setVisibility(8);
        this.webview.loadUrl(paramString);
    }

    private void resetProgressDialog() {
        this.pd = DialogHelper.getProgressBar("网页加载中，请稍候...");
        if (!this.backable)
            this.pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface paramDialogInterface,
                        int paramInt, KeyEvent paramKeyEvent) {
                    int i = 1;
                    if (paramInt == 4)
                        ;
                    while (true) {
                        try {
                            if (paramKeyEvent.getAction() == i) {
                                Browsers.this.pd.dismiss();
                                Browsers.this.pd = null;
                                Browsers.this.finish();
                                return true;
                            }
                        } catch (Exception localException) {
                            LogUtils.e("test", localException);
                        }
                        int j = 0;
                    }
                }
            });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        resetProgressDialog();
        setContentView(R.layout.browser);
        Intent localIntent = getIntent();
        this.url = localIntent.getStringExtra("url");
        this.backable = localIntent.getBooleanExtra("backable", false);
        initSegment();
        if (this.url == null)
            finish();
        else
            initWebView(this.url);
    }

    public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
        boolean bool;
        if ((this.backable) && (this.webview.canGoBack())) {
            this.webview.goBack();
            bool = true;
        } else {
            bool = super.onKeyUp(paramInt, paramKeyEvent);
        }
        return bool;
    }
}
