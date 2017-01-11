package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.DrugEntity;
import com.healthmanage.ylis.model.DrugInfoEntity;
import com.healthmanage.ylis.model.LeaveElderlyEntity;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.view.ListViewForScrollView;
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

public class AddDrugInfoActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.lvfs_drug_list) ListViewForScrollView lvDrugList;
    @Bind(R.id.lvfs_spare_drug_list) ListViewForScrollView lvSpareDrugList;

    @Bind(R.id.rl_add_drug) RelativeLayout rlAddDrug;
    @Bind(R.id.rl_drug_position) RelativeLayout rlDrugPsition;
    @Bind(R.id.rl_drug_elderly) RelativeLayout rlDrugElderly;
    @Bind(R.id.rl_drug_syr) RelativeLayout rlDrugSyr;
    @Bind(R.id.rl_drug_jsr) RelativeLayout rlDrugJsr;
    @Bind(R.id.rl_add_spare_drug) RelativeLayout rlAddSpareDrug;

    @Bind(R.id.tv_drug_position) TextView tvDrugPosition;
    @Bind(R.id.tv_drug_elderly) TextView tvDrugElderly;
    @Bind(R.id.et_drug_syr) EditText etDrugSyr;
    @Bind(R.id.tv_drug_jsr) TextView tvDrugJsr;

    @Bind(R.id.tv_commit) TextView tvCommit;

    private String userId, shiftId, usrOrg, userName;
    private List<DrugInfoEntity> drugEntityList = new ArrayList<DrugInfoEntity>();
    private DrugAdapter drufAdaprer;
    private String drufPosition, elderlyName, elderlyId, sendDrugName;
    private SharedPreferences userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_info);

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

        RxView.clicks(rlAddDrug)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent inteit = new Intent(context, AddDrugActivity.class);
                        inteit.putExtra("type", 0);
                        startActivityForResult(inteit, 0);
                    }
                });

        RxView.clicks(rlDrugPsition)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, InputInfoActivity.class);
                        intent.putExtra("title", "填写药品位置");
                        startActivityForResult(intent, 4);
                    }
                });

        RxView.clicks(rlDrugElderly)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(context, LeaveSearchElderLyActivity.class);
                        intent.putExtra("userId", userId);
                        startActivityForResult(intent, 3);
                    }
                });

//        RxView.clicks(rlDrugSyr)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//
//                    }
//                });


        RxView.clicks(tvCommit)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        checkDrugInfo();
                    }
                });



    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");

        userInfo = (SharedPreferences) getSharedPreferences(context
                        .getResources().getString(R.string.apk_name),
                android.content.Context.MODE_PRIVATE);
        userName = userInfo.getString("userName", "");
        tvDrugJsr.setText(userName);


        tvTitle.setText("添加药品");
    }

    private void checkDrugInfo(){
        if(drugEntityList.size() <= 0){
            Toast.makeText(context, "请添加药品!", Toast.LENGTH_SHORT).show();
            return;
        }
        drufPosition = tvDrugPosition.getText().toString();
        if(StringUtils.isEmpty(drufPosition)){
            Toast.makeText(context, "请添加药品位置!", Toast.LENGTH_SHORT).show();
            return;
        }

        sendDrugName = etDrugSyr.getText().toString();
        if(StringUtils.isEmpty(sendDrugName)){
            Toast.makeText(context, "请添加送药人名字!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(StringUtils.isEmpty(elderlyId)){
            Toast.makeText(context, "请选择老人!", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDrugInfo();
    }

    private void updateDrugInfo(){
        Map<String, String> map = new HashMap<String, String>();
        String drugs = getDrugInfo();
        map.put("drugs", drugs);
        map.put("ypwz", drufPosition);
        map.put("elderlyId", elderlyId);
        map.put("syr", sendDrugName);
        map.put("userId", userId);
        map.put("usrOrg", usrOrg);
        map.put("remark", "null");

        loading = LoadingDialog.loadingFind(context);
        loading.show();

        Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onNext(BaseResponseEntity taseList) {
                Log.e(TAG, "onNext");
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                if (taseList != null) {
                    if (taseList.isSuccess()) {
                       Toast.makeText(context, "添加药品成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.e(TAG, "fail - " + taseList.getMessage());
                    }

                } else {
                    Log.e(TAG, "fail - null");
                }

            }
        };
        HttpMethodImp.getInstance().updateDrugsInfo(subscriber, map);
    }

    private String getDrugInfo(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<drugEntityList.size(); i++){

            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugName())){
                sb.append(drugEntityList.get(i).getDrug().getDrugName() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugSum())){
                sb.append(drugEntityList.get(i).getDrug().getDrugSum() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugGG())){
                sb.append(drugEntityList.get(i).getDrug().getDrugGG() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugSumUnit())){
                sb.append(drugEntityList.get(i).getDrug().getDrugSumUnit() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugUseQuantityUnit())){
                sb.append(drugEntityList.get(i).getDrug().getDrugUseQuantityUnit() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugUseQuantity())){
                sb.append(drugEntityList.get(i).getDrug().getDrugUseQuantity() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugFrequency())){
                sb.append(drugEntityList.get(i).getDrug().getDrugFrequency() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrug().getDrugUsage())){
                sb.append(drugEntityList.get(i).getDrug().getDrugUsage() + "#");
            }else{
                sb.append("-#");
            }
            sb.append("0");
            sb.append("$");
        }
        for(int i=0; i<drugEntityList.size(); i++){
            if(drugEntityList.get(i).getDrugSpare() == null){
                break;
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugName())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugName() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugSum())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugSum() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugGG())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugGG() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugSumUnit())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugSumUnit() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugUseQuantityUnit())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugUseQuantityUnit() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugUseQuantity())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugUseQuantity() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugFrequency())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugFrequency() + "#");
            }else{
                sb.append("-#");
            }
            if(StringUtils.isNotEmpty(drugEntityList.get(i).getDrugSpare().getDrugUsage())){
                sb.append(drugEntityList.get(i).getDrugSpare().getDrugUsage() + "#");
            }else{
                sb.append("-#");
            }
            sb.append(i+1);
            sb.append("$");
        }

        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if(requestCode == 4 && resultCode == 1){
            drufPosition = data.getStringExtra("input");
            tvDrugPosition.setText(drufPosition);
        }
        if(requestCode == 3 && resultCode == 1){
            elderlyName = data.getStringExtra("name");
            elderlyId = data.getStringExtra("id");
            tvDrugElderly.setText(elderlyName);
        }

        if(resultCode == 2){
            Bundle bundle = data.getBundleExtra("drug");
            if(bundle == null){
                return;
            }
            DrugEntity drugEntity = bundle.getParcelable("drug");
            if(drugEntity == null){
                return;
            }

            if(requestCode == 0){
                DrugInfoEntity item = new DrugInfoEntity();
                item.setDrug(drugEntity);
                drugEntityList.add(item);


            }else if(requestCode == 1){
                drugEntityList.get(drugEntity.getDrugSpare()).setDrugSpare(drugEntity);

            }

            if(drufAdaprer == null){
                drufAdaprer = new DrugAdapter(context);
                lvDrugList.setAdapter(drufAdaprer);
            }else{
                drufAdaprer.notifyDataSetChanged();
            }
        }

    }

    class DrugAdapter extends BaseAdapter{
        private Context mContext;
//        private List<DrugInfoEntity> list;
        DrugAdapter(Context context){
            mContext = context;
//            list= items;
        }

        @Override
        public int getCount() {
            return drugEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return drugEntityList.get(position);
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
                        R.layout.drug_item, null);
                viewHolder.tvAddSpareDrug = (TextView) convertView.findViewById(R.id.tv_add_spare_drug);
                viewHolder.tvDrugInfo = (TextView) convertView.findViewById(R.id.tv_drug_info);
                viewHolder.tvSpareDrug = (TextView) convertView.findViewById(R.id.tv_spare_drug);
                viewHolder.ivDeleteDrug = (ImageView) convertView.findViewById(R.id.iv_delete);
                viewHolder.ivDeleteDrugSpare = (ImageView) convertView.findViewById(R.id.iv_delete_spare);
                viewHolder.llDrugSpare = (LinearLayout) convertView.findViewById(R.id.ll_drug_spare);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final DrugInfoEntity item = drugEntityList.get(position);
            DrugEntity itemDrug = item.getDrug();
            viewHolder.tvDrugInfo.setText(itemDrug.getDrugName() + "\n"
                + "共" + itemDrug.getDrugSum() + itemDrug.getDrugSumUnit() + "  每" + itemDrug.getDrugSumUnit()+itemDrug.getDrugGG()
                    + itemDrug.getDrugUseQuantityUnit() + "\n"
                + "每次" + itemDrug.getDrugUseQuantity() + itemDrug.getDrugUseQuantityUnit() + "\n"
                + itemDrug.getDrugFrequency());

            viewHolder.tvAddSpareDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent inteit = new Intent(context, AddDrugActivity.class);
                    inteit.putExtra("type", position + 1);
                    startActivityForResult(inteit, 1);
                }
            });

            if(item.getDrugSpare() != null){
                viewHolder.tvAddSpareDrug.setVisibility(View.GONE);
                viewHolder.llDrugSpare.setVisibility(View.VISIBLE);
                DrugEntity itemDrugSpare = item.getDrugSpare();
                viewHolder.tvSpareDrug.setText(itemDrugSpare.getDrugName() + "\n"
                        + "共" + itemDrugSpare.getDrugSum() + itemDrugSpare.getDrugSumUnit()
                        + "  每" + itemDrugSpare.getDrugSumUnit()+itemDrugSpare.getDrugGG()
                        + itemDrugSpare.getDrugUseQuantityUnit() + "\n"
                        + "每次" + itemDrugSpare.getDrugUseQuantity() + itemDrugSpare.getDrugUseQuantityUnit() + "\n"
                        + itemDrugSpare.getDrugFrequency());
            }else{
                viewHolder.tvAddSpareDrug.setVisibility(View.VISIBLE);
                viewHolder.llDrugSpare.setVisibility(View.GONE);
            }


            return convertView;
        }

        class ViewHolder{
            private TextView tvAddSpareDrug, tvDrugInfo, tvSpareDrug;
            private ImageView ivDeleteDrug, ivDeleteDrugSpare;
            private LinearLayout llDrugSpare;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
