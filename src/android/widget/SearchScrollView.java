package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class SearchScrollView extends ScrollView {
    private ViewGroup bottomChild;
    private final int[] bottomChildLocation = new int[2];
    private boolean downInBottomChild = false;
    private boolean downInTopChild = false;
    private final int[] myLocation = new int[2];
    private ViewGroup topChild;
    private final int[] topChildLocation = new int[2];

    public SearchScrollView(Context paramContext) {
        super(paramContext);
    }

    public SearchScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public SearchScrollView(Context paramContext,
            AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private boolean checkBottomChild(MotionEvent paramMotionEvent, int paramInt) {
        boolean bool = false;
        if (this.bottomChild != null) {
            float f = paramMotionEvent.getY();
            if ((f > paramInt)
                    && (f < paramInt + this.bottomChild.getMeasuredHeight()))
                bool = true;
            this.downInBottomChild = bool;
            bool = this.downInBottomChild;
        }
        return bool;
    }

    private boolean checkTopChild(MotionEvent paramMotionEvent, int paramInt) {
        boolean bool = false;
        if (this.topChild != null) {
            float f = paramMotionEvent.getY();
            if ((f > paramInt)
                    && (f < paramInt + this.topChild.getMeasuredHeight()))
                bool = true;
            this.downInTopChild = bool;
            bool = this.downInTopChild;
        }
        return bool;
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool;
        if (paramMotionEvent.getAction() == 0) {
            getLocationOnScreen(this.myLocation);
            this.topChild.getLocationOnScreen(this.topChildLocation);
            this.bottomChild.getLocationOnScreen(this.bottomChildLocation);
            int i = this.topChildLocation[1] - this.myLocation[1];
            int j = this.bottomChildLocation[1] - this.myLocation[1];
            float f1 = paramMotionEvent.getX();
            float f2 = paramMotionEvent.getY();
            if (!checkTopChild(paramMotionEvent, i)) {
                if (!checkBottomChild(paramMotionEvent, j)) {
                    paramMotionEvent.setLocation(f1, f2);
                    bool = super.onInterceptTouchEvent(paramMotionEvent);
                } else {
                    // paramMotionEvent.setLocation(bool, f2 - j);
                    paramMotionEvent.setLocation(f1, f2 - j);
                    bool = this.bottomChild
                            .onInterceptTouchEvent(paramMotionEvent);
                }
            } else {
                // paramMotionEvent.setLocation(bool, f2 - i);
                paramMotionEvent.setLocation(f1, f2 - j);
                bool = this.topChild.onInterceptTouchEvent(paramMotionEvent);
            }
        } else if (!this.downInTopChild) {
            bool = super.onInterceptTouchEvent(paramMotionEvent);
        } else {
            bool = this.topChild.onInterceptTouchEvent(paramMotionEvent);
        }
        return bool;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool;
        if (paramMotionEvent.getAction() == 0) {
            getLocationOnScreen(this.myLocation);
            this.topChild.getLocationOnScreen(this.topChildLocation);
            this.bottomChild.getLocationOnScreen(this.bottomChildLocation);
            int i = this.topChildLocation[1] - this.myLocation[1];
            int j = this.bottomChildLocation[1] - this.myLocation[1];
            float f1 = paramMotionEvent.getX();
            float f2 = paramMotionEvent.getY();
            if (!checkTopChild(paramMotionEvent, i)) {
                if (!checkTopChild(paramMotionEvent, j)) {
                    paramMotionEvent.setLocation(f1, f2);
                    bool = super.onTouchEvent(paramMotionEvent);
                } else {
                    paramMotionEvent.setLocation(f1, f2 - j);
                    bool = this.bottomChild.onTouchEvent(paramMotionEvent);
                }
            } else {
                // paramMotionEvent.setLocation(f1, f2 - bool);
                paramMotionEvent.setLocation(f1, f2 - j);
                bool = this.topChild.onTouchEvent(paramMotionEvent);
            }
        } else if (!this.downInTopChild) {
            if (!this.downInBottomChild)
                bool = super.onTouchEvent(paramMotionEvent);
            else
                bool = this.bottomChild.onTouchEvent(paramMotionEvent);
        } else {
            bool = this.topChild.onTouchEvent(paramMotionEvent);
        }
        return bool;
    }

    public void setBottomChild(ViewGroup paramViewGroup) {
        this.bottomChild = paramViewGroup;
    }

    public void setTopChild(ViewGroup paramViewGroup) {
        this.topChild = paramViewGroup;
    }
}
