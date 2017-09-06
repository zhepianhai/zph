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
import android.widget.RelativeLayout;
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
    private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3;
    private RelativeLayout mLinearLayout4;
    private List<LinearLayout> mLayoutList;

    private boolean mIsInTop;
    private int mViewRootHeight;
    private int mViewRootWidth;

    private float mStartX = 0;
    private float mStartY = 0;
    private float mDis = 0;

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
        mLayoutList = new ArrayList<>();
        mViewRootHeight = getHeight();
        mViewRootWidth = getWidth();
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            if (checkIsRoadView(motionEvent) || mCurrentScrollDirection == Direction.UP) {
                mCurrentScrollDirection = Direction.UP;
                mStartX = mLinearLayout1.getX();
                mStartY = mLinearLayout1.getY();
                mDis = motionEvent.getY() - mStartY;
            } else
                mCurrentScrollDirection = Direction.NONE;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            mCurrentScrollDirection = Direction.NONE;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEventStar, MotionEvent motionEventEnd, float v, float v1) {
            if (checkIsRoadView(motionEventEnd) || mCurrentScrollDirection == Direction.UP) {
                mCurrentScrollDirection = Direction.UP;
                MovieRoadView(motionEventEnd);
            } else
                mCurrentScrollDirection = Direction.NONE;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.i("TAG", "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent motionEventStar, MotionEvent motionEventEnd, float v, float v1) {
            if (checkIsRoadView(motionEventEnd) || mCurrentScrollDirection == Direction.UP) {
                mViewRootHeight = mViewRootHeight == 0 ? getHeight() : mViewRootHeight;
                Log.i("TAG", "Y" + motionEventEnd.getY());
                Log.i("TAG", "mViewRootHeight" + mViewRootHeight / 3);
                if (motionEventEnd.getY() <= mViewRootHeight / 3) {
                    moveToTop(motionEventEnd);
                } else {
                    recoverView(motionEventEnd);
                }
            }
            mDis = 0;
            mCurrentScrollDirection = Direction.NONE;
            return true;
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
                    ct = getHeight() - cHeight;
                    break;
                case 1:
                    cl = (getWidth() - cWidth) / 2;
                    ct = getHeight() - cHeight - 120;

                    break;
                case 2:
                    cl = getWidth() - cWidth - 10;
                    ct = getHeight() - cHeight - 120;
                    break;
                case 3:
                    cl = 0;
                    ct = getHeight();
                    break;

            }
            cr = getWidth();
            cb = cHeight+ ct;
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
        mLinearLayout4 = (RelativeLayout) getChildAt(3);
        mLayoutList.add(0, mLinearLayout1);
        mLayoutList.add(1, mLinearLayout2);
        mLayoutList.add(2, mLinearLayout3);
        mLinearLayout1.setVisibility(VISIBLE);
        mLinearLayout2.setVisibility(VISIBLE);

//        mLayoutList.add(3, mLinearLayout4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCurrentScrollDirection == Direction.ERR) {
            return false;
        }
        return mGestureDetector.onTouchEvent(ev);
    }

    private void recoverView(MotionEvent ev) {
        recoverLayout1(ev);
        recoverLayout2(ev);
        recoverLayout4(ev);
    }

    private void recoverLayout4(MotionEvent ev) {
        final int cl = 0;
        final int ct = getHeight();
        final int cr = getWidth();
        final int cb = ct + getHeight();
        int distance = (int) (getHeight() - mLinearLayout1.getMeasuredHeight() - ev.getY());
        Log.i("TAG", "Dis" + distance);
        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, distance + mDis);
        animation.setDuration(2200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout4.clearAnimation();
        mLinearLayout4.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("TAG", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("TAG", "onAnimationEnd");
                mLinearLayout4.layout(cl, ct, cr, cb);
                mLinearLayout4.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();

    }

    private void recoverLayout1(MotionEvent ev) {
        final int cl = 0;
        final int ct = getHeight() - mLinearLayout1.getMeasuredHeight();
        final int cr = cl + mLinearLayout1.getMeasuredWidth();
        final int cb = ct + mLinearLayout1.getMeasuredHeight();
        final int locationY = (int) (ev.getY() - mDis);
        int distance = (int) (ct - ev.getY() + mDis);
        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, distance);
        animation.setDuration(2200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout1.clearAnimation();
        mLinearLayout1.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("TAG", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("TAG", "onAnimationEnd");
                mLinearLayout1.layout(cl, ct, cr, cb);
                mLinearLayout1.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();

    }

    private void recoverLayout2(MotionEvent ev) {
        final int cl = (getWidth() - mLinearLayout2.getMeasuredWidth()) / 2;
        final int ct = getHeight() - mLinearLayout2.getMeasuredHeight() - 120;
        final int cr = cl + mLinearLayout2.getMeasuredWidth();
        final int cb = ct + mLinearLayout2.getMeasuredHeight();
        int distance = (int) (getHeight() - mLinearLayout1.getMeasuredHeight() - ev.getY());

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, distance + mDis);
        animation.setDuration(2200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout2.clearAnimation();
        mLinearLayout2.setAnimation(animation);
        final float alp = mLinearLayout2.getAlpha();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLinearLayout2.setAlpha(alp);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLinearLayout2.setAlpha(1.0f);
                mLinearLayout2.layout(cl, ct, cr, cb);
                mLinearLayout2.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();
    }


    private void MovieRoadView(MotionEvent ev) {
        if (checkIsLinearLayoutIsNull()) {
            initLinearLayoutView();
        }
        // left top right bottom
        // left 不变，right不变
        int left, top, right, bottom;
        int left2, top2, right2, bottom2;
        int left4, top4, right4, bottom4;
        int distance = (int) (getHeight() - mLinearLayout1.getMeasuredHeight() - ev.getY());
        left = 0;
        left2 = (getWidth() - mLinearLayout2.getMeasuredWidth()) / 2;
        left4 = 0;

        right = left + mLinearLayout1.getMeasuredWidth();
        right2 = left2 + mLinearLayout2.getMeasuredWidth();
        right4 = getWidth();

        top = (int) ev.getY() - (int) mDis;
        top2 = top - 120;
        top4 = (int) ev.getY() + (int) (mLinearLayout1.getMeasuredHeight() - mDis);


        bottom = top + mLinearLayout1.getMeasuredHeight();
        bottom2 = bottom - 120;
        bottom4 = bottom + mLinearLayout4.getMeasuredHeight();

        mLinearLayout1.layout(left, top, right, bottom);
        mLinearLayout2.layout(left2, top2, right2, bottom2);
        mLinearLayout4.layout(left4, top4, right4, bottom4);
        float precent = (float) (distance) / (getHeight() * 2 / 3 - mLinearLayout1.getMeasuredHeight());
        mLinearLayout2.setAlpha(1 - precent);

    }

    private boolean checkIsLinearLayoutIsNull() {
        if (null == mLinearLayout1 || null == mLinearLayout2 || null == mLinearLayout3 || null == mLinearLayout4) {
            return true;
        }
        return false;
    }

    public boolean checkIsRoadView(MotionEvent ev) {
        if (null == mLinearLayout1 || null == mLinearLayout2) {
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


    private void moveToTop(MotionEvent ev) {
        final int cl = 0;
        final int ct = 0;
        final int cr = getWidth();
        final int cb = ct + getHeight();
        int distance = (int) (-mLinearLayout1.getMeasuredHeight() - ev.getY());
        Log.i("TAG", "Dis" + distance);
        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, distance + mDis);
        animation.setDuration(2200);
        animation.setInterpolator(new OvershootInterpolator());
        mLinearLayout4.clearAnimation();
        mLinearLayout4.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("TAG", "onAnimationStart");
                mLinearLayout1.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("TAG", "onAnimationEnd");
                mLinearLayout1.setAlpha(0.0f);
                mLinearLayout1.setVisibility(INVISIBLE);
                mLinearLayout2.setVisibility(INVISIBLE);
                mCurrentScrollDirection = Direction.ERR;
                mLinearLayout4.layout(cl, ct, cr, cb);
                mLinearLayout4.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();

    }


}