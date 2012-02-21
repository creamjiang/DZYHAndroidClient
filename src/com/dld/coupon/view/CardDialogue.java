package com.dld.coupon.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.StringUtils;

public class CardDialogue {
    private static String[] cards;
    private static String[] codes;
    private static Context context;
    public static AlertDialog dialog;
    private static int length;
    static DialogInterface.OnMultiChoiceClickListener listener;
    private static boolean[] selected;
    private static SharePersistent sp;

    static {
        String[] arrayOfString = new String[14];
        arrayOfString[0] = "中国银行";
        arrayOfString[1] = "工商银行";
        arrayOfString[2] = "建设银行";
        arrayOfString[3] = "交通银行";
        arrayOfString[4] = "招商银行";
        arrayOfString[5] = "光大银行";
        arrayOfString[6] = "兴业银行";
        arrayOfString[7] = "中信银行";
        arrayOfString[8] = "民生银行";
        arrayOfString[9] = "北京银行";
        arrayOfString[10] = "华夏银行";
        arrayOfString[11] = "深圳发展银行";
        arrayOfString[12] = "上海浦东发展银行";
        arrayOfString[13] = "广东发展银行";
        cards = arrayOfString;
        arrayOfString = new String[14];
        arrayOfString[0] = "BOC";
        arrayOfString[1] = "ICBC";
        arrayOfString[2] = "CCB";
        arrayOfString[3] = "BOCOM";
        arrayOfString[4] = "CMB";
        arrayOfString[5] = "CEB";
        arrayOfString[6] = "CIB";
        arrayOfString[7] = "CITIC";
        arrayOfString[8] = "CMBC";
        arrayOfString[9] = "BOB";
        arrayOfString[10] = "HXB";
        arrayOfString[11] = "SDB";
        arrayOfString[12] = "SPDB";
        arrayOfString[13] = "GDB";
        codes = arrayOfString;
        length = cards.length;
        selected = new boolean[length];
        load();
        listener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface paramDialogInterface,
                    int paramInt, boolean paramBoolean) {
                CardDialogue.selected[paramInt] = paramBoolean;
            }
        };
    }

    public static String getBankCodes(
            DialogInterface.OnDismissListener paramOnDismissListener) {
        String str = "";
        int i = 0;
        int j = selected.length;
        while (i < j) {
            if (selected[i] != false) {
                if (!str.equals(""))
                    str = str + ",";
                str = str + codes[i];
            }
            i++;
        }
        if ((str.length() == 0) && (paramOnDismissListener != null))
            show(ActivityManager.getCurrent(), paramOnDismissListener);
        return str;
    }

    public static String getBanks() {
        String str = "";
        int j = 0;
        int i = selected.length;
        while (j < i) {
            if (selected[j] != false) {
                if (!str.equals(""))
                    str = str + "、";
                str = str + cards[j];
            }
            j++;
        }
        return str;
    }

    private static void load() {
        sp = SharePersistent.getInstance();
        String str = sp.getPerference(ActivityManager.getCurrent(), "cards");
        if (!StringUtils.isEmpty(str))
            for (int i = 0; i < length; i++) {
                boolean[] arrayOfBoolean = selected;
                boolean j;
                if ((str.length() <= i) || (str.charAt(i) != '1'))
                    j = false;
                else
                    j = true;
                arrayOfBoolean[i] = j;
            }
        for (int i = 0; i < length; i++)
            selected[i] = true;
    }

    private static void saveSelected() {
        char[] arrayOfChar = new char[length];
        for (int j = 0; j < length; j++) {
            char i;
            if (selected[j] == false)
                i = 48;
            else
                i = 49;
            arrayOfChar[j] = i;
        }
        sp.savePerference(context, "cards", new String(arrayOfChar));
    }

    public static void show(Context paramContext,
            DialogInterface.OnDismissListener paramOnDismissListener) {
        if ((dialog == null) || (paramOnDismissListener == null)) {
            context = paramContext;
            dialog = new AlertDialog.Builder(context)
                    .setTitle("请选择银行卡")
                    .setMultiChoiceItems(cards, selected, listener)
                    .setPositiveButton("保存",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    CardDialogue.saveSelected();
                                    paramDialogInterface.dismiss();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    // CardDialogue.access$2();
                                    paramDialogInterface.dismiss();
                                }
                            }).create();
            dialog.setOnDismissListener(paramOnDismissListener);
            dialog.show();
        }
    }
}
