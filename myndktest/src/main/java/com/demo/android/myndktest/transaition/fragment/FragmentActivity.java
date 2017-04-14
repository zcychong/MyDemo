package com.demo.android.myndktest.transaition.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;

import com.demo.android.myndktest.R;

public class FragmentActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        SmallFragment smallFragment = new SmallFragment();
        smallFragment.setExitTransition(new Slide());
        getSupportFragmentManager().beginTransaction().add(R.id.fl_context, smallFragment).commit();

    }
}
