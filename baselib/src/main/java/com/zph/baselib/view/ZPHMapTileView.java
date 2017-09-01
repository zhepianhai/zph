package com.zph.baselib.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.zph.baselib.R;

/**
 * Created by zph on 2017/9/1.
 */

public class ZPHMapTileView extends SlidingDrawer implements GestureDetector.OnGestureListener{

    public ZPHMapTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZPHMapTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(this.isOpened()){
            this.getHandle().setFocusable(false);
            LayoutParams par=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,(int) getResources().getDimension(R.dimen.fu));

            this.getHandle().setLayoutParams(par);
            return  false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        super.setOnDrawerCloseListener(onDrawerCloseListener);
    }


}