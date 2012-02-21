package com.dld.coupon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.db.MailingAddress;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Order;
import com.dld.protocol.json.OrderResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.User;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import java.util.ArrayList;

public class OrderInfoActivity extends BaseActivity {
    private TextView addAdressTv;
    private String additionComments;
    private RadioGroup addressGroup;
    private EditText addtionalIllusEt;
    private EditText buyNumEditText;
    private int buyNumber = 1;
    private String cid;
    private String city;
    private String customName;
    private EditText deliverlyIllusEt;
    private RadioGroup deliverlyTimeRG;
    private GroupDetail detail;
    private String detailAddress;
    Runnable editAddressRunnable = new Runnable() {
        public void run() {
            GenericDAO.getInstance(OrderInfoActivity.this)
                    .deleteAndInsertAddress(
                            new MailingAddress(OrderInfoActivity.this.province,
                                    OrderInfoActivity.this.city,
                                    OrderInfoActivity.this.partAddress,
                                    OrderInfoActivity.this.detailAddress,
                                    OrderInfoActivity.this.postCode,
                                    OrderInfoActivity.this.customName,
                                    OrderInfoActivity.this.mobile,
                                    OrderInfoActivity.this.id));
            OrderInfoActivity.this.handler.sendEmptyMessage(3);
        }
    };
    private TextView edit_AddressTv;
    private TextView extraETextView;
    private RadioButton firstRadioButton;
    private TextView freightChargeTv;
    private RelativeLayout freightchargeLayout = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            OrderInfoActivity.this.orderRequestProDialog.dismiss();
            switch (paramMessage.what) {
            case 0:
                OrderInfoActivity.this.skipToPay();
                break;
            case 1:
                Toast.makeText(OrderInfoActivity.this,
                        OrderInfoActivity.this.orderResponse.message, 1).show();
                break;
            case 2:
                Toast.makeText(OrderInfoActivity.this, "由于网络原因发送订单失败，请您稍后再试!",
                        1).show();
                break;
            case 3:
                OrderInfoActivity.this.initAddressRadioGroup();
            }
        }
    };
    private int id = 1;
    private String illustration;
    private ArrayList<MailingAddress> mailAddressList;
    private MailingAddress mailingAddress = null;
    private String mobile;
    private EditText mobileEdiText;
    private RelativeLayout needPostRelativeLayout = null;
    private Button nextButton;
    private final int orderRequestException = 2;
    private ProgressDialog orderRequestProDialog;
    private final int orderRequestSuccess = 0;
    private final int orderRequestfailure = 1;
    private OrderResponse orderResponse = null;
    private String partAddress;
    private RelativeLayout phoneLayout = null;
    private String postCode;
    private RadioButton priRadButton;
    private TextView productNameTv;
    private String projectName;
    private String province;
    private final int refreshRadioGroup = 3;
    Runnable runnable = new Runnable() {
        public void run() {
            int i = GenericDAO.getInstance(OrderInfoActivity.this)
                    .getMailAddCount();
            if (i < 3) {
                GenericDAO.getInstance(OrderInfoActivity.this).saveMailAddress(
                        new MailingAddress(OrderInfoActivity.this.province,
                                OrderInfoActivity.this.city,
                                OrderInfoActivity.this.partAddress,
                                OrderInfoActivity.this.detailAddress,
                                OrderInfoActivity.this.postCode,
                                OrderInfoActivity.this.customName,
                                OrderInfoActivity.this.mobile, i + 1));
            } else {
                GenericDAO.getInstance(OrderInfoActivity.this).deleteAndUpdate(
                        new MailingAddress(OrderInfoActivity.this.province,
                                OrderInfoActivity.this.city,
                                OrderInfoActivity.this.partAddress,
                                OrderInfoActivity.this.detailAddress,
                                OrderInfoActivity.this.postCode,
                                OrderInfoActivity.this.customName,
                                OrderInfoActivity.this.mobile, i + 1));
                OrderInfoActivity.this.handler.sendEmptyMessage(3);
            }
        }
    };
    private RadioButton secondRadioButton;
    private int sendTimeFlag;
    private RelativeLayout shuomingRelativeLayout = null;
    private TextView shuomingTextView;
    private RadioButton thirdRadioButton;
    private RadioGroup timeRadioGroup;
    private TextView topText;
    private String unitPrice;
    private TextView unitPriceTv;

    private void initAddressRadioGroup() {
        this.mailAddressList = GenericDAO.getInstance(this)
                .getMailingAddressList();
        LogUtils.log("main", "size=" + this.mailAddressList.size());
        if (this.mailAddressList != null)
            for (int i = 0; i < this.mailAddressList.size(); i++) {
                MailingAddress localMailingAddress = (MailingAddress) this.mailAddressList
                        .get(i);
                LogUtils.log("main", "addrdss id==" + localMailingAddress.id);
                switch (i) {
                case 0:
                    setRadioButton(
                            ((MailingAddress) this.mailAddressList.get(i)).detailAddress,
                            this.firstRadioButton);
                    break;
                case 1:
                    setRadioButton(
                            ((MailingAddress) this.mailAddressList.get(i)).detailAddress,
                            this.secondRadioButton);
                    break;
                case 2:
                    setRadioButton(
                            ((MailingAddress) this.mailAddressList.get(i)).detailAddress,
                            this.thirdRadioButton);
                }
            }
    }

    private void initDataforclick(MailingAddress paramMailingAddress) {
        this.province = paramMailingAddress.province;
        this.city = paramMailingAddress.city;
        this.partAddress = paramMailingAddress.partAddress;
        this.detailAddress = paramMailingAddress.detailAddress;
        this.customName = paramMailingAddress.receiverName;
        this.postCode = paramMailingAddress.zipCode;
        this.mobile = paramMailingAddress.receiverMobile;
        LogUtils.log("main", "address id" + paramMailingAddress.id);
    }

    private void initEditData() {
        switch (this.addressGroup.getCheckedRadioButtonId()) {
        case R.id.first:
            this.mailingAddress = ((MailingAddress) this.mailAddressList.get(0));
            this.id = 1;
            break;
        case R.id.second:
            this.mailingAddress = ((MailingAddress) this.mailAddressList.get(1));
            this.id = 2;
            break;
        case R.id.third:
            this.mailingAddress = ((MailingAddress) this.mailAddressList.get(2));
            this.id = 3;
        }
    }

    private void initNoPostWidget() {
        this.shuomingTextView = ((TextView) findViewById(R.id.shuoming));
        this.shuomingRelativeLayout = ((RelativeLayout) findViewById(R.id.shuominglayout));
        this.buyNumEditText = ((EditText) findViewById(R.id.buynumber));
        this.needPostRelativeLayout = ((RelativeLayout) findViewById(R.id.needpost));
        this.needPostRelativeLayout.setVisibility(8);
        this.freightchargeLayout = ((RelativeLayout) findViewById(R.id.freightchargelayout));
        this.freightchargeLayout.setVisibility(8);
        this.phoneLayout = ((RelativeLayout) findViewById(R.id.mobilelayout));
        this.phoneLayout.setVisibility(0);
        this.topText = ((TextView) findViewById(R.id.topText));
        this.productNameTv = ((TextView) findViewById(R.id.projectname));
        this.unitPriceTv = ((TextView) findViewById(R.id.unitprice));
        this.mobileEdiText = ((EditText) findViewById(R.id.phonenumber));
        initProgressBar();
        this.nextButton = ((Button) findViewById(R.id.next));
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                OrderInfoActivity.this.initNotPostSendData();
                Object localObject = OrderInfoActivity.this.buyNumEditText
                        .getText().toString();
                if (!StringUtils.isEmpty((String) localObject)) {
                    OrderInfoActivity.this.buyNumber = Integer
                            .parseInt((String) localObject);
                    if (!StringUtils.isEmpty(OrderInfoActivity.this.mobile)) {
                        if (StringUtils
                                .checkPhone(OrderInfoActivity.this.mobile)) {
                            if (OrderInfoActivity.this.detail.maxPerOrder >= OrderInfoActivity.this.detail.minPerOrder) {
                                if (OrderInfoActivity.this.detail.maxPerOrder != OrderInfoActivity.this.detail.minPerOrder) {
                                    if (OrderInfoActivity.this.detail.maxPerOrder > OrderInfoActivity.this.detail.minPerOrder) {
                                        LogUtils.log("main",
                                                "detail.max>detail.min");
                                        if (OrderInfoActivity.this.buyNumber >= OrderInfoActivity.this.detail.minPerOrder) {
                                            if (OrderInfoActivity.this.buyNumber > OrderInfoActivity.this.detail.maxPerOrder) {
                                                LogUtils.log(
                                                        "main",
                                                        "detail.max>detail.min &&buyNumber<detail.minPre buyNumber > detail.maxPerOrder");
                                                localObject = new StringBuffer();
                                                ((StringBuffer) localObject)
                                                        .append("一次最高购买量为")
                                                        .append(OrderInfoActivity.this.detail.maxPerOrder)
                                                        .append("件");
                                                Toast.makeText(
                                                        OrderInfoActivity.this,
                                                        ((StringBuffer) localObject)
                                                                .toString(), 1)
                                                        .show();
                                                return;
                                            }
                                        } else {
                                            LogUtils.log("main",
                                                    "detail.max>detail.min &&buyNumber<detail.minPre");
                                            localObject = new StringBuffer();
                                            ((StringBuffer) localObject)
                                                    .append("最低购买量为")
                                                    .append(OrderInfoActivity.this.detail.minPerOrder)
                                                    .append("件");
                                            Toast.makeText(
                                                    OrderInfoActivity.this,
                                                    ((StringBuffer) localObject)
                                                            .toString(), 1)
                                                    .show();
                                            return;
                                        }
                                    }
                                } else {
                                    LogUtils.log("main",
                                            "detail.max=detail.min");
                                    if (OrderInfoActivity.this.buyNumber != OrderInfoActivity.this.detail.maxPerOrder) {
                                        localObject = new StringBuffer();
                                        ((StringBuffer) localObject)
                                                .append("一次只能购买")
                                                .append(OrderInfoActivity.this.detail.minPerOrder)
                                                .append("件");
                                        Toast.makeText(
                                                OrderInfoActivity.this,
                                                ((StringBuffer) localObject)
                                                        .toString(), 1).show();
                                        return;
                                    }
                                }
                            } else {
                                LogUtils.log("main", "detail.max<detail.main");
                                if (OrderInfoActivity.this.buyNumber < OrderInfoActivity.this.detail.minPerOrder)
                                    ;// break label467;
                            }
                            if ((OrderInfoActivity.this.orderResponse == null)
                                    || (OrderInfoActivity.this.orderResponse.order == null)) {
                                OrderInfoActivity.this.orderRequestProDialog
                                        .show();
                                OrderInfoActivity.this.notPostOrderRequest();
                            } else {
                                OrderInfoActivity.this.orderRequestProDialog
                                        .show();
                                OrderInfoActivity.this
                                        .notPostOrderRequestfroUpdate();
                                // return;
                                label467: localObject = new StringBuffer();
                                ((StringBuffer) localObject)
                                        .append("最低购买量为")
                                        .append(OrderInfoActivity.this.detail.minPerOrder)
                                        .append("件");
                                Toast.makeText(
                                        OrderInfoActivity.this,
                                        ((StringBuffer) localObject).toString(),
                                        1).show();
                            }
                        } else {
                            Toast.makeText(OrderInfoActivity.this,
                                    "您填写的手机号码格式不对", 1).show();
                        }
                    } else
                        Toast.makeText(OrderInfoActivity.this, "您的手机号码还没有填写", 1)
                                .show();
                } else {
                    Toast.makeText(OrderInfoActivity.this, "您还没有选择购买数量", 1)
                            .show();
                }
            }
        });
    }

    private void initNotPostSendData() {
        this.projectName = this.productNameTv.getText().toString();
        this.unitPrice = this.unitPriceTv.getText().toString();
        this.mobile = this.mobileEdiText.getText().toString();
    }

    private void initProgressBar() {
        this.orderRequestProDialog = new ProgressDialog(this);
        this.orderRequestProDialog.setMessage("正在提交订单，请耐心等待!");
        this.orderRequestProDialog.setCancelable(false);
        this.orderRequestProDialog.setCanceledOnTouchOutside(false);
    }

    private void initSendData() {
        switch (this.addressGroup.getCheckedRadioButtonId()) {
        case R.id.first:
            initDataforclick((MailingAddress) this.mailAddressList.get(0));
            break;
        case R.id.second:
            initDataforclick((MailingAddress) this.mailAddressList.get(1));
            break;
        case R.id.third:
            initDataforclick((MailingAddress) this.mailAddressList.get(2));
        }
        this.projectName = this.productNameTv.getText().toString();
        this.unitPrice = this.unitPriceTv.getText().toString();
        switch (this.deliverlyTimeRG.getCheckedRadioButtonId()) {
        default:
            this.sendTimeFlag = 0;
            break;
        case R.id.only_workday:
            this.sendTimeFlag = 0;
            break;
        case R.id.weekendorholiday:
            this.sendTimeFlag = 1;
            break;
        case R.id.other:
            this.sendTimeFlag = 2;
            break;
        case R.id.randomday:
            this.sendTimeFlag = 3;
        }
        this.illustration = this.deliverlyIllusEt.getText().toString();
        this.additionComments = this.addtionalIllusEt.getText().toString();
        StringBuffer localStringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(this.province))
            localStringBuffer.append(this.province);
        localStringBuffer.append(this.city);
        localStringBuffer.append(this.partAddress);
        this.detailAddress = localStringBuffer.toString();
    }

    private void initUIData() {
        this.topText.setText("订单");
        this.unitPriceTv.setText(this.detail.price + "元");
        this.productNameTv.setText(this.detail.title);
        StringBuffer localStringBuffer;
        if (this.detail.delivery != 0) {
            localStringBuffer = new StringBuffer();
            if (this.detail.freightFree <= 0) {
                if (this.detail.freightFree != -1) {
                    if (this.detail.freight <= 0.0F)
                        this.freightChargeTv.setText("无");
                    else
                        this.freightChargeTv.setText(this.detail.freight + "元");
                } else
                    this.freightChargeTv.setText("无");
            } else {
                localStringBuffer.append(this.detail.freight).append("(")
                        .append(this.detail.freightFree).append("件包邮")
                        .append(")");
                this.freightChargeTv.setText(localStringBuffer.toString());
            }
        }
        if (this.detail.maxPerOrder != 0) {
            if (this.detail.minPerOrder != this.detail.maxPerOrder) {
                this.shuomingRelativeLayout.setVisibility(0);
                localStringBuffer = new StringBuffer();
                localStringBuffer.append("最低购买量为")
                        .append(this.detail.minPerOrder).append("件")
                        .append(",").append("最高可购买")
                        .append(this.detail.maxPerOrder).append("件");
                this.shuomingTextView.setText(localStringBuffer.toString());
            } else {
                this.shuomingRelativeLayout.setVisibility(0);
                localStringBuffer = new StringBuffer();
                localStringBuffer.append("一次只能购买")
                        .append(this.detail.minPerOrder).append("件");
                this.shuomingTextView.setText(localStringBuffer.toString());
            }
        } else if (this.detail.minPerOrder >= 1) {
            this.shuomingRelativeLayout.setVisibility(0);
            localStringBuffer = new StringBuffer();
            localStringBuffer.append("最低购买量为").append(this.detail.minPerOrder)
                    .append("件");
            this.shuomingTextView.setText(localStringBuffer.toString());
        }
        this.cid = SharePersistent.getInstance().getUserInfo(this).id;
        LogUtils.log("main", "cid==" + this.cid);
        this.buyNumEditText.setText(this.detail.minPerOrder);
    }

    private void initWidget() {
        initProgressBar();
        this.shuomingTextView = ((TextView) findViewById(R.id.shuoming));
        this.shuomingRelativeLayout = ((RelativeLayout) findViewById(R.id.shuominglayout));
        this.buyNumEditText = ((EditText) findViewById(R.id.buynumber));
        this.firstRadioButton = ((RadioButton) findViewById(R.id.first));
        this.secondRadioButton = ((RadioButton) findViewById(R.id.second));
        this.thirdRadioButton = ((RadioButton) findViewById(R.id.third));
        this.topText = ((TextView) findViewById(R.id.topText));
        this.productNameTv = ((TextView) findViewById(R.id.projectname));
        this.unitPriceTv = ((TextView) findViewById(R.id.unitprice));
        this.freightChargeTv = ((TextView) findViewById(R.id.freightcharge));
        this.addAdressTv = ((TextView) findViewById(R.id.addadress));
        this.priRadButton = ((RadioButton) findViewById(R.id.privateinfo));
        this.deliverlyTimeRG = ((RadioGroup) findViewById(R.id.deliverytime_radio));
        this.deliverlyIllusEt = ((EditText) findViewById(R.id.deliveryillus));
        this.addtionalIllusEt = ((EditText) findViewById(R.id.additionalillus));
        this.nextButton = ((Button) findViewById(R.id.next));
        this.timeRadioGroup = ((RadioGroup) findViewById(R.id.deliverytime_radio));
        this.addressGroup = ((RadioGroup) findViewById(R.id.addressgroup));
        this.edit_AddressTv = ((TextView) findViewById(R.id.edit_addadress));
        this.edit_AddressTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                if ((OrderInfoActivity.this.mailAddressList != null)
                        && (OrderInfoActivity.this.mailAddressList.size() > 0))
                    OrderInfoActivity.this.initEditData();
                if (OrderInfoActivity.this.mailingAddress != null) {
                    Intent localIntent = new Intent(OrderInfoActivity.this,
                            AddNewAddressActivity.class);
                    Bundle localBundle = new Bundle();
                    localBundle.putSerializable("mailingAddress",
                            OrderInfoActivity.this.mailingAddress);
                    localIntent.putExtra("bundle", localBundle);
                    localIntent.putExtra("actiontType", 2);
                    OrderInfoActivity.this.startActivityForResult(localIntent,
                            3);
                }
            }
        });
        this.addAdressTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent = new Intent(OrderInfoActivity.this,
                        AddNewAddressActivity.class);
                Bundle localBundle = new Bundle();
                localBundle.putInt("actionType", 1);
                localIntent.putExtra("bundle", localBundle);
                OrderInfoActivity.this.startActivityForResult(localIntent, 3);
            }
        });
        this.addressGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup paramRadioGroup,
                            int paramInt) {
                        LogUtils.log("main", paramInt + "/");
                        switch (paramInt) {
                        case R.id.first:
                            OrderInfoActivity.this
                                    .initDataforclick((MailingAddress) OrderInfoActivity.this.mailAddressList
                                            .get(0));
                            break;
                        case R.id.second:
                            OrderInfoActivity.this
                                    .initDataforclick((MailingAddress) OrderInfoActivity.this.mailAddressList
                                            .get(1));
                            break;
                        case R.id.third:
                            OrderInfoActivity.this
                                    .initDataforclick((MailingAddress) OrderInfoActivity.this.mailAddressList
                                            .get(2));
                        }
                    }
                });
        initAddressRadioGroup();
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                OrderInfoActivity.this.mailAddressList = GenericDAO
                        .getInstance(OrderInfoActivity.this)
                        .getMailingAddressList();
                if (OrderInfoActivity.this.mailAddressList != null) {
                    if (OrderInfoActivity.this.mailAddressList.size() > 0) {
                        Object localObject = OrderInfoActivity.this.buyNumEditText
                                .getText().toString();
                        if (!StringUtils.isEmpty((String) localObject)) {
                            OrderInfoActivity.this.buyNumber = Integer
                                    .parseInt((String) localObject);
                            if (OrderInfoActivity.this.detail.maxPerOrder >= OrderInfoActivity.this.detail.minPerOrder) {
                                if (OrderInfoActivity.this.detail.maxPerOrder != OrderInfoActivity.this.detail.minPerOrder) {
                                    if (OrderInfoActivity.this.detail.maxPerOrder > OrderInfoActivity.this.detail.minPerOrder)
                                        if (OrderInfoActivity.this.buyNumber >= OrderInfoActivity.this.detail.minPerOrder) {
                                            if (OrderInfoActivity.this.buyNumber > OrderInfoActivity.this.detail.maxPerOrder) {
                                                localObject = new StringBuffer();
                                                ((StringBuffer) localObject)
                                                        .append("一次最高购买量为")
                                                        .append(OrderInfoActivity.this.detail.maxPerOrder)
                                                        .append("件");
                                                Toast.makeText(
                                                        OrderInfoActivity.this,
                                                        ((StringBuffer) localObject)
                                                                .toString(), 1)
                                                        .show();
                                                return;
                                            }
                                        } else {
                                            localObject = new StringBuffer();
                                            ((StringBuffer) localObject)
                                                    .append("最低购买量为")
                                                    .append(OrderInfoActivity.this.detail.minPerOrder)
                                                    .append("件");
                                            Toast.makeText(
                                                    OrderInfoActivity.this,
                                                    ((StringBuffer) localObject)
                                                            .toString(), 1)
                                                    .show();
                                            return;
                                        }
                                } else if (OrderInfoActivity.this.buyNumber != OrderInfoActivity.this.detail.maxPerOrder) {
                                    localObject = new StringBuffer();
                                    ((StringBuffer) localObject)
                                            .append("一次只能购买")
                                            .append(OrderInfoActivity.this.detail.minPerOrder)
                                            .append("件");
                                    Toast.makeText(
                                            OrderInfoActivity.this,
                                            ((StringBuffer) localObject)
                                                    .toString(), 1).show();
                                    return;
                                }
                            } else if (OrderInfoActivity.this.buyNumber < OrderInfoActivity.this.detail.minPerOrder)
                                ; // break label446;
                            OrderInfoActivity.this.initSendData();
                            if ((OrderInfoActivity.this.orderResponse == null)
                                    || (OrderInfoActivity.this.orderResponse.order == null)) {
                                OrderInfoActivity.this.orderRequestProDialog
                                        .show();
                                OrderInfoActivity.this.orderRequest();
                            } else {
                                OrderInfoActivity.this.orderRequestProDialog
                                        .show();
                                OrderInfoActivity.this.orderRequestForUpdate();
                                // return;
                                label446: localObject = new StringBuffer();
                                ((StringBuffer) localObject)
                                        .append("最低购买量为")
                                        .append(OrderInfoActivity.this.detail.minPerOrder)
                                        .append("件");
                                Toast.makeText(
                                        OrderInfoActivity.this,
                                        ((StringBuffer) localObject).toString(),
                                        1).show();
                            }
                        } else {
                            Toast.makeText(OrderInfoActivity.this,
                                    "您还没有选择购买数量", 1).show();
                        }
                    } else {
                        Toast.makeText(OrderInfoActivity.this, "请您添加地址信息", 1)
                                .show();
                    }
                } else
                    Toast.makeText(OrderInfoActivity.this, "请您添加地址信息", 1)
                            .show();
            }
        });
    }

    private void notPostOrderRequest() {
        Param localParam = new Param();
        localParam.append("cid", this.cid)
                .append("pid", String.valueOf(this.detail.id))
                .append("num", String.valueOf(this.buyNumber))
                .append("mobile", String.valueOf(this.mobile));
        ProtocolHelper.orderRequest(this, localParam, false).startTransForUser(
                new OrderProtocolResult(), localParam);
    }

    private void notPostOrderRequestfroUpdate() {
        Param localParam = new Param();
        localParam.append("cid", this.cid)
                .append("pid", String.valueOf(this.detail.id))
                .append("oid", String.valueOf(this.orderResponse.order.id))
                .append("num", String.valueOf(this.buyNumber))
                .append("mobile", String.valueOf(this.mobile));
        ProtocolHelper.orderRequestUpdate(this, localParam, false)
                .startTransForUser(new OrderProtocolResult(), localParam);
    }

    private void orderRequest() {
        Param localParam = new Param();
        if (!StringUtils.isEmpty(this.province))
            localParam.append("prvn", this.province);
        else
            localParam.append("prvn", this.city);
        localParam.append("cid", this.cid)
                .append("pid", String.valueOf(this.detail.id))
                .append("num", String.valueOf(this.buyNumber))
                .append("mobile", this.mobile).append("ct", this.city)
                .append("recp", this.customName)
                .append("addr", this.detailAddress)
                .append("zip", this.postCode)
                .append("dt", String.valueOf(this.sendTimeFlag))
                .append("dn", this.illustration)
                .append("rm", this.additionComments);
        LogUtils.log("main", localParam.toString());
        ProtocolHelper.orderRequest(this, localParam, false).startTransForUser(
                new OrderProtocolResult(), localParam);
    }

    private void orderRequestForUpdate() {
        Param localParam = new Param();
        if (!StringUtils.isEmpty(this.province))
            localParam.append("prvn", this.province);
        else
            localParam.append("prvn", this.city);
        localParam.append("cid", this.cid)
                .append("pid", String.valueOf(this.detail.id))
                .append("oid", String.valueOf(this.orderResponse.order.id))
                .append("num", String.valueOf(this.buyNumber))
                .append("mobile", this.mobile).append("ct", this.city)
                .append("recp", this.customName)
                .append("addr", this.detailAddress)
                .append("zip", this.postCode)
                .append("dt", String.valueOf(this.sendTimeFlag))
                .append("dn", this.illustration)
                .append("rm", this.additionComments);
        LogUtils.log("main", localParam.toString());
        ProtocolHelper.orderRequestUpdate(this, localParam, false)
                .startTransForUser(new OrderProtocolResult(), localParam);
    }

    private void setRadioButton(String paramString, RadioButton paramRadioButton) {
        paramRadioButton.setText(paramString);
        LogUtils.log("main", "String=" + paramString);
        paramRadioButton.setVisibility(0);
    }

    private void skipToPay() {
        Intent localIntent = new Intent(this, ConfirmPayActivity.class);
        Bundle localBundle = new Bundle();
        localBundle.putInt("oid", this.orderResponse.order.id.intValue());
        localBundle.putString("cid", this.cid);
        localBundle.putString("detailaddress", this.detailAddress);
        localBundle.putInt("sendTimeFlag", this.sendTimeFlag);
        localBundle.putInt("buyNumber", this.buyNumber);
        localBundle.putFloat("freight", this.orderResponse.order.freight);
        localBundle.putFloat("total", this.orderResponse.order.total);
        localBundle.putSerializable("groupdetail", this.detail);
        localIntent.putExtra("orderinfo", localBundle);
        startActivityForResult(localIntent, 0);
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        Object localObject;
        if (paramInt2 != 1) {
            if (paramInt2 != 2) {
                if (paramInt2 == 3) {
                    localObject = paramIntent.getBundleExtra("userinfo");
                    this.province = ((Bundle) localObject)
                            .getString("province");
                    this.city = ((Bundle) localObject).getString("city");
                    this.partAddress = ((Bundle) localObject)
                            .getString("partaddress");
                    this.customName = ((Bundle) localObject)
                            .getString("customname");
                    this.postCode = ((Bundle) localObject)
                            .getString("postcode");
                    this.mobile = ((Bundle) localObject)
                            .getString("customphone");
                    LogUtils.log("OrderInfoActivity", "province="
                            + this.province);
                    LogUtils.log("OrderInfoActivity", "city=" + this.city);
                    localObject = new StringBuffer();
                    ((StringBuffer) localObject).append("保密").append(",");
                    if (this.province != null)
                        ((StringBuffer) localObject).append(this.province);
                    ((StringBuffer) localObject).append(this.city);
                    ((StringBuffer) localObject).append(this.partAddress)
                            .append(",").append(this.postCode).append(",")
                            .append(this.mobile);
                    this.detailAddress = ((StringBuffer) localObject)
                            .toString();
                    switch (this.id) {
                    case 1:
                        setRadioButton(this.detailAddress,
                                this.firstRadioButton);
                        break;
                    case 2:
                        setRadioButton(this.detailAddress,
                                this.secondRadioButton);
                        break;
                    case 3:
                        setRadioButton(this.detailAddress,
                                this.thirdRadioButton);
                    }
                    this.handler.post(this.editAddressRunnable);
                }
            } else
                finish();
        } else {
            localObject = paramIntent.getBundleExtra("userinfo");
            this.province = ((Bundle) localObject).getString("province");
            this.city = ((Bundle) localObject).getString("city");
            this.partAddress = ((Bundle) localObject).getString("partaddress");
            this.customName = ((Bundle) localObject).getString("customname");
            this.postCode = ((Bundle) localObject).getString("postcode");
            this.mobile = ((Bundle) localObject).getString("customphone");
            LogUtils.log("OrderInfoActivity", "province=" + this.province);
            LogUtils.log("OrderInfoActivity", "city=" + this.city);
            localObject = new StringBuffer();
            ((StringBuffer) localObject).append("保密").append(",");
            if (this.province != null)
                ((StringBuffer) localObject).append(this.province);
            ((StringBuffer) localObject).append(this.city);
            ((StringBuffer) localObject).append(this.partAddress).append(",")
                    .append(this.postCode).append(",").append(this.mobile);
            this.detailAddress = ((StringBuffer) localObject).toString();
            int i = GenericDAO.getInstance(this).getMailAddCount();
            this.mailAddressList.add(new MailingAddress(this.province,
                    this.city, this.partAddress, this.detailAddress,
                    this.postCode, this.customName, this.mobile, i + 1));
            switch (i) {
            default:
                this.mailAddressList.remove(i - 1);
                setRadioButton(this.detailAddress, this.thirdRadioButton);
                break;
            case 0:
                setRadioButton(this.detailAddress, this.firstRadioButton);
                break;
            case 1:
                setRadioButton(this.detailAddress, this.secondRadioButton);
                break;
            case 2:
                setRadioButton(this.detailAddress, this.thirdRadioButton);
            }
            this.handler.post(this.runnable);
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.orderinfo);
        this.detail = ((GroupDetail) getIntent().getSerializableExtra(
                "groupdetail"));
        LogUtils.log("main", this.detail);
        if (this.detail != null) {
            if (this.detail.delivery == 0)
                initNoPostWidget();
            else
                initWidget();
            initUIData();
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    private class OrderProtocolResult extends Protocol.OnJsonProtocolResult {
        public OrderProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            OrderInfoActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject == null) {
                OrderInfoActivity.this.handler.sendEmptyMessage(2);
            } else {
                OrderInfoActivity.this.orderResponse = ((OrderResponse) paramObject);
                if (OrderInfoActivity.this.orderResponse.code != 0)
                    OrderInfoActivity.this.handler.sendEmptyMessage(1);
                else
                    OrderInfoActivity.this.handler.sendEmptyMessage(0);
            }
        }
    }
}
