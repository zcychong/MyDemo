package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.DrugEntity;
import com.healthmanage.ylis.model.DrugListResponse;
import com.healthmanage.ylis.model.DrugResEntity;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by YHT on 2016/12/22.
 */

public class SendDrugAddDrugActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.rl_time) RelativeLayout rlTime;
    @Bind(R.id.tv_dose_time) TextView tvTime;
    @Bind(R.id.sp_drug_list) Spinner spDrugList;
    @Bind(R.id.tv_drug_gg) TextView tvDrugGG;
    @Bind(R.id.tv_sum_quantity) TextView tvSumQuantity;
    @Bind(R.id.et_use_quantity) EditText etUseQuantity;

    @Bind(R.id.tv_sum_quantity_unit) TextView tvSumUnit;
    @Bind(R.id.tv_use_unit) TextView tvUseUnit;
    @Bind(R.id.tv_drug_usage) TextView tvDrugUsage;

    @Bind(R.id.tv_commit) TextView tvCommit;

    private int hours, mumnit;
    private String userId, shiftId, usrOrg, doseTime, elderlyId;
    private String drugId;
    private ArrayAdapter simpleAdapter;
    private List<Map<String, String>> drugList = new ArrayList<Map<String, String>>();
    private DrugEntity drugEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_drug_add_drug);

        context = this;

        initView();
        initData();

        getData();
    }

    private void initView() {
        ButterKnife.bind(context);

        llBack.setVisibility(View.VISIBLE);

        RxView.clicks(llBack)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(rlTime)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showTimeDialog();
                    }
                });


        RxView.clicks(tvCommit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        checkParam();

                    }
                });

    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");
        elderlyId = getIntent().getStringExtra("elderlyId");

        hours = Integer.valueOf(DateOperate.getCurrentDataHours());
        mumnit = Integer.valueOf(DateOperate.getCurrentDataMumnit());

        drugEntity = new DrugEntity();

        tvTitle.setText("添加发药药品");
    }

    private void checkParam(){
        if(StringUtils.isEmpty(drugId)){
            Toast.makeText(context, "请选择药品!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(StringUtils.isEmpty(doseTime)){
            Toast.makeText(context, "请选择服药时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugTime(doseTime);

        String use = etUseQuantity.getText().toString();
        if(StringUtils.isEmpty(use)){
            Toast.makeText(context, "请填写本次用量!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugUseQuantity(use);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("drug", drugEntity);
        intent.putExtra("drug", bundle);
        setResult(1,intent);
        finish();
    }

    private void showTimeDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                String time= "";
                if(hourOfDay < 10){
                    time = DateOperate.getCurrentDataWithSpe() + " 0" + hourOfDay;
                }else{
                    time = DateOperate.getCurrentDataWithSpe() + " " + hourOfDay;
                }

                if(minute < 10){
                    time = time + ":0" + minute;
                }else{
                    time = time + ":" + minute;
                }

                time = time + ":00";
                tvTime.setText(time);
                doseTime = time;
            }
        }, hours, mumnit, true);

        timePickerDialog.show();
    }

    private void getData(){
        if (Network.checkNet(context)) {
            getDrugList();
        }else{
            Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDrugList(){
        loading = LoadingDialog.loadingFind(context);
        loading.show();

        Map<String, String> map = new HashMap<String, String>();
        map.put("usrOrg", usrOrg);
        map.put("elderlyId", elderlyId);

//        map.put("pageIndex", "1");
//        map.put("pageSize", "10000");

        Subscriber<DrugListResponse> subscriber = new Subscriber<DrugListResponse>() {

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
            }

            @Override
            public void onNext(DrugListResponse response) {
                Log.e(TAG, "onNext");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                if(response != null){
                    if(response.isSuccess()){
                        dealDrugListData(response.getITEMS());
                    }else{
                        Log.e(TAG, "fail - " + response.getMessage());
                    }
                }else{
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getDrugList(subscriber, map);
    }

    private void dealDrugListData(final List<DrugResEntity> items){

        if(items != null){
            if(items.size() > 0){
                String datas[] = new String[items.size()];
                for(int i=0; i<items.size(); i++){
                    datas[i] = items.get(i).getYpmc();
                }
                simpleAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, datas);

                spDrugList.setAdapter(simpleAdapter);
                spDrugList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        drugId = items.get(position).getYpId();
                        tvDrugGG.setText(items.get(position).getYppc());
                        tvSumQuantity.setText(items.get(position).getSyypsl());
                        tvSumUnit.setText(items.get(position).getMcyydw());
                        tvUseUnit.setText(items.get(position).getMcyydw());
                        tvDrugUsage.setText(items.get(position).getType());
                        etUseQuantity.setText(items.get(position).getMcyl());
                        etUseQuantity.extendSelection(items.get(position).getMcyl().length());

                        drugEntity.setDrugId(drugId);
                        drugEntity.setDrugFrequency(items.get(position).getYppc());
                        drugEntity.setDrugName(items.get(position).getYpmc());
                        drugEntity.setDrugTime(doseTime);
                        drugEntity.setDrugUseQuantityUnit(items.get(position).getMcyydw());
                        drugEntity.setDrugSum(items.get(position).getSyypsl());
                        drugEntity.setDrugUseQuantity(items.get(position).getMcyl());
                        drugEntity.setDrugUsage(items.get(position).getType());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
