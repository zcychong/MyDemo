package com.demo.android.myndktest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.Scene;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Activity context;
    private TextView tvText;
    private AppCompatButton btnBegin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();

    }

    private void initView(){
        tvText = (TextView)findViewById(R.id.tv_text);
        btnBegin = (AppCompatButton)findViewById(R.id.btn_begin);

//        final
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NdkJniUtils jniUtils = new NdkJniUtils();
                String text = jniUtils.getCLanguaeString();
                tvText.setText(text);
            }
        });


    }

}
