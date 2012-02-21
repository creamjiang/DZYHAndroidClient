package com.dld.coupon.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.LoginActivity;
import com.dld.coupon.activity.ShareActivity;
import com.dld.coupon.activity.TencentWeiboActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.SendWeiboUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.CouponInfo;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;

public class StoreDetailView extends DetailView {
    private static final String[] costs;
    private TextView buslineTv;
    private TextView consumelevTv;
    private TextView cookStyleTv;
    private CouponInfo couponInfo;
    private CouponDetail detail;
    private TextView distanceTv;
    private boolean first = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what != 1) {
                if (paramMessage.what != 2) {
                    if (paramMessage.what != 3)
                        if (paramMessage.what != 4) {
                            if (paramMessage.what == 5) {
                                StoreDetailView.this.dismissProgressDailog();
                                DialogHelper.showDownloadCouponSuccess();
                            }
                        } else {
                            StoreDetailView.this.dismissProgressDailog();
                            DialogHelper.showDownloadCouponFailed();
                        }
                } else
                    StoreDetailView.this
                            .initCouponInfo(StringUtils
                                    .getDiscountText(StoreDetailView.this.detail.wl_discount));
            } else {
                StringBuilder localStringBuilder = new StringBuilder();
                if (!StringUtils.isEmpty(StoreDetailView.this.detail.bank))
                    StoreDetailView.this.appendInfo(localStringBuilder, "发卡银行",
                            StoreDetailView.this.detail.getBank());
                Object localObject2 = StoreDetailView.this.shopHourTv;
                Object localObject3 = new StringBuilder("营业时间:");
                Object localObject1;
                if (!StringUtils
                        .isEmpty(StoreDetailView.this.detail.business_hours))
                    localObject1 = StoreDetailView.this.detail.business_hours;
                else
                    localObject1 = "暂无";
                ((TextView) localObject2).setText((String) localObject1);
                if (!StringUtils.isEmpty(StoreDetailView.this.detail.style)) {
                    localObject2 = StoreDetailView.this.cookStyleTv;
                    localObject3 = new StringBuilder("菜系:");
                    if (!StringUtils.isEmpty(StoreDetailView.this.detail.style))
                        localObject1 = StoreDetailView.this.detail.style;
                    else
                        localObject1 = "暂无";
                    ((TextView) localObject2).setText((String) localObject1);
                } else {
                    StoreDetailView.this.cookStyleTv.setVisibility(8);
                }
                if (!StringUtils
                        .isEmpty(StoreDetailView.this.detail.special_offer)) {
                    localObject1 = StoreDetailView.this.speciltyTv;
                    localObject2 = new StringBuilder("招牌菜:");
                    if (!StringUtils
                            .isEmpty(StoreDetailView.this.detail.special_offer))
                        localObject3 = StoreDetailView.this.detail.special_offer;
                    else
                        localObject3 = "暂无";
                    ((TextView) localObject1).setText((String) localObject3);
                } else {
                    StoreDetailView.this.speciltyTv.setVisibility(8);
                }
                localObject3 = StoreDetailView.this.buslineTv;
                localObject2 = new StringBuilder("公交路线:");
                if (!StringUtils.isEmpty(StoreDetailView.this.detail.bus_route))
                    localObject1 = StoreDetailView.this.detail.bus_route
                            .replaceAll("，|、", "$0 ");
                else
                    localObject1 = "暂无";
                ((TextView) localObject3).setText((String) localObject1);
                if ((StoreDetailView.this.detail.cost <= 0)
                        || (StoreDetailView.this.detail.cost > 8))
                    StoreDetailView.this.consumelevTv.setText("消费档次: 暂无");
                else
                    StoreDetailView.this.consumelevTv
                            .setText("消费档次:"
                                    + StoreDetailView.costs[(-1 + StoreDetailView.this.detail.cost)]);
                localObject2 = StoreDetailView.this.storeIntroTv;
                if (!StringUtils
                        .isEmpty(StoreDetailView.this.detail.introduction))
                    localObject1 = StoreDetailView.this.detail.introduction;
                else
                    localObject1 = "暂无";
                ((TextView) localObject2).setText((CharSequence) localObject1);
                StoreDetailView.this.appendInfo(localStringBuilder, "分店",
                        StoreDetailView.this.detail.branch_name);
                StoreDetailView.this.appendInfo(localStringBuilder, "营业时间",
                        StoreDetailView.this.detail.business_hours);
                StoreDetailView.this.appendInfo(localStringBuilder, "无线优惠券ID",
                        StoreDetailView.this.detail.bizcode);
                if ((StoreDetailView.this.detail.distance < 0)
                        || (StoreDetailView.this.detail.distance > 3000)) {
                    StoreDetailView.this.distanceTv.setVisibility(8);
                } else {
                    StoreDetailView.this.distanceTv.setVisibility(0);
                    StoreDetailView.this.distanceTv.setText("距离:"
                            + StoreDetailView.this.detail.distance + "米");
                }
            }
        }
    };
    private Dialog progressDialog;
    private boolean ready = false;
    private TextView shopHourTv;
    private TextView speciltyTv;
    private boolean startLoad = false;
    private TextView storeIntroTv;

    static {
        String[] arrayOfString = new String[8];
        arrayOfString[0] = "低档";
        arrayOfString[1] = "中档";
        arrayOfString[2] = "高档";
        arrayOfString[3] = "超豪华";
        arrayOfString[4] = "低档";
        arrayOfString[5] = "中档";
        arrayOfString[6] = "高档";
        arrayOfString[7] = "超豪华";
        costs = arrayOfString;
    }

    public StoreDetailView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void appendInfo(StringBuilder paramStringBuilder,
            String paramString1, String paramString2) {
        if (StringUtils.isEmpty(paramString2))
            ;
        // while (true)
        {
            // return;
            try {
                paramStringBuilder.append(paramString1 + "：" + paramString2
                        + "\n");
            } catch (Exception localException) {
            }
        }
    }

    private void dismissProgressDailog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private String getContent() {
        StringBuilder localStringBuilder = new StringBuilder(this.detail.name);
        if (!StringUtils.isEmpty(this.detail.address))
            localStringBuilder.append("(" + this.detail.address);
        if (!StringUtils.isEmpty(this.detail.telno))
            localStringBuilder.append("," + this.detail.telno + ")");
        localStringBuilder.append("，有空没空聚一聚，一起去吧！来源：【打折店优惠】");
        return localStringBuilder.toString();
    }

    private void initAddress() {
        LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.detail_address);
        ((ImageView) localLinearLayout.findViewById(R.id.image))
                .setBackgroundResource(R.drawable.address_img);
        ((TextView) localLinearLayout.findViewById(R.id.text)).setText(Html
                .fromHtml("<u>" + StringUtils.breakLines(this.detail.address)
                        + "</u>"));
        localLinearLayout.setOnTouchListener(new OnTouchListenerImpl(
                localLinearLayout));
        localLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MapUtil.openMap(StoreDetailView.this.detail.x,
                        StoreDetailView.this.detail.y,
                        StoreDetailView.this.detail.name);
            }
        });
    }

    private void initCouponDetail() {
        ((TextView) findViewById(R.id.detail_name)).setText(this.detail.name);
    }

    private void initCouponInfo(String paramString) {
        if (paramString != null) {
            TextView localTextView = (TextView) findViewById(R.id.couponinfo);
            localTextView.setTextSize(14.0F);
            localTextView.setText(paramString);
        }
        if (this.couponInfo == null)
            ;
    }

    private void initShare() {
        findViewById(R.id.share_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Object localObject = new StringBuffer();
                        ((StringBuffer) localObject)
                                .append(StoreDetailView.this.detail.name)
                                .append("(")
                                .append(StoreDetailView.this.detail.address);
                        if (StoreDetailView.this.detail.telno != null)
                            ((StringBuffer) localObject).append(
                                    StoreDetailView.this.detail.telno).append(
                                    ",");
                        ((StringBuffer) localObject).append(")").append(
                                ", 这家店靠谱，大家可以试试，我先把它收了！来源：【打折店优惠");
                        localObject = ((StringBuffer) localObject).toString();
                        String[] arrayOfString = new String[3];
                        arrayOfString[0] = "分享到新浪微博";
                        arrayOfString[1] = "分享到腾讯微博";
                        arrayOfString[2] = "短信分享";
                        final String content = (String) localObject;
                        new AlertDialog.Builder(ActivityManager.getCurrent())
                                .setTitle("分享")
                                .setItems(arrayOfString,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                switch (paramInt) {
                                                case 0:
                                                    Cache.put("weibo_content",
                                                            content);
                                                    ShareActivity.doLogin();
                                                    break;
                                                case 1:
                                                    Cache.put("weibo_content",
                                                            content);
                                                    TencentWeiboActivity
                                                            .initLogin(false);
                                                    break;
                                                case 2:
                                                    Common.goSmsActivity(
                                                            content,
                                                            ActivityManager
                                                                    .getCurrent());
                                                }
                                            }
                                        }).show();
                    }
                });
        findViewById(R.id.email_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.goSmsActivity(StoreDetailView.this.getContent()
                                + " http://c.dld.com",
                                ActivityManager.getCurrent());
                    }
                });
        Button localButton = (Button) findViewById(R.id.favorite_button);
        if (Cache.remove("fav") != null)
            localButton.setBackgroundResource(R.drawable.fav_del);
        else
            localButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    final Object localObject1 = SharePersistent.getInstance()
                            .getPerference(ActivityManager.getCurrent(),
                                    "customer_id");
                    if (!StringUtils.isEmpty((String) localObject1)) {
                        Object localObject2 = GenericDAO
                                .getInstance(ActivityManager.getCurrent());
                        if (!((GenericDAO) localObject2).containsFav(0,
                                StoreDetailView.this.detail)) {
                            ((GenericDAO) localObject2).saveFav(0,
                                    StoreDetailView.this.detail);
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "已添加到收藏夹", 0).show();
                            if (SharePersistent.getInstance()
                                    .getPerferenceBoolean(
                                            ActivityManager.getCurrent(),
                                            "isallowsendweibo")) {
                                localObject2 = SharePersistent.getInstance()
                                        .getPerference(
                                                ActivityManager.getCurrent(),
                                                "weibotype");
                                String str = StoreDetailView.this.detail.name
                                        + "（"
                                        + StoreDetailView.this.detail.address
                                        + "，"
                                        + StoreDetailView.this.detail.telno
                                        + "）  【打折店优惠】";
                                SendWeiboUtils.getInstance(
                                        ActivityManager.getCurrent())
                                        .sendWeibo((String) localObject2, str,
                                                null, 1);
                            }
                            new Thread(new Runnable() {
                                public void run() {
                                    Param localParam = new Param();
                                    localParam
                                            .append("userId",
                                                    (String) localObject1)
                                            .append("resourceId",
                                                    StoreDetailView.this.detail.id)
                                            .append("type", "7")
                                            .append("description", "2");
                                    ProtocolHelper.storeUpRequest(
                                            ActivityManager.getCurrent(),
                                            localParam, false).startTrans();
                                }
                            }).start();
                        } else {
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "该商家已存在", 0).show();
                        }
                    } else {
                        Intent intent = new Intent(
                                ActivityManager.getCurrent(),
                                LoginActivity.class);
                        ActivityManager.getCurrent().startActivityForResult(
                                intent, 0);
                    }
                }
            });
    }

    private void initTel() {
        Object localObject = (LinearLayout) findViewById(R.id.detail_tel);
        if (StringUtils.isEmpty(this.detail.telno)) {
            ((LinearLayout) localObject).setVisibility(View.GONE);
            findViewById(R.id.detail_tel_line).setVisibility(View.GONE);
        } else {
            ((ImageView) ((LinearLayout) localObject).findViewById(R.id.image))
                    .setBackgroundResource(R.drawable.tel_img);
            localObject = (TextView) ((LinearLayout) localObject)
                    .findViewById(R.id.text);
            ((TextView) localObject).setAutoLinkMask(4);
            String str = this.detail.telno;
            String[] arrayOfString = str.split(" ");
            if ((arrayOfString == null) || (arrayOfString.length <= 0)) {
                ((TextView) localObject).setText(str);
            } else {
                StringBuilder localStringBuilder = new StringBuilder();
                for (int i = 0; i < arrayOfString.length; i++) {
                    localStringBuilder.append(arrayOfString[i]);
                    if (i >= -1 + arrayOfString.length)
                        continue;
                    localStringBuilder.append(";");
                }
                ((TextView) localObject).setText(localStringBuilder.toString());
            }
        }
    }

    private void initView() {
        initCouponDetail();
        initAddress();
        initTel();
        initShare();
    }

    private void initWidget() {
        this.shopHourTv = ((TextView) findViewById(R.id.shophours));
        this.distanceTv = ((TextView) findViewById(R.id.distance));
        this.cookStyleTv = ((TextView) findViewById(R.id.cookingstyle));
        this.speciltyTv = ((TextView) findViewById(R.id.specilty));
        this.consumelevTv = ((TextView) findViewById(R.id.consumelevel));
        this.buslineTv = ((TextView) findViewById(R.id.busline));
        this.storeIntroTv = ((TextView) findViewById(R.id.storeintro));
    }

    private void requestCoupon() {
        this.handler.sendEmptyMessage(1);
    }

    public boolean isEmpty() {
        return this.first;
    }

    public boolean isReady() {
        return this.ready;
    }

    public void onCreate(CouponDetail paramCouponDetail, Showable paramShowable) {
        if (this.first) {
            this.first = false;
            this.detail = paramCouponDetail;
            initWidget();
            initView();
            requestCoupon();
        }
    }

    public void onCreate(CouponDetail paramCouponDetail,
            final Showable paramShowable,
            final CouponDetailView.Listener paramListener, final int paramInt,
            boolean paramBoolean) {
        if (!this.startLoad) {
            this.startLoad = true;
            ProtocolHelper.requestShopDetail(ActivityManager.getCurrent(),
                    paramCouponDetail.id).startTrans(
                    new Protocol.OnJsonProtocolResult(CouponDetail.class) {
                        public void onException(String paramString,
                                Exception paramException) {
                            StoreDetailView.this.ready = true;
                            LogUtils.e("test", "", paramException);
                        }

                        public void onResult(String paramString,
                                Object paramObject) {
                            StoreDetailView.this.ready = true;
                            paramListener.onComplete(paramInt);
                            if (paramObject != null) {
                                StoreDetailView.this.detail = ((CouponDetail) paramObject);
                                StoreDetailView.this.handler
                                        .post(new Runnable() {
                                            public void run() {
                                                StoreDetailView.this
                                                        .onCreate(
                                                                StoreDetailView.this.detail,
                                                                paramShowable);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    public void onDestroy() {
    }
}
