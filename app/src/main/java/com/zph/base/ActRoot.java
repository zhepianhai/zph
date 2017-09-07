package com.zph.base;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zph.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActRoot extends AppCompatActivity {
    public static final int NAVBTNRIGHT_TYPE_NOTITLE = 0;
    public static final int NAVBTNRIGHT_TYPE_HOME = 1;
    public static final int NAVBTNRIGHT_TYPE_TIME = 2;
    public static final int NAVBTNRIGHT_TYPE_DATA = 3;
    public static final int NAVBTNRIGHT_TYPE_LOC = 4;
    public static final int NAVBTNRIGHT_TYPE_HISTPH = 5;
    public static final int NAVBTNRIGHT_TYPE_MSGFILE = 6;
    public static final int NAVBTNRIGHT_TYPE_MEDIAUP = 7;

    @BindView(R.id.root_lay_show)
    protected LinearLayout mViewMain;

    @BindView(R.id.root_lay_show1)
    protected LinearLayout mViewMain1;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_root);
        ButterKnife.bind(this);

        initActionBar();

    }

    private void initActionBar() {
        mActionBar=getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setIcon(R.drawable.icoon_menu);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    public void setmActionBarType(int mActionBarType){
        if(mActionBarType==NAVBTNRIGHT_TYPE_NOTITLE){
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.hide();
        }
        else{
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
