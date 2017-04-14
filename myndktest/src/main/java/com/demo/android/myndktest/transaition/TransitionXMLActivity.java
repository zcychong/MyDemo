package com.demo.android.myndktest.transaition;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.demo.android.myndktest.R;

public class TransitionXMLActivity extends AppCompatActivity implements View.OnClickListener{
    private Activity context;
    private AppCompatButton appCompatButtonOne;
    private Scene scene1, scene2;
    private RelativeLayout rlGroup;
    private boolean isScene = false;
    private ImageView ivOne, ivTwo, ivThree, ivFour;
    private int primarySize;
    private boolean isBagger = false;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_xml);
        context = this;

        initView();
//        initScene();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initScene() {
        scene1 = Scene.getSceneForLayout(rlGroup, R.layout.change_bounds_scene_1, context);
        scene2 = Scene.getSceneForLayout(rlGroup, R.layout.change_bounds_scene_2, context);

        TransitionManager.go(scene1);
    }

    private void initView(){

        rlGroup = (RelativeLayout)findViewById(R.id.rl_group);

        ivOne = (ImageView)findViewById(R.id.iv_one);
        ivTwo = (ImageView)findViewById(R.id.iv_two);
        ivThree = (ImageView)findViewById(R.id.iv_three);
        ivFour = (ImageView)findViewById(R.id.iv_four);

        ivOne.setOnClickListener(this);
        ivTwo.setOnClickListener(this);
        ivThree.setOnClickListener(this);
        ivFour.setOnClickListener(this);

        primarySize = ivOne.getLayoutParams().width;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        chenge(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void chenge(View view){
//        TransitionSet trnas = new TransitionSet();
//        trnas.addTransition(new Fade(Fade.OUT));
//        trnas.addTransition(new ChangeBounds());
//        trnas.addTransition(new Fade(Fade.IN));

//        TransitionManager.go(scene2, new ChangeBounds());

        TransitionManager.beginDelayedTransition(rlGroup, TransitionInflater.from(context).inflateTransition(R.transition.changeboundsandfade));
        changeScene(view);

    }

    private void changeScene(View view) {

        changeSize(view);

        changeVisibility(ivOne, ivTwo, ivThree, ivFour);
        view.setVisibility(View.VISIBLE);

        isBagger = !isBagger;
    }

    private void changeSize(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(isBagger){
            layoutParams.width = primarySize;
            layoutParams.height = primarySize;
        }else{
            layoutParams.width = (int)(1.5*primarySize);
            layoutParams.height = (int)(1.5*primarySize);

        }
        view.setLayoutParams(layoutParams);
    }


    private void changeVisibility(View ...views){
        for (View view:views){
            view.setVisibility(view.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
        }
    }


}
