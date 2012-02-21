package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import com.dld.coupon.view.CommentDownLoadAdapter;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.GroupProtocolHelper;
import com.dld.protocol.json.GroupComment;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class GroupCommentActivity extends BaseActivity {
    private DownloadListView.DownLoadAdapter adapter;
    private Context context;
    private String dpid;
    private GroupComment groupComment = new GroupComment();
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what != 1) {
                if (paramMessage.what == 2)
                    GroupCommentActivity.this.adapter.notifyNoResult();
            } else
                GroupCommentActivity.this.adapter.notifyDownloadBack();
        }
    };
    private DownloadListView listView;
    private int pageIndex;

    private void request() {
        if (this.pageIndex == 0) {
            this.groupComment.details.clear();
            this.listView.reset();
        }
        GroupProtocolHelper
                .getComments(this.context, this.pageIndex, this.dpid)
                .startTrans(new ProtocolResult());
    }

    public void init() {
        this.listView = ((DownloadListView) findViewById(R.id.listview));
        this.adapter = new ListViewAdapter(this.listView, this.groupComment);
        this.listView.setAdapter(this.adapter);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.dpid = ((String) Cache.remove("dpid"));
        this.context = getApplicationContext();
        setContentView(R.layout.group_comment);
        init();
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        int i = 1;
        boolean bool = false;
        if (paramMenuItem.getItemId() != i)
            bool = super.onOptionsItemSelected(paramMenuItem);
        else
            ActivityManager.getCurrent().finish();
        return bool;
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        paramMenu.getItem(1).setTitle("返回详情");
        return super.onPrepareOptionsMenu(paramMenu);
    }

    protected void onStart() {
        super.onStart();
        this.pageIndex = 0;
        request();
    }

    private class ListViewAdapter extends CommentDownLoadAdapter {
        public ListViewAdapter(DownloadListView paramGroupComment,
                GroupComment arg3) {
            super(paramGroupComment, arg3);
        }

        public Context getContext() {
            return GroupCommentActivity.this.context;
        }

        public void onNotifyDownload() {
            GroupCommentActivity.this.pageIndex = (getListCount() / 5);
            GroupCommentActivity.this.request();
        }
    }

    private class ProtocolResult extends Protocol.OnJsonProtocolResult {
        public ProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            GroupCommentActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                GroupCommentActivity.this.handler.sendEmptyMessage(2);
            } else {
                GroupComment localGroupComment = (GroupComment) paramObject;
                if (localGroupComment.details.size() != 0) {
                    GroupCommentActivity.this.groupComment.details
                            .addAll(localGroupComment.details);
                    GroupCommentActivity.this.groupComment.total = localGroupComment.total;
                    GroupCommentActivity.this.handler.sendEmptyMessage(1);
                } else {
                    GroupCommentActivity.this.handler.sendEmptyMessage(2);
                }
            }
        }
    }
}
