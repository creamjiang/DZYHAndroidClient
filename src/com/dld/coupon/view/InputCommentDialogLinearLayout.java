package com.dld.coupon.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.UserConfig;
import com.dld.coupon.activity.R;

public class InputCommentDialogLinearLayout extends LinearLayout {
    private EditText editText;

    public InputCommentDialogLinearLayout(Context paramContext) {
        super(paramContext);
    }

    public InputCommentDialogLinearLayout(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void init() {
        initText();
        initCheckBox();
        initClearButton();
    }

    private void initCheckBox() {
        boolean bool = UserConfig.allowUploadSina(getContext());
        CheckBox localCheckBox = (CheckBox) findViewById(R.id.check_box);
        localCheckBox.setChecked(bool);
        localCheckBox.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                boolean i;
                if (!StringUtils.isEmpty(SharePersistent.getInstance()
                        .getPerference(
                                InputCommentDialogLinearLayout.this
                                        .getContext(), "user_phone"))) {
                    i = false;
                } else {
                    DialogHelper.showUploadSina();
                    i = true;
                }
                return i;
            }
        });
        localCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton paramCompoundButton,
                            boolean paramBoolean) {
                        UserConfig.setAllowUploadSina(
                                InputCommentDialogLinearLayout.this
                                        .getContext(), paramBoolean);
                    }
                });
    }

    private void initClearButton() {
        ((Button) findViewById(R.id.clear_button))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        InputCommentDialogLinearLayout.this.editText
                                .setText("");
                        SharePersistent.getInstance().savePerference(
                                InputCommentDialogLinearLayout.this
                                        .getContext(), "last_comment_content",
                                "");
                    }
                });
    }

    private void initText() {
        String str = SharePersistent.getInstance().getPerference(getContext(),
                "last_comment_content");
        this.editText = ((EditText) findViewById(R.id.edit_text));
        if (!StringUtils.isEmpty(str))
            this.editText.setText(str);
    }

    public DialogInterface.OnClickListener getOnCancelListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface,
                    int paramInt) {
                String str = InputCommentDialogLinearLayout.this.editText
                        .getText().toString();
                if (!StringUtils.isEmpty(str))
                    SharePersistent.getInstance().savePerference(
                            InputCommentDialogLinearLayout.this.getContext(),
                            "last_comment_content", str);
            }
        };
    }

    public DialogInterface.OnClickListener getOnCommentListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface,
                    int paramInt) {
                if (!StringUtils
                        .isEmpty(InputCommentDialogLinearLayout.this.editText
                                .getText().toString()))
                    StringUtils.isEmpty(SharePersistent.getInstance()
                            .getPerference(
                                    InputCommentDialogLinearLayout.this
                                            .getContext(), "user_phone"));
                else
                    Toast.makeText(ActivityManager.getCurrent(), "请输入评论内容", 0);
            }
        };
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }
}
