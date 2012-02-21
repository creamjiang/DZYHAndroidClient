package com.dld.coupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.util.LogUtils;
import com.dld.coupon.db.CustomerBank;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;

public class AddnewBankCardActivity extends CityActivity implements
        View.OnClickListener {
    private int action_type;
    private ArrayAdapter<String> adapter;
    protected TextView addressButton;
    private String addressText;
    private String bankcardNum;
    private EditText bankcardNumEt = null;
    private int bankcardType = 1;
    private RadioGroup cardTypeRadioGroup = null;
    private String city;
    private ArrayAdapter<String> creditAdapter;
    private Spinner creditBankSpiner = null;
    private String[] creditCardArray;
    private CustomerBank customerBank;
    private String[] debitCardArray;
    private Spinner deposiBankSpiner = null;
    private String depositBank = null;
    private String depositOwner;
    private EditText depositOwnerEt = null;
    Handler handler = new Handler();
    private String identiferId;
    private EditText identifierIdEt = null;
    private String mobile;
    private EditText mobileEt = null;
    private String province;
    private Button saveButton = null;
    private TextView tipTextView;
    private TextView topTextView;

    public AddnewBankCardActivity() {
        String[] arrayOfString = new String[58];
        arrayOfString[0] = "农业银行";
        arrayOfString[1] = "工商银行";
        arrayOfString[2] = "中国银行";
        arrayOfString[3] = "建设银行";
        arrayOfString[4] = "邮政储蓄银行";
        arrayOfString[5] = "交通银行";
        arrayOfString[6] = "光大银行";
        arrayOfString[7] = "平安银行";
        arrayOfString[8] = "中信银行";
        arrayOfString[9] = "民生银行";
        arrayOfString[10] = "兴业银行";
        arrayOfString[11] = "华夏银行";
        arrayOfString[12] = "浦东发展银行";
        arrayOfString[13] = "广发银行";
        arrayOfString[14] = "招商银行";
        arrayOfString[15] = "上海银行";
        arrayOfString[16] = "北京银行";
        arrayOfString[17] = "徽商银行";
        arrayOfString[18] = "大连银行";
        arrayOfString[19] = "福建农信社";
        arrayOfString[20] = "兰州银行";
        arrayOfString[21] = "广州银行";
        arrayOfString[22] = "顺德农商行";
        arrayOfString[23] = "广州农村商业银行";
        arrayOfString[24] = "贵阳银行";
        arrayOfString[25] = "河北银行";
        arrayOfString[26] = "承德银行";
        arrayOfString[27] = "哈尔滨银行";
        arrayOfString[28] = "宜昌银行";
        arrayOfString[29] = "长沙银行";
        arrayOfString[30] = "湖南农信社";
        arrayOfString[31] = "常熟农商行";
        arrayOfString[32] = "江阴农商行";
        arrayOfString[33] = "江苏银行";
        arrayOfString[34] = "江苏锡州银行";
        arrayOfString[35] = "上饶银行";
        arrayOfString[36] = "九江银行";
        arrayOfString[37] = "乌鲁木齐商业银行";
        arrayOfString[38] = "包商银行";
        arrayOfString[39] = "青海银行";
        arrayOfString[40] = "潍坊银行";
        arrayOfString[41] = "东营银行";
        arrayOfString[42] = "威海银行";
        arrayOfString[43] = "上海农商行";
        arrayOfString[44] = "成都市农信社";
        arrayOfString[45] = "台州银行";
        arrayOfString[46] = "宁波银行";
        arrayOfString[47] = "鄞州银行";
        arrayOfString[48] = "浙江泰隆银行";
        arrayOfString[49] = "浙江民泰银行";
        arrayOfString[50] = "杭州银行";
        arrayOfString[51] = "浙江稠州银行";
        arrayOfString[52] = "重庆银行";
        arrayOfString[53] = "重庆农信社(农商)";
        arrayOfString[54] = "龙江银行";
        arrayOfString[55] = "东亚银行";
        arrayOfString[56] = "锦州银行";
        arrayOfString[57] = "鄂尔多斯银行";
        this.creditCardArray = arrayOfString;
        arrayOfString = new String[7];
        arrayOfString[0] = "农业银行";
        arrayOfString[1] = "建设银行";
        arrayOfString[2] = "招商银行";
        arrayOfString[3] = "交通银行";
        arrayOfString[4] = "中信银行";
        arrayOfString[5] = "深发展银行";
        arrayOfString[6] = "兴业银行";
        this.debitCardArray = arrayOfString;
    }

    private int getBankPosition(String[] paramArrayOfString, String paramString) {
        int i = 0;
        while (true)
            if (i < paramArrayOfString.length) {
                if (paramString.equals(paramArrayOfString[i]))
                    break;
                i++;
                continue;
            } else {
                i = -1;
            }
        return i;
    }

    private void initAddressButton() {
        this.addressButton = ((TextView) findViewById(R.id.address_button));
        this.addressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent = new Intent(AddnewBankCardActivity.this,
                        ChoiceCityActivity.class);
                AddnewBankCardActivity.this.startActivityForResult(localIntent,
                        1);
            }
        });
    }

    private void initWidget() {
        this.topTextView = ((TextView) findViewById(R.id.topText));
        this.cardTypeRadioGroup = ((RadioGroup) findViewById(R.id.bankcardtype));
        this.cardTypeRadioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup paramRadioGroup,
                            int paramInt) {
                        if (paramInt != R.id.yinglangrb) {
                            AddnewBankCardActivity.this.deposiBankSpiner
                                    .setVisibility(8);
                            AddnewBankCardActivity.this.creditBankSpiner
                                    .setVisibility(0);
                        } else {
                            AddnewBankCardActivity.this.deposiBankSpiner
                                    .setVisibility(0);
                            AddnewBankCardActivity.this.creditBankSpiner
                                    .setVisibility(8);
                        }
                    }
                });
        this.bankcardNumEt = ((EditText) findViewById(R.id.idcard));
        this.depositOwnerEt = ((EditText) findViewById(R.id.name));
        this.identifierIdEt = ((EditText) findViewById(R.id.identitycard));
        this.deposiBankSpiner = ((Spinner) findViewById(R.id.depositofbank));
        this.creditBankSpiner = ((Spinner) findViewById(R.id.creditbank));
        this.saveButton = ((Button) findViewById(R.id.sava_info));
        this.mobileEt = ((EditText) findViewById(R.id.mobile));
        this.saveButton.setOnClickListener(this);
        this.tipTextView = ((TextView) findViewById(R.id.tip));
        this.tipTextView.setTextColor(R.color.black);
        this.tipTextView
                .setText(Html
                        .fromHtml(" 打折店与银联通力合作，采用<a href=\"http://www.gdyilian.com/dna_A1.html\">DNA 专利技术</a>,请放心使用。您在这里输入的信息仅用于银联语音支付 ,稍候您将接到银联专线“02096585”来电，请按照语音提示操作完成付款。"));
        this.tipTextView.setMovementMethod(LinkMovementMethod.getInstance());
        this.adapter = new ArrayAdapter(this, 17367048, this.debitCardArray);
        this.adapter.setDropDownViewResource(17367049);
        this.deposiBankSpiner.setAdapter(this.adapter);
        this.deposiBankSpiner.setBackgroundResource(R.drawable.dropdownwhitebg);
        this.deposiBankSpiner.setPrompt("开户银行");
        this.deposiBankSpiner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        TextView localTextView = (TextView) paramView;
                        AddnewBankCardActivity.this.depositBank = localTextView
                                .getText().toString();
                    }

                    public void onNothingSelected(
                            AdapterView<?> paramAdapterView) {
                    }
                });
        this.creditAdapter = new ArrayAdapter(this, 17367048,
                this.creditCardArray);
        this.creditAdapter.setDropDownViewResource(17367049);
        this.creditBankSpiner.setAdapter(this.creditAdapter);
        this.creditBankSpiner.setBackgroundResource(R.drawable.dropdownwhitebg);
        this.creditBankSpiner.setPrompt("开户银行");
        this.creditBankSpiner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> paramAdapterView,
                            View paramView, int paramInt, long paramLong) {
                        TextView localTextView = (TextView) paramView;
                        AddnewBankCardActivity.this.depositBank = localTextView
                                .getText().toString();
                    }

                    public void onNothingSelected(
                            AdapterView<?> paramAdapterView) {
                    }
                });
        this.creditBankSpiner.setVisibility(0);
        this.creditBankSpiner.setVisibility(8);
    }

    private void returnDatatoOrder() {
        switch (this.cardTypeRadioGroup.getCheckedRadioButtonId()) {
        case R.id.yinglangrb:
            this.bankcardType = 1;
            break;
        case R.id.creditcard:
            this.bankcardType = 0;
        }
        this.bankcardNum = this.bankcardNumEt.getText().toString();
        this.depositOwner = this.depositOwnerEt.getText().toString();
        this.identiferId = this.identifierIdEt.getText().toString();
        this.addressText = this.addressButton.getText().toString();
        this.mobile = this.mobileEt.getText().toString();
        if ((!StringUtils.isEmpty(this.addressText))
                && (!"选择省市".equals(this.addressText))) {
            if (!StringUtils.isEmpty(this.bankcardNum)) {
                if (!StringUtils.isEmpty(this.depositOwner)) {
                    if (!StringUtils.isEmpty(this.identiferId)) {
                        if (!StringUtils.isEmpty(this.mobile)) {
                            if (StringUtils.checkPhone(this.mobile)) {
                                Intent localIntent = new Intent(this,
                                        OrderInfoActivity.class);
                                Bundle localBundle = new Bundle();
                                LogUtils.log("Addnew", "bankcardtype=="
                                        + this.bankcardType + "addresstext=="
                                        + this.addressText + "depositbank=="
                                        + this.depositBank + "bankcardmum=="
                                        + this.bankcardNum + "depositowner=="
                                        + this.depositOwner);
                                localBundle.putInt("bankcardtype",
                                        this.bankcardType);
                                localBundle.putString("addresstext",
                                        this.addressText);
                                localBundle.putString("depositbank",
                                        this.depositBank);
                                localBundle.putString("bankcardnum",
                                        this.bankcardNum);
                                localBundle.putString("depositowner",
                                        this.depositOwner);
                                localBundle.putString("identiferid",
                                        this.identiferId);
                                localBundle.putString("mobile", this.mobile);
                                localIntent.putExtra("userdepositinfo",
                                        localBundle);
                                LogUtils.log("main", this.action_type
                                        + "/ acitontype");
                                if (this.action_type != 1) {
                                    if (this.action_type == 2)
                                        setResult(2, localIntent);
                                } else
                                    setResult(1, localIntent);
                                finish();
                            } else {
                                Toast.makeText(this, "您填写的手机号码格式不对", 1).show();
                            }
                        } else
                            Toast.makeText(this, "您的支付手机号还没有填写", 1).show();
                    } else
                        Toast.makeText(this, "您的开户银行没有填写!", 1).show();
                } else
                    Toast.makeText(this, "开户人姓名还没有填写!", 1).show();
            } else
                Toast.makeText(this, "您的银行卡号还没有填写!", 1).show();
        } else
            Toast.makeText(this, "您的开户地址还没有填写!", 1).show();
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if ((paramInt2 == 1) && (paramInt2 == 1)) {
            Bundle localBundle = paramIntent.getExtras();
            this.province = localBundle.getString("province");
            this.city = localBundle.getString("city");
            if (!StringUtils.isEmpty(this.province)) {
                this.addressText = (this.province + this.city);
                this.addressButton.setText(this.addressText);
            } else {
                this.addressText = this.city;
                this.addressButton.setText(this.city);
            }
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.sava_info:
            returnDatatoOrder();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.addnewbankcard);
        initAddressButton();
        initWidget();
        Bundle localBundle = getIntent().getBundleExtra("bundle");
        this.action_type = localBundle.getInt("actionType");
        if (this.action_type != 1) {
            this.topTextView.setText("编辑银行卡");
            this.customerBank = ((CustomerBank) localBundle
                    .getSerializable("customBank"));
            if (this.customerBank != null) {
                this.bankcardNumEt.setText(this.customerBank.bankcardNumber);
                this.depositOwnerEt.setText(this.customerBank.accountHolder);
                this.bankcardNumEt.setText(this.customerBank.bankcardNumber);
                this.identifierIdEt.setText(this.customerBank.identityId);
                this.addressButton.setText(this.customerBank.address);
                this.mobileEt.setText(this.customerBank.mobile);
                int i = this.customerBank.type.intValue();
                LogUtils.log("main", "bankcardType==" + i + "/");
                if (i != 1) {
                    this.cardTypeRadioGroup.check(R.id.creditcard);
                    i = getBankPosition(this.creditCardArray,
                            this.customerBank.depositBank.trim());
                    if (i != -1)
                        this.creditBankSpiner.setSelection(i);
                    this.cardTypeRadioGroup.check(R.id.creditcard);
                } else {
                    i = getBankPosition(this.debitCardArray,
                            this.customerBank.depositBank.trim());
                    if (i != -1)
                        this.deposiBankSpiner.setSelection(i);
                    this.cardTypeRadioGroup.check(R.id.yinglangrb);
                }
            }
        } else {
            this.topTextView.setText("添加银行卡");
        }
    }
}
