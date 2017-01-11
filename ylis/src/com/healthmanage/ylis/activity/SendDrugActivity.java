package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.DrugEntity;
import com.healthmanage.ylis.model.TastListResponse;
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

public class SendDrugActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.rl_add_drug) RelativeLayout rlAddFrug;
    @Bind(R.id.lv_drug_list) ListView lvDrugList;

    @Bind(R.id.tv_elderly_name) TextView tvElderlyName;
    @Bind(R.id.tv_room_info) TextView tvRoomInfo;
    @Bind(R.id.tv_commit) TextView tvCommit;

    private String userId, shiftId, usrOrg, elderlyName, elderlyId, elderlyRoom, elderlyBed;
    private List<DrugEntity> drugEntityList = new ArrayList<DrugEntity>();
    private DrugAdapter drugAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_drug);

        context = this;

        initView();
        initData();
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

        RxView.clicks(rlAddFrug).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityForResult(new Intent(context, SendDrugAddDrugActivity.class)
                                .putExtra("elderlyId", elderlyId), 1);
                    }
                });

        RxView.clicks(tvCommit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        checkDate();
                    }
                });


    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");

        elderlyName = getIntent().getStringExtra("elderlyName");
        elderlyId = getIntent().getStringExtra("elderlyId");
        elderlyRoom = getIntent().getStringExtra("elderlyRoom");
        elderlyBed = getIntent().getStringExtra("elderlyBed");

        tvTitle.setText("发放药品");
        tvElderlyName.setText(elderlyName);
        tvRoomInfo.setText(elderlyRoom + "房间 " + elderlyBed + "号床");
    }

    private void checkDate(){
        if(drugEntityList.size() == 0){
            Toast.makeText(context, "请添加发药药品!", Toast.LENGTH_SHORT).show();
            return;
        }

        update();
    }

    private void update(){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("elderlyId", elderlyId);
        map.put("fyr", userId);
        map.put("usrOrg", usrOrg);
        String ypList = getDrugsInfo();
        map.put("ypList", ypList);

        Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

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
            public void onNext(final BaseResponseEntity taseList) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, "onNext");
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        Toast.makeText(context, "发药完成!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().sendFrugsInfo(subscriber, map);
    }

    private String getDrugsInfo(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<drugEntityList.size(); i++){
            sb.append(drugEntityList.get(i).getDrugId() + "#");
            sb.append(drugEntityList.get(i).getDrugTime() + "#");
            sb.append(drugEntityList.get(i).getDrugUseQuantity() + "#");
            sb.append(drugEntityList.get(i).getDrugUsage() + "$");
        }

        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            return;
        }

        if(requestCode == 1){
            if(resultCode == 1){
                Bundle bundle = data.getBundleExtra("drug");
                DrugEntity drugEntity = bundle.getParcelable("drug");
                drugEntityList.add(drugEntity);
                if(drugAdapter == null){
                    drugAdapter = new DrugAdapter(context, drugEntityList);
                    lvDrugList.setAdapter(drugAdapter);
                }else{
                    drugAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    class DrugAdapter extends BaseAdapter {
        private List<DrugEntity> drugItems;
        private Context mContext;

        public DrugAdapter(Context context, List<DrugEntity> list) {
            mContext = context;
            drugItems = list;
        }

        @Override
        public int getCount() {
            return drugItems.size();
        }

        @Override
        public Object getItem(int position) {
            return drugItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.send_drug_item, null);
                viewHolder = new ViewHolder();

                viewHolder.drugName = (TextView) convertView.findViewById(R.id.tv_drug_name);
                viewHolder.doseTime = (TextView) convertView.findViewById(R.id.tv_drug_time);
                viewHolder.drugFrequency = (TextView) convertView.findViewById(R.id.tv_drug_pc);
                viewHolder.drugSum = (TextView) convertView.findViewById(R.id.tv_drug_leave_num);
                viewHolder.drugUseQuantity = (TextView) convertView.findViewById(R.id.tv_use_quantity);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DrugEntity item = drugItems.get(position);
            if(StringUtils.isNotEmpty(item.getDrugName())){
                viewHolder.drugName.setText(item.getDrugName());
            }else{
                viewHolder.drugName.setText("");
            }

            if(StringUtils.isNotEmpty(item.getDrugTime())){
                viewHolder.doseTime.setText(item.getDrugTime());
            }else{
                viewHolder.doseTime.setText("");
            }

            if(StringUtils.isNotEmpty(item.getDrugFrequency())){
                viewHolder.drugFrequency.setText(item.getDrugFrequency());
            }else{
                viewHolder.drugFrequency.setText("");
            }

            if(StringUtils.isNotEmpty(item.getDrugSum())){
                viewHolder.drugSum.setText(item.getDrugSum() + " " + item.getDrugUseQuantityUnit());
            }else{
                viewHolder.drugSum.setText("");
            }

            if(StringUtils.isNotEmpty(item.getDrugUseQuantity())){
                viewHolder.drugUseQuantity.setText(item.getDrugUseQuantity() + " " + item.getDrugUseQuantityUnit());
            }else{
                viewHolder.drugUseQuantity.setText("");
            }


            return convertView;
        }

        class ViewHolder {
            private TextView drugName, doseTime, drugFrequency, drugSum, drugUseQuantity;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
