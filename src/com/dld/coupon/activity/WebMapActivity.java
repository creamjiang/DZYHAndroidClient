package com.dld.coupon.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dld.android.util.LogUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.coupon.activity.R;

public class WebMapActivity extends BaseActivity {
    private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
    private String address = null;
    private boolean backable = false;
    private ProgressDialog pd;
    private TextView routeTextView = null;
    private WebView webView;

    private void initWidget() {
        this.routeTextView = ((TextView) findViewById(R.id.route));
        this.webView = ((WebView) findViewById(R.id.webview));
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView paramWebView, int paramInt) {
                if (paramInt == 100)
                    ;
                try {
                    if (WebMapActivity.this.pd != null) {
                        WebMapActivity.this.pd.dismiss();
                        WebMapActivity.this.pd = null;
                    }
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", localException);
                }
            }
        });
    }

    private void resetProgressDialog() {
        this.pd = DialogHelper.getProgressBar("地图加载中，请稍候...");
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
                                WebMapActivity.this.pd.dismiss();
                                WebMapActivity.this.pd = null;
                                WebMapActivity.this.finish();
                                return i == 0 ? false : true;
                            }
                        } catch (Exception localException) {
                            LogUtils.e("test", localException);
                        }
                        int j = 0;
                    }
                }
            });
    }

    private void setupWebView(double paramDouble1, double paramDouble2) {
        String str = "javascript:centerAt("
                + paramDouble1
                + ","
                + paramDouble2
                + ");"
                + "map.setZoom(12);"
                + "var myLatlng = new google.maps.LatLng("
                + paramDouble1
                + ","
                + paramDouble2
                + ");"
                + "var marker = new google.maps.Marker({position: myLatlng,title:'Hello World!'});"
                + "marker.setMap(map); var infowindow = new google.maps.InfoWindow({ content:'<div style=\"width:180px\">"
                + this.address + "</div>'});" + "infowindow.open(map,marker);";
        this.webView = ((WebView) findViewById(R.id.webview));
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView paramWebView, String paramString) {
                // WebMapActivity.this.webView.loadUrl(this.val$locationUrl);
            }
        });
        this.webView
                .loadUrl("http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.map_browser);
        initWidget();
        initSegment();
        resetProgressDialog();
        Object localObject = getIntent().getBundleExtra("mapbundle");
        double d2 = ((Bundle) localObject).getDouble("latitude");
        double d1 = ((Bundle) localObject).getDouble("longitude");
        this.address = ((Bundle) localObject).getString("address");
        LogUtils.log("main", "address==" + this.address);
        localObject = ((Bundle) localObject).getString("route");
        this.routeTextView.setText((CharSequence) localObject);
        setupWebView(d2, d1);
    }
}
