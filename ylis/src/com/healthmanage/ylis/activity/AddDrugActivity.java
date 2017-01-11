package com.healthmanage.ylis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.model.DrugEntity;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by YHT on 2016/12/22.
 */

public class AddDrugActivity extends BaseActivity {
    private Activity context;
    private Dialog loading;

    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_back_text) TextView tvBackText;
    @Bind(R.id.iv_back) ImageView ivBack;
    @Bind(R.id.ll_back) LinearLayout llBack;

    @Bind(R.id.et_drug_name) EditText etDrugName;
    @Bind(R.id.ll_sum_unit) LinearLayout llSumUnit;
    @Bind(R.id.tv_sum_unit) TextView tvSumUnit;
    @Bind(R.id.et_sum_quantity) EditText etSumQuantity;
    @Bind(R.id.tv_drug_sum_unit) TextView tvDrugSumUnit;
    @Bind(R.id.ll_drug_use_quantity_unit) LinearLayout llDrugUseQuantityUnit;
    @Bind(R.id.tv_use_quantity_unit) TextView tvUseQuantityUnit;
    @Bind(R.id.et_drug_gg) EditText etDrugGg;
    @Bind(R.id.tv_drug_gg_unit) TextView tvDrugGgUnit;
    @Bind(R.id.et_use_quantity) EditText etUseQuantity;
    @Bind(R.id.tv_ones_use_unit) TextView tvOnesUseUnit;
    @Bind(R.id.ll_drug_use_frequency) LinearLayout llDrugUseFrequency;
    @Bind(R.id.tv_drug_use_frequency) TextView tvDrugUseFrequency;
    @Bind(R.id.ll_drug_usage) LinearLayout llDrugUsage;
    @Bind(R.id.tv_drug_usage) TextView tvDrugUsage;

    @Bind(R.id.tv_commit) TextView tvCommit;

    private String userId, shiftId, usrOrg;
    private int type;// type:0-添加药品 其他-药品id
    private DrugEntity drugEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);

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

        RxView.clicks(tvCommit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        checkInfo();
                    }
                });

        RxView.clicks(llSumUnit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityForResult(new Intent(context, ChooseItemActivity.class)
                                .putExtra("type", 0), 1);
                    }
                });

        RxView.clicks(llDrugUseQuantityUnit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityForResult(new Intent(context, ChooseItemActivity.class)
                                .putExtra("type", 1), 1);
                    }
                });

        RxView.clicks(llDrugUseFrequency)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityForResult(new Intent(context, ChooseItemActivity.class)
                                .putExtra("type", 2), 1);
                    }
                });

        RxView.clicks(llDrugUsage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityForResult(new Intent(context, ChooseItemActivity.class)
                                .putExtra("type", 3), 1);
                    }
                });


    }

    private void initData() {

        userId = getIntent().getStringExtra("userId");
        shiftId = getIntent().getStringExtra("shiftId");
        usrOrg = getIntent().getStringExtra("usrOrg");

        type = getIntent().getIntExtra("type", 0);

        drugEntity = new DrugEntity();
        if(type == 0){
            tvTitle.setText("添加药品");
            drugEntity.setDrugSpare(0);
        }else{
            tvTitle.setText("添加备用药品");
            drugEntity.setDrugSpare(type);
        }


    }

    private void checkInfo(){
        String temp = etDrugName.getText().toString();

        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请输入药品名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugName(temp);
        temp = tvSumUnit.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请选择药品单位!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugSumUnit(temp);
        temp = etSumQuantity.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请输入药品总量!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugSum(temp);
        temp = tvUseQuantityUnit.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请选择每次药品用量单位!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugUseQuantityUnit(temp);

        temp = etDrugGg.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请输入药品规格!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugGG(temp);

        temp = etUseQuantity.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请输入每次药品用量!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugUseQuantity(temp);

        temp = tvDrugUseFrequency.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请选择用药频次!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugFrequency(temp);

        temp = tvDrugUsage.getText().toString();
        if(StringUtils.isEmpty(temp)){
            Toast.makeText(context, "请选择药品用法!", Toast.LENGTH_SHORT).show();
            return;
        }
        drugEntity.setDrugUsage(temp);

        drugEntity.setDrugSpare(0);

        Bundle bundle = new Bundle();
        bundle.putParcelable("drug", drugEntity);
        Intent intent = new Intent();
        intent.putExtra("drug", bundle);
        setResult(2, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if(requestCode == 1){
            if(resultCode == 0){
                String name = data.getStringExtra("name");
                if(StringUtils.isNotEmpty(name)){
                    tvSumUnit.setText(name);
                }
            }else if(resultCode == 1){
                String name = data.getStringExtra("name");
                if(StringUtils.isNotEmpty(name)){
                    tvUseQuantityUnit.setText(name);
                    tvOnesUseUnit.setText(name);
                }
            }else if(resultCode == 2){
                String name = data.getStringExtra("name");
                if(StringUtils.isNotEmpty(name)){
                    tvDrugUseFrequency.setText(name);
                }
            }else if(resultCode == 3){
                String name = data.getStringExtra("name");
                if(StringUtils.isNotEmpty(name)){
                    tvDrugUsage.setText(name);
                }
            }


            setDrugGgUnit();

        }
    }

    private void setDrugGgUnit(){
        tvDrugGgUnit.setText( tvUseQuantityUnit.getText().toString() + "/" +tvSumUnit.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
