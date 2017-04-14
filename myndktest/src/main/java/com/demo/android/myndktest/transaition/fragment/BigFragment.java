package com.demo.android.myndktest.transaition.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.android.myndktest.R;

/**
 * Created by YHT on 2017/4/13.
 */

public class BigFragment extends Fragment {

    public BigFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_big, container, false);

        return view;
    }

}
