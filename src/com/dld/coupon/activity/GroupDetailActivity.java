package com.dld.coupon.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.view.GroupDetailView;
import com.dld.coupon.view.Showable;
import com.dld.protocol.json.Group;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupDetailActivity extends BaseActivity {
    private Group group = new Group();
    private Handler handler = new Handler();
    private LayoutInflater layoutInflater;
    private int position;
    private Protocol protocol;
    private Showable showable;
    private DragViewGroup viewGroup;
    private List<LinearLayout> views = new ArrayList();

    private void addEmptyView() {
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.linear, null);
        this.viewGroup.addView(localLinearLayout);
        this.views.add(localLinearLayout);
    }

    private void init() {
        this.layoutInflater = LayoutInflater.from(this);
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.detail_container, null);
        this.viewGroup = ((DragViewGroup) localLinearLayout
                .findViewById(R.id.view_flipper));
        int i = 0;
        int j = this.group.details.size();
        while (i < j) {
            addEmptyView();
            i++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                if ((paramInt1 >= paramInt3 - 2)
                        && (paramInt1 < Math.min(-2
                                + GroupDetailActivity.this.group.total, 98))) {
                    int i = 0;
                    int j = Math.min(GroupDetailActivity.this.group.total
                            - paramInt3, 10);
                    while (i < j) {
                        GroupDetailActivity.this.addEmptyView();
                        i++;
                    }
                    Param localParam = GroupDetailActivity.this.protocol
                            .getParam();
                    localParam.append("pi", 1 + String.valueOf(Integer
                            .parseInt(localParam.get("pi"))));
                    GroupDetailActivity.this.protocol
                            .startTrans(new GroupDetailActivity.RecommendProtocolResult());
                }
                GroupDetailActivity.this.position = paramInt1;
                GroupDetailActivity.this.initViews();
            }
        });
        setContentView(localLinearLayout);
    }

    private void initViews() {
        updateTitle();
        int j = 0;
        int i = this.views.size();
        while (j < i) {
            LinearLayout localLinearLayout = (LinearLayout) this.views.get(j);
            if ((j < -1 + this.position) || (j > 1 + this.position)) {
                if (localLinearLayout.getChildCount() > 0) {
                    ((GroupDetailView) localLinearLayout.getChildAt(0))
                            .onDestroy();
                    localLinearLayout.removeAllViews();
                }
            } else {
                Object localObject;
                if (localLinearLayout.getChildCount() != 0) {
                    if (j < this.group.details.size()) {
                        localObject = (GroupDetail) this.group.details.get(j);
                        ((GroupDetailView) localLinearLayout.getChildAt(0))
                                .onCreate((GroupDetail) localObject,
                                        this.showable, false);
                    }
                } else {
                    localObject = (GroupDetailView) this.layoutInflater
                            .inflate(R.layout.group_detail, null);
                    localLinearLayout.addView((View) localObject);
                    if (j < this.group.details.size())
                        ((GroupDetailView) localObject).onCreate(
                                (GroupDetail) this.group.details.get(j),
                                this.showable, false);
                }
            }
            j++;
        }
    }

    private void updateTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("团购信息详情页 ("
                + (1 + this.position) + "/" + this.group.total + ")");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        try {
            Group localGroup = (Group) Cache.remove("group");
            if (localGroup != null) {
                this.group.details.addAll(localGroup.details);
                this.group.total = localGroup.total;
            }
            this.position = ((Integer) Cache.remove("position")).intValue();
            this.showable = ((Showable) Cache.remove("showable"));
            this.protocol = ((Protocol) Cache.remove("protocol"));
            init();
            initViews();
            initSegment();
            this.viewGroup.position = this.position;
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Iterator localIterator = this.views.iterator();
        while (localIterator.hasNext()) {
            LinearLayout localLinearLayout = (LinearLayout) localIterator
                    .next();
            if (localLinearLayout.getChildCount() <= 0)
                continue;
            ((GroupDetailView) localLinearLayout.getChildAt(0)).onDestroy();
            localLinearLayout.removeAllViews();
        }
    }

    private class RecommendProtocolResult extends Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            GroupDetailActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                Group localGroup = (Group) paramObject;
                if (localGroup.details.size() > 0) {
                    GroupDetailActivity.this.group.details
                            .addAll(localGroup.details);
                    GroupDetailActivity.this.group.total = localGroup.total;
                    GroupDetailActivity.this.handler.post(new Runnable() {
                        public void run() {
                            GroupDetailActivity.this.initViews();
                        }
                    });
                }
            }
        }
    }
}
