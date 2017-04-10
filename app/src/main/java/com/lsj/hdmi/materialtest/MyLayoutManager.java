package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.graphics.Point;
import android.icu.util.MeasureUnit;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by hdmi on 17-4-7.
 */
public class MyLayoutManager extends RecyclerView.LayoutManager {
    public static String TAG="MyLayoutManager";
    public static int currentItemCount =0;//应用在onMeasure中
    public Context mContext;

    public MyLayoutManager(Context context, AttributeSet attr) {
        mContext=context;
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
        final View topView=recycler.getViewForPosition(0);
        final View bottomView=recycler.getViewForPosition(state.getItemCount()-1);
        Log.d(TAG, "scrollVerticallyBy: ----------------------topView"+topView);
        Log.d(TAG, "scrollVerticallyBy: ----------------------bottomViewPosition"+(state.getItemCount()-1));
        Log.d(TAG, "scrollVerticallyBy: ----------------------bottomView"+bottomView);
        int topLine=getDecoratedTop(topView);
        int bottomLine=getDecoratedBottom(bottomView);
        int viewSpan=bottomLine-topLine;
        int verticalSpace=getVerticalSpace();
        Log.d(TAG, "scrollVerticallyBy: ----------------------topLine"+topLine);
        Log.d(TAG, "scrollVerticallyBy: ----------------------bottomLine"+bottomLine);
        Log.d(TAG, "scrollVerticallyBy: ----------------------viewSpan"+viewSpan);
        Log.d(TAG, "scrollVerticallyBy: ----------------------verticalSpace"+verticalSpace);
        if (viewSpan<verticalSpace){
            return 0;
        }

        int delta;
        boolean reachtopBound=getPosition(getChildAt(0))==0;
        Log.d(TAG, "scrollVerticallyBy: ---------------------ViewChildLast"+getPosition(getChildAt(getChildCount()-1)));
        Log.d(TAG, "scrollVerticallyBy:--------------------------reachTop"+reachtopBound);
        int childViewCount=getChildCount();
        boolean reachBottomBound=getPosition(getChildAt(childViewCount-1))==state.getItemCount()-1;
        if (dy > 0) {//scrollDown
            //can see the last item
            if (reachBottomBound){
                int bottomOffect;
                if(getPosition(getChildAt(getChildCount()-1))>=state.getItemCount()-1){
                    bottomOffect=getVerticalSpace()-getDecoratedBottom(bottomView)+getPaddingBottom();
                }else {
                    bottomOffect=getVerticalSpace()-(getDecoratedBottom(bottomView)+getDecoratedMeasuredHeight(bottomView)+getPaddingBottom());
                }
                delta=Math.max(-dy,bottomOffect);
            }else{
                delta=-dy;
            }
        }else{
            if (reachtopBound){
                int topOffset=getPaddingTop()-getDecoratedTop(topView);
                delta=Math.max(-dy,topOffset);
            }else{
                delta=-dy;
            }
        }

        offsetChildrenVertical(delta);



        return 0;
    }

    private int getVerticalSpace(){
        WindowManager windowManager= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point outSize=new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        return outSize.y;
//        return getHeight();
    }
}
