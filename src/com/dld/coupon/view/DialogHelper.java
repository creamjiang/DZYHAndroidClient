package com.dld.coupon.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;

import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.CityActivity;
import com.dld.coupon.util.UserConfig;
import com.dld.coupon.activity.R;

public class DialogHelper {
    public static final int INPUT_COMMENT = 100;

    public static void alert(String paramString1, String paramString2) {
        AlertDialog localAlertDialog = new AlertDialog.Builder(
                ActivityManager.getCurrent()).setTitle(paramString1)
                .setMessage(paramString2).setPositiveButton("确定", null)
                .create();
        localAlertDialog.setCanceledOnTouchOutside(true);
        localAlertDialog.show();
    }

    public static void commonDialog(String paramString) {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage(paramString).setNegativeButton("返回", null).show();
    }

    public static ProgressDialog getProgressBar(String paramString) {
        return getProgressBar(paramString, ActivityManager.getCurrent());
    }

    public static ProgressDialog getProgressBar(String paramString,
            Context paramContext) {
        ProgressDialog localProgressDialog = ProgressDialog.show(paramContext,
                "", paramString);
        localProgressDialog.setCancelable(true);
        localProgressDialog.setCanceledOnTouchOutside(true);
        return localProgressDialog;
    }

    public static ProgressDialog getProgressBarNoShow(String paramString) {
        ProgressDialog localProgressDialog = new ProgressDialog(
                ActivityManager.getCurrent());
        localProgressDialog.setMessage(paramString);
        localProgressDialog.setCancelable(true);
        localProgressDialog.setCanceledOnTouchOutside(true);
        return localProgressDialog;
    }

    public static void showDownloadCouponFailed() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("下发失败，请重试...").setNegativeButton("取消", null).show();
    }

    public static void showDownloadCouponSuccess() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("短信已经下发到您的手机，请注意查收.").setNegativeButton("确定", null)
                .show();
    }

    public static void showHasRegist() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("您已经成功注册打折店会员，感谢您的支持")
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                    }
                }).show();
    }

    public static void showInputComment() {
        InputCommentDialogLinearLayout localInputCommentDialogLinearLayout = (InputCommentDialogLinearLayout) View
                .inflate(ActivityManager.getCurrent(), R.layout.comment_dialog,
                        null);
        new AlertDialog.Builder(ActivityManager.getCurrent())
                .setView(localInputCommentDialogLinearLayout)
                .setPositiveButton(
                        "评论",
                        localInputCommentDialogLinearLayout
                                .getOnCommentListener())
                .setNegativeButton(
                        "取消",
                        localInputCommentDialogLinearLayout
                                .getOnCancelListener()).show();
    }

    public static void showInvalidLatLon() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("此商户信息还未加入地图").setNegativeButton("返回", null).show();
    }

    public static void showLocationFailed() {
        new AlertDialog.Builder(ActivityManager.getCurrent())
                .setTitle("提示")
                .setMessage("抱歉，没有成功定位您的位置，请选择城市")
                .setPositiveButton("选择城市",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface paramDialogInterface,
                                    int paramInt) {
                                Intent localIntent = new Intent();
                                localIntent.setClass(
                                        ActivityManager.getCurrent(),
                                        CityActivity.class);
                                ActivityManager.getCurrent().startActivity(
                                        localIntent);
                            }
                        }).show();
    }

    public static void showNotCorrect() {
        new AlertDialog.Builder(ActivityManager.getCurrent())
                .setTitle("提示")
                .setMessage("抱歉，您所在的城市还未开通优惠券服务，您可以选择一个城市在推荐栏目中查看优惠券信息。谢谢您的支持！")
                .setPositiveButton("选择城市",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface paramDialogInterface,
                                    int paramInt) {
                                Intent localIntent = new Intent();
                                localIntent.setClass(
                                        ActivityManager.getCurrent(),
                                        CityActivity.class);
                                ActivityManager.getCurrent().startActivity(
                                        localIntent);
                            }
                        }).show();
    }

    public static void showQueryResultIsNull() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("没有查询到结果,您可以换个关键词试试.")
                .setNegativeButton("返回", null).show();
    }

    public static void showRecommendResultIsNull() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("没有查询到结果,我们会尽快收录相关优惠信息.")
                .setNegativeButton("返回", null).show();
    }

    public static void showSaveOK() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("保存成功").setNegativeButton("返回", null).show();
    }

    public static void showTel() {
        new AlertDialog.Builder(ActivityManager.getCurrent()).setTitle("提示")
                .setMessage("此商户电话还未收录").setNegativeButton("返回", null).show();
    }

    public static void showUploadSina() {
        new AlertDialog.Builder(ActivityManager.getCurrent())
                .setTitle("同步提示")
                .setMessage("您是否要同步到新浪微博?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        UserConfig.setAllowUploadSina(
                                ActivityManager.getCurrent(), true);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        UserConfig.setAllowUploadSina(
                                ActivityManager.getCurrent(), false);
                    }
                }).show();
    }
}
