package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;

public class FcilitiesExceptionActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;

    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.tv_repair_goods_name) TextView tvRepairGoodsName;
    @Bind(R.id.tv_repair_goods_position) TextView tvRepairGoodsPosition;
    @Bind(R.id.tv_repair_goods_sattus) TextView tvRepairGoodsSattus;
    @Bind(R.id.tv_repair_goods_recive_people) TextView tvRepairGoodsRecivePeople;

    @Bind(R.id.et_repair_goods_name) EditText etRepairGoodsName;
    @Bind(R.id.et_repair_goods_position) EditText etRepairGoodsPosition;
    @Bind(R.id.et_repair_goods_sattus) EditText etRepairGoodsSattus;
    @Bind(R.id.et_repair_goods_recive_people) EditText etRepairGoodsRecivePeople;

    @Bind(R.id.tv_commit) TextView tvCommit;

    private String userId, shiftId, usrOrg;
    private String fcilitiesName, fcilitiesPosition, fcilitiesInfo, fcilitiesReciveName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcilities_exception);
        context = this;

        initView();
        initData();
    }

    private void initView() {
        ButterKnife.bind(context);

        llBack.setVisibility(View.VISIBLE);
        RxView.clicks(llBack).
                throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(tvCommit).
                throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        commitDate();
                    }
                });
    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");

        tvTitle.setText("本区域公共设施报修");
    }

    private void commitDate(){
        if (Network.checkNet(context)) {
            fcilitiesName = etRepairGoodsName.getText().toString();
            fcilitiesPosition = etRepairGoodsPosition.getText().toString();
            fcilitiesInfo = etRepairGoodsSattus.getText().toString();
            fcilitiesReciveName = etRepairGoodsRecivePeople.getText().toString();
            if(StringUtils.isEmpty(fcilitiesName)){
                Toast.makeText(context, "请输入报修物品名称!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(StringUtils.isEmpty(fcilitiesPosition)){
                Toast.makeText(context, "请输入报修物品位置!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(StringUtils.isEmpty(fcilitiesInfo)){
                Toast.makeText(context, "请输入报修物品目前情况!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(StringUtils.isEmpty(fcilitiesReciveName)){
                Toast.makeText(context, "请输入接报人!", Toast.LENGTH_SHORT).show();
                return;
            }
            updateFcilitiesException();
        }else{
            Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFcilitiesException(){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("bxName", fcilitiesName);
        map.put("bxWz", fcilitiesPosition);
        map.put("bxQk", fcilitiesInfo);
        map.put("jbr", fcilitiesReciveName);
        map.put("remark", "");
        map.put("userId", userId);
        map.put("shiftId", shiftId);

        Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onNext(BaseResponseEntity taseList) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, "onNext");
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        Intent intent = new Intent();
                        intent.putExtra("name", fcilitiesName);
                        setResult(2,intent);
                        finish();
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().updateAreaFcilitiesException(subscriber, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
