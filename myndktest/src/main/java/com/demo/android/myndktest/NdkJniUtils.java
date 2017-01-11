package com.demo.android.myndktest;

/**
 * Created by YHT on 2017/1/6.
 */

public class NdkJniUtils {
    static{
        System.loadLibrary("emJniLibName");
    }

    public native String getCLanguaeString();
}
