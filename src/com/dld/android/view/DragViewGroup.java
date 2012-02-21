package com.dld.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;

import com.dld.android.util.LogUtils;
import com.dld.coupon.util.Common;

import java.util.ArrayList;
import java.util.HashMap;

public class DragViewGroup extends ViewGroup {
    protected static final int INVALID_SCREEN = -1;
    protected static final int MOVE_DISTANCE = 200;
    protected static final int SNAP_VELOCITY = 500;
    protected static final int STATE_DRAGGING = 1;
    protected static final int STATE_REST = 0;
    protected boolean allowLongPress;
    protected HashMap<View, ArrayList<Rect>> cannotInterceptRect;
    protected int childIndex;
    protected int deltaX;
    protected boolean init = true;
    protected float lastMotionX;
    protected long lastSnapped;
    protected int mMaximumVelocity;
    protected Scroller mScroller;
    protected DragViewGroupNavigation navigation;
    protected int nextIndex;
    public int position;
    protected int scrollX;
    protected int scrollY;
    protected int state;
    protected float touchSlop = 100.0F * Common.getScale();
    protected VelocityTracker velocityTracker;

    public DragViewGroup(Context paramContext) {
        super(paramContext);
        init();
    }

    public DragViewGroup(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public void addCannotInterceptTouchEventChildRect(View paramView,
            Rect paramRect) {
        int m = getChildCount();
        int j = 0;
        int i = 0;
        int k = 0;
        while (i < m) {
            View localView = getChildAt(i);
            if ((localView != paramView) && (!isChild(localView, paramView))) {
                i++;
                continue;
            }
            if (this.cannotInterceptRect == null)
                this.cannotInterceptRect = new HashMap();
            ArrayList localArrayList = (ArrayList) this.cannotInterceptRect
                    .get(localView);
            if (localArrayList == null)
                localArrayList = new ArrayList();
            localArrayList.add(paramRect);
            this.cannotInterceptRect.put(getChildAt(i), localArrayList);
            k = 1;
        }
        if (k != 0)
            return;
        throw new IllegalArgumentException(
                "Cannot find the argumnet child in children!");
    }

    protected boolean canIntercept(int paramInt1, int paramInt2) {
        boolean bool = false;
        View localView = getChildAt(this.childIndex);
        ArrayList localArrayList = null;
        int i = 0;
        int j = 0;
        if ((this.cannotInterceptRect != null)
                && (!this.cannotInterceptRect.isEmpty())) {
            localArrayList = (ArrayList) this.cannotInterceptRect
                    .get(localView);
            if ((localArrayList != null) && (!localArrayList.isEmpty())) {
                bool = false;
                i = 0;
                j = localArrayList.size();
            }
        }
        while (true) {
            if (i < j) {
                bool = ((Rect) localArrayList.get(i)).contains(paramInt1,
                        paramInt2);
                if (!bool)
                    ;
            } else {
                return bool;
            }
            i++;
        }
    }

    void clearChildrenCache() {
        setChildrenDrawnWithCacheEnabled(false);
    }

    protected void clearVelcityTracker() {
        if (this.velocityTracker != null) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    public void computeScroll() {
        if (!this.mScroller.computeScrollOffset()) {
            if (this.nextIndex != -1) {
                this.childIndex = Math.max(0,
                        Math.min(this.nextIndex, -1 + getChildCount()));
                this.nextIndex = -1;
                clearChildrenCache();
            }
        } else {
            this.scrollX = this.mScroller.getCurrX();
            this.scrollY = this.mScroller.getCurrY();
            scrollTo(this.scrollX, this.scrollY);
            postInvalidate();
        }
    }

    protected void dispatchDraw(Canvas paramCanvas) {
        int i;
        if ((this.state == 1) || (this.nextIndex != -1))
            i = 0;
        else
            i = 1;
        if (i == 0) {
            long l = getDrawingTime();
            if ((this.nextIndex < 0) || (this.nextIndex >= getChildCount())
                    || (Math.abs(this.childIndex - this.nextIndex) != 1)) {
                int k = getChildCount();
                for (int j = 0; j < k; j++)
                    drawChild(paramCanvas, getChildAt(j), l);
            }
            drawChild(paramCanvas, getChildAt(this.childIndex), l);
            drawChild(paramCanvas, getChildAt(this.nextIndex), l);
        } else {
            drawChild(paramCanvas, getChildAt(this.childIndex),
                    getDrawingTime());
        }
    }

    public boolean dispatchUnhandledMove(View paramView, int paramInt) {
        return super.dispatchUnhandledMove(paramView, paramInt);
    }

    protected int doRender(int paramInt, View paramView) {
        if (paramView.getVisibility() != 8) {
            int i = paramView.getMeasuredWidth();
            paramView.layout(paramInt, 0, paramInt + i,
                    paramView.getMeasuredHeight());
            paramInt += i;
        }
        return paramInt;
    }

    void enableChildrenCache() {
        setChildrenDrawnWithCacheEnabled(true);
    }

    public int getFocusIndex() {
        return this.childIndex;
    }

    public long getLastSnapped() {
        return this.lastSnapped;
    }

    protected void init() {
        this.mMaximumVelocity = 1000;
        this.mScroller = new Scroller(getContext());
    }

    protected boolean isChild(View paramView1, View paramView2) {
        boolean i = false;
        if ((paramView1 instanceof ViewParent)) {
            ViewParent localViewParent = paramView2.getParent();
            while (localViewParent != null) {
                if (localViewParent != paramView1) {
                    localViewParent = localViewParent.getParent();
                    continue;
                }
                i = true;
            }
        }
        return i;
    }

    public boolean isScrolling() {
        boolean i;
        if (!this.mScroller.isFinished())
            i = true;
        else
            i = false;
        return i;
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean i = false;
        int m = paramMotionEvent.getAction();
        int j = (int) paramMotionEvent.getX();
        int k = (int) paramMotionEvent.getY();
        switch (m) {
        case 0:
            if (canIntercept(j, k))
                // break label129;
                return i;
            this.lastMotionX = j;
            this.allowLongPress = true;
            if (!this.mScroller.isFinished())
                j = 1;
            else
                j = 0;
            this.state = j;
            break;
        case 1:
        case 3:
            clearChildrenCache();
            break;
        case 2:
            if (canIntercept(j, k))
                // break label129;
                return i;
            setDraggingState(j);
        }
        if (this.state != 0)
            i = true;
        label129: return i;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4) {
        render();
        this.init = false;
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        int j = getChildCount();
        for (int i = 0; i < j; i++)
            getChildAt(i).measure(paramInt1, paramInt2);
    }

    protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
        int i;
        if (this.nextIndex == -1)
            i = this.childIndex;
        else
            i = this.nextIndex;
        getChildAt(i).requestFocus(paramInt, paramRect);
        return false;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if (this.mScroller.isFinished()) {
            int i = paramMotionEvent.getAction();
            if (this.velocityTracker == null)
                this.velocityTracker = VelocityTracker.obtain();
            this.velocityTracker.addMovement(paramMotionEvent);
            float f2 = paramMotionEvent.getX();
            float f1 = paramMotionEvent.getY();
            int j;
            switch (i) {
            default:
                break;
            case 0:
                if (canIntercept((int) f2, (int) f1))
                    break;
                this.lastMotionX = f2;
                break;
            case 1:
                if (this.state == 1) {
                    VelocityTracker localVelocityTracker = this.velocityTracker;
                    localVelocityTracker
                            .computeCurrentVelocity(this.mMaximumVelocity);
                    j = (int) localVelocityTracker.getXVelocity();
                    if ((j <= 500) || (Math.abs(this.deltaX) <= 200)
                            || (this.childIndex <= 0)) {
                        if ((j >= -500) || (Math.abs(this.deltaX) <= 200)
                                || (this.childIndex >= -1 + getChildCount()))
                            snapToDestination();
                        else
                            snapToScreen(1 + this.childIndex);
                    } else
                        snapToScreen(-1 + this.childIndex);
                    clearChildrenCache();
                }
                clearVelcityTracker();
                this.allowLongPress = false;
                this.state = 0;
                break;
            case 2:
                setDraggingState((int) f2);
                if (this.state != 1)
                    break;
                this.deltaX = (int) (this.lastMotionX - f2);
                this.lastMotionX = f2;
                if (this.deltaX >= 0) {
                    if (this.deltaX <= 0)
                        break;
                    j = getChildAt(-1 + getChildCount()).getRight()
                            - this.scrollX - getWidth();
                    if (j <= 0)
                        break;
                    j = Math.min(j, this.deltaX);
                    scrollBy(j, 0);
                    this.scrollX = (j + this.scrollX);
                    postInvalidate();
                } else {
                    if (this.scrollX <= 0)
                        break;
                    j = Math.max(-this.scrollX, this.deltaX);
                    scrollBy(j, 0);
                    this.scrollX = (j + this.scrollX);
                    postInvalidate();
                }
                break;
            case 3:
                this.state = 0;
            }
        }
        return false;
    }

    protected void render() {
        int k = 0;
        int j = getChildCount();
        int i = 0;
        while (true) {
            if (i >= j) {
                if ((this.init) && (this.position > 0))
                    show(this.position);
                return;
            }
            View localView = getChildAt(i);
            try {
                k = doRender(k, localView);
                k = k;
                i++;
            } catch (Exception localException) {
                while (true)
                    LogUtils.e("test", localException);
            }
        }
    }

    public void reset() {
        if (this.mScroller.isFinished()) {
            this.lastSnapped = System.currentTimeMillis();
            this.childIndex = 0;
            this.mScroller.startScroll(this.scrollX, 0, -this.scrollX, 0, 0);
            this.scrollX = 0;
            invalidate();
        }
    }

    public boolean rest() {
        boolean i;
        if (this.state != 0)
            i = false;
        else
            i = true;
        return i;
    }

    protected void setDraggingState(int paramInt) {
        int i;
        if ((int) Math.abs(paramInt - this.lastMotionX) <= this.touchSlop)
            i = 0;
        else
            i = 1;
        if (i != 0) {
            this.state = 1;
            enableChildrenCache();
            if (this.allowLongPress) {
                this.allowLongPress = false;
                getChildAt(this.childIndex).cancelLongPress();
            }
        }
    }

    public void setNavigation(
            DragViewGroupNavigation paramDragViewGroupNavigation) {
        this.navigation = paramDragViewGroupNavigation;
    }

    public boolean show(int paramInt) {
        boolean i = false;
        if (this.mScroller.isFinished()) {
            snapToScreen(paramInt, false);
            clearChildrenCache();
            i = true;
        }
        return i;
    }

    protected void snapToDestination() {
        int i = getChildAt(this.childIndex).getWidth();
        snapToScreen((this.scrollX + i / 2) / i);
    }

    public void snapToScreen(int paramInt) {
        snapToScreen(paramInt, true);
    }

    void snapToScreen(int paramInt, boolean paramBoolean) {
        if (this.mScroller.isFinished()) {
            this.lastSnapped = System.currentTimeMillis();
            int j = Math.max(0, Math.min(paramInt, -1 + getChildCount()));
            int k;
            if (j == this.childIndex)
                k = 0;
            else
                k = 1;
            this.nextIndex = j;
            View localView = getFocusedChild();
            if ((localView != null) && (k != 0)
                    && (localView == getChildAt(this.childIndex)))
                localView.clearFocus();
            if ((paramBoolean) && (k != 0) && (this.navigation != null))
                this.navigation.onChange(this.nextIndex, this.childIndex,
                        getChildCount());
            int i = this.scrollX;
            j = j * getChildAt(this.childIndex).getWidth() - i;
            Scroller localScroller = this.mScroller;
            int m;
            if (!this.init)
                m = 2 * Math.abs(j);
            else
                m = 0;
            localScroller.startScroll(i, 0, j, 0, m);
            invalidate();
        }
    }
}
