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
import com.healthmanage.ylis.adapter.MaybeTaskApdater;
import com.healthmanage.ylis.adapter.TaskListModelTwoApdater;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.GetMaybeTaskResponse;
import com.healthmanage.ylis.model.GetTaskListModelTwoLResponse;
import com.healthmanage.ylis.model.TaskItemModelTwo;
import com.healthmanage.ylis.model.TaskMaybeModelTwo;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.model.TastPersonEntity;
import com.healthmanage.ylis.model.TastRoomEntity;
import com.healthmanage.ylis.service.FindDoseService;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.healthmanage.ylis.view.ListViewForScrollView;
import com.healthmanage.ylis.view.UserRoomItemView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;

	@Bind(R.id.tv_option) TextView tvOption;
	@Bind(R.id.iv_option) ImageView ivOption;
	@Bind(R.id.ll_option) LinearLayout llOption;

	@Bind(R.id.v_commit_line) View vCommitLine;
	@Bind(R.id.v_maybe_line) View vMaybeLine;
	@Bind(R.id.tv_commit_task) TextView tvCommitTask;
	@Bind(R.id.tv_maybe_task) TextView tvMaybeTask;
	@Bind(R.id.gv_maybe_task_list) GridViewForScrollView gvMaybeList;

	@Bind(R.id.ll_room_list) LinearLayout llRoomList;
	// @Bind(R.id.lv_task_list) ListViewForScrollView lvTaskList;
	@Bind(R.id.ll_task_connect) LinearLayout llTaskConnect;
	
	@Bind(R.id.ll_histroy_task) LinearLayout llHistroyTask;//遗留任务
	
	@Bind(R.id.ll_leave) LinearLayout llLeave;//请假
	@Bind(R.id.tv_edit_tesk_connect) TextView tvEditTaskConnect;//交接
	@Bind(R.id.ll_sannitation) LinearLayout llSannitation;//环境卫生
	@Bind(R.id.ll_patrol) LinearLayout llPatroal;// 巡视记录
	@Bind(R.id.rl_dose) RelativeLayout rlDose;
	@Bind(R.id.tv_dose_msg) TextView tvDoseMsg;
	@Bind(R.id.tv_help_dose) TextView tvHelpDose;
	
	private String userId, shiftId, execTime, usrOrg;
	private boolean close = false;
	private boolean model2 = true;
	private boolean completStatus = false;
	private boolean isCommitTask = true;
	private ListViewForScrollView lvTaskList;
	private TaskListModelTwoApdater modelTwoAdapter;
	private MaybeTaskApdater maybeTaskAdapter;
	private StringBuffer peopleNames = new StringBuffer();

	private FindDoseService countService;
	private ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		public void onServiceConnected(ComponentName name, IBinder service) {
			countService = ((FindDoseService.ServiceBinder) service).getService();
			Log.e(TAG, "countService != null");

		}

		/** 无法获取到服务对象时的操作 */
		public void onServiceDisconnected(ComponentName name) {
			Log.e(TAG, "countService == null");
			countService = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

		initView();
		initData();

		// getFalsePeopleList();
		initService();
		if (Network.checkNet(context)) {
			getTaskList();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}

		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("ylis.dose");
		intentfilter.addAction("ylis.dose.finish");
		registerReceiver(newDoseReceiver, intentfilter);
	}



	private void initService(){
		bindService(new Intent(context, FindDoseService.class), conn, Context.BIND_AUTO_CREATE);
	}

	private void getTaskList() {
		if (model2) {
			llRoomList.setVisibility(View.GONE);
			lvTaskList.setVisibility(View.VISIBLE);
			getPeopleListModel2();
			getMaybeTaskList();
		} else {
			llRoomList.setVisibility(View.VISIBLE);
			lvTaskList.setVisibility(View.GONE);
			getPeopleListModel1();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("老年福智慧养老信息化管理系统");
		llBack.setVisibility(View.VISIBLE);
		llOption.setVisibility(View.VISIBLE);
		ivBack.setImageResource(R.drawable.icon_setting);
		tvBackText.setText("设置");

//		tvTitle.setText("发药管理");

//		ivOption.setImageResource(R.drawable.icon_change_model);
//		tvOption.setText("切换");

		RxView.clicks(llBack).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context, SettingInfoActivity.class).
						putExtra("userId", userId));
			}
		});
		
		RxView.clicks(llLeave).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context, LeaveActivity.class));
			}
		});
		
		
		RxView.clicks(llHistroyTask).
		throttleFirst(2, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						Intent intent = new Intent(context, HistroyElderlyListActivity.class);
						intent.putExtra("userId", userId);
						intent.putExtra("shiftId", shiftId);
						intent.putExtra("type", "0");
						startActivity(intent);
					}
				});
		
//		RxView.clicks(llOption).
//		throttleFirst(2, TimeUnit.SECONDS)
//				.subscribe(new Action1<Void>() {
//					@Override
//					public void call(Void aVoid) {
//						model2 = !model2;
//						getTaskList();
//					}
//				});

		RxView.clicks(llTaskConnect).throttleFirst(2, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {

						startActivity(new Intent(context,
								EditTaskConnectActivity.class)
								.putExtra("userId", userId)
								.putExtra("shiftId", shiftId));
					}
				});
		
		
		RxView.clicks(llPatroal).throttleFirst(2, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context,
						PatroalActivity.class)
						.putExtra("userId", userId)
						.putExtra("shiftId", shiftId)
						.putExtra("usrOrg", usrOrg)
						.putExtra("execTime",execTime));
			}
		});
		
		RxView.clicks(llSannitation).throttleFirst(2, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context,
						SannitationActivity.class)
						.putExtra("userId", userId)
						.putExtra("shiftId", shiftId)
						.putExtra("usrOrg", usrOrg)
						.putExtra("execTime",execTime));
			}
		});

		lvTaskList = (ListViewForScrollView) findViewById(R.id.lv_task_list);
		
		RxView.clicks(tvCommitTask).throttleFirst(1, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showTask(true);
			}
		});
		RxView.clicks(tvMaybeTask).throttleFirst(1, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showTask(false);
			}
		});

		RxView.clicks(tvHelpDose).throttleFirst(1, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						Intent intent = new Intent(context, DoseElderlyListActivity.class);
						intent.putExtra("userId", userId);
						startActivityForResult(intent, 1);
					}
				});
	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		execTime = getIntent().getStringExtra("execTime");
		usrOrg = getIntent().getStringExtra("usrOrg");
		Log.e(TAG, "execTime=" + execTime);
		if(StringUtils.isNotEmpty(execTime)){
			if(execTime.equals("A,B,C")){
				llSannitation.setVisibility(View.VISIBLE);
			}else{
				llSannitation.setVisibility(View.GONE);
			}
		}
		
		completStatus = getIntent().getBooleanExtra("completStatus", true);//true 全完成
		
//		if(completStatus){
//			llHistroyTask.setVisibility(View.GONE);
//		}else{
//			llHistroyTask.setVisibility(View.VISIBLE);
//		}
	}
	
	private void showTask(boolean isShow){
		if(isCommitTask){
			if(!isShow){
				tvCommitTask.setTextColor(getResources().getColor(R.color.main_black));
				vCommitLine.setVisibility(View.INVISIBLE);
				tvMaybeTask.setTextColor(getResources().getColor(R.color.main_color));
				vMaybeLine.setVisibility(View.VISIBLE);
				lvTaskList.setVisibility(View.GONE);
				gvMaybeList.setVisibility(View.VISIBLE);
				isCommitTask = false;
			}
		}else{
			if(isShow){
				tvMaybeTask.setTextColor(getResources().getColor(R.color.main_black));
				vMaybeLine.setVisibility(View.INVISIBLE);
				tvCommitTask.setTextColor(getResources().getColor(R.color.main_color));
				vCommitLine.setVisibility(View.VISIBLE);
				lvTaskList.setVisibility(View.VISIBLE);
				gvMaybeList.setVisibility(View.GONE);
				isCommitTask = true;
			}
		}
	}

	private void getFalsePeopleList() {
		TastListResponse response = new TastListResponse();
		response.setSuccess(true);
		// response.setOrgName("敬老院");
		List<TastRoomEntity> roomList = new ArrayList<TastRoomEntity>();
		TastRoomEntity room1 = new TastRoomEntity();
		room1.setRoomId("1111");
		room1.setRoomNo("4-001");

		TastRoomEntity room2 = new TastRoomEntity();
		room2.setRoomId("222");
		room2.setRoomNo("4-002");

		List<TastPersonEntity> personList = new ArrayList<TastPersonEntity>();
		List<TastPersonEntity> personList2 = new ArrayList<TastPersonEntity>();

		TastPersonEntity person1 = new TastPersonEntity();
		person1.setState("0");
		person1.setElderlyName("宋锦江");
		person1.setElderlySta("0");
		person1.setIshous("0");
		person1.setBedId("1");
		person1.setBedNo("1");
		personList.add(person1);

		TastPersonEntity person2 = new TastPersonEntity();
		person2.setState("0");
		person2.setElderlyName("刘国樑");
		person2.setElderlySta("0");
		person2.setIshous("0");
		person2.setBedId("2");
		person2.setBedNo("2");
		personList.add(person2);

		TastPersonEntity person3 = new TastPersonEntity();
		person3.setState("0");
		person3.setElderlyName("赵坤");
		person3.setElderlySta("0");
		person3.setIshous("0");
		person3.setBedId("4");
		person3.setBedNo("4");
		personList.add(person3);

		TastPersonEntity person4 = new TastPersonEntity();
		person4.setState("0");
		person4.setElderlyName("张杨");
		person4.setElderlySta("0");
		person4.setIshous("0");
		person4.setBedId("2");
		person4.setBedNo("2");
		personList2.add(person4);

		TastPersonEntity person5 = new TastPersonEntity();
		person5.setState("0");
		person5.setElderlyName("李嗣");
		person5.setElderlySta("0");
		person5.setIshous("0");
		person5.setBedId("3");
		person5.setBedNo("3");
		personList2.add(person5);

		TastPersonEntity person6 = new TastPersonEntity();
		person6.setState("0");
		person6.setElderlyName("何彦");
		person6.setElderlySta("0");
		person6.setIshous("1");
		person6.setBedId("5");
		person6.setBedNo("5");
		personList2.add(person6);

		room1.setBeds(personList);
		roomList.add(room1);

		room2.setBeds(personList2);
		roomList.add(room2);

		response.setITEMS(roomList);
		dealResponseData(response);
	}

	private void getPeopleListModel1() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		Subscriber<TastListResponse> subscriber = new Subscriber<TastListResponse>() {

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
			public void onNext(final TastListResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (taseList != null) {
					if (taseList.isSuccess()) {
						dealResponseData(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getTestListInfo(subscriber, map);
	}

	private void dealResponseData(final TastListResponse taseList) {
		if (llRoomList.getChildCount() > 0) {
			llRoomList.removeAllViews();
		}
		if (taseList != null) {
			if (taseList.isSuccess()) {
				// tvTitle.setText(taseList.getOrgName());
				for (int i = 0; i < taseList.getITEMS().size(); i++) {
					final int index = i;
					UserRoomItemView roomView = new UserRoomItemView(context,
							taseList.getITEMS().get(i));
					llRoomList.addView(roomView);
					roomView.getGvRoomList().setOnItemClickListener(
							new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									if(!taseList.getITEMS().get(index).getBeds()
											.get(position).getElderlySta().equals("3")){
										Intent intent = new Intent(context,
												TaskDetailActivity.class);
										intent.putExtra("userId", userId);
										intent.putExtra("shiftId", shiftId);
										intent.putExtra("ishous", taseList
												.getITEMS().get(index).getBeds()
												.get(position).getIshous());
										intent.putExtra("elderlyId", taseList
												.getITEMS().get(index).getBeds()
												.get(position).getElderlyId());
										intent.putExtra("bedId", taseList
												.getITEMS().get(index).getBeds()
												.get(position).getBedNo());
										intent.putExtra("peoName", taseList
												.getITEMS().get(index).getBeds()
												.get(position).getElderlyName());
										intent.putExtra("roomId", taseList
												.getITEMS().get(index).getRoomNo());
										Log.e(TAG, "elderlyId="
												+ taseList.getITEMS().get(index)
														.getBeds().get(position)
														.getElderlyId());
										startActivityForResult(intent, 0);
									}
									
								}
							});
				}

				for (int i = 0; i < taseList.getITEMS().size(); i++) {
					for (int j = 0; j < taseList.getITEMS().get(i).getBeds()
							.size(); j++) {
						if (i != 0 || j != 0) {
							peopleNames.append(",");
						}
						peopleNames.append(taseList.getITEMS().get(i).getBeds()
								.get(j).getElderlyName());
					}
				}
			}
		}
	}

	private void getPeopleListModel2() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("inputUser", userId);
		map.put("shiftId", shiftId);
		map.put("shiftId", shiftId);
		map.put("wczt", "0,1");

		Subscriber<GetTaskListModelTwoLResponse> subscriber = new Subscriber<GetTaskListModelTwoLResponse>() {

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
			public void onNext(GetTaskListModelTwoLResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						dealModelTwoData(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getTaskListModelTwo(subscriber, map);
		
	}
	
	private void getMaybeTaskList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("inputUser", userId);
//		map.put("shiftId", shiftId);
//		map.put("execTime", "E");
		
		Log.e(TAG + "inputUser", userId);
		Log.e(TAG + "shiftId", shiftId);
		Log.e(TAG + "execTime", "E");

		Subscriber<GetMaybeTaskResponse> subscriber = new Subscriber<GetMaybeTaskResponse>() {

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
			public void onNext(GetMaybeTaskResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						dealMaybeTaskList(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getMaybeTaskList(subscriber, map);
	}

	private void getFalseTaskModel2() {
		GetTaskListModelTwoLResponse res = new GetTaskListModelTwoLResponse();
		res.setSuccess(true);

		List<TaskItemModelTwo> list = new ArrayList<TaskItemModelTwo>();
		TaskItemModelTwo item1 = new TaskItemModelTwo();
		item1.setExecTime("A");
		item1.setItemName("洗脸");
		item1.setFinishCount("0");
		item1.setSunCount("4");

		TaskItemModelTwo item2 = new TaskItemModelTwo();
		item2.setExecTime("B");
		item2.setItemName("吃饭");
		item2.setFinishCount("2");
		item2.setSunCount("4");

		TaskItemModelTwo item3 = new TaskItemModelTwo();
		item3.setExecTime("C");
		item3.setItemName("吃饭");
		item3.setFinishCount("1");
		item3.setSunCount("1");

		TaskItemModelTwo item4 = new TaskItemModelTwo();
		item4.setExecTime("D");
		item4.setItemName("洗脚");
		item4.setFinishCount("2");
		item4.setSunCount("4");

		list.add(item1);
		list.add(item2);
		list.add(item3);
		list.add(item4);

		res.setITEMS(list);

		dealModelTwoData(res);
	}
	
	private void dealMaybeTaskList(final GetMaybeTaskResponse response){
		maybeTaskAdapter = new MaybeTaskApdater(context,
				response.getITEMS());
		gvMaybeList.setAdapter(maybeTaskAdapter);
		
		gvMaybeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				TaskMaybeModelTwo item = response.getITEMS().get(position);
				if(item != null){
					if(StringUtils.isNotEmpty(item.getIsCollect())){
						if(item.getIsCollect().equals("1")){
							if(StringUtils.isNotEmpty(item.getQuestNoes())){
								if(item.getQuestNoes().equals(getResources().getString(R.string.questNoes_6))){// 洗衣
									Intent intent = new Intent(context,LaundryActivity.class);
									intent.putExtra("usrOrg", usrOrg);
									intent.putExtra("userId", userId);
									intent.putExtra("shiftId", shiftId);
									intent.putExtra("execTime", execTime);
									intent.putExtra("bedNo", item.getBedNo());
									startActivity(intent);
								}else if(item.getQuestNoes().equals(getResources().getString(R.string.questNoes_3))){//翻身
									Intent intent = new Intent(context,ExpandTurnBodyActivity.class);
									intent.putExtra("usrOrg", usrOrg);
									intent.putExtra("userId", userId);
									intent.putExtra("shiftId", shiftId);
									intent.putExtra("execTime", execTime);
									intent.putExtra("bedNo", item.getBedNo());
									startActivity(intent);
								}
							}
						}else{
							Intent intent = new Intent(context,TaskDetailModelTwoActivity.class);
							intent.putExtra("userId", userId);
							intent.putExtra("usrOrg", usrOrg);
							intent.putExtra("bedNo", response.getITEMS().get(position).getGoodId());
							intent.putExtra("inputUser", response.getITEMS().get(position).getInputUser());
							intent.putExtra("shiftId", shiftId);
							intent.putExtra("itemName", response.getITEMS().get(position).getGoodName());
							intent.putExtra("execTime", response.getITEMS().get(position).getExecTime());
							intent.putExtra("isCollect", response.getITEMS().get(position).getIsCollect());
							intent.putExtra("questNoes", response.getITEMS().get(position).getQuestNoes());
							intent.putExtra("type", "1");
							startActivity(intent);
						}
					}
				}
			}
		});
	}

	private void dealModelTwoData(final GetTaskListModelTwoLResponse response) {

		modelTwoAdapter = new TaskListModelTwoApdater(context,
				response.getITEMS());
		Log.e(TAG, "lvTaskList");
		lvTaskList.setAdapter(modelTwoAdapter);

		lvTaskList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = null;
				if(StringUtils.isNotEmpty(response.getITEMS().get(position).getIsCollect())){
					if(response.getITEMS().get(position).getIsCollect().equals("1")){
						if(response.getITEMS().get(position).getQuestNoes().equals(getString(R.string.questNoes_6))){
							intent = new Intent(context,LaundryActivity.class);
							intent.putExtra("usrOrg", usrOrg);
						}else{
							intent = new Intent(context,TaskDetailModelTwoActivity.class);
						}
					}else{
						intent = new Intent(context,TaskDetailModelTwoActivity.class);
					}
				}else{
					intent = new Intent(context,TaskDetailModelTwoActivity.class);
				}
				
				intent.putExtra("userId", userId);
				intent.putExtra("usrOrg", usrOrg);
				intent.putExtra("bedNo", response.getITEMS().get(position).getBedNo());
				intent.putExtra("inputUser", response.getITEMS().get(position).getInputUser());
				intent.putExtra("shiftId", response.getITEMS().get(position).getShiftId());
				intent.putExtra("itemName", response.getITEMS().get(position).getItemName());
				intent.putExtra("execTime", response.getITEMS().get(position).getExecTime());
				intent.putExtra("isCollect", response.getITEMS().get(position).getIsCollect());
				intent.putExtra("questNoes", response.getITEMS().get(position).getQuestNoes());
				intent.putExtra("type", "0");
				startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 1) {

			}
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

	private BroadcastReceiver newDoseReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "onReceive");
			if(intent != null){
				if(intent.getAction().equals("ylis.dose")){
					String count = intent.getStringExtra("count");
					int con = Integer.valueOf(count);
					if(con != 0){
						rlDose.setVisibility(View.VISIBLE);
						tvDoseMsg.setText("有" + count + "位老人已发药,请准时协助服药!");
					}else{
						rlDose.setVisibility(View.GONE);
					}

				}else if(intent.getAction().equals("ylis.dose.finish")){
					rlDose.setVisibility(View.GONE);
				}

			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(newDoseReceiver);

		ButterKnife.unbind(this);
	}
}
