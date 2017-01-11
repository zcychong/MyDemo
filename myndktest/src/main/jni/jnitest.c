//
// Created by YHT on 2017/1/6.
//
#include "com_demo_android_myndktest_NdkJniUtils.h"

JNIEXPORT jstring JNICALL Java_com_demo_android_myndktest_NdkJniUtils_getCLanguaeString
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "This is Jni test!!!");
}