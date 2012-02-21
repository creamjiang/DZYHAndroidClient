package com.dld.coupon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.android.view.SegmentControl;
import com.dld.android.view.SegmentControlAdapter;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.BottomTabGroup;
import com.dld.coupon.activity.R;

public abstract class BaseActivity extends Activity {
    public static final int[] BUTTON_ID;
    public static final String[] BUTTON_TEXT;
    public static final String Base = "BaseActivity";
    public static final int[] FOCUS_IMAGE_ID;
    public static final int[] UNFOCUS_IMAGE_ID;
    public static int segmentPosition;

    static {
        int[] localObject = new int[5];
        localObject[0] = R.drawable.index;
        localObject[1] = R.drawable.around;
        localObject[2] = R.drawable.discount;
        localObject[3] = R.drawable.shopping;
        localObject[4] = R.drawable.cheapskatecard;
        FOCUS_IMAGE_ID = localObject;
        localObject = new int[5];
        localObject[0] = R.drawable.index_blue;
        localObject[1] = R.drawable.around_blue;
        localObject[2] = R.drawable.discount_blue;
        localObject[3] = R.drawable.shopping_blue;
        localObject[4] = R.drawable.cheapskatecard_blue;
        UNFOCUS_IMAGE_ID = localObject;
        String[] butText = new String[5];
        butText[0] = "首页";
        butText[1] = "周边";
        butText[2] = "爱打折";
        butText[3] = "逛街店";
        butText[4] = "小气鬼卡";
        BUTTON_TEXT = butText;
        localObject = new int[5];
        localObject[0] = R.id.button_arround;
        localObject[1] = R.id.button_recommand;
        localObject[2] = R.id.button_more;
        localObject[3] = R.id.button_group;
        localObject[4] = R.id.button_store;
        BUTTON_ID = localObject;
    }

    private View buildButton(int paramInt) {
    	RelativeLayout localLinearLayout = (RelativeLayout) findViewById(BUTTON_ID[paramInt]);
        ((ImageView) localLinearLayout.findViewById(R.id.image))
                .setImageDrawable(getUnfocusImageDrawable(paramInt));
        TextView localTextView = (TextView) localLinearLayout
                .findViewById(R.id.text);
        localTextView.setText(BUTTON_TEXT[paramInt]);
       // localTextView.setTextColor(R.color.button_text_color);
        return localLinearLayout;
    }

    private Drawable getFocusImageDrawable(int paramInt) {
        return getApplicationContext().getResources().getDrawable(
                FOCUS_IMAGE_ID[paramInt]);
    }

    private Drawable getUnfocusImageDrawable(int paramInt) {
        return getApplicationContext().getResources().getDrawable(
                UNFOCUS_IMAGE_ID[paramInt]);
    }

    public void finish() {
        super.finish();
        ActivityManager.pop();
    }

    protected void initSegment() {
    	Object btg = (BottomTabGroup) findViewById(R.id.bottom_button_group);
        ((SegmentControl) btg).setAdapter(new SegmentControlAdapter() {
            public boolean disable() {
                return false;
            }

            public int getCount() {
                return BaseActivity.BUTTON_TEXT.length;
            }

            public int getDefaultFocusIndex() {
                return segmentPosition;
            }

            public View getView(int paramInt) {
                return BaseActivity.this.buildButton(paramInt);
            }

            public void onBack() {
                Common.startActivity(MainActivity.class);
            }

            public void onBlur(View paramView, int paramInt) {
            }

            public void onFocus(View paramView, int paramInt) {
                BaseActivity.segmentPosition = paramInt;
                Common.startActivity(MainActivity.class);
            }
        });
        
        ((BottomTabGroup)btg).changeFocus(segmentPosition);
        btg = ((BottomTabGroup)btg).findViewById(BUTTON_ID[segmentPosition]);
        ((ImageView)((View)btg).findViewById(R.id.image)).setImageDrawable(getFocusImageDrawable(segmentPosition));
       // ((View)btg).setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.menu_focus));
        ((TextView)((View)btg).findViewById(R.id.text)).setTextColor(R.color.white);
  /**20120220
   *     ((SegmentControl) btg).changeFocus(segmentPosition); 
        btg = (BottomTabGroup) findViewById(BUTTON_ID[segmentPosition]);
        ((ImageView) (btg).findViewById(R.id.image))
                .setImageDrawable(getFocusImageDrawable(segmentPosition));
        btg.setBackgroundDrawable(getApplicationContext().getResources()
                .getDrawable(R.drawable.menu_focus));
        ((TextView) (btg).findViewById(R.id.text)).setTextColor(-1);
        **/
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setRequestedOrientation(1);
        LogUtils.log("BaseActivity", "baseactivity oncreate execute");
        ActivityManager.push(this);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        super.onCreateOptionsMenu(paramMenu);
        paramMenu.add(0, 0, 0, "设置").setIcon(R.drawable.menu_setting);
        paramMenu.add(0, 1, 1, "城市设置").setIcon(R.drawable.changecity);
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id")))
            paramMenu.add(0, 2, 2, "退出登录").setIcon(R.drawable.loginoff_menu);
        else
            paramMenu.add(0, 2, 2, "登录").setIcon(R.drawable.login_menu);
        paramMenu.add(0, 3, 3, "客服电话").setIcon(R.drawable.phone);
        paramMenu.add(0, 4, 4, "意见反馈").setIcon(R.drawable.feedback);
        paramMenu.add(0, 5, 5, "关于").setIcon(R.drawable.about);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        super.onOptionsItemSelected(paramMenuItem);
        Object localObject = new Intent();
        switch (paramMenuItem.getItemId()) {
        case 0:
            startActivity(new Intent(ActivityManager.getCurrent(),
                    MydoujiaoSettingActivity.class));
            break;
        case 1:
            ((Intent) localObject).setClass(ActivityManager.getCurrent(),
                    CityActivity.class);
            ActivityManager.getCurrent().startActivity((Intent) localObject);
            break;
        case 2:
            if (!StringUtils.isEmpty(SharePersistent.get("customer_id"))) {
                SharePersistent.getInstance().removePerference(this,
                        "customer_id");
                localObject = new Intent();
                ((Intent) localObject).setAction("dld.change");
                sendBroadcast((Intent) localObject);
                if (!(ActivityManager.getCurrent() instanceof InviteActivity))
                    break;
                ActivityManager.getCurrent().finish();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            break;
        case 3:
            localObject = new Intent("android.intent.action.DIAL");
            ((Intent) localObject).setData(Uri.parse("tel:4006266868"));
            ActivityManager.getCurrent().startActivity((Intent) localObject);
            break;
        case 4:
            localObject = new Intent();
            ((Intent) localObject).setClass(ActivityManager.getCurrent(),
                    FeedbackActivity.class);
            ActivityManager.getCurrent().startActivity((Intent) localObject);
            break;
        case 5:
            Builder dialog = new AlertDialog.Builder(
                    ActivityManager.getCurrent());
            dialog.setTitle("关于");
            dialog.setMessage("打折店优惠\n\n版本 2.3\n\n网址: www.dld.com");
            dialog.setPositiveButton("返回",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                        }
                    });
            dialog.show();
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        paramMenu.removeItem(2);
        if (!StringUtils.isEmpty(SharePersistent.get("customer_id")))
            paramMenu.add(0, 2, 2, "退出登录").setIcon(2130837650);
        else
            paramMenu.add(0, 2, 2, "登录").setIcon(2130837648);
        return true;
    }

    protected void onResume() {
        super.onResume();
        LogUtils.log("BaseActivity", "onresume ActivityManager.add(this) ");
        ActivityManager.push(this);
    }
}
