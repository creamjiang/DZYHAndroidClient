package com.dld.coupon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CompatibleDownloadAdapter;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.Showable;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.Bean;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Orders;
import com.dld.protocol.json.OrdersItem;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Ticket;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class MyDoujiaoActivity extends BaseActivity implements Showable {
    private static final String[] operations;
    private DownloadListView.DownLoadAdapter adapter;
    private Orders allOrders = new Orders();
    private DownloadListView.DownLoadAdapter allOrdersAdapter;
    private DownloadListView allOrdersListView;
    private TextView allOrdersTab;
    private RelativeLayout bankLayout;
    private TextView bankTextV;
    private Bean bean = new Bean();
    private Button changeButton = null;
    private String customerId;
    private LinearLayout favLayout;
    private int flag = 2;
    private RelativeLayout groupLayout;
    private TextView groupTextV;
    Handler handler = new Handler();
    private boolean isFirst = true;
    private String[] labels;
    private DownloadListView listView;
    private Button login_btnFav = null;
    private Button login_btnOrder = null;
    private String[] methods;
    private boolean[] noresultFlags = new boolean[2];
    private LinearLayout orderLayout;
    private upDateDoujiaoReceiver receiver;
    private boolean refresh = true;
    private Button setButton;
    private int showingTab = 1;
    private RelativeLayout storeLayout;
    private TextView storeTextV;
    List<String> ticketString = null;
    private TextView topTextView = null;
    private TextView total;
    private Orders unpaidOrders = new Orders();
    private DownloadListView.DownLoadAdapter unpaidOrdersAdapter;
    private DownloadListView unpaidOrdersListView;
    private TextView unpaidOrdersTab;
    private RelativeLayout youhuiLaout;
    private TextView youhuiTextV;

    static {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "查看";
        arrayOfString[1] = "删除";
        operations = arrayOfString;
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

    private void initAllOrders() {
        this.allOrders = new Orders();
        this.allOrdersListView = ((DownloadListView) findViewById(R.id.all_orders_list));
        this.allOrdersAdapter = new AllOrdersListviewAdapter(true);
        this.allOrdersListView.setAdapter(this.allOrdersAdapter);
        loadOrders(1, true);
    }

    private void initListView(DownloadListView paramDownloadListView,
            Bean paramBean) {
        paramDownloadListView
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(
                            AdapterView<?> paramAdapterView, View paramView,
                            int paramInt, long paramLong) {
                        new AlertDialog.Builder(ActivityManager.getCurrent())
                                .setTitle("请选择要执行的操作")
                                .setSingleChoiceItems(
                                        MyDoujiaoActivity.operations, 0,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface paramDialogInterface,
                                                    int paramInt) {
                                                if (paramInt != 0) {
                                                    final String str = SharePersistent
                                                            .getInstance()
                                                            .getPerference(
                                                                    ActivityManager
                                                                            .getCurrent(),
                                                                    "customer_id");
                                                    GenericDAO localGenericDAO = GenericDAO
                                                            .getInstance(ActivityManager
                                                                    .getCurrent());
                                                    DetailRef localDetailRef = (DetailRef) bean
                                                            .getDetails().get(
                                                                    paramInt);
                                                    final Detail localDetail = localDetailRef.detail;
                                                    int i = localDetailRef.type;
                                                    if (i != 2) {
                                                        if (i != 4) {
                                                            if (i != 3) {
                                                                if (i != 0) {
                                                                    if (i == 1)
                                                                        new Thread(
                                                                                new Runnable() {
                                                                                    public void run() {
                                                                                        Param localParam = new Param();
                                                                                        localParam
                                                                                                .append("userId",
                                                                                                        str)
                                                                                                .append("resourceId",
                                                                                                        ((CouponDetail) localDetail).id)
                                                                                                .append("type",
                                                                                                        "0");
                                                                                        ProtocolHelper
                                                                                                .deleteFavRequest(
                                                                                                        ActivityManager
                                                                                                                .getCurrent(),
                                                                                                        localParam,
                                                                                                        false)
                                                                                                .startTrans();
                                                                                    }
                                                                                })
                                                                                .start();
                                                                } else
                                                                    new Thread(
                                                                            new Runnable() {
                                                                                public void run() {
                                                                                    Param localParam = new Param();
                                                                                    localParam
                                                                                            .append("userId",
                                                                                                    str)
                                                                                            .append("resourceId",
                                                                                                    ((CouponDetail) localDetail).id)
                                                                                            .append("type",
                                                                                                    "7");
                                                                                    ProtocolHelper
                                                                                            .deleteFavRequest(
                                                                                                    ActivityManager
                                                                                                            .getCurrent(),
                                                                                                    localParam,
                                                                                                    false)
                                                                                            .startTrans();
                                                                                }
                                                                            })
                                                                            .start();
                                                            } else
                                                                new Thread(
                                                                        new Runnable() {
                                                                            public void run() {
                                                                                Param localParam = new Param();
                                                                                localParam
                                                                                        .append("userId",
                                                                                                str)
                                                                                        .append("resourceId",
                                                                                                String.valueOf(((BankCouponDetail) localDetail).id))
                                                                                        .append("type",
                                                                                                "5");
                                                                                ProtocolHelper
                                                                                        .deleteFavRequest(
                                                                                                ActivityManager
                                                                                                        .getCurrent(),
                                                                                                localParam,
                                                                                                false)
                                                                                        .startTrans();
                                                                            }
                                                                        })
                                                                        .start();
                                                        } else {
                                                            new Thread(
                                                                    new Runnable() {
                                                                        public void run() {
                                                                            Param localParam = new Param();
                                                                            localParam
                                                                                    .append("userId",
                                                                                            str)
                                                                                    .append("resourceId",
                                                                                            String.valueOf(((Ticket) localDetail).id))
                                                                                    .append("type",
                                                                                            "2")
                                                                                    .append("description",
                                                                                            String.valueOf(((Ticket) localDetail).id));
                                                                            ProtocolHelper
                                                                                    .deleteFavRequest(
                                                                                            ActivityManager
                                                                                                    .getCurrent(),
                                                                                            localParam,
                                                                                            false)
                                                                                    .startTrans();
                                                                        }
                                                                    }).start();
                                                            FileUtil.deleteTicketImage("http://www.dld.com/vlife/image?id="
                                                                    + ((Ticket) localDetail).id);
                                                        }
                                                    } else {
                                                        new Thread(
                                                                new Runnable() {
                                                                    public void run() {
                                                                        Param localParam = new Param();
                                                                        Object localObject1 = localParam
                                                                                .append("userId",
                                                                                        str);
                                                                        Object localObject2 = ((Param) localObject1)
                                                                                .append("resourceId",
                                                                                        (String) localObject1);
                                                                        if (((GroupDetail) localDetail).id > 0)
                                                                            localObject2 = ((GroupDetail) localDetail).id;
                                                                        else
                                                                            localObject2 = ((GroupDetail) localDetail).dealUrl;
                                                                        if (((GroupDetail) localDetail).id > 0)
                                                                            localObject1 = "6";
                                                                        else
                                                                            localObject1 = "3";
                                                                        ((Param) localObject2)
                                                                                .append("type",
                                                                                        (String) localObject1);
                                                                        ProtocolHelper
                                                                                .deleteFavRequest(
                                                                                        ActivityManager
                                                                                                .getCurrent(),
                                                                                        localParam,
                                                                                        false)
                                                                                .startTrans();
                                                                    }
                                                                }).start();
                                                        FileUtil.deleteGroupImage(((GroupDetail) localDetail).image);
                                                    }
                                                    localGenericDAO
                                                            .deleteFav(localDetailRef.detail
                                                                    .getDBId());
                                                    if ((i != 1) && (i != 0))
                                                        MyDoujiaoActivity.this
                                                                .onShowMyFav(i);
                                                    else
                                                        MyDoujiaoActivity.this
                                                                .onShowMyFav(0);
                                                } else {
                                                    MyDoujiaoActivity.this
                                                            .browse(bean,
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
                        MyDoujiaoActivity.this.browse(bean, paramInt);
                    }
                });
    }

    private void initMyFavUI() {
        init();
        initWidget();
        try {
            onShowMyFav(4);
            onShowOther();
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    private void initOrderUI() {
        initParam();
        initTabs();
        if (this.refresh) {
            initTitle();
            this.refresh = false;
            initAllOrders();
            initUnpaidOrders();
        }
    }

    private void initParam() {
        this.allOrdersTab = ((TextView) findViewById(R.id.all_orders));
        this.unpaidOrdersTab = ((TextView) findViewById(R.id.unpaid_orders));
        this.customerId = SharePersistent.getInstance().getPerference(this,
                "customer_id");
        this.labels = getLabels();
        this.methods = getMethods();
    }

    private void initTabs() {
        this.allOrdersTab.setText(this.labels[2]);
        this.unpaidOrdersTab.setText(this.labels[3]);
        this.allOrdersTab.setOnClickListener(new OnAllOrdersClick());
        this.unpaidOrdersTab.setOnClickListener(new OnUnpaidClick());
    }

    private void initTitle() {
        this.total = ((TextView) findViewById(R.id.order_total));
        this.total.setText(Html.fromHtml(this.labels[0]
                + "：<font color='#f28100'>0</font>笔　　" + this.labels[1]
                + "：<font color='#f28100'>0</font>笔"));
    }

    private void initUnpaidOrders() {
        this.unpaidOrders = new Orders();
        this.unpaidOrdersListView = ((DownloadListView) findViewById(R.id.unpaid_orders_list));
        this.unpaidOrdersAdapter = new AllOrdersListviewAdapter(false);
        this.unpaidOrdersListView.setAdapter(this.unpaidOrdersAdapter);
        loadOrders(1, false);
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
                MyDoujiaoActivity.this.onShowMyFav(2);
            }
        });
        this.youhuiLaout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MyDoujiaoActivity.this.onShowMyFav(4);
            }
        });
        this.storeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MyDoujiaoActivity.this.onShowMyFav(0);
            }
        });
        this.bankLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                MyDoujiaoActivity.this.onShowMyFav(3);
            }
        });
    }

    private void loadOrders(int paramInt, boolean paramBoolean) {
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
            this.handler.post(new Runnable() {
                public void run() {
                    MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                            .findViewById(R.id.no_result).setVisibility(8);
                }
            });
            Orders localOrders;
            Object localObject = new CProtocol(this, this.customerId,
                    new String(), new Param().append("p",
                            String.valueOf(paramInt)).append("s", "5"));
            if (!paramBoolean)
                localOrders = this.unpaidOrders;
            else
                localOrders = this.allOrders;
            final DownloadListView.DownLoadAdapter localDownLoadAdapter;
            if (!paramBoolean)
                localDownLoadAdapter = this.unpaidOrdersAdapter;
            else
                localDownLoadAdapter = this.allOrdersAdapter;
            if (!paramBoolean)
                localObject = this.unpaidOrdersListView;
            else
                localObject = this.allOrdersListView;
            if (paramInt == 1) {
                localOrders.list.clear();
                ((DownloadListView) localObject).reset();
            }
            if (!paramBoolean)
                localObject = this.methods[1];
            else
                localObject = this.methods[0];
            Cache.remove(((Protocol) localObject).getAbsoluteUrl());
            ((Protocol) localObject)
                    .startTrans(new Protocol.OnJsonProtocolResult() {
                        public void onException(String paramString,
                                Exception paramException) {
                            MyDoujiaoActivity.this.handler.post(new Runnable() {
                                public void run() {
                                    localDownLoadAdapter.notifyException();
                                }
                            });
                        }

                        public void onResult(String paramString,
                                Object paramObject) {
                            if (paramObject != null) {
                                final Object localObject = (Orders) paramObject;
                                if (!((Orders) localObject).list.isEmpty()) {
                                    MyDoujiaoActivity.this.handler
                                            .post(new Runnable() {
                                                public void run() {
                                                    if (((Orders) localObject).list
                                                            .containsAll(allOrders.list)) {
                                                        MyDoujiaoActivity.this
                                                                .showResult();
                                                    } else {
                                                        ((Orders) localObject).total = allOrders.total;
                                                        ((Orders) localObject).list
                                                                .addAll(allOrders.list);
                                                        localDownLoadAdapter
                                                                .notifyDownloadBack();
                                                        MyDoujiaoActivity.this.total.setText(Html
                                                                .fromHtml(MyDoujiaoActivity.this.labels[0]
                                                                        + "：<font color='#f28100'>"
                                                                        + MyDoujiaoActivity.this.allOrders.total
                                                                        + "</font>笔　　"
                                                                        + MyDoujiaoActivity.this.labels[1]
                                                                        + "：<font color='#f28100'>"
                                                                        + MyDoujiaoActivity.this.unpaidOrders.total
                                                                        + "</font>笔"));
                                                        MyDoujiaoActivity.this
                                                                .showResult();
                                                    }
                                                }
                                            });
                                } else {
                                    boolean[] local = MyDoujiaoActivity.this.noresultFlags;
                                    int i;
                                    if (!isFirst)
                                        i = 1;
                                    else
                                        i = 0;
                                    local[i] = true;
                                    if (((isFirst) && (MyDoujiaoActivity.this.showingTab == 1))
                                            || ((!isFirst) && (MyDoujiaoActivity.this.showingTab == 2)))
                                        MyDoujiaoActivity.this.showNoResult();
                                }
                            } else {
                                MyDoujiaoActivity.this.handler
                                        .post(new Runnable() {
                                            public void run() {
                                                allOrdersAdapter
                                                        .notifyException();
                                            }
                                        });
                            }
                        }
                    });
        } else {
            this.noresultFlags[0] = true;
            this.noresultFlags[1] = true;
            showNoResult();
        }
    }

    private void regReceiver() {
        this.receiver = new upDateDoujiaoReceiver();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("dld.change");
        registerReceiver(this.receiver, localIntentFilter);
    }

    private void showNoResult() {
        this.handler.post(new Runnable() {
            public void run() {
                MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                        .findViewById(R.id.no_result).setVisibility(0);
                ImageView localImageView = (ImageView) MyDoujiaoActivity.this
                        .findViewById(R.id.orderlayout).findViewById(
                                R.id.no_result);
                if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
                    localImageView.setImageResource(R.drawable.no_result);
                    MyDoujiaoActivity.this.login_btnOrder.setVisibility(8);
                } else {
                    localImageView.setImageResource(R.drawable.unlogintip);
                    MyDoujiaoActivity.this.login_btnOrder.setVisibility(0);
                }
                MyDoujiaoActivity.this.allOrdersListView.setVisibility(8);
                MyDoujiaoActivity.this.unpaidOrdersListView.setVisibility(8);
            }
        });
    }

    private void showResult() {
        this.handler.post(new Runnable() {
            public void run() {
                MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                        .findViewById(R.id.no_result).setVisibility(8);
                MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                        .findViewById(R.id.login_btn).setVisibility(8);
                if (MyDoujiaoActivity.this.showingTab != 1) {
                    if (MyDoujiaoActivity.this.showingTab == 2)
                        MyDoujiaoActivity.this.unpaidOrdersListView
                                .setVisibility(0);
                } else
                    MyDoujiaoActivity.this.allOrdersListView.setVisibility(0);
                MyDoujiaoActivity.this.total.setText(Html
                        .fromHtml(MyDoujiaoActivity.this.labels[0]
                                + "：<font color='#f28100'>"
                                + MyDoujiaoActivity.this.allOrders.total
                                + "</font>笔　　"
                                + MyDoujiaoActivity.this.labels[1]
                                + "：<font color='#f28100'>"
                                + MyDoujiaoActivity.this.unpaidOrders.total
                                + "</font>笔"));
            }
        });
    }

    public void LogOff() {
        this.refresh = true;
        if (this.flag != 1) {
            if (this.flag == 2)
                initMyFavUI();
        } else {
            initParam();
            this.total = ((TextView) findViewById(R.id.order_total));
            this.total.setText(Html.fromHtml(this.labels[0]
                    + "：<font color='#f28100'>0</font>笔　　" + this.labels[1]
                    + "：<font color='#f28100'>0</font>笔"));
            initTabs();
            initAllOrders();
            initUnpaidOrders();
        }
    }

    public void LogOn() {
        this.refresh = true;
        if (this.flag != 1) {
            if (this.flag == 2) {
                this.noresultFlags[0] = false;
                this.noresultFlags[1] = false;
                initMyFavUI();
            }
        } else {
            initParam();
            this.noresultFlags[0] = false;
            this.noresultFlags[1] = false;
            initTabs();
            initAllOrders();
            initUnpaidOrders();
        }
    }

    public void finish() {
        super.finish();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
            this.receiver = null;
        }
    }

    protected View getItemView(LayoutInflater paramLayoutInflater,
            OrdersItem paramOrdersItem, boolean paramBoolean) {
        View localView = paramLayoutInflater.inflate(R.layout.order_item, null);
        ((TextView) localView.findViewById(R.id.product))
                .setText(paramOrdersItem.product);
        TextView localTextView = (TextView) localView.findViewById(R.id.state);
        localTextView.setText(paramOrdersItem.getState());
        localTextView.setBackgroundResource(paramOrdersItem.getImage());
        ((TextView) localView.findViewById(R.id.website))
                .setText(paramOrdersItem.seller);
        ((TextView) localView.findViewById(R.id.quantity))
                .setText(paramOrdersItem.quantity + "份");
        ((TextView) localView.findViewById(R.id.amount)).setText("¥"
                + paramOrdersItem.amount);
        return localView;
    }

    protected String[] getLabels() {
        String[] arrayOfString = new String[4];
        arrayOfString[0] = "订单总数";
        arrayOfString[1] = "未付款";
        arrayOfString[2] = "全部订单";
        arrayOfString[3] = "未付款";
        return arrayOfString;
    }

    protected String[] getMethods() {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "all_orders";
        arrayOfString[1] = "unpaid_orders";
        return arrayOfString;
    }

    protected String getTitleString() {
        return "打折店-我的订单";
    }

    public void init() {
        this.listView = ((DownloadListView) findViewById(R.id.listview));
        this.adapter = new ListViewAdapter(this.bean);
        this.listView.setAdapter(this.adapter);
        initListView(this.listView, this.bean);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.mydoujiao);
        regReceiver();
        this.orderLayout = ((LinearLayout) findViewById(R.id.orderlayout));
        this.favLayout = ((LinearLayout) findViewById(R.id.favlayout));
        this.changeButton = ((Button) findViewById(R.id.feedback_button));
        this.topTextView = ((TextView) findViewById(R.id.title_text));
        this.setButton = ((Button) findViewById(R.id.set_button));
        this.setButton.setVisibility(0);
        if (this.isFirst) {
            this.login_btnFav = ((Button) this.favLayout
                    .findViewById(R.id.login_btn));
            this.login_btnFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    Intent localIntent = new Intent(MyDoujiaoActivity.this,
                            LoginActivity.class);
                    Cache.put("mydoujiaoshow", "mydoujiaoshow");
                    MyDoujiaoActivity.this.startActivity(localIntent);
                }
            });
            this.login_btnOrder = ((Button) this.orderLayout
                    .findViewById(R.id.login_btn));
            this.login_btnOrder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    Intent localIntent = new Intent(MyDoujiaoActivity.this,
                            LoginActivity.class);
                    Cache.put("mydoujiaoshow", "mydoujiaoshow");
                    MyDoujiaoActivity.this.startActivity(localIntent);
                }
            });
            this.setButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    Intent localIntent = new Intent(MyDoujiaoActivity.this,
                            MydoujiaoSettingActivity.class);
                    MyDoujiaoActivity.this.startActivity(localIntent);
                }
            });
            this.changeButton.setVisibility(0);
            this.changeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    if (MyDoujiaoActivity.this.flag != 1) {
                        MyDoujiaoActivity.this.changeButton.setText("我的收藏");
                        MyDoujiaoActivity.this.topTextView.setText("我的订单");
                        MyDoujiaoActivity.this.favLayout.setVisibility(8);
                        MyDoujiaoActivity.this.orderLayout.setVisibility(0);
                        MyDoujiaoActivity.this.initOrderUI();
                        MyDoujiaoActivity.this.flag = 1;
                    } else {
                        MyDoujiaoActivity.this.favLayout.setVisibility(0);
                        MyDoujiaoActivity.this.orderLayout.setVisibility(8);
                        MyDoujiaoActivity.this.initMyFavUI();
                        MyDoujiaoActivity.this.topTextView.setText("我的收藏");
                        MyDoujiaoActivity.this.flag = 2;
                        MyDoujiaoActivity.this.changeButton.setText("我的订单");
                    }
                }
            });
        }
        if (this.flag != 1) {
            this.topTextView.setText("我的收藏");
            this.changeButton.setText("我的订单");
        } else {
            this.topTextView.setText("我的订单");
            this.changeButton.setText("我的收藏");
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return false;
    }

    protected void onResume() {
        super.onResume();
        LogOn();
    }

    public void onShow() {
    }

    public void onShow(int paramInt) {
        if ((paramInt != 1) && (paramInt != 0))
            onShowMyFav(paramInt);
        else
            onShowMyFav(0);
    }

    public void onShowMyFav(int paramInt) {
        LogUtils.log("main", " onshow start" + System.currentTimeMillis());
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
            Object localObject = GenericDAO.getInstance();
            if (paramInt != 0)
                localObject = ((GenericDAO) localObject).listFavs(paramInt);
            else
                localObject = ((GenericDAO) localObject).listFavsOfStore();
            if (!((List) localObject).isEmpty()) {
                this.listView.setVisibility(0);
                findViewById(R.id.no_result).setVisibility(8);
                this.login_btnFav.setVisibility(8);
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
                this.login_btnFav.setVisibility(8);
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
            this.login_btnFav.setVisibility(0);
        }
    }

    public void onShowOther() {
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
            GenericDAO localGenericDAO = GenericDAO.getInstance();
            int j = localGenericDAO.getFavCountByType(2);
            this.groupTextV.setText("团购(" + j + ")");
            j = localGenericDAO.getFavCountByType(3);
            this.bankTextV.setText("卡优惠(" + j + ")");
            int i = localGenericDAO.getStoreCount();
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

    class AllOrdersListviewAdapter extends DownloadListView.DownLoadAdapter {
        private boolean all;
        private Orders orders;

        public AllOrdersListviewAdapter(boolean arg2) {
            boolean bool = false;
            this.all = bool;
            Orders localOrders;
            if (!bool)
                localOrders = MyDoujiaoActivity.this.unpaidOrders;
            else
                localOrders = MyDoujiaoActivity.this.allOrders;
            this.orders = localOrders;
        }

        public Context getContext() {
            return MyDoujiaoActivity.this;
        }

        public Object getItem(int paramInt) {
            return MyDoujiaoActivity.this.allOrders.list.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public int getListCount() {
            return this.orders.list.size();
        }

        public int getMax() {
            return this.orders.total;
        }

        public View getView(int paramInt) {
            OrdersItem localOrdersItem = (OrdersItem) this.orders.list
                    .get(paramInt);
            LayoutInflater localLayoutInflater = (LayoutInflater) getContext()
                    .getSystemService("layout_inflater");
            return MyDoujiaoActivity.this.getItemView(localLayoutInflater,
                    localOrdersItem, this.all);
        }

        public void onNotifyDownload() {
            MyDoujiaoActivity.this.loadOrders(1 + this.orders.list.size() / 5,
                    this.all);
        }
    }

    private class ListViewAdapter extends CompatibleDownloadAdapter {
        public ListViewAdapter(Bean arg2) {
            super(arg2);
        }

        public Context getContext() {
            return MyDoujiaoActivity.this;
        }

        public void onNotifyDownload() {
            MyDoujiaoActivity.this.handler.post(new Runnable() {
                public void run() {
                    MyDoujiaoActivity.this.onShowMyFav(4);
                }
            });
        }
    }

    class OnAllOrdersClick implements View.OnClickListener {
        OnAllOrdersClick() {
        }

        public void onClick(View paramView) {
            MyDoujiaoActivity.this.showingTab = 1;
            MyDoujiaoActivity.this.allOrdersTab
                    .setBackgroundResource(R.drawable.tab_selected);
            MyDoujiaoActivity.this.unpaidOrdersTab
                    .setBackgroundResource(R.drawable.tab_unselected);
            if (MyDoujiaoActivity.this.noresultFlags[0] == false) {
                MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                        .findViewById(R.id.no_result).setVisibility(8);
                MyDoujiaoActivity.this.unpaidOrdersListView.setVisibility(8);
                MyDoujiaoActivity.this.allOrdersListView.setVisibility(0);
            } else {
                MyDoujiaoActivity.this.showNoResult();
            }
        }
    }

    class OnUnpaidClick implements View.OnClickListener {
        OnUnpaidClick() {
        }

        public void onClick(View paramView) {
            MyDoujiaoActivity.this.showingTab = 2;
            MyDoujiaoActivity.this.allOrdersTab
                    .setBackgroundResource(R.drawable.tab_unselected);
            MyDoujiaoActivity.this.unpaidOrdersTab
                    .setBackgroundResource(R.drawable.tab_selected);
            if (MyDoujiaoActivity.this.noresultFlags[1] == true) {
                MyDoujiaoActivity.this.findViewById(R.id.orderlayout)
                        .findViewById(R.id.no_result).setVisibility(8);
                MyDoujiaoActivity.this.allOrdersListView.setVisibility(8);
                MyDoujiaoActivity.this.unpaidOrdersListView.setVisibility(0);
            } else {
                MyDoujiaoActivity.this.showNoResult();
            }
        }
    }

    class upDateDoujiaoReceiver extends BroadcastReceiver {
        upDateDoujiaoReceiver() {
        }

        public void onReceive(Context paramContext, Intent paramIntent) {
            MyDoujiaoActivity.this.LogOff();
        }
    }
}
