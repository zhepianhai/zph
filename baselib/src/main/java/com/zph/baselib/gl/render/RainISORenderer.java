package com.zph.baselib.gl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.zph.baselib.gl.shape.TriPrism;
import com.zph.baselib.gl.utils.MatrixState;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/28.
 */

public class RainISORenderer implements GLSurfaceView.Renderer{
    private Context mContext;
    private static final float[] RAIN_LEVEL = { 0, 10, 25, 50, 100, 250, Float.MAX_VALUE };
    private static final float[][] RAIN_COLOR = { { 1.0f, 1.0f, 1.0f, 1.0f }, { 0.608f, 1.000f, 0.529f, 1.0f },
            { 0.196f, 0.667f, 0.000f, 1.0f }, { 0.000f, 0.000f, 1.000f, 1.0f }, { 1.000f, 0.000f, 1.000f, 1.0f },
            { 1.000f, 0.000f, 0.000f, 1.0f }, { 1.000f, 0.000f, 0.000f, 1.0f } };

    private static final float SHOW_MAX_H = 1f;
    private static final float SHOW_MAX_V = 0.5f;

    private ArrayList<TriPrism> mArrPrism;

    private float mScale = 0.5f;
    private float mAlpha = 0.8f;
    private float mHAngle = 180;
    private float mVAngle = -30;



    public RainISORenderer( Context mContext){
        this.mContext=mContext;
        TriPrism.ISOSHOW_LEVEL = new float[] { 10, 25, 50, 100, 250 };
        TriPrism.ISOSHOW_COLOR = new float[][] { { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f },
                { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f } };

        mArrPrism = new ArrayList<TriPrism>();
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        // gl.glClearColor(0.7f, 0.7f, 0.7f, 0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, -10, 10);

//        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
//        MatrixState.setCamera(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        MatrixState.setInitStack();
//        if(null!=mArrPrism&&mArrPrism.size()>0){
//            for(int i=0;i<mArrPrism.size();++i){
//                mArrPrism.get(i).addMatrix(gl, width, height);
//            }
//        }

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glColor4f(0, 0, 0, 1);

        gl.glLoadIdentity();

        gl.glRotatef(mVAngle, 1, 0, 0);
        gl.glRotatef(mHAngle, 0, 1, 0);
        gl.glTranslatef(0.0f, 0.25f, 0.25f);


//
        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        float r = 0.5f ;//
//        float[] coords = {
//                -r,r,0,
//                -r,-r,0,
//                r,r,0,
//                r,-r,0,
//        };
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0,BufferUtil.floatToBuffer(coords));
//
//
//		gl.glTranslatef(0.0f, 0.25f, 0.25f);
        TriPrism prism = null;

        for (int i = 0; i < mArrPrism.size(); i++) {
            prism = mArrPrism.get(i);
            prism.draw(gl);
        }
    }

    public void showData(float[][] mDataGrid, float mDataMax, int col, int row) {
        TriPrism prism;

        int col_cen = (col % 2 == 0 ? col / 2 : col / 2 + 1);
        int row_cen = (row % 2 == 0 ? row / 2 : row / 2 + 1);

        TriPrism.SCALESHOW_COORD = RainISORenderer.SHOW_MAX_H / (col_cen >= row_cen ? col_cen : row_cen);
        TriPrism.SCALESHOW_VALUE = RainISORenderer.SHOW_MAX_V / 250;

        float v00, v10, v01, v11;
        for (int i = 0; i < mDataGrid.length - 1; i++) {
            for (int j = 0; j < mDataGrid[0].length - 1; j++) {
                mDataGrid[i][j] = mDataGrid[i][j] + 0.1f;
                mDataGrid[i + 1][j] = mDataGrid[i + 1][j] + 0.1f;
                mDataGrid[i][j + 1] = mDataGrid[i][j + 1] + 0.1f;
                mDataGrid[i + 1][j + 1] = mDataGrid[i + 1][j + 1] + 0.1f;

            }
        }

        for (int i = 0; i < mDataGrid.length - 1; i++) {
            for (int j = 0; j < mDataGrid[0].length - 1; j++) {
                v00 = mDataGrid[i][j];
                v10 = mDataGrid[i + 1][j];
                v01 = mDataGrid[i][j + 1];
                v11 = mDataGrid[i + 1][j + 1];

                if (v00 >= -0.1) {
                    if (v11 < 0) {
                        if (v01 >= -0.1 && v10 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - i), (j + 1 - row_cen), getShowData(v01) },
                                    { (col_cen - (i + 1)), (j - row_cen), getShowData(v10) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v10) };
                            int[][] indexs = { { i, j }, { i, j + 1 }, { i + 1, j } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    } else {
                        if (v10 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11) },
                                    { (col_cen - (i + 1)), (j - row_cen), getShowData(v10) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v11),
                                    this.getRainISOColor(v10) };
                            int[][] indexs = { { i, j }, { i + 1, j + 1 }, { i + 1, j } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                        if (v01 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - i), (j + 1 - row_cen), getShowData(v01) },
                                    { (col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v11) };
                            int[][] indexs = { { i, j }, { i, j + 1 }, { i + 1, j + 1 } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    }
                }
            }
        }
    }

    private float getShowData(float v) {
        if (v < 1) {
            return 1 * 2 + 30;
        } else {
            return v * 2 + 30;
        }
    }

    public float[] getRainISOColor(float v) {
        int index = -1;
        for (int i = 0; i < RAIN_LEVEL.length; i++) {
            if (v >= RAIN_LEVEL[i] && v < RAIN_LEVEL[i + 1]) {
                index = i;
                break;
            }
        }

        if (index == RAIN_LEVEL.length - 1) {
            return RAIN_COLOR[RAIN_LEVEL.length - 1];
        } else if (index >= 0) {
            float rain_value_0 = RAIN_LEVEL[index];
            float rain_value_1 = RAIN_LEVEL[index + 1];
            float[] rain_color_0 = RAIN_COLOR[index];
            float[] rain_color_1 = RAIN_COLOR[index + 1];

            float rain_r = rain_color_0[0]
                    + ((rain_color_1[0] - rain_color_0[0]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);
            float rain_g = rain_color_0[1]
                    + ((rain_color_1[1] - rain_color_0[1]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);
            float rain_b = rain_color_0[2]
                    + ((rain_color_1[2] - rain_color_0[2]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);

            return new float[] { rain_r, rain_g, rain_b, 1f };
        }

        return RAIN_COLOR[0];
    }
    public List<TriPrism> getTriPrismList(){
        if(null==mArrPrism){
            return null;
        }
        if(mArrPrism.size()==0){
            return null;
        }
        return mArrPrism;
    }

    private final float TOUCH_SCALE_FACTOR = 90.0f / 320;
    private float xRotation,yRotation,zRotation;
    public void handlTouchDrag(float x,float y,float z){
        if(null==mArrPrism||mArrPrism.size()==0){
            return;
        }
        for (int i = 0; i < mArrPrism.size(); ++i) {
            mArrPrism.get(i).yAngle += x * TOUCH_SCALE_FACTOR;//
            mArrPrism.get(i).xAngle += y * TOUCH_SCALE_FACTOR;//
            mArrPrism.get(i).zAngle += z * TOUCH_SCALE_FACTOR;//
        }
//        xRotation+=x;
//        yRotation+=y;
//        zRotation+=z;
    }


}
