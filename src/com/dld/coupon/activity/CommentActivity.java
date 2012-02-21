package com.dld.coupon.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.FlowLayout;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CouponDetailView;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Comment;
import com.dld.protocol.json.CommentResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class CommentActivity extends BaseActivity implements
        View.OnClickListener {
    private final int ACCESSEXCEPTION = 5;
    private final int ACCESSFAILURE = 4;
    private final int ACCESSSUCCESS = 3;
    private final int GETCOMMENTLISTFAILURE = 2;
    private final int GETCOMMENTLISTSUCCESS = 1;
    private List<Comment> commentList;
    private CommentResponse commentResponse;
    private EditText contentEt;
    private CouponDetailView couponDetailView;
    private Handler handler;
    private RelativeLayout linearLayout;
    private RatingBar mRatingBar;
    private ProgressDialog pd;
    private String shop_id;
    private String[] strArray;
    private Button submitButton;

    public CommentActivity() {
        String[] arrayOfString = new String[15];
        arrayOfString[0] = "好喜欢哦";
        arrayOfString[1] = "去了,感觉不错";
        arrayOfString[2] = "给力";
        arrayOfString[3] = "我去试试";
        arrayOfString[4] = "和宣传的不符啊";
        arrayOfString[5] = "还有意外惊喜哦";
        arrayOfString[6] = "经济实惠";
        arrayOfString[7] = "够贵的";
        arrayOfString[8] = "一般";
        arrayOfString[9] = "差劲死了";
        arrayOfString[10] = "物美价廉";
        arrayOfString[11] = "便宜,但东西一般";
        arrayOfString[12] = "贵点儿,但东西还不错";
        arrayOfString[13] = "下次还来";
        arrayOfString[14] = "再也不来了";
        this.strArray = arrayOfString;
        this.handler = new Handler() {
            public void handleMessage(Message paramMessage) {
                switch (paramMessage.what) {
                case 2:
                default:
                case 1:
                case 3:
                case 4:
                case 5:
                }
                // while (true)
                {
                    // return;
                    FlowLayout localFlowLayout = new FlowLayout(
                            CommentActivity.this);
                    StringBuffer localStringBuffer = new StringBuffer();
                    int i = 0;
                    if (i >= CommentActivity.this.strArray.length) {
                        CommentActivity.this.linearLayout.setVisibility(0);
                        CommentActivity.this.linearLayout.addView(
                                localFlowLayout, new LinearLayout.LayoutParams(
                                        -1, -1));
                        // continue;
                    }
                    TextView localTextView = new TextView(CommentActivity.this);
                    localTextView.setOnClickListener(CommentActivity.this);
                    localTextView.setPadding(5, 5, 20, 5);
                    localStringBuffer.setLength(0);
                    switch (i) {
                    default:
                        localTextView.setTag(String.valueOf(14));
                        localTextView.setBackgroundResource(2131230754);
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    }
                    // while (true)
                    {
                        localTextView.setText(CommentActivity.this.strArray[i]);
                        localTextView.setTextColor(-1);
                        localTextView.setTextSize(18.0F);
                        localTextView.setSingleLine(true);
                        localFlowLayout.addView(localTextView,
                                new FlowLayout.LayoutParams(2, 2));
                        i++;
                        // break;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_1);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_2);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_3);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_4);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_5);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_6);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_7);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_8);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_9);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_10);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_11);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_12);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_13);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_14);
                        // continue;
                        localTextView.setTag(String.valueOf(i));
                        localTextView.setBackgroundResource(R.color.color_15);
                    }
                    try {
                        CommentActivity.this.pd.dismiss();
                        new AlertDialog.Builder(CommentActivity.this)
                                .setTitle("提示")
                                .setMessage("评论成功")
                                .setIcon(R.drawable.infoicon)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                Intent localIntent = new Intent(
                                                        CommentActivity.this,
                                                        ShowCommentActivity.class);
                                                CommentActivity.this.setResult(
                                                        1, localIntent);
                                                CommentActivity.this.finish();
                                            }
                                        }).show();
                        if (CommentActivity.this.couponDetailView == null)
                            // continue;
                            CommentActivity.this.couponDetailView
                                    .getCommentNew();
                    } catch (Exception localException1) {
                    }
                    // continue;
                    try {
                        CommentActivity.this.pd.dismiss();
                        new AlertDialog.Builder(CommentActivity.this)
                                .setTitle("提示")
                                .setMessage(
                                        CommentActivity.this.commentResponse.message)
                                .setIcon(R.drawable.infoicon)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                CommentActivity.this.finish();
                                            }
                                        }).show();
                    } catch (Exception localException2) {
                    }
                    // continue;
                    try {
                        CommentActivity.this.pd.dismiss();
                        new AlertDialog.Builder(CommentActivity.this)
                                .setTitle("提示")
                                .setMessage("由于网络原因评价失败，请您稍候再试!")
                                .setIcon(R.drawable.infoicon)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                CommentActivity.this.finish();
                                            }
                                        }).show();
                    } catch (Exception localException3) {
                    }
                }
            }
        };
    }

    private float getCommentStar() {
        return this.mRatingBar.getRating();
    }

    private void initTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("评价");
    }

    private void sendComment(String paramString) {
        String str = getUserId();
        if (StringUtils.isEmpty(str))
            startActivity(new Intent(this, LoginActivity.class));
        // while (true)
        {
            // return;
            this.pd.show();
            int i = (int) getCommentStar();
            try {
                Param localParam = new Param();
                localParam.append("cid", str).append("content", paramString)
                        .append("star", String.valueOf(i));
                CProtocol localCProtocol = ProtocolHelper.addComment(this,
                        this.shop_id, localParam, "add_newcomment", false);
                Cache.remove(localCProtocol.getAbsoluteUrl());
                localCProtocol.startTransForUser(new DoAccessProtocolResult(),
                        localParam);
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
            }
        }
    }

    public String getUserId() {
        return SharePersistent.getInstance().getPerference(
                ActivityManager.getCurrent(), "customer_id");
    }

    public void init() {
        setContentView(R.layout.comment);
        initTitle();
        initWidget();
        Object localObject = Cache.remove("coupondetail");
        if (localObject == null) {
            LogUtils.log("main", "detail is  null in commentActivity");
        } else {
            LogUtils.log("main", "detail is not null in commentActivity");
            this.couponDetailView = ((CouponDetailView) localObject);
        }
    }

    public void initWidget() {
        this.mRatingBar = ((RatingBar) findViewById(R.id.edit_ratingBar));
        this.linearLayout = ((RelativeLayout) findViewById(R.id.content));
        this.contentEt = ((EditText) findViewById(R.id.content_et));
        this.submitButton = ((Button) findViewById(R.id.do_summit));
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                String str = CommentActivity.this.contentEt.getText()
                        .toString();
                CommentActivity.this.sendComment(str);
            }
        });
        this.pd = DialogHelper.getProgressBarNoShow("正在发送评价信息，请稍候...");
    }

    public void onClick(View paramView) {
        try {
            int i = Integer.parseInt((String) paramView.getTag());
            Object localObject = new StringBuffer();
            String str = this.contentEt.getText().toString();
            if (!StringUtils.isEmpty(str)) {
                ((StringBuffer) localObject).append(str);
                ((StringBuffer) localObject).append(",");
            }
            ((StringBuffer) localObject).append(this.strArray[i]);
            localObject = ((StringBuffer) localObject).toString();
            this.contentEt.setText((CharSequence) localObject);
            if (!StringUtils.isEmpty((String) localObject))
                this.contentEt.setSelection(((String) localObject).length());
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("main", localException);
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.shop_id = getIntent().getStringExtra("shop_id");
        init();
        this.handler.sendEmptyMessage(1);
    }

    private class DoAccessProtocolResult extends Protocol.OnJsonProtocolResult {
        public DoAccessProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            CommentActivity.this.handler.sendEmptyMessage(5);
            LogUtils.log("main", "exception handler");
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                CommentActivity.this.handler.sendEmptyMessage(5);
            } else {
                CommentActivity.this.commentResponse = ((CommentResponse) paramObject);
                if (CommentActivity.this.commentResponse.code != 0) {
                    CommentActivity.this.handler.sendEmptyMessage(4);
                } else {
                    CommentActivity.this.commentList = CommentActivity.this.commentResponse.commentList;
                    CommentActivity.this.handler.sendEmptyMessage(3);
                }
            }
            LogUtils.log("main", "on Result run");
        }
    }
}
