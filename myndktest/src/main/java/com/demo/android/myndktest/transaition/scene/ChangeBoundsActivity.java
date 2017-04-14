package com.demo.android.myndktest.transaition.scene;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.android.myndktest.R;

public class ChangeBoundsActivity extends AppCompatActivity {
    private Activity context;
    private AppCompatButton appCompatButtonOne;
    private Scene scene1, scene2;
    private LinearLayout llGroup;
    private boolean isScene = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bounds);
        context = this;

        initView();
        initScene();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initScene() {
        scene1 = Scene.getSceneForLayout(llGroup, R.layout.change_bounds_scene_1, context);
        scene2 = Scene.getSceneForLayout(llGroup, R.layout.change_bounds_scene_2, context);

        TransitionManager.go(scene1);
    }

    private void initView(){
        llGroup = (LinearLayout)findViewById(R.id.ll_group);
        appCompatButtonOne = (AppCompatButton)findViewById(R.id.acbtn_check);

        appCompatButtonOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                chenge();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void chenge(){
        TransitionManager.go(scene2, new ChangeBounds());
        isScene = !isScene;

        Scene tempScene = scene1;
        scene1 = scene2;
        scene2 = tempScene;
    }
}
