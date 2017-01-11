package com.healthmanage.ylis.network.download;

import android.util.Log;

import java.io.IOException;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;

/**
 * Created by ljd on 4/12/16.
 */
public class ProgressHelper {

    private static ProgressBean progressBean = new ProgressBean();
    private static ProgressHandler mProgressHandler;

    public static OkHttpClient addProgress(OkHttpClient builder){

        if (builder == null){
            builder = new OkHttpClient();
        }

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                Log.d("progress:",String.format("%d%% done\n",(100 * progress) / total));
                if (mProgressHandler == null){
                    return;
                }

                progressBean.setBytesRead(progress);
                progressBean.setContentLength(total);
                progressBean.setDone(done);
                mProgressHandler.sendMessage(progressBean);

            }
        };

        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
            	Log.e("ylis", chain.toString());
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });

        return builder;
    }

    public static void setProgressHandler(ProgressHandler progressHandler){
        mProgressHandler = progressHandler;
    }
}
