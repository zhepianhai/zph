package com.zph.baselib.gl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.zph.baselib.R;
import com.zph.baselib.gl.config.TestConfig;
import com.zph.baselib.gl.obj.Prism;
import com.zph.baselib.gl.utils.BufferUtil;
import com.zph.baselib.gl.utils.MatrixHelper;
import com.zph.baselib.gl.utils.MatrixState;
import com.zph.baselib.gl.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/28.
 */

public class TriPrism extends Prism{
    public static float SCALESHOW_COORD = 1.0f;
    public static float SCALESHOW_VALUE = 1.0f;

    public static float[] ISOSHOW_LEVEL = null;
    public static float[][] ISOSHOW_COLOR = null;

    public static boolean ISOSHOW_FLAG_GRID = false;

    private float[][] mDataValue;
    private float[][] mDataColor;
    private int[][] mDataIndex;
    private float[][] mData;

    /**
     * zph
     */

    private Context mContext;
    private float[] mLine = new float[9];

    public float yAngle = -30;//
    public float xAngle = 0;//
    public float zAngle = 0;//
    private String vertexShaderSource,fragmentShaderSource;
    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final int BYTES_PER_FLOAT = 4;//字节数
    static final int POSITION_COMPONENT_COUNT = 4;//每个顶点的个数
    //颜色线性插值的参数
    private static final int COLOR_COMPONENT_COUNT=3;
    private static final String A_COLOR = "a_Color";
    //用来划分位置值和颜色值的（跨距）知道颜色直接字节数间距，读取时候可以跳过
    private static final int STRIDE=(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
    private int aColorLocation;

    //Matrix
    private static final  String U_MATRIX="u_Matrix";
    private final float[] projectMatrix=new float[16];//存储矩阵
    private int uMatrixLocation;//保存Matrix位置

    private float[]modleMatrix=new float[16];



    public TriPrism(Context mContext,float[][] value, float[][] color, int[][] index, float[][] data) {
        this.mContext=mContext;


        mDataValue = value;
        mDataColor = color;
        mDataIndex = index;
        mData = data;

        // VALUE
        float[] tempValue = new float[54];
        // Bottom = 0 - 2 - 1
        tempValue[0] = SCALESHOW_COORD * value[0][0];
        tempValue[1] = 0;
        tempValue[2] = SCALESHOW_COORD * value[0][1];
        tempValue[3] = SCALESHOW_COORD * value[2][0];
        tempValue[4] = 0;
        tempValue[5] = SCALESHOW_COORD * value[2][1];
        tempValue[6] = SCALESHOW_COORD * value[1][0];
        tempValue[7] = 0;
        tempValue[8] = SCALESHOW_COORD * value[1][1];

        // 0 - 0' - 2' - 2
        tempValue[9] = SCALESHOW_COORD * value[0][0];
        tempValue[10] = 0;
        tempValue[11] = SCALESHOW_COORD * value[0][1];
        tempValue[12] = SCALESHOW_COORD * value[0][0];
        tempValue[13] = SCALESHOW_VALUE * value[0][2];
        tempValue[14] = SCALESHOW_COORD * value[0][1];
        tempValue[15] = SCALESHOW_COORD * value[2][0];
        tempValue[16] = SCALESHOW_VALUE * value[2][2];
        tempValue[17] = SCALESHOW_COORD * value[2][1];
        tempValue[18] = SCALESHOW_COORD * value[2][0];
        tempValue[19] = 0;
        tempValue[20] = SCALESHOW_COORD * value[2][1];
        // 0 - 1 - 1' - 0'
        tempValue[21] = SCALESHOW_COORD * value[0][0];
        tempValue[22] = 0;
        tempValue[23] = SCALESHOW_COORD * value[0][1];
        tempValue[24] = SCALESHOW_COORD * value[1][0];
        tempValue[25] = 0;
        tempValue[26] = SCALESHOW_COORD * value[1][1];
        tempValue[27] = SCALESHOW_COORD * value[1][0];
        tempValue[28] = SCALESHOW_VALUE * value[1][2];
        tempValue[29] = SCALESHOW_COORD * value[1][1];
        tempValue[30] = SCALESHOW_COORD * value[0][0];
        tempValue[31] = SCALESHOW_VALUE * value[0][2];
        tempValue[32] = SCALESHOW_COORD * value[0][1];
        // 1 - 2 - 2' - 1'
        tempValue[33] = SCALESHOW_COORD * value[1][0];
        tempValue[34] = 0;
        tempValue[35] = SCALESHOW_COORD * value[1][1];
        tempValue[36] = SCALESHOW_COORD * value[2][0];
        tempValue[37] = 0;
        tempValue[38] = SCALESHOW_COORD * value[2][1];
        tempValue[39] = SCALESHOW_COORD * value[2][0];
        tempValue[40] = SCALESHOW_VALUE * value[2][2];
        tempValue[41] = SCALESHOW_COORD * value[2][1];
        tempValue[42] = SCALESHOW_COORD * value[1][0];
        tempValue[43] = SCALESHOW_VALUE * value[1][2];
        tempValue[44] = SCALESHOW_COORD * value[1][1];
        // 0' - 1' - 2'
        tempValue[45] = SCALESHOW_COORD * value[0][0];
        tempValue[46] = SCALESHOW_VALUE * value[0][2];
        tempValue[47] = SCALESHOW_COORD * value[0][1];
        tempValue[48] = SCALESHOW_COORD * value[1][0];
        tempValue[49] = SCALESHOW_VALUE * value[1][2];
        tempValue[50] = SCALESHOW_COORD * value[1][1];
        tempValue[51] = SCALESHOW_COORD * value[2][0];
        tempValue[52] = SCALESHOW_VALUE * value[2][2];
        tempValue[53] = SCALESHOW_COORD * value[2][1];

        //// mLine[0]=tempValue[14];
        // mLine[0]=tempValue[14];
        // mLine[1]=tempValue[15];
        // mLine[2]=tempValue[16];
        // mLine[3]=tempValue[17];
        // mLine[4]=tempValue[18];
        // mLine[5]=tempValue[19];
        // mLine[6]=tempValue[20];
        // mLine[7]=tempValue[21];
        // mLine[8]=tempValue[22];

        mLine[0] = 0.5f;
        mLine[1] = 0.8235294f;
        mLine[2] = 0.5f;
        mLine[3] = 0.064f;
        mLine[4] = 0f;
        mLine[5] = 0.5f;

        mLine[6] = 0.82f;
        mLine[7] = 0.21f;
        mLine[8] = 0.5f;

        // 09-25 11:04:14.415: I/TAG(5713): data1:0.05882353
        // 09-25 11:04:14.415: I/TAG(5713): data2:-0.8235294
        // 09-25 11:04:14.415: I/TAG(5713): data3:0.064

        mPrismBuffer = BufferUtil.floatToBuffer(tempValue);

        mPointLineBuffer = BufferUtil.floatToBuffer(mLine);

        // COLOR
        float[] tempColor = new float[72];
        // Bottom = 0 - 2 - 1
        tempColor[0] = COLOR_BOTTOM[0];
        tempColor[1] = COLOR_BOTTOM[1];
        tempColor[2] = COLOR_BOTTOM[2];
        tempColor[3] = COLOR_BOTTOM[3];
        tempColor[4] = COLOR_BOTTOM[0];
        tempColor[5] = COLOR_BOTTOM[1];
        tempColor[6] = COLOR_BOTTOM[2];
        tempColor[7] = COLOR_BOTTOM[3];
        tempColor[8] = COLOR_BOTTOM[0];
        tempColor[9] = COLOR_BOTTOM[1];
        tempColor[10] = COLOR_BOTTOM[2];
        tempColor[11] = COLOR_BOTTOM[3];
        // 0 - 0' - 2' - 2
        tempColor[12] = COLOR_BOTTOM[0];
        tempColor[13] = COLOR_BOTTOM[1];
        tempColor[14] = COLOR_BOTTOM[2];
        tempColor[15] = COLOR_BOTTOM[3];
        tempColor[16] = color[0][0];
        tempColor[17] = color[0][1];
        tempColor[18] = color[0][2];
        tempColor[19] = color[0][3];
        tempColor[20] = color[2][0];
        tempColor[21] = color[2][1];
        tempColor[22] = color[2][2];
        tempColor[23] = color[2][3];
        tempColor[24] = COLOR_BOTTOM[0];
        tempColor[25] = COLOR_BOTTOM[1];
        tempColor[26] = COLOR_BOTTOM[2];
        tempColor[27] = COLOR_BOTTOM[3];
        // 0 - 1 - 1' - 0'
        tempColor[28] = COLOR_BOTTOM[0];
        tempColor[29] = COLOR_BOTTOM[1];
        tempColor[30] = COLOR_BOTTOM[2];
        tempColor[31] = COLOR_BOTTOM[3];
        tempColor[32] = COLOR_BOTTOM[0];
        tempColor[33] = COLOR_BOTTOM[1];
        tempColor[34] = COLOR_BOTTOM[2];
        tempColor[35] = COLOR_BOTTOM[3];
        tempColor[36] = color[1][0];
        tempColor[37] = color[1][1];
        tempColor[38] = color[1][2];
        tempColor[39] = color[1][3];
        tempColor[40] = color[0][0];
        tempColor[41] = color[0][1];
        tempColor[42] = color[0][2];
        tempColor[43] = color[0][3];
        // 1 - 2 - 2' - 1'
        tempColor[44] = COLOR_BOTTOM[0];
        tempColor[45] = COLOR_BOTTOM[1];
        tempColor[46] = COLOR_BOTTOM[2];
        tempColor[47] = COLOR_BOTTOM[3];
        tempColor[48] = COLOR_BOTTOM[0];
        tempColor[49] = COLOR_BOTTOM[1];
        tempColor[50] = COLOR_BOTTOM[2];
        tempColor[51] = COLOR_BOTTOM[3];
        tempColor[52] = color[2][0];
        tempColor[53] = color[2][1];
        tempColor[54] = color[2][2];
        tempColor[55] = color[2][3];
        tempColor[56] = color[1][0];
        tempColor[57] = color[1][1];
        tempColor[58] = color[1][2];
        tempColor[59] = color[1][3];
        // 0' - 1' - 2'
        tempColor[60] = color[0][0];
        tempColor[61] = color[0][1];
        tempColor[62] = color[0][2];
        tempColor[63] = color[0][3];
        tempColor[64] = color[1][0];
        tempColor[65] = color[1][1];
        tempColor[66] = color[1][2];
        tempColor[67] = color[1][3];
        tempColor[68] = color[2][0];
        tempColor[69] = color[2][1];
        tempColor[70] = color[2][2];
        tempColor[71] = color[2][3];

        mColorBuffer = BufferUtil.floatToBuffer(tempColor);



        // LINE - VALUE
        mLineCount = 0;

        float[] tempLineValue = new float[ISOSHOW_LEVEL.length * 2 * 3];
        float[] tempLineColor = new float[ISOSHOW_LEVEL.length * 2 * 4];

        float v0 = value[0][2];
        float v1 = value[1][2];
        float v2 = value[2][2];
        if (ISOSHOW_LEVEL.length > 0) {
            float iso_level;
            float x1, y1, x2, y2;
            for (int i = 0; i < ISOSHOW_LEVEL.length - 1; i++) {
                iso_level = ISOSHOW_LEVEL[i];
                if (v0 >= iso_level && (v1 < iso_level && v2 < iso_level)) {
                    x1 = value[1][0] + (value[0][0] - value[1][0]) / (v0 - v1) * (iso_level - v1);
                    y1 = value[1][1] + (value[0][1] - value[1][1]) / (v0 - v1) * (iso_level - v1);
                    x2 = value[2][0] + (value[0][0] - value[2][0]) / (v0 - v2) * (iso_level - v2);
                    y2 = value[2][1] + (value[0][1] - value[2][1]) / (v0 - v2) * (iso_level - v2);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                } else if (v1 >= iso_level && (v0 < iso_level && v2 < iso_level)) {
                    x1 = value[0][0] + (value[1][0] - value[0][0]) / (v1 - v0) * (iso_level - v0);
                    y1 = value[0][1] + (value[1][1] - value[0][1]) / (v1 - v0) * (iso_level - v0);
                    x2 = value[2][0] + (value[1][0] - value[2][0]) / (v1 - v2) * (iso_level - v2);
                    y2 = value[2][1] + (value[1][1] - value[2][1]) / (v1 - v2) * (iso_level - v2);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                } else if (v2 >= iso_level && (v0 < iso_level && v1 < iso_level)) {
                    x1 = value[0][0] + (value[2][0] - value[0][0]) / (v2 - v0) * (iso_level - v0);
                    y1 = value[0][1] + (value[2][1] - value[0][1]) / (v2 - v0) * (iso_level - v0);
                    x2 = value[1][0] + (value[2][0] - value[1][0]) / (v2 - v1) * (iso_level - v1);
                    y2 = value[1][1] + (value[2][1] - value[1][1]) / (v2 - v1) * (iso_level - v1);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                } else if (v0 < iso_level && (v1 >= iso_level && v2 >= iso_level)) {
                    x1 = value[0][0] + (value[1][0] - value[0][0]) / (v1 - v0) * (iso_level - v0);
                    y1 = value[0][1] + (value[1][1] - value[0][1]) / (v1 - v0) * (iso_level - v0);
                    x2 = value[0][0] + (value[2][0] - value[0][0]) / (v2 - v0) * (iso_level - v0);
                    y2 = value[0][1] + (value[2][1] - value[0][1]) / (v2 - v0) * (iso_level - v0);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                } else if (v1 < iso_level && (v0 >= iso_level && v2 >= iso_level)) {
                    x1 = value[1][0] + (value[0][0] - value[1][0]) / (v0 - v1) * (iso_level - v1);
                    y1 = value[1][1] + (value[0][1] - value[1][1]) / (v0 - v1) * (iso_level - v1);
                    x2 = value[1][0] + (value[2][0] - value[1][0]) / (v2 - v1) * (iso_level - v1);
                    y2 = value[1][1] + (value[2][1] - value[1][1]) / (v2 - v1) * (iso_level - v1);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                } else if (v2 < iso_level && (v0 >= iso_level && v1 >= iso_level)) {
                    x1 = value[2][0] + (value[0][0] - value[2][0]) / (v0 - v2) * (iso_level - v2);
                    y1 = value[2][1] + (value[0][1] - value[2][1]) / (v0 - v2) * (iso_level - v2);
                    x2 = value[2][0] + (value[1][0] - value[2][0]) / (v1 - v2) * (iso_level - v2);
                    y2 = value[2][1] + (value[1][1] - value[2][1]) / (v1 - v2) * (iso_level - v2);

                    this.initLineData(tempLineValue, tempLineColor, x1, y1, iso_level, i, x2, y2, iso_level, i);
                }
            }
        }

        mLineDataBuffer = BufferUtil.floatToBuffer(tempLineValue);
        mLineColorBuffer = BufferUtil.floatToBuffer(tempLineColor);




    }
    /**
     * 绑定着色器
     * */
    private void initParame() {
        vertexShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_verteex_shaer1);
        fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
        program = TestConfig.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(program);
        aColorLocation=GLES20.glGetAttribLocation(program,A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        GLES20.glVertexAttribPointer(aPositionLocation, 4,
                GLES20.GL_FLOAT, false, 0, this.mPrismBuffer);
        mPrismBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, this.mPrismBuffer);

        GLES20.glEnableVertexAttribArray(aColorLocation);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    private void initLineData(float[] lineValue, float[] lineColor, float x1, float y1, float v1, int c1, float x2,
                              float y2, float v2, int c2) {
        lineValue[mLineCount * 6 + 0] = SCALESHOW_COORD * x1;
        lineValue[mLineCount * 6 + 1] = SCALESHOW_VALUE * v1 * 1.01f;
        lineValue[mLineCount * 6 + 2] = SCALESHOW_COORD * y1;
        lineValue[mLineCount * 6 + 3] = SCALESHOW_COORD * x2;
        lineValue[mLineCount * 6 + 4] = SCALESHOW_VALUE * v2 * 1.01f;
        lineValue[mLineCount * 6 + 5] = SCALESHOW_COORD * y2;

        lineColor[mLineCount * 8 + 0] = ISOSHOW_COLOR[c1][0];
        lineColor[mLineCount * 8 + 1] = ISOSHOW_COLOR[c1][1];
        lineColor[mLineCount * 8 + 2] = ISOSHOW_COLOR[c1][2];
        lineColor[mLineCount * 8 + 3] = ISOSHOW_COLOR[c1][3];
        lineColor[mLineCount * 8 + 4] = ISOSHOW_COLOR[c2][0];
        lineColor[mLineCount * 8 + 5] = ISOSHOW_COLOR[c2][1];
        lineColor[mLineCount * 8 + 6] = ISOSHOW_COLOR[c2][2];
        lineColor[mLineCount * 8 + 7] = ISOSHOW_COLOR[c2][3];

        mLineCount = mLineCount + 1;
    }
    public void onChange(GL10 gl ,float xrot,float yrot){
        gl.glLoadIdentity();
    }

    public void onDraws(GL10 gl,float xrot,float yrot){
        if (mPrismBuffer == null || mColorBuffer == null) {
            return;
        }

//        gl.glLoadIdentity();
        Matrix.setIdentityM(modleMatrix,0);
        Matrix.rotateM(modleMatrix,0,xrot/10,1f,0f,0f);
        Matrix.rotateM(modleMatrix,0,yrot/10,0f,1f,0f);
        float[] temp=new float[16];
        Matrix.multiplyMM(temp,0,projectMatrix,0,modleMatrix,0);
        System.arraycopy(temp,0,projectMatrix,0,temp.length);

//        Matrix.translateM(modleMatrix,0,0f,0f,-2f);
        Matrix.rotateM(modleMatrix,0,xrot/10,1f,0f,0f);
        Matrix.rotateM(modleMatrix,0,yrot/10,0f,1f,0f);




//        gl.glRotatef(xrot/10, 1, 0, 0);
//        gl.glRotatef(yrot/10, 0, 1, 0);


        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mPrismBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 3);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        // Grid
        if (ISOSHOW_FLAG_GRID) {
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 3);
        }

        if (this.isNeedShow(0, 2, 1)) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 3, 4);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            // Border
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 2);
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 4, 2);
            // Grid
            if (ISOSHOW_FLAG_GRID) {
                gl.glDrawArrays(GL10.GL_LINE_LOOP, 3, 4);
            }
        }

        if (this.isNeedShow(0, 1, 2)) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 7, 4);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            // Border
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 7, 2);
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 15, 2);
            // Grid
            if (ISOSHOW_FLAG_GRID) {
                gl.glDrawArrays(GL10.GL_LINE_LOOP, 7, 4);
            }
        }

        if (this.isNeedShow(1, 2, 0)) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 11, 4);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            // Border
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 11, 2);
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 16, 2);
            // Grid
            if (ISOSHOW_FLAG_GRID) {
                gl.glDrawArrays(GL10.GL_LINE_LOOP, 11, 4);
            }
        }

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 15, 3);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        // Grid
        if (ISOSHOW_FLAG_GRID) {
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 15, 3);
        }

        // ISOLINE
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mLineDataBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mLineColorBuffer);

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDrawArrays(GL10.GL_LINES, 0, mLineCount * 2);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glFinish();

        initParame();

    }

    @Override
    public void draw(GL10 gl) {


//        gl.glRotatef(-30, xAngle, yAngle, zAngle);
//        MatrixState.rotate(xAngle, 1, 0, 0);
//        MatrixState.rotate(yAngle, 0, 1, 0);
//        MatrixState.rotate(zAngle, 0, 0, 1);
//        MatrixState.setCamera(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        MatrixState.setInitStack();


        // BASE




    }


    private boolean isNeedShow(int index1, int index2, int indexoth) {
        if (mDataIndex != null && mData != null) {
            int[] p0 = mDataIndex[indexoth];
            int[] p1 = mDataIndex[index1];
            int[] p2 = mDataIndex[index2];

            if (p1[0] == p2[0]) {
                int index_x = p1[0] * 2 - p0[0];
                if ((index_x >= 0 && index_x < mData.length)
                        && (mData[index_x][p1[1]] >= -0.1 || mData[index_x][p2[1]] >= -0.1)) {
                    return false;
                }
            } else if (p1[1] == p2[1]) {
                int index_y = p1[1] * 2 - p0[1];
                if ((index_y >= 0 && index_y < mData[0].length)
                        && (mData[p1[0]][index_y] >= -0.1 || mData[p2[0]][index_y] >= -0.1)) {
                    return false;
                }
            } else {
                if (p1[0] == p0[0]) {
                    int index_x = p2[0];
                    int index_y = p1[1];
                    if (mData[index_x][index_y] >= -0.1) {
                        return false;
                    }
                } else if (p1[1] == p0[1]) {
                    int index_y = p2[1];
                    int index_x = p1[0];
                    if (mData[index_x][index_y] >= -0.1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 添加正交投影矩阵
     * */
    public void addMatrix(GL10 gl, int width, int height) {
        //创建一个45°的透视投影
        MatrixHelper.perspectiveM(projectMatrix,45,(float)(width)/(float)(height),1f,0f);
        //把模型矩阵设置为单位矩阵，并向Z移动2个单位
        Matrix.setIdentityM(modleMatrix,0);
        //平移
        Matrix.translateM(modleMatrix,0,0f,0f,-2f);
        //添加旋转
//        Matrix.rotateM(modleMatrix,0,-60f,1f,0f,0f);

        //设置一个新的矩阵为两矩阵相乘结果
        float[] temp=new float[16];
        Matrix.multiplyMM(temp,0,projectMatrix,0,modleMatrix,0);
        System.arraycopy(temp,0,projectMatrix,0,temp.length);

        Matrix.translateM(modleMatrix,0,0f,0f,-2f);
        Matrix.rotateM(modleMatrix,0,-60f,1f,0f,0f);
//
//        final  float aspectRatio=width>height?
//                (float)width/(float)height:(float)height/(float)width;
//        if(width>height){
//            Matrix.orthoM(projectMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
//        }else {
//            Matrix.orthoM(projectMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
//        }
    }

}
