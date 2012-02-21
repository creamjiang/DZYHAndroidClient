package com.dld.coupon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dld.coupon.db.Category;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.activity.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryList extends ExpandableListView {
    private MyAdapter adapter;
    private Map<Integer, List<Category>> categoriesMap;
    private Context context;
    private GenericDAO dao = GenericDAO.getInstance(this.context);
    private Listener listener;
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        public void onScroll(AbsListView paramAbsListView, int paramInt1,
                int paramInt2, int paramInt3) {
            Object localObject = CategoryList.this
                    .getItemAtPosition(paramInt1 + 1);
            CategoryList.this.listener.onScroll((Category) localObject);
        }

        public void onScrollStateChanged(AbsListView paramAbsListView,
                int paramInt) {
        }
    };
    private List<Category> topCategories;

    public CategoryList(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
    }

    private List<Category> getCategories(int paramInt) {
        Object localObject = (List) this.categoriesMap.get(Integer
                .valueOf(paramInt));
        if (localObject == null) {
            localObject = (Category) this.topCategories.get(paramInt);
            localObject = this.dao.listCategories((Category) localObject);
            this.categoriesMap.put(Integer.valueOf(paramInt),
                    (List<Category>) localObject);
        }
        return (List<Category>) localObject;
    }

    public void collapse(String paramString) {
        int i = 0;
        int j = this.topCategories.size();
        while (i < j) {
            if (((Category) this.topCategories.get(i)).name != paramString) {
                i++;
                continue;
            }
            collapseGroup(i);
        }
    }

    public void init(Listener paramListener, Category paramCategory) {
        this.topCategories = this.dao.listCategories();
        this.categoriesMap = new HashMap();
        this.adapter = new MyAdapter();
        this.listener = paramListener;
        setAdapter(this.adapter);
        setOnScrollListener(this.scrollListener);
        setChildIndicator(null);
        setGroupIndicator(null);
        setFadingEdgeLength(0);
        setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(
                    ExpandableListView paramExpandableListView, View paramView,
                    int paramInt1, int paramInt2, long paramLong) {
                List localList = CategoryList.this.getCategories(paramInt1);
                listener.onSelected((Category) localList.get(paramInt2));
                return true;
            }
        });
        if ((paramCategory != null) && (paramCategory.id != 255)
                && (paramCategory.id != 0))
            expandGroup(this.topCategories.indexOf(this.dao
                    .getCategory(paramCategory.pid.intValue())));
    }

    public static abstract interface Listener {
        public abstract void onScroll(Category paramCategory);

        public abstract void onSelected(Category paramCategory);
    }

    class MyAdapter extends BaseExpandableListAdapter {
        MyAdapter() {
        }

        public Object getChild(int paramInt1, int paramInt2) {
            return CategoryList.this.topCategories.get(paramInt1);
        }

        public long getChildId(int paramInt1, int paramInt2) {
            return paramInt2;
        }

        public View getChildView(int paramInt1, int paramInt2,
                boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) CategoryList.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.address_textview_layout, null);
            TextView localTextView = (TextView) localView
                    .findViewById(R.id.single_textview);
            localTextView.setTextColor(-12369085);
            Category localCategory = (Category) CategoryList.this
                    .getCategories(paramInt1).get(paramInt2);
            if ((localCategory.pid.intValue() != 0) || (localCategory.id == 0)
                    || (localCategory.id == 255))
                localTextView.setText(localCategory.name);
            else
                localTextView.setText("全部分类");
            return localView;
        }

        public int getChildrenCount(int paramInt) {
            return CategoryList.this.getCategories(paramInt).size();
        }

        public Object getGroup(int paramInt) {
            return null;
        }

        public int getGroupCount() {
            return CategoryList.this.topCategories.size();
        }

        public long getGroupId(int paramInt) {
            return paramInt;
        }

        public View getGroupView(int paramInt, boolean paramBoolean,
                View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) CategoryList.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.address_item_layout, null);
            ImageView localImageView = (ImageView) localView
                    .findViewById(2131492890);
            int i;
            if (!paramBoolean)
                i = R.drawable.right_small;
            else
                i = R.drawable.down_small;
            localImageView.setImageResource(i);
            ((TextView) localView.findViewById(2131492889))
                    .setText(((Category) CategoryList.this.topCategories
                            .get(paramInt)).name);
            return localView;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int paramInt1, int paramInt2) {
            return true;
        }
    }
}
