package com.dld.coupon.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.OnTouchListenerImpl;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.FileUtil;
import com.dld.coupon.util.ServiceUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CardDialogue;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CommonFav;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailDelete;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Ticket;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MyDldSettingActivity extends BaseActivity implements
        View.OnClickListener {
    private final int SYNFAV_FAILURE = 2;
    private final int SYNFAV_SUCCESS = 1;
    private RelativeLayout aboutDoujiaoLayout;
    private RelativeLayout bankSetLayout;
    private RelativeLayout changeCityLayout;
    private RelativeLayout feedBackLayout;
    Handler handler = new Handler();
    private ProgressDialog pdd = null;
    private CheckBox storeAndSendBox;
    Handler synHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
            default:
            case 1:
            case 2:
            }
            while (true) {
                // return;
                try {
                    MyDldSettingActivity.this.pdd.dismiss();
                    new AlertDialog.Builder(ActivityManager.getCurrent())
                            .setTitle("提示")
                            .setMessage("恭喜您，已成功将收藏同步到您的手机!")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface paramDialogInterface,
                                                int paramInt) {
                                        }
                                    }).setCancelable(false).create().show();
                } catch (Exception localException1) {
                }
                continue;
                // try
                // {
                // MyCardSettingActivity.this.pdd.dismiss();
                // new
                // AlertDialog.Builder(MyCardSettingActivity.this).setTitle("提示").setMessage("抱歉,收藏同步到手机失败,请您稍候再试!").setPositiveButton("确定",
                // new DialogInterface.OnClickListener()
                // {
                // public void onClick(DialogInterface paramDialogInterface, int
                // paramInt)
                // {
                // }
                // }).setCancelable(false).create().show();
                // }
                // catch (Exception localException2)
                // {
                // }
            }
        }
    };
    private Button syn_btn = null;
    private TextView topTextView;
    private RelativeLayout userAgreementLayout;

    private void initWidget() {
        this.pdd = DialogHelper.getProgressBarNoShow("正在将网站的收藏同步到手机，请稍候!");
        this.topTextView = ((TextView) findViewById(R.id.topText));
        this.topTextView.setText("设置");
        this.changeCityLayout = ((RelativeLayout) findViewById(R.id.choice_city));
        this.changeCityLayout.setOnClickListener(this);
        this.changeCityLayout.setOnTouchListener(new OnTouchListenerImpl(
                this.changeCityLayout));
        this.bankSetLayout = ((RelativeLayout) findViewById(R.id.set_bank));
        this.bankSetLayout.setOnClickListener(this);
        this.bankSetLayout.setOnTouchListener(new OnTouchListenerImpl(
                this.bankSetLayout));
        this.userAgreementLayout = ((RelativeLayout) findViewById(R.id.agreement));
        this.userAgreementLayout.setOnClickListener(this);
        this.userAgreementLayout.setOnTouchListener(new OnTouchListenerImpl(
                this.userAgreementLayout));
        this.feedBackLayout = ((RelativeLayout) findViewById(R.id.feedback));
        this.feedBackLayout.setOnClickListener(this);
        this.feedBackLayout.setOnTouchListener(new OnTouchListenerImpl(
                this.feedBackLayout));
        this.aboutDoujiaoLayout = ((RelativeLayout) findViewById(R.id.about_dld));
        this.aboutDoujiaoLayout.setOnClickListener(this);
        this.aboutDoujiaoLayout.setOnTouchListener(new OnTouchListenerImpl(
                this.aboutDoujiaoLayout));
        this.storeAndSendBox = ((CheckBox) findViewById(R.id.store_and_share));
        this.storeAndSendBox.setChecked(SharePersistent.getInstance()
                .getPerferenceBoolean(this, "isallowsendweibo"));
        this.storeAndSendBox.setOnTouchListener(new OnTouchListenerImpl(
                this.storeAndSendBox));
        this.storeAndSendBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton paramCompoundButton,
                            boolean paramBoolean) {
                        SharePersistent.getInstance().savePerference(
                                MyDldSettingActivity.this,
                                "isallowsendweibo", paramBoolean);
                    }
                });
        this.syn_btn = ((Button) findViewById(R.id.syn_btn));
        this.syn_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                final String str = SharePersistent.getInstance().getPerference(
                        MyDldSettingActivity.this, "customer_id");
                if (StringUtils.isEmpty(str))
                    Toast.makeText(MyDldSettingActivity.this,
                            "您还没有登录，请登录后同步!", 1).show();
                // while (true)
                {
                    // return;
                    try {
                        MyDldSettingActivity.this.pdd.show();
                        new Thread(new Runnable() {
                            public void run() {
                                Object localObject = GenericDAO.getInstance();
                                String str5 = ((GenericDAO) localObject)
                                        .getIdStringByType(4);
                                String str4 = ((GenericDAO) localObject)
                                        .getIdStringByType(3);
                                String str3 = ((GenericDAO) localObject)
                                        .getIdStringByType(2);
                                String str1 = ((GenericDAO) localObject)
                                        .getCanByGroupString(2);
                                String str2 = ((GenericDAO) localObject)
                                        .getStoreIdString();
                                localObject = ((GenericDAO) localObject)
                                        .getCouponIdString();
                                Param localParam = new Param();
                                localParam.append("userId", str)
                                        .append("type_0", (String) localObject)
                                        .append("type_2", str5)
                                        .append("type_3", str3)
                                        .append("type_4", "")
                                        .append("type_5", str4)
                                        .append("type_6", str1)
                                        .append("type_7", str2);
                                LogUtils.log("main", "primary params"
                                        + localParam.toStringfrome());
                                ProtocolHelper
                                        .synchroFavRequest(
                                                MyDldSettingActivity.this,
                                                localParam, false)
                                        .startTransForUser(
                                                new MyDldSettingActivity.CommonFavResult(),
                                                localParam);
                            }
                        }).start();
                    } catch (Exception localException) {
                        MyDldSettingActivity.this.synHandler
                                .sendEmptyMessage(2);
                    }
                }
            }
        });
        View localView = findViewById(R.id.invite);
        localView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                if (!StringUtils.isEmpty(SharePersistent.get("customer_id")))
                    Common.startActivity(InviteActivity.class);
                else
                    Common.startActivity(LoginActivity.class);
            }
        });
        localView.setOnTouchListener(new OnTouchListenerImpl(localView));
    }

    public void onClick(View paramView) {
        Object localObject;
        switch (paramView.getId()) {
        case R.id.choice_city:
            localObject = new Intent();
            ((Intent) localObject).setClass(this, CityActivity.class);
            startActivity((Intent) localObject);
            break;
        case R.id.set_bank:
            CardDialogue.show(this, null);
            break;
        case R.id.agreement:
            ServiceUtil.show(null, this.handler);
            break;
        case R.id.feedback:
            localObject = new Intent();
            ((Intent) localObject).setClass(this, FeedbackActivity.class);
            startActivity((Intent) localObject);
            break;
        case R.id.about_dld:
            localObject = new AlertDialog.Builder(this);
            ((AlertDialog.Builder) localObject).setTitle("关于");
            ((AlertDialog.Builder) localObject)
                    .setMessage("打折店优惠\n\n版本 1.0\n\n网址: www.dld.com");
            ((AlertDialog.Builder) localObject).setPositiveButton("返回",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                        }
                    });
            ((AlertDialog.Builder) localObject).show();
        case R.id.store_share:
        case R.id.store_and_share:
        case R.id.syn_layout:
        case R.id.syn_btn:
        case R.id.invite:
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.mydldsetting);
        initWidget();
        initSegment();
    }

    private class CommonFavResult extends Protocol.OnJsonProtocolResult {
        public CommonFavResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.log("main", "on result for syn exception");
            MyDldSettingActivity.this.synHandler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                Object localObject1 = (CommonFav) paramObject;
                Object localObject2 = ((CommonFav) localObject1).details;
                Object localObject5 = ((CommonFav) localObject1).detailsDeleteList;
                LogUtils.log("main", ((List) localObject5).size()
                        + "/// deleteList size");
                localObject1 = GenericDAO
                        .getInstance(MyDldSettingActivity.this);
                Object localObject3 = (HashMap) ((GenericDAO) localObject1)
                        .listAsMap();
                LogUtils.log("main", ((HashMap) localObject3).entrySet().size()
                        + "allMap size");
                Object localObject4 = new HashMap();
                if ((localObject5 != null)
                        && (((List) localObject5).size() > 0)) {
                    Iterator localIterator = ((List) localObject5).iterator();
                    while (localIterator.hasNext()) {
                        localObject5 = (DetailDelete) localIterator.next();
                        if (!((HashMap) localObject3)
                                .containsKey(((DetailDelete) localObject5).id))
                            continue;
                        ((HashMap) localObject4).put(
                                ((DetailDelete) localObject5).id,
                                (DetailRef) ((HashMap) localObject3)
                                        .get(((DetailDelete) localObject5).id));
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
                                    .getValue()).detail.getDBId());
                }
                if (localObject2 != null) {
                    localObject3 = new ArrayList();
                    localObject2 = ((List) localObject2).iterator();
                    while (((Iterator) localObject2).hasNext()) {
                        localObject4 = (DetailRef) ((Iterator) localObject2)
                                .next();
                        ((GenericDAO) localObject1).saveFav(
                                ((DetailRef) localObject4).type,
                                ((DetailRef) localObject4).detail);
                        if (((DetailRef) localObject4).type != 4)
                            continue;
                        ((List) localObject3)
                                .add((Ticket) ((DetailRef) localObject4).detail);
                    }
                    new Thread() {
                        private void doRun(List<Ticket> paramList)
                                throws Exception {
                            Iterator localIterator = paramList.iterator();
                            while (localIterator.hasNext()) {
                                Object localObject = (Ticket) localIterator
                                        .next();
                                MyDldSettingActivity localMyCardSettingActivity = MyDldSettingActivity.this;
                                String str2 = "http://www.dld.com/vlife/image?id="
                                        + ((Ticket) localObject).id;
                                String str1 = "http://www.dld.com/tuan/viewimage?u="
                                        + URLEncoder.encode(str2)
                                        + "&w=140&h=200";
                                localObject = "http://www.dld.com/tuan/viewimage?u="
                                        + URLEncoder.encode(str2)
                                        + "&w=600&h=600";
                                readData(localMyCardSettingActivity, str2,
                                        str1);
                                readData(localMyCardSettingActivity, str2,
                                        (String) localObject);
                                FileUtil.saveTicketImage(str2);
                            }
                        }

                        // ERROR //
                        private void readData(
                                android.app.Activity paramActivity,
                                String paramString1, String paramString2) {
                            // Byte code:
                            // 0: aconst_null
                            // 1: astore 4
                            // 3: aconst_null
                            // 4: checkcast 93 [B
                            // 7: astore 5
                            // 9: aload_2
                            // 10: aload_1
                            // 11: invokestatic 99
                            // com/doujiao/android/net/HttpUtil:getInputStreamByUrl
                            // (Ljava/lang/String;Landroid/content/Context;)Ljava/io/InputStream;
                            // 14: astore 4
                            // 16: aload 4
                            // 18: invokestatic 103
                            // com/doujiao/coupon/util/FileUtil:getByteFromInputStream
                            // (Ljava/io/InputStream;)[B
                            // 21: astore 5
                            // 23: aload 5
                            // 25: astore 5
                            // 27: aload 4
                            // 29: ifnull +8 -> 37
                            // 32: aload 4
                            // 34: invokevirtual 108 java/io/InputStream:close
                            // ()V
                            // 37: aload 5
                            // 39: ifnull +17 -> 56
                            // 42: aload 5
                            // 44: arraylength
                            // 45: bipush 10
                            // 47: if_icmplt +9 -> 56
                            // 50: aload_3
                            // 51: aload 5
                            // 53: invokestatic 113
                            // com/tencent/weibo/utils/Cache:put
                            // (Ljava/lang/String;Ljava/lang/Object;)V
                            // 56: return
                            // 57: astore 6
                            // 59: ldc 115
                            // 61: aload 6
                            // 63: invokestatic 121
                            // com/doujiao/android/util/LogUtils:e
                            // (Ljava/lang/String;Ljava/lang/Exception;)V
                            // 66: aload 4
                            // 68: ifnull -31 -> 37
                            // 71: aload 4
                            // 73: invokevirtual 108 java/io/InputStream:close
                            // ()V
                            // 76: goto -39 -> 37
                            // 79: astore 4
                            // 81: ldc 115
                            // 83: aload 4
                            // 85: invokestatic 121
                            // com/doujiao/android/util/LogUtils:e
                            // (Ljava/lang/String;Ljava/lang/Exception;)V
                            // 88: goto -51 -> 37
                            // 91: astore 5
                            // 93: aload 4
                            // 95: ifnull +8 -> 103
                            // 98: aload 4
                            // 100: invokevirtual 108 java/io/InputStream:close
                            // ()V
                            // 103: aload 5
                            // 105: athrow
                            // 106: astore 4
                            // 108: ldc 115
                            // 110: aload 4
                            // 112: invokestatic 121
                            // com/doujiao/android/util/LogUtils:e
                            // (Ljava/lang/String;Ljava/lang/Exception;)V
                            // 115: goto -12 -> 103
                            // 118: astore 4
                            // 120: ldc 115
                            // 122: aload 4
                            // 124: invokestatic 121
                            // com/doujiao/android/util/LogUtils:e
                            // (Ljava/lang/String;Ljava/lang/Exception;)V
                            // 127: goto -90 -> 37
                            //
                            // Exception table:
                            // from to target type
                            // 9 23 57 java/lang/Exception
                            // 71 76 79 java/lang/Exception
                            // 9 23 91 finally
                            // 59 66 91 finally
                            // 98 103 106 java/lang/Exception
                            // 32 37 118 java/lang/Exception
                        }

                        public void run() {
                            try {
                                // doRun();
                                return;
                            } catch (Exception localException) {
                                // while (true)
                                LogUtils.e("test", localException);
                            }
                        }
                    }.start();
                    MyDldSettingActivity.this.handler.post(new Runnable() {
                        public void run() {
                            MyDldSettingActivity.this.synHandler
                                    .sendEmptyMessage(1);
                        }
                    });
                }
            }
        }
    }
}
