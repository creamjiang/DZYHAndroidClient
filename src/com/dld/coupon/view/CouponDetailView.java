package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CouponDetailScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NestedScrollView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.BankCouponDetailActivity;
import com.dld.coupon.activity.BaseActivity;
import com.dld.coupon.activity.BranchActivity;
import com.dld.coupon.activity.CommentActivity;
import com.dld.coupon.activity.FeedbackActivity;
import com.dld.coupon.activity.GroupDetailActivity;
import com.dld.coupon.activity.LoginActivity;
import com.dld.coupon.activity.ShowCommentActivity;
import com.dld.coupon.activity.TicketActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.SendWeiboUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.Tag;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.BankCoupon;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Comment;
import com.dld.protocol.json.CommentResponse;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.Group;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Ticket;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
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

public class CouponDetailView extends DetailView {
    private final int GET_COMMENTEXCEPTION = 3;
    private final int GET_NEWACCESS = 1;
    private final int NOCOMMENT = 2;
    private Activity activity = ActivityManager.getCurrent();
    private View assessLayout;
    private TextView assessNumTextView;
    private View assessView;
    private TextView contentTextView;
    private TextView dateTextView;
    private CouponDetail detail;
    private boolean first = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
            case 1:
                if (CouponDetailView.this.mComment == null)
                    break;
                CouponDetailView.this
                        .initNewComment(CouponDetailView.this.mComment);
                break;
            case 2:
                CouponDetailView.this.initNoComment();
                break;
            case 3:
                CouponDetailView.this.initNoComment();
            }
        }
    };
    private List<ImageView> imgs = new ArrayList();
    private Listener listener;
    private Comment mComment;
    private CommentResponse mCommentResponse;
    private TextView nameTextView;
    private View nameView;
    private int position;
    private RatingBar ratingBar;
    private RatingBar ratingBarAvg;
    private boolean ready = false;
    private CouponDetailScrollView scroll;
    private Showable showable;

    public CouponDetailView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void addTickets(LinearLayout paramLinearLayout,
            LayoutInflater paramLayoutInflater, int paramInt1, int paramInt2) {
        Object localObject = (NestedScrollView) findViewById(R.id.nested_scroll);
        for (int i = paramInt1; i < paramInt2; i++) {
            Ticket localTicket = (Ticket) this.detail.tickets.get(i);
            localObject = paramLayoutInflater.inflate(
                    R.layout.coupon_detail_item, null);
            ImageView localImageView = (ImageView) ((View) localObject)
                    .findViewById(R.id.ticket_image);
            this.imgs.add(localImageView);
            String str = "http://www.dld.com/vlife/image?id=" + localTicket.id;
            new ImageProtocol(this.activity,
                    "http://www.dld.com/tuan/viewimage?u="
                            + URLEncoder.encode(str) + "&w=140&h=200")
                    .startTrans(localImageView);
            ((TextView) ((View) localObject).findViewById(R.id.ticket_name))
                    .setText(StringUtils.breakLines(localTicket.title));
            ((TextView) ((View) localObject).findViewById(R.id.ticket_deadline))
                    .setText(new SimpleDateFormat("截止时间：yyyy年M月d日")
                            .format(new Date(localTicket.end_time)));
            ((TextView) ((View) localObject).findViewById(R.id.ticket_source))
                    .setText("来源：" + localTicket.source);
            final int j = i;
            ((View) localObject).setOnTouchListener(new OnTouchListenerImpl(
                    (View) localObject));
            ((View) localObject).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    Cache.put("tickets", CouponDetailView.this.detail.tickets);
                    Cache.put("position", Integer.valueOf(j));
                    Cache.put("showable", CouponDetailView.this.showable);
                    Intent localIntent = new Intent();
                    localIntent.setClass(CouponDetailView.this.activity,
                            TicketActivity.class);
                    CouponDetailView.this.activity.startActivity(localIntent);
                }
            });
            paramLinearLayout.addView((View) localObject);
            if (i == paramInt2 - 1)
                continue;
            paramLinearLayout.addView(paramLayoutInflater.inflate(
                    R.layout.list_seperator, null));
        }
        if (paramInt2 <= 3)
            ((NestedScrollView) localObject).setVisibility(8);
        else
            this.scroll.setChild((ViewGroup) localObject);
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
        TextView localTextView = (TextView) findViewById(R.id.shop_address);
        localTextView.setText(Html.fromHtml("<u>"
                + StringUtils.breakLines(this.detail.address) + "</u>"));
        localTextView
                .setOnTouchListener(new OnTouchListenerImpl(localTextView));
        localTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MapUtil.openMap(CouponDetailView.this.detail.y,
                        CouponDetailView.this.detail.x,
                        CouponDetailView.this.detail.name);
            }
        });
    }

    private void initAssessWidget() {
        this.ratingBarAvg = ((RatingBar) findViewById(R.id.item_rating_avg));
        this.assessLayout = findViewById(R.id.assess_layout);
        this.assessView = findViewById(R.id.assess_view);
        this.nameView = findViewById(R.id.name_layout);
        this.assessNumTextView = ((TextView) findViewById(R.id.assess_num));
        this.ratingBar = ((RatingBar) findViewById(R.id.item_rating));
        this.nameTextView = ((TextView) findViewById(R.id.name));
        this.contentTextView = ((TextView) findViewById(R.id.content));
        this.dateTextView = ((TextView) findViewById(R.id.date));
        this.assessLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Object localObject = (String) CouponDetailView.this.contentTextView
                        .getTag();
                LogUtils.log("main", "storeId="
                        + CouponDetailView.this.detail.id);
                if (!"no_comment".equals(localObject)) {
                    Cache.put("coupondetail", CouponDetailView.this);
                    localObject = new Intent(CouponDetailView.this.activity,
                            ShowCommentActivity.class);
                    ((Intent) localObject).putExtra("shop_id",
                            CouponDetailView.this.detail.id);
                    CouponDetailView.this.activity
                            .startActivity((Intent) localObject);
                } else if (!StringUtils.isEmpty(CouponDetailView.this
                        .getUserId())) {
                    localObject = new Intent(CouponDetailView.this.activity,
                            CommentActivity.class);
                    ((Intent) localObject).putExtra("shop_id",
                            CouponDetailView.this.detail.id);
                    CouponDetailView.this.activity
                            .startActivity((Intent) localObject);
                    Cache.put("coupondetail", CouponDetailView.this);
                } else {
                    localObject = new Intent(CouponDetailView.this.activity,
                            LoginActivity.class);
                    CouponDetailView.this.activity
                            .startActivity((Intent) localObject);
                }
            }
        });
    }

    private void initBanksList() {
        Object localObject = (LinearLayout) findViewById(R.id.banks_layout);
        ((TextView) ((LinearLayout) localObject).findViewById(R.id.title))
                .setText("银行卡优惠");
        LinearLayout localLinearLayout = (LinearLayout) ((LinearLayout) localObject)
                .findViewById(R.id.list);
        if (!this.detail.banks.isEmpty()) {
            ((LinearLayout) localObject).setVisibility(0);
            BankCouponDetail localBankCouponDetail1 = null;
            final int i = this.detail.banks.size();
            while ((localBankCouponDetail1.dbId < i)
                    && (localBankCouponDetail1.distance <= 5)) {
                BankCouponDetail localBankCouponDetail2 = (BankCouponDetail) this.detail.banks
                        .get(localBankCouponDetail1.dbId);
                LayoutInflater localLayoutInflater = LayoutInflater
                        .from(this.activity);
                localObject = localLayoutInflater.inflate(
                        R.layout.coupon_detail_item, null);
                ((ImageView) ((View) localObject)
                        .findViewById(R.id.ticket_image))
                        .setImageResource(localBankCouponDetail2.getImage());
                ((TextView) ((View) localObject).findViewById(R.id.ticket_name))
                        .setText(StringUtils
                                .breakLines(localBankCouponDetail2.discount));
                ((TextView) ((View) localObject)
                        .findViewById(R.id.ticket_deadline))
                        .setText(new SimpleDateFormat("截止时间：yyyy年M月d日")
                                .format(new Date(
                                        1000L * localBankCouponDetail2.end)));
                ((TextView) ((View) localObject)
                        .findViewById(R.id.ticket_source))
                        .setText(localBankCouponDetail2.getBank());
                localBankCouponDetail2 = localBankCouponDetail1;
                ((View) localObject)
                        .setOnTouchListener(new OnTouchListenerImpl(
                                (View) localObject));
                ((View) localObject)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                Object localObject = new BankCoupon();
                                ((BankCoupon) localObject).total = CouponDetailView.this.detail.banks
                                        .size();
                                ((BankCoupon) localObject).details
                                        .addAll(CouponDetailView.this.detail.banks);
                                Cache.put("coupon", localObject);
                                Cache.put("position", Integer.valueOf(i));
                                localObject = new Intent();
                                ((Intent) localObject).setClass(
                                        CouponDetailView.this.activity,
                                        BankCouponDetailActivity.class);
                                CouponDetailView.this.activity
                                        .startActivity((Intent) localObject);
                            }
                        });
                localLinearLayout.addView((View) localObject);
                if (localBankCouponDetail1.dbId != i - 1)
                    localLinearLayout.addView(localLayoutInflater.inflate(
                            R.layout.list_seperator, null));
                localBankCouponDetail1.dbId++;
            }
        }
        ((LinearLayout) localObject).setVisibility(8);
    }

    private void initCouponDetail() {
        ((TextView) findViewById(R.id.shop_name)).setText(this.detail.name);
    }

    private void initCouponsList() {
        LinearLayout localLinearLayout1 = (LinearLayout) findViewById(R.id.coupons_layout);
        ((TextView) localLinearLayout1.findViewById(R.id.title))
                .setText("优惠券列表");
        LinearLayout localLinearLayout2;
        if (this.detail.tickets.size() <= 3) {
            localLinearLayout2 = (LinearLayout) localLinearLayout1
                    .findViewById(R.id.list2);
            localLinearLayout2.setVisibility(0);
        } else {
            localLinearLayout2 = (LinearLayout) localLinearLayout1
                    .findViewById(R.id.list);
        }
        if (!this.detail.tickets.isEmpty()) {
            localLinearLayout1.setVisibility(0);
            addTickets(localLinearLayout2, LayoutInflater.from(this.activity),
                    0, this.detail.tickets.size());
        } else {
            localLinearLayout1.setVisibility(8);
        }
    }

    private void initGroupsList() {
        Object localObject1 = (LinearLayout) findViewById(R.id.groups_layout);
        ((TextView) ((LinearLayout) localObject1).findViewById(R.id.title))
                .setText("团购列表");
        LinearLayout localLinearLayout = (LinearLayout) ((LinearLayout) localObject1)
                .findViewById(R.id.list);
        if (!this.detail.groups.isEmpty()) {
            ((LinearLayout) localObject1).setVisibility(0);
            int localObject2 = 0;
            final int i = this.detail.groups.size();
            while ((localObject2 < i) && (localObject2 <= 5)) {
                Object localObject3 = (GroupDetail) this.detail.groups
                        .get(localObject2);
                LayoutInflater localLayoutInflater = LayoutInflater
                        .from(this.activity);
                localObject1 = localLayoutInflater.inflate(
                        R.layout.coupon_detail_item, null);
                Object localObject4 = (ImageView) ((View) localObject1)
                        .findViewById(R.id.ticket_image);
                this.imgs.add((ImageView) localObject4);
                String str = ((GroupDetail) localObject3).image;
                new ImageProtocol(this.activity,
                        "http://www.dld.com/tuan/viewimage?u="
                                + URLEncoder.encode(str) + "&w=140&h=200")
                        .startTrans((ImageView) localObject4);
                ((TextView) ((View) localObject1)
                        .findViewById(R.id.ticket_name)).setText(StringUtils
                        .breakLines(((GroupDetail) localObject3).title));
                ((TextView) ((View) localObject1)
                        .findViewById(R.id.ticket_deadline))
                        .setText(new SimpleDateFormat("截止时间：yyyy年M月d日")
                                .format(new Date(
                                        1000L * ((GroupDetail) localObject3).endTime)));
                ((TextView) ((View) localObject1)
                        .findViewById(R.id.ticket_source)).setText("来源："
                        + ((GroupDetail) localObject3).website);
                localObject3 = localObject2;
                localObject4 = new OnTouchListenerImpl((View) localObject1);
                ((View) localObject1)
                        .setOnTouchListener((View.OnTouchListener) localObject4);
                localObject3 = new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Object localObject = new Group();
                        ((Group) localObject).total = CouponDetailView.this.detail.groups
                                .size();
                        ((Group) localObject).details
                                .addAll(CouponDetailView.this.detail.groups);
                        Cache.put("group", localObject);
                        Cache.put("position", Integer.valueOf(i));
                        localObject = new Intent();
                        ((Intent) localObject).setClass(
                                CouponDetailView.this.activity,
                                GroupDetailActivity.class);
                        CouponDetailView.this.activity
                                .startActivity((Intent) localObject);
                    }
                };
                ((View) localObject1)
                        .setOnClickListener((View.OnClickListener) localObject3);
                localLinearLayout.addView((View) localObject1);
                if (localObject2 != i - 1)
                    localLinearLayout.addView(localLayoutInflater.inflate(
                            R.layout.list_seperator, null));
                localObject2++;
            }
        }
        ((LinearLayout) localObject1).setVisibility(8);
    }

    private void initList() {
        initCouponsList();
        initGroupsList();
        initBanksList();
    }

    private void initNewComment(Comment paramComment) {
        this.assessView.setVisibility(0);
        this.nameView.setVisibility(0);
        this.ratingBarAvg.setVisibility(0);
        this.ratingBarAvg.setRating(this.mCommentResponse.avgStar);
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("评价").append(" ").append("(").append("共")
                .append(this.mCommentResponse.total).append("条").append(")");
        this.assessNumTextView.setText(localStringBuffer.toString());
        this.ratingBar.setRating(paramComment.starNum);
        this.nameTextView.setText(paramComment.name);
        this.contentTextView.setText(paramComment.content);
        this.contentTextView.setTag("has_comment");
        this.dateTextView.setText(paramComment.date);
    }

    private void initNoComment() {
        this.assessView.setVisibility(0);
        if (this.mCommentResponse != null) {
            this.ratingBarAvg.setVisibility(0);
            this.ratingBarAvg.setRating(this.mCommentResponse.avgStar);
        }
        this.nameView.setVisibility(8);
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("评价").append(" ").append("(").append("共")
                .append(0).append("条").append(")");
        this.assessNumTextView.setText(localStringBuffer.toString());
        this.contentTextView.setTag("no_comment");
        this.contentTextView.setText("还没有人评论，给个评论吧!");
    }

    private void initShare() {
        if (this.detail.shop_id > 0)
            findViewById(R.id.shop_branch_button).setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View paramView) {
                            Cache.put(
                                    "shopid",
                                    Integer.valueOf(CouponDetailView.this.detail.shop_id));
                            Cache.put("detail_activity", CouponDetailView.this);
                            Cache.put("activity",
                                    CouponDetailView.this.activity);
                            Intent localIntent = new Intent();
                            localIntent.setClass(
                                    CouponDetailView.this.activity,
                                    BranchActivity.class);
                            CouponDetailView.this.activity
                                    .startActivity(localIntent);
                        }
                    });
        findViewById(R.id.weibo_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Cache.put("weibo_content",
                                CouponDetailView.this.getContent());
                        Common.share();
                    }
                });
        Button localButton = (Button) findViewById(R.id.favorite_button);
        if ((Cache.remove("fav") != null) || (!localButton.isEnabled())) {
            localButton.setBackgroundResource(R.drawable.fav_del);
            localButton.setEnabled(false);
        } else {
            localButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    final Object localObject1 = SharePersistent.getInstance()
                            .getPerference(CouponDetailView.this.activity,
                                    "customer_id");
                    if (!StringUtils.isEmpty((String) localObject1)) {
                        Object localObject2 = GenericDAO
                                .getInstance(CouponDetailView.this.activity);
                        if (!((GenericDAO) localObject2).containsFav(1,
                                CouponDetailView.this.detail)) {
                            ((GenericDAO) localObject2).saveFav(1,
                                    CouponDetailView.this.detail);
                            Toast.makeText(CouponDetailView.this.activity,
                                    "已添加到收藏夹", 0).show();
                            if (SharePersistent.getInstance()
                                    .getPerferenceBoolean(
                                            CouponDetailView.this.activity,
                                            "isallowsendweibo")) {
                                localObject2 = SharePersistent.getInstance()
                                        .getPerference(
                                                CouponDetailView.this.activity,
                                                "weibotype");
                                SendWeiboUtils.getInstance(
                                        CouponDetailView.this.activity)
                                        .sendWeibo(
                                                (String) localObject2,
                                                CouponDetailView.this
                                                        .getContent(), null, 1);
                            }
                            new Thread(new Runnable() {
                                public void run() {
                                    Param localParam = new Param();
                                    localParam
                                            .append("userId",
                                                    String.valueOf(localObject1))
                                            .append("resourceId",
                                                    CouponDetailView.this.detail.id)
                                            .append("type", "0")
                                            .append("description", "3");
                                    ProtocolHelper.storeUpRequest(
                                            ActivityManager.getCurrent(),
                                            localParam, false).startTrans();
                                }
                            }).start();
                        } else {
                            Toast.makeText(CouponDetailView.this.activity,
                                    "该店铺已存在", 0).show();
                        }
                    } else {
                        Intent intent = new Intent(
                                CouponDetailView.this.activity,
                                LoginActivity.class);
                        CouponDetailView.this.activity.startActivityForResult(
                                intent, 0);
                    }
                }
            });
        }
        findViewById(R.id.bottom_feedback_btn).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Intent localIntent = new Intent();
                        localIntent.setClass(ActivityManager.getCurrent(),
                                FeedbackActivity.class);
                        ActivityManager.getCurrent().startActivity(localIntent);
                    }
                });
        findViewById(R.id.email_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        Common.goSmsActivity(CouponDetailView.this.getContent()
                                + " http://c.dld.com",
                                CouponDetailView.this.activity);
                    }
                });
    }

    private void initView() {
        initCouponDetail();
        initAssessWidget();
        initAddress();
        initShare();
        initList();
        Object localObject2 = (TextView) findViewById(R.id.introduction);
        Object localObject1;
        if (!StringUtils.isEmpty(this.detail.introduction))
            localObject1 = this.detail.introduction;
        else
            localObject1 = "暂无商家简介";
        if (!StringUtils.isEmpty(this.detail.introduction)) {
            if (!this.detail.introduction.startsWith("　"))
                localObject1 = "　　"
                        + StringUtils.breakLines((String) localObject1);
        } else
            localObject1 = "暂无商家简介";
        ((TextView) localObject2).setText((CharSequence) localObject1);
        if (!StringUtils.isEmpty(this.detail.telno)) {
            localObject1 = Pattern.compile("[0-9\\-]+").matcher(
                    this.detail.telno);
            localObject2 = new StringBuilder();
            while (((Matcher) localObject1).find()) {
                if (((StringBuilder) localObject2).length() > 0)
                    ((StringBuilder) localObject2).append(";");
                ((StringBuilder) localObject2).append(((Matcher) localObject1)
                        .group());
            }
            if (((StringBuilder) localObject2).length() != 0)
                ((TextView) findViewById(R.id.shop_tel))
                        .setText(((StringBuilder) localObject2).toString());
            else
                findViewById(R.id.tel_image).setVisibility(8);
        } else {
            findViewById(R.id.tel_image).setVisibility(8);
        }
        if (this.detail.shop_id > 0) {
            localObject1 = (ImageView) findViewById(R.id.shop_image);
            localObject2 = URLEncoder
                    .encode("http://www.dld.com/vlife/image?type=1&id="
                            + this.detail.shop_id);
            localObject2 = "http://www.dld.com/tuan/viewimage?u="
                    + (String) localObject2 + "&w=200&h=200";
            new ImageProtocol(getContext(), (String) localObject2)
                    .startTrans((ImageView) localObject1);
        } else {
            findViewById(R.id.shop_branch_button).setVisibility(8);
        }
    }

    private void load(boolean paramBoolean) {
        initView();
        String str;
        if (BaseActivity.segmentPosition != 2)
            str = "detail";
        else
            str = "search_detail";
        if ((this.detail.tickets.isEmpty()) && (this.detail.banks.isEmpty())
                && (this.detail.groups.isEmpty())) {
            findViewById(R.id.assess_layout).setVisibility(0);
            ProtocolHelper.requestDetail(this.activity, this.detail.id,
                    paramBoolean, str).startTrans(new ProtocolResult());
            getCommentNew();
        }
    }

    public void getCommentNew() {
        try {
            Param localParam = new Param();
            ProtocolHelper.addComment(this.activity, this.detail.id,
                    localParam, "get_commentnew", false).startTransForUserGet(
                    new DoCommentProtocolResult(), localParam);
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    public String getUserId() {
        return SharePersistent.getInstance().getPerference(
                ActivityManager.getCurrent(), "customer_id");
    }

    public boolean isEmpty() {
        return this.first;
    }

    public boolean isReady() {
        return this.ready;
    }

    public void onCreate(CouponDetail paramCouponDetail,
            Showable paramShowable, Listener paramListener, int paramInt,
            boolean paramBoolean) {
        if (this.first) {
            this.first = false;
            this.showable = paramShowable;
            this.listener = paramListener;
            this.detail = paramCouponDetail;
            this.position = paramInt;
            this.scroll = ((CouponDetailScrollView) findViewById(R.id.scroll));
            this.scroll.setDrawingCacheEnabled(true);
            this.scroll.buildDrawingCache();
            load(paramBoolean);
        }
    }

    public void onDestroy() {
        Iterator localIterator = null;
        if (!this.first)
            localIterator = this.imgs.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ImageView localImageView = (ImageView) localIterator.next();
            if (localImageView.getTag() == Tag.IMG_CHANGED) {
                Bitmap localBitmap = ((BitmapDrawable) localImageView
                        .getDrawable()).getBitmap();
                localImageView.setImageBitmap(null);
                removeView(localImageView);
                localBitmap.recycle();
            }
            localImageView.setTag(Tag.IMG_RECYLED);
        }
    }

    private class DoCommentProtocolResult extends Protocol.OnJsonProtocolResult {
        public DoCommentProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            CouponDetailView.this.handler.sendEmptyMessage(3);
            LogUtils.log("main", "exception handler");
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                CouponDetailView.this.handler.sendEmptyMessage(3);
            } else {
                CouponDetailView.this.mCommentResponse = ((CommentResponse) paramObject);
                if (CouponDetailView.this.mCommentResponse.commentList.size() != 0) {
                    CouponDetailView.this.mComment = ((Comment) CouponDetailView.this.mCommentResponse.commentList
                            .get(0));
                    CouponDetailView.this.handler.sendEmptyMessage(1);
                } else {
                    CouponDetailView.this.handler.sendEmptyMessage(2);
                }
            }
            LogUtils.log("main", "on Result run");
        }
    }

    public static abstract interface Listener {
        public abstract void onComplete(int paramInt);
    }

    private class ProtocolResult extends Protocol.OnJsonProtocolResult {
        public ProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            CouponDetailView.this.ready = true;
            LogUtils.e("test", "", paramException);
        }

        public void onResult(String paramString, Object paramObject) {
            CouponDetailView.this.ready = true;
            CouponDetailView.this.listener
                    .onComplete(CouponDetailView.this.position);
            if (paramObject != null) {
                CouponDetailView.this.detail = ((CouponDetail) paramObject);
                CouponDetailView.this.handler.post(new Runnable() {
                    public void run() {
                        CouponDetailView.this.initView();
                    }
                });
            }
        }
    }

    public static class StoreProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public StoreProtocolResult(Class<?> paramClass) {
            super();
        }

        public void onException(String paramString, Exception paramException) {
        }

        public void onResult(String paramString, Object paramObject) {
        }
    }
}
