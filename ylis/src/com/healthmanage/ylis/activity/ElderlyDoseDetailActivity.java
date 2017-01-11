package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.DrugListResponse;
import com.healthmanage.ylis.model.DrugResEntity;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.ListViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by YHT on 2016/12/22.
 */

public class ElderlyDoseDetailActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.tv_option) TextView tvOption;
    @Bind(R.id.ll_option) LinearLayout llOption;

    @Bind(R.id.tv_name) TextView tvName;
    @Bind(R.id.tv_gender) TextView tvGender;
    @Bind(R.id.tv_room_info) TextView tvRoomInfo;
    @Bind(R.id.lvfs_dose_list) ListViewForScrollView lvDoseList;

    @Bind(R.id.ll_no_value) LinearLayout llNoValue;

    private String userId, shiftId, usrOrg, type;// type:0-添加药品 1-条件备用药品
    private String elderlyId, elderlyName, bedNo, roomNo;
    private DrugListAdapter drugListAdapter;
    private boolean editing = false;
    private List<DrugResEntity> drugResEntitieList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_dose_detail);

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

        RxView.clicks(llOption)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(!editing){
                            if(drugResEntitieList != null && drugResEntitieList.size() != 0){
                                for(int i=0; i<drugResEntitieList.size(); i++){
                                    drugResEntitieList.get(i).setStatus("1");
                                }
                                drugListAdapter.notifyDataSetChanged();
                                tvOption.setText("取消");
                                editing = true;
                            }
                        }else{
                            if(drugResEntitieList != null && drugResEntitieList.size() != 0){
                                for(int i=0; i<drugResEntitieList.size(); i++){
                                    drugResEntitieList.get(i).setStatus("0");
                                }
                                drugListAdapter.notifyDataSetChanged();
                                tvOption.setText("编辑");
                                editing = false;
                            }

                        }

                    }
                });
    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");
        elderlyId = getIntent().getStringExtra("elderlyId");
        elderlyName = getIntent().getStringExtra("elderlyName");
        bedNo = getIntent().getStringExtra("bedNo");
        roomNo = getIntent().getStringExtra("roomNo");

        tvTitle.setText("药品管理详情");
        tvName.setText(elderlyName);
        tvRoomInfo.setText(roomNo + "房间  " + bedNo + "床");
        tvOption.setText("编辑");
    }

    private void getData(){
        if (Network.checkNet(context)) {
            getDrugInfo();
        }else{
            Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDrugInfo(){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("usrOrg", usrOrg);
        map.put("elderlyId", elderlyId);

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
                        drugResEntitieList = response.getITEMS();
                        dealDrugListData(response.getITEMS());
                    }else{
                        llNoValue.setVisibility(View.VISIBLE);
                        Log.e(TAG, "fail - " + response.getMessage());
                    }
                }else{
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().getDrugList(subscriber, map);

    }

    private void dealDrugListData(List<DrugResEntity> list){
        drugListAdapter = new DrugListAdapter(context, list);
        lvDoseList.setAdapter(drugListAdapter);
    }

    private void showDeleteDrugDialog(String drugName, final String drugId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("删除药品?");
        builder.setMessage("您确定要删除" + drugName + "吗?" );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDrug(drugId);
            }
        });

        builder.create().show();
    }

    private void deleteDrug(String drugId){
        loading = LoadingDialog.loadingFind(context);
        loading.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("ypId", drugId);

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
            }

            @Override
            public void onNext(BaseResponseEntity response) {
                Log.e(TAG, "onNext");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                if(response != null){
                    if(response.isSuccess()){
                        Toast.makeText(context, "删除药品成功!", Toast.LENGTH_SHORT).show();
                        getData();
                    }else{
                        llNoValue.setVisibility(View.VISIBLE);
                        Log.e(TAG, "fail - " + response.getMessage());
                    }
                }else{
                    Log.e(TAG, "fail - null");
                }
            }
        };
        HttpMethodImp.getInstance().deleteDrug(subscriber, map);
    }

    class DrugListAdapter extends BaseAdapter {
        private Context mContext;
        private List<DrugResEntity> drugList;
        DrugListAdapter(Context context, List<DrugResEntity> items){
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
                        R.layout.dose_detail_item, null);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tvDrugInfo = (TextView) convertView.findViewById(R.id.tv_drug_info);
                viewHolder.tvDrugPosition = (TextView) convertView.findViewById(R.id.tv_drug_position);
                viewHolder.ivDeleteDrug = (ImageView) convertView.findViewById(R.id.iv_delete_drug);
                viewHolder.tvSyr = (TextView) convertView.findViewById(R.id.tv_drug_syr);

                viewHolder.llDrugSpare = (LinearLayout) convertView.findViewById(R.id.ll_spare_drug);
                viewHolder.tvSpareDrugInfo = (TextView) convertView.findViewById(R.id.tv_spare_drug_info);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final DrugResEntity item = drugList.get(position);
            if(StringUtils.isNotEmpty(item.getInputTime())){
                viewHolder.tvTime.setText(item.getInputTime());
            }else{
                viewHolder.tvTime.setText("");
            }

            if(StringUtils.isNotEmpty(item.getYpmc())){
                viewHolder.tvDrugInfo.setText(item.getYpmc() + "  共" + item.getYpzl() + item.getYpdw() +
                        "  药品规格:" + item.getYpgg() + item.getMcyydw() +"/" + item.getYpdw() + "  "
                        + item.getYppc() + "  " + "  每次" + item.getMcyl() + item.getMcyydw());
            }else{
                viewHolder.tvTime.setText("");
            }

            if(StringUtils.isNotEmpty(item.getYpwz())){
                viewHolder.tvDrugPosition.setText(item.getYpwz());
            }else{
                viewHolder.tvDrugPosition.setText("");
            }

            if(StringUtils.isNotEmpty(item.getSyr())){
                viewHolder.tvSyr.setText(item.getSyr());
            }else{
                viewHolder.tvSyr.setText("");
            }

            if(StringUtils.isNotEmpty(item.getByyp())){
                viewHolder.tvSpareDrugInfo.setText(item.getByyp());
                viewHolder.llDrugSpare.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tvSpareDrugInfo.setText("");
                viewHolder.llDrugSpare.setVisibility(View.GONE);
            }

            if(StringUtils.isNotEmpty(item.getStatus())){
                if(!item.getStatus().equals("0")){
                    viewHolder.ivDeleteDrug.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ivDeleteDrug.setVisibility(View.GONE);
                }
            }else{
                viewHolder.ivDeleteDrug.setVisibility(View.GONE);
            }

            viewHolder.ivDeleteDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDrugDialog(item.getYpmc(), item.getYpId());
                }
            });


            return convertView;
        }

        class ViewHolder{
            private TextView tvTime, tvDrugInfo, tvDrugPosition, tvSyr, tvSpareDrugInfo;
            private ImageView ivDeleteDrug;
            private LinearLayout llDrugSpare;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
