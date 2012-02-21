package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.dld.android.util.LogUtils;
import com.dld.coupon.util.Common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NestedScrollView extends ScrollView {
    private Method abortAnimation;
    private Method isFinished;
    private boolean mIsBeingDragged = false;
    private float mLastMotionY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private Object mScroller;
    private float mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public NestedScrollView(Context paramContext) {
        super(paramContext);
        loadConfig();
    }

    public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        loadConfig();
    }

    public NestedScrollView(Context paramContext,
            AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        loadConfig();
    }

    private boolean canScroll() {
        boolean i = false;
        View localView = getChildAt(0);
        if (localView != null) {
            int j = localView.getHeight();
            if (getHeight() < j + getPaddingTop() + getPaddingBottom())
                i = true;
        }
        return i;
    }

    private void loadScroller() throws Exception {
        if (this.mScroller == null) {
            Field localField = ScrollView.class.getDeclaredField("mScroller");
            localField.setAccessible(true);
            this.mScroller = localField.get(this);
            this.isFinished = this.mScroller.getClass().getMethod("isFinished",
                    new Class[0]);
            this.abortAnimation = this.mScroller.getClass().getMethod(
                    "abortAnimation", new Class[0]);
        }
    }

    public void loadConfig() {
        ViewConfiguration localViewConfiguration = ViewConfiguration
                .get(getContext());
        this.mTouchSlop = (20.0F * Common.getScale());
        this.mMinimumVelocity = localViewConfiguration
                .getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = localViewConfiguration
                .getScaledMaximumFlingVelocity();
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean j = true;
        super.onInterceptTouchEvent(paramMotionEvent);
        int i = paramMotionEvent.getAction();
        if ((i == 2) && (this.mIsBeingDragged))
            ;
        // for (i = j; ; i = 0)
        // {
        // return i;
        // if (canScroll())
        // break;
        // this.mIsBeingDragged = false;
        // }
        float f = paramMotionEvent.getY();
        switch (i) {
        default:
        case 2:
        case 0:
        case 1:
        case 3:
        }
        // while (true)
        {
            boolean bool = this.mIsBeingDragged;
            // break;
            if ((int) Math.abs(f - this.mLastMotionY) <= this.mTouchSlop)
                return false;
            // continue;
            this.mIsBeingDragged = j;
            // continue;
            this.mLastMotionY = f;
            try {
                loadScroller();
                if (((Boolean) this.isFinished.invoke(this.mScroller,
                        new Object[0])).booleanValue())
                    j = false;
                this.mIsBeingDragged = j;
            } catch (Exception localException) {
                LogUtils.log("test", localException);
                this.mIsBeingDragged = false;
            }
            // continue;
            this.mIsBeingDragged = false;
        }
        return j;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean i = false;
        super.onTouchEvent(paramMotionEvent);
        if ((paramMotionEvent.getAction() == 0)
                && (paramMotionEvent.getEdgeFlags() != 0))
            ;
        // do
        // return i;
        // while (!canScroll());
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
        this.mVelocityTracker.addMovement(paramMotionEvent);
        int m = paramMotionEvent.getAction();
        float f = paramMotionEvent.getY();
        switch (m) {
        default:
        case 0:
        case 2:
        case 1:
        }
        // while (true)
        {
            int j = 1;
            // break;
            try {
                loadScroller();
                if (!((Boolean) this.isFinished.invoke(this.mScroller,
                        new Object[0])).booleanValue())
                    this.abortAnimation.invoke(this.mScroller, new Object[0]);
                this.mLastMotionY = j;
            } catch (Exception localException) {
                // while (true)
                LogUtils.e("test", localException);
            }
            int n = (int) (this.mLastMotionY - j);
            this.mLastMotionY = j;
            if (n < 0) {
                if (getScrollY() <= 0)
                    return i;
                // continue;
                scrollBy(0, n);
                // continue;
            }
            if (n <= 0)
                return i;
            // continue;
            j = getHeight() - getPaddingBottom();
            j = getChildAt(0).getBottom() - getScrollY() - j;
            if (j <= 0)
                return i;
            // continue;
            scrollBy(0, Math.min(j, n));
            // continue;
            VelocityTracker localVelocityTracker = this.mVelocityTracker;
            localVelocityTracker.computeCurrentVelocity(1000,
                    this.mMaximumVelocity);
            int k = (int) localVelocityTracker.getYVelocity();
            if ((Math.abs(k) > this.mMinimumVelocity) && (getChildCount() > 0))
                fling(-k);
            if (this.mVelocityTracker == null)
                return i;
            // continue;
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        return i;
    }
}
