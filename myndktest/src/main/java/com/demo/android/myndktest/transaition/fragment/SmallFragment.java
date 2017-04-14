package com.demo.android.myndktest.transaition.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.demo.android.myndktest.R;

/**
 * Created by YHT on 2017/4/13.
 */

public class SmallFragment extends Fragment{
    private ImageView ivSmallCircle;

    public SmallFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_small, container, false);
        ivSmallCircle = (ImageView)view.findViewById(R.id.iv_small);
        ivSmallCircle.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                BigFragment bigFragment = new BigFragment();
                bigFragment.setSharedElementEnterTransition(new ChangeBounds());
                bigFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                getFragmentManager().beginTransaction()
                        .replace(R.id.fl_context,bigFragment)
                        .addToBackStack(null)
                        .addSharedElement(ivSmallCircle, "iv")
                        .commit();
            }
        });


        return view;
    }
}
