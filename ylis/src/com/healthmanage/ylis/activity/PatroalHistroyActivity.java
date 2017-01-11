package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.functions.Action1;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.PatroalHistroyItem;
import com.healthmanage.ylis.model.PatroalHistroyResponse;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PatroalHistroyActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.lv_items) ListView lvItems;
	@Bind(R.id.ll_no_value) LinearLayout llNoValue;
			
	private String userId, shiftId;
	private PatroalHistroyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patroal_histroy);
		
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
		tvTitle.setText("巡视记录信息");
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
	}
	
	private void getPatroalHistroy(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		
		Subscriber<PatroalHistroyResponse> subscriber = new Subscriber<PatroalHistroyResponse>() {
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
			public void onNext(PatroalHistroyResponse response) {
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
		HttpMethodImp.getInstance().getPatroalHistroyList(subscriber, map);
	}
	
	private void dealData(final List<PatroalHistroyItem> items){
		adapter = new PatroalHistroyAdapter(context, items);
		
		lvItems.setAdapter(adapter);
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					if(items.get(position).getZbxcqk().equals("1")){
						Intent intent = new Intent(context, PatroalExceptionListActivity.class);
						intent.putExtra("xsjlId", items.get(position).getXsjlId());
						startActivity(intent);
					}
			}
		});
	}
	
	class PatroalHistroyAdapter extends BaseAdapter {
		private List<PatroalHistroyItem> histroyItems;

		public PatroalHistroyAdapter(Context context, List<PatroalHistroyItem> list) {
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
						R.layout.patroal_histroy_item, null);
				viewHolder = new ViewHolder();
	
				viewHolder.patroalState = (TextView) convertView.findViewById(R.id.tv_state);
				viewHolder.patroalTime = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.patroalName = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.patroalDate = (TextView)convertView.findViewById(R.id.tv_date);
				viewHolder.ivMore = (ImageView)convertView.findViewById(R.id.iv_more);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			PatroalHistroyItem item = histroyItems.get(position);
			if(StringUtils.isNotEmpty(item.getZbxcqk())){
				if(item.getZbxcqk().equals("0")){
					viewHolder.patroalState.setText("正常");
					viewHolder.patroalState.setBackgroundResource(R.drawable.border_bg_green);
					viewHolder.ivMore.setVisibility(View.GONE);
					
				}else{
					viewHolder.patroalState.setText("异常");
					viewHolder.patroalState.setBackgroundResource(R.drawable.border_bg_main_color);
					viewHolder.ivMore.setVisibility(View.VISIBLE);
				}
			}
			
			if(StringUtils.isNotEmpty(item.getXssj())){
				viewHolder.patroalTime.setText(item.getXssj());
			}else{
				viewHolder.patroalTime.setText("");
			}
			
			if(StringUtils.isNotEmpty(item.getXsrmc())){
				viewHolder.patroalName.setText(item.getXsrmc());
			}else{
				viewHolder.patroalName.setText("");
			}
			
			if(StringUtils.isNotEmpty(item.getXsrq())){
				viewHolder.patroalDate.setText(item.getXsrq());
			}else{
				viewHolder.patroalDate.setText("");
			}
			
			return convertView;
		}

		class ViewHolder {
			private ImageView ivMore;
			private TextView patroalState, patroalName, patroalTime, patroalDate;
		}

	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
