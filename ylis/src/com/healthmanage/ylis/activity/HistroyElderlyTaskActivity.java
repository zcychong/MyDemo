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
import com.healthmanage.ylis.common.DateOperate;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.TaskDetailEntity;
import com.healthmanage.ylis.model.TaskDetailResponse;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistroyElderlyTaskActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_room_id) TextView tvRoomId;
	@Bind(R.id.tv_time) TextView tvTime;
	@Bind(R.id.tv_bed_id) TextView tvBedId;
	@Bind(R.id.tv_people_name) TextView tvPeopleName;
	@Bind(R.id.tv_frequency_a) TextView tvFrequencyA;
	@Bind(R.id.tv_frequency_b) TextView tvFrequencyB;
	@Bind(R.id.tv_frequency_c) TextView tvFrequencyC;
	@Bind(R.id.tv_frequency_d) TextView tvFrequencyD;
	@Bind(R.id.tv_commit) TextView tvCommit;
	@Bind(R.id.tv_edit_48hours_record) TextView tvHoursRecord;
	
	@Bind(R.id.gv_task_item_a) GridViewForScrollView gvTaskListA;
	@Bind(R.id.gv_task_item_b) GridViewForScrollView gvTaskListB;
	@Bind(R.id.gv_task_item_c) GridViewForScrollView gvTaskListC;
	@Bind(R.id.gv_task_item_d) GridViewForScrollView gvTaskListD;
	
	@Bind(R.id.rl_frequency_a) RelativeLayout rlFrequencyA;
	@Bind(R.id.rl_frequency_b) RelativeLayout rlFrequencyB;
	@Bind(R.id.rl_frequency_c) RelativeLayout rlFrequencyC;
	@Bind(R.id.rl_frequency_d) RelativeLayout rlFrequencyD;
	
	private String userId, elderlyId, shiftId, ishous;
	private TaskAdapter adapterA, adapterB, adapterC, adapterD;
	private TaskDetailResponse response;
	private List<TaskDetailEntity> taskDetailListA = new ArrayList<TaskDetailEntity>();
	private List<TaskDetailEntity> taskDetailListB = new ArrayList<TaskDetailEntity>();
	private List<TaskDetailEntity> taskDetailListC = new ArrayList<TaskDetailEntity>();
	private List<TaskDetailEntity> taskDetailListD = new ArrayList<TaskDetailEntity>();
	private StringBuffer detiIds = new StringBuffer();

	private String peoName, bedID, roomId, userNo;
	private SharedPreferences userInfo;
	private String frequency, type; //type 0-可以完成 1-不能完成

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_histroy_elderly_task);
		context = this;

		initView();
		initData();

		if (Network.checkNet(context)) {
			getTastList();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
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

		RxView.clicks(tvCommit).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						commit();
					}
				});

		RxView.clicks(tvHoursRecord).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						startActivity(new Intent(context,
								HoursRecordActivity.class).putExtra(
								"elderlyId", elderlyId).putExtra("userId",
								userId));
					}
				});
	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		
		elderlyId = getIntent().getStringExtra("elderlyId");
		peoName = getIntent().getStringExtra("peoName");
		bedID = getIntent().getStringExtra("bedId");
		roomId = getIntent().getStringExtra("roomId");
		shiftId = getIntent().getStringExtra("shiftId");
		ishous = getIntent().getStringExtra("ishous");
		if (StringUtils.isNotEmpty(ishous)) {
			if (ishous.equals("1")) {
				tvHoursRecord.setVisibility(View.VISIBLE);
			} else {
				tvHoursRecord.setVisibility(View.GONE);
			}
		}
		type = getIntent().getStringExtra("type");

		Log.e(TAG, "peoName=" + peoName);
		Log.e(TAG, "bedID=" + bedID);

		tvBedId.setText("床号:" + bedID);
		tvPeopleName.setText(peoName);
		tvTime.setText(DateOperate.getCurrentDataWithSpe());
		tvRoomId.setText(roomId);

		tvTitle.setText(peoName);

		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);

		frequency = userInfo.getString("execTime", "");
		userNo = userInfo.getString("userNo", "");
	}
	
	private void getTastList() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("elderlyId", elderlyId);
		map.put("shiftId", shiftId);
		map.put("inputUser", userNo);
		if(type.equals("0")){
			map.put("wczt", "2,3");
//			Log.e("wczt", "2,3");
		}else{
			map.put("wczt", "0");
//			Log.e("wczt", "0");
			tvCommit.setVisibility(View.GONE);
		}
		
//		Log.e("inputUser", userNo);
//		Log.e("wczt", "2,3");
//		Log.e("elderlyId", elderlyId);
//		Log.e("shiftId", shiftId);
//		Log.e("frequency", frequency);
		
		Subscriber<TaskDetailResponse> subscriber = new Subscriber<TaskDetailResponse>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(TaskDetailResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						showTaskList(taseList);
						response = taseList;
					} else {
						Log.e(TAG, "fail -" + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().getTestDetailInfo(subscriber, map);
	}
	
	private void commit() {
		Map<String, String> map = new HashMap<String, String>();
		if (detiIds.length() > 0) {
			detiIds.delete(0, detiIds.length());
		}
		for (int i = 0; i < response.getITEMS().size(); i++) {
			String detiId = response.getITEMS().get(i).getDetiId();
			String wczt = response.getITEMS().get(i).getWczt();
			if (wczt.equals("8")) {
				if (detiIds.length() > 0) {
					detiIds.append(",");
				}
				detiIds.append(detiId);
				detiIds.append("-");
				detiIds.append("1");
			} else if (wczt.equals("9")) {
				if (detiIds.length() > 0) {
					detiIds.append(",");
				}
				detiIds.append(detiId);
				detiIds.append("-");
				detiIds.append("3");
			}
		}

		if (detiIds.length() == 0) {
			Toast.makeText(context, "请选择已完成的任务!", Toast.LENGTH_SHORT).show();
			return;
		}
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		map.put("detiIds", detiIds.toString());

		Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(BaseResponseEntity taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						setResult(1);
						Toast.makeText(context, "所选任务已完成!", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Log.e(TAG, "fail -" + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().updateTestDetailInfo(subscriber, map);
	}

	private void showTaskList(final TaskDetailResponse taseList) {
		// tvRoomId.setText(taseList.getRoomName());
		// tvTime.setText(taseList.getTime());
		// tvBedId.setText(taseList.getBedName());
		// tvPeopleName.setText(taseList.getPeopleName());
		
			tvFrequencyA.setText("晨间");
			tvFrequencyB.setText("上午");
			tvFrequencyC.setText("下午");
			tvFrequencyD.setText("晚间");
			for (TaskDetailEntity item : taseList.getITEMS()) {
				if(item.getExecTime().equals("A")){
					taskDetailListA.add(item);
				}else if(item.getExecTime().equals("B")){
					taskDetailListB.add(item);
				}else if(item.getExecTime().equals("C")){
					taskDetailListC.add(item);
				}else if(item.getExecTime().equals("D")){
					taskDetailListD.add(item);
				}

			}
			if(taskDetailListA.size() == 0){
				gvTaskListA.setVisibility(View.GONE);
				rlFrequencyA.setVisibility(View.GONE);
			}else{
				gvTaskListA.setVisibility(View.VISIBLE);
				rlFrequencyA.setVisibility(View.VISIBLE);
			}
			if(taskDetailListB.size() == 0){
				gvTaskListB.setVisibility(View.GONE);
				rlFrequencyB.setVisibility(View.GONE);
			}else{
				gvTaskListB.setVisibility(View.VISIBLE);
				rlFrequencyB.setVisibility(View.VISIBLE);
			}
			if(taskDetailListC.size() == 0){
				gvTaskListC.setVisibility(View.GONE);
				rlFrequencyC.setVisibility(View.GONE);
			}else{
				gvTaskListC.setVisibility(View.VISIBLE);
				rlFrequencyC.setVisibility(View.VISIBLE);
			}
			if(taskDetailListD.size() == 0){
				gvTaskListD.setVisibility(View.GONE);
				rlFrequencyD.setVisibility(View.GONE);
			}else{
				gvTaskListD.setVisibility(View.VISIBLE);
				rlFrequencyD.setVisibility(View.VISIBLE);
			}
			adapterA = new TaskAdapter(context, taskDetailListA);
			gvTaskListA.setAdapter(adapterA);
			if(type.equals("0")){
				gvTaskListA.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (StringUtils.isNotEmpty(taskDetailListA.get(position)
								.getIsCollect())) {
							if (taskDetailListA.get(position).getIsCollect().equals("0")) {// 无二级任务
								if (taskDetailListA.get(position).getWczt().equals("2")) {
									taskDetailListA.get(position).setWczt("9");
								} else if (taskDetailListA.get(position).getWczt().equals("9")) {
									taskDetailListA.get(position).setWczt("2");
								}
								adapterA.notifyDataSetChanged();
							} else {
	
							}
						}else{
							if (taskDetailListA.get(position).getWczt().equals("2")) {
								taskDetailListA.get(position).setWczt("9");
							} else if (taskDetailListA.get(position).getWczt().equals("9")) {
								taskDetailListA.get(position).setWczt("2");
							}
							adapterA.notifyDataSetChanged();
						}
	
					}
				});
			}
			
			adapterB = new TaskAdapter(context, taskDetailListB);
			gvTaskListB.setAdapter(adapterB);
			if(type.equals("0")){
				gvTaskListB.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (StringUtils.isNotEmpty(taskDetailListB.get(position).getIsCollect())) {
							if (taskDetailListB.get(position).getIsCollect().equals("0")) {// 无二级任务
								if (taskDetailListB.get(position).getWczt().equals("2")) {
									taskDetailListB.get(position).setWczt("9");
								} else if (taskDetailListB.get(position).getWczt()
										.equals("9")) {
									taskDetailListB.get(position).setWczt("2");
								}
								adapterB.notifyDataSetChanged();
							} else {
	
							}
						}else{
							if (taskDetailListB.get(position).getWczt().equals("2")) {
								taskDetailListB.get(position).setWczt("9");
							} else if (taskDetailListB.get(position).getWczt()
									.equals("9")) {
								taskDetailListB.get(position).setWczt("2");
							}
							adapterB.notifyDataSetChanged();
						}
					}
				});
			}
			
			
			adapterC = new TaskAdapter(context, taskDetailListC);
			gvTaskListC.setAdapter(adapterC);
			if(type.equals("0")){
				gvTaskListC.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (StringUtils.isNotEmpty(taskDetailListC.get(position).getIsCollect())) {
							if (taskDetailListC.get(position).getIsCollect().equals("0")) {// 无二级任务
								if (taskDetailListC.get(position).getWczt().equals("2")) {
									taskDetailListC.get(position).setWczt("9");
								} else if (taskDetailListC.get(position).getWczt()
										.equals("9")) {
									taskDetailListC.get(position).setWczt("2");
								}
								adapterC.notifyDataSetChanged();
							} else {
	
							}
						}else{
							if (taskDetailListC.get(position).getWczt().equals("2")) {
								taskDetailListC.get(position).setWczt("9");
							} else if (taskDetailListC.get(position).getWczt()
									.equals("9")) {
								taskDetailListC.get(position).setWczt("2");
							}
							adapterC.notifyDataSetChanged();
						}
					}
				});
			}
			
			adapterD = new TaskAdapter(context, taskDetailListD);
			gvTaskListD.setAdapter(adapterD);
			if(type.equals("0")){
				gvTaskListD.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (StringUtils.isNotEmpty(taskDetailListD.get(position).getIsCollect())) {
							if (taskDetailListD.get(position).getIsCollect().equals("0")) {// 无二级任务
								if (taskDetailListD.get(position).getWczt().equals("2")) {
									taskDetailListD.get(position).setWczt("9");
								} else if (taskDetailListD.get(position).getWczt()
										.equals("9")) {
									taskDetailListD.get(position).setWczt("2");
								}
								adapterD.notifyDataSetChanged();
							} else {
	
							}
						}else{
							if (taskDetailListD.get(position).getWczt().equals("2")) {
								taskDetailListD.get(position).setWczt("9");
							} else if (taskDetailListD.get(position).getWczt()
									.equals("9")) {
								taskDetailListD.get(position).setWczt("2");
							}
							adapterD.notifyDataSetChanged();
						}
					}
				});
			}
			
	}

	class TaskAdapter extends BaseAdapter {
		private List<TaskDetailEntity> taskList;

		public TaskAdapter(Context context, List<TaskDetailEntity> list) {
			taskList = list;
		}

		@Override
		public int getCount() {
			return taskList.size();
		}

		@Override
		public Object getItem(int position) {
			return taskList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.task_item, null);
				viewHolder = new ViewHolder();
				viewHolder.ivFinishState = (ImageView) convertView
						.findViewById(R.id.iv_finish_state);
				viewHolder.ivSecondTask = (ImageView) convertView
						.findViewById(R.id.iv_second_task);
				viewHolder.tvTaskName = (TextView) convertView
						.findViewById(R.id.tv_task_name);
				viewHolder.llView = (LinearLayout) convertView
						.findViewById(R.id.ll_view);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String strWczt = taskList.get(position).getWczt();
			if (StringUtils.isNotEmpty(strWczt)) {
				if (strWczt.equals("0") || strWczt.equals("2")) {
					viewHolder.ivFinishState
							.setImageResource(R.drawable.icon_task_default);
				} else if (strWczt.equals("1") || strWczt.equals("3")) {
					viewHolder.ivFinishState
							.setImageResource(R.drawable.icon_task_finishd);
				} else {
					viewHolder.ivFinishState
							.setImageResource(R.drawable.icon_task_checked);
				}
			}

			if (StringUtils.isNotEmpty(taskList.get(position).getIsCollect())) {
				if (taskList.get(position).getIsCollect().equals("0")) {
					viewHolder.ivSecondTask.setVisibility(View.GONE);
				} else {
					viewHolder.ivSecondTask.setVisibility(View.VISIBLE);
				}
			}

			if (StringUtils.isNotEmpty(taskList.get(position).getItemName())) {
				viewHolder.tvTaskName.setText(taskList.get(position)
						.getItemName());
			}
			
			

			return convertView;
		}

		class ViewHolder {
			private ImageView ivFinishState, ivSecondTask;
			private TextView tvTaskName;
			private LinearLayout llView;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
