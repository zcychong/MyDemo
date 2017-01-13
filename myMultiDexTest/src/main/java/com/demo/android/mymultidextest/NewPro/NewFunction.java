package com.demo.android.mymultidextest.NewPro;

import android.app.Activity;
import android.widget.Toast;

import com.demo.android.mymultidextest.Utils.MeasureTuils;

/**
 * Created by YHT on 2017/1/11.
 */

public class NewFunction {
    private Activity activity;
    public NewFunction(Activity context){
        activity = context;
    }

    public void func(){
        Toast.makeText(activity, MeasureTuils.measure(10,1), Toast.LENGTH_SHORT).show();
    }

}
