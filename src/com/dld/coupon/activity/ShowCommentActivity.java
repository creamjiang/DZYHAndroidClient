package com.dld.coupon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CouponDetailView;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Comment;
import com.dld.protocol.json.CommentResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class ShowCommentActivity extends BaseActivity implements
        View.OnClickListener {
    private DownloadListView.DownLoadAdapter commentAdapter;
    private DownloadListView commentListView;
    private CommentResponse commentResponseOut = new CommentResponse();
    private CouponDetailView couponDetailView;
    private Button doComment;
    private Handler handler = new Handler();
    private String shop_id;

    private void init() {
        initSegment();
        initWidget();
        initAdapter();
        loadComment(1);
    }

    private void initAdapter() {
        this.commentListView = ((DownloadListView) findViewById(R.id.comment_listview));
        this.commentAdapter = new CommentListviewAdapter(this);
        this.commentListView.setAdapter(this.commentAdapter);
    }

    private void initWidget() {
        this.doComment = ((Button) findViewById(R.id.do_comment));
        this.doComment.setOnClickListener(this);
    }

    private void loadComment(int paramInt) {
        if (paramInt == 1) {
            this.commentResponseOut.commentList.clear();
            this.commentListView.reset();
        }
        Param localParam = new Param();
        localParam.append("p", String.valueOf(paramInt)).append("s", "10");
        ProtocolHelper.addComment(this, this.shop_id, localParam,
                "get_commentlist", false).startTrans(
                new Protocol.OnJsonProtocolResult() {
                    public void onException(String paramString,
                            Exception paramException) {
                        ShowCommentActivity.this.handler.post(new Runnable() {
                            public void run() {
                                ShowCommentActivity.this.commentAdapter
                                        .notifyException();
                            }
                        });
                    }

                    public void onResult(String paramString, Object paramObject) {
                        if (paramObject != null) {
                            final CommentResponse localCommentResponse = (CommentResponse) paramObject;
                            if (!localCommentResponse.commentList.isEmpty())
                                ShowCommentActivity.this.handler
                                        .post(new Runnable() {
                                            public void run() {
                                                if (!ShowCommentActivity.this.commentResponseOut.commentList
                                                        .containsAll(localCommentResponse.commentList)) {
                                                    ShowCommentActivity.this.commentResponseOut.total = localCommentResponse.total;
                                                    ShowCommentActivity.this.commentResponseOut.commentList
                                                            .addAll(localCommentResponse.commentList);
                                                    ShowCommentActivity.this.commentAdapter
                                                            .notifyDownloadBack();
                                                }
                                            }
                                        });
                            else
                                ShowCommentActivity.this.handler
                                        .post(new Runnable() {
                                            public void run() {
                                                ShowCommentActivity.this.commentAdapter
                                                        .notifyNoResult();
                                            }
                                        });
                        } else {
                            ShowCommentActivity.this.handler
                                    .post(new Runnable() {
                                        public void run() {
                                            ShowCommentActivity.this.commentAdapter
                                                    .notifyException();
                                        }
                                    });
                        }
                    }
                });
    }

    private void skipToCommentActivity() {
        if (!StringUtils.isEmpty(getUserId())) {
            Intent localIntent = new Intent(this, CommentActivity.class);
            localIntent.putExtra("shop_id", this.shop_id);
            startActivityForResult(localIntent, 1);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public View getItemView(LayoutInflater paramLayoutInflater,
            Comment paramComment) {
        View localView = paramLayoutInflater.inflate(
                R.layout.show_comment_item, null);
        ((TextView) localView.findViewById(R.id.name))
                .setText(paramComment.name);
        ((RatingBar) localView.findViewById(R.id.item_rating))
                .setRating(paramComment.starNum);
        ((TextView) localView.findViewById(R.id.date))
                .setText(paramComment.date);
        ((TextView) localView.findViewById(R.id.content))
                .setText(paramComment.content);
        return localView;
    }

    public String getUserId() {
        return SharePersistent.getInstance().getPerference(
                ActivityManager.getCurrent(), "customer_id");
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        if (paramInt2 == 1)
            finish();
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.do_comment:
            Cache.put("coupondetail", this.couponDetailView);
            skipToCommentActivity();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.show_comment);
        this.shop_id = getIntent().getStringExtra("shop_id");
        Log.e("main", "shopId in showComment" + this.shop_id);
        init();
        Object localObject = Cache.remove("coupondetail");
        if (localObject == null) {
            LogUtils.log("main", "detail is  null in show");
        } else {
            LogUtils.log("main", "detail is not null in show");
            this.couponDetailView = ((CouponDetailView) localObject);
        }
    }

    class CommentListviewAdapter extends DownloadListView.DownLoadAdapter {
        private CommentResponse commentResponse;
        private Context context;

        public CommentListviewAdapter(Context arg2) {
            Object localObject = null;
            this.context = (Context) localObject;
            this.commentResponse = ShowCommentActivity.this.commentResponseOut;
        }

        public Context getContext() {
            return this.context;
        }

        public Object getItem(int paramInt) {
            return this.commentResponse.commentList.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public int getListCount() {
            return this.commentResponse.commentList.size();
        }

        public int getMax() {
            LogUtils.log("main", "max==" + this.commentResponse.total);
            return this.commentResponse.total;
        }

        public View getView(int paramInt) {
            Comment localComment = (Comment) this.commentResponse.commentList
                    .get(paramInt);
            LayoutInflater localLayoutInflater = (LayoutInflater) getContext()
                    .getSystemService("layout_inflater");
            return ShowCommentActivity.this.getItemView(localLayoutInflater,
                    localComment);
        }

        public void onNotifyDownload() {
            ShowCommentActivity.this
                    .loadComment(1 + this.commentResponse.commentList.size() / 10);
        }
    }
}
