package com.lsj.hdmi.materialtest.bean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by hdmi on 17-4-7.
 */
public class MyLayoutManager extends RecyclerView.LayoutManager {
    public MyLayoutManager(Context context, AttributeSet attr) {
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int childCount=state.getItemCount();
    }

}
