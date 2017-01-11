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
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.TurnBodyItem;
import com.jakewharton.rxbinding.view.RxView;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandTurnBodyActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.ll_back) LinearLayout llBack;
	@Bind(R.id.tv_title) TextView tvTitle;
	@Bind(R.id.ll_option) LinearLayout llOption;
	@Bind(R.id.iv_option) ImageView  ivOption;
	@Bind(R.id.tv_option) TextView tvOption;
	
	@Bind(R.id.tv_time) TextView tvTime;
	
	@Bind(R.id.rl_chose_time) RelativeLayout rlTime;
	@Bind(R.id.tv_decubitus) TextView tvDecubitus;
	@Bind(R.id.rl_decubitus) RelativeLayout rlDecubitus;
	@Bind(R.id.rl_rlderly_name) RelativeLayout rlElderlyName;
	@Bind(R.id.tv_rlderly_name) TextView tvRlderlyName;
	@Bind(R.id.tv_turn_body_exception) TextView tvTurnBodyException;
	@Bind(R.id.tv_turn_body_nromal) TextView tvTurnBodyNromal;
	@Bind(R.id.rl_add_exception) RelativeLayout rlAddException;
	@Bind(R.id.lv_turn_body_list) ListView lvTurnBodyList;
	@Bind(R.id.v_line) View vLine;
	
	@Bind(R.id.tv_commit) TextView tvCommit;

	private boolean isNormal = true;
	private TurnBodyAdapter adapter;
	private String userId, execTime, shiftId, usrOrg, bedNo, ids;
	private String strTime, strDecubitus;
	private List<TurnBodyItem> turnBodyList = new ArrayList<TurnBodyItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn_body);
		context = this;

		initView();
		initData();
	}

	private void initView() {
		ButterKnife.bind(context);

		tvTitle.setText("翻身");
		llBack.setVisibility(View.VISIBLE);
		
		ivOption.setImageResource(R.drawable.icon_item_histroy);
		tvOption.setText("翻身记录");
		
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void aVoid) {
						finish();
					}
				});
		
		RxView.clicks(llOption).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				startActivity(new Intent(context, TurnBodyHistroyActivity.class)
				.putExtra("userId", userId).putExtra("shiftId", shiftId));
			}
		});
		
		RxView.clicks(llBack).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				finish();
			}
		});

		RxView.clicks(rlTime).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showTimeDialog();
			}
		});
		
		RxView.clicks(rlDecubitus).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				showDecubitusDialog();
			}
		});
		
		RxView.clicks(tvTurnBodyNromal)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				tvTurnBodyNromal.setBackgroundResource(R.drawable.login_btn_left_checked);
				tvTurnBodyException.setBackgroundResource(R.drawable.login_btn_right);
				tvTurnBodyNromal.setTextColor(getResources().getColor(R.color.white));
				tvTurnBodyException.setTextColor(getResources().getColor(R.color.three_black));
				
				rlAddException.setVisibility(View.GONE);
				rlElderlyName.setVisibility(View.VISIBLE);
				lvTurnBodyList.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				isNormal = true;
			}
		});
		
		RxView.clicks(tvTurnBodyException)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				tvTurnBodyNromal.setBackgroundResource(R.drawable.login_btn_left);
				tvTurnBodyException.setBackgroundResource(R.drawable.login_btn_right_checked);
				tvTurnBodyNromal.setTextColor(getResources().getColor(R.color.three_black));
				tvTurnBodyException.setTextColor(getResources().getColor(R.color.white));
				
				rlAddException.setVisibility(View.VISIBLE);
				rlElderlyName.setVisibility(View.GONE);
				lvTurnBodyList.setVisibility(View.VISIBLE);
				vLine.setVisibility(View.VISIBLE);
				isNormal = false;
			}
		});

		RxView.clicks(rlAddException)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				if(StringUtils.isEmpty(strTime)){
					Toast.makeText(context, "请选择翻身时间!", Toast.LENGTH_SHORT).show();
					return;
				}
				if(StringUtils.isEmpty(strDecubitus)){
					Toast.makeText(context, "请选择卧位!", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(context, TurnBodyInfoActivity.class);
				intent.putExtra("userId", userId);
				intent.putExtra("shiftId", shiftId);
				intent.putExtra("execTime", execTime);
				intent.putExtra("usrOrg", usrOrg);
				intent.putExtra("strTime", strTime);
				intent.putExtra("strDecubitus", strDecubitus);
				intent.putExtra("bedNo", bedNo);
				startActivityForResult(intent, 1);
			}
		});
		
		RxView.clicks(rlElderlyName).throttleFirst(4, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				Intent intent = new Intent(context, ChooseElderlyActivity.class);
				intent.putExtra("userId", userId);
				if(StringUtils.isNotEmpty(bedNo)){
					intent.putExtra("type", "1");
					intent.putExtra("bedNo", bedNo);
					intent.putExtra("execTime", "E");
				}else{
					intent.putExtra("type", "0");
				}
				startActivityForResult(intent, 1);
			}
		});
		
		RxView.clicks(tvCommit).throttleFirst(2, TimeUnit.SECONDS)
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				if(isNormal){
					updateNormal();
				}else{
					Toast.makeText(context, "翻身记录提交完成!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
		
		
		
	}

	private void initData() {
		shiftId = getIntent().getStringExtra("shiftId");
		userId = getIntent().getStringExtra("userId");
		execTime = getIntent().getStringExtra("execTime");
		usrOrg = getIntent().getStringExtra("usrOrg");
		bedNo = getIntent().getStringExtra("bedNo");
	}
	
	private void updateNormal(){
		if(StringUtils.isEmpty(strTime)){
			Toast.makeText(context, "请选翻身择时间!", Toast.LENGTH_SHORT).show();
			return;
		}
		if(StringUtils.isEmpty(strDecubitus)){
			Toast.makeText(context, "请选卧位!", Toast.LENGTH_SHORT).show();
			return;
		}
		if(StringUtils.isEmpty(tvRlderlyName.getText().toString())){
			Toast.makeText(context, "请选老人!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Network.checkNet(context)) {
			update();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void update(){
		loading = LoadingDialog.loadingSave(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		map.put("execTime", execTime);
		map.put("usrOrg", usrOrg);
		map.put("elderlyId", ids);
		map.put("fssj", strTime);
		map.put("wowei", strDecubitus);
		map.put("execTime", "E");
		map.put("status", "0");
		
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
						Toast.makeText(context, "翻身记录提交完成!", Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Log.e(TAG, "fail - " + response.getMessage());
					}
				}else{
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().saveTurnBodyInfo(subscriber, map);
	}
	
	private void showTimeDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("选择翻身时间");
		final String[] times;
		if(execTime.equals("D")){
			times = getResources().getStringArray(R.array.turn_body_time_night);
		}else{
			times = getResources().getStringArray(R.array.turn_body_time_day);
		}
		builder.setSingleChoiceItems(times, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int posotion) {
				dialog.dismiss();
				tvTime.setText(times[posotion]);
				strTime = times[posotion];
			}
		}); 
		builder.create().show();
	}
	
	private void showDecubitusDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("选择卧位");
		final String[] decubitus = getResources().getStringArray(R.array.turn_body_decubitus);
		builder.setSingleChoiceItems(decubitus, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int posotion) {
				dialog.dismiss();
				tvDecubitus.setText(decubitus[posotion]);
				strDecubitus = String.valueOf(posotion);
			}
		}); 
		builder.create().show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(requestCode == 1){
				if(resultCode == 1){
					String names = data.getStringExtra("names");
					ids = data.getStringExtra("ids");
					tvRlderlyName.setText(names);
				}else if(resultCode == 2){
					initAdapter(data);
					
				}
			}
		}
	}
	
	private void initAdapter(Intent data){
		lvTurnBodyList.setVisibility(View.VISIBLE);
		String strSkinInfo = data.getStringExtra("strSkinInfo");
		String strDealInfo = data.getStringExtra("strDealInfo");
		String strDealResult = data.getStringExtra("strDealResult");
		String elderlyNames = data.getStringExtra("names");
		String[] decubitus = getResources().getStringArray(R.array.turn_body_decubitus); 
		TurnBodyItem item = new TurnBodyItem();
		item.setFssj(strTime);
		item.setName(elderlyNames);
		item.setPfqk(strSkinInfo);
		item.setClfs(strDealInfo);
		item.setLrfy(strDealResult);
		item.setWowei( decubitus[Integer.valueOf(strDecubitus)]);
		
		turnBodyList.add(item);
		
		if(adapter == null){
			adapter = new TurnBodyAdapter(context, turnBodyList);
		}else{
			adapter.notifyDataSetChanged();
		}
		lvTurnBodyList.setAdapter(adapter);
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
			
			TurnBodyItem item = taskList.get(position);
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
				viewHolder.patroalInfo.setText("卧位: " + item.getWowei());
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
			private ImageView ivColor;
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
