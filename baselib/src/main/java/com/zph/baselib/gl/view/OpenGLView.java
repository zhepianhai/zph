package com.zph.baselib.gl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zph.baselib.gl.render.RainISORenderer;
import com.zph.baselib.gl.shape.TriPrism;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/28.
 */

public class OpenGLView extends GLSurfaceViewBase {
    private Context mContext;
    private ArrayList<TriPrism> mArrPrism;
    private static final float[] RAIN_LEVEL = {0, 10, 25, 50, 100, 250, Float.MAX_VALUE};
    private static final float[][] RAIN_COLOR = {{1.0f, 1.0f, 1.0f, 1.0f}, {0.608f, 1.000f, 0.529f, 1.0f},
            {0.196f, 0.667f, 0.000f, 1.0f}, {0.000f, 0.000f, 1.000f, 1.0f}, {1.000f, 0.000f, 1.000f, 1.0f},
            {1.000f, 0.000f, 0.000f, 1.0f}, {1.000f, 0.000f, 0.000f, 1.0f}};

    private static final float SHOW_MAX_H = 1f;
    private static final float SHOW_MAX_V = 0.5f;

    public OpenGLView(Context context) {
        super(context,200);
        this.mContext = context;
        TriPrism.ISOSHOW_LEVEL = new float[]{10, 25, 50, 100, 250};
        TriPrism.ISOSHOW_COLOR = new float[][]{{1.000f, 1.000f, 1.000f, 1.0f}, {1.000f, 1.000f, 1.000f, 1.0f},
                {1.000f, 1.000f, 1.000f, 1.0f}, {1.000f, 1.000f, 1.000f, 1.0f}, {1.000f, 1.000f, 1.000f, 1.0f}};

        mArrPrism = new ArrayList<>();

    }

    public void setRenderer(RainISORenderer renderer) {
    }

    public OpenGLView(Context context, int fps) {
        super(context, 20);
    }

    @Override
    protected void init(GL10 gl) {
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

    float xrot = 0f;
    float yrot = 0.0f; // 程序运行开始时x、y方向的旋转角度

    @Override
    protected void drawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glColor4f(0, 0, 0, 1);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.25f, 0.25f);
//
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        TriPrism prism = null;
        for (int i = 0; i < mArrPrism.size(); i++) {
            prism = mArrPrism.get(i);
//            if (flag) {
//                prism.onChange(gl, xrot, yrot);
//            }
            prism.onDraws(gl, xrot, yrot);


        }
    }

    float oldx, oldy;
    boolean flag;

    public boolean onTouchEvent(final MotionEvent event) { // 处理屏幕点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) { // 当按下屏幕时---获取点击的x、y坐标
            oldx = event.getX();
            oldy = event.getY();
            flags = true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) { // 当在屏幕上移动时---根据移动的距离改变旋转角度
            xrot -= (oldy - event.getY()); // 为什么x轴对应于y方向的移动是因为绕x轴转动其实是y方向的旋转   下同
            yrot -= (oldx - event.getX());
            oldx = event.getX();
            oldy = event.getY();
            flags = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            flags = false;
        }
        return true;
    }


    public void showData(float[][] mDataGrid, float mDataMax, int col, int row) {
        TriPrism prism;

        int col_cen = (col % 2 == 0 ? col / 2 : col / 2 + 1);
        int row_cen = (row % 2 == 0 ? row / 2 : row / 2 + 1);

        TriPrism.SCALESHOW_COORD = OpenGLView.SHOW_MAX_H / (col_cen >= row_cen ? col_cen : row_cen);
        TriPrism.SCALESHOW_VALUE = OpenGLView.SHOW_MAX_V / 250;

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
                            float[][] points = {{(col_cen - i), (j - row_cen), getShowData(v00)},
                                    {(col_cen - i), (j + 1 - row_cen), getShowData(v01)},
                                    {(col_cen - (i + 1)), (j - row_cen), getShowData(v10)}};
                            float[][] colors = new float[][]{this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v10)};
                            int[][] indexs = {{i, j}, {i, j + 1}, {i + 1, j}};
                            prism = new TriPrism(mContext, points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    } else {
                        if (v10 >= -0.1) {
                            float[][] points = {{(col_cen - i), (j - row_cen), getShowData(v00)},
                                    {(col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11)},
                                    {(col_cen - (i + 1)), (j - row_cen), getShowData(v10)}};
                            float[][] colors = new float[][]{this.getRainISOColor(v00), this.getRainISOColor(v11),
                                    this.getRainISOColor(v10)};
                            int[][] indexs = {{i, j}, {i + 1, j + 1}, {i + 1, j}};
                            prism = new TriPrism(mContext, points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                        if (v01 >= -0.1) {
                            float[][] points = {{(col_cen - i), (j - row_cen), getShowData(v00)},
                                    {(col_cen - i), (j + 1 - row_cen), getShowData(v01)},
                                    {(col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11)}};
                            float[][] colors = new float[][]{this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v11)};
                            int[][] indexs = {{i, j}, {i, j + 1}, {i + 1, j + 1}};
                            prism = new TriPrism(mContext, points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    }
                }
            }
        }
        flags = true;
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

            return new float[]{rain_r, rain_g, rain_b, 1f};
        }

        return RAIN_COLOR[0];
    }


}
