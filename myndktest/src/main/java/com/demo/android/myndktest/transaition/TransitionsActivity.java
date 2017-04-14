package com.demo.android.myndktest.transaition;

import android.app.Activity;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.android.myndktest.R;
import com.demo.android.myndktest.transaition.fragment.FragmentActivity;
import com.demo.android.myndktest.transaition.scene.ChangeBoundsActivity;
import com.demo.android.myndktest.transaition.scene.ChangeClipBoundsActivity;
import com.demo.android.myndktest.transaition.scene.ChangeFSEActivity;
import com.demo.android.myndktest.transaition.scene.ChangeImgTransformActivity;
import com.demo.android.myndktest.transaition.scene.ChangeTransformActivity;

public class TransitionsActivity extends AppCompatActivity {
    private Activity context;
    private AppCompatButton appCompatButtonChangeBounds, appCompatButtonChangeTransform;
    private AppCompatButton appCompatButtonChangeClipBounds, appCBtnTransitionXml;
    private AppCompatButton appCBtnChangeImageTransform, appCBtnChangeFSE;

    private AppCompatButton appFragmentTrans;

    private TextView tvSare;
    private ImageView ivShare;
    private CardView cvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitions);
        context = this;

        initView();
    }

    private void initView(){
        appCompatButtonChangeBounds = (AppCompatButton)findViewById(R.id.acbtn_change_bounds);
        appCompatButtonChangeBounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangeBoundsActivity.class));
            }
        });

        appCompatButtonChangeTransform = (AppCompatButton)findViewById(R.id.acbtn_change_transform);
        appCompatButtonChangeTransform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangeTransformActivity.class));
            }
        });

        appCompatButtonChangeClipBounds = (AppCompatButton)findViewById(R.id.acbtn_change_clip_transform);
        appCompatButtonChangeClipBounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangeClipBoundsActivity.class));
            }
        });

        appCBtnChangeImageTransform = (AppCompatButton)findViewById(R.id.acbtn_change_img_transform);
        appCBtnChangeImageTransform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangeImgTransformActivity.class));
            }
        });

        appCBtnChangeFSE= (AppCompatButton)findViewById(R.id.acbtn_change_fse);
        appCBtnChangeFSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangeFSEActivity.class));
            }
        });

        appCBtnTransitionXml= (AppCompatButton)findViewById(R.id.acbtn_xml_transform);
        appCBtnTransitionXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TransitionXMLActivity.class));
            }
        });

        appFragmentTrans = (AppCompatButton)findViewById(R.id.acbtn_fragment_transform);
        appFragmentTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FragmentActivity.class));
            }
        });

        tvSare = (TextView)findViewById(R.id.tv_share);
        ivShare = (ImageView)findViewById(R.id.iv_share);

        cvItem = (CardView)findViewById(R.id.cv_item);
        cvItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WithSharedElementActivity.class);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context, new Pair<View, String>(ivShare, "shared_iv")
                        ,new Pair<View, String>(tvSare, "shared_tv")
                );
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

    }
}
