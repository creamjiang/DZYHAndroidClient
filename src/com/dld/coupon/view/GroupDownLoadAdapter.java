package com.dld.coupon.view;

import android.view.View;

import com.dld.protocol.json.Group;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.JsonBean;

import java.util.List;

public abstract class GroupDownLoadAdapter extends JsonBeanDownLoadAdapter {
    public GroupDownLoadAdapter(DownloadListView paramDownloadListView,
            Group paramGroup) {
        super(paramDownloadListView, paramGroup);
    }

    public View getView(int paramInt) {
        return groupDetailToView((GroupDetail) this.bean.getDetails().get(
                paramInt));
    }
}
