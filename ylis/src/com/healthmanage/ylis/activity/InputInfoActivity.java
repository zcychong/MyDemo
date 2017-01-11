package com.healthmanage.ylis.activity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.R.layout;
import com.healthmanage.ylis.common.StringUtils;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputInfoActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.et_info) EditText etInputInfo;
	@Bind(R.id.tv_commit) TextView tvCommit;
	
	private String strTitle, strInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_info);
		
		context = this;
		
		initView();
		initData();
	}
	
	private void initView() {
		ButterKnife.bind(context);
		
		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					finish();
				}
		});
		
		RxView.clicks(tvCommit).
				throttleFirst(2, TimeUnit.SECONDS)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					checkInfo();
				}
			});
	}
	
	private void checkInfo(){
		strInput = etInputInfo.getText().toString();
		if(StringUtils.isNotEmpty(strInput)){
			Intent intent = new Intent();
			intent.putExtra("input", strInput);
			setResult(1,intent);
			finish();
		}else{
			Toast.makeText(context, "内容不能为空!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initData(){
		strTitle = getIntent().getStringExtra("title");
		
		tvTitle.setText(strTitle);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
	
}
