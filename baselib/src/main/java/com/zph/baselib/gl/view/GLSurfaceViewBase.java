package com.zph.baselib.gl.view;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zph.baselib.gl.render.RainISORenderer;
import com.zph.baselib.gl.utils.MatrixState;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
/**
 * Created by 3119 on 2017/9/28.
 */

public abstract class GLSurfaceViewBase extends GLSurfaceView implements SurfaceHolder.Callback, Runnable {
    protected EGLContext glContext;
    protected ViewAnimator animator;
    protected SurfaceHolder sHolder;
    protected Thread t;
    protected boolean running;
    int width;
    int height;
    boolean resize;
    int fps;




    public GLSurfaceViewBase(Context context) {
        this(context, -1);
    }

    public GLSurfaceViewBase(Context context, int fps) {
        super(context);
        sHolder = getHolder();
        sHolder.addCallback(this); // 添加回调
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU); // 一般这个方法不调用的，系统会设置默认surfaceview的值的
        this.fps = fps;
    }

    @Override
    protected void onAttachedToWindow() {
//		if (animator != null) {
//			// If we're animated, start the animation
//			animator.start();
//		}
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
//		if (animator != null) {
//			// If we're animated, stop the animation
//			animator.stop();
//		}
        super.onDetachedFromWindow();
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        synchronized (this) {
            this.width = width;
            this.height = height;
            this.resize = true;


//            float ratio = (float) width / height;
//            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
//            MatrixState.setCamera(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//            MatrixState.setInitStack();

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        running = false;
        try {
            t.join();
        }
        catch (InterruptedException ex) {}
        t = null;
    }

    @Override
    public void run() {
        EGL10 egl = (EGL10) EGLContext.getEGL(); // 获得EGL实例
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY); // 创建EGLDisplay

        int[] version = new int[2];
        egl.eglInitialize(dpy, version);
        // 每个 EGLDisplay 在使用前都需要初始化。初始化 EGLDisplay 的同时能够得到系统中 EGL 的实现版本号
        int[] configSpec = {
                EGL10.EGL_RED_SIZE,      5,
                EGL10.EGL_GREEN_SIZE,    6,
                EGL10.EGL_BLUE_SIZE,     5,
                EGL10.EGL_DEPTH_SIZE,   16,
                EGL10.EGL_NONE
        };

        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        egl.eglChooseConfig(dpy, configSpec, configs, 1, num_config);
        EGLConfig config = configs[0];

        EGLContext context = egl.eglCreateContext(dpy, config,
                EGL10.EGL_NO_CONTEXT, null);

        EGLSurface surface = egl.eglCreateWindowSurface(dpy, config, sHolder, null);
        egl.eglMakeCurrent(dpy, surface, surface, context);

        GL10 gl = (GL10)context.getGL();

        init(gl);

        int delta = -1;
        if (fps > 0) {
            delta = 1000/fps;
        }
        long time = System.currentTimeMillis();

        running = true;
        while (running) {
            int w, h;
            synchronized(this) {
                w = width;
                h = height;
            }
            if (System.currentTimeMillis()-time < delta) {
                try {
                    Thread.sleep(System.currentTimeMillis()-time);
                }
                catch (InterruptedException ex) {}
            }
            drawFrame(gl, w, h);
            egl.eglSwapBuffers(dpy, surface);

            if (egl.eglGetError() == EGL11.EGL_CONTEXT_LOST) {
                Context c = getContext();
                if (c instanceof Activity) {
                    ((Activity)c).finish();
                }
            }
            time = System.currentTimeMillis();
        }
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surface);
        egl.eglDestroyContext(dpy, context);
        egl.eglTerminate(dpy);
    }

    private void drawFrame(GL10 gl, int w, int h) {
        if (resize) {
            resize(gl, w, h);
            resize = false;
        }
        if(flags)
            drawFrame(gl);

//        render.onDrawFrame(gl);
    }

    protected void resize(GL10 gl, int w, int h) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glViewport(0,0,w,h);
        GLU.gluPerspective(gl, 45.0f, ((float)w)/h, 1f, 100f);
    }

    protected void init(GL10 gl) {
        flags=true;
    }
    public static boolean flags=false;
    protected abstract void drawFrame(GL10 gl);
}
