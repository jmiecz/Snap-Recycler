package com.snaprecycler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.DimenRes;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
public class SnapRecyclerView extends RecyclerView {
    private int childWidth = 0;
    private boolean swipeRightToLeft = true;
    private GestureDetectorCompat detectorCompat;

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
        detectorCompat = new GestureDetectorCompat(getContext(), new SwipeListener());
    }

    private void setScrollListener(){
        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollToView(getFirstView());
                }

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
        if(childWidth != 0){
            return;
        }
        if(getChildCount() > 1){
            View child = getChildAt(0);
            childWidth = child.getMeasuredWidth();

            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            childWidth += layoutParams.getMarginStart();
            childWidth += layoutParams.getMarginEnd();

            scrollToView(getFirstView());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        handleDirection(event);
        View targetView = getChildClosestToX((int) event.getX());

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (targetView != getFirstView()) {
                scrollToView(targetView);
                return true;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        detectorCompat.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        View targetView = getChildClosestToX((int) e.getX());

        if (targetView != getFirstView()) {
            return true;
        }

        return super.onInterceptTouchEvent(e);
    }

    float downXValue;
    private void handleDirection(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downXValue = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                swipeRightToLeft = downXValue > currentX;
        }

    }

    private void scrollToView(View child) {
        if (child == null)
            return;

        int scrollDistance = getScrollDistance(child);

        if (scrollDistance != 0)
            smoothScrollBy(scrollDistance, 0);
    }

    private int getScrollDistance(View child) {
        int childX = (int) child.getX();
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

        return childX - layoutParams.getMarginStart();
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

    class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        /**
         * Swipe min distance.
         */
        private static final int SWIPE_MIN_DISTANCE = 10;
        /**
         * Swipe max off path.
         */
        private static final int SWIPE_MAX_OFF_PATH = 250;
        /**
         * Swipe threshold velocity.
         */
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(e1.getX() > e2.getX()){
                Log.wtf("TEST", "right to left");
                swipeRightToLeft = true;
            }else{
                Log.wtf("TEST", "left to right");
                swipeRightToLeft = false;
            }

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

}
