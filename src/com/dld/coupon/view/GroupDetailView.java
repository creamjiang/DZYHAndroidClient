package com.dld.coupon.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.MyHtmlTagHandler;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.GroupCommentActivity;
import com.dld.coupon.activity.LoginActivity;
import com.dld.coupon.activity.OrderInfoActivity;
import com.dld.coupon.activity.WebviewActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.SendWeiboUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.Tag;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Shop;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupDetailView extends LinearLayout {
    private GroupDetail detail;
    private boolean first = true;
    private ImageView img;
    private boolean inFav;

    public GroupDetailView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void gotoOrderActivity() {
        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent;
                if (!StringUtils.isEmpty(SharePersistent.getInstance()
                        .getPerference(ActivityManager.getCurrent(),
                                "customer_id"))) {
                    LogUtils.log("GroupDetailView",
                            GroupDetailView.this.detail.id);
                    Cache.put("website", GroupDetailView.this.detail.website);
                    Cache.put("amount",
                            Double.valueOf(GroupDetailView.this.detail.price));
                    localIntent = new Intent();
                    localIntent.putExtra("groupdetail",
                            GroupDetailView.this.detail);
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

    private void initBrowse() {
        View.OnClickListener local4 = new View.OnClickListener() {
            public void onClick(View paramView) {
                Cache.put("detail", GroupDetailView.this.detail);
                Cache.put("activity", ActivityManager.getCurrent());
                Cache.put("inFav", Boolean.valueOf(true));
                Activity localActivity = ActivityManager.getCurrent();
                Intent localIntent = new Intent();
                localIntent.setClass(localActivity, WebviewActivity.class);
                localActivity.startActivity(localIntent);
                localActivity.overridePendingTransition(R.anim.zoomin,
                        R.anim.zoomout);
            }
        };
        findViewById(R.id.description).setOnClickListener(local4);
        this.img.setOnClickListener(local4);
        if (this.detail.id <= 0)
            findViewById(R.id.buy).setVisibility(View.GONE);
        else
            gotoOrderActivity();
    }

    private void initShare() {
        final String str2;
        if (this.detail.title.length() <= 80)
            str2 = this.detail.title;
        else
            str2 = this.detail.title.substring(0, 80) + "...";
        final Object localObject = new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(1000L * this.detail.endTime));
        final String str1 = str2 + "，截止到" + (String) localObject + "。"
                + "物价疯涨工资不涨的年代，咱们还是团购吧！来源：【打折店优惠】";
        findViewById(R.id.weibo_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Cache.put("weibo_content", str1
                                + " http://www.dld.com/group/255");
                        Cache.put("weibo_group_id",
                                GroupDetailView.this.detail.image);
                        Common.share();
                    }
                });
        findViewById(R.id.email_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.goSmsActivity(
                                new StringBuilder(String.valueOf(str2))
                                        .append("，截止到")
                                        .append((String) localObject)
                                        .append("。")
                                        .append("有空没空聚一聚，一起去吧！来源：【打折店优惠】")
                                        .toString()
                                        + " http://c.dld.com",
                                ActivityManager.getCurrent());
                    }
                });
        Object local = (Button) findViewById(R.id.favorite_button);
        if (Cache.remove("fav") != null)
            ((Button) local).setBackgroundResource(R.drawable.fav_del);
        else
            ((Button) local).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    final Object localObject1 = SharePersistent.getInstance()
                            .getPerference(ActivityManager.getCurrent(),
                                    "customer_id");
                    if (!StringUtils.isEmpty((String) localObject1)) {
                        Object localObject2 = GenericDAO
                                .getInstance(ActivityManager.getCurrent());
                        if (!((GenericDAO) localObject2).containsFav(2,
                                GroupDetailView.this.detail)) {
                            ((GenericDAO) localObject2).saveFav(2,
                                    GroupDetailView.this.detail);
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "已添加到收藏夹", 0).show();
                            LogUtils.log(
                                    "main",
                                    "is allow"
                                            + SharePersistent
                                                    .getInstance()
                                                    .getPerferenceBoolean(
                                                            ActivityManager
                                                                    .getCurrent(),
                                                            "isallowsendweibo"));
                            if (SharePersistent.getInstance()
                                    .getPerferenceBoolean(
                                            ActivityManager.getCurrent(),
                                            "isallowsendweibo")) {
                                localObject2 = SharePersistent.getInstance()
                                        .getPerference(
                                                ActivityManager.getCurrent(),
                                                "weibotype");
                                SendWeiboUtils
                                        .getInstance(
                                                ActivityManager.getCurrent())
                                        .sendWeibo(
                                                (String) localObject2,
                                                str1
                                                        + " http://www.dld.com/index/tuan",
                                                GroupDetailView.this.detail.image,
                                                1);
                            }
                            new Thread(new Runnable() {
                                public void run() {
                                    Param localParam1 = new Param();
                                    Param localParam2 = localParam1.append(
                                            "userId", (String) localObject1);
                                    String str;
                                    if (GroupDetailView.this.detail.id > 0)
                                        str = String
                                                .valueOf(GroupDetailView.this.detail.id);
                                    else
                                        str = GroupDetailView.this.detail.dealUrl;
                                    localParam2 = localParam2.append(
                                            "resourceId", str);
                                    if (GroupDetailView.this.detail.id > 0)
                                        str = "6";
                                    else
                                        str = "3";
                                    localParam2.append("type", str);
                                    ProtocolHelper.storeUpRequest(
                                            ActivityManager.getCurrent(),
                                            localParam1, false).startTrans();
                                }
                            }).start();
                        } else {
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "该团购信息已存在", 0).show();
                        }
                    } else {
                        Intent intent = new Intent(
                                ActivityManager.getCurrent(),
                                LoginActivity.class);
                        ActivityManager.getCurrent().startActivity(intent);
                    }
                }
            });
    }

    private void initShops() {
        if (!this.detail.shops.isEmpty()) {
            LayoutInflater localLayoutInflater = LayoutInflater
                    .from(ActivityManager.getCurrent());
            LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.shops);
            Iterator localIterator = this.detail.shops.iterator();
            while (localIterator.hasNext()) {
                final Shop localShop = (Shop) localIterator.next();
                View localView = localLayoutInflater.inflate(R.layout.shop,
                        null);
                Object localObject = (TextView) localView
                        .findViewById(R.id.shop_name);
                if (!StringUtils.isEmpty(localShop.name))
                    ((TextView) localObject).setText(localShop.name);
                else
                    ((TextView) localObject).setVisibility(8);
                localObject = localView.findViewById(R.id.shop_addr);
                if (!StringUtils.isEmpty(localShop.addr)) {
                    ((ImageView) ((View) localObject).findViewById(R.id.image))
                            .setBackgroundResource(R.drawable.group_address);
                    ((TextView) ((View) localObject).findViewById(R.id.text))
                            .setText(StringUtils.breakLines(localShop.addr));
                    ((View) localObject)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View paramView) {
                                    MapUtil.openMap(localShop.lat,
                                            localShop.lng, localShop.name);
                                }
                            });
                } else {
                    ((View) localObject).setVisibility(8);
                    ((View) localObject).findViewById(R.id.image)
                            .setVisibility(8);
                }
                localView.findViewById(R.id.shop_tel_button)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                Object localObject2 = null;
                                Object localObject1 = null;
                                int length = 0;
                                // while (true)
                                {
                                    try {
                                        if (!StringUtils.isEmpty(localShop.tel))
                                            return;
                                        // continue;
                                        DialogHelper.commonDialog("尚未收录该商家的电话");
                                        // return;
                                        localObject2 = Pattern.compile(
                                                "[0-9\\-]+").matcher(
                                                localShop.tel);
                                        localObject1 = new ArrayList();
                                        if (!((Matcher) localObject2).find()) {
                                            localObject2 = new String[((List) localObject1)
                                                    .size()];
                                            // ((List)localObject1).toArray(localObject2);
                                            length = ((String) localObject2)
                                                    .getBytes().length;
                                            if (length != 0)
                                                return;
                                            // break;
                                            DialogHelper.showTel();
                                        }
                                    } catch (Exception localException) {
                                        DialogHelper.showTel();
                                    }
                                    ((List) localObject1)
                                            .add(((Matcher) localObject2)
                                                    .group());
                                }
                                if (length > 1) {
                                    new AlertDialog.Builder(ActivityManager
                                            .getCurrent())
                                            .setTitle("请选择要拨打的电话")
                                            .setSingleChoiceItems(
                                                    (Integer) localObject2,
                                                    0,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface paramDialogInterface,
                                                                int paramInt) {
                                                            Intent localIntent = new Intent(
                                                                    "android.intent.action.DIAL");
                                                            localIntent
                                                                    .setData(Uri
                                                                            .parse("tel:"
                                                                                    + localShop));
                                                            ActivityManager
                                                                    .getCurrent()
                                                                    .startActivity(
                                                                            localIntent);
                                                            paramDialogInterface
                                                                    .dismiss();
                                                        }
                                                    }).create().show();
                                } else {
                                    localObject1 = new Intent(
                                            "android.intent.action.DIAL");
                                    ((Intent) localObject1).setData(Uri
                                            .parse("tel:" + localShop.tel));
                                    ActivityManager.getCurrent().startActivity(
                                            (Intent) localObject1);
                                }
                            }
                        });
                localView.findViewById(R.id.shop_map_button)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                MapUtil.openMap(localShop.lat, localShop.lng,
                                        localShop.name);
                            }
                        });
                localObject = (Button) localView
                        .findViewById(R.id.shop_comment_button);
                ((Button) localObject).setText(Html.fromHtml("评论("
                        + localShop.comments + ")"));
                ((Button) localObject).findViewById(R.id.shop_comment_button)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                Cache.put("dpid", localShop.dpid);
                                Intent localIntent = new Intent();
                                localIntent.setClass(
                                        ActivityManager.getCurrent(),
                                        GroupCommentActivity.class);
                                ActivityManager.getCurrent().startActivity(
                                        localIntent);
                            }
                        });
                localLinearLayout.addView(localView);
            }
        }
        findViewById(R.id.shop_empty).setVisibility(0);
    }

    private void initTime() {
        TextView localTextView = (TextView) findViewById(R.id.left);
        long l1 = this.detail.endTime - System.currentTimeMillis() / 1000L;
        if (l1 > 0L) {
            String str = "";
            long l2 = l1 / 86400L;
            if (l2 > 0L)
                str = str + l2 + "天";
            l2 = l1 / 3600L % 24L;
            if (l2 > 0L)
                str = str + l2 + "小时";
            l1 = l1 / 60L % 60L;
            if (l1 > 0L)
                str = str + l1 + "分";
            localTextView.setText("剩余 " + str);
        }
    }

    private void initView() {
        String str = StringUtils.shortenNumber(this.detail.price);
        ((TextView) findViewById(R.id.price)).setText("¥" + str);
        ((TextView) findViewById(R.id.bought)).setText(Integer
                .toString(this.detail.bought));
        str = StringUtils.breakLines(this.detail.title, 13, 26);
        ((TextView) findViewById(R.id.description)).setText(Html.fromHtml("<u>"
                + str + "详情>></u>"));
        str = "原价：" + "<br>" + "折扣：<font color='#ff6a10'>"
                + StringUtils.shortenNumber(this.detail.rebate) + "</font> 折";
        ((TextView) findViewById(R.id.discount_text)).setText(Html
                .fromHtml(str));
        str = "<s>¥" + StringUtils.shortenNumber(this.detail.value) + " </s>";
        ((TextView) findViewById(R.id.value_text)).setText(Html.fromHtml(str,
                null, new MyHtmlTagHandler()));
        str = this.detail.image;
        if (!this.inFav) {
            if (str != null)
                new ImageProtocol(ActivityManager.getCurrent(),
                        "http://www.dld.com/tuan/viewimage?u="
                                + URLEncoder.encode(str) + "&w=400&h=400",
                        this.detail.dealUrl).startTrans(this.img);
        } else
            FileUtil.readImage(this.img, str, 400, 400);
        initTime();
        initShare();
        initBrowse();
        initShops();
    }

    public void onCreate(GroupDetail paramGroupDetail, Showable paramShowable,
            boolean paramBoolean) {
        if (this.first) {
            this.inFav = paramBoolean;
            this.img = ((ImageView) findViewById(R.id.couponimage));
            this.first = false;
            this.detail = paramGroupDetail;
            LogUtils.log("GroupDetail", paramGroupDetail);
            initView();
        }
    }

    public void onDestroy() {
        if (!this.first) {
            if (this.img.getTag() == Tag.IMG_CHANGED) {
                ((BitmapDrawable) this.img.getDrawable()).getBitmap().recycle();
                LogUtils.log("test", "recyled");
            }
            this.img.setTag(Tag.IMG_RECYLED);
        }
    }
}
