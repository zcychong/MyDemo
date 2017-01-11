package com.healthmanage.ylis.activity;

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
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.DoseHistoryResponse;
import com.healthmanage.ylis.model.DoseItem;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class ExpandDoseActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back)
	LinearLayout llBack;
	@Bind(R.id.tv_title)
	TextView tvTitle;

	@Bind(R.id.ll_time)
	LinearLayout llTime;
	@Bind(R.id.tv_time)
	TextView tvTime;

	@Bind(R.id.et_medicine_name)
	EditText etMedicineName;
	@Bind(R.id.et_medicine_unit)
	EditText etMedicineUnit;
	@Bind(R.id.et_people_info)
	EditText etPeopleInfo;

	private String strTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expand_dose);
		context = this;

		initView();
		initData();

		getDoseHistroy();
	}

	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("服药");
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});

		RxView.clicks(llTime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showTimeDialog();
			}
		});
	}

	private void initData() {
		tvTime.setText(DateOperate.getCurrentDataWithSpe());
	}

	private void getDoseHistroy() {
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("staTime", DateOperate.getCurrentTime());

		Subscriber<DoseHistoryResponse> subscriber = new Subscriber<DoseHistoryResponse>() {
			@Override
			public void onCompleted() {
				Log.e(TAG, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
			}

			@Override
			public void onNext(DoseHistoryResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, "onNext");
				if (taseList != null) {
					if (taseList.isSuccess()) {

					} else {
						Log.e(TAG, "fail " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}

			}
		};
		HttpMethodImp.getInstance().getDoseHistroy(subscriber, map);
	}

	private void showTimeDialog() {
		TimePickerDialog dialog = new TimePickerDialog(context,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						StringBuffer sbTime = new StringBuffer();
						sbTime.append(" ");
						if (hourOfDay > 9) {
							sbTime.append(hourOfDay);
						} else {
							sbTime.append("0");
							sbTime.append(hourOfDay);
						}
						sbTime.append(":");
						if (minute > 9) {
							sbTime.append(minute);
						} else {
							sbTime.append("0");
							sbTime.append(minute);
						}
						tvTime.setText(DateOperate.getCurrentDataWithSpe()
								+ sbTime.toString());
					}
				}, 0, 0, true);
		dialog.setTitle(DateOperate.getCurrentDataWithSpe());
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

	class DoseAdapter extends BaseAdapter {
		private List<DoseItem> doses;

		@Override
		public int getCount() {
			return doses.size();
		}

		@Override
		public Object getItem(int position) {
			return doses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
