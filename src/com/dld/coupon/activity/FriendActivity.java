package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CompatibleDownloadAdapter;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.Bean;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CommonFav;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailDelete;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Ticket;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FriendActivity extends BaseActivity {
    private final int SYNFAV_FAILURE = 2;
    private final int SYNFAV_SUCCESS = 1;
    private DownloadListView.DownLoadAdapter adapter;
    private RelativeLayout bankLayout;
    private TextView bankTextV;
    private Bean bean = new Bean();
    private InviteActivity.Contact contact;
    private RelativeLayout groupLayout;
    private TextView groupTextV;
    Handler handler = new Handler();
    private DownloadListView listView;
    private RelativeLayout storeLayout;
    private TextView storeTextV;
    Handler synHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            FriendActivity.this.onShowMyFav(4);
            FriendActivity.this.onShowOther();
        }
    };
    private RelativeLayout youhuiLaout;
    private TextView youhuiTextV;

    private void browse(Bean paramBean, int paramInt) {
        Cache.put("bean", paramBean);
        Cache.put("position", Integer.valueOf(paramInt));
        Cache.put("friend", Boolean.valueOf(true));
        Intent localIntent = new Intent();
        localIntent.setClass(ActivityManager.getCurrent(),
                CompatibleDetailActivity.class);
        ActivityManager.getCurrent().startActivity(localIntent);
    }

    private void init() {
        try {
            this.contact = ((InviteActivity.Contact) Cache.remove("contact"));
            this.listView = ((DownloadListView) findViewById(R.id.listview));
            this.adapter = new ListViewAdapter(this.bean);
            this.listView.setAdapter(this.adapter);
            initListView(this.listView, this.bean);
            ((TextView) findViewById(R.id.title_text))
                    .setText(this.contact.name + "的收藏");
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.log("test", localException);
        }
    }

    private void initListView(DownloadListView paramDownloadListView,
            Bean paramBean) {
        final Bean bean = paramBean;
        paramDownloadListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        FriendActivity.this.browse(bean, paramInt);
                    }
                });
    }

    private void initWidget() {
        this.groupLayout = ((RelativeLayout) findViewById(R.id.grouplayout));
        this.youhuiLaout = ((RelativeLayout) findViewById(R.id.youhuilayout));
        this.storeLayout = ((RelativeLayout) findViewById(R.id.storelayout));
        this.bankLayout = ((RelativeLayout) findViewById(R.id.banklayout));
        this.groupTextV = ((TextView) findViewById(R.id.grouptv));
        this.youhuiTextV = ((TextView) findViewById(R.id.youhuitv));
        this.storeTextV = ((TextView) findViewById(R.id.storetv));
        this.bankTextV = ((TextView) findViewById(R.id.banktv));
        this.groupLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                FriendActivity.this.onShowMyFav(2);
            }
        });
        this.youhuiLaout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                FriendActivity.this.onShowMyFav(4);
            }
        });
        this.storeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                FriendActivity.this.onShowMyFav(0);
            }
        });
        this.bankLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                FriendActivity.this.onShowMyFav(3);
            }
        });
    }

    private void sync() {
        int i = this.contact.id;
        Object localObject = GenericDAO.getInstance();
        String str3 = ((GenericDAO) localObject).getIdStringByType(4, i);
        String str1 = ((GenericDAO) localObject).getIdStringByType(3, i);
        String str5 = ((GenericDAO) localObject).getIdStringByType(2, i);
        String str2 = ((GenericDAO) localObject).getCanByGroupString(2, i);
        String str4 = ((GenericDAO) localObject).getStoreIdString(i);
        String str6 = ((GenericDAO) localObject).getCouponIdString(i);
        localObject = new Param();
        ((Param) localObject).append("userId", String.valueOf(i))
                .append("type_0", str6).append("type_2", str3)
                .append("type_3", str5).append("type_4", "")
                .append("type_5", str1).append("type_6", str2)
                .append("type_7", str4);
        LogUtils.log("main",
                "primary params" + ((Param) localObject).toStringfrome());
        ProtocolHelper.synchroFavRequest(ActivityManager.getCurrent(),
                (Param) localObject, false).startTransForUser(
                new CommonFavResult(), (Param) localObject);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.friend_view);
        initWidget();
        init();
        initSegment();
        sync();
    }

    public void onShowMyFav(int paramInt) {
        LogUtils.log("main", " onshow start" + System.currentTimeMillis());
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
            int i = this.contact.id;
            Object localObject = GenericDAO.getInstance();
            if (paramInt != 0)
                localObject = ((GenericDAO) localObject).listFavs(paramInt, i);
            else
                localObject = ((GenericDAO) localObject).listFavsOfStore(i);
            if (!((List) localObject).isEmpty()) {
                this.listView.setVisibility(0);
                findViewById(R.id.no_result).setVisibility(8);
                this.bean.setDetails((List) localObject);
                this.bean.setTotal(((List) localObject).size());
                switch (paramInt) {
                case 0:
                    this.storeTextV.setText("商家(" + ((List) localObject).size()
                            + ")");
                    this.storeTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                    break;
                case 2:
                    this.groupTextV.setText("团购(" + ((List) localObject).size()
                            + ")");
                    this.groupTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                    break;
                case 3:
                    this.bankTextV.setText("卡优惠(" + ((List) localObject).size()
                            + ")");
                    this.bankTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    break;
                case 4:
                    this.youhuiTextV.setText("优惠("
                            + ((List) localObject).size() + ")");
                    this.youhuiTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                case 1:
                }
                ((DownloadListView.DownLoadAdapter) this.listView.getAdapter())
                        .notifyDownloadBack();
            } else {
                switch (paramInt) {
                case 0:
                    this.storeTextV.setText("商家(0)");
                    this.storeTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                    break;
                case 2:
                    this.groupTextV.setText("团购(0)");
                    this.groupTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                    break;
                case 3:
                    this.bankTextV.setText("卡优惠(0)");
                    this.bankTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.youhuiTextV.setBackgroundDrawable(null);
                    break;
                case 4:
                    this.youhuiTextV.setText("优惠(0)");
                    this.youhuiTextV
                            .setBackgroundResource(R.drawable.storetype_selected);
                    this.groupTextV.setBackgroundDrawable(null);
                    this.storeTextV.setBackgroundDrawable(null);
                    this.bankTextV.setBackgroundDrawable(null);
                case 1:
                }
                this.listView.setVisibility(8);
                ((ImageView) findViewById(R.id.no_result).findViewById(
                        R.id.unlogin_img))
                        .setImageResource(R.drawable.not_found);
                findViewById(R.id.no_result).setVisibility(0);
            }
        } else {
            this.listView.setVisibility(8);
            switch (paramInt) {
            case 0:
                this.storeTextV.setText("商家(0)");
                this.storeTextV
                        .setBackgroundResource(R.drawable.storetype_selected);
                this.groupTextV.setBackgroundDrawable(null);
                this.youhuiTextV.setBackgroundDrawable(null);
                this.bankTextV.setBackgroundDrawable(null);
                break;
            case 2:
                this.groupTextV.setText("团购(0)");
                this.groupTextV
                        .setBackgroundResource(R.drawable.storetype_selected);
                this.youhuiTextV.setBackgroundDrawable(null);
                this.storeTextV.setBackgroundDrawable(null);
                this.bankTextV.setBackgroundDrawable(null);
                break;
            case 3:
                this.bankTextV.setText("卡优惠(0)");
                this.bankTextV
                        .setBackgroundResource(R.drawable.storetype_selected);
                this.groupTextV.setBackgroundDrawable(null);
                this.storeTextV.setBackgroundDrawable(null);
                this.youhuiTextV.setBackgroundDrawable(null);
                break;
            case 4:
                this.youhuiTextV.setText("优惠(0)");
                this.youhuiTextV
                        .setBackgroundResource(R.drawable.storetype_selected);
                this.groupTextV.setBackgroundDrawable(null);
                this.storeTextV.setBackgroundDrawable(null);
                this.bankTextV.setBackgroundDrawable(null);
            case 1:
            }
            findViewById(R.id.no_result).setVisibility(0);
            ((ImageView) findViewById(R.id.no_result).findViewById(
                    R.id.unlogin_img)).setImageResource(R.drawable.unlogintip);
        }
    }

    public void onShowOther() {
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
            int j = this.contact.id;
            GenericDAO localGenericDAO = GenericDAO.getInstance();
            int k = localGenericDAO.getFavCountByType(2, j);
            this.groupTextV.setText("团购(" + k + ")");
            k = localGenericDAO.getFavCountByType(3, j);
            this.bankTextV.setText("卡优惠(" + k + ")");
            int i = localGenericDAO.getStoreCount(j);
            this.storeTextV.setText("商家(" + i + ")");
        } else {
            this.listView.setVisibility(8);
            this.groupTextV.setText("团购(0)");
            this.youhuiTextV.setText("优惠(0)");
            this.youhuiTextV
                    .setBackgroundResource(R.drawable.storetype_selected);
            this.groupTextV.setBackgroundDrawable(null);
            this.storeTextV.setBackgroundDrawable(null);
            this.bankTextV.setBackgroundDrawable(null);
            this.storeTextV.setText("商家(0)");
            this.bankTextV.setText("卡优惠(0)");
        }
    }

    private class CommonFavResult extends Protocol.OnJsonProtocolResult {
        public CommonFavResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.log("main", "on result for syn exception");
            FriendActivity.this.synHandler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                int i = FriendActivity.this.contact.id;
                Object localObject1 = (CommonFav) paramObject;
                Object localObject2 = ((CommonFav) localObject1).details;
                Object localObject5 = ((CommonFav) localObject1).detailsDeleteList;
                LogUtils.log("main", ((List) localObject5).size()
                        + "/// deleteList size");
                localObject1 = GenericDAO.getInstance(ActivityManager
                        .getCurrent());
                Object localObject3 = (HashMap) ((GenericDAO) localObject1)
                        .listAsMap(i);
                LogUtils.log("main", ((HashMap) localObject3).entrySet().size()
                        + "allMap size");
                Object localObject4 = new HashMap();
                if ((localObject5 != null)
                        && (((List) localObject5).size() > 0)) {
                    localObject5 = ((List) localObject5).iterator();
                    while (((Iterator) localObject5).hasNext()) {
                        DetailDelete localDetailDelete = (DetailDelete) ((Iterator) localObject5)
                                .next();
                        if (!((HashMap) localObject3)
                                .containsKey(localDetailDelete.id))
                            continue;
                        ((HashMap) localObject4).put(localDetailDelete.id,
                                (DetailRef) ((HashMap) localObject3)
                                        .get(localDetailDelete.id));
                    }
                }
                LogUtils.log("main", ((HashMap) localObject4).entrySet().size()
                        + "refsize");
                localObject3 = ((HashMap) localObject4).entrySet().iterator();
                while (((Iterator) localObject3).hasNext()) {
                    localObject4 = (Map.Entry) ((Iterator) localObject3).next();
                    LogUtils.log("main", "entry key=="
                            + ((Map.Entry) localObject4).getKey());
                    ((GenericDAO) localObject1)
                            .deleteFav(((DetailRef) ((Map.Entry) localObject4)
                                    .getValue()).detail.getDBId(), i);
                }
                if (localObject2 != null) {
                    localObject3 = new ArrayList();
                    localObject2 = ((List) localObject2).iterator();
                    while (((Iterator) localObject2).hasNext()) {
                        localObject4 = (DetailRef) ((Iterator) localObject2)
                                .next();
                        ((GenericDAO) localObject1).saveFav(
                                ((DetailRef) localObject4).type,
                                ((DetailRef) localObject4).detail, i);
                        if (((DetailRef) localObject4).type != 4)
                            continue;
                        ((List) localObject3)
                                .add((Ticket) ((DetailRef) localObject4).detail);
                    }
                    FriendActivity.this.synHandler.sendEmptyMessage(1);
                }
            }
        }
    }

    private class ListViewAdapter extends CompatibleDownloadAdapter {
        public ListViewAdapter(Bean arg2) {
            super(arg2);
        }

        public Context getContext() {
            return FriendActivity.this;
        }

        public void onNotifyDownload() {
            FriendActivity.this.handler.post(new Runnable() {
                public void run() {
                    FriendActivity.this.onShowMyFav(4);
                }
            });
        }
    }
}
