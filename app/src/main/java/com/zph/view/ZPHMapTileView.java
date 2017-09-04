package com.zph.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Toast;

import com.zph.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zph on 2017/9/1.
 *
 * @childView1 ：searchview,postions params in top
 * @childView2 ：road line ,postions params in bottom and center
 * @childView3 : FloatingActionButton view  Intent UserActivty pager. postions params in bottom|right
 * @childView4 : Up Gesture Loading new pager,postions params in buttom ,below of @childView2,init visiable is gone
 * @deprecated
 */

public class ZPHMapTileView extends ViewGroup implements GestureDetector.OnGestureListener, View.OnTouchListener {
    private final String TAG = "ZPHMapTileView";


    private enum Direction {
        NONE, UP, DOWN, VERTICAL, ERR
    }

    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private PointF mCurrentOrigin = new PointF(0f, 0f);
    private Direction mCurrentScrollDirection = Direction.NONE;

    private Context mContext;
    private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4;
    private List<LinearLayout> mLayoutList;

    private boolean mIsInTop;
    private int mViewRootHeight;
    private int mViewRootWidth;


    public ZPHMapTileView(Context context) {
        this(context, null);
    }

    public ZPHMapTileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ZPHMapTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
//        this.setBackgroundColor(Color.WHITE);
        mLayoutList=new ArrayList<>();
        mViewRootHeight=getHeight();
        mViewRootWidth=getWidth();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int buttom) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
//        MarginLayoutParams cParams = null;


        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
//            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i) {
                case 0:
                    cl = 0;
                    ct = 0;
                    break;
                case 1:
                    cl = (getWidth() - cWidth) / 2;
                    ct = getHeight() - cHeight - 10;

                    break;
                case 2:
                    cl = getWidth() - cWidth - 10;
                    ct = getHeight() - cHeight - 10;
                    break;
                case 3:
                    cl = getWidth();
                    ct = getHeight();
                    break;

            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initLinearLayoutView();
    }

    private void initLinearLayoutView() {
        mLayoutList.clear();
        mLinearLayout1 = (LinearLayout) getChildAt(0);
        mLinearLayout2 = (LinearLayout) getChildAt(1);
        mLinearLayout3 = (LinearLayout) getChildAt(2);
        mLinearLayout4 = (LinearLayout) getChildAt(3);
        mLayoutList.add(0,mLinearLayout1);
        mLayoutList.add(1,mLinearLayout2);
        mLayoutList.add(2,mLinearLayout3);
        mLayoutList.add(3,mLinearLayout4);
        mLinearLayout2.setOnTouchListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



       /* int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG,"widthMeasureSpec:"+widthMeasureSpec);
        Log.i(TAG,"heightMeasureSpec:"+heightMeasureSpec);
        measureChildren(widthMode, heightMode);
        int width = 0;
        int height = 0;
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
//        MarginLayoutParams cParams = null;
        int lHeight = 0;
        int rHeight = 0;
        int tWidth = 0;
        int bWidth = 0;
        Log.i(TAG,"view的个数："+cCount);

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
//            cParams = (MarginLayoutParams) childView.getLayoutParams();
            tWidth += cWidth ;
            lHeight += cHeight ;
            Log.i(TAG,"第"+i+"个的宽高："+tWidth+"-"+cWidth+"-"+lHeight+"-"+cHeight);
        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);*/
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN||ev.getAction()== MotionEvent.ACTION_MOVE) {
            if (checkIsRoadView(ev)) {
                mCurrentScrollDirection =Direction.UP;
                MovieRoadView(ev);
            }
            mCurrentScrollDirection=Direction.NONE;
        }
        return onTouchEvent(ev);
    }

    private void MovieRoadView(MotionEvent ev) {
        if(checkIsLinearLayoutIsNull()){
            initLinearLayoutView();
        }
        int eventX = (int) ev.getX();
        int eventY = (int) ev.getY();
//        int getHeight() - mLinearLayout2.getHeight() - 10;
        // left top right bottom
        // left 不变，right不变
        int left,top,right,bottom;
        left=(getWidth() - mLinearLayout2.getMeasuredWidth()) / 2;
        right=left+mLinearLayout2.getMeasuredWidth();
        bottom=mViewRootHeight-mLinearLayout2.getMeasuredHeight()-10;
        top=bottom-mLinearLayout2.getMeasuredHeight();
        mLinearLayout2.layout(left,eventY-mLinearLayout2.getMeasuredHeight(), right,eventY);

    }

    private boolean checkIsLinearLayoutIsNull() {
        if(null==mLinearLayout1||null==mLinearLayout2||null==mLinearLayout3||null==mLinearLayout4){
            return true;
        }
        return false;
    }

    public boolean checkIsRoadView(MotionEvent ev) {
        if (null == mLinearLayout2) {
            mLinearLayout2 = (LinearLayout) getChildAt(1);
        }
        float x, y, ex, ey;
        x = mLinearLayout2.getX();
        y = mLinearLayout2.getY();
        ex = ev.getX();
        ey = ev.getY();
        if (ex > x && ex - x < mLinearLayout2.getMeasuredWidth() && ey > y && ey - y < mLinearLayout2.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

}