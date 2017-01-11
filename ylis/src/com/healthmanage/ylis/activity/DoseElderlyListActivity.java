package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.UserRoomItemView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by YHT on 2017/1/4.
 */

public class DoseElderlyListActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.ll_back) LinearLayout llBack;
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackTitle;
    @Bind(R.id.ll_rooms) LinearLayout llRooms;

    @Bind(R.id.ll_no_value) LinearLayout llNoValue;

    private String userId;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dose_elderly);
        context = this;

        initView();
        initData();
        getDoseList();

//        IntentFilter intentfilter = new IntentFilter();
//        intentfilter.addAction("ylis.dose.finish");
//        registerReceiver(doseReceiver, intentfilter);
    }

    private void initView(){
        ButterKnife.bind(this);

        llBack.setVisibility(View.VISIBLE);
        RxView.clicks(llBack).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.putExtra("count", String.valueOf(count));
                        intent.setAction("ylis.dose");
                        sendBroadcast(intent);
                        finish();
                    }
                });
        tvTitle.setText("协助服药");
    }

    private void initData(){
        userId = getIntent().getStringExtra("userId");
    }

    private void getDoseList(){
        if (llRooms.getChildCount() > 0) {
            llRooms.removeAllViews();
        }
        count = 0;
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("userId", userId);
        Subscriber<TastListResponse> subscriber = new Subscriber<TastListResponse>() {

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(final TastListResponse taseList) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, "onNext");
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        dealResponseData(taseList);
                    } else {
                        llNoValue.setVisibility(View.VISIBLE);
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getElderlySendDrugInfo(subscriber, map);
    }

    private void dealResponseData(final TastListResponse taseList) {

        if (taseList != null) {
            if (taseList.isSuccess()) {
                // tvTitle.setText(taseList.getOrgName());
                for(int i=0;i<taseList.getITEMS().size(); i++){
                    if(taseList.getITEMS().get(i).getBeds().size() != 0){
                        count += taseList.getITEMS().get(i).getBeds().size();
                    }
                }
                for (int i = 0; i < taseList.getITEMS().size(); i++) {
                    final int index = i;
                    UserRoomItemView roomView = new UserRoomItemView(context,
                            taseList.getITEMS().get(i));
                    llRooms.addView(roomView);
                    roomView.getGvRoomList().setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int position, long arg3) {

                                    Intent intent = new Intent(context, DoseDetailActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("elderlyId", taseList.getITEMS().get(index).getBeds().get(position).getElderlyId());
                                    intent.putExtra("elderlyName", taseList.getITEMS().get(index).getBeds().get(position).getElderlyName());
                                    startActivityForResult(intent, 1);
                                }
                            });
                }

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                getDoseList();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("count", String.valueOf(count));
            intent.setAction("ylis.dose");
            sendBroadcast(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

//    private BroadcastReceiver doseReceiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e(TAG, "onReceive");
//            if(intent != null){
//                if(intent.getAction().equals("ylis.dose.finish")){
//                    finish();
//                }
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
