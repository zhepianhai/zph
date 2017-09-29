package com.zph.baselib.gl.obj;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/28.
 */

public abstract class Prism {
    public static final float[] COLOR_BOTTOM = {0.5f, 0.5f, 0, 0.5f};

    protected FloatBuffer mPrismBuffer;
    protected FloatBuffer mColorBuffer;

    protected FloatBuffer mLineDataBuffer;
    protected FloatBuffer mLineColorBuffer;
    protected int mLineCount;

    public abstract void draw(GL10 gl);



    protected  FloatBuffer mPointLineBuffer;

}
