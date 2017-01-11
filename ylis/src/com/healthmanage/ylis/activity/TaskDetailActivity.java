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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TaskDetailActivity extends BaseActivity {
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
	@Bind(R.id.tv_commit) TextView tvCommit;
	@Bind(R.id.tv_edit_48hours_record) TextView tvHoursRecord;
	
	@Bind(R.id.gv_task_item_a) GridViewForScrollView gvTaskListA;
	@Bind(R.id.gv_task_item_b) GridViewForScrollView gvTaskListB;
	@Bind(R.id.gv_task_item_c) GridViewForScrollView gvTaskListC;
	
	@Bind(R.id.rl_frequency_a) RelativeLayout rlFrequencyA;
	@Bind(R.id.rl_frequency_b) RelativeLayout rlFrequencyB;
	@Bind(R.id.rl_frequency_c) RelativeLayout rlFrequencyC;

	private String userId, elderlyId, shiftId, ishous;
	private TaskAdapter adapterA, adapterB, adapterC;
	private TaskDetailResponse response;
	private List<TaskDetailEntity> taskDetailListA = new ArrayList<TaskDetailEntity>();
	private List<TaskDetailEntity> taskDetailListB = new ArrayList<TaskDetailEntity>();
	private List<TaskDetailEntity> taskDetailListC = new ArrayList<TaskDetailEntity>();
	
//	private List<TaskDetailEntity> leaveTasks = new ArrayList<TaskDetailEntity>();
	private StringBuffer detiIds = new StringBuffer();

	private String peoName, bedID, roomId, userNo;
	private SharedPreferences userInfo;
	private String frequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		context = this;

		initView();
		initData();

		if (Network.checkNet(context)) {
			getTastList();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
		// getFalieTastList();
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

	private void getFalieTastList() {
		TaskDetailResponse response = new TaskDetailResponse();
		response.setBedName("一号床");
		response.setFrequency("下午");
		response.setPeopleName("刘毅");
		response.setRoomName("101房间");
		response.setTime("2016-11-11");
		response.setSuccess(true);

		List<TaskDetailEntity> taskDetailList = new ArrayList<TaskDetailEntity>();

		TaskDetailEntity detail1 = new TaskDetailEntity();
		detail1.setItemName("洗脸");
		detail1.setIsCollect("0");
		detail1.setWczt("0");

		TaskDetailEntity detail2 = new TaskDetailEntity();
		detail2.setItemName("刷牙");
		detail2.setIsCollect("0");
		detail2.setWczt("0");

		TaskDetailEntity detail3 = new TaskDetailEntity();
		detail3.setItemName("吃早饭");
		detail3.setIsCollect("0");
		detail3.setWczt("0");

		TaskDetailEntity detail4 = new TaskDetailEntity();
		detail4.setItemName("吃药");
		detail4.setIsCollect("1");
		detail4.setWczt("0");

		TaskDetailEntity detail5 = new TaskDetailEntity();
		detail5.setItemName("翻身");
		detail5.setIsCollect("0");
		detail5.setWczt("1");

		TaskDetailEntity detail6 = new TaskDetailEntity();
		detail6.setItemName("擦身体");
		detail6.setIsCollect("0");
		detail6.setWczt("0");

		TaskDetailEntity detail7 = new TaskDetailEntity();
		detail7.setItemName("收拾屋子");
		detail7.setIsCollect("0");
		detail7.setWczt("1");

		taskDetailList.add(detail1);
		taskDetailList.add(detail2);
		taskDetailList.add(detail3);
		taskDetailList.add(detail4);
		taskDetailList.add(detail5);
		taskDetailList.add(detail6);
		taskDetailList.add(detail7);

		response.setITEMS(taskDetailList);

		List<TaskDetailEntity> leaveTasks = new ArrayList<TaskDetailEntity>();

		TaskDetailEntity lDetail1 = new TaskDetailEntity();
		lDetail1.setItemName("洗脸");
		lDetail1.setIsCollect("0");
		lDetail1.setWczt("0");

		TaskDetailEntity lDetail2 = new TaskDetailEntity();
		lDetail2.setItemName("刷牙");
		lDetail2.setIsCollect("0");
		lDetail2.setWczt("0");

		leaveTasks.add(lDetail1);
		leaveTasks.add(lDetail2);

		showTaskList(response);
	}

	private void getTastList() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("inputUser", userNo);
		map.put("elderlyId", elderlyId);
		map.put("shiftId", shiftId);
		map.put("execTime", frequency);
		
		Log.e("inputUser", userNo);
		Log.e("elderlyId", elderlyId);
		Log.e("shiftId", shiftId);
		Log.e("execTime", frequency);

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
		
		if(frequency.equals("D")){
			tvFrequencyA.setText("晚间");
			rlFrequencyB.setVisibility(View.GONE);
			rlFrequencyC.setVisibility(View.GONE);
			gvTaskListB.setVisibility(View.GONE);
			gvTaskListC.setVisibility(View.GONE);
			for (TaskDetailEntity item : taseList.getITEMS()) {
				taskDetailListA.add(item);
			}
			adapterA = new TaskAdapter(context, taskDetailListA);
			gvTaskListA.setAdapter(adapterA);
			gvTaskListA.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (StringUtils.isNotEmpty(taskDetailListA.get(position)
							.getIsCollect())) {
						if (taskDetailListA.get(position).getIsCollect().equals("0")) {// 无二级任务
							if (taskDetailListA.get(position).getWczt().equals("0")) {
								taskDetailListA.get(position).setWczt("8");
							} else if (taskDetailListA.get(position).getWczt()
									.equals("8")) {
								taskDetailListA.get(position).setWczt("0");
							}
							adapterA.notifyDataSetChanged();
						} else {

						}
					}else{
						if (taskDetailListA.get(position).getWczt().equals("0")) {
							taskDetailListA.get(position).setWczt("8");
						} else if (taskDetailListA.get(position).getWczt()
								.equals("8")) {
							taskDetailListA.get(position).setWczt("0");
						}
						adapterA.notifyDataSetChanged();
					}

				}
			});
		}else{
			tvFrequencyA.setText("晨间");
			tvFrequencyB.setText("上午");
			tvFrequencyC.setText("下午");
			rlFrequencyB.setVisibility(View.VISIBLE);
			rlFrequencyC.setVisibility(View.VISIBLE);
			gvTaskListB.setVisibility(View.VISIBLE);
			gvTaskListC.setVisibility(View.VISIBLE);
			for (TaskDetailEntity item : taseList.getITEMS()) {
				if(item.getExecTime().equals("A")){
					taskDetailListA.add(item);
				}else if(item.getExecTime().equals("B")){
					taskDetailListB.add(item);
				}else if(item.getExecTime().equals("C")){
					taskDetailListC.add(item);
				}
			}
			adapterA = new TaskAdapter(context, taskDetailListA);
			gvTaskListA.setAdapter(adapterA);
			gvTaskListA.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (StringUtils.isNotEmpty(taskDetailListA.get(position)
							.getIsCollect())) {
						if (taskDetailListA.get(position).getIsCollect().equals("0")) {// 无二级任务
							if (taskDetailListA.get(position).getWczt().equals("0")) {
								taskDetailListA.get(position).setWczt("8");
							} else if (taskDetailListA.get(position).getWczt()
									.equals("8")) {
								taskDetailListA.get(position).setWczt("0");
							}
							adapterA.notifyDataSetChanged();
						} else {

						}
					}else{
						if (taskDetailListA.get(position).getWczt().equals("0")) {
							taskDetailListA.get(position).setWczt("8");
						} else if (taskDetailListA.get(position).getWczt()
								.equals("8")) {
							taskDetailListA.get(position).setWczt("0");
						}
						adapterA.notifyDataSetChanged();
					}

				}
			});
			adapterB = new TaskAdapter(context, taskDetailListB);
			gvTaskListB.setAdapter(adapterB);
			gvTaskListB.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (StringUtils.isNotEmpty(taskDetailListB.get(position).getIsCollect())) {
						if (taskDetailListB.get(position).getIsCollect().equals("0")) {// 无二级任务
							if (taskDetailListB.get(position).getWczt().equals("0")) {
								taskDetailListB.get(position).setWczt("8");
							} else if (taskDetailListB.get(position).getWczt()
									.equals("8")) {
								taskDetailListB.get(position).setWczt("0");
							}
							adapterB.notifyDataSetChanged();
						} else {

						}
					}else{
						if (taskDetailListB.get(position).getWczt().equals("0")) {
							taskDetailListB.get(position).setWczt("8");
						} else if (taskDetailListB.get(position).getWczt()
								.equals("8")) {
							taskDetailListB.get(position).setWczt("0");
						}
						adapterB.notifyDataSetChanged();
					}
				}
			});
			
			adapterC = new TaskAdapter(context, taskDetailListC);
			gvTaskListC.setAdapter(adapterC);
			gvTaskListC.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (StringUtils.isNotEmpty(taskDetailListC.get(position).getIsCollect())) {
						if (taskDetailListC.get(position).getIsCollect().equals("0")) {// 无二级任务
							if (taskDetailListC.get(position).getWczt().equals("0")) {
								taskDetailListC.get(position).setWczt("8");
							} else if (taskDetailListC.get(position).getWczt()
									.equals("8")) {
								taskDetailListC.get(position).setWczt("0");
							}
							adapterC.notifyDataSetChanged();
						} else {

						}
					}else{
						if (taskDetailListC.get(position).getWczt().equals("0")) {
							taskDetailListC.get(position).setWczt("8");
						} else if (taskDetailListC.get(position).getWczt()
								.equals("8")) {
							taskDetailListC.get(position).setWczt("0");
						}
						adapterC.notifyDataSetChanged();
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
