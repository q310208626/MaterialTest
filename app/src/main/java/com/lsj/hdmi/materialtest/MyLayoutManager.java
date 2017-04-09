package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.icu.util.MeasureUnit;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hdmi on 17-4-7.
 */
public class MyLayoutManager extends RecyclerView.LayoutManager {
    public static String TAG="MyLayoutManager";
    public static int currentItemCount =0;//应用在onMeasure中

    public MyLayoutManager(Context context, AttributeSet attr) {
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //super.onLayoutChildren(recycler, state);
        int childCount=state.getItemCount();
        detachAndScrapAttachedViews(recycler);
        int offectY=getPaddingTop();
        for (int i=0;i<childCount;i++){
            View childView=recycler.getViewForPosition(i);
            RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) childView.getLayoutParams();
            addView(childView);
            measureChild(childView,0,0);
            int width=getDecoratedMeasuredWidth(childView);
            Log.d(TAG, "onLayoutChildren: -----------------------"+i+":"+width);
            Log.d(TAG, "onLayoutChildren: -----------------------"+getWidth());
            int heigth=getDecoratedMeasuredHeight(childView);

            setMeasuredDimension(width,heigth);
            layoutDecorated(childView,getPaddingLeft()+parmas.leftMargin,
                    offectY+parmas.topMargin,
                    width+getPaddingLeft()+parmas.leftMargin,
                    offectY+heigth+parmas.topMargin);
            offectY+=heigth+parmas.topMargin+parmas.bottomMargin;
        }
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(getWidth(),getWidthMode(),
                getPaddingLeft()+getPaddingRight(), parmas.width,true);
        int childHeightMeasureSpec = getChildMeasureSpec(getWidth(), getHeight(),
                getPaddingTop()+getPaddingBottom(), parmas.height,false);

        if(parmas.width== RecyclerView.LayoutParams.MATCH_PARENT){
            childWidthMeasureSpec = getChildMeasureSpec(getWidth(),getWidthMode(),
                    getPaddingLeft()+getPaddingRight()+parmas.leftMargin+parmas.rightMargin, parmas.width,false);
            Log.d(TAG, "measureChild: ---------------------------leftmargin"+parmas.leftMargin);
        }
        child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
