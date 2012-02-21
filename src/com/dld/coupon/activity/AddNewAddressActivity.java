package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.coupon.db.MailingAddress;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;

public class AddNewAddressActivity extends Activity implements
        View.OnClickListener {
    private int action_type = 0;
    private String city = "北京";
    private String customName = null;
    private String customPhone = null;
    private EditText customerNameEt = null;
    private EditText customerPhoneEt = null;
    private MailingAddress mailingAddress = null;
    private TextView modifyCityTV = null;
    private String partAddress = null;
    private EditText partAddrssEt = null;
    private String postCode = null;
    private EditText postCodeEt = null;
    private String proAndcity = null;
    private TextView proandcityTextView = null;
    private String province = "";
    private Button saveAndUserBt = null;
    private TextView topTextView = null;

    private void checkUserInfo() {
        this.proAndcity = this.proandcityTextView.getText().toString();
        this.partAddress = this.partAddrssEt.getText().toString();
        this.postCode = this.postCodeEt.getText().toString();
        this.customPhone = this.customerPhoneEt.getText().toString();
        this.customName = this.customerNameEt.getText().toString();
        if (!StringUtils.isEmpty(this.proAndcity)) {
            if (!StringUtils.isEmpty(this.partAddress)) {
                if (!StringUtils.isEmpty(this.customName)) {
                    if (!StringUtils.isEmpty(this.customPhone)) {
                        if (StringUtils.checkPhone(this.customPhone)) {
                            Intent localIntent = new Intent(this,
                                    OrderInfoActivity.class);
                            Bundle localBundle = new Bundle();
                            localBundle.putString("province", this.province);
                            localBundle.putString("city", this.city);
                            localBundle.putString("partaddress",
                                    this.partAddress);
                            localBundle
                                    .putString("customname", this.customName);
                            localBundle.putString("postcode", this.postCode);
                            localBundle.putString("customphone",
                                    this.customPhone);
                            localIntent.putExtra("userinfo", localBundle);
                            if (this.action_type != 1)
                                setResult(3, localIntent);
                            else
                                setResult(1, localIntent);
                            finish();
                        } else {
                            Toast.makeText(this, "您填写的手机号码格式不对", 1).show();
                        }
                    } else
                        Toast.makeText(this, "收件人的手机号码没有填写", 1).show();
                } else
                    Toast.makeText(this, "您的收件人姓名没有填写", 1).show();
            } else
                Toast.makeText(this, "您的详细地址没有填写", 1).show();
        } else
            Toast.makeText(this, "您的省市没有填写", 1).show();
    }

    private void initWidget() {
        this.topTextView = ((TextView) findViewById(R.id.topText));
        this.proandcityTextView = ((TextView) findViewById(R.id.provandcity));
        this.partAddrssEt = ((EditText) findViewById(R.id.detail_address));
        this.postCodeEt = ((EditText) findViewById(R.id.postcode));
        this.customerNameEt = ((EditText) findViewById(R.id.customer_name));
        this.customerPhoneEt = ((EditText) findViewById(R.id.customer_phone));
        this.saveAndUserBt = ((Button) findViewById(R.id.sava_info));
        this.modifyCityTV = ((TextView) findViewById(R.id.modify_city));
        this.modifyCityTV.setOnClickListener(this);
        this.saveAndUserBt.setOnClickListener(this);
        Bundle localBundle = getIntent().getBundleExtra("bundle");
        this.action_type = localBundle.getInt("actionType");
        if (this.action_type != 1) {
            this.topTextView.setText("编辑收件人地址");
            this.mailingAddress = ((MailingAddress) localBundle
                    .get("mailingAddress"));
            if (this.mailingAddress != null) {
                this.proAndcity = null;
                this.partAddress = this.partAddrssEt.getText().toString();
                this.postCode = this.postCodeEt.getText().toString();
                this.customPhone = this.customerPhoneEt.getText().toString();
                this.customName = this.customerNameEt.getText().toString();
                if (this.mailingAddress.province == null)
                    this.proandcityTextView.setText(this.mailingAddress.city);
                else
                    this.proandcityTextView
                            .setText(this.mailingAddress.province
                                    + this.mailingAddress.city);
                this.partAddrssEt.setText(this.mailingAddress.partAddress);
                if (!StringUtils.isEmpty(this.mailingAddress.zipCode))
                    this.postCodeEt.setText(this.mailingAddress.zipCode);
                if (!StringUtils.isEmpty(this.mailingAddress.receiverName))
                    this.customerNameEt
                            .setText(this.mailingAddress.receiverName);
                if (!StringUtils.isEmpty(this.mailingAddress.receiverMobile))
                    this.customerPhoneEt
                            .setText(this.mailingAddress.receiverMobile);
            }
        } else {
            this.topTextView.setText("添加新地址");
        }
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt2 == 1) {
            Bundle localBundle = paramIntent.getExtras();
            this.province = localBundle.getString("province");
            this.city = localBundle.getString("city");
            if (!StringUtils.isEmpty(this.province)) {
                this.proandcityTextView.setText(this.province + this.city);
            } else {
                this.province = null;
                this.proandcityTextView.setText(this.city);
            }
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.modify_city:
            startActivityForResult(new Intent(this, ChoiceCityActivity.class),
                    1);
            break;
        case R.id.sava_info:
            checkUserInfo();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.add_receiving_address);
        initWidget();
    }
}
