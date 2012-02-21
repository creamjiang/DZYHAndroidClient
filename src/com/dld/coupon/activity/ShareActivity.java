package com.dld.coupon.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.image.ImageProtocol;
import com.sina.AccessToken;
import com.sina.AsyncWeiboRunner;
import com.sina.AsyncWeiboRunner.RequestListener;
import com.sina.RequestToken;
import com.sina.Weibo;
import com.sina.WeiboException;
import com.sina.WeiboParameters;
import com.tencent.weibo.utils.Cache;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class ShareActivity extends BaseActivity implements
        View.OnClickListener, AsyncWeiboRunner.RequestListener {
    public static final String CONSUMER_KEY = "3514322953";
    public static final String CONSUMER_SECRET = "b962e4ae4b6ecdae3e62239d69585b39";
    public static final String EXTRA_ACCESS_TOKEN = "com.weibo.android.accesstoken";
    public static final String EXTRA_PIC_URI = "com.weibo.android.pic.uri";
    public static final String EXTRA_TOKEN_SECRET = "com.weibo.android.token.secret";
    public static final String EXTRA_WEIBO_CONTENT = "com.weibo.android.content";
    public static final String GET_USERINFO = "http://api.t.sina.com.cn/account/verify_credentials.json";
    private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
    public static final int WEIBO_MAX_LENGTH = 140;
    private String content;
    private EditText contentEditText = null;
    Dialog d;
    private static Handler handler = new Handler();
    private String image;
    private String mAccessToken = "";
    private String mTokenSecret = "";
    private Weibo mWeibo = null;
    private Button submitButton = null;
    private Integer ticketId;

    public static void doLogin() {
        final Activity localActivity = ActivityManager.getCurrent();
        final ProgressDialog localProgressDialog = DialogHelper
                .getProgressBarNoShow("正在打开新浪微博登录界面，请稍候...");
        new Thread() {
            private void error() {
                handler.post(new Runnable() {
                    public void run() {
                        localProgressDialog.dismiss();
                        Toast.makeText(localActivity, "打开登录界面失败，请稍后重试。", 0)
                                .show();
                        localActivity.finish();
                    }
                });
            }

            public void run() {
                try {
                    Activity activity = ActivityManager.getCurrent();
                    Object localObject2 = SharePersistent.getInstance()
                            .getAccessToken(activity);
                    Object localObject1 = ((AccessToken) localObject2)
                            .getToken();
                    localObject2 = ((AccessToken) localObject2).getSecret();
                    if ((localObject1 != null) && (localObject2 != null)
                            && (Cache.remove("login") == null)) {
                        Intent localIntent = new Intent(activity,
                                ShareActivity.class);
                        localIntent.putExtra("com.weibo.android.accesstoken",
                                (String) localObject1);
                        localIntent.putExtra("com.weibo.android.token.secret",
                                (String) localObject2);
                        localIntent.putExtra("flag", "doujiao");
                        activity.startActivity(localIntent);
                    }
                    // while (true)
                    {
                        handler.post(new Runnable() {
                            public void run() {
                                localProgressDialog.dismiss();
                            }
                        });
                        // return;
                        handler.post(new Runnable() {
                            public void run() {
                                localProgressDialog.show();
                            }
                        });
                        localObject1 = Weibo.getInstance();
                        ((Weibo) localObject1).setupConsumerConfig(
                                "3514322953",
                                "b962e4ae4b6ecdae3e62239d69585b39");
                        localObject1 = ((Weibo) localObject1).getRequestToken(
                                activity, Weibo.APP_KEY, Weibo.APP_SECRET,
                                "weiboandroidsdk://TimeLineActivity");
                        localObject1 = Weibo.URL_AUTHENTICATION
                                + "?display=wap3.0&oauth_token="
                                + ((RequestToken) localObject1).getToken();
                        localObject2 = Uri.parse((String) localObject1);
                        LogUtils.log("test", localObject1);
                        activity.startActivity(new Intent(
                                "android.intent.action.VIEW",
                                (Uri) localObject2));
                        activity.finish();
                    }
                } catch (Exception localException) {
                    while (true) {
                        LogUtils.e("test", "", localException);
                        error();
                    }
                }
            }
        }.start();
    }

    private void info(final Weibo paramWeibo, final String paramString1,
            final String paramString2, final String paramString3,
            final String paramString4) throws MalformedURLException,
            IOException, WeiboException {
        this.d = DialogHelper.getProgressBar("正在获取用户id，请稍候...");
        new Thread(new Runnable() {
            public void run() {
                WeiboParameters localWeiboParameters = new WeiboParameters();
                localWeiboParameters.add("source", paramString1);
                if (!TextUtils.isEmpty(paramString2))
                    localWeiboParameters.add("lon", paramString2);
                if (!TextUtils.isEmpty(paramString3))
                    localWeiboParameters.add("lat", paramString3);
                new AsyncWeiboRunner(paramWeibo)
                        .request(
                                ShareActivity.this,
                                "http://api.t.sina.com.cn/account/verify_credentials.json",
                                localWeiboParameters, "GET", ShareActivity.this);
            }
        }).start();
    }

    private void initUI() {
        this.ticketId = ((Integer) Cache.remove("weibo_ticket_id"));
        this.content = ((String) Cache.getCache("weibo_content"));
        this.image = ((String) Cache.getCache("weibo_group_id"));
        if (this.ticketId == null) {
            if (this.image != null)
                loadImage(this.image);
        } else
            loadImage("http://www.dld.com/vlife/image?id=" + this.ticketId);
    }

    private void initWidget() {
        this.contentEditText = ((EditText) findViewById(R.id.content));
        this.contentEditText.setText(this.content);
        this.submitButton = ((Button) findViewById(R.id.submit));
        this.submitButton.setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Activity localActivity = ActivityManager.getCurrent();
                SharePersistent.getInstance().clearAccessToken(localActivity);
                Cache.put("weibo_ticket_id", ShareActivity.this.ticketId);
                Cache.put("weibo_group_id", ShareActivity.this.image);
                Cache.put("weibo_content", ShareActivity.this.content);
                Cache.put("login", "true");
                ShareActivity.doLogin();
            }
        });
    }

    private void loadImage(String paramString) {
        ImageView localImageView = (ImageView) findViewById(R.id.image);
        localImageView.setVisibility(0);
        String str = URLEncoder.encode(paramString);
        new ImageProtocol(this, "http://www.dld.com/tuan/viewimage?u=" + str
                + "&w=400&h=400").startTrans(localImageView);
    }

    private void update(final Weibo paramWeibo, final String paramString1,
            final String paramString2, final String paramString3,
            final String paramString4) throws MalformedURLException,
            IOException, WeiboException {
        this.d = DialogHelper.getProgressBar("正在发送微博信息，请稍候...");
        new Thread(new Runnable() {
            public void run() {
                WeiboParameters localWeiboParameters = new WeiboParameters();
                localWeiboParameters.add("source", paramString1);
                localWeiboParameters.add("status", paramString2);
                if (!TextUtils.isEmpty(paramString3))
                    localWeiboParameters.add("lon", paramString3);
                if (!TextUtils.isEmpty(paramString4))
                    localWeiboParameters.add("lat", paramString4);
                String str = Weibo.SERVER + "statuses/update.json";
                new AsyncWeiboRunner(paramWeibo).request(ShareActivity.this,
                        str, localWeiboParameters, "POST", ShareActivity.this);
            }
        }).start();
    }

    private void upload(final Weibo paramWeibo, final String paramString1,
            final String paramString2, final String paramString3,
            final String paramString4, final String paramString5)
            throws WeiboException {
        this.d = DialogHelper.getProgressBar("正在发送微博信息，请稍候...");
        new Thread(new Runnable() {
            public void run() {
                WeiboParameters localWeiboParameters = new WeiboParameters();
                localWeiboParameters.add("source", paramString1);
                localWeiboParameters.add("pic", paramString2);
                localWeiboParameters.add("status", paramString3);
                if (!TextUtils.isEmpty(paramString4))
                    localWeiboParameters.add("lon", paramString4);
                if (!TextUtils.isEmpty(paramString5))
                    localWeiboParameters.add("lat", paramString5);
                String str = Weibo.SERVER + "statuses/upload.json";
                new AsyncWeiboRunner(paramWeibo).request(ShareActivity.this,
                        str, localWeiboParameters, "POST", ShareActivity.this);
            }
        }).start();
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        default:
        case R.id.submit:
        }
        while (true) {
            // return;
            if (StringUtils.isEmpty(this.contentEditText.getText().toString())) {
                Toast.makeText(ActivityManager.getCurrent(), "请输入您要发布到微博的信息", 0)
                        .show();
                continue;
            }
            try {
                if (TextUtils.isEmpty(this.mWeibo.getAccessToken().getToken()))
                    continue;
                if ((this.ticketId != null) || (this.image != null))
                    if (this.ticketId != null) {
                        String str1 = "http://www.dld.com/vlife/image?id="
                                + this.ticketId;
                        str1 = "http://www.dld.com/tuan/viewimage?u="
                                + URLEncoder.encode(str1) + "&w=400&h=400";
                        upload(this.mWeibo, Weibo.APP_KEY, str1,
                                this.contentEditText.getText().toString()
                                        + " @打折店网", "", "");
                    }
            } catch (WeiboException localWeiboException) {
                localWeiboException.printStackTrace();
            }
        }
    }

    public void onComplete(String paramString) {
        LogUtils.log("main", "response;==" + paramString);
        this.handler.post(new Runnable() {
            public void run() {
                ShareActivity.this.d.dismiss();
                Toast.makeText(ShareActivity.this, "发送成功", 1).show();
                ShareActivity.this.finish();
            }
        });
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.tencent_weibo);
        initUI();
        this.mWeibo = Weibo.getInstance();
        initWidget();
        Weibo localWeibo = Weibo.getInstance();
        Intent localIntent = getIntent();
        if ("doujiao".equals(localIntent.getStringExtra("flag"))) {
            this.mAccessToken = localIntent
                    .getStringExtra("com.weibo.android.accesstoken");
            this.mTokenSecret = localIntent
                    .getStringExtra("com.weibo.android.token.secret");
            LogUtils.log("ShareActivity", "mAccessToken=" + this.mAccessToken
                    + "mTokenSecret=" + this.mTokenSecret);
            localWeibo.setAccessToken(new AccessToken(this.mAccessToken,
                    this.mTokenSecret));
        }
        // while (true)
        {
            // return;
            localWeibo.addOauthverifier(getIntent().getData()
                    .getQueryParameter("oauth_verifier"));
            try {
                localWeibo.generateAccessToken(this, null);
                SharePersistent.getInstance().saveAccessToken(this,
                        localWeibo.getAccessToken());
            } catch (WeiboException localWeiboException) {
                localWeiboException.printStackTrace();
            }
        }
    }

    public void onError(WeiboException paramWeiboException) {
        LogUtils.log("ShareActivity", paramWeiboException.toString() + "/");
        this.handler.post(new Runnable() {
            public void run() {
                if (ShareActivity.this.d != null)
                    ShareActivity.this.d.dismiss();
                Toast.makeText(ShareActivity.this, "发送失败", 3).show();
            }
        });
    }

    public void onIOException(IOException paramIOException) {
        this.handler.post(new Runnable() {
            public void run() {
                ShareActivity.this.d.dismiss();
                Toast.makeText(ShareActivity.this, "发送失败", 3).show();
            }
        });
    }
}
