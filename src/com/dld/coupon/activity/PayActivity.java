package com.dld.coupon.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.CommonProtocolHelper;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;

public class PayActivity extends BaseActivity {
    private String amount;
    Button feedbackBt;
    EditText feedbackText;
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what != 1) {
                if (paramMessage.what == 2) {
                    PayActivity.this.dismissProgressDailog();
                    Toast.makeText(ActivityManager.getCurrent(),
                            "发送信息失败，请您稍后再试", 0).show();
                }
            } else {
                PayActivity.this.dismissProgressDailog();
                Toast.makeText(ActivityManager.getCurrent(), "支付成功。", 1).show();
            }
            PayActivity.this.finish();
        }
    };
    EditText mailText;
    EditText password;
    private Dialog progressDialog;
    private String website;

    private void dismissProgressDailog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private void initFeedbackButton() {
        this.feedbackBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                String str1 = PayActivity.this.mailText.getText().toString();
                String str2 = PayActivity.this.password.getText().toString();
                if (StringUtils.isEmpty(str1))
                    Toast.makeText(ActivityManager.getCurrent(), "请输入您的银行卡卡号",
                            0).show();
                while (true) {
                    // return;
                    if (StringUtils.isEmpty(str2)) {
                        Toast.makeText(ActivityManager.getCurrent(),
                                "请输入您的手机号码", 0).show();
                        continue;
                    }
                    try {
                        PayActivity.this.dismissProgressDailog();
                        PayActivity.this.progressDialog = DialogHelper
                                .getProgressBar("支付中，请稍候...");
                        PayActivity.this.progressDialog.show();
                        SharePersistent localSharePersistent = SharePersistent
                                .getInstance();
                        localSharePersistent.savePerference(
                                ActivityManager.getCurrent(), "pay_card", str1);
                        localSharePersistent.savePerference(
                                ActivityManager.getCurrent(), "pay_phone", str2);
                        CommonProtocolHelper.pay(PayActivity.this,
                                PayActivity.this.website,
                                PayActivity.this.amount, str1, str2)
                                .startTrans(
                                        new Protocol.OnJsonProtocolResult() {
                                            public void onException(
                                                    String paramString,
                                                    Exception paramException) {
                                                LogUtils.e("test",
                                                        paramException);
                                                PayActivity.this.handler
                                                        .sendEmptyMessage(2);
                                            }

                                            public void onResult(
                                                    String paramString,
                                                    Object paramObject) {
                                                PayActivity.this.handler
                                                        .sendEmptyMessage(1);
                                            }
                                        }, 1800);
                    } catch (Exception localException) {
                        LogUtils.e("test", localException);
                        PayActivity.this.handler.sendEmptyMessage(2);
                    }
                }
            }
        });
    }

    public void init() {
        setContentView(R.layout.pay);
        this.website = ((String) Cache.remove("website"));
        this.amount = Cache.remove("amount").toString();
        ((TextView) findViewById(2131493044)).setText("订单金额：" + this.amount
                + "元");
        this.feedbackBt = ((Button) findViewById(R.id.feedback_button));
        this.mailText = ((EditText) findViewById(R.id.mail_field));
        this.password = ((EditText) findViewById(R.id.password_field));
        Object localObject = SharePersistent.getInstance();
        String str = ((SharePersistent) localObject).getPerference(this,
                "pay_card");
        localObject = ((SharePersistent) localObject).getPerference(this,
                "pay_phone");
        if (!StringUtils.isEmpty(str))
            this.mailText.setText(str);
        if (!StringUtils.isEmpty((String) localObject))
            this.password.setText((CharSequence) localObject);
        initFeedbackButton();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        init();
    }
}
