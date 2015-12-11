package com.snaprecycler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.DimenRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
public class SnapRecyclerView extends RecyclerView {
    private final static int MINIMUM_SCROLL_EVENT_OFFSET_MS = 20;

    private boolean viewScrolling = false;
    private boolean userScrolling = false;
    private int currentScrollState;

    private long lastScrollTime = 0;

    private int childWidth = 0;
    private int padding = 0;

    public SnapRecyclerView(Context context) {
        super(context);
        setupView();
    }

    public SnapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public SnapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    private void setupView(){
        setHasFixedSize(false);
        setScrollListener();
    }

    private void setScrollListener(){
        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    if (!viewScrolling) {
                        userScrolling = true;
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (userScrolling) {
                        scrollToView(getFirstView());
                    }

                    userScrolling = false;
                    viewScrolling = false;
                } else if (newState == SCROLL_STATE_FLING) {
                    viewScrolling = true;
                }

                currentScrollState = newState;
            }
        };

        addOnScrollListener(scrollListener);
    }

    public void setVisibleItemCount(int visibleItemCount){
        setVisibleItemCount(visibleItemCount, 0);
    }

    public void setVisibleItemCount(int visibleItemCount, int padding){
        setVisibleItemCount(visibleItemCount, padding, padding, padding, padding);
    }

    public void setVisibleItemCount(int visibleItemCount, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        AutoSizeManger autoSizeManger = new AutoSizeManger(getContext(), LinearLayoutManager.HORIZONTAL, false);
        autoSizeManger.setVisibleItemCount(visibleItemCount);
        autoSizeManger.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        //autoSizeManger.setSmoothScrollbarEnabled(true);
        setLayoutManager(autoSizeManger);
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        setPadding();
    }

    private void setPadding(){
        if(padding != 0){
            return;
        }

        if(getChildCount() <= 1){
            padding = 0;
        }else{
            View child = getChildAt(0);
            childWidth = child.getMeasuredWidth();

            int paddingLeft = child.getPaddingLeft();
            int paddingRight = child.getPaddingRight();
            padding = paddingLeft + paddingRight;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        long currentTime = System.currentTimeMillis();

        if (viewScrolling && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if ((currentTime - lastScrollTime) < MINIMUM_SCROLL_EVENT_OFFSET_MS) {
                userScrolling = true;
            }
        }

        lastScrollTime = currentTime;

        View targetView = getChildClosestToX((int) event.getX());

        if (!userScrolling) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (targetView != getFirstView()) {
                    scrollToView(targetView);
                    return true;
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        View targetView = getChildClosestToX((int) e.getX());

        if (targetView != getFirstView()) {
            return true;
        }

        return super.onInterceptTouchEvent(e);
    }

    private void scrollToView(View child) {
        if (child == null)
            return;

        stopScroll();

        int scrollDistance = getScrollDistance(child);

        if (scrollDistance != 0)
            smoothScrollBy(scrollDistance, 0);
    }

    private int getScrollDistance(View child) {
        int childX = (int) child.getX();
        int currentX = (int) getX();

        return childX + padding - currentX;
    }

    private View getFirstView() {
        return getChildClosestToX(childWidth);
    }

    private View getChildClosestToX(int x) {
        if (getChildCount() <= 0)
            return null;

        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);

            int childStart = (int) child.getX();
            int childMiddle = childStart + (childWidth / 2);
            int childEnd = childStart + childWidth;


            if(x >= childStart && x < childMiddle){
                return getChildAt(index - 1);
            }else if(x >= childMiddle && x < childEnd){
                return child;
            }
        }

        return null;
    }

}
