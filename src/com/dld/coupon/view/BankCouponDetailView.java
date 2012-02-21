package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.LoginActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.SendWeiboUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CProtocol;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;

public class BankCouponDetailView extends LinearLayout {
    private BankCouponDetail detail;
    private boolean first = true;

    public BankCouponDetailView(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private String getContent() {
        StringBuilder localStringBuilder = new StringBuilder(this.detail.name);
        if (!StringUtils.isEmpty(this.detail.address))
            localStringBuilder.append("(" + this.detail.address);
        if (!StringUtils.isEmpty(this.detail.tel))
            localStringBuilder.append("," + this.detail.tel + ")");
        localStringBuilder.append("，有空没空聚一聚，一起去吧！来源：【打折店优惠】");
        return localStringBuilder.toString();
    }

    private void initAddress() {
        TextView localTextView = (TextView) findViewById(R.id.address);
        localTextView.setText(Html.fromHtml("<u>"
                + StringUtils.breakLines(this.detail.address) + "</u>"));
        localTextView
                .setOnTouchListener(new OnTouchListenerImpl(localTextView));
        localTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MapUtil.openMap(BankCouponDetailView.this.detail.lat,
                        BankCouponDetailView.this.detail.lng,
                        BankCouponDetailView.this.detail.name);
            }
        });
    }

    private void initButtons() {
        findViewById(R.id.share_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        StringBuffer localStringBuffer = new StringBuffer();
                        localStringBuffer
                                .append(BankCouponDetailView.this.detail.name)
                                .append("（")
                                .append(BankCouponDetailView.this.detail.address);
                        if (BankCouponDetailView.this.detail.tel != null) {
                            localStringBuffer.append(",");
                            localStringBuffer
                                    .append(BankCouponDetailView.this.detail.tel);
                        }
                        localStringBuffer
                                .append("）")
                                .append("提供 ")
                                .append(BankCouponDetailView.this.detail
                                        .getBank())
                                .append("持卡优惠，刷卡还能打折，省点儿是点儿吧！来源：【打折店优惠】");
                        Cache.put("weibo_content", localStringBuffer.toString());
                        Common.share();
                    }
                });
        findViewById(R.id.email_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.goSmsActivity(
                                BankCouponDetailView.this.getContent()
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
                        if (!((GenericDAO) localObject2).containsFav(3,
                                BankCouponDetailView.this.detail)) {
                            ((GenericDAO) localObject2).saveFav(3,
                                    BankCouponDetailView.this.detail);
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "已添加到收藏夹", 0).show();
                            if (SharePersistent.getInstance()
                                    .getPerferenceBoolean(
                                            ActivityManager.getCurrent(),
                                            "isallowsendweibo")) {
                                String str = BankCouponDetailView.this.detail.name
                                        + "（"
                                        + BankCouponDetailView.this.detail.address
                                        + "，"
                                        + BankCouponDetailView.this.detail.tel
                                        + "）"
                                        + "提供"
                                        + BankCouponDetailView.this.detail
                                                .getBank()
                                        + "持卡优惠，刷卡还能打折，省点儿是点儿吧！来源：【打折店优惠】";
                                localObject2 = SharePersistent.getInstance()
                                        .getPerference(
                                                ActivityManager.getCurrent(),
                                                "weibotype");
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
                                                    String.valueOf(localObject1))
                                            .append("resourceId",
                                                    String.valueOf(BankCouponDetailView.this.detail.id))
                                            .append("type", "5");
                                    ProtocolHelper.storeUpRequest(
                                            ActivityManager.getCurrent(),
                                            localParam, false).startTrans();
                                }
                            }).start();
                        } else {
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "该优惠信息已存在", 0).show();
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

    private void initDiscount() {
        TextView localTextView = (TextView) findViewById(R.id.couponinfo);
        String str;
        if (StringUtils.isEmpty(this.detail.discount))
            str = "暂无优惠信息";
        else
            str = StringUtils.breakLines(this.detail.discount);
        localTextView.setText("　　" + str);
    }

    private void initIntroduction() {
        TextView localTextView = (TextView) findViewById(R.id.introduction_text);
        String str;
        if (StringUtils.isEmpty(this.detail.introduction))
            str = "暂无商家简介";
        else
            str = StringUtils.breakLines(this.detail.introduction);
        localTextView.setText("　　" + str);
    }

    private void initName() {
        ((ImageView) findViewById(R.id.image)).setImageResource(this.detail
                .getImage());
        ((TextView) findViewById(R.id.detail_name)).setText(this.detail.name);
        ((TextView) findViewById(R.id.bank)).setText(this.detail.getBank());
    }

    private void initTel() {
        Object localObject1 = (LinearLayout) findViewById(R.id.detail_tel);
        if (!StringUtils.isEmpty(this.detail.tel)) {
            ((ImageView) ((LinearLayout) localObject1).findViewById(R.id.image))
                    .setBackgroundResource(R.drawable.tel_img);
            TextView localTextView = (TextView) ((LinearLayout) localObject1)
                    .findViewById(R.id.text);
            localTextView.setAutoLinkMask(4);
            Object localObject2 = this.detail.tel;
            localObject1 = ((String) localObject2).split(" ");
            if ((localObject1 == null)
                    || ((String) localObject1).getBytes().length <= 0) {
                localTextView.setText((CharSequence) localObject2);
            } else {
                localObject2 = new StringBuilder();
                for (int i = 0; i < ((String) localObject1).getBytes().length; i++) {
                    ((StringBuilder) localObject2).append(localObject1);
                    if (i >= -1 + ((String) localObject1).getBytes().length)
                        continue;
                    ((StringBuilder) localObject2).append(";");
                }
                localTextView
                        .setText(((StringBuilder) localObject2).toString());
            }
        } else {
            ((LinearLayout) localObject1).setVisibility(8);
        }
    }

    private void initView() {
        initName();
        initDiscount();
        initIntroduction();
        initAddress();
        initTel();
        initButtons();
    }

    public void onCreate(BankCouponDetail paramBankCouponDetail,
            Showable paramShowable) {
        if (this.first) {
            this.first = false;
            this.detail = paramBankCouponDetail;
            initView();
        }
    }
}
