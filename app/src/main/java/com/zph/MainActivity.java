package com.zph;

import android.app.Activity;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.zph.base.ActRootMap;
import com.zph.baselib.gl.render.AirHockeyRender;
import com.zph.baselib.gl.render.OpenGLRender;
import com.zph.baselib.gl.render.TestRender;
import com.zph.baselib.gl.utils.BaseGLESSeting;
import com.zph.baselib.utils.ZPHUtilsMaterialDesign;
import com.zph.view.ZPHMapTileView;

public class MainActivity extends Activity {
    private boolean rendererSet;
    private GLSurfaceView glv;
    private boolean isExist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setmActionBarType(NAVBTNRIGHT_TYPE_NOTITLE);
        glv = new GLSurfaceView(this);
        if (BaseGLESSeting.CheckSupposeGLES(this)) {
            this.rendererSet = true;
            glv.setEGLContextClientVersion(2); // Pick an OpenGL ES 2.0 context.
            glv.setRenderer(new AirHockeyRender(this));
            setContentView(glv);
//            mViewMain.addView(glv);
        } else {
            Toast.makeText(this, R.string.not_suppose_opengl, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isExist) {
//            Snackbar snackbar = Snackbar.make(mViewMain, R.string.exit_app, Snackbar.LENGTH_SHORT);
//            ZPHUtilsMaterialDesign.setSnackbarColor(snackbar, Color.BLACK, Color.WHITE);
//            isExist = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExist = false;
                }
            }, 1800);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.rendererSet) {
            this.glv.onResume();
        }
    }

    @Override

    protected void onPause() {
        super.onPause();
        if (this.rendererSet) {
            this.glv.onPause();
        }
    }
}
