package com.snaprecycler;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
public class SnapRecyclerView extends RecyclerView {
    private boolean swipeRightToLeft = true;
    private GestureDetectorCompat detectorCompat;
    private boolean firstLoad = true;

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
        setLayoutManager(autoSizeManger);
    }

    @Override
    public void onDraw(Canvas c) {
        setPadding();
        super.onDraw(c);

    }

    private void setPadding(){
        if(firstLoad){
            firstLoad = false;
            scrollToView(getFirstView());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View targetView = getFirstView();

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
        if (getChildCount() <= 0)
            return null;

        View child = getChildAt(0);
        ChildSizes childSize = new ChildSizes(child);

        if(swipeRightToLeft) {
            if (childSize.getStart() < 0 && childSize.getMiddle() < 0) {
                return getChildAt(1);
            } else {
                return child;
            }
        }else{
            if(childSize.getMiddle() < 0){
                return getChildAt(1);
            }else{
                return child;
            }
        }
    }

    class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(e1.getX() > e2.getX()){
                swipeRightToLeft = true;
            }else{
                swipeRightToLeft = false;
            }

            return false;
        }

    }

}
