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
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.TaskConnectNewResponse;
import com.healthmanage.ylis.model.TaskConnectResponse;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TastConnectFirstActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.et_user_name)
	EditText etUserId;

	@Bind(R.id.ll_a_time)
	LinearLayout llATime;
	@Bind(R.id.ll_b_time)
	LinearLayout llBTime;

	@Bind(R.id.iv_a_time)
	ImageView ivAImage;
	@Bind(R.id.iv_b_time)
	ImageView ivBImage;

	@Bind(R.id.btn_commit)
	Button btnCommit;

	@Bind(R.id.tv_title)
	TextView tvTitle;

	@Bind(R.id.ll_input)
	LinearLayout llInput;

	private String userId, usrOrg;
	private String[] timeIds, timeNames;
	private String time, anotherId;
	private boolean isCheckedA = false, isCheckedB = false;
	private SharedPreferences userInfo;
	private String[] times;
	private boolean close = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tast_connect);
		context = this;

		initView();
		initData();

		// 获取班次信息
		// getFrequencyInfo();
	}

	private void initView() {
		ButterKnife.bind(context);
		tvTitle.setText("任务交接-查找");

		RxView.clicks(llATime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void arg0) {
				clearCheckedState();
				time = times[0];
				isCheckedA = true;
				ivAImage.setImageResource(R.drawable.icon_radio_choosed);
			}
		});
		RxView.clicks(llBTime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void arg0) {
				clearCheckedState();
				time = times[1];
				isCheckedB = true;
				ivBImage.setImageResource(R.drawable.icon_radio_choosed);
			}
		});

		RxView.clicks(btnCommit).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						anotherId = etUserId.getText().toString();
						if (StringUtils.isNotEmpty(anotherId)) {
							if (isCheckedA || isCheckedB) {
								saveFrequency(time);
								if (anotherId.equals("000000")) {// 6个0 默认第一次使用
																	// 不交接
									getTaskConnectInfo(anotherId);
								} else {
									getTaskConnectInfo();
								}
							} else {
								Toast.makeText(context, "请选择班次!",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "请输入交接人员工号!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initData() {
		userId = getIntent().getStringExtra("userId");
		times = getResources().getStringArray(R.array.time_value_list);

		userInfo = (SharedPreferences) getSharedPreferences(context
				.getResources().getString(R.string.apk_name),
				android.content.Context.MODE_PRIVATE);

		usrOrg = userInfo.getString("usrOrg", "");
	}

	private void clearCheckedState() {
		ivAImage.setImageResource(R.drawable.icon_radio_unchoosed);
		ivBImage.setImageResource(R.drawable.icon_radio_unchoosed);

		isCheckedA = false;
		isCheckedB = false;
	}

	private void saveFrequency(String frequency) {
		Editor editor = userInfo.edit();
		editor.putString("execTime", frequency);// 班次
		editor.commit();
	}

	/**
	 * 不交接
	 */
	private void getTaskConnectInfo(final String str) {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("inputId", userId);
		map.put("userOrg", usrOrg);
		map.put("execTimes", time);

		Log.e("inputId", userId);
		Log.e("userOrg", usrOrg);
		Log.e("execTimes", time);

		Subscriber<TaskConnectNewResponse> subscriber = new Subscriber<TaskConnectNewResponse>() {
			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(TaskConnectNewResponse connectResponse) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (connectResponse.isSuccess()) {
					Intent intent = new Intent(context, MainActivity.class);
					intent.putExtra("shiftId", connectResponse.getShiftId());
					intent.putExtra("userId", userId);
					intent.putExtra("execTime", time);
					
					startActivity(intent);
				} else {
					Toast.makeText(context, "查询护理任务失败!", Toast.LENGTH_SHORT)
							.show();
					Log.e(TAG, "fail - " + connectResponse.getMessage());
				}
			}
		};

		HttpMethodImp.getInstance().getTaskConnectInfoNew(subscriber, map);
	}

	/**
	 * 获取交接单数据
	 */
	private void getTaskConnectInfo() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("inputId", anotherId);

		Subscriber<TaskConnectResponse> subscriber = new Subscriber<TaskConnectResponse>() {
			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(TaskConnectResponse connectResponse) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (connectResponse != null) {
					if (connectResponse.isSuccess()) {
						Intent intent = new Intent(context,
								TastConnectSecondActivity.class);
						intent.putExtra("userId", userId);
						intent.putExtra("anotherId", anotherId);
						intent.putExtra("frequency", time);
						startActivity(intent);

						finish();
					} else {
						if (connectResponse.getMessage().equals("NOVALUE")) {
							Toast.makeText(context, "该员工没有可交接任务单!",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		};

		HttpMethodImp.getInstance().getTaskConnectInfo(subscriber, map);
	}

	// 监听返回键
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (close) {
				finish();
				System.exit(0);
			} else if (!close) {
				close = !close;
				Toast.makeText(context, getString(R.string.hint_logout),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

}
