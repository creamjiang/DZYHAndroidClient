package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.CityActivity;
import com.dld.coupon.activity.CityActivity.NotifyChangeObserver;
import com.dld.coupon.db.Category;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;

public abstract class ListTab extends LinearLayout implements
        CityActivity.NotifyChangeObserver {
    protected static final int PAGE_SIZE = 10;
    protected Activity activity = ActivityManager.getCurrent();
    protected String address;
    private TextView addressButton;
    private AddressList addressList;
    private String addressText;
    protected Category category;
    protected TextView categoryButton;
    private CategoryList categoryList;
    private TextView categoryName;
    protected String city = SharePersistent.get("city_name");
    protected String cityId = SharePersistent.get("city_id");
    private TextView districtName;
    protected int pageIndex = 1;
    private View scrollingCategory;
    private View scrollingDistrict;
    public boolean showing;

    public ListTab(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    protected void hideList() {
        if (this.addressList != null) {
            this.addressList.setVisibility(8);
            this.scrollingDistrict.setVisibility(8);
            this.addressButton.setText(this.addressText);
        }
        if (this.categoryList != null) {
            this.categoryList.setVisibility(8);
            this.scrollingCategory.setVisibility(8);
            this.categoryButton.setText(this.category.name);
        }
    }

    protected abstract void init();

    protected void initAddressList(String paramString, boolean paramBoolean) {
        this.scrollingDistrict = findViewById(R.id.scrolling_district);
        this.districtName = ((TextView) this.scrollingDistrict
                .findViewById(2131492889));
        this.addressList = ((AddressList) findViewById(R.id.addresslist));
        this.addressButton = ((TextView) findViewById(R.id.address_button));
        if (paramString == null) {
            if (!StringUtils.isEmpty(this.city)) {
                this.addressButton.setText("全" + this.city);
                this.addressText = ("全" + this.city);
            }
        } else {
            if (!paramBoolean)
                this.addressText = paramString;
            else
                this.addressText = ("全" + paramString);
            this.addressButton.setText(this.addressText);
            this.address = paramString;
        }
        this.addressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                int i;
                if (ListTab.this.addressList.getVisibility() != 8)
                    i = 0;
                else
                    i = 1;
                ListTab.this.hideList();
                if (i != 0) {
                    ListTab.this.scrollingDistrict.setVisibility(0);
                    ListTab.this.addressList.setVisibility(0);
                    ListTab.this.addressButton.setText("选择商圈");
                }
            }
        });
        this.scrollingDistrict.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                ListTab.this.addressList.collapse(ListTab.this.districtName
                        .getText().toString());
            }
        });
        if (!StringUtils.isEmpty(this.cityId))
            this.addressList.init(new AddressList.Listener() {
                public void onScroll(String paramString) {
                    if ((paramString != null)
                            && (ListTab.this.addressList.getVisibility() == 0)) {
                        ListTab.this.scrollingDistrict.setVisibility(0);
                        ListTab.this.districtName.setText(paramString);
                    } else {
                        ListTab.this.scrollingDistrict.setVisibility(8);
                    }
                }

                public void onSelected(String paramString, boolean paramBoolean) {
                    ListTab.this.addressList.setVisibility(8);
                    ListTab.this.scrollingDistrict.setVisibility(8);
                    if (paramString != null) {
                        if (!paramBoolean)
                            ListTab.this.addressText = paramString;
                        else
                            ListTab.this.addressText = ("全" + paramString);
                    } else
                        ListTab.this.addressText = ("全" + ListTab.this.city);
                    ListTab.this.addressButton
                            .setText(ListTab.this.addressText);
                    if (!StringUtils.equals(ListTab.this.address, paramString)) {
                        ListTab.this.address = paramString;
                        ListTab.this.pageIndex = 1;
                        ListTab.this.request();
                    }
                }
            }, this.address);
    }

    protected void initCategoryList() {
        initCategoryList(255);
    }

    protected void initCategoryList(int paramInt) {
        this.category = GenericDAO.getInstance().getCategory(paramInt);
        this.scrollingCategory = findViewById(R.id.scrolling_category);
        this.categoryName = ((TextView) this.scrollingCategory
                .findViewById(R.id.category_item_title));
        this.categoryList = ((CategoryList) findViewById(R.id.categorylist));
        this.categoryList.init(new CategoryList.Listener() {
            public void onScroll(Category paramCategory) {
                if ((paramCategory != null)
                        && (ListTab.this.categoryList.getVisibility() == 0)) {
                    ListTab.this.scrollingCategory.setVisibility(View.VISIBLE);
                    ListTab.this.categoryName.setText(paramCategory.name);
                } else {
                    ListTab.this.scrollingCategory.setVisibility(View.GONE);
                }
            }

            public void onSelected(Category paramCategory) {
                ListTab.this.categoryList.setVisibility(View.GONE);
                ListTab.this.scrollingCategory.setVisibility(View.GONE);
                ListTab.this.categoryButton.setText(paramCategory.name);
                if (ListTab.this.category.id != paramCategory.id) {
                    ListTab.this.category = paramCategory;
                    ListTab.this.pageIndex = 1;
                    ListTab.this.request();
                }
            }
        }, this.category);
        this.categoryButton = ((TextView) findViewById(R.id.keyword_spinner));
        if (this.category.id != 255)
            this.categoryButton.setText(this.category.name);
        this.categoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                int i;
                if (ListTab.this.categoryList.getVisibility() != 8)
                    i = 0;
                else
                    i = 1;
                ListTab.this.hideList();
                if (i != 0)
                    ListTab.this.onCategoryClicked();
            }
        });
        this.scrollingCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                ListTab.this.categoryList.collapse(ListTab.this.categoryName
                        .getText().toString());
            }
        });
    }

    public boolean onBack() {
        boolean i;
        if (((this.addressList != null) && (this.addressList.getVisibility() != 8))
                || ((this.categoryList != null) && (this.categoryList
                        .getVisibility() != 8))) {
            hideList();
            i = true;
        } else {
            i = false;
        }
        return i;
    }

    protected void onCategoryClicked() {
        this.scrollingCategory.setVisibility(0);
        this.categoryList.setVisibility(0);
        this.categoryButton.setText("选择分类");
    }

    public void onChangeCity(String paramString1, String paramString2) {
        if (!StringUtils.isEmpty(paramString2)) {
            this.cityId = paramString2;
            this.city = paramString1;
            init();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        CityActivity.registObserver(this);
    }

    protected abstract void request();
}
