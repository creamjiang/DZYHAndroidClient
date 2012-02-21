package com.dld.coupon.view;

import com.dld.protocol.json.JsonBean;

import java.util.List;

public abstract class JsonBeanDownLoadAdapter extends BaseDownLoadAdapter {
    protected JsonBean bean;
    protected DownloadListView list;

    public JsonBeanDownLoadAdapter(DownloadListView paramDownloadListView,
            JsonBean paramJsonBean) {
        this.list = paramDownloadListView;
        this.bean = paramJsonBean;
    }

    public Object getItem(int paramInt) {
        Object localObject;
        if (this.bean != null)
            localObject = this.bean.getDetails().get(paramInt);
        else
            localObject = null;
        return localObject;
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public int getListCount() {
        return this.bean.getDetails().size();
    }

    public int getMax() {
        return this.bean.getTotal();
    }
}
