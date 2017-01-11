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
import com.healthmanage.ylis.model.PatroalExceptionItem;
import com.healthmanage.ylis.model.PatroalExceptionListResponse;
import com.jakewharton.rxbinding.view.RxView;

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

public class PatroalExceptionListActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.lv_list) ListView lvList;
	@Bind(R.id.ll_no_value) LinearLayout llNoValue;
	
	private String xsjlId;
	private PatraoalExceptionItemAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patroal_exception_list);
		
		context = this;

		initView();
		initData();
		
		getData();
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
		tvTitle.setText("巡视异常记录信息");
		xsjlId = getIntent().getStringExtra("xsjlId");
	}
	
	private void getData(){
		if (Network.checkNet(context)) {
			getPatroalExecptionList();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void getPatroalExecptionList(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("xsjlId", xsjlId);
		
		Subscriber<PatroalExceptionListResponse> subscriber = new Subscriber<PatroalExceptionListResponse>() {
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
			public void onNext(PatroalExceptionListResponse response) {
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
		HttpMethodImp.getInstance().getPatroalExceptionList(subscriber, map);
	}
	
	private void dealData(List<PatroalExceptionItem> items){
		adapter = new PatraoalExceptionItemAdapter(context, items);
		
		lvList.setAdapter(adapter);
	}
	
	class PatraoalExceptionItemAdapter extends BaseAdapter {
		private List<PatroalExceptionItem> taskList;

		public PatraoalExceptionItemAdapter(Context context, List<PatroalExceptionItem> list) {
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
						R.layout.patroal_execption_item, null);
				viewHolder = new ViewHolder();
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
			
			PatroalExceptionItem item = taskList.get(position);
			if(StringUtils.isNotEmpty(item.getFssj())){
				viewHolder.time.setVisibility(View.VISIBLE);
				viewHolder.time.setText("发生时间: " + item.getFssj());
			}else{
				viewHolder.time.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getElderlyName())){
				viewHolder.peopleName.setVisibility(View.VISIBLE);
				viewHolder.peopleName.setText("老人姓名: " + item.getElderlyName());
			}else{
				viewHolder.peopleName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getTsqk())){
				viewHolder.patroalInfo.setVisibility(View.VISIBLE);
				viewHolder.patroalInfo.setText("情况简述: " + item.getTsqk());
			}else{
				viewHolder.patroalInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getClcs())){
				viewHolder.patroalDeal.setVisibility(View.VISIBLE);
				viewHolder.patroalDeal.setText("处理措施: " + item.getClcs());
			}else{
				viewHolder.patroalDeal.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getLrzk())){
				viewHolder.elderlyInfo.setVisibility(View.VISIBLE);
				viewHolder.elderlyInfo.setText("老人情况: " + item.getLrzk());
			}else{
				viewHolder.elderlyInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getLdxm())){
				viewHolder.leaderName.setVisibility(View.VISIBLE);
				viewHolder.leaderName.setText("上报领导姓名: " + item.getLdxm());
			}else{
				viewHolder.leaderName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getLxjsxm())){
				viewHolder.hoomName.setVisibility(View.VISIBLE);
				viewHolder.hoomName.setText("联系家属姓名: " + item.getLxjsxm());
			}else{
				viewHolder.hoomName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getJsdh())){
				viewHolder.hoomPhone.setVisibility(View.VISIBLE);
				viewHolder.hoomPhone.setText("家属电话: " + item.getJsdh());
			}else{
				viewHolder.hoomPhone.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getJshf())){
				viewHolder.hoomBack.setVisibility(View.VISIBLE);
				viewHolder.hoomBack.setText("家属回复: " + item.getJshf());
			}else{
				viewHolder.hoomBack.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getRemark())){
				viewHolder.another.setVisibility(View.VISIBLE);
				viewHolder.another.setText("其他: " + item.getRemark());
			}else{
				viewHolder.another.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
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
