package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.functions.Action1;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.GetLaundryListResponse;
import com.healthmanage.ylis.model.LaundryItem;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LaundryHistroyActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.lv_items) ListView lvItems;
	@Bind(R.id.ll_no_value) LinearLayout llNoValue;
	
	private String userId, shiftId;
	private LaundrylHistroyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laundry_histroy);
		
		context = this;

		initView();
		initData();
		
		if (Network.checkNet(context)) {
			getPatroalHistroy();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initView() {
		ButterKnife.bind(context);

		llBack.setVisibility(View.VISIBLE);
		RxView.clicks(llBack).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				finish();
			}
		});
		
	}
	
	private void initData() {
		tvTitle.setText("洗衣记录信息");
		
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
	}
	
	private void getPatroalHistroy(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		
		Subscriber<GetLaundryListResponse> subscriber = new Subscriber<GetLaundryListResponse>() {
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
			public void onNext(GetLaundryListResponse response) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(response != null){
					if(response.isSuccess()){
						Log.e(TAG, "success");
						dealData(response.getITEMS());
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
						if(response.getMessage().equals("NOVALUE")){
							llNoValue.setVisibility(View.VISIBLE);
						}
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getLaundryListInfo(subscriber, map);
	}
	
	private void dealData(List<LaundryItem> items){
		adapter = new LaundrylHistroyAdapter(context, items);
		lvItems.setAdapter(adapter);
	}
	
	class LaundrylHistroyAdapter extends BaseAdapter {
		private List<LaundryItem> histroyItems;

		public LaundrylHistroyAdapter(Context context, List<LaundryItem> list) {
			histroyItems = list;
		}

		@Override
		public int getCount() {
			return histroyItems.size();
		}

		@Override
		public Object getItem(int position) {
			return histroyItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.laundry_histroy_item, null);
				viewHolder = new ViewHolder();
	
				viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.elderlyName = (TextView) convertView.findViewById(R.id.tv_elderly_name);
				viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			LaundryItem item = histroyItems.get(position);
			
			if(StringUtils.isNotEmpty(item.getSxsj())){
				viewHolder.time.setText(item.getSxsj());
			}else{
				viewHolder.time.setText("");
			}
			
			if(StringUtils.isNotEmpty(item.getName())){
				viewHolder.elderlyName.setText(item.getName());
			}else{
				viewHolder.elderlyName.setText("");
			}
			
			if(StringUtils.isNotEmpty(item.getSxlx())){
				String[] items = getResources().getStringArray(R.array.laundry_items);
				viewHolder.type.setText(items[Integer.valueOf(item.getSxlx())]);
			}else{
				viewHolder.type.setText("");
			}
			
			return convertView;
		}

		class ViewHolder {
			private TextView time, elderlyName, type;
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
