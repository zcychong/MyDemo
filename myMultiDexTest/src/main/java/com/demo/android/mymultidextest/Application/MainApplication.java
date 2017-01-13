package com.demo.android.mymultidextest.Application;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by YHT on 2017/1/13.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Application init...", Toast.LENGTH_SHORT).show();
        String dex2 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "classes2.dex";
        Log.e("yubo", "dex2 path: " + dex2);
        inject(dex2);
    }

    public String inject(String libPath) {
        boolean hasBaseDexClassLoader = true;
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
        } catch (ClassNotFoundException e) {
            hasBaseDexClassLoader = false;
        }
        if(hasBaseDexClassLoader){
            PathClassLoader pathClassLoader = (PathClassLoader)getClassLoader();
            DexClassLoader dexClassLoader = new DexClassLoader(libPath, getDir("dex", 0).getAbsolutePath(), libPath, getClassLoader());
            try{
                Object dexElements = combineArray(getDexElements(getPathList(pathClassLoader)), getDexElements(getPathList(dexClassLoader)));
                Object pathList = getPathList(pathClassLoader);
                setField(pathList, pathList.getClass(), "dexElements", dexElements);
                return "SUCCESS";
            } catch (Throwable e) {
                e.printStackTrace();
                return android.util.Log.getStackTraceString(e);
            }
        }
        return "SUCCESS";
    }

    public Object getPathList(Object baseDexClassLoader)
        throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
            return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    public Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static void setField(Object obj, Class<?> cl, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    public Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }

    public static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }
}
