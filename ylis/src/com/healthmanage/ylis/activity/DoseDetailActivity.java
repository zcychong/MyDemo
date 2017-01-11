package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.DoseEntity;
import com.healthmanage.ylis.model.DoseListResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.ListViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by YHT on 2017/1/4.
 */

public class DoseDetailActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.ll_back) LinearLayout llBack;
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackTitle;

    @Bind(R.id.lvfs_dose_list) ListViewForScrollView lvDrugs;

    @Bind(R.id.tv_info) TextView tvInfo;
    @Bind(R.id.tv_commit) TextView tvCommit;

    private DrugListAdapter drugListAdapter;
    private SharedPreferences userInfo;
    private String userId, elderlyId, elderlyName, shiftId;
    private StringBuilder sbDrugs = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;

        initView();
        initData();
        getDoseList();
    }

    private void initView(){
        ButterKnife.bind(this);

        llBack.setVisibility(View.VISIBLE);
        RxView.clicks(llBack).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(tvCommit).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialog();

                    }
                });

        tvTitle.setText("协助服药");


    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("老人情况");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
        final EditText etElderlyInfo = (EditText)view.findViewById(R.id.et_elderly_info);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String elderlyInfo = etElderlyInfo.getText().toString();
                if(StringUtils.isNotEmpty(elderlyInfo)){
                    dialog.dismiss();
                    dese(elderlyInfo);
                }else{
                    Toast.makeText(context, "请输入老人服药后情况", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    private void initData(){
        userId = getIntent().getStringExtra("userId");
        elderlyId = getIntent().getStringExtra("elderlyId");
        elderlyName = getIntent().getStringExtra("elderlyName");

        userInfo = (SharedPreferences) getSharedPreferences(context
                        .getResources().getString(R.string.apk_name),
                android.content.Context.MODE_PRIVATE);
        shiftId = userInfo.getString("shiftId", "");
        tvInfo.setText(elderlyName + "老人应该服用的药品:");
    }

    private void dese(String lrqk){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("ids", sbDrugs.toString());
        map.put("zxsj", DateOperate.getCurrentTime());
        map.put("shiftId", shiftId);
        map.put("lrqk", lrqk);

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
                        Toast.makeText(context, "服药完成!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent();
//                        intent.setAction("ylis.dose.finish");
//                        sendBroadcast(intent);
                        setResult(1);
                        finish();
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().dose(subscriber, map);
    }

    private void getDoseList(){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("elderlyId", elderlyId);


        Subscriber<DoseListResponse> subscriber = new Subscriber<DoseListResponse>() {

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
            public void onNext(final DoseListResponse taseList) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Log.e(TAG, "onNext");
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                        dealResponseData(taseList.getITEMS());
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }
                } else {
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getElderlyDoseInfo(subscriber, map);
    }

    private void dealResponseData(List<DoseEntity> list){
        if(sbDrugs.length() > 0){
            sbDrugs.delete(0, sbDrugs.length());
        }
        for(int i=0; i<list.size(); i++){
            sbDrugs.append(list.get(i).getId());
            sbDrugs.append("#");
        }
        if(drugListAdapter == null){
            drugListAdapter = new DrugListAdapter(context, list);
            lvDrugs.setAdapter(drugListAdapter);
        }
    }

    class DrugListAdapter extends BaseAdapter {
        private Context mContext;
        private List<DoseEntity> drugList;
        DrugListAdapter(Context context, List<DoseEntity> items){
            mContext = context;
            drugList = items;
        }

        @Override
        public int getCount() {
            return drugList.size();
        }

        @Override
        public Object getItem(int position) {
            return drugList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            boolean hasSpareDrug = false;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.dose_info_item, null);
                viewHolder.tvDrugName = (TextView) convertView.findViewById(R.id.tv_drug_name);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_dose_time);
                viewHolder.tvYpyl = (TextView) convertView.findViewById(R.id.tv_ypyl);
                viewHolder.tvUsage = (TextView) convertView.findViewById(R.id.tv_usage);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            DoseEntity doseEntity = drugList.get(position);
            if(StringUtils.isNotEmpty(doseEntity.getYpmc())){
                viewHolder.tvDrugName.setText(doseEntity.getYpmc());
            }else{
                viewHolder.tvDrugName.setText("");
            }

            if(StringUtils.isNotEmpty(doseEntity.getFssj())){
                viewHolder.tvTime.setText(doseEntity.getFssj());
            }else{
                viewHolder.tvTime.setText("");
            }

            if(StringUtils.isNotEmpty(doseEntity.getYpyl())){
                viewHolder.tvYpyl.setText(doseEntity.getYpyl());
            }else{
                viewHolder.tvYpyl.setText("");
            }

            if(StringUtils.isNotEmpty(doseEntity.getType())){
                viewHolder.tvUsage.setText(doseEntity.getType());
            }else{
                viewHolder.tvUsage.setText("");
            }

            return convertView;
        }

        class ViewHolder{
            private TextView tvDrugName, tvTime, tvYpyl, tvUsage;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
