package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.GroupDetailActivity;
import com.dld.coupon.activity.CityActivity.NotifyChangeObserver;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.GroupProtocolHelper;
import com.dld.protocol.json.Group;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class GroupLinearLayout extends ListTab implements MainViewBase,
        NotifyChangeObserver {
    private DownloadListView.DownLoadAdapter adapter;
    private Group group = new Group();
    Handler handler = new Handler();
    private boolean isFirst = true;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAdapterView,
                View paramView, int paramInt, long paramLong) {
            Cache.put("group", GroupLinearLayout.this.group);
            Cache.put("position", Integer.valueOf(paramInt));
            Cache.put("protocol", GroupLinearLayout.this.protocolCreator
                    .createProtocol(GroupLinearLayout.this.activity, 3000, -1
                            + GroupLinearLayout.this.pageIndex, 10,
                            GroupLinearLayout.this.city,
                            GroupLinearLayout.this.category.id,
                            GroupLinearLayout.this.address));
            Intent localIntent = new Intent();
            localIntent.setClass(GroupLinearLayout.this.activity,
                    GroupDetailActivity.class);
            GroupLinearLayout.this.activity.startActivity(localIntent);
        }
    };
    private String lastUrl = "";
    private DownloadListView list;
    LinearLayout noResultView;
    private ProtocolCreator protocolCreator;

    public GroupLinearLayout(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        GroupLinearLayout.this.request(true);
                    }
                });
    }

    private void request(boolean paramBoolean) {
        if (!StringUtils.isEmpty(this.city)) {
            if (paramBoolean) {
                for (int i = 0; i < this.pageIndex; i++)
                    Cache.remove(this.protocolCreator.createProtocol(
                            this.activity, 3000, i, 10, this.city,
                            this.category.id, this.address).getAbsoluteUrl());
                this.pageIndex = 1;
            }
            LogUtils.log("GroupLinearLayout", "false");
            if (this.pageIndex == 1) {
                LogUtils.log("GroupLinearLayout", "group.details.clear");
                this.group.details.clear();
                this.list.reset();
            }
            Protocol localProtocol = this.protocolCreator.createProtocol(
                    this.activity, 3000, -1 + this.pageIndex, 10, this.city,
                    this.category.id, this.address);
            this.lastUrl = localProtocol.getAbsoluteUrl();
            localProtocol.startTrans(new RecommendProtocolResult());
        }
    }

    protected void init() {
        init(this.protocolCreator, null, false, 255);
    }

    protected void init(ProtocolCreator paramProtocolCreator,
            String paramString, boolean paramBoolean, int paramInt) {
        if (paramProtocolCreator != null)
            this.protocolCreator = paramProtocolCreator;
        else
            this.protocolCreator = new ProtocolCreator() {
                public Protocol createProtocol(Context paramContext,
                        int paramInt1, int paramInt2, int paramInt3,
                        String paramString1, int paramInt4, String paramString2) {
                    return GroupProtocolHelper.recommendByCity(paramContext,
                            paramInt4, paramInt2, paramString2);
                }
            };
        this.city = SharePersistent.getInstance().getPerference(this.activity,
                "city_name");
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(this.itemClickListener);
        this.adapter = new ListViewAdapter(this.list, this.group);
        this.list.setAdapter(this.adapter);
        initAddressList(paramString, paramBoolean);
        initCategoryList(paramInt);
        initRefresh();
        request();
    }

    public void onChangeCity(String paramString1, String paramString2) {
        if ((this.showing) && (!StringUtils.isEmpty(paramString1))) {
            if (StringUtils.isEmpty(paramString1))
                this.city = paramString1;
            init();
        }
    }

    public void onHide() {
        this.showing = false;
        hideList();
    }

    public void onShow() {
        show(null, null, false, 255);
    }

    protected void request() {
        request(false);
    }

    public void show(ProtocolCreator paramProtocolCreator, String paramString,
            boolean paramBoolean, int paramInt) {
        this.showing = true;
        String str = SharePersistent.getInstance().getPerference(this.activity,
                "city_name");
        if (!this.isFirst) {
            if ((StringUtils.isEmpty(this.city)) || (this.city.equals(str))) {
                if (StringUtils.isEmpty(str)) {
                    SharePersistent.getInstance().getPerference(this.activity,
                            "city_name");
                    this.pageIndex = 1;
                    request();
                }
            } else
                init(paramProtocolCreator, paramString, paramBoolean, paramInt);
        } else {
            init(paramProtocolCreator, paramString, paramBoolean, paramInt);
            this.isFirst = false;
        }
    }

    private class ListViewAdapter extends GroupDownLoadAdapter {
        public ListViewAdapter(DownloadListView paramGroup, Group arg3) {
            super(paramGroup, arg3);
        }

        public Context getContext() {
            return GroupLinearLayout.this.activity;
        }

        public void onNotifyDownload() {
            GroupLinearLayout.this.pageIndex = (1 + (-1 + (10 + getListCount())) / 10);
            GroupLinearLayout.this.request();
        }
    }

    public static abstract interface ProtocolCreator {
        public abstract Protocol createProtocol(Context paramContext,
                int paramInt1, int paramInt2, int paramInt3,
                String paramString1, int paramInt4, String paramString2);
    }

    private class RecommendProtocolResult extends Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        private void notifyNoResult() {
            try {
                GroupLinearLayout.this.handler.post(new Runnable() {
                    public void run() {
                        GroupLinearLayout.this.adapter.notifyNoResult();
                    }
                });
                return;
            } catch (Exception localException) {
                while (true)
                    LogUtils.e("test", "", localException);
            }
        }

        public void onException(String paramString, Exception paramException) {
            if (GroupLinearLayout.this.lastUrl.equals(paramString))
                notifyNoResult();
        }

        public void onResult(String paramString, Object paramObject) {
            if (GroupLinearLayout.this.lastUrl.equals(paramString))
                if (paramObject == null) {
                    notifyNoResult();
                } else {
                    final Group localGroup = (Group) paramObject;
                    if (localGroup.details.size() != 0)
                        GroupLinearLayout.this.handler.post(new Runnable() {
                            public void run() {
                                synchronized (GroupLinearLayout.this) {
                                    if (!GroupLinearLayout.this.group.details
                                            .containsAll(localGroup.details)) {
                                        GroupLinearLayout.this.group.total = localGroup.total;
                                        GroupLinearLayout.this.group.details
                                                .addAll(localGroup.details);
                                        GroupLinearLayout.this.adapter
                                                .notifyDownloadBack();
                                    }
                                    return;
                                }
                            }
                        });
                    else
                        notifyNoResult();
                }
        }
    }
}
