package com.dld.coupon.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.UserConfig;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.coupon.util.MapUtil.LocationObserver;
import com.dld.coupon.view.BankCouponDownLoadAdapter;
import com.dld.coupon.view.CardDialogue;
import com.dld.coupon.view.CouponDownLoadAdapter;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.GroupDownLoadAdapter;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.BankCouponProtocolHelper;
import com.dld.protocol.GroupProtocolHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.BankCoupon;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.Group;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class AroundActivity extends ListTabActivity {
    private static final int FIRST_PAGE_INDEX = 1;
    private static final int PAGE_SIZE = 10;
    private static String cityId;
    private static String cityName;
    public static MapUtil.LatAndLon location;
    static final String[] sortLabel;
    static final String[] sortString;
    static final int[] sortTypes;
    private DownloadListView.DownLoadAdapter bankAdapter;
    private BankCoupon bankCoupon = new BankCoupon();
    private DownloadListView bankListView;
    private Coupon coupon = new Coupon();
    private DownloadListView.DownLoadAdapter couponAdapter;
    private DownloadListView couponListView;
    private Group group = new Group();
    private DownloadListView.DownLoadAdapter groupAdapter;
    private DownloadListView groupListView;
    private Handler handler = new Handler() {
        private void showCoupon(boolean paramBoolean, Coupon paramCoupon,
                DownloadListView paramDownloadListView, Message paramMessage) {
            if ((AroundActivity.this.locationFailedFlag)
                    || (AroundActivity.location != null)) {
                int j = paramMessage.arg1;
                if (paramMessage.arg2 > 0) {
                    Cache.remove(AroundActivity.this.lastUrl);
                    for (int i = 1; i <= j; i++)
                        Cache.remove(AroundActivity.this.createGroupProtocal(i)
                                .getAbsoluteUrl());
                    j = 1;
                }
                if ((j == 1) && (paramCoupon != null)
                        && (paramCoupon.details != null)) {
                    paramCoupon.details.clear();
                    paramDownloadListView.reset();
                    AroundActivity.this.showSuccessView();
                }
                if ((AroundActivity.location != null)
                        || (AroundActivity.this.locationFailedFlag)) {
                    Protocol localProtocol = AroundActivity.this
                            .createCouponProtocol(paramCoupon, j);
                    AroundActivity.this.lastUrl = localProtocol
                            .getAbsoluteUrl();
                    localProtocol
                            .startTrans(new Protocol.OnJsonProtocolResult() {
                                public void onException(String paramString,
                                        Exception paramException) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        AroundActivity.this.handler
                                                .sendEmptyMessage(3);
                                }

                                public void onResult(String paramString,
                                        Object paramObject) {
                                    if (!AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        ;
                                    Object localObject1;
                                    do
                                    // while (true)
                                    {
                                        // return;
                                        localObject1 = (Coupon) paramObject;
                                        if (!((Coupon) localObject1).details
                                                .isEmpty())
                                            break;
                                        AroundActivity.this.handler
                                                .sendEmptyMessage(6);
                                    } while (coupon.details
                                            .containsAll(((Coupon) localObject1).details));
                                    // while (true)
                                    {
                                        synchronized (AroundActivity.this) {
                                            coupon.details
                                                    .addAll(((Coupon) localObject1).details);
                                            coupon.pageInfo = ((Coupon) localObject1).pageInfo;
                                            localObject1 = AroundActivity.this.handler;
                                            boolean isStore = false;
                                            if (isStore) {
                                                int i = 21;
                                                ((Handler) localObject1)
                                                        .sendEmptyMessage(i);
                                            }
                                        }
                                        int i = 1;
                                    }
                                }
                            });
                }
            }
        }

        public void handleMessage(Message paramMessage) {
            try {
                LogUtils.log("test", "around, msg:" + paramMessage.what);
                if (paramMessage.what == 21) {
                    AroundActivity.this.storeAdapter.notifyDownloadBack();
                    // break label718;
                }
                if (paramMessage.what == 1)
                    AroundActivity.this.couponAdapter.notifyDownloadBack();
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
            }
            // Protocol localProtocol4;
            int j = 0;
            // label482: int j;
            if (paramMessage.what == 11) {
                AroundActivity.this.groupAdapter.notifyDownloadBack();
            } else if (paramMessage.what == 31) {
                AroundActivity.this.bankAdapter.notifyDownloadBack();
            } else if (paramMessage.what == 3) {
                Object localObject = (TextView) AroundActivity.this
                        .findViewById(R.id.location);
                if ((AroundActivity.location == null) && (localObject != null))
                    ((TextView) localObject).setText("十分抱歉，未能定位您所在的位置。");
                if (AroundActivity.this.type == 5) {
                    localObject = AroundActivity.this
                            .createSelectedCouponProtocol(
                                    AroundActivity.this.coupon, 1);
                    AroundActivity.this.lastUrl = ((Protocol) localObject)
                            .getAbsoluteUrl();
                    ((Protocol) localObject)
                            .startTrans(new Protocol.OnJsonProtocolResult(
                                    Coupon.class) {
                                public void onException(String paramString,
                                        Exception paramException) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        if (AroundActivity.this.coupon.details
                                                .isEmpty())
                                            AroundActivity.this.handler
                                                    .post(new Runnable() {
                                                        public void run() {
                                                            AroundActivity.this
                                                                    .showFailedView();
                                                        }
                                                    });
                                        else
                                            AroundActivity.this.handler
                                                    .post(new Runnable() {
                                                        public void run() {
                                                            AroundActivity.this.couponAdapter
                                                                    .notifyException();
                                                        }
                                                    });
                                }

                                public void onResult(String paramString,
                                        Object paramObject) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        AroundActivity.this.handler
                                                .post(new Runnable() {
                                                    public void run() {
                                                        Coupon localCoupon = (Coupon) coupon;
                                                        if (!localCoupon.details
                                                                .isEmpty())
                                                            AroundActivity.this.handler
                                                                    .post(new Runnable() {
                                                                        public void run() {
                                                                            AroundActivity.this.coupon.details
                                                                                    .addAll(coupon.details);
                                                                            AroundActivity.this.coupon.pageInfo = coupon.pageInfo;
                                                                            AroundActivity.this.couponAdapter
                                                                                    .notifyDownloadBack();
                                                                        }
                                                                    });
                                                        else
                                                            AroundActivity.this
                                                                    .showFailedView();
                                                    }
                                                });
                                }
                            });
                } else if (AroundActivity.this.type == 15) {
                    localObject = AroundActivity.this
                            .createSelectedGroupProtocal(1);
                    AroundActivity.this.lastUrl = ((Protocol) localObject)
                            .getAbsoluteUrl();
                    ((Protocol) localObject)
                            .startTrans(new Protocol.OnJsonProtocolResult(
                                    Group.class) {
                                public void onException(String paramString,
                                        Exception paramException) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        if (AroundActivity.this.group.details
                                                .isEmpty())
                                            AroundActivity.this.handler
                                                    .post(new Runnable() {
                                                        public void run() {
                                                            AroundActivity.this
                                                                    .showFailedView();
                                                        }
                                                    });
                                        else
                                            AroundActivity.this.groupAdapter
                                                    .notifyException();
                                }

                                public void onResult(String paramString,
                                        Object paramObject) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        AroundActivity.this.handler
                                                .post(new Runnable() {
                                                    public void run() {
                                                        Group localGroup = (Group) group;
                                                        if (localGroup.details
                                                                .isEmpty())
                                                            AroundActivity.this
                                                                    .showFailedView();
                                                        while (true) {
                                                            // return;
                                                            synchronized (AroundActivity.this) {
                                                                if (!AroundActivity.this.group.details
                                                                        .containsAll(localGroup.details)) {
                                                                    AroundActivity.this.group.total = localGroup.total;
                                                                    AroundActivity.this.group.details
                                                                            .addAll(localGroup.details);
                                                                    AroundActivity.this.handler
                                                                            .sendEmptyMessage(11);
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                }
                            });
                } else {
                    AroundActivity.this.showFailedView();
                }
            } else if (paramMessage.what == 4) {
                AroundActivity.this.showFailedView();
            } else if (paramMessage.what == 25) {
                showCoupon(true, AroundActivity.this.store,
                        AroundActivity.this.storeListView, paramMessage);
            } else if (paramMessage.what == 5) {
                showCoupon(false, AroundActivity.this.coupon,
                        AroundActivity.this.couponListView, paramMessage);
            } else if (paramMessage.what == 15) {
                if ((AroundActivity.this.locationFailedFlag)
                        || (AroundActivity.location != null)) {
                    int i = paramMessage.arg1;
                    if (paramMessage.arg2 > 0) {
                        Cache.remove(AroundActivity.this.lastUrl);
                        // localProtocol4 = 1;
                        // break label719;
                    }
                    if (i == 1) {
                        AroundActivity.this.group.details.clear();
                        AroundActivity.this.groupListView.reset();
                        AroundActivity.this.showSuccessView();
                    }
                    Protocol localProtocol1 = AroundActivity.this
                            .createGroupProtocal(i);
                    AroundActivity.this.lastUrl = localProtocol1
                            .getAbsoluteUrl();
                    localProtocol1
                            .startTrans(new Protocol.OnJsonProtocolResult(
                                    Group.class) {
                                public void onException(String paramString,
                                        Exception paramException) {
                                    if (AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        AroundActivity.this.handler
                                                .sendEmptyMessage(3);
                                }

                                public void onResult(String paramString,
                                        Object paramObject) {
                                    if (!AroundActivity.this.lastUrl
                                            .equals(paramString))
                                        ;
                                    // while (true)
                                    {
                                        // return;
                                        Group localGroup = (Group) paramObject;
                                        if (localGroup.details.isEmpty()) {
                                            AroundActivity.this.handler
                                                    .sendEmptyMessage(6);
                                            // continue;
                                        }
                                        synchronized (AroundActivity.this) {
                                            if (!AroundActivity.this.group.details
                                                    .containsAll(localGroup.details)) {
                                                AroundActivity.this.group.total = localGroup.total;
                                                AroundActivity.this.group.details
                                                        .addAll(localGroup.details);
                                                AroundActivity.this.handler
                                                        .sendEmptyMessage(11);
                                            }
                                        }
                                    }
                                }
                            });
                    // break label718;
                    // Cache.remove(AroundActivity.this.createGroupProtocal(localProtocol4).getAbsoluteUrl());
                    // localProtocol4++;
                    // break label719;
                }
            } else if (paramMessage.what == 35) {
                if ((!AroundActivity.this.locationFailedFlag)
                        && (AroundActivity.location == null))
                    return;
                // break label718;
                j = paramMessage.arg1;
                if (paramMessage.arg2 > 0) {
                    Cache.remove(AroundActivity.this.lastUrl);
                    // localProtocol4 = 1;
                    // break label729;
                }
            }
            // while (true)
            {
                if (j == 1) {
                    AroundActivity.this.bankCoupon.details.clear();
                    AroundActivity.this.bankListView.reset();
                    AroundActivity.this.showSuccessView();
                }
                Protocol localProtocol2 = AroundActivity.this
                        .createBankProtocol(j);
                AroundActivity.this.lastUrl = localProtocol2.getAbsoluteUrl();
                localProtocol2.startTrans(new Protocol.OnJsonProtocolResult(
                        BankCoupon.class) {
                    public void onException(String paramString,
                            Exception paramException) {
                        if (AroundActivity.this.lastUrl.equals(paramString))
                            AroundActivity.this.handler.sendEmptyMessage(4);
                    }

                    public void onResult(String paramString, Object paramObject) {
                        if (!AroundActivity.this.lastUrl.equals(paramString))
                            ;
                        // while (true)
                        {
                            // return;
                            BankCoupon localBankCoupon = (BankCoupon) paramObject;
                            if (localBankCoupon.details.isEmpty()) {
                                AroundActivity.this.handler.sendEmptyMessage(6);
                                // continue;
                            }
                            synchronized (AroundActivity.this) {
                                if (!AroundActivity.this.bankCoupon.details
                                        .containsAll(localBankCoupon.details)) {
                                    AroundActivity.this.bankCoupon.total = localBankCoupon.total;
                                    AroundActivity.this.bankCoupon.details
                                            .addAll(localBankCoupon.details);
                                    AroundActivity.this.handler
                                            .sendEmptyMessage(31);
                                }
                            }
                        }
                    }
                });
                label718: label719: label729:
                // do
                {
                    // Cache.remove(AroundActivity.this.createBankProtocol(localProtocol4).getAbsoluteUrl());
                    // localProtocol4++;
                    // continue;
                    // if (paramMessage.what == 6)
                    // {
                    // AroundActivity.this.showFailedView();
                    // }
                    // else if ((paramMessage.what != 7) && (paramMessage.what
                    // != 8) && (paramMessage.what == 9) &&
                    // (!AroundActivity.this.locationing))
                    // {
                    // AroundActivity.location = null;
                    // AroundActivity.this.requestLocation();
                    // }
                    // return;
                    // if (localProtocol4 <= localProtocol2)
                    j = 0;
                    // break label482;
                    // localProtocol3 = 1;
                    // break;
                }
                // while (localProtocol4 <= localProtocol3);
                // Protocol localProtocol3 = 1;
            }
        }
    };
    private MapUtil.LatAndLon lastLocation;
    private String lastUrl = "";
    private boolean locationFailedFlag = false;
    private View locationSuccess;
    private boolean locationing;
    private View mNoResultView;
    private View notAllowGps;
    private MapUtil.LocationObserver ob = new MapUtil.LocationObserver() {
        public void onFailed() {
            if (AroundActivity.location == null)
                AroundActivity.this.locationFailedFlag = true;
            AroundActivity.this.handler.sendEmptyMessage(3);
        }

        public void onLocationChanged(MapUtil.LatAndLon paramLatAndLon) {
            try {
                LogUtils.log("test", "locationUpdated");
                if (paramLatAndLon != null) {
                    MapUtil.LatAndLon localLatAndLon = AroundActivity.location;
                    if ((localLatAndLon == null)
                            || (Math.abs(localLatAndLon.lat
                                    - paramLatAndLon.lat) >= 0.001D)
                            || (Math.abs(localLatAndLon.lon
                                    - paramLatAndLon.lon) >= 0.001D)) {
                        AroundActivity.location = paramLatAndLon;
                        AroundActivity.this.locationing = false;
                        AroundActivity.this.locationFailedFlag = false;
                        if (AroundActivity.this.getListSize() == 0)
                            AroundActivity.this.request(
                                    AroundActivity.this.type, 1);
                    }
                }
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
            }
        }
    };
    private boolean showingDialog = false;
    private int sortIndex;
    private Coupon store = new Coupon();
    private DownloadListView.DownLoadAdapter storeAdapter;
    private DownloadListView storeListView;
    private int type;

    static {
        int[] localObject = new int[2];
        localObject[0] = 2;
        localObject[1] = 1;
        sortTypes = localObject;
        Object[] local = new String[2];
        local[0] = "默认排序";
        local[1] = "按距离排序";
        sortString = (String[]) local;
        local = new String[2];
        local[0] = "默认↓";
        local[1] = "距离↓";
        sortLabel = (String[]) local;
    }

    private boolean checkNetwork() {
        boolean bool = false;
        Object localObject = (ConnectivityManager) getSystemService("connectivity");
        if (((ConnectivityManager) localObject).getActiveNetworkInfo() != null)
            bool = ((ConnectivityManager) localObject).getActiveNetworkInfo()
                    .isAvailable();
        if ((!bool) && (!this.showingDialog)) {
            this.showingDialog = true;
            localObject = new AlertDialog.Builder(this).setTitle("没有可用的网络")
                    .setMessage("网络访问失败，请检查网络连接。");
            ((AlertDialog.Builder) localObject)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    Intent localIntent = new Intent("/");
                                    localIntent
                                            .setComponent(new ComponentName(
                                                    "com.android.settings",
                                                    "com.android.settings.WirelessSettings"));
                                    localIntent
                                            .setAction("android.intent.action.VIEW");
                                    AroundActivity.this
                                            .startActivity(localIntent);
                                    AroundActivity.this.showingDialog = false;
                                }
                            })
                    .setNeutralButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    paramDialogInterface.cancel();
                                    AroundActivity.this.showingDialog = false;
                                }
                            }).create();
            ((AlertDialog.Builder) localObject).show();
        }
        return bool;
    }

    private Protocol createBankProtocol(int paramInt) {
        DialogInterface.OnDismissListener local11 = new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface paramDialogInterface) {
                AroundActivity.this.refresh();
            }
        };
        if (paramInt == 1)
            this.lastLocation = location;
        double d2 = 0.0D;
        double d1 = 0.0D;
        if (this.lastLocation != null) {
            d2 = this.lastLocation.getLatitude();
            d1 = this.lastLocation.getLongitude();
        }
        return BankCouponProtocolHelper.buildSearchByLatLon(this,
                CardDialogue.getBankCodes(local11), d2, d1, paramInt - 1, 10,
                cityId, this.category.id, sortTypes[this.sortIndex]);
    }

    private Protocol createCouponProtocol(Coupon paramCoupon, int paramInt) {
        int i = 1;
        Protocol localProtocol;
        if (!this.locationFailedFlag) {
            if (paramInt == i)
                this.lastLocation = location;
            if (this.store != paramCoupon)
                i = 0;
            localProtocol = ProtocolHelper.buildSearchByLatLon(i == 0 ? false
                    : true, this, this.lastLocation.getLatitude(),
                    this.lastLocation.getLongitude(), paramInt, 10,
                    this.category.id, sortTypes[this.sortIndex]);
        } else {
            localProtocol = ProtocolHelper.buildBySelected(this, paramInt, 10);
        }
        return localProtocol;
    }

    private Protocol createGroupProtocal(int paramInt) {
        Protocol localProtocol;
        if (!this.locationFailedFlag) {
            if (paramInt == 1)
                this.lastLocation = location;
            localProtocol = GroupProtocolHelper.buildSearchByLatLon(this,
                    this.lastLocation.getLatitude(),
                    this.lastLocation.getLongitude(), paramInt - 1, 10, cityId,
                    this.category.id, sortTypes[this.sortIndex]);
        } else {
            localProtocol = GroupProtocolHelper.buildBySelected(this,
                    paramInt - 1);
        }
        return localProtocol;
    }

    private Protocol createSelectedCouponProtocol(Coupon paramCoupon,
            int paramInt) {
        return ProtocolHelper.buildBySelected(this, paramInt, 10);
    }

    private Protocol createSelectedGroupProtocal(int paramInt) {
        return GroupProtocolHelper.buildBySelected(this, paramInt - 1);
    }

    private void doShowList(int paramInt, DownloadListView paramDownloadListView) {
        this.storeListView.setVisibility(8);
        this.couponListView.setVisibility(8);
        this.groupListView.setVisibility(8);
        this.bankListView.setVisibility(8);
        paramDownloadListView.setVisibility(0);
        showSuccessView();
        request(paramInt, 1);
    }

    private int getListSize() {
        int i;
        switch (this.type) {
        default:
            i = this.bankCoupon.details.size();
            break;
        case 5:
            i = this.coupon.details.size();
            break;
        case 15:
            i = this.group.details.size();
            break;
        case 25:
            i = this.store.details.size();
        }
        return i;
    }

    private void initBankListView() {
        this.bankListView = ((DownloadListView) findViewById(R.id.banklistview));
        this.bankListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        Cache.put("coupon", AroundActivity.this.bankCoupon);
                        Cache.put("position", Integer.valueOf(paramInt));
                        Cache.put(
                                "protocol",
                                AroundActivity.this
                                        .createBankProtocol(AroundActivity.this.bankCoupon.details
                                                .size() / 10));
                        Intent localIntent = new Intent();
                        localIntent.setClass(AroundActivity.this,
                                BankCouponDetailActivity.class);
                        AroundActivity.this.startActivity(localIntent);
                    }
                });
        this.bankAdapter = new BankListviewAdapter(this.groupListView,
                this.bankCoupon);
        this.bankListView.setAdapter(this.bankAdapter);
    }

    private void initCouponListview() {
        this.couponListView = ((DownloadListView) findViewById(R.id.couponlistview));
        this.couponAdapter = new ListViewAdapter(false, this.coupon);
        this.couponListView.setAdapter(this.couponAdapter);
        initListView(this.couponListView, this.coupon, false);
    }

    private void initDistanceSpinner() {
        final TextView localTextView = (TextView) findViewById(R.id.distance_spinner);
        localTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                try {
                    DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            paramDialogInterface.dismiss();
                            if (AroundActivity.this.sortIndex != paramInt) {
                                AroundActivity.this.sortIndex = paramInt;
                                localTextView
                                        .setText(AroundActivity.sortLabel[paramInt]);
                                AroundActivity.this.request(
                                        AroundActivity.this.type, 1);
                            }
                        }
                    };
                    new AlertDialog.Builder(AroundActivity.this)
                            .setTitle("请选择排序方式")
                            .setSingleChoiceItems(AroundActivity.sortString,
                                    AroundActivity.this.sortIndex, local1)
                            .show();
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", "", localException);
                }
            }
        });
    }

    private void initGroupListView() {
        this.groupListView = ((DownloadListView) findViewById(R.id.grouplistview));
        this.groupListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        Cache.put("group", AroundActivity.this.group);
                        Cache.put("position", Integer.valueOf(paramInt));
                        Cache.put(
                                "protocol",
                                AroundActivity.this
                                        .createGroupProtocal(AroundActivity.this.group.details
                                                .size() / 10));
                        Intent localIntent = new Intent();
                        localIntent.setClass(AroundActivity.this,
                                GroupDetailActivity.class);
                        AroundActivity.this.startActivity(localIntent);
                    }
                });
        this.groupAdapter = new GroupListviewAdapter(this.groupListView,
                this.group);
        this.groupListView.setAdapter(this.groupAdapter);
    }

    private void initListView(DownloadListView paramDownloadListView,
            final Coupon paramCoupon, boolean paramBoolean) {
        paramDownloadListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        Cache.put("coupon", paramCoupon);
                        Cache.put("protocol", AroundActivity.this
                                .createCouponProtocol(paramCoupon,
                                        paramCoupon.details.size() / 10));
                        Intent localIntent = new Intent();
                        localIntent.putExtra("position", paramInt);
                        localIntent.putExtra("use_gps", true);
                        AroundActivity localAroundActivity = AroundActivity.this;
                        Object localObject;
                        if (!false)
                            localObject = DetailActivity.class;
                        else
                            localObject = StoreDetailActivity.class;
                        localIntent.setClass(localAroundActivity,
                                (Class) localObject);
                        AroundActivity.this.startActivity(localIntent);
                    }
                });
    }

    private void initLocation() {
        this.notAllowGps = findViewById(R.id.no_location_view);
        ((Button) this.notAllowGps.findViewById(R.id.setting_gps_button))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        UserConfig.setAllowLocationService(AroundActivity.this,
                                true);
                        AroundActivity.this.showSuccessView();
                        AroundActivity.this.requestLocation();
                    }
                });
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        AroundActivity.this.refresh();
                    }
                });
    }

    private void initStoreListview() {
        this.storeListView = ((DownloadListView) findViewById(R.id.storelistview));
        this.storeAdapter = new ListViewAdapter(true, this.store);
        this.storeListView.setAdapter(this.storeAdapter);
        initListView(this.storeListView, this.store, true);
    }

    private void initTypeSpinner() {
        final TextView localTextView = (TextView) findViewById(R.id.type_spinner);
        localTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        try {
                            paramDialogInterface.dismiss();
                            switch (paramInt) {
                            case 1:
                                if (AroundActivity.this.type == 15)
                                    return;
                                AroundActivity.this.type = 15;
                                AroundActivity.this.showGroup();
                                localTextView.setText(R.string.group);
                            case 3:
                            case 2:
                            case 0:
                            }
                        } catch (Exception localException) {
                            LogUtils.e("test", "", localException);
                        }
                        if (AroundActivity.this.type != 25) {
                            AroundActivity.this.type = 25;
                            AroundActivity.this.showStore();
                            localTextView.setText(R.string.store);
                            // return;
                            if (AroundActivity.this.type != 35) {
                                AroundActivity.this.type = 35;
                                AroundActivity.this.showBank();
                                localTextView.setText(R.string.bank);
                                // return;
                                if (AroundActivity.this.type != 5) {
                                    AroundActivity.this.type = 5;
                                    AroundActivity.this.showCoupon();
                                    localTextView.setText(R.string.coupon);
                                }
                            }
                        }
                    }
                };
                int i = 0;
                switch (AroundActivity.this.type) {
                default:
                    i = 0;
                case 15:
                    i = 1;
                    break;
                case 35:
                    i = 2;
                    break;
                case 25:
                    i = 3;
                    break;
                }
                try {
                    // while (true)
                    {
                        new AlertDialog.Builder(AroundActivity.this)
                                .setTitle("请选择搜索类型")
                                .setSingleChoiceItems(R.array.types, i, local1)
                                .show();
                    }
                } catch (Exception localException) {
                    // while (true)
                    LogUtils.e("test", "", localException);
                }
            }
        });
    }

    private void refresh() {
        new Thread() {
            public void run() {
                // AroundActivity.this.request(AroundActivity.this.type, 1 +
                // AroundActivity.access$2(AroundActivity.this) / 10, true);
                if (Cache.getCache("gps_address") == null) {
                    if (MapUtil.isInited())
                        MapUtil.reverseGeoCode(null, AroundActivity.this);
                } else
                    MapUtil.updateAddress(AroundActivity.this,
                            AroundActivity.this.handler);
            }
        }.start();
    }

    private void request(int paramInt1, int paramInt2) {
        request(paramInt1, paramInt2, false);
    }

    private void request(int paramInt1, int paramInt2, boolean paramBoolean) {
        if ((location != null) || (this.locationFailedFlag)) {
            Handler localHandler2 = this.handler;
            Handler localHandler1 = this.handler;
            int i;
            if (!paramBoolean)
                i = 0;
            else
                i = 1;
            localHandler2.sendMessage(Message.obtain(localHandler1, paramInt1,
                    paramInt2, i));
        }
    }

    private void requestLocation() {
        LogUtils.log("main", "start_time" + System.currentTimeMillis());
        if (location != null) {
            request(this.type, 1);
        } else {
            this.locationing = true;
            MapUtil.regist(this.ob, this.handler, this);
        }
    }

    private void showBank() {
        showList(35, this.bankListView);
    }

    private void showCoupon() {
        showList(5, this.couponListView);
    }

    private void showFailedView() {
        this.locationSuccess.setVisibility(8);
        this.notAllowGps.setVisibility(8);
        this.mNoResultView.setVisibility(0);
    }

    private void showGroup() {
        showList(15, this.groupListView);
    }

    private void showList(int paramInt, DownloadListView paramDownloadListView) {
        if (!this.locationFailedFlag)
            doShowList(paramInt, paramDownloadListView);
        else
            this.handler.sendEmptyMessage(3);
    }

    private void showSuccessView() {
        if (this.locationSuccess.getVisibility() != 0)
            this.locationSuccess.setVisibility(0);
        if (this.notAllowGps.getVisibility() != 8)
            this.notAllowGps.setVisibility(8);
        if (this.mNoResultView.getVisibility() != 8)
            this.mNoResultView.setVisibility(8);
    }

    public void init() {
        this.type = 5;
        this.sortIndex = 0;
        this.locationFailedFlag = false;
        if (StringUtils.isEmpty(cityId))
            cityId = SharePersistent.getInstance().getPerference(this,
                    "city_id");
        if (StringUtils.isEmpty(cityName))
            cityName = SharePersistent.getInstance().getPerference(this,
                    "city_name");
        this.mNoResultView = findViewById(R.id.no_result_view);
        this.locationSuccess = findViewById(R.id.location_success_view);
        initDistanceSpinner();
        initTypeSpinner();
        initCategoryList();
        initRefresh();
        initStoreListview();
        initCouponListview();
        initGroupListView();
        initBankListView();
        initLocation();
        showCoupon();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.around_view);
        init();
        if (checkNetwork()) {
            showSuccessView();
            if (location == null)
                requestLocation();
        } else {
            showFailedView();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return false;
    }

    protected void onResume() {
        super.onResume();
        new Thread() {
            public void run() {
                if (Cache.getCache("gps_address") != null)
                    MapUtil.updateAddress(AroundActivity.this,
                            AroundActivity.this.handler);
                if ((AroundActivity.location != null)
                        || (AroundActivity.this.locationing)) {
                    if (Cache.getCache("gps_address") == null) {
                        if (MapUtil.isInited())
                            MapUtil.reverseGeoCode(null, AroundActivity.this);
                    } else
                        MapUtil.updateAddress(AroundActivity.this,
                                AroundActivity.this.handler);
                } else
                    AroundActivity.this.requestLocation();
            }
        }.start();
    }

    protected void request() {
        request(this.type, 1);
    }

    public void showStore() {
        showList(25, this.storeListView);
    }

    protected class BankListviewAdapter extends BankCouponDownLoadAdapter {
        public BankListviewAdapter(DownloadListView paramBankCoupon,
                BankCoupon arg3) {
            super(paramBankCoupon, arg3);
        }

        public Context getContext() {
            return AroundActivity.this;
        }

        public void onNotifyDownload() {
            AroundActivity.this.request(35, 1 + getListCount() / 10);
        }
    }

    protected class GroupListviewAdapter extends GroupDownLoadAdapter {
        public GroupListviewAdapter(DownloadListView paramGroup, Group arg3) {
            super(paramGroup, arg3);
        }

        public Context getContext() {
            return AroundActivity.this;
        }

        public void onNotifyDownload() {
            AroundActivity.this.request(15, 1 + getListCount() / 10);
        }
    }

    protected class ListViewAdapter extends CouponDownLoadAdapter {
        public ListViewAdapter(boolean paramCoupon, Coupon arg3) {
            super(paramCoupon, null, arg3);
        }

        public Context getContext() {
            return AroundActivity.this;
        }

        public void onNotifyDownload() {
            AroundActivity localAroundActivity = AroundActivity.this;
            int i;
            if (!this.isStore)
                i = 5;
            else
                i = 25;
            localAroundActivity.request(i, 1 + getListCount() / 10);
        }
    }
}
