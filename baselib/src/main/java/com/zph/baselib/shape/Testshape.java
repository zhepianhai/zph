package com.zph.baselib.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.zph.baselib.R;
import com.zph.baselib.config.TestConfig;
import com.zph.baselib.utils.TextResourceReader;

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
    static float triangleCoords[] = {0.0f, 0.5f,   // top
            -0.5f, -0.5f,   // bottom left
            0.5f, -0.5f};   // bottom right

    private static final int POSITION_COMPONENT_COUNT = 3;
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

    public Testshape(Context context) {
        this.context = context;
        init();
        getProgram();
        //----------第三步: 获取这两个ID ，是通过前面定义的标签获得的
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        //---------第五步: 传入数据
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    public void draw(GL10 gl) {
//        GLE20.glDr

        //开启颜色渲染功能
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
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
