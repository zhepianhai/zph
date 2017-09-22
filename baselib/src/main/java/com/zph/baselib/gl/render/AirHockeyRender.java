package com.zph.baselib.gl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.zph.baselib.gl.shape.AirHockeyShape;
import com.zph.baselib.gl.shape.AirHockeyShape1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zph on 2017/9/19.
 */

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private Context context;
    AirHockeyShape1 airHockeyShape;
    public AirHockeyRender(Context context){
        this.context=context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
//        gl10.glClearColor(0f,0f,0f,0f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0f,0f, 0f,0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        airHockeyShape=new AirHockeyShape1(context);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        //
        airHockeyShape.addMatrix(gl,width,height);
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f,
                (float) width / (float) height,
                0.1f, 100.0f);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset the modelview matrix
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        airHockeyShape.draw(gl);
    }
}
