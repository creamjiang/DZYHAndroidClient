package com.dld.coupon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.Device;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.MD5Encoder;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Response;
import com.dld.protocol.json.User;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.sina.AsyncWeiboRunner;
import com.sina.AsyncWeiboRunner.RequestListener;
import com.sina.RequestToken;
import com.sina.Weibo;
import com.sina.WeiboException;
import com.sina.WeiboParameters;
import com.tencent.weibo.api.User_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Cache;
import com.tencent.weibo.utils.OAuthClient;
import com.tencent.weibo.utils.TokenStore;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements
        View.OnClickListener, AsyncWeiboRunner.RequestListener {
    private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://WeiboLoginActivity";
    private static OAuthClient auth;
    public static boolean created = false;
    private static OAuth oauth;
    private final int LOGINEXCEPTION = 4;
    private final int LOGININFO = 1;
    private final int RESETEXCEPTION = 3;
    private final int RESETINFO = 2;
    private final int TECENTWEIBOREGISTER = 8;
    private final int WEIBOREGISTER = 5;
    private final int WEIBOREGISTERFAILURE = 7;
    private final int WEIBORGISTERSUCCESS = 6;
    Dialog dialog;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            Object localObject = null;
            if (paramMessage.what == 1) {
                if ((LoginActivity.this.dialog != null)
                        && (LoginActivity.this.dialog.isShowing()))
                    LoginActivity.this.dialog.dismiss();
                if (LoginActivity.this.response.code != 0)
                    ;
                // break label635;
                LoginActivity.this
                        .cancelDialog(LoginActivity.this.loginWaitDialog);
                localObject = LoginActivity.this.response.user;
                ((User) localObject).username = LoginActivity.this.name;
                ((User) localObject).password = LoginActivity.this.password;
                SharePersistent.getInstance().saveUserInfo(LoginActivity.this,
                        (User) localObject);
                Toast.makeText(LoginActivity.this, "登录成功", 1).show();
                SharePersistent.getInstance().savePerference(
                        ActivityManager.getCurrent(), "customer_id",
                        LoginActivity.this.response.user.id, "weibotype", "0");
                Common.hideKeyboard(LoginActivity.this
                        .findViewById(R.id.login_layout));
                LoginActivity.this.finish();
            }
            // while (true)
            {
                if (paramMessage.what == 2) {
                    LoginActivity.this.cancelWaitResetPswDialog();
                    if (LoginActivity.this.response.code == 0)
                        Toast.makeText(LoginActivity.this, "重置成功", 1).show();
                } else {
                    label218: if (paramMessage.what == 3) {
                        LoginActivity.this.cancelWaitResetPswDialog();
                        Toast.makeText(LoginActivity.this, "重置密码失败，请稍后再试!", 1)
                                .show();
                    }
                    if (paramMessage.what == 4) {
                        LoginActivity.this
                                .cancelDialog(LoginActivity.this.loginWaitDialog);
                        if ((LoginActivity.this.dialog != null)
                                && (LoginActivity.this.dialog.isShowing()))
                            LoginActivity.this.dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败，请稍后再试!", 1)
                                .show();
                    }
                    if (paramMessage.what == 5) {
                        LogUtils.log("main", "doRegisterWeibo");
                        LoginActivity.this.doRegisterofWeibo();
                    }
                    if (paramMessage.what == 6) {
                        if (LoginActivity.this.dialog != null)
                            LoginActivity.this.dialog.dismiss();
                        if (LoginActivity.this.response.code != 0)
                            // break label689;
                            localObject = LoginActivity.this.response.user;
                        ((User) localObject).username = LoginActivity.this.response.user.id;
                        SharePersistent.getInstance().saveUserInfo(
                                LoginActivity.this, (User) localObject);
                        Toast.makeText(LoginActivity.this, "使用微博账号登录打折店成功", 1)
                                .show();
                        SharePersistent.getInstance().savePerference(
                                ActivityManager.getCurrent(), "customer_id",
                                LoginActivity.this.response.user.id,
                                "weibotype", LoginActivity.this.weiboFlag);
                        SharePersistent.getInstance().savePerference(
                                LoginActivity.this, "unicId",
                                LoginActivity.this.weiboId);
                        LoginActivity.this.finish();
                    }
                    if (paramMessage.what == 7) {
                        if (LoginActivity.this.dialog != null)
                            LoginActivity.this.dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "微博登录打折店失败，请稍后再试!",
                                1).show();
                    }
                    if (paramMessage.what != 8)
                        ;
                }
                try {
                    LogUtils.log("main", "info执行");
                    localObject = new User_API().info(LoginActivity.oauth,
                            "json");
                    LogUtils.log("main", localObject + "////");
                    localObject = new JSONObject((String) localObject)
                            .getJSONObject("data");
                    LoginActivity.this.weiboId = ((JSONObject) localObject)
                            .getString("name");
                    LoginActivity.this.weiboName = ((JSONObject) localObject)
                            .getString("nick");
                    LoginActivity.this.doRegisterofWeibo();
                    // return;
                    label635: LoginActivity.this
                            .cancelDialog(LoginActivity.this.loginWaitDialog);
                    DialogHelper
                            .commonDialog(LoginActivity.this.response.message);
                    // continue;
                    Toast.makeText(LoginActivity.this,
                            LoginActivity.this.response.message, 1).show();
                    // break label218;
                    label689: DialogHelper
                            .commonDialog(LoginActivity.this.response.message);
                } catch (Exception localException) {
                    // while (true)
                    {
                        localException.printStackTrace();
                        LoginActivity.this.onError(null);
                        LogUtils.log("main", "json exception");
                    }
                }
            }
        }
    };
    private ProgressDialog loginWaitDialog;
    private String name = null;
    private String password = null;
    private Button resetCancelButton = null;
    private View resetDialogView = null;
    private EditText resetEditText = null;
    private Button resetOkButton = null;
    private AlertDialog resetPswDialog = null;
    private String resetofEemal = null;
    private Response response;
    private View sinaloginLayout;
    private View tencentloginLayout;
    private ProgressDialog waitResetPswDialog;
    private String weiboFlag;
    private String weiboId = null;
    private String weiboName = null;

    private void cancelDialog(ProgressDialog paramProgressDialog) {
        if (paramProgressDialog != null)
            paramProgressDialog.dismiss();
    }

    private void cancelWaitResetPswDialog() {
        if (this.waitResetPswDialog != null)
            this.waitResetPswDialog.dismiss();
    }

    private void doRegisterofWeibo() {
        LogUtils.log("main", "doresister to my server");
        Param localParam = new Param();
        localParam.append("uid", this.weiboId).append("lt", this.weiboFlag)
                .append("uname", this.weiboName).append("ct", "0")
                .append("chn", "-1");
        ProtocolHelper.registerWeiboRequest(this, localParam, false)
                .startTransForUser(new RegisterProtocolResult(), localParam);
    }

    private void doReset() {
        this.resetofEemal = this.resetEditText.getText().toString();
        if (!StringUtils.isEmpty(this.resetofEemal)) {
            if (this.resetofEemal.matches("\\w+@\\w+(\\.\\w+)+")) {
                showWaitResetPswDialog(this);
                Param localParam = new Param();
                localParam.append("email", this.resetofEemal);
                ProtocolHelper.resetPswRequest(this, localParam, false)
                        .startTransForUser(new ResetPswProtocolResult(),
                                localParam);
            } else {
                DialogHelper.commonDialog("邮箱的格式不对");
            }
        } else
            DialogHelper.commonDialog("邮箱不能为空");
    }

    public static void doSinaWeiboLogin() {
        final Activity localActivity = ActivityManager.getCurrent();
        final ProgressDialog localProgressDialog = DialogHelper
                .getProgressBarNoShow("正在打开新浪微博登录界面，请稍候...");
        final Handler localHandler = new Handler();
        LogUtils.log("main", "do sinaexecute");
        new Thread() {
            private void error() {
                localHandler.post(new Runnable() {
                    public void run() {
                        localProgressDialog.dismiss();
                        Toast.makeText(localActivity, "打开登录界面失败，请稍后重试。", 0)
                                .show();
                    }
                });
            }

            public void run() {
                try {
                    localHandler.post(new Runnable() {
                        public void run() {
                            localProgressDialog.show();
                        }
                    });
                    Object localObject = Weibo.getInstance();
                    ((Weibo) localObject).setupConsumerConfig("3514322953",
                            "b962e4ae4b6ecdae3e62239d69585b39");
                    localObject = ((Weibo) localObject).getRequestToken(
                            localActivity, Weibo.APP_KEY, Weibo.APP_SECRET,
                            "weiboandroidsdk://WeiboLoginActivity");
                    localObject = Weibo.URL_AUTHENTICATION
                            + "?display=wap2.0&oauth_token="
                            + ((RequestToken) localObject).getToken();
                    Uri localUri = Uri.parse((String) localObject);
                    LogUtils.log("test", localObject);
                    localActivity.startActivity(new Intent(
                            "android.intent.action.VIEW", localUri));
                    localHandler.post(new Runnable() {
                        public void run() {
                            localProgressDialog.dismiss();
                            localActivity.finish();
                        }
                    });
                    return;
                } catch (Exception localException) {
                    while (true)
                        error();
                }
            }
        }.start();
    }

    private static void doTecentWeiboLogin() {
        final Activity localActivity = ActivityManager.getCurrent();
        final Object localObject = Device.getInstance().getNetType(
                localActivity);
        final ProgressDialog progressDialog;
        final Handler handler = new Handler();
        if (!StringUtils.isEmpty((String) localObject)) {
            if (!((String) localObject).equalsIgnoreCase("cmwap")) {
                progressDialog = DialogHelper
                        .getProgressBar("正在打开腾讯微博登录界面，请稍候...");
                new Thread() {
                    private void error() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(localActivity,
                                            "打开登录界面失败，请稍后重试。", 0).show();
                                    progressDialog.dismiss();
                                    localActivity.finish();
                                    return;
                                } catch (Exception localException) {
                                    // while (true)
                                    localException.printStackTrace();
                                }
                            }
                        });
                    }

                    public void run() {
                        LoginActivity.oauth = new OAuth(
                                "doujiao://TencentWeiboLoginActivity");
                        LoginActivity.auth = new OAuthClient();
                        try {
                            LoginActivity.oauth = LoginActivity.auth
                                    .requestToken(LoginActivity.oauth);
                            if (LoginActivity.oauth.getStatus() == 1) {
                                error();
                                return;
                            }
                        } catch (Exception localObject) {
                            // while (true)
                            {
                                LogUtils.e("test", "", localObject);
                                error();
                                // continue;
                                Object localObject1 = LoginActivity.oauth
                                        .getOauth_token();
                                String str = "http://open.t.qq.com/cgi-bin/authorize?oauth_token="
                                        + (String) localObject1;
                                localObject1 = new Intent();
                                ((Intent) localObject1)
                                        .setAction("android.intent.action.VIEW");
                                ((Intent) localObject1).setData(Uri.parse(str));
                                ((Intent) localObject1).setClassName(
                                        "com.android.browser",
                                        "com.android.browser.BrowserActivity");
                                ActivityManager.getCurrent().startActivity(
                                        (Intent) localObject1);
                                handler.post(new Runnable() {
                                    public void run() {
                                        try {
                                            progressDialog.dismiss();
                                            localActivity.finish();
                                            return;
                                        } catch (Exception localException) {
                                            // while (true)
                                            LogUtils.e("test", localException);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }.start();
            } else {
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
            }
        } else
            Toast.makeText(localActivity, "网络异常，请稍后重试。", 0).show();
    }

    private void getToken(String paramString1, String paramString2) {
        oauth.setOauth_verifier(paramString1);
        oauth.setOauth_token(paramString2);
        new Thread(new Runnable() {
            public void run() {
                try {
                    LoginActivity.oauth = LoginActivity.auth
                            .accessToken(LoginActivity.oauth);
                    if (LoginActivity.oauth.getStatus() == 2) {
                        LoginActivity.this.onError(null);
                        return;
                    }
                } catch (Exception localException) {
                    // while (true)
                    {
                        LoginActivity.this.handler.sendEmptyMessage(4);
                        // continue;
                        LogUtils.log("test", "OAuthActivity Oauth_token : "
                                + LoginActivity.oauth.getOauth_token());
                        LogUtils.log(
                                "test",
                                "OAuthActivity Oauth_token_secret : "
                                        + LoginActivity.oauth
                                                .getOauth_token_secret());
                        TokenStore.store(ActivityManager.getCurrent(),
                                LoginActivity.oauth);
                        LoginActivity.this.handler.sendEmptyMessage(8);
                    }
                }
            }
        }).start();
    }

    private void info(final Weibo paramWeibo, final String source,
            final String lon, final String lat, String paramString4)
            throws MalformedURLException, IOException, WeiboException {
        this.dialog = DialogHelper.getProgressBar("正在使用微博登录，请稍候...");
        new Thread(new Runnable() {
            public void run() {
                try {
                    paramWeibo.generateAccessToken(LoginActivity.this, null);
                    SharePersistent.getInstance().saveAccessToken(
                            LoginActivity.this, paramWeibo.getAccessToken());
                    WeiboParameters localWeiboParameters = new WeiboParameters();
                    localWeiboParameters.add("source", source);
                    if (!TextUtils.isEmpty(lon))
                        localWeiboParameters.add("lon", lon);
                    if (!TextUtils.isEmpty(lat))
                        localWeiboParameters.add("lat", lat);
                    new AsyncWeiboRunner(paramWeibo)
                            .request(
                                    LoginActivity.this,
                                    "http://api.t.sina.com.cn/account/verify_credentials.json",
                                    localWeiboParameters, "GET",
                                    LoginActivity.this);
                    return;
                } catch (WeiboException localWeiboException) {
                    // while (true)
                    localWeiboException.printStackTrace();
                }
            }
        }).start();
    }

    private void initUI() {
        this.sinaloginLayout = findViewById(R.id.sina_loginlayout);
        this.tencentloginLayout = findViewById(R.id.tecentlogin_layout);
        this.sinaloginLayout.setOnClickListener(this);
        this.tencentloginLayout.setOnClickListener(this);
    }

    private void loginRequest() {
        Param localParam = new Param();
        localParam.append("name", this.name).append("password",
                MD5Encoder.toMd5(this.password.getBytes()));
        ProtocolHelper.loginRequest(this, localParam, false).startTransForUser(
                new LoginProtocolResult(), localParam);
    }

    private void showRegisterDialog(Context paramContext) {
        if (this.loginWaitDialog == null) {
            this.loginWaitDialog = new ProgressDialog(paramContext);
            this.loginWaitDialog.setMessage("正在登录,请稍候!");
        }
        this.loginWaitDialog.setButton(
                paramContext.getResources().getString(R.string.label_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        paramDialogInterface.cancel();
                    }
                });
        this.loginWaitDialog.show();
    }

    private void showWaitResetPswDialog(Context paramContext) {
        if (this.waitResetPswDialog == null) {
            this.waitResetPswDialog = new ProgressDialog(paramContext);
            this.waitResetPswDialog.setMessage(paramContext.getResources()
                    .getString(R.string.findpsw));
        }
        this.waitResetPswDialog.show();
    }

    public void cancelResetPswDialog() {
        if (this.resetPswDialog != null)
            this.resetPswDialog.dismiss();
    }

    public void doLogin() {
        if (!StringUtils.isEmpty(this.name)) {
            if (!StringUtils.isEmpty(this.password)) {
                showRegisterDialog(this);
                new Thread(new Runnable() {
                    public void run() {
                        LoginActivity.this.loginRequest();
                    }
                }).start();
            } else {
                DialogHelper.commonDialog("密码不能为空");
            }
        } else
            DialogHelper.commonDialog("用户名不能为空");
    }

    public void doRegister() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, RegisterActivity.class);
        startActivity(localIntent);
    }

    protected View getresetTextView() {
        return LayoutInflater.from(this).inflate(R.layout.user_resetpswitem,
                null);
    }

    public void initRestPswDialog() {
        if (this.resetPswDialog == null) {
            this.resetDialogView = LayoutInflater.from(this).inflate(
                    R.layout.resetpswdialogcontent, null);
            this.resetEditText = ((EditText) this.resetDialogView
                    .findViewById(R.id.email_id));
            this.resetOkButton = ((Button) this.resetDialogView
                    .findViewById(R.id.ok));
            this.resetOkButton.setOnClickListener(this);
            this.resetCancelButton = ((Button) this.resetDialogView
                    .findViewById(R.id.cancell));
            this.resetCancelButton.setOnClickListener(this);
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
            localBuilder.setTitle("找回密码").setView(this.resetDialogView);
            this.resetPswDialog = localBuilder.create();
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.sina_loginlayout:
            this.weiboFlag = "1";
            Cache.put("weibotype", "1");
            doSinaWeiboLogin();
            break;
        case R.id.tecentlogin_layout:
            this.weiboFlag = "2";
            Cache.put("weibotype", "2");
            doTecentWeiboLogin();
            break;
        case R.id.register:
            doRegister();
            break;
        case R.id.ok:
            cancelResetPswDialog();
            doReset();
            break;
        case R.id.cancell:
            if (this.resetPswDialog == null)
                break;
            this.resetPswDialog.dismiss();
        }
    }

    public void onComplete(String paramString) {
        LogUtils.log("main", "login response" + paramString);
        if (paramString != null)
            ;
        try {
            JSONObject localJSONObject = new JSONObject(paramString);
            if (localJSONObject != null) {
                this.weiboId = localJSONObject.getString("id");
                this.weiboName = localJSONObject.getString("name");
                if (this.weiboId != null)
                    this.handler.sendEmptyMessage(5);
            }
            return;
        } catch (JSONException localJSONException) {
            while (true)
                localJSONException.printStackTrace();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.login);
        initUI();
    }

    public void onError(WeiboException paramWeiboException) {
        if ((this.dialog != null) && (this.dialog.isShowing())) {
            this.dialog.dismiss();
            this.handler.sendEmptyMessage(7);
        }
    }

    public void onIOException(IOException paramIOException) {
        if (this.dialog != null)
            this.dialog.dismiss();
    }

    protected void onResume() {
        super.onResume();
        String str = (String) Cache.remove("weibotype");
        Weibo localWeibo = Weibo.getInstance();
        this.weiboFlag = str;
        Object localObject = getIntent().getData();
        if ("1".equals(str))
            if (localObject != null) {
                localObject = ((Uri) localObject)
                        .getQueryParameter("oauth_verifier");
                if (!StringUtils.isEmpty((String) localObject))
                    localWeibo.addOauthverifier((String) localObject);
            }
        // while (true)
        {
            try {
                info(localWeibo, Weibo.APP_KEY, "", "", "");
                return;
            } catch (Exception localException) {
                localException.printStackTrace();
                LogUtils.log("main", "on error");
                onError(null);
                // continue;
            }
            // if ((!"2".equals(str)) || (localObject == null))
            // continue;
            // LogUtils.log("main", "on resume");
            this.dialog = DialogHelper.getProgressBar("正在登录，请稍候...");
            this.dialog.show();
            getToken(((Uri) localObject).getQueryParameter("oauth_verifier"),
                    ((Uri) localObject).getQueryParameter("oauth_token"));
        }
    }

    public void showResetPswDialog() {
        initRestPswDialog();
        this.resetPswDialog.show();
    }

    private class LoginProtocolResult extends Protocol.OnJsonProtocolResult {
        public LoginProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.log("main", "login exception");
            LoginActivity.this.handler.sendEmptyMessage(4);
        }

        public void onResult(String paramString, Object paramObject) {
            LogUtils.log("main", "login 正常");
            if (paramObject != null) {
                LoginActivity.this.response = ((Response) paramObject);
                LoginActivity.this.handler.sendEmptyMessage(1);
            }
        }
    }

    private class RegisterProtocolResult extends Protocol.OnJsonProtocolResult {
        public RegisterProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LoginActivity.this.handler.sendEmptyMessage(7);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null)
                LoginActivity.this.response = ((Response) paramObject);
            LoginActivity.this.handler.sendEmptyMessage(6);
        }
    }

    private class ResetPswProtocolResult extends Protocol.OnJsonProtocolResult {
        public ResetPswProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LoginActivity.this.handler.sendEmptyMessage(3);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                LoginActivity.this.response = ((Response) paramObject);
                LoginActivity.this.handler.sendEmptyMessage(2);
            }
        }
    }
}
