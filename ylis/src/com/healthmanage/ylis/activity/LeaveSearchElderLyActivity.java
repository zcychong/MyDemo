package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.adapter.LeaveElderlyApdater;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.LeaveSrarchElderlyResponse;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeaveSearchElderLyActivity extends BaseActivity{
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.tv_commit) TextView tvCommit;
	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.iv_clean) ImageView ivClean;
	@Bind(R.id.et_people_name) EditText etPeopleName;
	@Bind(R.id.lv_people_list) ListView lvPeopleList;
	@Bind(R.id.tv_elderly_count) TextView tvElderlyCount;
	@Bind(R.id.ll_no_value) LinearLayout llNoValue;
			
	private LeaveElderlyApdater adapter;
	private String strPeopleName;
	private String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave_search);
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
		
		RxView.clicks(tvCommit).throttleFirst(2, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				strPeopleName = etPeopleName.getText().toString();
				if (Network.checkNet(context)) {
					getElderlyInfo(strPeopleName);
				}else{
					Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		RxView.clicks(ivClean).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				etPeopleName.setText("");
			}
		});
		
		etPeopleName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0){
					ivClean.setVisibility(View.VISIBLE);
				}else{
					ivClean.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		} );
		
	}
	
	private void initData() {
		tvTitle.setText("查找老人");
		userId = getIntent().getStringExtra("userId");
	}
	
	private void getElderlyInfo(String strPeopleName){
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", strPeopleName);
		map.put("userId", userId);
		
		Log.e(TAG, "name= " + strPeopleName);
		
		Subscriber<LeaveSrarchElderlyResponse> subscriber = new Subscriber<LeaveSrarchElderlyResponse>() {

			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(LeaveSrarchElderlyResponse info) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (info != null) {
					if (info.isSuccess()) {
						
						dealElderlyInfo(info);
						
					} else {
						tvElderlyCount.setText("0");
						lvPeopleList.setVisibility(View.GONE);
						llNoValue.setVisibility(View.VISIBLE);
						Log.e(TAG, "fail - " + info.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().getLeaveElerlyInfo(subscriber, map);;
	}
	
	private void dealElderlyInfo(final LeaveSrarchElderlyResponse res){
		tvElderlyCount.setText(String.valueOf(res.getITEMS().size()));
		lvPeopleList.setVisibility(View.VISIBLE);
		llNoValue.setVisibility(View.GONE);
		adapter = new LeaveElderlyApdater(context, res.getITEMS(),strPeopleName);
		lvPeopleList.setAdapter(adapter);
		
		lvPeopleList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("name", res.getITEMS().get(position).getName());
				intent.putExtra("id", res.getITEMS().get(position).getElderlyId());
				intent.putExtra("room", res.getITEMS().get(position).getRoomNo());
				intent.putExtra("bed", res.getITEMS().get(position).getBedNo());
				setResult(1, intent);
				
				finish();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

}
