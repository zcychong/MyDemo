package com.healthmanage.ylis.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.GetRoomListModelTwoResponse;
import com.healthmanage.ylis.model.PeopleItemModelTwo;
import com.healthmanage.ylis.model.RoomItemModelTwo;
import com.healthmanage.ylis.view.UserRoomItemModelTwoView;
import com.jakewharton.rxbinding.view.RxView;

public class TaskDetailModelTwoActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.tv_back_text) TextView tvBackTitle;

	@Bind(R.id.ll_room_list) LinearLayout llRoomList;
	@Bind(R.id.tv_commit) TextView tvCommit;

	@Bind(R.id.ll_option) LinearLayout llOption;
	@Bind(R.id.tv_option) TextView tvOption;

	private String bedNo, userId, usrOrg;
	private String inputUser;
	private String shiftId;
	private String itemName;
	private String execTime;
	private String isCollect, questNoes, type;
	private boolean isChooseAll = false;
	private GetRoomListModelTwoResponse response;
	private StringBuffer detiIds = new StringBuffer();
	private List<UserRoomItemModelTwoView> items = new ArrayList<UserRoomItemModelTwoView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail_model_two);
		context = this;

		initView();
		initData();

		if (Network.checkNet(context)) {
			checkInfo();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}

		// getFalseList();
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

		RxView.clicks(llOption).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				choosedAll();
			}
		});

	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		bedNo = getIntent().getStringExtra("bedNo");
		inputUser = getIntent().getStringExtra("inputUser");
		shiftId = getIntent().getStringExtra("shiftId");
		itemName = getIntent().getStringExtra("itemName");
		execTime = getIntent().getStringExtra("execTime");
		usrOrg = getIntent().getStringExtra("usrOrg");

		isCollect = getIntent().getStringExtra("isCollect");
		questNoes = getIntent().getStringExtra("questNoes");

		type = getIntent().getStringExtra("type");

		if (itemName != null) {
			tvBackTitle.setText(itemName);
		}
		tvOption.setText("全选");

	}

	private void checkInfo(){
		Log.e(TAG, "checkInfo");
		String questNoes1 = getResources().getString(R.string.questNoes_1);
		if(StringUtils.isNotEmpty(type)){
			if(StringUtils.isNotEmpty(isCollect)){
				if(type.equals("1")){
					getMaybeList();
				}else{
					if(isCollect.equals("1")){
						if(StringUtils.isNotEmpty(questNoes)){
							if(questNoes.equals(questNoes1)){
								get48HoursInfo();
							}
						}
					}else{
						getTastList();
					}
				}
			}else{
				getTastList();
			}
		}else{
			getTastList();
		}

	}

	private void get48HoursInfo(){
		Log.e(TAG, "get48HoursInfo");
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);

		Subscriber<GetRoomListModelTwoResponse> subscriber = new Subscriber<GetRoomListModelTwoResponse>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(GetRoomListModelTwoResponse taseList) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						response = taseList;
						dealData(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}

				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().get48HourselderlyList(subscriber, map);
	}

	private void choosedAll() {
		isChooseAll = !isChooseAll;
		if (isChooseAll) {
			tvOption.setText("取消全选");
		} else {
			tvOption.setText("全选");
		}
		for (int i = 0; i < response.getITEMS().size(); i++) {
			for (int j = 0; j < response.getITEMS().get(i).getBeds().size(); j++) {
				PeopleItemModelTwo temp = response.getITEMS().get(i).getBeds().get(j);
				if (temp.getWczt() != null) {
					if (!isChooseAll) {
						if (temp.getWczt().equals("8")) {
							temp.setWczt("0");
						}
					} else {
//						if (temp.getWczt().equals("0")) {
							temp.setWczt("8");
//						}
					}
				}
			}
			items.get(i).notifyDataSetChanged();
		}
	}

	private void getTastList() {
		Log.e(TAG, "getTastList");
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("bedNo", bedNo);
		map.put("inputUser", inputUser);
		map.put("shiftId", shiftId);
		map.put("execTime", execTime);

		Subscriber<GetRoomListModelTwoResponse> subscriber = new Subscriber<GetRoomListModelTwoResponse>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(GetRoomListModelTwoResponse taseList) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						response = taseList;
						dealData(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}

				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().getRoomListModelTwo(subscriber, map);
	}

	private void getMaybeList(){
		Log.e(TAG, "getMaybeList");
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("bedNo", bedNo);
		map.put("inputUser", inputUser);

		Log.e("goodId", bedNo);
		Log.e("inputUser", inputUser);

		Subscriber<GetRoomListModelTwoResponse> subscriber = new Subscriber<GetRoomListModelTwoResponse>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(GetRoomListModelTwoResponse taseList) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						response = taseList;
						dealData(taseList);
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}

				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().getMaybeTaskElderlyList(subscriber, map);
	}

	private void dealData(GetRoomListModelTwoResponse taseList) {
		if (taseList.isSuccess()) {
			if (llRoomList.getChildCount() > 0) {
				llRoomList.removeAllViews();
			}
			for (final RoomItemModelTwo item : taseList.getITEMS()) {
				final UserRoomItemModelTwoView roomItem = new UserRoomItemModelTwoView(
						context, item);
				roomItem.getGvRoomList().setOnItemClickListener(
						new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								PeopleItemModelTwo tempItem = item.getBeds().get(position);
								if(StringUtils.isNotEmpty(tempItem.getElderlySta())){
									if(!tempItem.getElderlySta().equals("3")){
										if(StringUtils.isNotEmpty(isCollect)){
											if(isCollect.equals("1")){
												if(StringUtils.isNotEmpty(questNoes)){
													if(questNoes.equals(getResources().getString(R.string.questNoes_1))){
														Intent intent = new Intent(context, HoursRecordActivity.class);
														intent.putExtra("elderlyId", tempItem.getElderlyId());
														intent.putExtra("userId", userId);
														startActivity(intent);
													}else{
														setItemCheckd(tempItem, roomItem);
													}
												}else{
													setItemCheckd(tempItem, roomItem);
												}
											}else{
												setItemCheckd(tempItem, roomItem);
											}
										}else{
											setItemCheckd(tempItem, roomItem);
										}
										roomItem.notifyDataSetChanged();
									}
								}else{
									setItemCheckd(tempItem, roomItem);
								}
							}
						});

				llRoomList.addView(roomItem);
				items.add(roomItem);
			}
		} else {
			Log.e(TAG, "fail -" + taseList.getMessage());
		}
	}

	private void setItemCheckd(PeopleItemModelTwo tempItem, UserRoomItemModelTwoView roomItem){
		if (StringUtils.isNotEmpty(tempItem.getWczt())) {
			if (tempItem.getWczt().equals("0")) {
				tempItem.setWczt("8");
			} else if (tempItem.getWczt().equals("8")) {
				tempItem.setWczt("0");
			}
		}else{
			tempItem.setWczt("8");
		}
		roomItem.notifyDataSetChanged();
	}

	private void commit() {
		Map<String, String> map = new HashMap<String, String>();
		if (detiIds.length() > 0) {
			detiIds.delete(0, detiIds.length());
		}
		for (int i = 0; i < response.getITEMS().size(); i++) {
			for (int j = 0; j < response.getITEMS().get(i).getBeds().size(); j++) {
				String detiId = response.getITEMS().get(i).getBeds().get(j)
						.getDetiId();
				String wczt = response.getITEMS().get(i).getBeds().get(j).getWczt();
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
		}

		if(StringUtils.isNotEmpty(type)){
			if(type.equals("1")){
				updateMaybeTask();
				return;
			}
		}

		if (detiIds.length() == 0) {
			Toast.makeText(context, "请选择将要完成的任务所属的老人!", Toast.LENGTH_SHORT).show();
			return;
		}else{
			loading = LoadingDialog.loadingSave(context);
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

	}

	private void updateMaybeTask(){
		Log.e(TAG, "updateMaybeTask");
		Map<String, String> map = new HashMap<String, String>();
		if (detiIds.length() == 0) {
			Toast.makeText(context, "请选择将要完成的任务所属的老人!", Toast.LENGTH_SHORT).show();
			return;
		}else{
			loading = LoadingDialog.loadingSave(context);
			loading.show();
			//项目编号#@项目名称#@完成状态#@护工Id#@所属机构#@班次#@交接表编号
			map.put("planList", bedNo + "#@" + itemName + "#@" + "1" + "#@" + userId + "#@" + usrOrg + "#@" + execTime +  "#@" + shiftId );
			StringBuffer sbElderlyList = new StringBuffer();

			for(int i=0; i<response.getITEMS().size(); i++){
				for(int j=0; j<response.getITEMS().get(i).getBeds().size(); j++){
					if(StringUtils.isNotEmpty(response.getITEMS().get(i).getBeds().get(j).getWczt())){
						if(response.getITEMS().get(i).getBeds().get(j).getWczt().equals("8")){
							if(sbElderlyList.length() != 0){
								sbElderlyList.append(",");
							}
							sbElderlyList.append(response.getITEMS().get(i).getBeds().get(j).getElderlyId());
						}
					}
				}
			}
			map.put("elderlyIds", sbElderlyList.toString());
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
			HttpMethodImp.getInstance().updateMaybeTask(subscriber, map);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
