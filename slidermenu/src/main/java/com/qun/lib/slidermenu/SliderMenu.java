package com.qun.lib.slidermenu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class SliderMenu extends ViewGroup {

    private View mSliderMenu;
    private View mMainContent;
    private Scroller mScroller;
    private SliderMenuState mState = SliderMenuState.CLOSE;

    public SliderMenuState getState() {
        return mState;
    }

    public enum SliderMenuState {
        OPEN, CLOSE
    }

    public SliderMenu(Context context) {
        this(context, null);
    }

    public SliderMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mSliderMenu = getChildAt(0);
        mMainContent = getChildAt(1);
        setMeasuredDimension(mMainContent.getMeasuredWidth(),
                mSliderMenu.getMeasuredHeight() >= mMainContent.getMeasuredHeight() ?
                        mSliderMenu.getMeasuredHeight() : mMainContent.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mSliderMenu.layout(-mSliderMenu.getMeasuredWidth(), 0, 0, b);
        mMainContent.layout(l, t, r, b);
    }

    private int mLastX;
    private int mLastY;
    private boolean isCanSlider = true;

    public void setCanSlider(boolean canSlider) {
        isCanSlider = canSlider;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isCanSlider) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int curX = (int) ev.getX();
                int rowX = (int) ev.getRawX();
                if (Math.abs(curX - mLastX) > 20) {
                    if (mState == SliderMenuState.CLOSE) {
                        if (rowX < 300) {
                            return true;
                        }
                    } else {
                        if (rowX > 500) {
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanSlider) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int curX = (int) event.getX();
                int curY = (int) event.getY();
                int dx = curX - mLastX;

                if (getScrollX() - dx <= -mSliderMenu.getWidth()) {
                    scrollTo(-mSliderMenu.getWidth(), 0);
                } else if (getScrollX() - dx >= 0) {
                    scrollTo(0, 0);
                } else {
                    scrollBy(-dx, 0);
                }
                mLastX = curX;
                mLastY = curY;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() <= -mSliderMenu.getWidth() / 3) {
                    mScroller.startScroll(getScrollX(), 0, -(mSliderMenu.getWidth() - Math.abs(getScrollX())), 0);
                    invalidate();
                    mState = SliderMenuState.OPEN;
                } else {
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    invalidate();
                    mState = SliderMenuState.CLOSE;
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void changedState(SliderMenuState state) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        if (mState == SliderMenuState.OPEN) {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        } else {
            mScroller.startScroll(getScrollX(), 0, -(mSliderMenu.getWidth() - Math.abs(getScrollX())), 0);
        }
        invalidate();
        mState = state;
    }
}
