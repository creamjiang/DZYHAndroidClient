package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Callback;
import com.dld.android.net.Http;
import com.dld.android.net.HttpConfig;
import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.LoginActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.SendWeiboUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.Tag;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Ticket;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketView extends LinearLayout {
    private DetailContainer container;
    private boolean first = true;

    public TicketView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private String getContent(Ticket paramTicket) {
        String str = "优惠券：" + paramTicket.title
                + "，打折、优惠，从此告别打印时代，电子优惠券更靠谱！来源：【打折店优惠】";
        if (!StringUtils.isEmpty(paramTicket.branch_name))
            str = paramTicket.branch_name + "提供" + str;
        return str + " http://www.dld.com/index/list/" + paramTicket.id;
    }

    private String getContentforZuju(Ticket paramTicket) {
        String str = "优惠券：" + paramTicket.title
                + "，打折、优惠，从此告别打印时代，电子优惠券更靠谱！有空没空聚一聚，一起去吧！来源：【打折店优惠】";
        if (!StringUtils.isEmpty(paramTicket.branch_name))
            str = paramTicket.branch_name + "提供" + str;
        return str + " http://www.dld.com/index/list/" + paramTicket.id;
    }

    private void initFavButton(final Ticket paramTicket, Showable paramShowable) {
        Object localObject = (Button) findViewById(R.id.favorite_button);
        if (Cache.remove("fav") != null) {
            this.container.init(true);
            ((Button) localObject).setVisibility(8);
            localObject = findViewById(R.id.ticket_title).getLayoutParams();
            ((ViewGroup.LayoutParams) localObject).width = -1;
            findViewById(R.id.ticket_title).setLayoutParams(
                    (ViewGroup.LayoutParams) localObject);
        } else {
            ((Button) localObject)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            final Object localObject1 = SharePersistent
                                    .getInstance().getPerference(
                                            ActivityManager.getCurrent(),
                                            "customer_id");
                            if (!StringUtils.isEmpty((String) localObject1)) {
                                Object localObject2 = GenericDAO
                                        .getInstance(ActivityManager
                                                .getCurrent());
                                if (!((GenericDAO) localObject2).containsFav(4,
                                        paramTicket)) {
                                    String str1 = "http://www.dld.com/vlife/image?id="
                                            + paramTicket.id;
                                    ((GenericDAO) localObject2).saveFav(4,
                                            paramTicket);
                                    Toast.makeText(
                                            ActivityManager.getCurrent(),
                                            "已添加到收藏夹", 0).show();
                                    new Http(
                                            new TicketView.ImageCallback(str1),
                                            new HttpConfig(10, false)).start();
                                    if (SharePersistent.getInstance()
                                            .getPerferenceBoolean(
                                                    ActivityManager
                                                            .getCurrent(),
                                                    "isallowsendweibo")) {
                                        localObject2 = TicketView.this
                                                .getContent(paramTicket);
                                        str1 = "http://www.dld.com/vlife/image?id="
                                                + paramTicket.id;
                                        String str2 = SharePersistent
                                                .getInstance().getPerference(
                                                        ActivityManager
                                                                .getCurrent(),
                                                        "weibotype");
                                        SendWeiboUtils.getInstance(
                                                ActivityManager.getCurrent())
                                                .sendWeibo(str2,
                                                        (String) localObject2,
                                                        str1, 2);
                                    }
                                    new Thread(new Runnable() {
                                        public void run() {
                                            Param localParam = new Param();
                                            localParam
                                                    .append("userId",
                                                            (String) localObject1)
                                                    .append("resourceId",
                                                            String.valueOf(paramTicket.id))
                                                    .append("type", "2")
                                                    .append("description",
                                                            paramTicket.branch_id);
                                            ProtocolHelper.storeUpRequest(
                                                    ActivityManager
                                                            .getCurrent(),
                                                    localParam, false)
                                                    .startTrans();
                                        }
                                    }).start();
                                } else {
                                    Toast.makeText(
                                            ActivityManager.getCurrent(),
                                            "该优惠券已存在", 0).show();
                                }
                            } else {
                                Intent intent = new Intent(ActivityManager
                                        .getCurrent(), LoginActivity.class);
                                ActivityManager.getCurrent()
                                        .startActivityForResult(intent, 0);
                            }
                        }
                    });
        }
    }

    private void initImage(Ticket paramTicket, boolean paramBoolean) {
        ImageView localImageView = (ImageView) findViewById(R.id.image);
        String str = "http://www.dld.com/vlife/image?id=" + paramTicket.id;
        if (!paramBoolean)
            new ImageProtocol(ActivityManager.getCurrent(),
                    "http://www.dld.com/tuan/viewimage?u="
                            + URLEncoder.encode(str) + "&w=140&h=200")
                    .startTrans(localImageView);
        else
            FileUtil.readImage(localImageView, str, 140, 200);
    }

    private void initIntroduction(Ticket paramTicket) {
        TextView localTextView = (TextView) findViewById(R.id.description);
        if (StringUtils.isEmpty(paramTicket.description))
            paramTicket.description = "该店铺优惠券到店出示电子版本即可";
        localTextView.setVisibility(0);
        localTextView.setText(paramTicket.description);
    }

    private void initOther(final Ticket paramTicket) {
        ((TextView) findViewById(R.id.ticket_title)).setText(StringUtils
                .breakLines(paramTicket.title));
        if (!StringUtils.isEmpty(paramTicket.address)) {
            ((TextView) findViewById(R.id.shop_address)).setText(StringUtils
                    .breakLines(paramTicket.address));
        } else {
            findViewById(R.id.distance_image).setVisibility(View.GONE);
            findViewById(R.id.shop_address).setVisibility(8);
        }
        ((TextView) findViewById(R.id.time)).setText("有效期至 "
                + new SimpleDateFormat("yyyy-M-d").format(new Date(
                        paramTicket.end_time)));
        if (!StringUtils.isEmpty(paramTicket.telno)) {
            Matcher localMatcher = Pattern.compile("[0-9\\-]+").matcher(
                    paramTicket.telno);
            StringBuilder localStringBuilder = new StringBuilder();
            while (localMatcher.find()) {
                if (localStringBuilder.length() > 0)
                    localStringBuilder.append(";");
                localStringBuilder.append(localMatcher.group());
            }
            if (localStringBuilder.length() != 0)
                ((TextView) findViewById(R.id.shop_tel))
                        .setText(localStringBuilder.toString());
            else
                findViewById(R.id.tel_image).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tel_image).setVisibility(View.GONE);
        }
        findViewById(R.id.shop_tel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.call(paramTicket.telno);
                    }
                });
        findViewById(R.id.email_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.goSmsActivity(
                                TicketView.this.getContentforZuju(paramTicket)
                                        + " http://c.dld.com",
                                ActivityManager.getCurrent());
                    }
                });
        ((TextView) findViewById(R.id.source)).setText("来自"
                + paramTicket.source);
    }

    private void initShareButton(final Ticket paramTicket) {
        ((Button) findViewById(R.id.share_button))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Cache.put("weibo_content",
                                TicketView.this.getContent(paramTicket));
                        Cache.put("weibo_ticket_id",
                                Integer.valueOf(paramTicket.id));
                        Common.share();
                    }
                });
    }

    private void initZoom(final Ticket paramTicket) {
        View.OnClickListener local1 = new View.OnClickListener() {
            public void onClick(View paramView) {
                TicketView.this.container.loadImage(paramTicket.id);
            }
        };
        findViewById(R.id.image).setOnClickListener(local1);
        findViewById(R.id.zoom_image).setOnClickListener(local1);
    }

    public void onCreate(View paramView, Ticket paramTicket,
            Showable paramShowable, boolean paramBoolean) {
        if (this.first) {
            this.container = ((DetailContainer) paramView);
            initImage(paramTicket, paramBoolean);
            initZoom(paramTicket);
            initIntroduction(paramTicket);
            initFavButton(paramTicket, paramShowable);
            initShareButton(paramTicket);
            initOther(paramTicket);
        }
    }

    public void onDestroy() {
        if (!this.first) {
            ImageView localImageView = (ImageView) findViewById(R.id.image);
            if (localImageView.getTag() == Tag.IMG_CHANGED)
                ((BitmapDrawable) localImageView.getDrawable()).getBitmap()
                        .recycle();
            localImageView.setTag(Tag.IMG_RECYLED);
        }
    }

    private class ImageCallback extends Callback {
        private String fullUrl;
        private String url;

        public ImageCallback(String arg2) {
            this.custom = true;
            String str;
            this.url = arg2;
            this.fullUrl = ("http://www.dld.com/tuan/viewimage?u="
                    + URLEncoder.encode(arg2) + "&w=600&h=600");
        }

        public String getUrl() {
            return this.url;
        }

        public void onException(Exception paramException) {
            LogUtils.e("test", "", paramException);
        }

        public void onRecieve(byte[] paramArrayOfByte) {
            try {
                if (paramArrayOfByte.length > 100) {
                    Cache.put(this.fullUrl, paramArrayOfByte);
                    FileUtil.saveTicketImage(this.url);
                } else {
                    onException(new Exception("Image too small."));
                }
            } catch (Exception localException) {
                onException(localException);
            }
        }
    }
}
