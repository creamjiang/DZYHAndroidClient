package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.AroundActivity;
import com.dld.coupon.activity.DetailActivity;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.protocol.json.AllResult;
import com.dld.protocol.json.AllResultDetail;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.List;

public class AllLinearLayout extends ListTab implements MainViewBase {
    static final TypeImage[] TYPE_IMAGES;
    protected DownloadListView.DownLoadAdapter adapter;
    Handler handler = new Handler();
    protected boolean isFirst = true;
    private String lastUrl = "";
    private double lat = 0.0D;
    protected DownloadListView list;
    private double lng = 0.0D;
    private ProtocolCreator protocolCreator;
    private AllResult result = new AllResult();

    static {
        TypeImage[] arrayOfTypeImage = new TypeImage[4];
        arrayOfTypeImage[0] = new TypeImage(R.drawable.search_all_type_4, "商家");
        arrayOfTypeImage[1] = new TypeImage(R.drawable.search_all_type_1, "优惠");
        arrayOfTypeImage[2] = new TypeImage(R.drawable.search_all_type_2, "团购");
        arrayOfTypeImage[3] = new TypeImage(R.drawable.search_all_type_3, "卡优惠");
        TYPE_IMAGES = arrayOfTypeImage;
    }

    public AllLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        AllLinearLayout.this.request(true);
                    }
                });
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                AllResult localAllResult = new AllResult();
                localAllResult.setTotal(AllLinearLayout.this.result.total);
                Object localObject = new ArrayList();
                ((List) localObject).addAll(AllLinearLayout.this.result
                        .getDetails());
                localAllResult.setDetails((List) localObject);
                localObject = new Intent();
                ((Intent) localObject).putExtra("position", paramInt);
                ((Intent) localObject).putExtra("use_gps", false);
                Cache.put("coupon", AllLinearLayout.this.result);
                Cache.put("protocol", AllLinearLayout.this.protocolCreator
                        .createProtocol(AllLinearLayout.this.activity, 3000,
                                AllLinearLayout.this.pageIndex, 10,
                                AllLinearLayout.this.cityId,
                                AllLinearLayout.this.address,
                                AllLinearLayout.this.lat,
                                AllLinearLayout.this.lng,
                                AllLinearLayout.this.category.id));
                ((Intent) localObject).setClass(ActivityManager.getCurrent(),
                        DetailActivity.class);
                ActivityManager.getCurrent()
                        .startActivity((Intent) localObject);
            }
        };
    }

    protected void init() {
        init(this.protocolCreator, null, false, 255);
    }

    protected void init(ProtocolCreator paramProtocolCreator,
            String paramString, boolean paramBoolean, int paramInt) {
        if (paramProtocolCreator != null)
            this.protocolCreator = paramProtocolCreator;
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(getItemClickListener());
        this.adapter = new ListViewAdapter();
        this.list.setAdapter(this.adapter);
        initAddressList(paramString, paramBoolean);
        initCategoryList(paramInt);
        initRefresh();
        this.pageIndex = 1;
        request();
    }

    public void onHide() {
        this.showing = false;
        hideList();
    }

    public void onShow() {
        show(null, null, false, 0);
    }

    protected void request() {
        request(false);
    }

    protected void request(boolean paramBoolean) {
        // while (true)
        {
            try {
                if (!StringUtils.isEmpty(this.cityId))
                    ;
                // break label216;
                // break label215;
                int i = 0;
                if (i > this.pageIndex) {
                    this.pageIndex = 1;
                    if (this.pageIndex != 1)
                        return;
                    // continue;
                    if (AroundActivity.location == null)
                        return;
                    // continue;
                    this.lat = AroundActivity.location.lat;
                    this.lng = AroundActivity.location.lon;
                    this.result.details.clear();
                    this.list.reset();
                    Protocol localProtocol = this.protocolCreator
                            .createProtocol(this.activity, 3000,
                                    this.pageIndex, 10, this.cityId,
                                    this.address, this.lat, this.lng,
                                    this.category.id);
                    this.lastUrl = localProtocol.getAbsoluteUrl();
                    localProtocol.startTrans(new AllProtocolResult());
                }
            } catch (Exception localException) {
                LogUtils.e("test", localException);
            }
            int j = 1;
            Cache.remove(this.protocolCreator.createProtocol(this.activity,
                    3000, j, 10, this.cityId, this.address, this.lat, this.lng,
                    this.category.id).getAbsoluteUrl());
            // localException++;
            // continue;
            // label215: return;
            // label216: if (!paramBoolean)
            // continue;

        }
    }

    public void show(ProtocolCreator paramProtocolCreator, String paramString,
            boolean paramBoolean, int paramInt) {
        this.showing = true;
        String str = SharePersistent.getInstance().getPerference(this.activity,
                "city_id");
        if (!this.isFirst) {
            if ((StringUtils.isEmpty(this.cityId)) || (this.cityId.equals(str))) {
                if (StringUtils.isEmpty(str)) {
                    SharePersistent.getInstance().getPerference(this.activity,
                            "city_id");
                    this.pageIndex = 0;
                    request();
                }
            } else
                init(paramProtocolCreator, paramString, paramBoolean, paramInt);
        } else {
            init(paramProtocolCreator, paramString, paramBoolean, paramInt);
            this.isFirst = false;
        }
    }

    protected class AllProtocolResult extends Protocol.OnJsonProtocolResult {
        public AllProtocolResult() {
            super();
        }

        private void notifyException() {
            AllLinearLayout.this.handler.post(new Runnable() {
                public void run() {
                    AllLinearLayout.this.adapter.notifyNoResult();
                }
            });
        }

        public void onException(String paramString, Exception paramException) {
            if (AllLinearLayout.this.lastUrl.equals(paramString))
                notifyException();
        }

        public void onResult(String paramString, Object paramObject) {
            if (AllLinearLayout.this.lastUrl.equals(paramString))
                if (paramObject != null) {
                    final AllResult localAllResult = (AllResult) paramObject;
                    if (!localAllResult.details.isEmpty())
                        AllLinearLayout.this.handler.post(new Runnable() {
                            public void run() {
                                synchronized (AllLinearLayout.this) {
                                    if (!AllLinearLayout.this.result.details
                                            .containsAll(localAllResult.details)) {
                                        AllLinearLayout.this.result.details
                                                .addAll(localAllResult.details);
                                        AllLinearLayout.this.result.total = localAllResult.total;
                                        AllLinearLayout.this.adapter
                                                .notifyDownloadBack();
                                    }
                                    return;
                                }
                            }
                        });
                    else
                        notifyException();
                } else {
                    notifyException();
                }
        }
    }

    private class ListViewAdapter extends DownloadListView.DownLoadAdapter {
        private ListViewAdapter() {
        }

        public Context getContext() {
            return AllLinearLayout.this.activity;
        }

        public Object getItem(int paramInt) {
            return AllLinearLayout.this.result.details.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public int getListCount() {
            return AllLinearLayout.this.result.details.size();
        }

        public int getMax() {
            return AllLinearLayout.this.result.total;
        }

        public View getView(int paramInt) {
            Object localObject = (AllResultDetail) AllLinearLayout.this.result.details
                    .get(paramInt);
            View localView = ((LayoutInflater) getContext().getSystemService(
                    "layout_inflater")).inflate(
                    R.layout.search_all_item_layout, null);
            ((TextView) localView.findViewById(R.id.coupon_name))
                    .setText(((AllResultDetail) localObject).name);
            ((TextView) localView.findViewById(R.id.coupon_address))
                    .setText(((AllResultDetail) localObject).address);
            if (((AllResultDetail) localObject).hasCoupon)
                localView.findViewById(R.id.icon_coupon).setVisibility(
                        View.VISIBLE);
            if (((AllResultDetail) localObject).hasGroup)
                localView.findViewById(R.id.icon_group).setVisibility(
                        View.VISIBLE);
            if (((AllResultDetail) localObject).hasBank)
                localView.findViewById(R.id.icon_bank).setVisibility(
                        View.VISIBLE);
            TextView localTextView = (TextView) localView
                    .findViewById(R.id.coupon_tag);
            StringBuilder localStringBuilder = new StringBuilder();
            if (!StringUtils.isEmpty(((AllResultDetail) localObject).landmark)) {
                String str = ((AllResultDetail) localObject).landmark
                        .split(" ")[0];
                if (str.length() > 10)
                    str = str.substring(0, 10) + "...";
                localStringBuilder.append(str);
            }
            if (!StringUtils
                    .isEmpty(((AllResultDetail) localObject).trade_name)) {
                if (localStringBuilder.length() != 0)
                    localStringBuilder.append("/");
                localStringBuilder
                        .append(((AllResultDetail) localObject).trade_name);
            }
            localObject = localStringBuilder.toString();
            if (!StringUtils.isEmpty((String) localObject))
                localTextView.setText((CharSequence) localObject);
            else
                localTextView.setVisibility(8);
            return (View) localView;
        }

        public void onNotifyDownload() {
            AllLinearLayout.this.pageIndex = (1 + (-1 + (10 + getListCount())) / 10);
            AllLinearLayout.this.request();
        }
    }

    public static abstract interface ProtocolCreator {
        public abstract Protocol createProtocol(Context paramContext,
                int paramInt1, int paramInt2, int paramInt3,
                String paramString1, String paramString2, double paramDouble1,
                double paramDouble2, int paramInt4);
    }

    static final class TypeImage {
        int image;
        String text;

        TypeImage(int paramInt, String paramString) {
            this.image = paramInt;
            this.text = paramString;
        }
    }
}
