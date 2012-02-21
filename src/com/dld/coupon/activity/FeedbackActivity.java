package com.dld.coupon.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.CommonProtocolHelper;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends BaseActivity {
    Button feedbackBt;
    EditText feedbackText;
    private Handler handler = new Handler();
    EditText mailText;
    private Dialog progressDialog;

    private void dismissProgressDailog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    public static boolean emailFormat(String paramString) {
        return Pattern
                .compile(
                        "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
                .matcher(paramString).find();
    }

    private void initFeedbackButton() {
        this.feedbackBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                String str2 = FeedbackActivity.this.feedbackText.getText()
                        .toString();
                String str1 = FeedbackActivity.this.mailText.getText()
                        .toString();
                String str3 = null;
                Object localObject = null;
                if (StringUtils.isEmpty(str2))
                    Toast.makeText(ActivityManager.getCurrent(), "请输入反馈内容", 0)
                            .show();
                while (true) {
                    // return;
                    if (!StringUtils.isEmpty(str1)) {
                        if (!FeedbackActivity.emailFormat(str1)) {
                            if (!FeedbackActivity.phoneFormat(str1)) {
                                Toast.makeText(ActivityManager.getCurrent(),
                                        "请输入正确邮箱地址或手机号码", 0).show();
                                break;
                            }
                        }
                        str3 = str1;
                    }
                    while (true) {
                        try {
                            FeedbackActivity.this.dismissProgressDailog();
                            FeedbackActivity.this.progressDialog = DialogHelper
                                    .getProgressBar("正在上传反馈内容...");
                            FeedbackActivity.this.progressDialog
                                    .setCancelable(false);
                            FeedbackActivity.this.progressDialog.show();
                            CommonProtocolHelper.feedback(
                                    ActivityManager.getCurrent(), str2, str3,
                                    (String) localObject).startTrans(
                                    new Protocol.OnJsonProtocolResult() {
                                        public void onException(
                                                String paramString,
                                                Exception paramException) {
                                            FeedbackActivity.this.handler
                                                    .post(new Runnable() {
                                                        public void run() {
                                                            FeedbackActivity.this
                                                                    .dismissProgressDailog();
                                                            Toast.makeText(
                                                                    ActivityManager
                                                                            .getCurrent(),
                                                                    "提交失败，请稍后重试",
                                                                    0).show();
                                                        }
                                                    });
                                        }

                                        public void onResult(
                                                String paramString,
                                                Object paramObject) {
                                            FeedbackActivity.this.handler
                                                    .post(new Runnable() {
                                                        public void run() {
                                                            FeedbackActivity.this
                                                                    .dismissProgressDailog();
                                                            Toast.makeText(
                                                                    ActivityManager
                                                                            .getCurrent(),
                                                                    "您的反馈信息已提交。感谢您的支持。",
                                                                    0).show();
                                                        }
                                                    });
                                        }
                                    }, 600, 1);
                        } catch (Exception localException) {
                            LogUtils.e("test", "", localException);
                        }
                        if (!FeedbackActivity.phoneFormat(str1)) {
                            Toast.makeText(ActivityManager.getCurrent(),
                                    "请输入正确邮箱地址或手机号码", 0).show();
                            break;
                        }
                        // break label169;
                        // localObject = localException;
                    }
                }
            }
        });
    }

    private void initTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("意见反馈");
    }

    public static boolean phoneFormat(String paramString) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")
                .matcher(paramString).find();
    }

    public void init() {
        setContentView(R.layout.feedback);
        this.feedbackBt = ((Button) findViewById(R.id.submit_feedback));
        this.mailText = ((EditText) findViewById(R.id.mail_field));
        this.feedbackText = ((EditText) findViewById(R.id.feedback_field));
        initTitle();
        initFeedbackButton();
        initSegment();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        init();
    }
}
