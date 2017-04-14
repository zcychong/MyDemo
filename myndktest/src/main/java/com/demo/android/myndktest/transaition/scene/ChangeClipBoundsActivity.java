package com.demo.android.myndktest.transaition.scene;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.android.myndktest.R;

import java.util.zip.Inflater;

public class ChangeClipBoundsActivity extends AppCompatActivity {
    private Activity context;
    private AppCompatButton appCompatButtonOne;
    private Scene scene1, scene2;
    private LinearLayout llGroup;
    private boolean isScene = false;
    private View inflater1, inflater2;
    private ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_clip_bounds);

        context = this;
        initView();
        initScene();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initScene() {

        inflater1 = LayoutInflater.from(context).inflate(R.layout.change_clip_bounds_scene_1, null);
        inflater2 = LayoutInflater.from(context).inflate(R.layout.change_clip_bounds_scene_2, null);

        imageView1 = (ImageView)inflater1.findViewById(R.id.iv_scene_one);
        imageView2 = (ImageView)inflater2.findViewById(R.id.iv_scene_one);

        imageView1.setClipBounds(new Rect(100,100,300,300));
        imageView2.setClipBounds(new Rect(0,0,400,400));

        scene1 = new Scene(llGroup, inflater1);
        scene2 = new Scene(llGroup, inflater2);

        TransitionManager.go(scene1);
    }

    private void initView(){
        llGroup = (LinearLayout)findViewById(R.id.ll_group);

        appCompatButtonOne = (AppCompatButton)findViewById(R.id.acbtn_check);

        appCompatButtonOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                chenge();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void chenge(){
        TransitionManager.go(scene2, new ChangeClipBounds());
        isScene = !isScene;

        Scene tempScene = scene1;
        scene1 = scene2;
        scene2 = tempScene;
    }
}
