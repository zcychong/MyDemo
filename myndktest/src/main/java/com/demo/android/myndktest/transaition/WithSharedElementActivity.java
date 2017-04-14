package com.demo.android.myndktest.transaition;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.android.myndktest.R;

public class WithSharedElementActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_shared_element);

        init();
    }

    private void init(){
        textView = (TextView)findViewById(R.id.tv_share_1);
        imageView = (ImageView)findViewById(R.id.iv_share_1);

}
        }
