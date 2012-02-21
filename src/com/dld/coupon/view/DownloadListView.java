package com.dld.coupon.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;

public class DownloadListView extends ListView implements
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private AdapterView.OnItemClickListener mOnItemClickL;

    public DownloadListView(Context paramContext) {
        super(paramContext);
        init();
    }

    public DownloadListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public DownloadListView(Context paramContext,
            AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private DownLoadAdapter getDownLoadAdapter() {
        return (DownLoadAdapter) getAdapter();
    }

    private void init() {
        setOnScrollListener(this);
    }

    private boolean isMax() {
        DownLoadAdapter localDownLoadAdapter = getDownLoadAdapter();
        boolean i;
        if (localDownLoadAdapter.getCount() < localDownLoadAdapter.getMax())
            i = false;
        else
            i = true;
        return i;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
            int paramInt, long paramLong) {
        if (paramInt < getDownLoadAdapter().getListCount())
            this.mOnItemClickL.onItemClick(paramAdapterView, paramView,
                    paramInt, paramLong);
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1,
            int paramInt2, int paramInt3) {
        if (((paramInt1 != 0) || (paramInt2 != 0) || (paramInt3 != 0))
                && (!isMax())) {
            DownLoadAdapter localDownLoadAdapter = getDownLoadAdapter();
            if ((localDownLoadAdapter.mState == 0)
                    && (paramInt1 + paramInt2 == paramInt3))
                localDownLoadAdapter.notifyDownload();
        }
    }

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
    }

    public void reset() {
        setAdapter(getAdapter());
    }

    public void setAdapter(ListAdapter paramListAdapter) {
        ((DownLoadAdapter) paramListAdapter).mState = 1;
        super.setAdapter(paramListAdapter);
    }

    public void setOnItemClickListener(
            AdapterView.OnItemClickListener paramOnItemClickListener) {
        this.mOnItemClickL = paramOnItemClickListener;
        super.setOnItemClickListener(this);
    }

    public static abstract class DownLoadAdapter extends BaseAdapter {
        static final int STATE_EXCEPTION = 2;
        static final int STATE_LOADING = 1;
        static final int STATE_NORMAL = 0;
        static final int STATE_NO_RESULT = 3;
        private Handler mAnimStartHandle = new Handler() {
            public void handleMessage(Message paramMessage) {
                ((AnimationDrawable) paramMessage.obj).start();
            }
        };
        private int mState = 1;
        private String noResultMessage;

        private View startLoading() {
            View localView = getFooter();
            AnimationDrawable localAnimationDrawable = (AnimationDrawable) ((ImageView) localView
                    .findViewById(R.id.download_list_footer_img)).getDrawable();
            Message localMessage = new Message();
            localMessage.obj = localAnimationDrawable;
            this.mAnimStartHandle.sendMessage(localMessage);
            return localView;
        }

        public void clear() {
            this.mState = 1;
        }

        public abstract Context getContext();

        public int getCount() {
            int i = getListCount();
            switch (this.mState) {
            case 1:
            case 2:
                i++;
                break;
            case 3:
                i++;
            }
            return i;
        }

        public View getExceptionView() {
            View localView = ((LayoutInflater) getContext().getSystemService(
                    "layout_inflater")).inflate(
                    R.layout.updatelist_networkerr_footer, null);
            ((Button) localView.findViewById(R.id.updatelist_networkerr_button))
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            DownloadListView.DownLoadAdapter.this.mState = 1;
                            DownloadListView.DownLoadAdapter.this
                                    .notifyDataSetChanged();
                            DownloadListView.DownLoadAdapter.this
                                    .onNotifyDownload();
                        }
                    });
            return localView;
        }

        public View getFooter() {
            return ((LayoutInflater) getContext().getSystemService(
                    "layout_inflater")).inflate(R.layout.download_list_footer,
                    null);
        }

        public abstract int getListCount();

        public abstract int getMax();

        public View getNoResultView() {
            LinearLayout localLinearLayout = (LinearLayout) ((LayoutInflater) getContext()
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.no_result_list, null);
            if (!StringUtils.isEmpty(this.noResultMessage))
                ((TextView) localLinearLayout.findViewById(R.id.text))
                        .setText(this.noResultMessage);
            ((Button) localLinearLayout
                    .findViewById(R.id.updatelist_networkerr_button))
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            DownloadListView.DownLoadAdapter.this.mState = 1;
                            DownloadListView.DownLoadAdapter.this
                                    .notifyDataSetChanged();
                            DownloadListView.DownLoadAdapter.this
                                    .onNotifyDownload();
                        }
                    });
            return localLinearLayout;
        }

        public abstract View getView(int paramInt);

        public final View getView(int paramInt, View paramView,
                ViewGroup paramViewGroup) {
            View localView;
            if (paramInt >= getListCount()) {
                localView = null;
                switch (this.mState) {
                default:
                    break;
                case 1:
                    localView = startLoading();
                    break;
                case 2:
                    localView = getExceptionView();
                    break;
                case 3:
                    localView = getNoResultView();
                    break;
                }
            } else {
                localView = getView(paramInt);
            }
            return localView;
        }

        void notifyDownload() {
            LogUtils.log("xiaozhi", "notifyDownload");
            this.mState = 1;
            notifyDataSetChanged();
            onNotifyDownload();
        }

        public void notifyDownloadBack() {
            this.mState = 0;
            notifyDataSetChanged();
        }

        public void notifyException() {
            this.mState = 2;
            notifyDataSetChanged();
        }

        public void notifyNoResult() {
            this.mState = 3;
            notifyDataSetChanged();
        }

        public void notifyNoResult(String paramString) {
            this.mState = 3;
            this.noResultMessage = paramString;
            notifyDataSetChanged();
        }

        public abstract void onNotifyDownload();
    }
}
