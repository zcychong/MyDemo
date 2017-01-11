package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.jakewharton.rxbinding.view.RxView;

public class LeaveActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.tv_commit) TextView tvCommit;
	@Bind(R.id.ll_back) LinearLayout llBack;

	@Bind(R.id.tv_people_name) TextView tvName;
	@Bind(R.id.iv_choose_people) ImageView ivName;
	@Bind(R.id.rl_name) RelativeLayout rlName;
	@Bind(R.id.tv_room_info) TextView tvRoomInfo;
	@Bind(R.id.et_shuttle_name) EditText etShuttleName;
	@Bind(R.id.et_shuttle_name_phone) EditText etShuttleNamePhone;
	@Bind(R.id.et_leave_because) EditText etLeaveBecause;

	@Bind(R.id.rl_leave_time) RelativeLayout rlLeaveTime;
	@Bind(R.id.rl_back_time) RelativeLayout rlBackTime;
	@Bind(R.id.tv_leave_time) TextView tvLeaveTime;
	@Bind(R.id.tv_back_time) TextView tvBackTime;
	
	private int year;
	private int month;
	private int day;
	private boolean checkPeople = false;
	private String elderlyId="";
	private StringBuffer sbDate = new StringBuffer();
	private StringBuffer sbTime = new StringBuffer();
	private String strShuttleName, strShuttleNamePhone;
	private String strLeaveBecause;
	private String strLeaveTime, strBackTime;
	private SharedPreferences userInfo;
	private String userId, usrOrg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave);
		context = this;

		initView();
		initData();

	}

	private void initView() {
		ButterKnife.bind(context);

		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});

		RxView.clicks(rlLeaveTime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showChooseDateTimeDialog(0);
			}
		});

		RxView.clicks(rlBackTime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showChooseDateTimeDialog(1);
			}
		});

		RxView.clicks(tvCommit).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				checkLeaveInfo();
			}
		});
		
		RxView.clicks(rlName).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, LeaveSearchElderLyActivity.class);
				intent.putExtra("userId", userId);
				startActivityForResult(intent, 0);
			}
		});
		
		

	}

	private void initData() {
		tvTitle.setText("请假");
		
		year = Integer.valueOf(DateOperate.getCurrentDataYear());
		month = Integer.valueOf(DateOperate.getCurrentDataMonth());
		day = Integer.valueOf(DateOperate.getCurrentDataDay());
		
		Log.e(TAG, "year"+  year);
		Log.e(TAG, "month"+  month);
		Log.e(TAG, "day"+  day);
		
		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);

		userId = userInfo.getString("userId", "");
		usrOrg = userInfo.getString("usrOrg", "");
	}

	private void showChooseDateTimeDialog(int i) {
		final int type = i;
		AlertDialog.Builder builder = new Builder(context);
		View view = View.inflate(context, R.layout.time_picker, null);
		if(i == 0){
			builder.setTitle("请选择离开时间:");
		}else{
			builder.setTitle("请选择返回时间:");
		}
		final DatePicker datePicker = (DatePicker)view.findViewById(R.id.dp_date_picker);
		final TimePicker timePicker = (TimePicker)view.findViewById(R.id.tp_timepicker);
		
//		datePicker.init(year, month-1, day, new OnDateChangedListener() {
//			@Override
//			public void onDateChanged(DatePicker view, int year, int monthOfYear,
//					int dayOfMonth) {
//				if(sbDate.length() > 0){
//					sbDate.delete(0, sbDate.length());
//				}
//				Log.e(TAG, year +"-"+ (monthOfYear+1) +"-"+ dayOfMonth);
//				sbDate.append(year);
//				sbDate.append("-");
//				if(monthOfYear+1 < 10){
//					sbDate.append("0");
//				}
//				sbDate.append(monthOfYear+1);
//				sbDate.append("-");
//				if(dayOfMonth < 10){
//					sbDate.append("0");
//				}
//				sbDate.append(dayOfMonth);
//			}
//		});
//		
//		
//		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
//			
//			@Override
//			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				if(sbTime.length() > 0){
//					sbTime.delete(0, sbTime.length());
//				}
//				if(hourOfDay < 10){
//					sbTime.append("0");
//				}
//				sbTime.append(hourOfDay);
//				sbTime.append(":");
//				if(minute < 10){
//					sbTime.append("0");
//				}
//				sbTime.append(minute);
//				sbTime.append(":");
//				sbTime.append("00");
//			}
//		});
		
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
				
				if(type == 0){ //离开时间
//					strLeaveTime = sbDate.toString() + " " + sbTime.toString();
					
					strLeaveTime = getDateTime(datePicker, timePicker);
					
					tvLeaveTime.setText(strLeaveTime);
				}else{
//					strBackTime = sbDate.toString() + " " + sbTime.toString();
					strBackTime = getDateTime(datePicker, timePicker);
					tvBackTime.setText(strBackTime);
				}
				
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
	
	private void checkLeaveInfo(){
		if(!checkPeople){
			Toast.makeText(context, "请选择老人!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(sbDate.length() <= 0){
			Toast.makeText(context, "请选择离开时间!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(sbTime.length() <= 0){
			Toast.makeText(context, "请选择返回时间!", Toast.LENGTH_SHORT).show();
			return ;
		}
		strShuttleName = etShuttleName.getText().toString();
		strShuttleNamePhone = etShuttleNamePhone.getText().toString();
		if(!StringUtils.isNotEmpty(strShuttleName)){
			Toast.makeText(context, "请填写接送人姓名!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(!StringUtils.isNotEmpty(strShuttleNamePhone)){
			Toast.makeText(context, "请填写接送人电话!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		strLeaveBecause = etLeaveBecause.getText().toString();
		if(!StringUtils.isNotEmpty(strLeaveBecause)){
			Toast.makeText(context, "请填写请假原因!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		leave();
	}

	private void leave() {
		loading = LoadingDialog.loadingSave(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("inputUser", userId);
		map.put("usrOrg", usrOrg);
		map.put("elderlyId", elderlyId);
		map.put("qjsj", strLeaveTime);
		map.put("hysj", strBackTime);
		String remark = "接送人:" + strShuttleName + " " + "接送人电话:" + strShuttleNamePhone 
				+ " " + "请假原因:" + strLeaveBecause;
		map.put("remark", remark);

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
			public void onNext(BaseResponseEntity checkVersion) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (checkVersion != null) {
					if (checkVersion.isSuccess()) {
						Toast.makeText(context, "请假成功!", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Log.e(TAG, "fail - " + checkVersion.getMessage());
						Toast.makeText(context, "请假失败!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().postLeave(subscriber, map);
	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		String name = "", room="", bed="";
		if(requestCode == 0){
			if(resultCode == 1){
				if(data != null){
					checkPeople = true;
					name = data.getStringExtra("name");
					room = data.getStringExtra("room");
					bed = data.getStringExtra("bed");
					elderlyId = data.getStringExtra("id");
					tvName.setText(name);
					tvRoomInfo.setText(room + "房间 - " + bed + "号床");
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
