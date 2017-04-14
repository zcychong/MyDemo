package com.demo.android.myndktest.transaition.scene;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.android.myndktest.R;

public class ChangeFSEActivity extends AppCompatActivity {
    private Activity context;
    private AppCompatButton acbtnFade, acbtnSlide, acbtnExplode;
    private Scene scene1, scene2;
    private LinearLayout llGroup;
    private boolean isScene = false;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_fse);

        context = this;
        initView();
        initScene();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initScene() {
        scene1 = Scene.getSceneForLayout(llGroup, R.layout.change_fse_scene_1, context);
        scene2 = Scene.getSceneForLayout(llGroup, R.layout.change_fse_scene_2, context);

        TransitionManager.go(scene1);
    }

    private void initView(){
        llGroup = (LinearLayout)findViewById(R.id.ll_group);

        acbtnFade = (AppCompatButton)findViewById(R.id.acbtn_check_fade);
        acbtnFade.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                chengeFade();
            }
        });

        acbtnSlide = (AppCompatButton)findViewById(R.id.acbtn_slide);
        acbtnSlide.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                chengeSlide();
            }
        });

        acbtnExplode = (AppCompatButton)findViewById(R.id.acbtn_explode);
        acbtnExplode.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                chengeExplode();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void chengeSlide(){

        TransitionManager.go(scene2, new Slide());
        isScene = !isScene;

        Scene tempScene = scene1;
        scene1 = scene2;
        scene2 = tempScene;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void chengeExplode(){

        TransitionManager.go(scene2, new Explode());
        isScene = !isScene;

        Scene tempScene = scene1;
        scene1 = scene2;
        scene2 = tempScene;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void chengeFade(){

        TransitionManager.go(scene2, new Fade());
        isScene = !isScene;

        Scene tempScene = scene1;
        scene1 = scene2;
        scene2 = tempScene;
    }

}
