package com.healthmanage.ylis.activity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.R.id;
import com.healthmanage.ylis.R.layout;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {
	private Activity context;
	
	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;

	@Bind(R.id.iv_aboout_us) ImageView ivAboutUs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		
		context = this;

		initView();
		initData();
	}
	
	private void initView() {
		ButterKnife.bind(context);
		
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				finish();
			}
		});
	}
	
	private void initData() {
		tvTitle.setText("关于我们");
		llBack.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
