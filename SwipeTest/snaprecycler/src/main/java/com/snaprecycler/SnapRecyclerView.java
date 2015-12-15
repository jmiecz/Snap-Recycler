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
import android.view.ViewGroup;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
public class SnapRecyclerView extends RecyclerView {
    private boolean swipeRightToLeft = true;
    private boolean swipeBottomToTop = true;
    private GestureDetectorCompat detectorCompat;
    private boolean firstLoad = true;
    private int orientation;

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
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof LinearLayoutManager)) {
            throw new RuntimeException("LayoutManger must be an instance of LinearLayoutManager");
        } else {
            orientation = ((LinearLayoutManager) layout).getOrientation();
        }

        super.setLayoutManager(layout);
    }

    private void setupView() {
        setHasFixedSize(false);
        setScrollListener();
        detectorCompat = new GestureDetectorCompat(getContext(), new SwipeListener());
    }

    private void setScrollListener() {
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

    public void setAutoSizeColumns(AutoSizeColumns autoSizeColumns) {
        orientation = autoSizeColumns.getOrientation();

        AutoSizeManger autoSizeManger = new AutoSizeManger(getContext(), orientation, false);
        autoSizeManger.setAutoSizeColumns(autoSizeColumns);
        setLayoutManager(autoSizeManger);
    }

    @Override
    public void onDraw(Canvas c) {
        setPadding();
        super.onDraw(c);
    }

    private void setPadding() {
        if (firstLoad) {
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

        if (scrollDistance != 0) {
            if (orientation == AutoSizeColumns.VERTICAL) {
                smoothScrollBy(0, scrollDistance);
            } else {
                smoothScrollBy(scrollDistance, 0);
            }
        }
    }

    private int getScrollDistance(View child) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        if (orientation == AutoSizeColumns.VERTICAL) {
            int childY = (int) child.getY();
            return childY - layoutParams.topMargin;
        } else {
            int childX = (int) child.getX();
            return childX - layoutParams.getMarginStart();
        }


    }

    private View getFirstView() {
        if (getChildCount() <= 0)
            return null;

        View child = getChildAt(0);
        ChildSizes childSize = new ChildSizes(child, orientation);
        boolean swipe = (orientation == AutoSizeColumns.VERTICAL ? swipeBottomToTop : swipeRightToLeft);

        if (swipe) {
            View lastChild = getChildAt(getChildCount() - 1);
            if (getChildAdapterPosition(lastChild) == getAdapter().getItemCount() - 1) {
                return getChildAt(1);
            }

            if (childSize.getStart() < 0 && childSize.getMiddle() < 0) {
                return getChildAt(1);
            } else {
                return child;
            }
        } else {
            if (childSize.getMiddle() < 0) {
                return getChildAt(1);
            } else {
                return child;
            }
        }
    }

    class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (orientation == AutoSizeColumns.VERTICAL) {
                if (e1.getY() > e2.getY()) {
                    swipeBottomToTop = true;
                } else {
                    swipeBottomToTop = false;
                }
            } else {
                if (e1.getX() > e2.getX()) {
                    swipeRightToLeft = true;
                } else {
                    swipeRightToLeft = false;
                }
            }

            return false;
        }

    }

}
