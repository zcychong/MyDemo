package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.TastListResponse;
import com.healthmanage.ylis.view.UserRoomItemView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistroyElderlyListActivity extends BaseActivity{
	private Activity context;
	private Dialog loading;
	
	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_room_list) LinearLayout llRoomList;

	
	private SharedPreferences userInfo;
	private String userId, shiftId, userNo, type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_histroy_task);
		context = this;

		initView();
		initData();

		if (Network.checkNet(context)) {
			getPeopleListModel1();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initData() {
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		type = getIntent().getStringExtra("type");
		
		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);
		
		userNo = userInfo.getString("userNo", "");

	}
	
	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("交接人未完成任务");
		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});
	}
	
	private void getPeopleListModel1() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		
		if(type.equals("0")){
			map.put("wczt", "2");
			map.put("userId", userNo);
		}else{
			map.put("wczt", "0");
			map.put("userId", userId);
		}
		
		Subscriber<TastListResponse> subscriber = new Subscriber<TastListResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
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
		HttpMethodImp.getInstance().getLeavlTaskList(subscriber, map);
	}

	private void dealResponseData(final TastListResponse taseList) {
		if (llRoomList.getChildCount() > 0) {
			llRoomList.removeAllViews();
		}
		if (taseList != null) {
			if (taseList.isSuccess()) {
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
									Intent intent = new Intent(context,
											HistroyElderlyTaskActivity.class);
									intent.putExtra("type", type);//0-可以完成 1-不能完成
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
							});
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
