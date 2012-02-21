package com.dld.coupon.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.EditText;

import com.dld.coupon.util.Common;
import com.dld.coupon.util.StringUtils;

public abstract class PromptDialog extends AlertDialog.Builder implements
        DialogInterface.OnClickListener {
    private final View view;

    public PromptDialog(Context paramContext, String paramString1,
            String paramString2) {
        this(paramContext, paramString1, paramString2, null);
    }

    public PromptDialog(Context paramContext, String paramString1,
            String paramString2, View paramView) {
        super(paramContext);
        setTitle(paramString1);
        if (paramView == null) {
            EditText localEditText = new EditText(paramContext);
            if (!StringUtils.isEmpty(paramString2)) {
                localEditText.setText(paramString2);
                localEditText.setSelection(paramString2.length());
            }
            paramView = localEditText;
        }
        this.view = paramView;
        setView(paramView);
        setPositiveButton("确定", this);
        setNegativeButton("取消", this);
        show();
    }

    public void onCancelClicked(DialogInterface paramDialogInterface) {
        Common.hideKeyboard(this.view);
        paramDialogInterface.dismiss();
    }

    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
        if (paramInt != -1) {
            onCancelClicked(paramDialogInterface);
        } else {
            onOkClicked(this.view);
            paramDialogInterface.dismiss();
        }
    }

    public abstract void onOkClicked(View paramView);
}
