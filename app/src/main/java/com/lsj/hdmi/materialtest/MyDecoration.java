package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by hdmi on 17-4-4.
 */
public class MyDecoration extends RecyclerView.ItemDecoration {
    private static final int[] Attrs=new int[]{android.R.attr.divider};
    public static final int Horizontal_list= LinearLayout.HORIZONTAL;
    public static final int Vertical_list= LinearLayout.VERTICAL;

    private int mOrientation;
    private Drawable mDivider;
    public MyDecoration(Context context,int orientation) {
        final TypedArray a = context.obtainStyledAttributes(Attrs);
        mDivider=a.getDrawable(0);
        a.recycle();

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
