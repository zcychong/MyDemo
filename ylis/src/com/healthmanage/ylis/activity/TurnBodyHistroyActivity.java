package com.healthmanage.ylis.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.GetTurnBodyResponse;
import com.healthmanage.ylis.model.TurnBodyItem;
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

public class TurnBodyHistroyActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;
	
	@Bind(R.id.lv_list) ListView lvList;
	@Bind(R.id.ll_no_value) LinearLayout llNoValue;
	
	private String userId, shiftId;
	private TurnBodyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn_body_histroy);
		
		context = this;

		initView();
		initData();
		
		getData();
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
	}
	
	private void initData(){
		tvTitle.setText("翻身记录信息");
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
	}
	
	private void getData(){
		if (Network.checkNet(context)) {
			getTurnBodyHistroy();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void getTurnBodyHistroy(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		
		Subscriber<GetTurnBodyResponse> subscriber = new Subscriber<GetTurnBodyResponse>() {
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
			public void onNext(GetTurnBodyResponse response) {
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
		HttpMethodImp.getInstance().getTurnBodyList(subscriber, map);
	}
	
	private void dealData(List<TurnBodyItem> items){
		adapter = new TurnBodyAdapter(context, items);
		lvList.setAdapter(adapter);
		
	}
	
	class TurnBodyAdapter extends BaseAdapter {
		private List<TurnBodyItem> taskList;

		public TurnBodyAdapter(Context context, List<TurnBodyItem> list) {
			taskList = list;
		}

		@Override
		public int getCount() {
			return taskList.size();
		}

		@Override
		public Object getItem(int position) {
			return taskList.get(position);
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
						R.layout.turn_body_histroy_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
				viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.peopleName = (TextView) convertView.findViewById(R.id.tv_elderly_name);
				viewHolder.patroalInfo = (TextView) convertView.findViewById(R.id.tv_patroal_info);
				viewHolder.patroalDeal = (TextView) convertView.findViewById(R.id.tv_patroal_deal);
				viewHolder.elderlyInfo = (TextView) convertView.findViewById(R.id.tv_elderly_info);
				
				viewHolder.leaderName = (TextView) convertView.findViewById(R.id.tv_leader_name);
				viewHolder.hoomName = (TextView) convertView.findViewById(R.id.tv_room_name);
				viewHolder.hoomPhone = (TextView) convertView.findViewById(R.id.tv_room_phone);
				viewHolder.hoomBack = (TextView) convertView.findViewById(R.id.tv_room_back);
				viewHolder.another = (TextView) convertView.findViewById(R.id.tv_another);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			TurnBodyItem item = taskList.get(position);
			
			if(StringUtils.isNotEmpty(item.getStatus())){
				if(item.getStatus().equals("0")){ //正常
					viewHolder.tvState.setText("正常");
					viewHolder.tvState.setBackgroundResource(R.drawable.border_bg_green);
				}else{
					viewHolder.tvState.setText("异常");
					viewHolder.tvState.setBackgroundResource(R.drawable.border_bg_main_color);
				}
			}
			
			if(StringUtils.isNotEmpty(item.getFssj())){
				viewHolder.time.setVisibility(View.VISIBLE);
				viewHolder.time.setText("翻身时间: " + item.getFssj());
			}else{
				viewHolder.time.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getName())){
				viewHolder.peopleName.setVisibility(View.VISIBLE);
				viewHolder.peopleName.setText("老人姓名: " + item.getName());
			}else{
				viewHolder.peopleName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getWowei())){
				viewHolder.patroalInfo.setVisibility(View.VISIBLE);
				String[] items = getResources().getStringArray(R.array.turn_body_decubitus);
				viewHolder.patroalInfo.setText("卧位: " + items[Integer.valueOf(item.getWowei())]);
			}else{
				viewHolder.patroalInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getPfqk())){
				viewHolder.patroalDeal.setVisibility(View.VISIBLE);
				viewHolder.patroalDeal.setText("皮肤情况: " + item.getPfqk());
			}else{
				viewHolder.patroalDeal.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getClfs())){
				viewHolder.elderlyInfo.setVisibility(View.VISIBLE);
				viewHolder.elderlyInfo.setText("处理方式: " + item.getClfs());
			}else{
				viewHolder.elderlyInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getLrfy())){
				viewHolder.leaderName.setVisibility(View.VISIBLE);
				viewHolder.leaderName.setText("处理结果: " + item.getLrfy());
			}else{
				viewHolder.leaderName.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		class ViewHolder {
			private TextView tvState;
			private TextView another, elderlyInfo, hoomBack, hoomName, hoomPhone;
			private TextView leaderName, patroalDeal, patroalInfo, peopleName, time;
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
