package com.healthmanage.ylis.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.model.GetMaybeTaskResponse;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

/**
 * Created by YHT on 2017/1/4.
 */

public class FindDoseService extends Service {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private Timer timer;
    private SharedPreferences userInfo;
    private String userId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        userInfo =  (SharedPreferences) getSharedPreferences(context
                        .getResources().getString(R.string.apk_name),
                android.content.Context.MODE_PRIVATE);
        userId = userInfo.getString("userId", "");
        checkDoseInfo();

    }

    private void checkDoseInfo(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getDoseInfo();
            }
        };
        timer.schedule(timerTask, 0, 60000);

    }

    private void getDoseInfo(){
        Log.e("service", "getDoseInfo");
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        Log.e("userId", userId);
        Subscriber<TastListResponse> subscriber = new Subscriber<TastListResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(TastListResponse taseList) {
                Log.e("service", "onNext");
                int count = 0;
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        for(int i=0;i<taseList.getITEMS().size(); i++){
                            if(taseList.getITEMS().get(i).getBeds().size() != 0){
                                count += taseList.getITEMS().get(i).getBeds().size();
                            }
                        }

                        Intent intent = new Intent();
                        intent.setAction("ylis.dose");
                        intent.putExtra("count", String.valueOf(count));
                        sendBroadcast(intent);
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getElderlySendDrugInfo(subscriber, map);

    }

    public class ServiceBinder extends Binder {
        public FindDoseService getService() {
            return FindDoseService.this;
        }
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
