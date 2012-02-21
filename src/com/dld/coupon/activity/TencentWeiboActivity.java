package com.dld.coupon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dld.android.util.Device;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.image.ImageProtocol;
import com.dld.coupon.activity.R;
import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Cache;
import com.tencent.weibo.utils.OAuthClient;
import com.tencent.weibo.utils.TokenStore;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

public class TencentWeiboActivity extends BaseActivity {
    private static OAuthClient auth;
    private static OAuth oauth;
    private String content;
    private EditText contentText;
    private static Handler handler = new Handler() {
        private String getContent(Message paramMessage) {
            String str;
            switch (paramMessage.what) {
            default:
                str = null;
                break;
            case 1:
                str = "您提交的内容已经成功分享到微博。";
                break;
            case 2:
                str = "发送失败，请您稍后再试";
                break;
            case 3:
                str = "已经登录到腾讯微博。";
                break;
            case 4:
                str = "登录腾讯微博失败。";
            }
            return str;
        }

        public void handleMessage(Message paramMessage) {
            dismissProgressDailog();
            Toast.makeText(ActivityManager.getCurrent(),
                    getContent(paramMessage), 0).show();
            if (paramMessage.what == 1)
                ActivityManager.getCurrent().finish();
        }
    };
    private String image;
    private static Dialog progressDialog;
    private Integer ticketId;

    private static void dismissProgressDailog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private static void doLogin(boolean paramBoolean) throws Exception {
        if (paramBoolean)
            TokenStore.clear(ActivityManager.getCurrent());
        final Activity localActivity = ActivityManager.getCurrent();
        final ProgressDialog localProgressDialog = DialogHelper
                .getProgressBar("正在打开腾讯微博登录界面，请稍候...");
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
                TencentWeiboActivity.oauth = new OAuth(
                        "doujiao://TencentWeiboActivity");
                TencentWeiboActivity.auth = new OAuthClient();
                try {
                    TencentWeiboActivity.oauth = TencentWeiboActivity.auth
                            .requestToken(TencentWeiboActivity.oauth);
                    if (TencentWeiboActivity.oauth.getStatus() == 1) {
                        error();
                        return;
                    }
                } catch (Exception localException) {
                    // while (true)
                    {
                        LogUtils.e("test", "", localException);
                        error();
                        // continue;
                        String str = TencentWeiboActivity.oauth
                                .getOauth_token();
                        str = "http://open.t.qq.com/cgi-bin/authorize?oauth_token="
                                + str;
                        Intent localIntent = new Intent();
                        localIntent.setAction("android.intent.action.VIEW");
                        localIntent.setData(Uri.parse(str));
                        localIntent.setClassName("com.android.browser",
                                "com.android.browser.BrowserActivity");
                        localActivity.startActivity(localIntent);
                        handler.post(new Runnable() {
                            public void run() {
                                localProgressDialog.dismiss();
                                localActivity.finish();
                            }
                        });
                    }
                }
            }
        }.start();
    }

    private void getToken(String paramString1, String paramString2) {
        oauth.setOauth_verifier(paramString1);
        oauth.setOauth_token(paramString2);
        dismissProgressDailog();
        this.progressDialog = DialogHelper
                .getProgressBar("正在验证腾讯微博登录信息，请稍候...");
        new Thread(new Runnable() {
            public void run() {
                try {
                    TencentWeiboActivity.oauth = TencentWeiboActivity.auth
                            .accessToken(TencentWeiboActivity.oauth);
                    if (TencentWeiboActivity.oauth.getStatus() == 2) {
                        TencentWeiboActivity.this.handler.sendEmptyMessage(4);
                        return;
                    }
                } catch (Exception localException) {
                    // while (true)
                    {
                        TencentWeiboActivity.this.handler.sendEmptyMessage(4);
                        // continue;
                        // LogUtils.log("test", "OAuthActivity Oauth_token : " +
                        // TencentWeiboActivity.oauth.getOauth_token());
                        LogUtils.log(
                                "test",
                                "OAuthActivity Oauth_token_secret : "
                                        + TencentWeiboActivity.oauth
                                                .getOauth_token_secret());
                        TokenStore.store(ActivityManager.getCurrent(),
                                TencentWeiboActivity.oauth);
                        TencentWeiboActivity.this.handler.sendEmptyMessage(3);
                    }
                }
            }
        }).start();
    }

    private void initButton() {
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Cache.put("weibo_ticket_id", TencentWeiboActivity.this.ticketId);
                Cache.put("weibo_group_id", TencentWeiboActivity.this.image);
                Cache.put("weibo_content", TencentWeiboActivity.this.content);
                TencentWeiboActivity.initLogin(true);
            }
        });
        findViewById(R.id.submit).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        String str = TencentWeiboActivity.this.contentText
                                .getText().toString() + " 来自@doujiaohui001";
                        if (!StringUtils.isEmpty(str)) {
                            TencentWeiboActivity.this.dismissProgressDailog();
                            TencentWeiboActivity.this.progressDialog = DialogHelper
                                    .getProgressBar("正在发送微博信息，请稍候...");
                            TencentWeiboActivity.this.progressDialog.show();
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Object localObject = null;
                                        if ((TencentWeiboActivity.this.ticketId == null)
                                                && (TencentWeiboActivity.this.image == null))
                                            localObject = new T_API()
                                                    .add(TencentWeiboActivity.oauth,
                                                            "json",
                                                            content,
                                                            TencentWeiboActivity.this
                                                                    .getLocalIpAddress());
                                        while (true) {
                                            LogUtils.log("test", localObject);
                                            if (((String) localObject)
                                                    .contains("\"ok\"")) {
                                                TencentWeiboActivity.this.handler
                                                        .sendEmptyMessage(1);
                                                // break;
                                                new StringBuilder(
                                                        "http://www.dld.com/vlife/image?id=")
                                                        .append(TencentWeiboActivity.this.ticketId)
                                                        .toString();
                                                if (TencentWeiboActivity.this.ticketId != null)
                                                    ;
                                                for (localObject = "http://www.dld.com/vlife/image?id="
                                                        + TencentWeiboActivity.this.ticketId;; localObject = TencentWeiboActivity.this.image) {
                                                    localObject = (byte[]) Cache.getCache("http://www.dld.com/tuan/viewimage?u="
                                                            + URLEncoder
                                                                    .encode((String) localObject)
                                                            + "&w=400&h=400");
                                                    if (localObject != null)
                                                        // break label236;
                                                        localObject = new T_API()
                                                                .add(TencentWeiboActivity.oauth,
                                                                        "json",
                                                                        content,
                                                                        TencentWeiboActivity.this
                                                                                .getLocalIpAddress());
                                                    break;
                                                }
                                                label236: FileUtil.saveFile(
                                                        "temp",
                                                        (byte[]) localObject,
                                                        true);
                                                localObject = new File(
                                                        Environment
                                                                .getExternalStorageDirectory(),
                                                        "doujiao/temp")
                                                        .getPath();
                                                localObject = new T_API()
                                                        .add_pic(
                                                                TencentWeiboActivity.oauth,
                                                                "json",
                                                                content,
                                                                TencentWeiboActivity.this
                                                                        .getLocalIpAddress(),
                                                                (String) localObject);
                                                continue;
                                            } else {
                                                TencentWeiboActivity.this.handler
                                                        .sendEmptyMessage(2);
                                            }
                                        }
                                    } catch (Exception localException) {
                                        TencentWeiboActivity.this.handler
                                                .sendEmptyMessage(2);
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "请输入您要发布到微博的信息", 0).show();
                        }
                    }
                });
    }

    public static void initLogin(boolean paramBoolean) {
        final Activity localActivity = ActivityManager.getCurrent();
        String str1 = Device.getInstance().getNetType(localActivity);
        if (StringUtils.isEmpty(str1))
            Toast.makeText(localActivity, "网络异常，请稍后重试。", 0).show();
        // while (true)
        {
            // return;
            if (str1.equalsIgnoreCase("cmwap")) {
                new AlertDialog.Builder(localActivity)
                        .setTitle("网络类型提示")
                        .setMessage("使用腾讯微博登录需要切换到中国移动 NET 网络，是否立即切换网络类型？")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface paramDialogInterface,
                                            int paramInt) {
                                        localActivity
                                                .startActivity(new Intent(
                                                        "android.settings.APN_SETTINGS"));
                                    }
                                }).setNegativeButton("否", null).show();
                // continue;
            }
            if (paramBoolean)
                try {
                    doLogin(paramBoolean);
                } catch (Exception localException1) {
                    LogUtils.e("test", "", localException1);
                }
            Object[] localObject = TokenStore.fetch(ActivityManager
                    .getCurrent());
            String str2 = (String) localObject[0];
            // localObject = localObject[1];
            if ((str2 == null) || (localObject == null))
                try {
                    doLogin(paramBoolean);
                } catch (Exception localException2) {
                    LogUtils.e("test", "", localException2);
                }
            Intent localIntent = new Intent();
            localIntent.putExtra("oauth_token", str2);
            localIntent.putExtra("oauth_token_secret", (String) localObject[1]);
            localIntent.setClass(localActivity, TencentWeiboActivity.class);
            localActivity.startActivity(localIntent);
        }
    }

    private void initToken() {
        Object localObject = getIntent();
        if (((Intent) localObject).hasExtra("oauth_token")) {
            String str = ((Intent) localObject).getStringExtra("oauth_token");
            localObject = ((Intent) localObject)
                    .getStringExtra("oauth_token_secret");
            LogUtils.log("test", "oauth_token:" + str);
            LogUtils.log("test", "oauth_token_secret:" + (String) localObject);
            oauth = new OAuth("doujiao://TencentWeiboActivity");
            oauth.setOauth_token(str);
            oauth.setOauth_token_secret((String) localObject);
        }
    }

    private void initUI() {
        setContentView(R.layout.tencent_weibo);
        this.contentText = ((EditText) findViewById(R.id.content));
        this.ticketId = ((Integer) Cache.remove("weibo_ticket_id"));
        this.image = ((String) Cache.remove("weibo_group_id"));
        this.content = ((String) Cache.remove("weibo_content"));
        this.contentText.setText(this.content);
        if (this.ticketId == null) {
            if (this.image != null)
                loadImage(this.image);
        } else
            loadImage("http://www.dld.com/vlife/image?id=" + this.ticketId);
        initButton();
    }

    private void loadImage(String paramString) {
        ImageView localImageView = (ImageView) findViewById(R.id.image);
        localImageView.setVisibility(0);
        String str = URLEncoder.encode(paramString);
        new ImageProtocol(this, "http://www.dld.com/tuan/viewimage?u=" + str
                + "&w=400&h=400").startTrans(localImageView);
    }

    public String getLocalIpAddress() {
        Object localObject2;
        try {
            while (true) {
                Enumeration localEnumeration = NetworkInterface
                        .getNetworkInterfaces();
                while (true)
                    if (localEnumeration.hasMoreElements()) {
                        Object localObject1 = ((NetworkInterface) localEnumeration
                                .nextElement()).getInetAddresses();
                        if (!((Enumeration) localObject1).hasMoreElements())
                            continue;
                        InetAddress localInetAddress = (InetAddress) ((Enumeration) localObject1)
                                .nextElement();
                        if (localInetAddress.isLoopbackAddress())
                            break;
                        localObject1 = localInetAddress.getHostAddress()
                                .toString();
                        localObject1 = localObject1;
                    }
            }
        } catch (SocketException local2) {
            LogUtils.e("test", local2);
            localObject2 = null;
        }
        return (String) localObject2;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initUI();
        initToken();
    }

    public void onResume() {
        super.onResume();
        Uri localUri = getIntent().getData();
        if (localUri != null)
            getToken(localUri.getQueryParameter("oauth_verifier"),
                    localUri.getQueryParameter("oauth_token"));
    }
}
