package com.futurethought.snaprecycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by josh.mieczkowski on 12/7/2015.
 */
class AutoSizeManger extends LinearLayoutManager {
    private AutoSizeColumns autoSizeColumns;
    private int childSize = Integer.MAX_VALUE;

    public AutoSizeManger(Context context) {
        super(context);
    }

    public AutoSizeManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoSizeManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAutoSizeColumns(AutoSizeColumns autoSizeColumns) {
        this.autoSizeColumns = autoSizeColumns;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (autoSizeColumns == null) {
            return;
        }

        if (childSize == Integer.MAX_VALUE) {
            int sizeWithoutPadding = getSizeWithoutPadding();
            childSize = sizeWithoutPadding / autoSizeColumns.getVisibleItemCount();

            if (autoSizeColumns.getPercentToShowOfOffViews() > 0f) {
                int offView = (int) (childSize * autoSizeColumns.getPercentToShowOfOffViews()) / autoSizeColumns.getVisibleItemCount();
                childSize -= offView;
            }
        }

        if (!state.isPreLayout()) {
            for (int index = 0; index < getChildCount(); index++) {
                View child = getChildAt(index);

                if (child != null) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

                    if (autoSizeColumns.getOrientation() == AutoSizeColumns.VERTICAL) {
                        if (layoutParams.height != childSize) {
                            layoutParams.height = childSize;
                        }
                    } else {
                        if (layoutParams.width != childSize) {
                            layoutParams.width = childSize;
                        }
                    }
                    layoutParams.setMargins(autoSizeColumns.getPaddingLeft(), autoSizeColumns.getPaddingTop(), autoSizeColumns.getPaddingRight(), autoSizeColumns.getPaddingBottom());
                }
            }
        }
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);

    }

    private int getSizeWithoutPadding() {
        if (autoSizeColumns.getOrientation() == AutoSizeColumns.VERTICAL) {
            return getHeight() - (autoSizeColumns.getVisibleItemCount() * autoSizeColumns.getPaddingTop()
                    + autoSizeColumns.getVisibleItemCount() * autoSizeColumns.getPaddingBottom());
        } else {
            return getWidth() - (autoSizeColumns.getVisibleItemCount() * autoSizeColumns.getPaddingLeft()
                    + autoSizeColumns.getVisibleItemCount() * autoSizeColumns.getPaddingRight());
        }
    }


}
