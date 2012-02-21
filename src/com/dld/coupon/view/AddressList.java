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

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressList extends ExpandableListView {
    private MyAdapter adapter;
    private Map<Integer, List<String>> addressesMap;
    private String city;
    private Context context;
    private GenericDAO dao = GenericDAO.getInstance(this.context);
    private List<String> districts;
    private Listener listener;
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        public void onScroll(AbsListView paramAbsListView, int paramInt1,
                int paramInt2, int paramInt3) {
            Object localObject = AddressList.this
                    .getItemAtPosition(paramInt1 + 1);
            AddressList.Listener localListener = AddressList.this.listener;
            if (localObject != null)
                localObject = localObject.toString();
            else
                localObject = null;
            localListener.onScroll((String) localObject);
        }

        public void onScrollStateChanged(AbsListView paramAbsListView,
                int paramInt) {
        }
    };

    public AddressList(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
    }

    private List<String> getAddresses(int paramInt) {
        Object localObject = (List<String>) this.addressesMap.get(Integer.valueOf(paramInt));
        if (localObject == null) {
            localObject = (String) this.districts.get(paramInt);
            localObject = this.dao.getAddresses(this.city, (String) localObject);
            this.addressesMap.put(Integer.valueOf(paramInt),(List<String>) localObject);
        }
        return (List<String>) localObject;
    }

    public void collapse(String paramString) {
        collapseGroup(this.districts.indexOf(paramString));
    }

    public void init(Listener paramListener, String paramString) {
        this.city = SharePersistent.getInstance().getPerference(this.context,
                "city_name");
        this.districts = this.dao.getDistricts(this.city);
        this.addressesMap = new HashMap();
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
                boolean bool = true;
                String str = null;
                if (paramInt2 != 0) {
                    bool = false;
                   // str = (String) AddressList.this.getAddresses(paramInt1).get(paramInt2);
                    List list=AddressList.this.getAddresses(paramInt1);
    				if (!list.isEmpty()) {
    					String[] strings=(String[]) list.get(paramInt2);
    					str=strings[0];
    				}
                } else if (paramInt1 != 0) {
                    str = (String) AddressList.this.districts.get(paramInt1);
                } else {
                    str = null;
                }
                listener.onSelected(str, bool);
                return true;
            }
        });
    }

    public static abstract interface Listener {
        public abstract void onScroll(String paramString);

        public abstract void onSelected(String paramString, boolean paramBoolean);
    }

    class MyAdapter extends BaseExpandableListAdapter {
        MyAdapter() {
        }

        public Object getChild(int paramInt1, int paramInt2) {
            return AddressList.this.districts.get(paramInt1);
        }

        public long getChildId(int paramInt1, int paramInt2) {
            return paramInt2;
        }

        public View getChildView(int paramInt1, int paramInt2,
                boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) AddressList.this.context.getSystemService("layout_inflater")).inflate(R.layout.address_textview_layout, null);
            TextView localTextView = (TextView) localView
                    .findViewById(R.id.single_textview);
            localTextView.setTextColor(-12369085);
            Object object=AddressList.this.getAddresses(paramInt1).get(paramInt2);
            Class theClass = object.getClass();
            if (String.class.equals(theClass)) {
            	localTextView.setText((String) object);
			}else {
				List list=AddressList.this.getAddresses(paramInt1);//get(paramInt2));
				if (!list.isEmpty()) {
					String[] strings=(String[]) list.get(paramInt2);
					localTextView.setText(strings[0]);
				}
	            
			}
            
            
            return localView;
        }

        public int getChildrenCount(int paramInt) {
            return AddressList.this.getAddresses(paramInt).size();
        }

        public Object getGroup(int paramInt) {
            return null;
        }

        public int getGroupCount() {
            return AddressList.this.districts.size();
        }

        public long getGroupId(int paramInt) {
            return paramInt;
        }

        public View getGroupView(int paramInt, boolean paramBoolean,
                View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) AddressList.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.address_item_layout, null);
            ImageView localImageView = (ImageView) localView
                    .findViewById(R.id.indicator);
            int i;
            if (!paramBoolean)
                i = R.drawable.right_small;
            else
                i = R.drawable.down_small;
            localImageView.setImageResource(i);
            ((TextView) localView.findViewById(2131492889))
                    .setText((String) AddressList.this.districts.get(paramInt));
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
