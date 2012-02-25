package com.dld.coupon.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.MD5Encoder;
import com.dld.coupon.util.PhoneUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Response;
import com.dld.protocol.json.User;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;

public class RegisterActivity extends BaseActivity implements
        View.OnClickListener {
    private final int EXCEPTION = 2;
    private final int GETDATA = 1;
    private String email;
    private EditText emailEdiText;
    private String firstPsw;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            if (RegisterActivity.this.registerDialog != null)
                RegisterActivity.this.registerDialog.dismiss();
            switch (paramMessage.what) {
            case 1:
                if (RegisterActivity.this.response.code != 0) {
                    DialogHelper
                            .commonDialog(RegisterActivity.this.response.message);
                } else {
                    User localUser = RegisterActivity.this.response.user;
                    localUser.username = RegisterActivity.this.email;
                    localUser.password = RegisterActivity.this.firstPsw;
                    SharePersistent.getInstance().saveUserInfo(
                            RegisterActivity.this, localUser);
                    Toast.makeText(RegisterActivity.this, "注册成功", 1).show();
                    RegisterActivity.this.finish();
                    ActivityManager.getCurrent().finish();
                    SharePersistent.getInstance().savePerference(
                            ActivityManager.getCurrent(), "customer_id",
                            RegisterActivity.this.response.user.id);
                }
                break;
            case 2:
                RegisterActivity.this
                        .cancelDialog(RegisterActivity.this.registerDialog);
                DialogHelper.commonDialog("注册失败,请稍后再试!");
            }
        }
    };
    private EditText passwordwEdiText;
    private Button registerButton;
    private ProgressDialog registerDialog;
    private Response response;

    private void cancelDialog(ProgressDialog paramProgressDialog) {
        if (paramProgressDialog != null)
            paramProgressDialog.dismiss();
    }

    private void doRegister() {
        this.email = this.emailEdiText.getText().toString();
        this.firstPsw = this.passwordwEdiText.getText().toString();
        if (this.email != null) {
            if (this.email.matches("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+")) {
                if (!this.firstPsw.equals("")) {
                    if (this.firstPsw.length() >= 6) {
                        showRegisterDialog(this);
                        new Thread(new Runnable() {
                            public void run() {
                                RegisterActivity.this.registerRequest();
                            }
                        }).start();
                    } else {
                        DialogHelper.commonDialog("密码长度至少为6位");
                    }
                } else
                    DialogHelper.commonDialog("密码不能为空  ");
            } else
                DialogHelper.commonDialog("邮箱的格式不对");
        } else
            DialogHelper.commonDialog("邮箱不能为空!");
    }

    private void initUI() {
        this.emailEdiText = ((EditText) findViewById(R.id.email));
        this.passwordwEdiText = ((EditText) findViewById(R.id.password));
        this.registerButton = ((Button) findViewById(R.id.register));
        this.registerButton.setOnClickListener(this);
        final CheckBox localCheckBox = (CheckBox) findViewById(R.id.show_password);
        View.OnClickListener local2 = new View.OnClickListener() {
            public void onClick(View paramView) {
                if (paramView != localCheckBox) {
                    // CheckBox localCheckBox = localCheckBox;
                    boolean bool;
                    if (!localCheckBox.isChecked())
                        bool = true;
                    else
                        bool = false;
                    localCheckBox.setChecked(bool);
                }
                if (!localCheckBox.isChecked())
                    RegisterActivity.this.passwordwEdiText.setInputType(129);
                else
                    RegisterActivity.this.passwordwEdiText.setInputType(144);
            }
        };
        localCheckBox.setOnClickListener(local2);
        findViewById(R.id.show_password_label).setOnClickListener(local2);
    }

    private void registerRequest() {
        LogUtils.log("test", this.firstPsw);
        String str = SharePersistent.getInstance().getPerference(this,
                "customer_id");
        if (StringUtils.isEmpty(str))
            str = "-1";
        Param localParam = new Param();
        localParam.append("email", this.email)
                .append("password", MD5Encoder.toMd5(this.firstPsw.getBytes()))
                .append("imei", PhoneUtil.getIMEI(this))
                .append("imsi", PhoneUtil.getIMSI(this)).append("ct", "0")
                .append("chn", "-1").append("gid", str);
        ProtocolHelper.registerRequest(this, localParam, false)
                .startTransForUser(new RegisterProtocolResult(), localParam);
    }

    private void showRegisterDialog(Context paramContext) {
        if (this.registerDialog == null) {
            this.registerDialog = new ProgressDialog(paramContext);
            this.registerDialog.setMessage(paramContext.getResources()
                    .getString(R.string.isregister));
        }
        this.registerDialog.setButton(
                paramContext.getResources().getString(R.string.label_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        paramDialogInterface.cancel();
                    }
                });
        this.registerDialog.show();
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
        case R.id.register:
            doRegister();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.register);
        initUI();
    }

    private class RegisterProtocolResult extends Protocol.OnJsonProtocolResult {
        public RegisterProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            RegisterActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null)
                RegisterActivity.this.response = ((Response) paramObject);
            RegisterActivity.this.handler.sendEmptyMessage(1);
        }
    }
}
