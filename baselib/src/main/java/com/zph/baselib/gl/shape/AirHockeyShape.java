package com.zph.baselib.gl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.zph.baselib.R;
import com.zph.baselib.gl.config.TestConfig;
import com.zph.baselib.gl.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/19.
 */

public class AirHockeyShape {
    private Context context;
    private String vertexShaderSource,fragmentShaderSource;
    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private FloatBuffer vertexBuffer;
    private static final int BYTES_PER_FLOAT = 4;//字节数
    static final int POSITION_COMPONENT_COUNT = 2;//每个顶点的个数
    //颜色线性插值的参数
    private static final int COLOR_COMPONENT_COUNT=3;
    private static final String A_COLOR = "a_Color";
    //用来划分位置值和颜色值的（跨距）知道颜色直接字节数间距，读取时候可以跳过
    private static final int STRIDE=(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
    private int aColorLocation;

    private float tableVertexDate[]={
            0f,0f,1.0f,1.0f,1.0f,
            -0.5f,-0.5f,0.7f,0.7f,0.7f,
            0.5f,-0.5f,0.7f,0.7f,0.7f,
            //triangle2
            0.5f,0.5f,0.7f,0.7f,0.7f,
            -0.5f,0.5f,0.7f,0.7f,0.7f,
            -0.5f,-0.5f,0.7f,0.7f,0.7f,

            //line
            -0.5f,0f,1f,0f,0f,
            0.5f,0f,1f,0f,0f,
            //point
            0f,-0.25f,0f,0f,1f,
            0f,0.25f,1f,0f,0f,


    };

    public AirHockeyShape(Context context){
        this.context=context;
        initVar();
        initParame();
    }

    private void initParame() {
        //获取顶点着色器文本
        vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_vertex_shader);
//        .readTextFileFromResource(context, R.raw.simple_verteex_shaer1);
        //获取片段着色器文本
        fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_fragment_shader);

        //绑定(将顶点和颜色片段获取ID 然后绑定，返回一个新的ID)
        program = TestConfig.buildProgram(vertexShaderSource, fragmentShaderSource);
        //给定的程序对象是将用于使用程序当前事件，
        // 不加的话真机不会出现绘制内容，
        // 模拟器会出现颜色uColorLocation不正常，出现布局叠加，默认白色
        GLES20.glUseProgram(program);
        Log.i("TAG","program"+program);
        //获取Uniform位置(Color)返回-1代表错误，0是有效值
//        uColorLocation= GLES20.glGetUniformLocation(program, U_COLOR);
//        获取颜色位置(Color)返回-1代表错误，0是有效值
        aColorLocation=GLES20.glGetAttribLocation(program,A_COLOR);
        Log.i("TAG","aColorLocation"+aColorLocation);
        //获取属性的位置(Position)aPositionLocation返回-1代表错误，0是有效值
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        Log.i("TAG","aPositionLocation"+aPositionLocation);
        //传入数据 没有跨距的（颜色跨距）
//        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT, false, 0, this.vertexBuffer);
        //传入数据，带有颜色跨距的
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE,this.vertexBuffer);
        //将数据和颜色着色器关联
        vertexBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, this.vertexBuffer);

        //解析数据
        GLES20.glEnableVertexAttribArray(aColorLocation);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    private void initVar() {
        vertexBuffer = ByteBuffer
                .allocateDirect(tableVertexDate.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(tableVertexDate);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);
    }


    public void draw(GL10 gl){
//        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        //和颜色器关联的时候
        //draw triangle
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 1.0f,1.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
//        gl.glFinish();
        //draw line
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
        gl.glDrawArrays(GLES20.GL_LINES,6,2);
//        gl.glFinish();
        //draw point
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GLES20.GL_POINTS,8,1);
//        gl.glFinish();
        //draw point
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GLES20.GL_POINTS,9,1);
//        gl.glFinish();



//        //没有和颜色器进行关联的时候
//        //draw triangle
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 1.0f,1.0f, 1.0f, 1.0f);
//        gl.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
//        gl.glFinish();
//        //draw line
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
//        gl.glDrawArrays(GLES20.GL_LINES,6,2);
//        gl.glFinish();
//        //draw point
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
//        gl.glDrawArrays(GLES20.GL_POINTS,8,1);
//        gl.glFinish();
//        //draw point
//        gl.glLoadIdentity();
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        gl.glDrawArrays(GLES20.GL_POINTS,9,1);
//        gl.glFinish();
    }

}
