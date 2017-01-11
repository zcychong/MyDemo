package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PatroalAddExceptionActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.rl_elderly_name) RelativeLayout rlElderlyName;
	@Bind(R.id.rl_patroal_info) RelativeLayout rlPatroalInfo;
	@Bind(R.id.rl_patroal_time) RelativeLayout rlPatroalTime;
	@Bind(R.id.rl_deal_info) RelativeLayout rlDealInfo;
	@Bind(R.id.rl_elderly_info) RelativeLayout rlElderlyInfo;
	
	@Bind(R.id.et_leader_name) EditText etLeaderName;
	@Bind(R.id.et_home_name) EditText etHomeName;
	@Bind(R.id.et_home_phone) EditText etHomePhone;
	@Bind(R.id.et_home_back) EditText etHomeBack;
	@Bind(R.id.et_home_another) EditText etHomeAnother;
	
	@Bind(R.id.tv_patroal_info) TextView tvPatroalInfo;
	@Bind(R.id.tv_patroal_deal) TextView tvPatroalDeal;
	@Bind(R.id.tv_patroal_elderly) TextView tvPatroalElderly;
	
	@Bind(R.id.tv_elderly_name) TextView tvElderlyName;
	@Bind(R.id.tv_patroal_time) TextView tvPatroalTime;
	@Bind(R.id.tv_commit) TextView tvCommit;
	
	private StringBuffer sbDate = new StringBuffer();
	private StringBuffer sbTime = new StringBuffer();
	private int year;
	private int month;
	private int day;
	private String elderlyId;
	private String execTime, userId, shiftId, xsrmc, usrOrg;
	private String patroalPeople, patroalTime;//巡视时间
	private String peopleName, patroalInfo, patroalDeal, elderlyInfo, time;//发生时间
	private String leaderName, hoomName, hoomPhone, hoomBack, another;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patroal_add_exception);
		
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
		
		RxView.clicks(rlElderlyName).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					Intent intent = new Intent(context, LeaveSearchElderLyActivity.class);
					intent.putExtra("userId", userId);
					startActivityForResult(intent, 4);
				}
		});
				
		RxView.clicks(rlPatroalInfo).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					Intent intent = new Intent(context, InputInfoActivity.class);
					intent.putExtra("title", getString(R.string.patroal_info));
					startActivityForResult(intent, 1);
				}
		});
		
		RxView.clicks(rlPatroalTime).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					showChooseDateTimeDialog();
				}
		});
		
		
		RxView.clicks(rlDealInfo).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					Intent intent = new Intent(context, InputInfoActivity.class);
					intent.putExtra("title", getString(R.string.patroal_deal_info));
					startActivityForResult(intent, 2);
				}
		});
		
		RxView.clicks(rlElderlyInfo).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					Intent intent = new Intent(context, InputInfoActivity.class);
					intent.putExtra("title", getString(R.string.patroal_elderly_info));
					startActivityForResult(intent, 3);
				}
		});
		
		RxView.clicks(tvCommit).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					if (Network.checkNet(context)) {
						commitException();
					}else{
						Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
					}
				}
		});
		
	}
	
	private void initData(){
		tvTitle.setText("异常情况");
		
		execTime = getIntent().getStringExtra("execTime");
		patroalTime = getIntent().getStringExtra("patroalTime");
		patroalPeople = getIntent().getStringExtra("patroalPeople");
		
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		xsrmc = getIntent().getStringExtra("xsrmc");
		usrOrg = getIntent().getStringExtra("usrOrg");
		
		year = Integer.valueOf(DateOperate.getCurrentDataYear());
		month = Integer.valueOf(DateOperate.getCurrentDataMonth());
		day = Integer.valueOf(DateOperate.getCurrentDataDay());
	}
	
	private void commitException(){
		
//		peopleName = tvElderlyName.getText().toString();
		patroalInfo = tvPatroalInfo.getText().toString();
		patroalDeal = tvPatroalDeal.getText().toString();
		elderlyInfo = tvPatroalElderly.getText().toString();
		
		leaderName = etLeaderName.getText().toString();
		hoomName = etHomeName.getText().toString();
		hoomPhone = etHomePhone.getText().toString();
		hoomBack = etHomeBack.getText().toString();
		another = etHomeAnother.getText().toString();
		
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("xsrmc", xsrmc);
		map.put("shiftId", shiftId);
		map.put("usrOrg", usrOrg);
		map.put("zbxcqk", "1");
		
		map.put("xssj", patroalTime);
		map.put("execTime", execTime);
		if(execTime.equals("D")){
			map.put("xslx", "1");
		}else{
			map.put("xslx", "0");
		}
		map.put("elderlyName", peopleName);
		map.put("elderlyId", elderlyId);
		
		map.put("tsqk", patroalInfo);
		map.put("fssj", time);
		map.put("clcs", patroalDeal);
		map.put("lrzk", elderlyInfo);
		map.put("ldxm", leaderName);
		map.put("lxjsxm", hoomName);
		map.put("jsdh", hoomPhone);
		map.put("jshf", hoomBack);
		map.put("remark", another);
		

		Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

			@Override
			public void onCompleted() {
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
			public void onNext(BaseResponseEntity taseList) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						
						Intent intent = new Intent();
						intent.putExtra("peopleName", peopleName);
						intent.putExtra("patroalInfo", patroalInfo);
						intent.putExtra("time", time);
						intent.putExtra("patroalDeal", patroalDeal);
						intent.putExtra("elderlyInfo", elderlyInfo);
						
						intent.putExtra("leaderName", leaderName);
						intent.putExtra("hoomName", hoomName);
						intent.putExtra("hoomPhone", hoomPhone);
						intent.putExtra("hoomBack", hoomBack);
						intent.putExtra("another", another);
						
						setResult(1, intent);
						finish();
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().savePatroalExceptionItem(subscriber, map);
	}
	
	private void showChooseDateTimeDialog() {
		AlertDialog.Builder builder = new Builder(context);
		View view = View.inflate(context, R.layout.time_picker, null);
		builder.setTitle("请选择发生时间:");
		final DatePicker datePicker = (DatePicker)view.findViewById(R.id.dp_date_picker);
		final TimePicker timePicker = (TimePicker)view.findViewById(R.id.tp_timepicker);
		
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(sbDate.length() > 0){
					sbDate.delete(0, sbDate.length());
				}
				if(sbTime.length() > 0){
					sbTime.delete(0, sbTime.length());
				}
				
				time = getDateTime(datePicker, timePicker);
				tvPatroalTime.setText(time);
			
			}
			
		});
		AlertDialog dialog = builder.create();
		dialog.setView(view);

		dialog.show();
	}
	
	private String getDateTime(DatePicker datePicker, TimePicker timePicker){
		sbDate.append(year);
		sbDate.append("-");
		if(datePicker.getMonth()+1 < 10){
			sbDate.append("0");
		}
		sbDate.append(datePicker.getMonth()+1);
		sbDate.append("-");
		if(datePicker.getDayOfMonth() < 10){
			sbDate.append("0");
		}
		sbDate.append(datePicker.getDayOfMonth());
		
		if(timePicker.getCurrentHour() < 10){
			sbTime.append("0");
		}
		sbTime.append(timePicker.getCurrentHour());
		sbTime.append(":");
		if(timePicker.getCurrentMinute() < 10){
			sbTime.append("0");
		}
		sbTime.append(timePicker.getCurrentMinute());
		sbTime.append(":");
		sbTime.append("00");
		
		return (sbDate.toString() + " " + sbTime.toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		String input = "";
		if(data != null){
			if(resultCode == 1){
				if(requestCode == 1){
					tvPatroalInfo.setText(data.getStringExtra("input"));
				}else if(requestCode == 2){
					tvPatroalDeal.setText(data.getStringExtra("input"));
				}else if(requestCode == 3){
					tvPatroalElderly.setText(data.getStringExtra("input"));
				}else if(requestCode == 4){
					peopleName = data.getStringExtra("name");
					elderlyId = data.getStringExtra("id");
					String room = data.getStringExtra("room");
					String bed = data.getStringExtra("bed");
					
					tvElderlyName.setText(room + "房间" + bed + "床" + peopleName);
				}
			}	
		}
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
