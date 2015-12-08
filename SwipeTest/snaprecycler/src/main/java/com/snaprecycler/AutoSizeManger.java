package com.snaprecycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
class AutoSizeManger extends LinearLayoutManager {
    private int visiableItemCount = 1;

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

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int childWidth = getWidth() / visiableItemCount;
        try {
            for(int index = 0; index < state.getItemCount(); index++){
                getChildAt(index).getLayoutParams().width = childWidth;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
