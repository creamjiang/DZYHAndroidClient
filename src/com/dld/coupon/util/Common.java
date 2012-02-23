package com.dld.coupon.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.Browsers;
import com.dld.coupon.activity.ShareActivity;
import com.dld.coupon.activity.TencentWeiboActivity;
import com.dld.coupon.view.DialogHelper;
import com.tencent.weibo.utils.Cache;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    public static final String ABOUT = "关于";
    public static final String ABOUT_MESSAGE = "店连店优惠\n\n版本 2.3\n\n网址: www.dld.com";
    public static final String CARD = "我的卡";
    public static final String CITY_SETTING = "城市设置";
    public static final String CLEAR = "清除缓存";
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");
    public static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    public static final String FEEDBACK = "意见反馈";
    public static final String LOCATION_CLOSE = "关闭定位";
    public static final String LOCATION_ON = "允许定位";
    public static final String LOGIN = "登录";
    public static final String LOGOFF = "退出登录";
    public static final String PHONE = "客服电话";
    public static final String RETURN = "返回搜索";
    public static final String RETURN_DETAIL = "返回详情";
    public static final String SETTING = "设置";
    private static float scale = -1.0F;

    public static void attachNavigate(int paramInt,
            Class<? extends Activity> paramClass) {
        setOnclickListener(ActivityManager.getCurrent().findViewById(paramInt),
                paramClass);
    }

    public static void attachTouchNavigate(int paramInt,
            Class<? extends Activity> paramClass) {
        View localView = ActivityManager.getCurrent().findViewById(paramInt);
        setOnclickListener(localView, paramClass);
        localView.setOnTouchListener(new OnTouchListenerImpl(localView));
    }

    public static void call(final String paramString) {
        int length = 0;
        if (!StringUtils.isEmpty(paramString)) {
            Object localObject2 = Pattern.compile("[0-9\\-]+").matcher(
                    paramString);
            Object localObject1 = new ArrayList();
            while (((Matcher) localObject2).find())
                ((List) localObject1).add(((Matcher) localObject2).group());
            localObject2 = new String[((List) localObject1).size()];
            // ((List)localObject1).toArray(localObject2);
            length = ((String) localObject2).getBytes().length;
            if (length != 0) {
                if (length <= 1) {
                    localObject1 = new Intent("android.intent.action.DIAL");
                    ((Intent) localObject1).setData(Uri.parse("tel:"
                            + paramString));
                    ActivityManager.getCurrent().startActivity(
                            (Intent) localObject1);
                } else {
                    new AlertDialog.Builder(ActivityManager.getCurrent())
                            .setTitle("请选择要拨打的电话")
                            .setSingleChoiceItems((Integer) localObject2, 0,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface paramDialogInterface,
                                                int paramInt) {
                                            Intent localIntent = new Intent(
                                                    "android.intent.action.DIAL");
                                            localIntent.setData(Uri
                                                    .parse("tel:" + paramString));
                                            ActivityManager.getCurrent()
                                                    .startActivity(localIntent);
                                            paramDialogInterface.dismiss();
                                        }
                                    }).create().show();
                }
            } else
                DialogHelper.showTel();
        } else {
            DialogHelper.commonDialog("尚未收录该商家的电话");
        }
    }

    public static float getScale() {
        if (scale < 0.0F)
            scale = ActivityManager.getCurrent().getWindowManager()
                    .getDefaultDisplay().getWidth() / 480.0F;
        return scale;
    }

    public static void goSmsActivity(String paramString, Activity paramActivity) {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.putExtra("sms_body", paramString);
        localIntent.setType("vnd.android-dir/mms-sms");
        paramActivity.startActivity(localIntent);
    }

    public static void hideKeyboard(View paramView) {
        ((InputMethodManager) ActivityManager.getCurrent().getSystemService(
                "input_method")).hideSoftInputFromWindow(
                paramView.getWindowToken(), 0);
    }

    public static void openBrowser(String paramString) {
        openBrowser(paramString, false);
    }

    public static void openBrowser(String paramString, boolean paramBoolean) {
        Activity localActivity = ActivityManager.getCurrent();
        Intent localIntent = new Intent();
        localIntent.setClass(localActivity, Browsers.class);
        localIntent.putExtra("url", paramString);
        localIntent.putExtra("backable", paramBoolean);
        ActivityManager.getCurrent().startActivity(localIntent);
    }

    private static void setOnclickListener(View paramView,
            final Class<? extends Activity> paramClass) {
        paramView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Common.startActivity(paramClass);
            }
        });
    }

    public static void share() {
        String[] arrayOfString = new String[3];
        arrayOfString[0] = "分享到新浪微博";
        arrayOfString[1] = "分享到腾讯微博";
        arrayOfString[2] = "短信分享";
        final Activity localActivity = ActivityManager.getCurrent();
        new AlertDialog.Builder(localActivity).setTitle("分享")
                .setItems(arrayOfString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        switch (paramInt) {
                        case 0:
                            ShareActivity.doLogin();
                            break;
                        case 1:
                            TencentWeiboActivity.initLogin(false);
                            break;
                        case 2:
                            Common.goSmsActivity(
                                    (String) Cache.remove("weibo_content"),
                                    localActivity);
                        }
                    }
                }).show();
    }

    public static void startActivity(Class<? extends Activity> paramClass) {
        Activity localActivity = ActivityManager.getCurrent();
        Intent localIntent = new Intent();
        localIntent.setClass(localActivity, paramClass);
        localActivity.startActivity(localIntent);
    }
}
