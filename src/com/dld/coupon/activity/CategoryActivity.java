package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dld.coupon.db.Category;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.coupon.activity.R;
import java.util.List;

public class CategoryActivity extends BaseActivity {
    private List<Category> children;
    protected DownloadListView list;
    private String title;
    private int type;

    private void initTitle() {
        ((TextView) findViewById(R.id.title_text)).setText(this.title);
    }

    private void setAdapter() {
        DownloadListView.DownLoadAdapter local2 = new DownloadListView.DownLoadAdapter() {
            public Context getContext() {
                return CategoryActivity.this;
            }

            public Object getItem(int paramInt) {
                return CategoryActivity.this.children.get(paramInt);
            }

            public long getItemId(int paramInt) {
                return paramInt;
            }

            public int getListCount() {
                return CategoryActivity.this.children.size();
            }

            public int getMax() {
                return CategoryActivity.this.children.size();
            }

            public View getView(int paramInt) {
                View localView = ((LayoutInflater) getContext()
                        .getSystemService("layout_inflater")).inflate(
                        R.layout.category_item_layout, null);
                ((TextView) localView.findViewById(R.id.text))
                        .setText(((Category) CategoryActivity.this.children
                                .get(paramInt)).name);
                return localView;
            }

            public void onNotifyDownload() {
            }
        };
        this.list.setAdapter(local2);
        local2.notifyDownloadBack();
    }

    private void setOnClickListener() {
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                Category localCategory = (Category) CategoryActivity.this.children
                        .get(paramInt);
                Activity localActivity = ActivityManager.getCurrent();
                Intent localIntent = new Intent();
                localIntent.setClass(localActivity, SearchResultActivity.class);
                localIntent.putExtra("type", CategoryActivity.this.type);
                localIntent.putExtra("cat", localCategory.id);
                localIntent.putExtra("cat_name", localCategory.name);
                localActivity.startActivity(localIntent);
            }
        });
    }

    public void initList() {
        this.list = ((DownloadListView) findViewById(R.id.listview));
        setAdapter();
        setOnClickListener();
    }

    public void initParams() {
        Object localObject = getIntent();
        this.type = ((Intent) localObject).getIntExtra("type", 0);
        int i = ((Intent) localObject).getIntExtra("cat", 1);
        this.title = ((Intent) localObject).getStringExtra("cat_name");
        localObject = GenericDAO.getInstance();
        this.children = ((GenericDAO) localObject)
                .listCategories(((GenericDAO) localObject).getCategory(i));
        ((Category) this.children.get(0)).name = "全部";
    }

    public void initUI() {
        initTitle();
        initSegment();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.category);
        initParams();
        initUI();
        initList();
    }
}
