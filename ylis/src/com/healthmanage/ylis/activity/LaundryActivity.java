package com.healthmanage.ylis.activity;

import java.util.ArrayList;
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
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.GetLaundryListResponse;
import com.healthmanage.ylis.model.LaundryItem;
import com.healthmanage.ylis.model.LeaveElderlyEntity;
import com.healthmanage.ylis.model.LeaveSrarchElderlyResponse;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class LaundryActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.iv_option) ImageView ivOption;
	@Bind(R.id.tv_option) TextView tvOption;
	@Bind(R.id.ll_option) LinearLayout llOption;
	
	@Bind(R.id.rl_chose_time) RelativeLayout rlChoseTime;
	@Bind(R.id.tv_time) TextView tvTime;
	@Bind(R.id.gv_laundry) GridViewForScrollView gvLaundry;
	@Bind(R.id.gv_elderly) GridViewForScrollView gvElderly;
	
	@Bind(R.id.tv_commit) TextView tvCommit;
	
	private StringBuffer sbDate = new StringBuffer();
	private StringBuffer sbTime = new StringBuffer();
	private int year;
	private int month;
	private int day;
	
	private String userId, shiftId, usrOrg, execTime, bedNo;
	private String[] laundryItems;
	private List<Map<String,String>> laundryList = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> elderlyList = new ArrayList<Map<String,String>>();
	private SimpleAdapter adapter, elderlyAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laundry);
		
		context = this;

		initView();
		initData();
		
		initAdapter(null);
		if (Network.checkNet(context)) {
//			getLaundryInfo();
			getElderlyInfo();
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
		
		RxView.clicks(llOption).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context, LaundryHistroyActivity.class)
						.putExtra("userId", userId).putExtra("shiftId", shiftId));
			}
		});
		
		RxView.clicks(rlChoseTime).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showChooseDateTimeDialog();
			}
		});
		
		RxView.clicks(tvCommit).
		throttleFirst(4, TimeUnit.SECONDS).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				String time = tvTime.getText().toString();
				if(StringUtils.isNotEmpty(time)){
					updateLaundryInfo(time);
				}else{
					Toast.makeText(context, "请选择送洗时间!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		gvLaundry.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(laundryList.size() > 0){
					if(StringUtils.isNotEmpty(laundryList.get(position).get("state"))){
						if(laundryList.get(position).get("state").equals("0")){
							laundryList.get(position).put("state", "1");
						}else{
							laundryList.get(position).put("state", "0");
						}
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	
	private void initData() {
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		usrOrg = getIntent().getStringExtra("usrOrg");
		execTime = getIntent().getStringExtra("execTime");
		bedNo = getIntent().getStringExtra("bedNo");
		
		year = Integer.valueOf(DateOperate.getCurrentDataYear());
		month = Integer.valueOf(DateOperate.getCurrentDataMonth());
		day = Integer.valueOf(DateOperate.getCurrentDataDay());
		
		laundryItems = getResources().getStringArray(R.array.laundry_items);
		
		tvTitle.setText("洗衣服务");
		tvOption.setText("洗衣记录");
		ivOption.setImageResource(R.drawable.icon_item_histroy);
	}
	
	private void getElderlyInfo(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("bedNo", bedNo);
		map.put("execTime", "E");
		
		Subscriber<LeaveSrarchElderlyResponse> subscriber = new Subscriber<LeaveSrarchElderlyResponse>() {

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
			public void onNext(LeaveSrarchElderlyResponse response) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(response != null){
					if(response.isSuccess()){
						dealElderlyData(response.getITEMS());
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getElderlyInfo(subscriber, map);
	}
	
	private void dealElderlyData(List<LeaveElderlyEntity> res){
		for(int i=0; i<res.size(); i++){
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", res.get(i).getName());
			map.put("id", res.get(i).getElderlyId());
			map.put("room", res.get(i).getRoomNo());
			map.put("bed", res.get(i).getBedNo());
			map.put("state", "0");
			elderlyList.add(map);
		}
		
		elderlyAdapter  = new SimpleAdapter(context, elderlyList, R.layout.sannitation_item, new String[] {}, new int[] {}){
			@Override
			public View getView(final int position, View convertView,ViewGroup parent){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.sannitation_item, null);
				TextView tvLaundry = (TextView)convertView.findViewById(R.id.tv_sannitation);
				String name = elderlyList.get(position).get("name");
				String room = elderlyList.get(position).get("room");
				String bed = elderlyList.get(position).get("bed");
				tvLaundry.setText(room+"房间" + bed + "床\n" + name);
				if(elderlyList.get(position).get("state").equals("0")){
					tvLaundry.setTextColor(context.getResources().getColor(R.color.three_black));
					tvLaundry.setBackgroundResource(R.drawable.border_black);
				}else{
					tvLaundry.setTextColor(context.getResources().getColor(R.color.white));
					tvLaundry.setBackgroundResource(R.drawable.border_click_item);
				}
				return convertView;
			}
		};
		gvElderly.setAdapter(elderlyAdapter);
		
		gvElderly.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(StringUtils.isNotEmpty(elderlyList.get(position).get("state"))){
					if(elderlyList.get(position).get("state").equals("0")){
						elderlyList.get(position).put("state", "1");
					}else{
						elderlyList.get(position).put("state", "0");
					}
					elderlyAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void updateLaundryInfo(String time){
		loading = LoadingDialog.loadingSave(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		map.put("usrOrg", usrOrg);
		map.put("sxsj", time);
		StringBuffer sbSxlx = new StringBuffer();
		for(int i=0;i<laundryList.size(); i++){
			if(laundryList.get(i).get("state").equals("1")){
				if(sbSxlx.length() == 0){
					sbSxlx.append(String.valueOf(i));
				}else{
					sbSxlx.append("#");
					sbSxlx.append(String.valueOf(i));
				}
			}
		}
		if(sbSxlx.length() > 0){
			map.put("sxlx", sbSxlx.toString());
		}else{
			Toast.makeText(context, "请选择衣物类型!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		StringBuffer sbElderly = new StringBuffer();
		for(int i=0; i<elderlyList.size(); i++){
			if(elderlyList.get(i).get("state").equals("1")){
				if(sbElderly.length() == 0){
					sbElderly.append(elderlyList.get(i).get("id"));
				}else{
					sbElderly.append("#");
					sbElderly.append(elderlyList.get(i).get("id"));
				}
			}
		}
		if(sbElderly.length() > 0){
			map.put("elderlyId", sbElderly.toString());
		}else{
			Toast.makeText(context, "请选择老人!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		map.put("execTime", execTime);
		
		Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

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
			public void onNext(BaseResponseEntity checkVersion) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(checkVersion != null){
					if(checkVersion.isSuccess()){
						Toast.makeText(context, "洗衣服务完成!", Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Log.e(TAG, "fail - " + checkVersion.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().saveLaundryInfo(subscriber, map);
	}
	
	private void showChooseDateTimeDialog() {
		AlertDialog.Builder builder = new Builder(context);
		View view = View.inflate(context, R.layout.time_picker, null);
		builder.setTitle("请选择送洗时间:");
		final DatePicker datePicker = (DatePicker)view.findViewById(R.id.dp_date_picker);
		final TimePicker timePicker = (TimePicker)view.findViewById(R.id.tp_timepicker);
		
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(sbDate.length() > 0){
					sbDate.delete(0, sbDate.length());
				}
				if(sbTime.length() > 0){
					sbTime.delete(0, sbTime.length());
				}
				
				String strTime = getDateTime(datePicker, timePicker);
				tvTime.setText(strTime);
			
			}
			
		});
		AlertDialog dialog = builder.create();
		dialog.setView(view);

		dialog.show();
	}
	
	private String getDateTime(DatePicker datePicker, TimePicker timePicker){
		sbDate.append(year);
		sbDate.append("-");
		if(datePicker.getMonth()+1 < 10){
			sbDate.append("0");
		}
		sbDate.append(datePicker.getMonth()+1);
		sbDate.append("-");
		if(datePicker.getDayOfMonth() < 10){
			sbDate.append("0");
		}
		sbDate.append(datePicker.getDayOfMonth());
		
		if(timePicker.getCurrentHour() < 10){
			sbTime.append("0");
		}
		sbTime.append(timePicker.getCurrentHour());
		sbTime.append(":");
		if(timePicker.getCurrentMinute() < 10){
			sbTime.append("0");
		}
		sbTime.append(timePicker.getCurrentMinute());
		sbTime.append(":");
		sbTime.append("00");
		
		return (sbDate.toString() + " " + sbTime.toString());
	}
	
	private void getLaundryInfo(){
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
			public void onNext(GetLaundryListResponse checkVersion) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(checkVersion != null){
					if(checkVersion.isSuccess()){
						initAdapter(checkVersion.getITEMS());
					}else{
						Log.e(TAG, "fail - " + checkVersion.getMessage());
						if(checkVersion.getMessage().equals("NOVALUE")){
							initAdapter(null);
						}
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getLaundryListInfo(subscriber, map);
	}
	
	private void initAdapter(List<LaundryItem> lists){
		if(lists != null){
			for(int i=0; i<laundryItems.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemName", laundryItems[i]);
				boolean isFInished = false;
				for(int j=0; j<lists.size(); j++){
					if(lists.get(j).getSxlx().equals(String.valueOf(i))){
						isFInished = true;
						break;
					}
				}
				if(isFInished){
					map.put("state", "1");
				}else{
					map.put("state", "0");
				}
				laundryList.add(map);
			}
		}else{
			for(int i=0; i<laundryItems.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("itemName", laundryItems[i]);
				map.put("state", "0");
				laundryList.add(map);
			}
		}
		if(adapter == null){
			adapter = new SimpleAdapter(context, laundryList, R.layout.sannitation_item, new String[] {}, new int[] {}){
				@Override
				public View getView(final int position, View convertView,ViewGroup parent){
					convertView = LayoutInflater.from(context).inflate(
							R.layout.sannitation_item, null);
					TextView tvLaundry = (TextView)convertView.findViewById(R.id.tv_sannitation);
					tvLaundry.setText(laundryList.get(position).get("itemName"));
					if(laundryList.get(position).get("state").equals("0")){
						tvLaundry.setTextColor(context.getResources().getColor(R.color.three_black));
						tvLaundry.setBackgroundResource(R.drawable.border_black);
					}else{
						tvLaundry.setTextColor(context.getResources().getColor(R.color.white));
						tvLaundry.setBackgroundResource(R.drawable.border_click_item);
					}
					return convertView;
				}
			};
			gvLaundry.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
}
