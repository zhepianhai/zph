package com.zph.baselib.gl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zph.baselib.gl.render.RainISORenderer;
import com.zph.baselib.gl.shape.TriPrism;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/28.
 */

public class OpenGLView extends GLSurfaceViewBase{
    float lightAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f }; // 环境光、阴影
    float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f }; // 扩散漫射光、漫反射光
    float[] lightPos = new float[] {0,0,3,1}; // 光源位置

    float matAmbient[] = new float[] { 1f, 1f, 1f, 1.0f };
    float matDiffuse[] = new float[] { 1f, 1f, 1f, 1.0f };


    public OpenGLView(Context context) {
        super(context, 20); // 第二个参数是 帧每秒
    }

    public OpenGLView(Context context, int fps) {
      super(context,20);
    }

    @Override
    protected void init(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHTING);
		/* 使用我们当前设定的光照参数计算每个点的颜色---(如果去掉这句，那么默认的白色漫反射光源会代替我们之前设定好的光源，
		 * 如果我们是画的一个单独的立体球的话，你将看见一个白色的没有立体感的球)*/
        gl.glEnable(GL10.GL_LIGHT0); // 启用GL_LIGHT0光源---GL_LIGHT0为光源的名称，是第一个光源
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		/* glMaterialfv设置物体的材质，也就是物体的颜色
		 * 参数一：当前材质映射到哪个面上(OPENGL目前只支持前后)；参数二：材质的属性(这边指定使用材质的环境反射光)，用第三个参数来具体设置
		 * 参数三：包括了实际属性或元素的GL_FLOAT数组的指针*/
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
        //GL_DIFFUSE---漫反射光

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient,	0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse,	0);
        // 设置光源属性---参数一：光源名称；参数二：光源类型；参数三：具体光源参数；参数四：偏移量
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
        // 设置光源属性---参数一：光源名称；参数二：说明一下后面指定的是光源的位置；参数三：光源位置

        gl.glEnable(GL10.GL_BLEND); // 启用融合处理
//		gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
//
//		gl.glEnable(GL10.GL_TEXTURE_2D); // 启用二维处理
//
//		gl.glShadeModel(GL10.GL_SMOOTH); // 阴影使用平滑处理模式
//		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // 黑色刷屏
//		gl.glClearDepthf(1.0f); // 深度基值


        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // 清除颜色和深度缓存
        gl.glMatrixMode(GL10.GL_MODELVIEW); // 把当前矩阵转换为指定的矩阵类型(这边是转换成视点坐标系)
        gl.glLoadIdentity();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // 启用顶点数组
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // 启用纹理数组
        gl.glEnable(GL10.GL_TEXTURE_2D); // 启用二维处理
        gl.glShadeModel(GL10.GL_SMOOTH); // 阴影使用平滑处理模式
//        gl.glClearColor(0.3f, 0.4f, 0.5f, 0.5f); // 刷屏颜色RGBA设置
        gl.glClearColor(0,0,0,0);
        gl.glClearDepthf(1.0f); //深度基值
        gl.glEnable(GL10.GL_DEPTH_TEST); // 开启深度测试
        gl.glDepthFunc(GL10.GL_LEQUAL); // 小于或等于深度基值的点会被绘制
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);


    }
    float xrot = 0.0f;
    float yrot = 0.0f; // 程序运行开始时x、y方向的旋转角度
    @Override
    protected void drawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // 清除颜色和深度缓存
        gl.glMatrixMode(GL10.GL_MODELVIEW); // 把当前矩阵转换为指定的矩阵类型(这边是转换成视点坐标系)
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);
		/* 后面9个参数分别是眼睛的位置，眼睛朝向的位置(反的？？？)，以及相片朝上的方向(都是xyz)*/
        gl.glRotatef(xrot, 1, 0, 0);
        gl.glRotatef(yrot, 0, 1, 0);
        // 以上三句让物体旋转一个角度(也只是程序运行最初的角度，后面动画中的角度跟这个无关的啦～～～)


//        for(int i = 0; i < 6; i++) {
//            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff[i]);
//            // 指定数组顶点用glVertexPointer，指定完了就可以用glDrawArray()来把指定的数组中的顶点绘制出来了
//            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//        }
    }
        float oldx,oldy;
        int flag;
        public boolean onTouchEvent(final MotionEvent event) { // 处理屏幕点击事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) { // 当按下屏幕时---获取点击的x、y坐标
                oldx = event.getX();
                oldy = event.getY();
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE) { // 当在屏幕上移动时---根据移动的距离改变旋转角度
                xrot -= (oldy - event.getY()); // 为什么x轴对应于y方向的移动是因为绕x轴转动其实是y方向的旋转   下同
                yrot -= (oldx - event.getX());
                oldx = event.getX();
                oldy = event.getY();
            }
            return true;
        }



    }
