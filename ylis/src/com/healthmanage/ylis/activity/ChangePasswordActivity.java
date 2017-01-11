package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.MD5Code;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_commit) TextView tvCommit;
	
	@Bind(R.id.et_old_pwd) EditText etOldPwd;
	@Bind(R.id.et_new_pwd) EditText etNewPwd;
	@Bind(R.id.et_commit_pwd) EditText etCommitPwd;
	
	private SharedPreferences userInfo;
	private String userId;
	private String oldPwd, newPwd, commitPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		context = this;
		
		initView();
		initData();
	}
	
	private void initView() {
		ButterKnife.bind(context);
		tvTitle.setText(getResources().getString(R.string.change_password));

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
				checkPwdStyle();
			}
		});
		
	}
	
	private void initData() {
//		userInfo = getSharedPreferences(getString(R.string.apk_name), MODE_PRIVATE);
//		pwd = userInfo.getString("login_password", "");
		userId = getIntent().getStringExtra("userId");
		
	}
	
	private void checkPwdStyle(){
		oldPwd = etOldPwd.getText().toString();
		newPwd = etNewPwd.getText().toString();
		commitPwd = etCommitPwd.getText().toString();
		if(StringUtils.isEmpty(oldPwd)){
			Toast.makeText(context, "旧密码不能为空!", Toast.LENGTH_SHORT).show();
			return ;
		}
		if(StringUtils.isEmpty(newPwd)){
			Toast.makeText(context, "新密码不能为空!", Toast.LENGTH_SHORT).show();
			return ;
		}
		if(StringUtils.isEmpty(commitPwd)){
			Toast.makeText(context, "确认密码不能为空!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(newPwd.length() < 4){
			Toast.makeText(context, "新密码长度最少是4位!", Toast.LENGTH_SHORT).show();
		}
		if(commitPwd.length() < 4){
			Toast.makeText(context, "确认密码长度最少是4位!", Toast.LENGTH_SHORT).show();
		}
		
		if(oldPwd.equals(newPwd)){
			Toast.makeText(context, "新密码不能和旧密码相同!", Toast.LENGTH_SHORT).show();
		}
		
		if(!newPwd.equals(commitPwd)){
			Toast.makeText(context, "新密码和确认密码不一致!", Toast.LENGTH_SHORT).show();
		}
		
		if (Network.checkNet(context)) {
			changePwd();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void changePwd(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("oldPwd", MD5Code.MD5(oldPwd));
		map.put("pwd", MD5Code.MD5(newPwd));
		
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
						Toast.makeText(context, "修改成功!", Toast.LENGTH_SHORT)
								.show();
					} else {
						Log.e(TAG, "fail - " + checkVersion.getMessage());
						Toast.makeText(context, "修改失败!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().changePwd(subscriber, map);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
