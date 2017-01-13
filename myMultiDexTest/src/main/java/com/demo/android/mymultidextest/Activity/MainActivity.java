package com.demo.android.mymultidextest.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.demo.android.mymultidextest.NewPro.NewFunction;
import com.demo.android.mymultidextest.R;

public class MainActivity extends AppCompatActivity {
    private Activity context;
    private AppCompatButton btnBegin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();


    }

    private void initView() {
        btnBegin = (AppCompatButton)findViewById(R.id.btn_begin);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                measure();
            }
        });
    }

    public void measure(){
        NewFunction newFunction = new NewFunction(context);
        newFunction.func();
    }
}
