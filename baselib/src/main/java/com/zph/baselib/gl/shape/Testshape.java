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
 * Created by zph on 2017/9/11.
 */

public class Testshape {
    private Context context;
    // 数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 2;
    // 每个顶点的坐标数     X ,  Y
    static float triangleCoords[] = {
            //两个三角形和三角形的颜色分量
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

//两条直线和直线的颜色分量
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

//两个顶点和顶点的颜色分量
            0f, -0.25f, 0f, 0f, 1f,
            0f, 0.25f, 1f, 0f, 0f
    };
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexBuffer;
    private int program;

    //定义两个标签，分别于着色器代码中的变量名相同,
    // 第一个是顶点着色器的变量名，第二个是片段着色器的变量名
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";

    //------------第二步: 定义两个ID,我们就是通ID来实现数据的传递的,这个与前面
    //------------获得program的ID的含义类似的
    private int uColorLocation;
    private int aPositionLocation;


    private static final int COLOR_COMPONENT_COUNT=3;

    private static final String A_COLOR = "a_Color";

    private int aColorLocation;

    private static final int STRIDE=(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;



    public Testshape(Context context) {
        this.context = context;
        init();
        getProgram();
        //----------第三步: 获取这两个ID ，是通过前面定义的标签获得的
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
//        vertexBuffer.position(0);


        //---------第五步: 传入数据
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //解析数据
        GLES20.glEnableVertexAttribArray(aPositionLocation);

//        vertexBuffer.position(POSITION_COMPONENT_COUNT);
//        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, this.vertexBuffer);
//        GLES20.glEnableVertexAttribArray(aColorLocation);

    }

    public void draw(GL10 gl) {
        //开启颜色渲染功能
//        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//        GLES20.glUseProgram(program);
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
//        告诉OpenGL绘制三角扇形
        Log.i("TAG","draw1");
        gl.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
        //告诉OpenGL绘制直线
        gl.glDrawArrays(GLES20.GL_LINES, 6, 2);
        //告诉OpenGL绘制点
        gl.glDrawArrays(GLES20.GL_POINTS, 8, 1);
        //告诉OpenGL绘制点
        gl.glDrawArrays(GLES20.GL_POINTS, 9, 1);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
        Log.i("TAG","draw2");


    }

    private void getProgram() {
        //获取顶点着色器文本
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_fragment_shader);
        //获取program的id
        program = TestConfig.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(program);

    }

    private void init() {
        vertexBuffer = ByteBuffer
                .allocateDirect(triangleCoords.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(triangleCoords);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);
    }


}
