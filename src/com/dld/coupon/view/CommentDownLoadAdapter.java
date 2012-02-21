package com.dld.coupon.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dld.protocol.json.GroupComment;
import com.dld.protocol.json.GroupCommentDetail;
import com.dld.coupon.activity.R;
import java.util.List;

public abstract class CommentDownLoadAdapter extends
        DownloadListView.DownLoadAdapter {
    protected GroupComment groupComment;
    protected DownloadListView list;

    public CommentDownLoadAdapter(DownloadListView paramDownloadListView,
            GroupComment paramGroupComment) {
        this.list = paramDownloadListView;
        this.groupComment = paramGroupComment;
    }

    public Object getItem(int paramInt) {
        GroupCommentDetail localGroupCommentDetail;
        if (this.groupComment != null)
            localGroupCommentDetail = (GroupCommentDetail) this.groupComment.details
                    .get(paramInt);
        else
            localGroupCommentDetail = null;
        return localGroupCommentDetail;
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public int getListCount() {
        return this.groupComment.details.size();
    }

    public int getMax() {
        return this.groupComment.total;
    }

    public View getView(int paramInt) {
        GroupCommentDetail localGroupCommentDetail = (GroupCommentDetail) this.groupComment.details
                .get(paramInt);
        View localView = ((LayoutInflater) getContext().getSystemService(
                "layout_inflater")).inflate(R.layout.comment_item, null);
        ((TextView) localView.findViewById(R.id.comment))
                .setText(localGroupCommentDetail.comment);
        ((TextView) localView.findViewById(R.id.user)).setText("―― "
                + localGroupCommentDetail.user);
        return localView;
    }
}
