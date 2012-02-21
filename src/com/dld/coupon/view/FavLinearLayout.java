package com.dld.coupon.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.CompatibleDetailActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.FileUtil;
import com.dld.protocol.json.Bean;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Ticket;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class FavLinearLayout extends LinearLayout implements MainViewBase,
        Showable {
    private static final String[] operations;
    private DownloadListView.DownLoadAdapter adapter;
    private Bean bean = new Bean();
    private Context context;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            List localList = GenericDAO.getInstance(
                    FavLinearLayout.this.context).listFavs();
            if (!localList.isEmpty()) {
                FavLinearLayout.this.listView.setVisibility(0);
                FavLinearLayout.this.findViewById(R.id.no_result)
                        .setVisibility(8);
                FavLinearLayout.this.bean.setDetails(localList);
                FavLinearLayout.this.bean.setTotal(localList.size());
                ((DownloadListView.DownLoadAdapter) FavLinearLayout.this.listView
                        .getAdapter()).notifyDownloadBack();
            } else {
                FavLinearLayout.this.listView.setVisibility(8);
                FavLinearLayout.this.findViewById(R.id.no_result)
                        .setVisibility(0);
            }
        }
    };
    protected boolean isFirst = true;
    private DownloadListView listView;

    static {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "查看";
        arrayOfString[1] = "删除";
        operations = arrayOfString;
    }

    public FavLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
    }

    private void browse(Bean paramBean, int paramInt) {
        Cache.put("bean", paramBean);
        Cache.put("position", Integer.valueOf(paramInt));
        Cache.put("showable", this);
        Intent localIntent = new Intent();
        localIntent.setClass(ActivityManager.getCurrent(),
                CompatibleDetailActivity.class);
        ActivityManager.getCurrent().startActivity(localIntent);
    }

    private void initListView(DownloadListView paramDownloadListView,
            final Bean paramBean) {
        paramDownloadListView
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(
                            AdapterView<?> paramAdapterView, View paramView,
                            int paramInt, long paramLong) {
                        new AlertDialog.Builder(ActivityManager.getCurrent())
                                .setTitle("请选择要执行的操作")
                                .setSingleChoiceItems(
                                        FavLinearLayout.operations, 0,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                if (paramInt != 0) {
                                                    GenericDAO localGenericDAO = GenericDAO
                                                            .getInstance(ActivityManager
                                                                    .getCurrent());
                                                    DetailRef localDetailRef = (DetailRef) paramBean
                                                            .getDetails().get(
                                                                    paramInt);
                                                    Detail localDetail = localDetailRef.detail;
                                                    if (localDetailRef.type != 2) {
                                                        if (localDetailRef.type == 4)
                                                            FileUtil.deleteTicketImage("http://www.dld.com/vlife/image?id="
                                                                    + ((Ticket) localDetail).id);
                                                    } else
                                                        FileUtil.deleteGroupImage(((GroupDetail) localDetail).image);
                                                    LogUtils.log(
                                                            "test",
                                                            "ref.type:"
                                                                    + localDetailRef.type);
                                                    localGenericDAO
                                                            .deleteFav(localDetailRef.detail
                                                                    .getDBId());
                                                    FavLinearLayout.this
                                                            .onShow();
                                                } else {
                                                    FavLinearLayout.this
                                                            .browse(paramBean,
                                                                    paramInt);
                                                }
                                                paramDialogInterface.dismiss();
                                            }
                                        }).create().show();
                        return true;
                    }
                });
        paramDownloadListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        FavLinearLayout.this.browse(paramBean, paramInt);
                    }
                });
    }

    public void init() {
        this.listView = ((DownloadListView) findViewById(R.id.listview));
        this.adapter = new ListViewAdapter(this.bean);
        this.listView.setAdapter(this.adapter);
        initListView(this.listView, this.bean);
    }

    public void onHide() {
    }

    public void onShow() {
        if (this.isFirst) {
            init();
            this.isFirst = false;
        }
        this.handler.sendEmptyMessage(0);
    }

    public void onShow(int paramInt) {
    }

    private class ListViewAdapter extends CompatibleDownloadAdapter {
        public ListViewAdapter(Bean arg2) {
            super(arg2);
        }

        public Context getContext() {
            return FavLinearLayout.this.context;
        }

        public void onNotifyDownload() {
            FavLinearLayout.this.handler.sendEmptyMessage(0);
        }
    }
}
