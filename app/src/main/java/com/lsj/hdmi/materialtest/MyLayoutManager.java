package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.util.MeasureUnit;
import android.os.Debug;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by hdmi on 17-4-7.
 */
public class MyLayoutManager extends LinearLayoutManager {
    public static String TAG="MyLayoutManager";
    public static int currentItemCount =0;//应用在onMeasure中
    public Context mContext;
    private int verticalOffset=0;
    private int totalHeight;
    SparseArray<View> viewCache = new SparseArray<View>(getChildCount());


    public MyLayoutManager(Context context) {
        super(context);
        this.mContext = context;
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
        totalHeight=getPaddingTop();
        for (int i=0;i<childCount;i++){
            View childView=recycler.getViewForPosition(i);
            RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) childView.getLayoutParams();
            addView(childView);
            measureChild(childView,0,0);
            int width=getDecoratedMeasuredWidth(childView);
            int heigth=getDecoratedMeasuredHeight(childView);
            Log.d(TAG, "onLayoutChildren: ------------------------getDecoratedMeasuredHeight"+heigth);
            setMeasuredDimension(width,heigth);
            int bottom;
            if (i<childCount-1){
                bottom=offectY+heigth+parmas.topMargin;
            }else {
                bottom=offectY+heigth+parmas.topMargin+parmas.bottomMargin;
            }
                layoutDecorated(childView,getPaddingLeft()+parmas.leftMargin,
                        offectY+parmas.topMargin,
                        width+getPaddingLeft()+parmas.leftMargin,
                        bottom);
            Log.d(TAG, "onLayoutChildren: ----------------------------getBottomDecorationHeight");
            offectY+=(heigth+parmas.topMargin+parmas.bottomMargin+getBottomDecorationHeight(childView));
            totalHeight+=heigth+parmas.topMargin+parmas.bottomMargin;
        }

        Log.d(TAG, "onLayoutChildren: ================totalHeight"+totalHeight);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        super.measureChild(child,widthUsed,heightUsed);
        RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(getWidth(),getWidthMode(),
                getPaddingLeft()+getPaddingRight(), parmas.width,true);
        int childHeightMeasureSpec = getChildMeasureSpec(getWidth(), getHeight(),
                getPaddingTop()+getPaddingBottom(), parmas.height,true);

        if(parmas.width== RecyclerView.LayoutParams.MATCH_PARENT){
            childWidthMeasureSpec = getChildMeasureSpec(getWidth(),getWidthMode(),
                    getPaddingLeft()+getPaddingRight()+parmas.leftMargin+parmas.rightMargin, parmas.width,true);
        }
        child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        View topView=getChildAt(0);
        View bottomView=getChildAt(getChildCount()-1);
        int topLine=getDecoratedTop(topView);
        int bottomLine=getDecoratedBottom(bottomView);
        int viewSpan=bottomLine-topLine;
        Log.d(TAG, "scrollVerticallyBy: ---------------------viewspan"+viewSpan);
        int verticalSpace=getVerticalSpace();
        Log.d(TAG, "scrollVerticallyBy: ---------------------verticalSpace"+verticalSpace);
        if (viewSpan<verticalSpace){
            Log.d(TAG, "scrollVerticallyBy: ----------------------viewSpan<verticalSpace");
            return 0;
        }
        LinearLayoutManager layoutManager= (LinearLayoutManager)this;
        int firstVisiablePosition=layoutManager.findFirstVisibleItemPosition();
        int lastVisiablePosition=layoutManager.findLastVisibleItemPosition();
        int lastCompleteVisiablePosition=layoutManager.findLastCompletelyVisibleItemPosition();
        int firstCompleteVisiablePosition=layoutManager.findFirstCompletelyVisibleItemPosition();
        Log.d(TAG, "scrollVerticallyBy: ---------------------ViewChildFirst"+firstVisiablePosition);
        Log.d(TAG, "scrollVerticallyBy: ---------------------ViewChildLast"+lastVisiablePosition);
        int childViewCount=getChildCount();
        boolean reachtopBound=firstVisiablePosition==0;
        boolean reachBottomBound=lastVisiablePosition==getChildCount()-1;
        int delta=0;
        if (dy > 0) {//scrollDown
            //can see the last item
            Log.d(TAG, "scrollVerticallyBy: -------------------------DY>0");
            if (reachBottomBound){
                Log.d(TAG, "scrollVerticallyBy: ------------------------reachBottom");
                int bottomOffect = 0;
                RecyclerView.LayoutParams parmas= (RecyclerView.LayoutParams) bottomView.getLayoutParams();
                if(lastCompleteVisiablePosition>=getChildCount()-1){
                    Log.d(TAG, "scrollVerticallyBy: ------------------------realreachBottom");
                    bottomOffect=getVerticalSpace()-getDecoratedBottom(bottomView)-parmas.bottomMargin;
                    Log.d(TAG, "scrollVerticallyBy: ---------------------decorationBottom"+getDecoratedBottom(bottomView));
                    Log.d(TAG, "scrollVerticallyBy: ---------------------VerticalSpace"+getVerticalSpace());
                    Log.d(TAG, "scrollVerticallyBy: ---------------------paddingbottom"+getPaddingBottom());
                }
                else {
                    Log.d(TAG, "scrollVerticallyBy: ------------------------reachBottomAsFarAsSoon");
                    Log.d(TAG, "scrollVerticallyBy: ---------------------decorationBottom"+getDecoratedBottom(bottomView));
                    Log.d(TAG, "scrollVerticallyBy: ---------------------VerticalSpace"+getVerticalSpace());
                    Log.d(TAG, "scrollVerticallyBy: ------------------------------getDecoratedMeasuredHeight(bottomView)"+getDecoratedMeasuredHeight(bottomView));
                    bottomOffect=getVerticalSpace()-(getDecoratedBottom(bottomView)+getDecoratedMeasuredHeight(bottomView)+parmas.bottomMargin);
                }
                if (-dy>bottomOffect){
                    Log.d(TAG, "scrollVerticallyBy: -----------------(-dy)>bottomoffset");
                }else {
                    Log.d(TAG, "scrollVerticallyBy: -----------------(-dy)<bottomoffset");
                }
                Log.d(TAG, "scrollVerticallyBy: ------------------------dy"+(-dy));
                Log.d(TAG, "scrollVerticallyBy: ------------------------bottomoffset"+bottomOffect);
                delta=Math.max(-dy,bottomOffect);
            }else{
                delta=-dy;
                Log.d(TAG, "scrollVerticallyBy: ----------------------noReachBottomDy"+delta);
            }
        }else{
            Log.d(TAG, "scrollVerticallyBy: -----------------------Dy<0");
            if (reachtopBound){
                Log.d(TAG, "scrollVerticallyBy: ---------------reachTop");
                int topOffset=getPaddingTop()-getDecoratedTop(topView);
                delta=Math.min(-dy,topOffset);
            }else{
                Log.d(TAG, "scrollVerticallyBy: ---------------noReachTop");
                delta=-dy;
            }
        }
        Log.d(TAG, "scrollVerticallyBy: ----------------------offectDelta"+delta);
        offsetChildrenVertical(delta);
        return -delta;
    }

    private int getVerticalSpace(){
        return getHeight()-getPaddingTop()-getPaddingBottom();
    }

}
