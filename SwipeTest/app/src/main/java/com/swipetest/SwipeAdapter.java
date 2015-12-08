package com.swipetest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by josh.mieczkowski on 12/4/2015.
 */
public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeView>{

    class SwipeView extends RecyclerView.ViewHolder{

        public SwipeView(View itemView) {
            super(itemView);
        }
    }

    @Override
    public SwipeView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.swipe_card, viewGroup, false);
        return new SwipeView(view);
    }

    @Override
    public void onBindViewHolder(SwipeView swipeView, int i) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }


}
