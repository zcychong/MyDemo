package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TurnBodyInfoActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;
	
	@Bind(R.id.et_skin_info) EditText etSkinInfo;
	@Bind(R.id.et_deal_info) EditText etDealInfo;
	@Bind(R.id.et_deal_result) EditText etDealResult;
	
	@Bind(R.id.tv_rlderly_name) TextView tvRlderlyName;
	@Bind(R.id.rl_rlderly_name) RelativeLayout rlRlderlyName;
	
	@Bind(R.id.tv_commit) TextView tvCommit;
	private String strSkinInfo, strDealInfo, strDealResult;
	private String 	userId, shiftId, execTime, usrOrg, strTime, strDecubitus, bedNo, ids, names;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn_body_info);
		
		context = this;

		initView();
		initData();
	}
	
	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("翻身异常信息");
		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack)
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
		
		RxView.clicks(rlRlderlyName).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, ChooseElderlyActivity.class);
				intent.putExtra("userId", userId);
				intent.putExtra("type", "1");
				intent.putExtra("bedNo", bedNo);
				intent.putExtra("execTime", "E");
				startActivityForResult(intent, 1);
			}
		});
		
		
	}
	
	private void initData(){
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		execTime = getIntent().getStringExtra("execTime");
		usrOrg = getIntent().getStringExtra("usrOrg");
		strTime = getIntent().getStringExtra("strTime");
		strDecubitus = getIntent().getStringExtra("strDecubitus");
		bedNo = getIntent().getStringExtra("bedNo");
	}
	
	private void commit(){
		strSkinInfo = etSkinInfo.getText().toString();
		strDealInfo = etDealInfo.getText().toString();
		strDealResult = etDealResult.getText().toString();
		
//		Intent intent = new Intent();
//		intent.putExtra("strSkinInfo", strSkinInfo);
//		intent.putExtra("strDealInfo", strDealInfo);
//		intent.putExtra("strDealResult", strDealResult);
//		setResult(1, intent);
//		finish();
		if(StringUtils.isNotEmpty(names)){
			if (Network.checkNet(context)) {
				updateTurnBodyInfo();
			}else{
				Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "请选择老人!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void updateTurnBodyInfo(){
		loading = LoadingDialog.loadingSave(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		map.put("usrOrg", usrOrg);
		map.put("execTime", execTime);
		map.put("elderlyId", ids);
		map.put("wowei", strDecubitus);
		
		map.put("fssj", strTime);
		map.put("pfqk", strSkinInfo);
		map.put("clfs", strDealInfo);
		map.put("lrfy", strDealResult);
		map.put("status", "1");
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
			public void onNext(BaseResponseEntity response) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(response != null){
					if(response.isSuccess()){
						Log.e(TAG, "success");
						Intent intent = new Intent();
						intent.putExtra("strSkinInfo", strSkinInfo);
						intent.putExtra("strDealInfo", strDealInfo);
						intent.putExtra("strDealResult", strDealResult);
						intent.putExtra("names", names);
						setResult(2, intent);
						finish();
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().saveTurnBodyInfo(subscriber, map);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(requestCode == 1){
				if(resultCode == 1){
					names = data.getStringExtra("names");
					ids = data.getStringExtra("ids");
					tvRlderlyName.setText(names);
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
