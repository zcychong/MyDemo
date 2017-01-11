package com.healthmanage.ylis.activity;

import java.util.ArrayList;
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
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.GerAllUserResponse;
import com.healthmanage.ylis.model.PatroalExceptionEntity;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PatroalActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.iv_option) ImageView ivOption;
	@Bind(R.id.tv_option) TextView tvOption;
	@Bind(R.id.ll_option) LinearLayout llOption;
	
	@Bind(R.id.tv_patroal_nromal) TextView tvPatroalNormal;
	@Bind(R.id.tv_patroal_exception) TextView tvPatroalException;
	@Bind(R.id.rl_add_exception) RelativeLayout rlAddException;
	
	@Bind(R.id.tv_time) TextView tvTime;
	@Bind(R.id.rl_chose_time) RelativeLayout rlChoseTime;
	@Bind(R.id.lv_patroal_list) ListView lvPartoalList;
	@Bind(R.id.v_line) View vLine;
	@Bind(R.id.actv_patroa_people) AutoCompleteTextView actvPatroalPeople;
	@Bind(R.id.tv_commit) TextView tvCommit;
	
	private boolean isException = false;
	private String userId, execTime, shiftId, usrOrg;
	private String patroalPeople;
	private List<PatroalExceptionEntity> exceptionList = new ArrayList<PatroalExceptionEntity>();
	private ArrayList<HashMap<String, String>> userInfoList = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter actvAdapter;
	private PatraoalExceptionAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patroal);
		context = this;
		
		initView();
		initData();

		if (Network.checkNet(context)) {
			getUsetAll();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
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
		
		RxView.clicks(tvPatroalNormal)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				tvPatroalNormal.setBackgroundResource(R.drawable.login_btn_left_checked);
				tvPatroalException.setBackgroundResource(R.drawable.login_btn_right);
				tvPatroalNormal.setTextColor(getResources().getColor(R.color.white));
				tvPatroalException.setTextColor(getResources().getColor(R.color.three_black));
				
				rlAddException.setVisibility(View.GONE);
				lvPartoalList.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				isException = false;
			}
		});
		
		RxView.clicks(tvPatroalException)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				tvPatroalNormal.setBackgroundResource(R.drawable.login_btn_left);
				tvPatroalException.setBackgroundResource(R.drawable.login_btn_right_checked);
				tvPatroalNormal.setTextColor(getResources().getColor(R.color.three_black));
				tvPatroalException.setTextColor(getResources().getColor(R.color.white));
				
				rlAddException.setVisibility(View.VISIBLE);
				lvPartoalList.setVisibility(View.VISIBLE);
				vLine.setVisibility(View.VISIBLE);
				isException = true;
			}
		});
		
		
		RxView.clicks(rlAddException)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				
				if(StringUtils.isEmpty(tvTime.getText().toString())){
					Toast.makeText(context, "请选择巡视时间!", Toast.LENGTH_SHORT).show();
					return ;
				}
				patroalPeople = actvPatroalPeople.getText().toString();
				if(StringUtils.isEmpty(patroalPeople)){
					Toast.makeText(context, "请添加巡视人!", Toast.LENGTH_SHORT).show();
					return ;
				}
				Intent intent = new Intent(context, PatroalAddExceptionActivity.class);
				intent.putExtra("userId", userId);
				intent.putExtra("shiftId", shiftId);
				intent.putExtra("xsrmc", patroalPeople);
				intent.putExtra("execTime", execTime);
				intent.putExtra("patroalTime", tvTime.getText().toString());
				intent.putExtra("patroalPeople", patroalPeople);
				intent.putExtra("usrOrg", usrOrg);
				startActivityForResult(intent, 0);
			}
		});
		
		RxView.clicks(rlChoseTime)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showChoseTimeDialog();
			}
		});
		
		RxView.clicks(tvCommit)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				if(isException){
					Toast.makeText(context, "巡视记录添加完成!", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					updatePatroal();
				}
			}
		});
		
		RxView.clicks(llOption)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, PatroalHistroyActivity.class);
				intent.putExtra("userId", userId);
				intent.putExtra("shiftId", shiftId);
				startActivity(intent);
			}
		});
		
		
	}
	
	private void initData(){
		shiftId = getIntent().getStringExtra("shiftId");
		userId = getIntent().getStringExtra("userId");
		execTime = getIntent().getStringExtra("execTime");
		usrOrg = getIntent().getStringExtra("usrOrg");
		
		tvTitle.setText("巡视记录");
		ivOption.setImageResource(R.drawable.icon_item_histroy);
		tvOption.setText("记录信息");
	}

	private void getUsetAll(){
		loading = LoadingDialog.loadingSave(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrOrg", usrOrg);

		Subscriber<GerAllUserResponse> subscriber = new Subscriber<GerAllUserResponse>() {

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
			public void onNext(GerAllUserResponse response) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(response != null){
					if(response.isSuccess()){
						dealAllUsetInfo(response);
						Log.e(TAG, "success");
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getAllUserList(subscriber, map);
	}

	private void dealAllUsetInfo(GerAllUserResponse response){
		for(int i=0; i<response.getITEMS().size(); i++){
			HashMap<String, String> itemMap = new HashMap<String, String>();
			itemMap.put("brandSearchText", response.getITEMS().get(i).getUserNo());
			itemMap.put("brandName", response.getITEMS().get(i).getName());
			userInfoList.add(itemMap);
			Log.e(TAG, response.getITEMS().get(i).getUserNo() + response.getITEMS().get(i).getName());
		}

		actvAdapter = new SimpleAdapter(this, userInfoList,
				R.layout.autocomplete_tv_item, new String[] {
				"brandSearchText", "brandName" }, new int[] {
				R.id.searchText, R.id.brandName });

		actvPatroalPeople.setAdapter(actvAdapter);
		actvPatroalPeople.setThreshold(1);
		actvPatroalPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tv = (TextView)view.findViewById(R.id.brandName);
				String selectName = tv.getText().toString();
				patroalPeople = selectName;
				actvPatroalPeople.setText(selectName);
				actvPatroalPeople.setSelection(selectName.length());
			}
		});
	}

	private void showChoseTimeDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("选择巡视时间");
		final String[] times;
		if(execTime.equals("D")){
			times = getResources().getStringArray(R.array.patroal_times_night);
		}else{
			times = getResources().getStringArray(R.array.patroal_times_day);
		}
		builder.setSingleChoiceItems(times, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int posotion) {
				dialog.dismiss();
				tvTime.setText(times[posotion]);
			}
		}); 
		builder.create().show();
	}
	
	private void updatePatroal(){
		if(StringUtils.isEmpty(tvTime.getText().toString())){
			Toast.makeText(context, "请选择巡视时间!", Toast.LENGTH_SHORT).show();
			return ;
		}
//		patroalPeople = etPatroalPeople.getText().toString();
		if(StringUtils.isEmpty(patroalPeople)){
			Toast.makeText(context, "请添加巡视人!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if (Network.checkNet(context)) {
			savePatroal();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void savePatroal(){
		loading = LoadingDialog.loadingSave(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("execTime", execTime);
		map.put("usrOrg", usrOrg);
		map.put("shiftId", shiftId);
		map.put("xsrmc", patroalPeople);
		map.put("xssj", tvTime.getText().toString());
		if(execTime.equals("D")){
			map.put("xslx", "1");
		}else{
			map.put("xslx", "0");
		}
		map.put("zbxcqk", "0");// 0-正常 1-异常
		
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
			public void onNext(BaseResponseEntity response) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if(response != null){
					if(response.isSuccess()){
						Log.e(TAG, "success");
						Toast.makeText(context, "巡视记录提交完成!", Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().savePatroalNormalItem(subscriber, map);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(resultCode == 1){
				PatroalExceptionEntity item = new PatroalExceptionEntity();
				item.setAnother(data.getStringExtra("another"));
				item.setElderlyInfo(data.getStringExtra("elderlyInfo"));
				item.setHoomBack(data.getStringExtra("hoomBack"));
				item.setHoomName(data.getStringExtra("hoomName"));
				item.setHoomPhone(data.getStringExtra("hoomPhone"));
				
				item.setLeaderName(data.getStringExtra("leaderName"));
				item.setPatroalDeal(data.getStringExtra("patroalDeal"));
				item.setPatroalInfo(data.getStringExtra("patroalInfo"));
				item.setPeopleName(data.getStringExtra("peopleName"));
				item.setTime(data.getStringExtra("time"));
				
				exceptionList.add(item);
				
				if(adapter == null){
					adapter = new PatraoalExceptionAdapter(context, exceptionList);
					lvPartoalList.setAdapter(adapter);
				}else{
					adapter.notifyDataSetChanged();
				}
				
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
	
	class PatraoalExceptionAdapter extends BaseAdapter {
		private List<PatroalExceptionEntity> taskList;

		public PatraoalExceptionAdapter(Context context, List<PatroalExceptionEntity> list) {
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
						R.layout.patroal_exception_histroy_item, null);
				viewHolder = new ViewHolder();
				viewHolder.ivColor = (ImageView) convertView.findViewById(R.id.iv_color);
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
			if(position%2 == 0){
				viewHolder.ivColor.setBackgroundResource(R.color.patroal_exception_a);
			}else{
				viewHolder.ivColor.setBackgroundResource(R.color.patroal_exception_b);
			}
			
			PatroalExceptionEntity item = taskList.get(position);
			if(StringUtils.isNotEmpty(item.getTime())){
				viewHolder.time.setVisibility(View.VISIBLE);
				viewHolder.time.setText("发生时间: " + item.getTime());
			}else{
				viewHolder.time.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getPeopleName())){
				viewHolder.peopleName.setVisibility(View.VISIBLE);
				viewHolder.peopleName.setText("老人姓名: " + item.getPeopleName());
			}else{
				viewHolder.peopleName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getPatroalInfo())){
				viewHolder.patroalInfo.setVisibility(View.VISIBLE);
				viewHolder.patroalInfo.setText("情况简述: " + item.getPatroalInfo());
			}else{
				viewHolder.patroalInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getPatroalDeal())){
				viewHolder.patroalDeal.setVisibility(View.VISIBLE);
				viewHolder.patroalDeal.setText("处理措施: " + item.getPatroalDeal());
			}else{
				viewHolder.patroalDeal.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getElderlyInfo())){
				viewHolder.elderlyInfo.setVisibility(View.VISIBLE);
				viewHolder.elderlyInfo.setText("老人情况: " + item.getElderlyInfo());
			}else{
				viewHolder.elderlyInfo.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getLeaderName())){
				viewHolder.leaderName.setVisibility(View.VISIBLE);
				viewHolder.leaderName.setText("上报领导姓名: " + item.getLeaderName());
			}else{
				viewHolder.leaderName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getHoomName())){
				viewHolder.hoomName.setVisibility(View.VISIBLE);
				viewHolder.hoomName.setText("联系家属姓名: " + item.getHoomName());
			}else{
				viewHolder.hoomName.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getHoomPhone())){
				viewHolder.hoomPhone.setVisibility(View.VISIBLE);
				viewHolder.hoomPhone.setText("家属电话: " + item.getHoomPhone());
			}else{
				viewHolder.hoomPhone.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getHoomBack())){
				viewHolder.hoomBack.setVisibility(View.VISIBLE);
				viewHolder.hoomBack.setText("家属回复: " + item.getHoomBack());
			}else{
				viewHolder.hoomBack.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(item.getAnother())){
				viewHolder.another.setVisibility(View.VISIBLE);
				viewHolder.another.setText("其他: " + item.getAnother());
			}else{
				viewHolder.another.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
			private ImageView ivColor;
			private TextView another, elderlyInfo, hoomBack, hoomName, hoomPhone;
			private TextView leaderName, patroalDeal, patroalInfo, peopleName, time;
		}

	}
}
