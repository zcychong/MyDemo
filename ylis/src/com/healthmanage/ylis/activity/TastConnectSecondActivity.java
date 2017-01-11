package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.application.MainApplication;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.AcceptConnectResponse;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.TaskConnectResponse;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TastConnectSecondActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title)
	TextView tvTitle;

	@Bind(R.id.tv_old_user)
	TextView tvOldUser;
	@Bind(R.id.tv_new_user)
	TextView tvNewUser;
	@Bind(R.id.tv_old_staff_number)
	TextView tvOldUserNumber;
	@Bind(R.id.tv_new_staff_number)
	TextView tvNewUserNumber;
	@Bind(R.id.tv_people_list)
	TextView tvPeopleList;
	@Bind(R.id.tv_people_mind)
	TextView tvPeopleMind;
	@Bind(R.id.tv_people_mood)
	TextView tvPeopleMood;
	@Bind(R.id.tv_people_food)
	TextView tvPeopleFood;
	@Bind(R.id.tv_people_dose)
	TextView tvPeopleDose;
	@Bind(R.id.tv_people_injection)
	TextView tvPeopleInjection;
	@Bind(R.id.tv_people_skin)
	TextView tvPeopleSkin;
	@Bind(R.id.tv_people_body_clean)
	TextView tvPeopleBodyClean;
	@Bind(R.id.tv_people_clothes_clean)
	TextView tvPeopleClothesClean;
	@Bind(R.id.tv_people_room_health)
	TextView tvPeopleRoomHealth;
	@Bind(R.id.tv_people_another)
	TextView tvPeopleAnother;

	@Bind(R.id.ll_connect_accept)
	LinearLayout llConnectAccept;
	@Bind(R.id.ll_connect_disagree)
	LinearLayout llConnectDisagree;
	@Bind(R.id.ll_connect_problem)
	LinearLayout llConnectProblem;

	@Bind(R.id.btn_task_list) Button btnTaskList;
	
	private String userId, time, anotherId, shiftId, usrOrg;
	private StringBuffer sbPeopleList = new StringBuffer();
	private SharedPreferences userInfo;
	private boolean close = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tast_connect_second);
		context = this;

		initView();
		initData();

		if (Network.checkNet(context)) {
			getTaskConnectInfo();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}

	private void initView() {
		ButterKnife.bind(context);
		tvTitle.setText("任务交接-任务核查");
	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		time = getIntent().getStringExtra("frequency");
		anotherId = getIntent().getStringExtra("anotherId");

		userInfo = getSharedPreferences(getString(R.string.apk_name),
				MODE_PRIVATE);
		String userName = userInfo.getString("userName", "");
		String userNo = userInfo.getString("userNo", "");
		usrOrg = userInfo.getString("usrOrg", "");

		tvNewUser.setText(userName);
		tvNewUserNumber.setText(userNo);
		Log.e(TAG, userName);
		Log.e(TAG, userNo);

		RxView.clicks(llConnectAccept).throttleFirst(2, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						// startActivity(new Intent(context,
						// MainActivity.class).
						// putExtra("userId", userId));
						// finish();
						acceptConnect();
					}
				});

		RxView.clicks(llConnectDisagree)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						refuseConnect();
					}
				});

		RxView.clicks(llConnectProblem).throttleFirst(2, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						Intent intent = new Intent(context, ReportActivity.class);
						startActivity(intent);
					}
				});
		
		RxView.clicks(btnTaskList).throttleFirst(2, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, HistroyElderlyListActivity.class);
				intent.putExtra("userId", anotherId);
				intent.putExtra("shiftId", shiftId);
				intent.putExtra("type", "1");
				startActivity(intent);
			}
		});
		
		
	}

	// private void getFalesData(){
	// tvNewUser.setText("李嘉");
	// TaskConnectResponse response = new TaskConnectResponse();
	// response.setSuccess(true);
	// response.setUserName("刘建国");
	// response.setPepleSkin("皮肤状态一切正常");
	// response.setPepleMood("一切正常");
	// response.setPepleMind("一切正常");
	// response.setPepleFood("一切正常");
	// response.setPepleClothesClean("刘老衣物都清理完毕,宋老还有一些衣服可能要再洗一次");
	// response.setPepleDose("李老今天早上不肯吃药,就没吃");
	// response.setPepleInjection("一切正常");
	// response.setPepleBodyClean("一切正常");
	// response.setPepleAnother("无");
	//
	// List<TaskConnectPeopleEntity> peopleList = new
	// ArrayList<TaskConnectPeopleEntity>();
	//
	// TaskConnectPeopleEntity people1 = new TaskConnectPeopleEntity();
	// people1.setUserName("刘经南");
	//
	// TaskConnectPeopleEntity people2 = new TaskConnectPeopleEntity();
	// people2.setUserName("宋笃");
	//
	// TaskConnectPeopleEntity people3 = new TaskConnectPeopleEntity();
	// people3.setUserName("王欧典");
	//
	// TaskConnectPeopleEntity people4 = new TaskConnectPeopleEntity();
	// people4.setUserName("冯康定");
	//
	// TaskConnectPeopleEntity people5 = new TaskConnectPeopleEntity();
	// people5.setUserName("刘光");
	//
	// peopleList.add(people1);
	// peopleList.add(people2);
	// peopleList.add(people3);
	// peopleList.add(people4);
	// peopleList.add(people5);
	//
	// response.setPeoples(peopleList);
	//
	// showData(response);
	// }

	private void acceptConnect() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("shiftId", shiftId);// 交接编号
		map.put("userId", userId);// 交接人
		map.put("execTimes", time);// 班次
		map.put("userIdH", anotherId);// 护理人
		map.put("userOrg", usrOrg);// 交接人机构

		Subscriber<AcceptConnectResponse> subscriber = new Subscriber<AcceptConnectResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(AcceptConnectResponse connectResponse) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}

				if (connectResponse != null) {
					if (connectResponse.isSuccess()) {
						startActivity(new Intent(context, MainActivity.class)
								.putExtra("userId", userId).putExtra("shiftId",
										connectResponse.getShiftId()));
						finish();
					} else {
						Log.e(TAG, "fail - " + connectResponse.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};

		HttpMethodImp.getInstance().acceptTaskConnectInfo(subscriber, map);
	}

	private void refuseConnect() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", anotherId);
		map.put("shiftId", shiftId);
		map.put("jjzt", "1");

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
			public void onNext(BaseResponseEntity connectResponse) {
				Log.e(TAG, "onNext");
				if (connectResponse != null) {
					if (connectResponse.isSuccess()) {
						MainApplication.getInstance().exit(context);
						startActivity(new Intent(context, LoginActivity.class));
					} else {
						Log.e(TAG, "fail - " + connectResponse.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};

		HttpMethodImp.getInstance().refuseTaskConnectInfo(subscriber, map);
	}

	private void getTaskConnectInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("inputId", anotherId);
		Log.e(TAG, "--" + anotherId);

		Subscriber<TaskConnectResponse> subscriber = new Subscriber<TaskConnectResponse>() {
			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(TaskConnectResponse connectResponse) {
				Log.e(TAG, "onNext");
				showData(connectResponse);
			}
		};

		HttpMethodImp.getInstance().getTaskConnectInfo(subscriber, map);
	}

	private void showData(TaskConnectResponse connectResponse) {
		if (connectResponse != null) {
			if (connectResponse.isSuccess()) {
				if (sbPeopleList != null) {
					sbPeopleList.delete(0, sbPeopleList.length());
				}
				// for(int i=0; i <connectResponse.getPeoples().size(); i++){
				// if(i != 0){
				// sbPeopleList.append(",  ");
				// }
				// sbPeopleList.append(connectResponse.getPeoples().get(i).getUserName());
				// }
				// tvPeopleList.setText(sbPeopleList.toString());
				if(connectResponse.getITEMS().get(0).getWczt().equals("0")
						|| connectResponse.getITEMS().get(0).getWczt().equals("1")){
					btnTaskList.setVisibility(View.VISIBLE);
				}else{
					btnTaskList.setVisibility(View.GONE);
				}
				if (connectResponse.getITEMS() != null) {
					if (connectResponse.getITEMS().size() > 0) {
						// tvOldUser.setText(connectResponse.getUserName());
						// Log.e(TAG, "11 - " +
						// connectResponse.getITEMS().get(0).getLrjs());
						tvOldUserNumber.setText(connectResponse.getITEMS()
								.get(0).getInputUser());
						tvOldUser.setText(connectResponse.getITEMS().get(0)
								.getInputName());
						shiftId = connectResponse.getITEMS().get(0)
								.getShiftId();
						tvPeopleMind.setText(connectResponse.getITEMS().get(0)
								.getLrjs());
						tvPeopleMood.setText(connectResponse.getITEMS().get(0)
								.getLrqx());
						tvPeopleFood.setText(connectResponse.getITEMS().get(0)
								.getLrys());
						tvPeopleDose.setText(connectResponse.getITEMS().get(0)
								.getLrfy());
						tvPeopleInjection.setText(connectResponse.getITEMS()
								.get(0).getLrzj());
						tvPeopleSkin.setText(connectResponse.getITEMS().get(0)
								.getLrpf());
						tvPeopleBodyClean.setText(connectResponse.getITEMS()
								.get(0).getStqj());
						tvPeopleClothesClean.setText(connectResponse.getITEMS()
								.get(0).getYwqx());
						tvPeopleRoomHealth.setText(connectResponse.getITEMS()
								.get(0).getSwws());
						tvPeopleAnother.setText(connectResponse.getITEMS()
								.get(0).getOther());
					}
				}

			} else {
				Log.e(TAG, "false - " + connectResponse.getMessage());
			}
		} else {
			Log.e(TAG, "false - null");
		}
	}

	// 监听返回键
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (close) {
				finish();
				System.exit(0);
			} else if (!close) {
				close = !close;
				Toast.makeText(context, getString(R.string.hint_logout),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
