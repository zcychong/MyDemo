package com.healthmanage.ylis.activity;

import java.util.ArrayList;
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
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.GetSannitationInfoResponse;
import com.healthmanage.ylis.model.LeaveElderlyEntity;
import com.healthmanage.ylis.model.LeaveSrarchElderlyResponse;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SannitationActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.gv_sannitation) GridViewForScrollView gvSannitation;
	@Bind(R.id.gv_elderly) GridViewForScrollView gvElderly;
	@Bind(R.id.tv_commit) TextView tvCommit;
	private String userId, shiftId, usrOrg;
	private String[] strSnanitations;
//	private String[] strSnanitationFinishState;
	private List<Map<String,String>> sannitations = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> elderlyList = new ArrayList<Map<String,String>>();
	private SimpleAdapter adapter, elderlyAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sannitation);
		
		context = this;

		initView();
		initData();
		
		if (Network.checkNet(context)) {
			getSannitationInfo();
			initSannitationInfo();
			getElderlyInfo();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void initView() {
		ButterKnife.bind(context);
		
		RxView.clicks(llBack).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				finish();
			}
		});
		
		RxView.clicks(tvCommit).
		subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				updateSannitation();
			}
		});
		
		
		
		strSnanitations = getResources().getStringArray(R.array.sannitation);
//		strSnanitationFinishState = new String[strSnanitations.length];
		for(int i=0; i<strSnanitations.length; i++){
			Map<String, String> map = new HashMap();
			map.put("sannitation", strSnanitations[i]);
			map.put("state", "0");
			sannitations.add(map);
//			strSnanitationFinishState[i] = "0";
		}
		
		
		gvSannitation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				if(sannitations.get(position).get("state").equals("0")){
					sannitations.get(position).put("state", "1");
//					strSnanitationFinishState[position] = "1";
				}else{
//					strSnanitationFinishState[position] = "0";
					sannitations.get(position).put("state", "0");
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initData() {
		userId = getIntent().getStringExtra("userId");
		shiftId = getIntent().getStringExtra("shiftId");
		usrOrg = getIntent().getStringExtra("usrOrg");
		
		llBack.setVisibility(View.VISIBLE);
		tvTitle.setText("居室卫生");
	}

	private void getElderlyInfo(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);

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
	
	private void getSannitationInfo(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();
		Map<String, String> map = new HashMap<String, String>();
		
		Log.e("userId", userId);
		Log.e("shiftId", shiftId);
		Log.e("usrOrg", usrOrg);
		
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		map.put("usrOrg", usrOrg);
		Subscriber<GetSannitationInfoResponse> subscriber = new Subscriber<GetSannitationInfoResponse>() {

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
			public void onNext(GetSannitationInfoResponse checkVersion) {
				Log.e(TAG, "onNext");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (checkVersion.isSuccess()) {
					//地面
					if(StringUtils.isNotEmpty(checkVersion.getDm())){
						if(checkVersion.getDm().equals("1")){
							sannitations.get(0).put("state", "1");
//							strSnanitationFinishState[0] = "1";
						}else{
							sannitations.get(0).put("state", "0");
//							strSnanitationFinishState[0] = "0";
						}
					}else{
						sannitations.get(0).put("state", "0");
//						strSnanitationFinishState[0] = "0";
					}
					//墙面
					if(StringUtils.isNotEmpty(checkVersion.getQm())){
						if(checkVersion.getQm().equals("1")){
//							strSnanitationFinishState[1] = "1";
							sannitations.get(1).put("state", "1");
						}else{
//							strSnanitationFinishState[1] = "0";
							sannitations.get(1).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[1] = "0";
						sannitations.get(1).put("state", "0");
					}
					//天花板
					if(StringUtils.isNotEmpty(checkVersion.getThb())){
						if(checkVersion.getThb().equals("1")){
//							strSnanitationFinishState[2] = "1";
							sannitations.get(2).put("state", "1");
						}else{
//							strSnanitationFinishState[2] = "0";
							sannitations.get(2).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[2] = "0";
						sannitations.get(2).put("state", "0");
					}
					//窗台
					if(StringUtils.isNotEmpty(checkVersion.getCt())){
						if(checkVersion.getCt().equals("1")){
//							strSnanitationFinishState[3] = "1";
							sannitations.get(3).put("state", "1");
						}else{
//							strSnanitationFinishState[3] = "0";
							sannitations.get(3).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[3] = "0";
						sannitations.get(3).put("state", "0");
					}
					//玻璃	
					if(StringUtils.isNotEmpty(checkVersion.getBl())){
						if(checkVersion.getBl().equals("1")){
//							strSnanitationFinishState[4] = "1";
							sannitations.get(4).put("state", "1");
						}else{
//							strSnanitationFinishState[4] = "0";
							sannitations.get(4).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[4] = "0";
						sannitations.get(4).put("state", "0");
					}
					//门
					if(StringUtils.isNotEmpty(checkVersion.getMen())){
						if(checkVersion.getMen().equals("1")){
//							strSnanitationFinishState[5] = "1";
							sannitations.get(5).put("state", "1");
						}else{
//							strSnanitationFinishState[5] = "0";
							sannitations.get(5).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[5] = "0";
						sannitations.get(5).put("state", "0");
					}
					//灯具
					if(StringUtils.isNotEmpty(checkVersion.getDj())){
						if(checkVersion.getDj().equals("1")){
//							strSnanitationFinishState[6] = "1";
							sannitations.get(6).put("state", "1");
						}else{
//							strSnanitationFinishState[6] = "0";
							sannitations.get(6).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[6] = "0";
						sannitations.get(6).put("state", "0");
					}
					//家具
					if(StringUtils.isNotEmpty(checkVersion.getJj())){
						if(checkVersion.getJj().equals("1")){
//							strSnanitationFinishState[7] = "1";
							sannitations.get(7).put("state", "1");
						}else{
//							strSnanitationFinishState[7] = "0";
							sannitations.get(7).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[7] = "0";
						sannitations.get(7).put("state", "0");
					}
					//卫生间
					if(StringUtils.isNotEmpty(checkVersion.getWsj())){
						if(checkVersion.getWsj().equals("1")){
//							strSnanitationFinishState[8] = "1";
							sannitations.get(8).put("state", "1");
						}else{
//							strSnanitationFinishState[8] = "0";
							sannitations.get(8).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[8] = "0";
						sannitations.get(8).put("state", "0");
					}
					//窗帘
					if(StringUtils.isNotEmpty(checkVersion.getCl())){
						if(checkVersion.getCl().equals("1")){
//							strSnanitationFinishState[9] = "1";
							sannitations.get(9).put("state", "1");
						}else{
//							strSnanitationFinishState[9] = "0";
							sannitations.get(9).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[9] = "0";
						sannitations.get(9).put("state", "0");
					}
					//电器
					if(StringUtils.isNotEmpty(checkVersion.getDq())){
						if(checkVersion.getDq().equals("1")){
//							strSnanitationFinishState[10] = "1";
							sannitations.get(10).put("state", "1");
						}else{
//							strSnanitationFinishState[10] = "0";
							sannitations.get(10).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[10] = "0";
						sannitations.get(10).put("state", "0");
					}
					//公共区域设施设备
					if(StringUtils.isNotEmpty(checkVersion.getGgqysb())){
						if(checkVersion.getGgqysb().equals("1")){
//							strSnanitationFinishState[11] = "1";
							sannitations.get(11).put("state", "1");
						}else{
//							strSnanitationFinishState[11] = "0";
							sannitations.get(11).put("state", "0");
						}
					}else{
//						strSnanitationFinishState[11] = "0";
						sannitations.get(11).put("state", "0");
					}
//					if(adapter != null){
//						adapter.notifyDataSetChanged();
//					}
					adapter = new SimpleAdapter(context, sannitations, R.layout.sannitation_item, new String[] {}, new int[] {}){
						@Override
						public View getView(final int position, View convertView,ViewGroup parent){
							convertView = LayoutInflater.from(context).inflate(
									R.layout.sannitation_item, null);
							TextView tvSannitation = (TextView)convertView.findViewById(R.id.tv_sannitation);
							tvSannitation.setText(strSnanitations[position]);
							if(sannitations.get(position).get("state").equals("0")){
								tvSannitation.setTextColor(context.getResources().getColor(R.color.three_black));
								tvSannitation.setBackgroundResource(R.drawable.border_black);
							}else{
								tvSannitation.setTextColor(context.getResources().getColor(R.color.white));
								tvSannitation.setBackgroundResource(R.drawable.border_click_item);
							}
							return convertView;
						}
					};
						
					gvSannitation.setAdapter(adapter);
					
				}
			}
		};
		HttpMethodImp.getInstance().getSannatationInfo(subscriber, map);
	}

	private void initSannitationInfo(){
		adapter = new SimpleAdapter(context, sannitations, R.layout.sannitation_item, new String[] {}, new int[] {}){
			@Override
			public View getView(final int position, View convertView,ViewGroup parent){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.sannitation_item, null);
				TextView tvSannitation = (TextView)convertView.findViewById(R.id.tv_sannitation);
				tvSannitation.setText(strSnanitations[position]);
				if(sannitations.get(position).get("state").equals("0")){
					tvSannitation.setTextColor(context.getResources().getColor(R.color.three_black));
					tvSannitation.setBackgroundResource(R.drawable.border_black);
				}else{
					tvSannitation.setTextColor(context.getResources().getColor(R.color.white));
					tvSannitation.setBackgroundResource(R.drawable.border_click_item);
				}
				return convertView;
			}
		};

		gvSannitation.setAdapter(adapter);
	}
	
	private void updateSannitation(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("shiftId", shiftId);
		
		map.put("dm", sannitations.get(0).get("state"));
		map.put("qm", sannitations.get(1).get("state"));
		map.put("thb", sannitations.get(2).get("state"));
		map.put("ct", sannitations.get(3).get("state"));
		
		map.put("bl", sannitations.get(4).get("state"));
		map.put("men", sannitations.get(5).get("state"));
		map.put("dj", sannitations.get(6).get("state"));
		map.put("jj", sannitations.get(7).get("state"));
		
		map.put("wsj", sannitations.get(8).get("state"));
		map.put("cl", sannitations.get(9).get("state"));
		map.put("dq", sannitations.get(10).get("state"));
		map.put("ggqysb", sannitations.get(11).get("state"));

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
		map.put("elderlyId", sbElderly.toString());
		
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
				if (checkVersion.isSuccess()) {
					Toast.makeText(context, "所选任务已完成!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		};
		HttpMethodImp.getInstance().saveSannitationInfo(subscriber, map);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
	
}
