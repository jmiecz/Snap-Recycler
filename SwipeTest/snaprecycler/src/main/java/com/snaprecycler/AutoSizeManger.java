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
    private ColumnHandler columnHandler;

    public AutoSizeManger(Context context) {
        super(context);
    }

    public AutoSizeManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoSizeManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setColumnHandler(ColumnHandler columnHandler) {
        this.columnHandler = columnHandler;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int widthWithoutPadding = getWidth() - (columnHandler.getVisibleItemCount() * columnHandler.getPaddingLeft()
                + columnHandler.getVisibleItemCount() * columnHandler.getPaddingRight());
        int childWidth = widthWithoutPadding / columnHandler.getVisibleItemCount();
        if(columnHandler.getPercentToShowOfOffViews() > 0f){
            int offView = (int)(childWidth * columnHandler.getPercentToShowOfOffViews()) / columnHandler.getVisibleItemCount();
            childWidth -= offView;
        }

        for(int index = 0; index < state.getItemCount(); index++){
            View child = getChildAt(index);

            if(child != null) {
                child.getLayoutParams().width = childWidth;

                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)child.getLayoutParams();
                marginLayoutParams.setMargins(columnHandler.getPaddingLeft(), columnHandler.getPaddingTop(), columnHandler.getPaddingRight(), columnHandler.getPaddingBottom());
            }
        }
    }
}
