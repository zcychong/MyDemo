package com.healthmanage.ylis.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.adapter.GvTipsAdapter;
import com.healthmanage.ylis.application.MainApplication;
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditTaskConnectActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_user_name) TextView tvUserName;
//	@Bind(R.id.et_people_mind) EditText etPeopleMind;
//	@Bind(R.id.et_people_mood) EditText etPeopleMood;
//	@Bind(R.id.et_people_food) EditText etPeopleFood;
//	@Bind(R.id.et_people_dose) EditText etPeopleDosd;
//	@Bind(R.id.et_people_injection) EditText etPeopleInjection;
//	@Bind(R.id.et_people_skin) EditText etPeopleSkin;
//	@Bind(R.id.et_people_body_clean) EditText etPeopleBodyClean;
//	@Bind(R.id.et_people_clothes_clean) EditText etPeopleClothesClean;
//	@Bind(R.id.et_people_room_health) EditText etPeopleRooomHealth;
//	@Bind(R.id.et_people_another) EditText etPeopleAnother;

	@Bind(R.id.tv_people_mind) TextView tvPeopleMind;
	@Bind(R.id.tv_people_mood) TextView tvPeopleMood;
	@Bind(R.id.tv_people_food) TextView tvPeopleFood;
	@Bind(R.id.tv_people_dose) TextView tvPeopleDose;
	@Bind(R.id.tv_people_injection) TextView tvPeopleInjection;
	@Bind(R.id.tv_people_skin) TextView tvPeopleSkin;
	@Bind(R.id.tv_people_body_clean) TextView tvPeopleBodyClean;
	@Bind(R.id.tv_people_clothes_clean) TextView tvPeopleClothesClean;
	@Bind(R.id.tv_people_room_health) TextView tvPeopleRooomHealth;
	@Bind(R.id.tv_people_another) TextView tvPeopleAnother;


	
//	@Bind(R.id.tv_people_mind_good) TextView tvPeopleMindGood;
//	@Bind(R.id.tv_people_mind_exception) TextView tvPeopleMindException;
//	@Bind(R.id.tv_people_mood_good) TextView tvPeopleMoodGood;
//	@Bind(R.id.tv_people_mood_exception) TextView tvPeopleMoodException;
//	@Bind(R.id.tv_people_food_good) TextView tvPeopleFoodGood;
//	@Bind(R.id.tv_people_food_exception) TextView tvPeopleFoodException;
//	@Bind(R.id.tv_people_dose_good) TextView tvPeopleDoseGood;
//	@Bind(R.id.tv_people_dose_exception) TextView tvPeopleDosdException;
//	@Bind(R.id.tv_people_injection_good) TextView tvPeopleInjectionGood;
//	@Bind(R.id.tv_people_injection_exception) TextView tvPeopleInjectionException;
//	@Bind(R.id.tv_people_skin_good) TextView tvPeopleSkinGood;
//	@Bind(R.id.tv_people_skin_exception) TextView tvPeopleSkinException;
//	@Bind(R.id.tv_people_body_clean_good) TextView tvPeopleBodyCleanGood;
//	@Bind(R.id.tv_people_body_clean_exception) TextView tvPeopleBodyCleanException;
//	@Bind(R.id.tv_people_clothes_clean_good) TextView tvPeopleClothesCleanGood;
//	@Bind(R.id.tv_people_clothes_clean_exception) TextView tvPeopleClothesCleanException;
//	@Bind(R.id.tv_people_room_health_good) TextView tvPeopleRooomHealthGood;
//	@Bind(R.id.tv_people_room_health_exception) TextView tvPeopleRooomHealthException;

	@Bind(R.id.gv_people_mind) GridViewForScrollView gvPeopleMind;
	@Bind(R.id.gv_people_mood) GridViewForScrollView gvPeopleMood;
	@Bind(R.id.gv_people_food) GridViewForScrollView gvPeopleFood;
	@Bind(R.id.gv_people_dose) GridViewForScrollView gvPeoplDose;
	@Bind(R.id.gv_people_injection) GridViewForScrollView gvPeopleInjection;
	@Bind(R.id.gv_people_skin) GridViewForScrollView gvPeopleSkin;
	@Bind(R.id.gv_people_body_clean) GridViewForScrollView gvPeopleBodyClean;
	@Bind(R.id.gv_people_clothes_clean) GridViewForScrollView gvPeopleClothesClean;
	@Bind(R.id.gv_people_room_health) GridViewForScrollView gvPeopleRoomHealth;
	@Bind(R.id.gv_people_another) GridViewForScrollView gvPeopleAnother;

	
	@Bind(R.id.tv_commit) TextView tvCommit;

	private String strPeopleMind = "", strPeopleMood = "", strPeopleFood = "";
	private String strPeopleDosd = "", strPeopleInjection = "",
			strPeopleSkin = "";
	private String strPeopleBodyClean = "", strPeopleClothesClean = "",
			strPeopleRooomHealth = "", strPeopleAnother = "";
	private String userId, usrOrg, userName, shiftId;
	private SharedPreferences userInfo;

//	private GvTipsAdapter adapter;
	private List<GvTipsAdapter> adapterList = new ArrayList<GvTipsAdapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task_connect);
		context = this;

		initView();
		initData();

	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		userInfo = getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);
		usrOrg = userInfo.getString("usrOrg", "");

		userName = userInfo.getString("userName", "");
		tvUserName.setText(userName);
	}

	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("填写任务交接单");
		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});

		RxView.clicks(tvCommit).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						if (Network.checkNet(context)) {
							commitTaskConnect();
						}else{
							Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
						}
					}
				});
		
		
//		tvPeopleMindGood.setOnClickListener(this);
//		tvPeopleMindException.setOnClickListener(this);
//		tvPeopleMoodGood.setOnClickListener(this);
//		tvPeopleMoodException.setOnClickListener(this);
//		tvPeopleFoodGood.setOnClickListener(this);
//		tvPeopleFoodException.setOnClickListener(this);
//		tvPeopleDoseGood.setOnClickListener(this);
//		tvPeopleDosdException.setOnClickListener(this);
//		tvPeopleInjectionGood.setOnClickListener(this);
//	    tvPeopleInjectionException.setOnClickListener(this);
//		tvPeopleSkinGood.setOnClickListener(this);
//		tvPeopleSkinException.setOnClickListener(this);
//		tvPeopleBodyCleanGood.setOnClickListener(this);
//		tvPeopleBodyCleanException.setOnClickListener(this);
//		tvPeopleClothesCleanGood.setOnClickListener(this);
//		tvPeopleClothesCleanException.setOnClickListener(this);
//		tvPeopleRooomHealthGood.setOnClickListener(this);
//		tvPeopleRooomHealthException.setOnClickListener(this);

		initAdapterData(gvPeopleMind, tvPeopleMind, R.array.people_mind_tips, 0);
		initAdapterData(gvPeopleMood, tvPeopleMood, R.array.people_mind_tips, 1);
		initAdapterData(gvPeopleFood, tvPeopleFood, R.array.people_mind_tips, 2);
		initAdapterData(gvPeoplDose, tvPeopleDose, R.array.people_dose_tips, 3);
		initAdapterData(gvPeopleInjection, tvPeopleInjection, R.array.people_injection, 4);
		initAdapterData(gvPeopleSkin, tvPeopleSkin, R.array.people_mind_tips, 5);
		initAdapterData(gvPeopleBodyClean, tvPeopleBodyClean, R.array.people_mind_tips, 6);
		initAdapterData(gvPeopleClothesClean, tvPeopleClothesClean, R.array.people_mind_tips, 7);
		initAdapterData(gvPeopleRoomHealth, tvPeopleRooomHealth, R.array.people_mind_tips, 8);
		initAdapterData(gvPeopleAnother, tvPeopleAnother, R.array.people_another, 9);
	}

	private void initAdapterData(GridViewForScrollView view, final TextView tvPeople, int resId, final int type){
		final GvTipsAdapter adapter = new GvTipsAdapter(context, view, resId);
		view.setAdapter(adapter);

		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String responseCode = adapter.click(position);
				Log.e(TAG, "" + responseCode);
				if(responseCode.equals("1")){
					startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
						.putExtra("userId", userId).putExtra("shiftId", shiftId).putExtra("usrOrg", usrOrg)
							.putExtra("type", type), type);
				}else if(responseCode.equals("0")){
					tvPeople.setVisibility(View.GONE);
				}else if(responseCode.equals("6")){
					Intent intent = new Intent(context, FcilitiesExceptionActivity.class);
					intent.putExtra("userId", userId);
					intent.putExtra("shiftId", shiftId);
					startActivityForResult(intent, 1);
				}else if(responseCode.equals("2")){
//					tvPeople.setVisibility(View.VISIBLE);
					tvPeople.setText(adapter.getCheckText());
				}
			}
		});
		adapterList.add(adapter);
	}
	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tv_people_mind_good:
//			etPeopleMind.setText(tvPeopleMindGood.getText().toString());
//			break;
//		case R.id.tv_people_mind_exception:
////			etPeopleMind.setText(tvPeopleMindException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 0);
//			break;
//		case R.id.tv_people_mood_good:
//			etPeopleMood.setText(tvPeopleMoodGood.getText().toString());
//			break;
//		case R.id.tv_people_mood_exception:
////			etPeopleMood.setText(tvPeopleMoodException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 1);
//			break;
//		case R.id.tv_people_food_good:
//			etPeopleFood.setText(tvPeopleFoodGood.getText().toString());
//			break;
//		case R.id.tv_people_food_exception:
////			etPeopleFood.setText(tvPeopleFoodException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 2);
//			break;
//		case R.id.tv_people_dose_good:
//			etPeopleDosd.setText(tvPeopleDoseGood.getText().toString());
//			break;
//		case R.id.tv_people_dose_exception:
////			etPeopleDosd.setText(tvPeopleDosdException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 3);
//			break;
//		case R.id.tv_people_injection_good:
//			etPeopleInjection.setText(tvPeopleInjectionGood.getText().toString());
//			break;
//		case R.id.tv_people_injection_exception:
////			etPeopleInjection.setText(tvPeopleInjectionException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 4);
//			break;
//		case R.id.tv_people_skin_good:
//			etPeopleSkin.setText(tvPeopleSkinGood.getText().toString());
//			break;
//		case R.id.tv_people_skin_exception:
////			etPeopleSkin.setText(tvPeopleSkinException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 5);
//			break;
//		case R.id.tv_people_body_clean_good:
//			etPeopleBodyClean.setText(tvPeopleBodyCleanGood.getText().toString());
//			break;
//		case R.id.tv_people_body_clean_exception:
////			etPeopleBodyClean.setText(tvPeopleBodyCleanException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 6);
//			break;
//		case R.id.tv_people_clothes_clean_good:
//			etPeopleClothesClean.setText(tvPeopleClothesCleanGood.getText().toString());
//			break;
//		case R.id.tv_people_clothes_clean_exception:
////			etPeopleClothesClean.setText(tvPeopleClothesCleanException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 7);
//			break;
//		case R.id.tv_people_room_health_good:
//			etPeopleRooomHealth.setText(tvPeopleRooomHealthGood.getText().toString());
//			break;
//		case R.id.tv_people_room_health_exception:
////			etPeopleRooomHealth.setText(tvPeopleRooomHealthException.getText().toString());
//			startActivityForResult(new Intent(context, ConnextTaskExceptionActivity.class)
//				.putExtra("userId", userId).putExtra("shiftId", shiftId), 8);
//			break;
//		default:
//			break;
//		}
//	}

	private void initAnswer(){
//		for(){
//
//		}
//		strPeopleMind
	}

	private void commitTaskConnect() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		initAnswer();
//		strPeopleMind = etPeopleMind.getText().toString();
//		strPeopleMood = etPeopleMood.getText().toString();
//		strPeopleFood = etPeopleFood.getText().toString();
//		strPeopleDosd = etPeopleDosd.getText().toString();
//		strPeopleInjection = etPeopleInjection.getText().toString();
//		strPeopleSkin = etPeopleSkin.getText().toString();
//		strPeopleBodyClean = etPeopleBodyClean.getText().toString();
//		strPeopleClothesClean = etPeopleClothesClean.getText().toString();
//		strPeopleRooomHealth = etPeopleRooomHealth.getText().toString();
//		strPeopleAnother = etPeopleAnother.getText().toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("staTime", DateOperate.getCurrentTime());
		map.put("endTime", DateOperate.getCurrentTime());
		map.put("shiftId", shiftId);
//		map.put("lrjs", strPeopleMind);
//		map.put("lrqx", strPeopleMood);
//		map.put("lrys", strPeopleFood);
//		map.put("lrfy", strPeopleDosd);
//		map.put("lrzj", strPeopleInjection);
//		map.put("lrpf", strPeopleSkin);
//		map.put("stqj", strPeopleBodyClean);
//		map.put("ywqx", strPeopleClothesClean);
//		map.put("swws", strPeopleRooomHealth);
//		map.put("other", strPeopleAnother);
		map.put("lrjs", tvPeopleMind.getText().toString());
		map.put("lrqx", tvPeopleMood.getText().toString());
		map.put("lrys", tvPeopleFood.getText().toString());
		map.put("lrfy", tvPeopleDose.getText().toString());
		map.put("lrzj", tvPeopleInjection.getText().toString());
		map.put("lrpf", tvPeopleSkin.getText().toString());
		map.put("stqj", tvPeopleBodyClean.getText().toString());
		map.put("ywqx", tvPeopleClothesClean.getText().toString());
		map.put("swws", tvPeopleRooomHealth.getText().toString());
		map.put("other", tvPeopleAnother.getText().toString());
//		map.put("elderlyId", "1");
		map.put("inputUser", userId);
		map.put("usrOrg", usrOrg);

		Log.e("lrjs", strPeopleMind);
		Log.e("lrqx", strPeopleMood);
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
			public void onNext(BaseResponseEntity taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (taseList != null) {
					if (taseList.isSuccess()) {
						cleanLocalData();
						
						MainApplication.getInstance().exit(context);
						startActivity(new Intent(context, LoginActivity.class));
					} else {
						Log.e(TAG, "fail " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().updateTaskConnect(subscriber, map);
	}
	
	private void cleanLocalData(){
		SharedPreferences loginInfo = getSharedPreferences(
				getString(R.string.apk_name), MODE_PRIVATE);
		Editor editor = loginInfo.edit();
		editor.clear();
		editor.commit();
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(resultCode == 1){
				String name = data.getStringExtra("name");
				switch (requestCode) {
				case 0:
					tvPeopleMind.setVisibility(View.VISIBLE);
					tvPeopleMind.setText(tvPeopleMind.getText().toString() +  name + "老人异常情况已添加" + "," );
					break;
				case 1:
					tvPeopleMood.setVisibility(View.VISIBLE);
					tvPeopleMood.setText(tvPeopleMood.getText().toString() + name + "老人异常情况已添加" + "," );
					break;
				case 2:
					tvPeopleFood.setVisibility(View.VISIBLE);
					tvPeopleFood.setText(tvPeopleFood.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 3:
					tvPeopleDose.setVisibility(View.VISIBLE);
					tvPeopleDose.setText(tvPeopleDose.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 4:
					tvPeopleInjection.setVisibility(View.VISIBLE);
					tvPeopleInjection.setText(tvPeopleInjection.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 5:
					tvPeopleSkin.setVisibility(View.VISIBLE);
					tvPeopleSkin.setText(tvPeopleSkin.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 6:
					tvPeopleBodyClean.setVisibility(View.VISIBLE);
					tvPeopleBodyClean.setText(tvPeopleBodyClean.getText().toString() + name + "老人有特殊情况" + ",");
					break;
				case 7:
					tvPeopleClothesClean.setVisibility(View.VISIBLE);
					tvPeopleClothesClean.setText(tvPeopleClothesClean.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 8:
					tvPeopleRooomHealth.setVisibility(View.VISIBLE);
					tvPeopleRooomHealth.setText(tvPeopleRooomHealth.getText().toString() + name + "老人异常情况已添加" + ",");
					break;
				case 9:
					tvPeopleAnother.setVisibility(View.VISIBLE);
					tvPeopleAnother.setText(tvPeopleAnother.getText().toString() + name + "老人异常情况已添加" + "\n");
					break;
				default:
					break;
				}
			}else if(resultCode == 2){

				String name = data.getStringExtra("name");
				Log.e(TAG, name);
				tvPeopleAnother.setVisibility(View.VISIBLE);
				tvPeopleAnother.setText(tvPeopleAnother.getText().toString() + name + " - 本区域公共设施设备报修情况已添加" + "\n");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

}
