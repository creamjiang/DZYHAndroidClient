package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class CouponDetailScrollView extends ScrollView {
    private ViewGroup child;
    private final int[] childLocation = new int[2];
    private boolean downInChild = false;
    private final int[] myLocation = new int[2];

    public CouponDetailScrollView(Context paramContext) {
        super(paramContext);
    }

    public CouponDetailScrollView(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CouponDetailScrollView(Context paramContext,
            AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private boolean checkChild(MotionEvent paramMotionEvent, int paramInt) {
        boolean bool = false;
        if (this.child != null) {
            float f = paramMotionEvent.getY();
            if ((f > paramInt)
                    && (f < paramInt + this.child.getMeasuredHeight()))
                bool = true;
            this.downInChild = bool;
            bool = this.downInChild;
        }
        return bool;
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool;
        if (this.child != null) {
            getLocationOnScreen(this.myLocation);
            this.child.getLocationOnScreen(this.childLocation);
            int i = this.childLocation[1] - this.myLocation[1];
            if (checkChild(paramMotionEvent, i)) {
                paramMotionEvent.setLocation(paramMotionEvent.getX(),
                        paramMotionEvent.getY() - i);
                bool = this.child.onInterceptTouchEvent(paramMotionEvent);
            } else {
                bool = super.onInterceptTouchEvent(paramMotionEvent);
            }
        } else {
            bool = super.onInterceptTouchEvent(paramMotionEvent);
        }
        return bool;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool;
        if (this.child != null) {
            if (paramMotionEvent.getAction() == 0) {
                getLocationOnScreen(this.myLocation);
                this.child.getLocationOnScreen(this.childLocation);
                int i = this.childLocation[1] - this.myLocation[1];
                if (checkChild(paramMotionEvent, i)) {
                    paramMotionEvent.setLocation(paramMotionEvent.getX(),
                            paramMotionEvent.getY() - i);
                    bool = this.child.onTouchEvent(paramMotionEvent);
                } else {
                    bool = super.onTouchEvent(paramMotionEvent);
                }
            } else if (!this.downInChild) {
                bool = super.onTouchEvent(paramMotionEvent);
            } else {
                bool = this.child.onTouchEvent(paramMotionEvent);
            }
        } else
            bool = super.onTouchEvent(paramMotionEvent);
        return bool;
    }

    public void setChild(ViewGroup paramViewGroup) {
        this.child = paramViewGroup;
    }
}
