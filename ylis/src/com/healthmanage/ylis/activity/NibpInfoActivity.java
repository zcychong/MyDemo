package com.healthmanage.ylis.activity;

import com.healthmanage.ylis.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class NibpInfoActivity extends Activity {
	private final String TAG = getClass().getSimpleName();
	private Activity context;
	private Button btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_nibp_info);
		context = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		LinearLayout llBack = (LinearLayout)findViewById(R.id.ll_back);
		llBack.setVisibility(View.VISIBLE);
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		initView();
	}
	
	private void initView(){
		btnBack = (Button)context.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
