package com.snaprecycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
class AutoSizeManger extends LinearLayoutManager {
    private int visiableItemCount = 1;
    int paddingLeft;
    int paddingBottom;
    int paddingRight;
    int paddingTop;

    public AutoSizeManger(Context context) {
        super(context);
    }

    public AutoSizeManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoSizeManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVisibleItemCount(int visibleItemCount){
        this.visiableItemCount = visibleItemCount;
    }

    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int widthWithoutPadding = getWidth() - (visiableItemCount * paddingLeft + visiableItemCount * paddingRight);
        int childWidth = widthWithoutPadding / visiableItemCount;
        for(int index = 0; index < state.getItemCount(); index++){
            View child = getChildAt(index);

            if(child != null) {
                child.getLayoutParams().width = childWidth;

                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)child.getLayoutParams();
                marginLayoutParams.setMargins(paddingLeft, paddingTop, paddingRight, paddingBottom);
            }
        }
    }
}
