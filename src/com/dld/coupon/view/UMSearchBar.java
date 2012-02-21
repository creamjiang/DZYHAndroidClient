package com.dld.coupon.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;

public class UMSearchBar extends LinearLayout {
    private ArrayAdapter<String> adapter;
    private OnSearchListener defaultClickListener = new OnSearchListener() {
        public void onSearch(int paramInt, String paramString) {
            new Intent().addFlags(268435456);
        }
    };
    private int position = 0;
    private AutoCompleteTextView searchField;
    private OnSearchListener searchListener;

    public UMSearchBar(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void init() {
        this.position = 0;
        this.searchField = ((AutoCompleteTextView) findViewById(R.id.main_input_search));
        this.adapter = new ArrayAdapter(getContext(), R.layout.dropdown_item);
        this.searchField.setAdapter(this.adapter);
        this.searchField.setThreshold(0);
        TextView localTextView = (TextView) findViewById(R.id.search_spinner);
        localTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        try {
                            paramDialogInterface.dismiss();
                            UMSearchBar.this.position = paramInt;
                            String[] arrayOfString = ActivityManager
                                    .getCurrent().getResources()
                                    .getStringArray(R.array.search_types);
                            searchField
                                    .setText(arrayOfString[UMSearchBar.this.position]);
                            return;
                        } catch (Exception localException) {
                            while (true)
                                LogUtils.e("test", "", localException);
                        }
                    }
                };
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            UMSearchBar.this.getContext());
                    builder.setTitle("请选择搜索类型")
                            .setSingleChoiceItems(2131165186,
                                    UMSearchBar.this.position, local1).show();
                    return;
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", "", localException);
                }
            }
        });
        findViewById(R.id.search_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        UMSearchBar.this.startSearch();
                    }
                });
        this.searchField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt,
                    KeyEvent paramKeyEvent) {
                boolean i = false;
                if ((paramKeyEvent.getAction() == 1) && (paramInt == 66)) {
                    UMSearchBar.this.startSearch();
                    i = true;
                }
                return i;
            }
        });
    }

    public String getKeyword() {
        return this.searchField.getEditableText().toString();
    }

    public int getPosition() {
        return this.position;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void setOnSearchListener(OnSearchListener paramOnSearchListener) {
        this.searchListener = paramOnSearchListener;
    }

    public void startSearch() {
        String str = this.searchField.getText().toString().trim();
        if (!StringUtils.isEmpty(str)) {
            if (this.searchListener == null)
                this.searchListener = this.defaultClickListener;
            Common.hideKeyboard(this);
            if (this.searchListener != null)
                this.searchListener.onSearch(this.position, str);
        }
    }

    public static abstract interface OnSearchListener {
        public abstract void onSearch(int paramInt, String paramString);
    }
}
