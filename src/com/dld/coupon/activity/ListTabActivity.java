package com.dld.coupon.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.db.Category;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.CategoryList;
import com.dld.coupon.view.CategoryList.Listener;
import com.dld.coupon.activity.R;

public abstract class ListTabActivity extends BaseActivity implements
        CityActivity.NotifyChangeObserver {
    protected static final int PAGE_SIZE = 10;
    protected String address;
    protected Category category;
    protected TextView categoryButton;
    private CategoryList categoryList;
    private TextView categoryName;
    protected String city = SharePersistent.get("city_name");
    protected String cityId = SharePersistent.get("city_id");
    protected int pageIndex = 1;
    private View scrollingCategory;
    public boolean showing;

    protected void hideList() {
        if (this.categoryList != null) {
            this.categoryList.setVisibility(8);
            this.scrollingCategory.setVisibility(8);
            this.categoryButton.setText(this.category.name);
        }
    }

    protected abstract void init();

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
                        && (ListTabActivity.this.categoryList.getVisibility() == 0)) {
                    ListTabActivity.this.scrollingCategory.setVisibility(0);
                    ListTabActivity.this.categoryName
                            .setText(paramCategory.name);
                } else {
                    ListTabActivity.this.scrollingCategory.setVisibility(8);
                }
            }

            public void onSelected(Category paramCategory) {
                ListTabActivity.this.categoryList.setVisibility(8);
                ListTabActivity.this.scrollingCategory.setVisibility(8);
                ListTabActivity.this.categoryButton.setText(paramCategory.name);
                if (ListTabActivity.this.category.id != paramCategory.id) {
                    ListTabActivity.this.category = paramCategory;
                    ListTabActivity.this.pageIndex = 1;
                    ListTabActivity.this.request();
                }
            }
        }, this.category);
        this.categoryButton = ((TextView) findViewById(R.id.keyword_spinner));
        if (this.category.id != 255)
            this.categoryButton.setText(this.category.name);
        this.categoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                int i;
                if (ListTabActivity.this.categoryList.getVisibility() != 8)
                    i = 0;
                else
                    i = 1;
                ListTabActivity.this.hideList();
                if (i != 0)
                    ListTabActivity.this.onCategoryClicked();
            }
        });
        this.scrollingCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                ListTabActivity.this.categoryList
                        .collapse(ListTabActivity.this.categoryName.getText()
                                .toString());
            }
        });
    }

    public boolean onBack() {
        boolean i;
        if ((this.categoryList != null)
                && (this.categoryList.getVisibility() != 8)) {
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

    protected abstract void request();
}
