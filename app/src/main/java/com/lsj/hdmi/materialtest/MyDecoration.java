package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by hdmi on 17-4-4.
 */
public class MyDecoration extends RecyclerView.ItemDecoration {
    private static final int[] Attrs=new int[]{android.R.attr.listDivider};
    public static final int Horizontal_list= LinearLayout.HORIZONTAL;
    public static final int Vertical_list= LinearLayout.VERTICAL;
    public static final String TAG="MyDecoration";

    private int mOrientation;
    private Drawable mDivider;
    public MyDecoration(Context context,int orientation) {
        final TypedArray a = context.obtainStyledAttributes(Attrs);
        mDivider=a.getDrawable(0);
        Log.d(TAG, "MyDecoration: ---------------------"+mDivider);
        a.recycle();
        setOrientation(orientation);
    }

    private void setOrientation(int orientation){
        if (orientation!=Horizontal_list&&orientation!=Vertical_list){
            String errorMessage="there is not Horizontal_list or Vertical_list";
            throw new IllegalArgumentException(errorMessage);
        }
        mOrientation=orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation==Vertical_list){
            onDrawVertical(c,parent,state);
        }else if(mOrientation==Horizontal_list){
            onDrawHorizotal(c,parent,state);
        }

    }

    private void onDrawVertical(Canvas c, RecyclerView parent, RecyclerView.State state){
        int childCount=parent.getChildCount();
        Paint paint=new Paint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.setColor(parent.getContext().getColor(R.color.colorPrimaryDark));
        }else {
            paint.setColor(new Color().parseColor("black"));
        }
        for (int i=0;i<childCount;i++){
            if (parent.getAdapter().getItemViewType(i)==0) {
                //判断是否是contentView，是才加decoration
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams parmas = (RecyclerView.LayoutParams) child.getLayoutParams();
                int left = child.getLeft() + child.getPaddingLeft();
                int right = child.getRight() - child.getPaddingRight();
                int top = child.getBottom() + parmas.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                Rect diverRect = new Rect(left, top, right, bottom);
                c.drawRect(diverRect, paint);
            }
        }
    }

    private void onDrawHorizotal(Canvas c, RecyclerView parent, RecyclerView.State state){
        int childCount=parent.getChildCount();
        Paint paint=new Paint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.setColor(parent.getContext().getColor(R.color.colorPrimaryDark));
        }else {
            paint.setColor(new Color().parseColor("black"));
        }
        for (int i=0;i<childCount;i++){
            if (parent.getAdapter().getItemViewType(i)==0) {
                View child=parent.getChildAt(i);
                RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) child.getLayoutParams();
                int left=child.getLeft()-parmas.leftMargin;
                int right=left+mDivider.getIntrinsicHeight();
                int bottom=child.getHeight()+parmas.topMargin+parmas.bottomMargin;
                int top=parent.getPaddingTop();
                Rect diverRect = new Rect(left, top, right, bottom);
                c.drawRect(diverRect, paint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation==Vertical_list){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else if(mOrientation==Horizontal_list){
            outRect.set(mDivider.getIntrinsicHeight(),0,0,0);
        }

    }
}
