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
    private boolean hasAdapterSet = false;

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

    @Override
    public void setAdapter(Adapter adapter) {
        if(!hasAdapterSet){
            throw new RuntimeException("setColumnHandler was not called");
        }

        super.setAdapter(adapter);
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

    public void setColumnHandler(ColumnHandler columnHandler){
        AutoSizeManger autoSizeManger = new AutoSizeManger(getContext(), LinearLayoutManager.HORIZONTAL, false);
        autoSizeManger.setColumnHandler(columnHandler);
        setLayoutManager(autoSizeManger);

        hasAdapterSet = true;
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
            View lastChild = getChildAt(getChildCount() - 1);
            if(getChildAdapterPosition(lastChild) == getAdapter().getItemCount() - 1){
                return getChildAt(1);
            }

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
