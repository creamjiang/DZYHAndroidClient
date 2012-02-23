package com.dld.coupon.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.PhoneUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.UserConfig;
import com.dld.protocol.CommonProtocolHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CheckVersionResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Response;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;

public class MainActivity extends TabActivity implements
        TabHost.OnTabChangeListener {
    public static int DIALOG_LOCATION = 100;
    public static int DIALOG_QUIT = 101;
    TabHost.TabSpec aroundTabSpec;
    boolean first;
    TabHost.TabSpec shoppingTabSpec;
    Handler handler;
    private boolean loginChecked;
    private Integer[] myMenuRes;
    TabHost.TabSpec cheapskateCardTabSpec;
    String[] names;
    TabHost.TabSpec discountTabSpec;
    TabHost.TabSpec indexTabSpec;
    TabHost tabHost;
    TabWidget tabWidget;

    public MainActivity() {
        Object[] localObject = new Integer[5];
        localObject[0] = Integer.valueOf(R.drawable.tab_index_default);
        localObject[1] = Integer.valueOf(R.drawable.tab_around_default);
        localObject[2] = Integer.valueOf(R.drawable.tab_discount_default);
        localObject[3] = Integer.valueOf(R.drawable.tab_shopping_default);
        localObject[4] = Integer.valueOf(R.drawable.tab_cheapskatecard_default);
        this.myMenuRes = ((Integer[]) localObject);
        localObject = new String[5];
        localObject[0] = "首页";
        localObject[1] = "周边";
        localObject[2] = "爱打折";
        localObject[3] = "逛街店";
        localObject[4] = "小气鬼卡";
        this.names = ((String[]) localObject);
        this.loginChecked = false;
        this.first = true;
        this.handler = new Handler();
    }

    private void activate() {
        String str = PhoneUtil.getIMEI(this);
        Object localObject = UserConfig.getChannelNum(this);
        localObject = new Param().append("imei", str).append("ct", "0")
                .append("chn", (String) localObject);
        ProtocolHelper.postRequest("guest_register").startTransForUser(
                new Protocol.OnJsonProtocolResult(Response.class) {
                    public void onException(String paramString,
                            Exception paramException) {
                        LogUtils.e("test", "", paramException);
                    }

                    public void onResult(String paramString, Object paramObject) {
                        if (paramObject != null)
                            ;
                        try {
                            if (((Response) paramObject).code == 0)
                                SharePersistent
                                        .saveActivateState(MainActivity.this);
                            return;
                        } catch (Exception localException) {
                            while (true)
                                LogUtils.e("test", "", localException);
                        }
                    }
                }, 60, 3, (Param) localObject);
    }

    private void checkVersion() {
        CommonProtocolHelper.getVersion(this).startTrans(
                new Protocol.OnJsonProtocolResult(CheckVersionResponse.class) {
                    public void onException(String paramString,
                            Exception paramException) {
                    }

                    public void onResult(String paramString, Object paramObject) {
                        try {
                            final CheckVersionResponse localCheckVersionResponse = (CheckVersionResponse) paramObject;
                            if (Integer
                                    .parseInt(MainActivity.this.getResources()
                                            .getString(R.string.version)) < localCheckVersionResponse.ver)
                                MainActivity.this.handler.post(new Runnable() {
                                    public void run() {
                                        MainActivity.this
                                                .showUpdate(localCheckVersionResponse);
                                    }
                                });
                            return;
                        } catch (Exception localException) {
                            while (true)
                                LogUtils.e("test", "get version error",
                                        localException);
                        }
                    }
                }, 30, 3);
    }

    private void initTabChildView() {
        Object localObject = LayoutInflater.from(this);
        View localView = ((LayoutInflater) localObject).inflate(
                R.layout.tab_child_view_main, null);
        ((ImageView) localView.findViewById(R.id.icon))
                .setImageResource(this.myMenuRes[0].intValue());
        localView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_bg));
        ((TextView) localView.findViewById(R.id.text)).setText(this.names[0]);
        this.indexTabSpec.setIndicator(localView);
        localView = ((LayoutInflater) localObject).inflate(
                R.layout.tab_child_view_main, null);
        ((ImageView) localView.findViewById(R.id.icon))
                .setImageResource(this.myMenuRes[1].intValue());
        localView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_bg));
        ((TextView) localView.findViewById(R.id.text)).setText(this.names[1]);
        this.aroundTabSpec.setIndicator(localView);
        localView = ((LayoutInflater) localObject).inflate(
                R.layout.tab_child_view_main, null);
        ((ImageView) localView.findViewById(R.id.icon))
                .setImageResource(this.myMenuRes[2].intValue());
        localView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_bg));
        ((TextView) localView.findViewById(R.id.text)).setText(this.names[2]);
        this.discountTabSpec.setIndicator(localView);
        localView = ((LayoutInflater) localObject).inflate(
                R.layout.tab_child_view_main, null);
        ((ImageView) localView.findViewById(R.id.icon))
                .setImageResource(this.myMenuRes[3].intValue());
        localView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_bg));
        ((TextView) localView.findViewById(R.id.text)).setText(this.names[3]);
        this.shoppingTabSpec.setIndicator(localView);
        localObject = ((LayoutInflater) localObject).inflate(
                R.layout.tab_child_view_main, null);
        ((ImageView) ((View) localObject).findViewById(R.id.icon))
                .setImageResource(this.myMenuRes[4].intValue());
        localView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_bg));
        ((TextView) ((View) localObject).findViewById(R.id.text))
                .setText(this.names[4]);
        this.cheapskateCardTabSpec.setIndicator((View) localObject);
    }

    private void initUI() {
        this.tabHost = getTabHost();
        this.tabWidget = getTabWidget();
        this.tabHost.setOnTabChangedListener(this);
        this.indexTabSpec = this.tabHost.newTabSpec("tid1");
        this.aroundTabSpec = this.tabHost.newTabSpec("tid2");
        this.discountTabSpec = this.tabHost.newTabSpec("tid3");
        this.shoppingTabSpec = this.tabHost.newTabSpec("tid4");
        this.cheapskateCardTabSpec = this.tabHost.newTabSpec("tid5");
        initTabChildView();
        this.indexTabSpec.setContent(new Intent(this, SearchActivity.class));
        this.aroundTabSpec
                .setContent(new Intent(this, AroundActivity.class));
        this.discountTabSpec.setContent(new Intent(this, YouhuiActivity.class));
        this.shoppingTabSpec.setContent(new Intent(this, GroupActivity.class));
        this.cheapskateCardTabSpec.setContent(new Intent(this,
                MyDoujiaoActivity.class));
        this.tabHost.addTab(this.indexTabSpec);
        this.tabHost.addTab(this.aroundTabSpec);
        this.tabHost.addTab(this.discountTabSpec);
        this.tabHost.addTab(this.shoppingTabSpec);
        this.tabHost.addTab(this.cheapskateCardTabSpec);
        this.tabHost.setCurrentTab(0);
    }

    private void login() {
        try {
            if (!this.loginChecked) {
                LogUtils.log("test", "login execute");
                String str1 = SharePersistent.getInstance().getPerference(this,
                        "customer_id");
                String str2 = SharePersistent.getInstance().getPerference(this,
                        "unicId");
                String str3 = SharePersistent.getInstance().getPerference(
                        ActivityManager.getCurrent(), "weibotype");
                if ((!StringUtils.isEmpty(str1))
                        && (!StringUtils.isEmpty(str2))) {
                    LogUtils.log("test", "已注册为用户");
                    userLogin(str1, str2, str3);
                }
            }
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    private void showUpdate(final CheckVersionResponse paramCheckVersionResponse) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle(paramCheckVersionResponse.title)
                    .setMessage(paramCheckVersionResponse.desc)
                    .setPositiveButton("立即升级",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    MainActivity.this
                                            .downFile(paramCheckVersionResponse.url);
                                }
                            })
                    .setNegativeButton("暂不更新",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                }
                            }).create().show();
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    private void userLogin(String paramString1, String paramString2,
            String paramString3) {
        Param localParam = new Param();
        LogUtils.log("test", "userLogin");
        localParam.append("cid", paramString1).append("uid", paramString2)
                .append("lt", paramString3);
        ProtocolHelper.loginRequest(this, localParam, false).startTransForUser(
                null, localParam);
    }

    void downFile(String paramString) {
        startActivity(new Intent("android.intent.action.VIEW",
                Uri.parse(paramString)));
    }

    public void finish() {
        super.finish();
        ActivityManager.pop();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setRequestedOrientation(1);
        ActivityManager.push(this);
        setContentView(R.layout.main);
        initUI();
        String str = getResources().getString(R.string.version);
        /** 20120220
        if ((!str.equals(SharePersistent.getVersion(this)))
                || (!SharePersistent.getActivateState(this))) {
            SharePersistent.saveVersion(this, str);
            activate();
        }
        **/
        str = SharePersistent.get("gps_city");
        if ((!StringUtils.isEmpty(str))
                && (!StringUtils.equals(str, SharePersistent.get("city_name"))))
            MapUtil.showCityChanged(str);
    }

    protected Dialog onCreateDialog(int paramInt) {
        Object localObject;
        if (paramInt != DIALOG_QUIT)
            localObject = super.onCreateDialog(paramInt);
        else
            localObject = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否退出店连店优惠？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    MainActivity.this.finish();
                                }
                            })
                    .setNegativeButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                }
                            }).create();
        return (Dialog) localObject;
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        LogUtils.log("main", "on key down");
        boolean i;
        if (paramInt != 4) {
            i = false;
        } else {
            showDialog(DIALOG_QUIT);
            i = true;
        }
        return i;
    }

    protected void onResume() {
        super.onResume();
        ActivityManager.push(this);
        if (this.first) {
            this.first = false;
           //20120220 checkVersion();
            //20120220 login();
        }
        this.tabHost.setCurrentTab(BaseActivity.segmentPosition);
    }

    public void onTabChanged(String paramString) {
        int j = this.tabHost.getCurrentTab();
        BaseActivity.segmentPosition = j;
        for (int i = 0; i < this.tabWidget.getChildCount(); i++) {
            TextView localTextView = (TextView) this.tabWidget.getChildAt(i)
                    .findViewById(R.id.text);
           
            if (j != i)
                localTextView.setTextColor(R.color.white);
            else
                localTextView.setTextColor(R.color.button_text_color);
               
        }
    }
}
