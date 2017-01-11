package com.demo.android.mydemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.demo.android.mydemo.utils.MeasureUtils;
import com.demo.android.mydemo.utils.StringUtils;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private AppCompatButton btnCheck;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        btnCheck = (AppCompatButton)findViewById(R.id.btn_check);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, MeasureUtils.measure(10,0), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
