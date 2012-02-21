package com.dld.coupon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.pay.MobileSecurePayHelper;
import com.dld.android.pay.MobileSecurePayer;
import com.dld.android.pay.ResultChecker;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.CustomerBank;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.OrderResponse;
import com.dld.protocol.json.PayResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Response;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tenpay.android.service.TenpayServiceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmPayActivity extends BaseActivity implements
        View.OnClickListener {
    private final int CANCELLFAILURE = 9;
    private final int CANCELLSUCCESS = 8;
    private final int MSG_PAY_RESULT = 6;
    private final int SENDFAILUREFAILURE = 13;
    private final int SENDFAILURESUCCESS = 12;
    private final int STARTCAIFUTONGPAY = 7;
    private final int ZHIFUBAORESULT = 11;
    private final int ZHIFUBAOSUCCESS = 10;
    private String account = null;
    private TextView addNewBankCardTv = null;
    private String addressText;
    private String bankAddress = null;
    private RadioGroup bank_RadioRg;
    private String bankcardNum;
    private int bankcardType = 1;
    private TextView buyNmuberTv = null;
    private int buyNumber;
    private RelativeLayout caifuLayout;
    private ImageView caifuTongImageV = null;
    private final int caifutongPayFailure = 5;
    Thread caifutongThread = new Thread(new Runnable() {
        public void run() {
            ConfirmPayActivity.this.sendCaifuTongPay();
        }
    });
    private ProgressDialog cancellDialog;
    private ProgressDialog cancellforFailure;
    private String[] categoryArray = null;
    private String cid;
    private Button confirmPayBt = null;
    private CustomerBank customBank;
    private ArrayList<CustomerBank> customBankList = null;
    private RelativeLayout deal_cardLayout;
    private String depositBank;
    private String depositOwner;
    private GroupDetail detail;
    private String detailAddress = null;
    private TextView editBankCardTv = null;
    Runnable editBankRunnable = new Runnable() {
        public void run() {
            GenericDAO
                    .getInstance(ConfirmPayActivity.this)
                    .deleteAndInsertBank(
                            new CustomerBank(
                                    Integer.valueOf(ConfirmPayActivity.this.bankcardType),
                                    ConfirmPayActivity.this.addressText,
                                    ConfirmPayActivity.this.depositOwner,
                                    ConfirmPayActivity.this.bankcardNum,
                                    ConfirmPayActivity.this.depositBank,
                                    ConfirmPayActivity.this.identiferId,
                                    ConfirmPayActivity.this.totalInfo,
                                    ConfirmPayActivity.this.mobile,
                                    Integer.valueOf(ConfirmPayActivity.this.id)));
            ConfirmPayActivity.this.handler.sendEmptyMessage(3);
        }
    };
    private RadioButton firstRadioButton = null;
    private float freight;
    private TextView freightChargeTv = null;
    private RelativeLayout freightchargeLayout = null;
    private Handler handler = new Handler();
    // {
    // public void handleMessage(Message paramMessage)
    // {
    // super.handleMessage(paramMessage);
    // if (ConfirmPayActivity.this.sendPayDialog != null)
    // ConfirmPayActivity.this.sendPayDialog.dismiss();
    // if (ConfirmPayActivity.this.cancellDialog.isShowing())
    // ConfirmPayActivity.this.cancellDialog.dismiss();
    // switch (paramMessage.what)
    // {
    // case 4:
    // default:
    // case 13:
    // case 12:
    // case 0:
    // case 9:
    // case 8:
    // case 1:
    // case 2:
    // case 3:
    // case 5:
    // case 10:
    // case 11:
    // case 6:
    // case 7:
    // }
    // while (true)
    // {
    // return;
    // if (ConfirmPayActivity.this.cancellforFailure != null)
    // ConfirmPayActivity.this.cancellforFailure.dismiss();
    // Object localObject1 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("提示").setMessage("请您重新下单进行支付！").setIcon(R.drawable.infoicon);
    // Object localObject3 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // ConfirmPayActivity.this.closeThisActivity();
    // }
    // };
    // ((AlertDialog.Builder)localObject1).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject3).show();
    // continue;
    // if (ConfirmPayActivity.this.cancellforFailure != null)
    // ConfirmPayActivity.this.cancellforFailure.dismiss();
    // localObject3 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("提示").setMessage("你可以更改支付方式进行支付！").setIcon(R.drawable.infoicon);
    // localObject1 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject3).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject1).show();
    // continue;
    // localObject3 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("提示").setMessage("支付请求发送成功，您将收到电话通知付费!").setIcon(R.drawable.infoicon);
    // localObject1 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // ConfirmPayActivity.this.closeThisActivity();
    // }
    // };
    // ((AlertDialog.Builder)localObject3).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject1).show();
    // continue;
    // Toast.makeText(ConfirmPayActivity.this, "请您重新下单支付", 1).show();
    // continue;
    // Toast.makeText(ConfirmPayActivity.this, "支付已取消", 1).show();
    // continue;
    // Toast.makeText(ConfirmPayActivity.this,
    // ConfirmPayActivity.this.orderResponse.message, 1).show();
    // continue;
    // Toast.makeText(ConfirmPayActivity.this, "由于网络原因支付请求发送失败，请您稍后再试",
    // 1).show();
    // ConfirmPayActivity.this.closeThisActivity();
    // continue;
    // ConfirmPayActivity.this.refreshBankInfo();
    // continue;
    // Toast.makeText(ConfirmPayActivity.this,
    // ConfirmPayActivity.this.payResponse.message, 1).show();
    // continue;
    // new
    // MobileSecurePayer().pay(ConfirmPayActivity.this.payResponse.orderInfo,
    // ConfirmPayActivity.this.handler, 11, ConfirmPayActivity.this);
    // continue;
    // localObject1 = null;
    //
    // String localObject6 = null;
    // try
    // {
    // localObject1 = (String)paramMessage.obj;
    // LogUtils.log("test", localObject1);
    // int j = ((String)localObject1).indexOf("memo=");
    // LogUtils.log("main", j + "/ start");
    // int k = j + "memo=".length();
    // j = ((String)localObject1).indexOf(";result=");
    // int n = ((String)localObject1).indexOf("resultStatus=") +
    // "resultStatus=".length();
    // LogUtils.log("main", "codestart" + n);
    // int m = n + 1;
    // n += 5;
    // localObject6 = ((String)localObject1).substring(m, n);
    // LogUtils.log("main", "code==" + (String)localObject6);
    // ((String)localObject1).substring(k, j);
    // ResultChecker localResultChecker = new
    // ResultChecker((String)localObject1);
    // if (localResultChecker.checkSign() != 1)
    // break label677;
    // ConfirmPayActivity.this.showDialog(ConfirmPayActivity.this, "提示",
    // "您的订单信息已被非法篡改", 17301543);
    // }
    // catch (Exception localException)
    // {
    // localException.printStackTrace();
    // ConfirmPayActivity.this.showDialog(ConfirmPayActivity.this, "提示",
    // (String)localObject1, R.drawable.infoicon);
    // }
    // continue;
    // label677: if ("6001".equals(localObject6))
    // {
    // ConfirmPayActivity.this.cancellDialog.show();
    // LogUtils.log("main", "pid==" + ConfirmPayActivity.this.payResponse.id);
    // // localObject4 = new CProtocol(ConfirmPayActivity.this,
    // String.valueOf(ConfirmPayActivity.this.payResponse.id),
    // "cancellzhifubao", null);
    // // ((Protocol)localObject4).startTransForUser(new
    // ConfirmPayActivity.CancellPayProtocolResult(), new Param().append("st",
    // "TRADE_CANCELED"));
    // continue;
    // }
    // if ("9000".equals(localObject6))
    // {
    // // localObject5 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("提示").setMessage("支付成功").setIcon(R.drawable.infoicon);
    // // localObject4 = new DialogInterface.OnClickListener()
    // // {
    // // public void onClick(DialogInterface paramDialogInterface, int
    // paramInt)
    // // {
    // // ConfirmPayActivity.this.closeThisActivity();
    // // }
    // // };
    // ((AlertDialog.Builder)localObject5).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject4).show();
    // continue;
    // }
    // ConfirmPayActivity.this.cancellforFailure.show();
    // // Object localObject4 = new CProtocol(ConfirmPayActivity.this,
    // ConfirmPayActivity.this.payResponse.id, "cancellzhifubao", new
    // Param().append("st", "TRADE_CANCELED"));
    // // ((Protocol)localObject4).startTransForUser(new
    // ConfirmPayActivity.CancellPayProtocolResult(), new Param().append("st",
    // "TRADE_FAILED"));
    // continue;
    // // Object localObject6 = (String)paramMessage.obj;
    // localObject1 = null;
    // // localObject4 = null;
    // Object localObject5 = null;
    // try
    // {
    // localObject6 = new JSONObject((String)localObject6);
    // if (localObject6 != null)
    // {
    // localObject1 = ((JSONObject)localObject6).getString("statusCode");
    // localObject4 = ((JSONObject)localObject6).getString("info");
    // localObject5 = ((JSONObject)localObject6).getString("result");
    // localObject5 = localObject5;
    // }
    // new
    // StringBuilder("statusCode = ").append((String)localObject1).append(", info = ").append((String)localObject4).append(", result = ").append((String)localObject5).toString();
    // if ("0".equals(localObject1))
    // {
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("支付成功");
    // localObject1 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject1).setCancelable(false).create().show();
    // }
    // }
    // catch (JSONException localJSONException)
    // {
    // while (true)
    // localJSONException.printStackTrace();
    // if ("66200000".equals(localObject1))
    // {
    // localObject1 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("网络异常。支付失败");
    // localObject4 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject1).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject4).setCancelable(false).create().show();
    // continue;
    // }
    // if ("66200001".equals(localObject1))
    // {
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage(" 服务端系统繁忙，支付失败，请您稍后再试");
    // localObject1 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject1).setCancelable(false).create().show();
    // continue;
    // }
    // if ("66200002".equals(localObject1))
    // {
    // localObject1 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("支付失败，请您稍后再试!");
    // localObject4 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject1).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject4).setCancelable(false).create().show();
    // continue;
    // }
    // int i;
    // if ("66200003".equals(localObject1))
    // {
    // ConfirmPayActivity.this.cancellDialog.show();
    // localObject4 = new Param();
    // i = 100 * (int)ConfirmPayActivity.this.total;
    // ((Param)localObject4).append("attach",
    // ConfirmPayActivity.this.payResponse.id).append("pay_result",
    // "20111110").append("total_fee", i);
    // ProtocolHelper.cancellCaiFutong(ConfirmPayActivity.this,
    // (Param)localObject4, false).startTransForUserGet(new
    // ConfirmPayActivity.CancellPayProtocolResult(ConfirmPayActivity.this,
    // Response.class), (Param)localObject4);
    // continue;
    // }
    // Object localObject2 = null;
    // if ("66200004".equals(i))
    // {
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("内存访问出错,支付失败");
    // localObject2 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject2).setCancelable(false).create().show();
    // continue;
    // }
    // if ("66210013".equals(localObject2))
    // {
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("查询订单信息出错!");
    // localObject2 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject2).setCancelable(false).create().show();
    // continue;
    // }
    // if ("66210020".equals(localObject2))
    // {
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("抱歉,暂不支持此支付类型!");
    // localObject2 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject2).setCancelable(false).create().show();
    // }
    // }
    // if (!"66210035".equals(localObject2))
    // continue;
    // localObject4 = new
    // AlertDialog.Builder(ConfirmPayActivity.this).setTitle("支付结果").setMessage("此订单已支付成功，请勿重复支付");
    // Object localObject2 = new DialogInterface.OnClickListener()
    // {
    // public void onClick(DialogInterface paramDialogInterface, int paramInt)
    // {
    // ConfirmPayActivity.this.closeThisActivity();
    // }
    // };
    // ((AlertDialog.Builder)localObject4).setPositiveButton("确定",
    // (DialogInterface.OnClickListener)localObject2).setCancelable(false).create().show();
    // continue;
    // localObject2 = new TenpayServiceHelper(ConfirmPayActivity.this);
    // localObject4 = new HashMap();
    // ((HashMap)localObject4).put("token_id",
    // ConfirmPayActivity.this.payResponse.token);
    // LogUtils.log("main", "token_id==" +
    // ConfirmPayActivity.this.payResponse.token + "bargainor_id==" +
    // ConfirmPayActivity.this.payResponse.merchant);
    // ((HashMap)localObject4).put("bargainor_id",
    // ConfirmPayActivity.this.payResponse.merchant);
    // ((HashMap)localObject4).put("caller",
    // "com.dld.coupon.activity.ConfirmPayActivity");
    // LogUtils.log("main", "before tenpayHelper.pay");
    // boolean bool = ((TenpayServiceHelper)localObject2).pay((Map)localObject4,
    // ConfirmPayActivity.this.handler, 6);
    // LogUtils.log("main", "b==" + bool);
    // LogUtils.log("main", "after tenpayHelper.pay");
    // }
    // }
    // };
    private int id = 1;
    private String identiferId;
    private String identyCard = null;
    private String mobile = null;
    private String name = null;
    private int oid;
    private OrderResponse orderResponse;
    private final int payException = 2;
    private final int payFailure = 1;
    private PayResponse payResponse = null;
    private final int paySuccess = 0;
    private int payTypeFlag = 0;
    private RelativeLayout postRelativeLayout = null;
    private TextView projectNameTv = null;
    private final int refreshBankInfo = 3;
    private Response response;
    Runnable runnable = new Runnable() {
        public void run() {
            int i = GenericDAO.getInstance(ConfirmPayActivity.this)
                    .getCustomBankS();
            LogUtils.log("main", "count==" + i);
            if (i < 3) {
                GenericDAO
                        .getInstance(ConfirmPayActivity.this)
                        .SaveCustomBankInfo(
                                new CustomerBank(
                                        Integer.valueOf(ConfirmPayActivity.this.bankcardType),
                                        ConfirmPayActivity.this.addressText,
                                        ConfirmPayActivity.this.depositOwner,
                                        ConfirmPayActivity.this.bankcardNum,
                                        ConfirmPayActivity.this.depositBank,
                                        ConfirmPayActivity.this.identiferId,
                                        ConfirmPayActivity.this.totalInfo,
                                        ConfirmPayActivity.this.mobile, Integer
                                                .valueOf(i + 1)));
                ConfirmPayActivity.this.handler.sendEmptyMessage(3);
            } else {
                GenericDAO
                        .getInstance(ConfirmPayActivity.this)
                        .deleteAndUpdateBank(
                                new CustomerBank(
                                        Integer.valueOf(ConfirmPayActivity.this.bankcardType),
                                        ConfirmPayActivity.this.addressText,
                                        ConfirmPayActivity.this.depositOwner,
                                        ConfirmPayActivity.this.bankcardNum,
                                        ConfirmPayActivity.this.depositBank,
                                        ConfirmPayActivity.this.identiferId,
                                        ConfirmPayActivity.this.totalInfo,
                                        ConfirmPayActivity.this.mobile, Integer
                                                .valueOf(i + 1)));
                ConfirmPayActivity.this.customBankList.clear();
                ConfirmPayActivity.this.handler.sendEmptyMessage(3);
            }
        }
    };
    private RadioButton secondRadioButton;
    private TextView sendAddressTv = null;
    private ProgressDialog sendPayDialog;
    private String sendTime = null;
    private int sendTimeFlag;
    private TextView sendTimeTv = null;
    private TextView sumMoneyTv = null;
    private RadioButton thirdRadioButton;
    private TextView topText;
    private float total;
    private String totalInfo;
    private TextView typeTv = null;
    private TextView unitPriceTv = null;
    Thread yinLianthread = new Thread(new Runnable() {
        public void run() {
            ConfirmPayActivity.this.sendyinLianPayRequest();
        }
    });
    private ImageView yingLianImageV = null;
    private RelativeLayout yinlianLayout;
    private ImageView zhifuBaoImageV = null;
    private RelativeLayout zhifuBaoLayout;

    private void closeThisActivity() {
        setResult(2, new Intent(this, OrderInfoActivity.class));
        finish();
    }

    private void initDataforclick(CustomerBank paramCustomerBank) {
        this.bankcardType = paramCustomerBank.type.intValue();
        this.addressText = paramCustomerBank.address;
        this.depositOwner = paramCustomerBank.accountHolder;
        this.identiferId = paramCustomerBank.identityId;
        this.bankcardNum = paramCustomerBank.bankcardNumber;
        this.depositBank = paramCustomerBank.depositBank;
        this.mobile = paramCustomerBank.mobile;
    }

    private void initEditData() {
        switch (this.bank_RadioRg.getCheckedRadioButtonId()) {
        case R.id.firstbank:
            this.customBank = ((CustomerBank) this.customBankList.get(0));
            this.id = 1;
            break;
        case R.id.secondbank:
            this.customBank = ((CustomerBank) this.customBankList.get(1));
            this.id = 2;
            break;
        case R.id.thirdbank:
            this.customBank = ((CustomerBank) this.customBankList.get(2));
            this.id = 3;
        }
    }

    private void initProgressBar() {
        this.cancellDialog = new ProgressDialog(this);
        this.cancellDialog.setMessage("正在发送取消支付请求，请稍后!");
        this.cancellDialog.setCancelable(false);
        this.cancellDialog.setCanceledOnTouchOutside(false);
        this.cancellforFailure = new ProgressDialog(this);
        this.cancellforFailure.setMessage("正在向服务器发送支付失败信息，以便您能够及时查看订单!");
        this.cancellforFailure.setCancelable(true);
        this.cancellforFailure.setCanceledOnTouchOutside(true);
    }

    private void initWidget() {
        initProgressBar();
        this.yinlianLayout = ((RelativeLayout) findViewById(R.id.choicebanklayout));
        this.yinlianLayout.setOnClickListener(this);
        this.caifuLayout = ((RelativeLayout) findViewById(R.id.choicecaifulayout));
        this.caifuLayout.setOnClickListener(this);
        this.deal_cardLayout = ((RelativeLayout) findViewById(R.id.deal_card));
        this.editBankCardTv = ((TextView) findViewById(R.id.edit_newbankcard));
        this.editBankCardTv.setOnClickListener(this);
        this.typeTv = ((TextView) findViewById(R.id.type));
        this.freightchargeLayout = ((RelativeLayout) findViewById(R.id.freightchargelayout));
        this.postRelativeLayout = ((RelativeLayout) findViewById(R.id.postlayout));
        this.firstRadioButton = ((RadioButton) findViewById(R.id.firstbank));
        this.secondRadioButton = ((RadioButton) findViewById(R.id.secondbank));
        this.thirdRadioButton = ((RadioButton) findViewById(R.id.thirdbank));
        this.yingLianImageV = ((ImageView) findViewById(R.id.yinglianbank));
        this.yingLianImageV.setOnClickListener(this);
        this.caifuTongImageV = ((ImageView) findViewById(R.id.caifutong));
        this.caifuTongImageV.setOnClickListener(this);
        this.zhifuBaoImageV = ((ImageView) findViewById(R.id.zhifubao));
        this.zhifuBaoImageV.setOnClickListener(this);
        this.zhifuBaoLayout = ((RelativeLayout) findViewById(R.id.zhifubaolayout));
        this.zhifuBaoLayout.setOnClickListener(this);
        this.bank_RadioRg = ((RadioGroup) findViewById(R.id.bank_radio));
        this.bank_RadioRg
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup paramRadioGroup,
                            int paramInt) {
                        switch (paramInt) {
                        case R.id.firstbank:
                            ConfirmPayActivity.this
                                    .initDataforclick((CustomerBank) ConfirmPayActivity.this.customBankList
                                            .get(0));
                            break;
                        case R.id.secondbank:
                            ConfirmPayActivity.this
                                    .initDataforclick((CustomerBank) ConfirmPayActivity.this.customBankList
                                            .get(1));
                            break;
                        case R.id.thirdbank:
                            ConfirmPayActivity.this
                                    .initDataforclick((CustomerBank) ConfirmPayActivity.this.customBankList
                                            .get(2));
                        }
                    }
                });
        this.topText = ((TextView) findViewById(R.id.topText));
        this.projectNameTv = ((TextView) findViewById(R.id.projectname));
        this.typeTv = ((TextView) findViewById(R.id.type));
        this.buyNmuberTv = ((TextView) findViewById(R.id.buynumber));
        this.unitPriceTv = ((TextView) findViewById(R.id.unitprice));
        this.freightChargeTv = ((TextView) findViewById(R.id.freightcharge));
        this.sumMoneyTv = ((TextView) findViewById(R.id.summoney));
        this.sendAddressTv = ((TextView) findViewById(R.id.sendaddress));
        this.sendAddressTv.setText(this.detailAddress);
        this.sendTimeTv = ((TextView) findViewById(R.id.sendtime));
        this.addNewBankCardTv = ((TextView) findViewById(R.id.addnewbankcard));
        this.addNewBankCardTv.setOnClickListener(this);
        this.confirmPayBt = ((Button) findViewById(R.id.confirmpay));
        this.confirmPayBt.setOnClickListener(this);
        refreshBankInfo();
        switch (this.sendTimeFlag) {
        default:
            this.sendTime = "只工作日送货";
            break;
        case 0:
            this.sendTime = "只双休日假日送货";
            break;
        case 1:
            this.sendTime = "白天没人其他时间送";
            break;
        case 2:
            this.sendTime = "工作日双休日假日均可送货";
            break;
        case 3:
            break;
        }
        // while (true)
        {
            if (this.detail.delivery == 0) {
                this.freightchargeLayout.setVisibility(8);
                this.postRelativeLayout.setVisibility(8);
            }
            this.topText.setText("购买");
            this.sendTimeTv.setText(this.sendTime);
            this.buyNmuberTv.setText(this.buyNumber);
            this.unitPriceTv.setText(this.detail.price + "元");
            this.sumMoneyTv.setText(this.total + "元");
            this.projectNameTv.setText(this.detail.title);
            if (this.freight <= 0.0F)
                this.freightChargeTv.setText("无");
            try {
                // while (true)
                {
                    this.typeTv
                            .setText(this.categoryArray[this.detail.category]);
                    // return;
                    this.freightChargeTv.setText(this.freight + "元");
                }
            } catch (Exception localException) {
                // while (true)
                localException.printStackTrace();
            }
        }
    }

    private void refreshBankInfo() {
        this.customBankList = GenericDAO.getInstance(this).getAllCustomBanks();
        Iterator localIterator = this.customBankList.iterator();
        while (localIterator.hasNext()) {
            CustomerBank localCustomerBank = (CustomerBank) localIterator
                    .next();
            LogUtils.log("main", "id是" + localCustomerBank.id);
        }
        if (this.customBankList != null)
            for (int i = 0; i < this.customBankList.size(); i++) {
                // ((CustomerBank)this.customBankList.get(i));
                LogUtils.log("main", "id====" + this.id);
                switch (i) {
                case 0:
                    setRadioButton(
                            ((CustomerBank) this.customBankList.get(i)).totalInfo,
                            this.firstRadioButton);
                    break;
                case 1:
                    setRadioButton(
                            ((CustomerBank) this.customBankList.get(i)).totalInfo,
                            this.secondRadioButton);
                    break;
                case 2:
                    setRadioButton(
                            ((CustomerBank) this.customBankList.get(i)).totalInfo,
                            this.thirdRadioButton);
                }
            }
    }

    private void sendCaifuTongPay() {
        Param localParam = new Param();
        localParam.append("oid", String.valueOf(this.oid)).append("cid",
                String.valueOf(this.cid));
        LogUtils.log("main", "oid==" + this.oid + "ci==" + this.cid);
        ProtocolHelper
                .caiFuTongpayRequest(this, localParam, false)
                .startTransForUser(new CaiFutongPayProtocolResult(), localParam);
    }

    private void sendyinLianPayRequest() {
        switch (this.bank_RadioRg.getCheckedRadioButtonId()) {
        case R.id.firstbank:
            initDataforclick((CustomerBank) this.customBankList.get(0));
            break;
        case R.id.secondbank:
            initDataforclick((CustomerBank) this.customBankList.get(1));
            break;
        case R.id.thirdbank:
            initDataforclick((CustomerBank) this.customBankList.get(2));
        }
        Param localParam = new Param();
        localParam.append("oid", String.valueOf(this.oid))
                .append("cid", String.valueOf(this.cid))
                .append("acc", this.bankcardNum)
                .append("ctype", String.valueOf(this.bankcardType))
                .append("rname", this.depositOwner)
                .append("mobile", this.mobile).append("crd", this.identiferId)
                .append("crdt", "01").append("bloc", this.addressText);
        ProtocolHelper.payRequest(this, localParam, false).startTransForUser(
                new YinLianPayProtocolResult(), localParam);
    }

    private void setRadioButton(String paramString, RadioButton paramRadioButton) {
        paramRadioButton.setText(paramString);
        paramRadioButton.setVisibility(0);
    }

    private void zhifubaoPayRequest() {
        Param localParam = new Param();
        localParam.append("oid", String.valueOf(this.oid)).append("cid",
                String.valueOf(this.cid));
        ProtocolHelper.zhifubaoPayRequest(this, null, false).startTransForUser(
                new ZhiFuBaoPayProtocolResult(), localParam);
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        LogUtils.log("main", paramInt2 + "/");
        Object localObject;
        if (paramInt2 != 1) {
            if (paramInt2 == 2) {
                LogUtils.log("main", "编辑");
                LogUtils.log("main", this.bankcardNum + "/" + this.bankcardType
                        + "/" + this.depositOwner);
                localObject = paramIntent.getBundleExtra("userdepositinfo");
                this.bankcardType = ((Bundle) localObject)
                        .getInt("bankcardtype");
                this.addressText = ((Bundle) localObject)
                        .getString("addresstext");
                this.depositOwner = ((Bundle) localObject)
                        .getString("depositowner");
                this.identiferId = ((Bundle) localObject)
                        .getString("identiferid");
                this.bankcardNum = ((Bundle) localObject)
                        .getString("bankcardnum");
                this.depositBank = ((Bundle) localObject)
                        .getString("depositbank");
                this.mobile = ((Bundle) localObject).getString("mobile");
                localObject = new StringBuffer();
                ((StringBuffer) localObject).append(this.depositOwner)
                        .append(",").append(this.identiferId).append(",")
                        .append(this.depositBank).append(",")
                        .append(this.bankcardNum);
                this.totalInfo = ((StringBuffer) localObject).toString();
                LogUtils.log("main", "totalInfo==" + this.totalInfo);
                switch (this.id) {
                case 1:
                    setRadioButton(((StringBuffer) localObject).toString(),
                            this.firstRadioButton);
                    break;
                case 2:
                    setRadioButton(((StringBuffer) localObject).toString(),
                            this.secondRadioButton);
                    break;
                case 3:
                    setRadioButton(((StringBuffer) localObject).toString(),
                            this.thirdRadioButton);
                }
                this.handler.post(this.editBankRunnable);
            }
        } else {
            localObject = paramIntent.getBundleExtra("userdepositinfo");
            this.bankcardType = ((Bundle) localObject).getInt("bankcardtype");
            this.addressText = ((Bundle) localObject).getString("addresstext");
            this.depositOwner = ((Bundle) localObject)
                    .getString("depositowner");
            this.identiferId = ((Bundle) localObject).getString("identiferid");
            this.bankcardNum = ((Bundle) localObject).getString("bankcardnum");
            this.depositBank = ((Bundle) localObject).getString("depositbank");
            this.mobile = ((Bundle) localObject).getString("mobile");
            StringBuffer localStringBuffer = new StringBuffer();
            localStringBuffer.append(this.depositOwner).append(",")
                    .append(this.identiferId).append(",")
                    .append(this.depositBank).append(",")
                    .append(this.bankcardNum);
            LogUtils.log("main", "totalInfo==" + this.totalInfo);
            this.totalInfo = localStringBuffer.toString();
            int i = GenericDAO.getInstance(this).getCustomBankS();
            this.customBankList.add(new CustomerBank(Integer
                    .valueOf(this.bankcardType), this.addressText,
                    this.depositOwner, this.bankcardNum, this.depositBank,
                    this.identiferId, this.totalInfo, this.mobile, Integer
                            .valueOf(i + 1)));
            switch (i) {
            default:
                this.customBankList.remove(i - 1);
                setRadioButton("错误", this.thirdRadioButton);
                break;
            case 0:
                setRadioButton(localStringBuffer.toString(),
                        this.firstRadioButton);
                break;
            case 1:
                setRadioButton(localStringBuffer.toString(),
                        this.secondRadioButton);
                break;
            case 2:
                setRadioButton(localStringBuffer.toString(),
                        this.thirdRadioButton);
            }
            this.handler.post(this.runnable);
        }
    }

    public void onClick(View paramView) {
        Object localObject1;
        Object localObject2;
        switch (paramView.getId()) {
        case R.id.choicebanklayout:
            this.yingLianImageV.setImageResource(R.drawable.select_on_normal);
            this.caifuTongImageV.setImageResource(R.drawable.select_off_normal);
            this.zhifuBaoImageV.setImageResource(R.drawable.select_off_normal);
            this.payTypeFlag = 0;
            break;
        case R.id.yinglianbank:
            this.yingLianImageV.setImageResource(R.drawable.select_on_normal);
            this.caifuTongImageV.setImageResource(R.drawable.select_off_normal);
            this.zhifuBaoImageV.setImageResource(R.drawable.select_off_normal);
            this.payTypeFlag = 0;
            break;
        case R.id.addnewbankcard:
            localObject1 = new Intent(this, AddnewBankCardActivity.class);
            localObject2 = new Bundle();
            ((Bundle) localObject2).putInt("actionType", 1);
            ((Intent) localObject1).putExtra("bundle", (Bundle) localObject2);
            startActivityForResult((Intent) localObject1, 3);
            break;
        case R.id.edit_newbankcard:
            if ((this.customBankList != null)
                    && (this.customBankList.size() > 0))
                initEditData();
            if (this.customBank == null)
                break;
            LogUtils.log("main", "banklist!=null");
            localObject2 = new Intent(this, AddnewBankCardActivity.class);
            localObject1 = new Bundle();
            ((Bundle) localObject1).putSerializable("customBank",
                    this.customBank);
            ((Bundle) localObject1).putInt("actionType", 2);
            ((Intent) localObject2).putExtra("bundle", (Bundle) localObject1);
            startActivityForResult((Intent) localObject2, 3);
            break;
        case R.id.choicecaifulayout:
            this.yingLianImageV.setImageResource(R.drawable.select_off_normal);
            this.caifuTongImageV.setImageResource(R.drawable.select_on_normal);
            this.zhifuBaoImageV.setImageResource(R.drawable.select_off_normal);
            this.payTypeFlag = 1;
            break;
        case R.id.caifutong:
            this.yingLianImageV.setImageResource(R.drawable.select_off_normal);
            this.caifuTongImageV.setImageResource(R.drawable.select_on_normal);
            this.zhifuBaoImageV.setImageResource(R.drawable.select_off_normal);
            this.payTypeFlag = 1;
            break;
        case R.id.zhifubaolayout:
            this.payTypeFlag = 2;
            this.yingLianImageV.setImageResource(R.drawable.select_off_normal);
            this.caifuTongImageV.setImageResource(R.drawable.select_off_normal);
            this.zhifuBaoImageV.setImageResource(R.drawable.select_on_normal);
            break;
        case R.id.confirmpay:
            if (this.payTypeFlag != 0) {
                if (this.payTypeFlag != 1) {
                    if (!new MobileSecurePayHelper(this).detectMobile_sp())
                        break;
                    this.sendPayDialog = DialogHelper
                            .getProgressBar("正在发送请求，请稍后");
                    new Thread(new Runnable() {
                        public void run() {
                            ConfirmPayActivity.this.zhifubaoPayRequest();
                        }
                    }).start();
                } else {
                    localObject1 = new TenpayServiceHelper(this);
                    if (((TenpayServiceHelper) localObject1)
                            .isTenpayServiceInstalled()) {
                        this.sendPayDialog = DialogHelper
                                .getProgressBar("正在发送请求，请稍后");
                        new Thread(new Runnable() {
                            public void run() {
                                ConfirmPayActivity.this.sendCaifuTongPay();
                            }
                        }).start();
                    } else {
                        ((TenpayServiceHelper) localObject1)
                                .installTenpayService();
                    }
                }
            } else if (this.customBankList != null) {
                if (this.customBankList.size() != 0) {
                    this.sendPayDialog = DialogHelper
                            .getProgressBar("正在发送请求，请稍后");
                    new Thread(new Runnable() {
                        public void run() {
                            ConfirmPayActivity.this.sendyinLianPayRequest();
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "请您添加银行卡信息！", 1).show();
                }
            } else
                Toast.makeText(this, "请您添加银行卡信息！", 1).show();
        case R.id.bank_radio:
        case R.id.firstbank:
        case R.id.secondbank:
        case R.id.thirdbank:
        case R.id.deal_card:
        case R.id.zhifubao:
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.confirmpay);
        Bundle localBundle = getIntent().getBundleExtra("orderinfo");
        this.cid = localBundle.getString("cid");
        this.oid = localBundle.getInt("oid");
        this.sendTimeFlag = localBundle.getInt("sendTimeFlag");
        this.detailAddress = localBundle.getString("detailaddress");
        this.detail = ((GroupDetail) localBundle.getSerializable("groupdetail"));
        this.freight = localBundle.getFloat("freight");
        this.total = localBundle.getFloat("total");
        this.buyNumber = localBundle.getInt("buyNumber");
        this.categoryArray = getResources().getStringArray(
                R.array.search_keywords);
        LogUtils.log("main", "moblile==" + this.mobile);
        initWidget();
    }

    public void showDialog(Activity paramActivity, String paramString1,
            String paramString2, int paramInt) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(
                paramActivity);
        localBuilder.setIcon(paramInt);
        localBuilder.setTitle(paramString1);
        localBuilder.setMessage(paramString2);
        localBuilder.setPositiveButton("确定", null);
        localBuilder.show();
    }

    public static class AlixOnCancelListener implements
            DialogInterface.OnCancelListener {
        Activity mcontext;

        public AlixOnCancelListener(Activity paramActivity) {
            this.mcontext = paramActivity;
        }

        public void onCancel(DialogInterface paramDialogInterface) {
            this.mcontext.onKeyDown(4, null);
        }
    }

    private class CaiFutongPayProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public CaiFutongPayProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            ConfirmPayActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                ConfirmPayActivity.this.handler.sendEmptyMessage(2);
            } else {
                ConfirmPayActivity.this.payResponse = ((PayResponse) paramObject);
                if (ConfirmPayActivity.this.payResponse.code != 0)
                    ConfirmPayActivity.this.handler.sendEmptyMessage(5);
                else
                    ConfirmPayActivity.this.handler.sendEmptyMessage(7);
            }
        }
    }

    private class CancellPayProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public CancellPayProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.log("main", "cancell exception");
            ConfirmPayActivity.this.handler.sendEmptyMessage(9);
        }

        public void onResult(String paramString, Object paramObject) {
            LogUtils.log("main", "cancell  no exception");
            if (paramObject != null)
                ConfirmPayActivity.this.response = ((Response) paramObject);
            if (ConfirmPayActivity.this.response.code != 0)
                ConfirmPayActivity.this.handler.sendEmptyMessage(9);
            else
                ConfirmPayActivity.this.handler.sendEmptyMessage(8);
        }
    }

    private class PayFailureProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public PayFailureProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            ConfirmPayActivity.this.handler.sendEmptyMessage(13);
        }

        public void onResult(String paramString, Object paramObject) {
            LogUtils.log("main", "cancell  no exception");
            if (paramObject != null)
                ConfirmPayActivity.this.response = ((Response) paramObject);
            if (ConfirmPayActivity.this.response.code != 0)
                ConfirmPayActivity.this.handler.sendEmptyMessage(13);
            else
                ConfirmPayActivity.this.handler.sendEmptyMessage(12);
        }
    }

    private class YinLianPayProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public YinLianPayProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            ConfirmPayActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                ConfirmPayActivity.this.handler.sendEmptyMessage(2);
            } else {
                ConfirmPayActivity.this.orderResponse = ((OrderResponse) paramObject);
                if (ConfirmPayActivity.this.orderResponse.code != 0)
                    ConfirmPayActivity.this.handler.sendEmptyMessage(1);
                else
                    ConfirmPayActivity.this.handler.sendEmptyMessage(0);
            }
        }
    }

    private class ZhiFuBaoPayProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public ZhiFuBaoPayProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            ConfirmPayActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                ConfirmPayActivity.this.payResponse = ((PayResponse) paramObject);
                if (ConfirmPayActivity.this.payResponse.code != 0)
                    ConfirmPayActivity.this.handler.sendEmptyMessage(5);
                else
                    ConfirmPayActivity.this.handler.sendEmptyMessage(10);
                LogUtils.log("main",
                        ConfirmPayActivity.this.payResponse.orderInfo + "/");
            }
        }
    }
}
