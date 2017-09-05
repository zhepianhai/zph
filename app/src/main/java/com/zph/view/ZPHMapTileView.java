package com.zph.view;

import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
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

public class ZPHMapTileView extends ViewGroup {
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

    private float mStartX = 0;
    private float mStartY = 0;
    private float mDis=0;
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
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);
        mLayoutList=new ArrayList<>();
        mViewRootHeight=getHeight();
        mViewRootWidth=getWidth();
    }
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.i("TAG", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.i("TAG", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.i("TAG", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.i("TAG", "onScroll");

            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.i("TAG", "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.i("TAG", "onFling");
            return false;
        }
    };
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
                    ct = getHeight()-cHeight;
                    break;
                case 1:
                    cl = (getWidth() - cWidth) / 2;
                    ct = getHeight() - cHeight - 100;

                    break;
                case 2:
                    cl = getWidth() - cWidth - 10;
                    ct = getHeight() - cHeight - 100;
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
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i("TAG","AAAA");
//        boolean val = mGestureDetector.onTouchEvent(ev);
//        Log.i("TAG","AAAA="+val);
//        return val;




        if (checkIsRoadView(ev)) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mStartX=mLinearLayout1.getX();
                    mStartY=mLinearLayout1.getY();
                    mDis=ev.getY()-mStartY;
                    Log.i("TAG","Down");
               return true;
                case MotionEvent.ACTION_MOVE:

                    mCurrentScrollDirection =Direction.UP;
                    MovieRoadView(ev);
                 break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG","Up");
                    mDis=0;
                    if(ev.getY()<=mViewRootHeight/2){

                    }
                    else{
                        recoverView(ev);

                    }

                 break;
            }
        }
        else{
            mCurrentScrollDirection=Direction.NONE;
        }


        return super.onTouchEvent(ev);
    }

    private void recoverView(MotionEvent ev) {
        recoverLayout1(ev);
        recoverLayout2(ev);

    }

    private void recoverLayout1(MotionEvent ev) {
        int cl = 0, ct = 0, cr = 0, cb = 0;
        cl = 0;
        ct = getHeight() - mLinearLayout1.getMeasuredHeight();
        cr = cl + mLinearLayout1.getMeasuredWidth();
        cb = ct+mLinearLayout1.getMeasuredHeight();
        int locationY=(int)(mDis-ev.getY());
        int distance=(int)(ct-ev.getY());
        Log.i("TAG","Dis"+distance);
        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,locationY,
                Animation.ABSOLUTE,locationY+100);
        animation.setDuration(1200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout1.setAnimation(animation);
        animation.start();
        mLinearLayout1.layout(cl, ct, cr, cb);
    }

    private void recoverLayout2(MotionEvent ev) {
        int cl = 0, ct = 0, cr = 0, cb = 0;
        cl = (getWidth() - mLinearLayout2.getMeasuredWidth()) / 2;
        ct = getHeight() - mLinearLayout2.getMeasuredHeight() - 100;
        cr = cl + mLinearLayout2.getMeasuredWidth();
        cb = ct+mLinearLayout2.getMeasuredHeight();


        TranslateAnimation animation = new TranslateAnimation(0,0,0,ct);
        animation.setDuration(1200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout2.setAnimation(animation);
        animation.start();
        mLinearLayout2.layout(cl, ct, cr, cb);
    }


    private void MovieRoadView(MotionEvent ev) {
        if(checkIsLinearLayoutIsNull()){
            initLinearLayoutView();
        }
//        int getHeight() - mLinearLayout2.getHeight() - 10;
        // left top right bottom
        // left 不变，right不变
        int left,top,right,bottom;
        int left2,top2,right2,bottom2;
        left=0;
        left2=(getWidth() - mLinearLayout2.getMeasuredWidth()) / 2;
        right=left+mLinearLayout1.getMeasuredWidth();
        right2=left2+mLinearLayout2.getMeasuredWidth();
        top=(int)ev.getY()-(int)mDis;
        top2=top-100;
        bottom=top+mLinearLayout1.getMeasuredHeight();
        bottom2=bottom-100;
        mLinearLayout1.layout(left,top, right,bottom);
        mLinearLayout2.layout(left2,top2, right2,bottom2);

    }

    private boolean checkIsLinearLayoutIsNull() {
        if(null==mLinearLayout1||null==mLinearLayout2||null==mLinearLayout3||null==mLinearLayout4){
            return true;
        }
        return false;
    }

    public boolean checkIsRoadView(MotionEvent ev) {
        if (null == mLinearLayout1 ||null==mLinearLayout2) {
            mLinearLayout1 = (LinearLayout) getChildAt(0);
            mLinearLayout2 = (LinearLayout) getChildAt(1);
        }
        float x, y, ex, ey;
        x = mLinearLayout1.getX();
        y = mLinearLayout1.getY();
        ex = ev.getX();
        ey = ev.getY();
        if (ex > x && ex - x < mLinearLayout1.getMeasuredWidth() && ey > y && ey - y < mLinearLayout1.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

}