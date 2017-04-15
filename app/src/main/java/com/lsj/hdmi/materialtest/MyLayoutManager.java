package com.lsj.hdmi.materialtest;

import android.content.Context;
import android.graphics.Point;
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

            setMeasuredDimension(width,heigth);
            layoutDecorated(childView,getPaddingLeft()+parmas.leftMargin,
                    offectY+parmas.topMargin,
                    width+getPaddingLeft()+parmas.leftMargin,
                    offectY+heigth+parmas.topMargin);
            offectY+=heigth+parmas.topMargin+parmas.bottomMargin;
            totalHeight+=heigth+parmas.topMargin+parmas.bottomMargin;
        }

        Log.d(TAG, "onLayoutChildren: ================totalHeight"+totalHeight);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
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
                int bottomOffect;
                if(lastCompleteVisiablePosition>=getChildCount()-1){
                    Log.d(TAG, "scrollVerticallyBy: ------------------------realreachBottom");
                    bottomOffect=getVerticalSpace()-viewSpan+getPaddingBottom();
                }else {
                    Log.d(TAG, "scrollVerticallyBy: ------------------------reachBottomAsFarAsSoon");
                    bottomOffect=getVerticalSpace()-(viewSpan+getDecoratedMeasuredHeight(bottomView)+getPaddingBottom());
                }
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
//        Log.d(TAG, "scrollVerticallyBy: -----------------------dy"+dy);
//        offsetChildrenVertical(-dy);
//        return  dy;

    }

    private int getVerticalSpace(){
//        WindowManager windowManager= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        Point outSize=new Point();
//        windowManager.getDefaultDisplay().getSize(outSize);
//        return outSize.y;
        return getHeight()-getPaddingTop()-getPaddingBottom();
    }

//    private void fillView(RecyclerView.Recycler recycler,){
//
//    }
}
