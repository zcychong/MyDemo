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
import com.healthmanage.ylis.model.LeaveElderlyEntity;
import com.healthmanage.ylis.model.LeaveSrarchElderlyResponse;
import com.healthmanage.ylis.view.GridViewForScrollView;
import com.jakewharton.rxbinding.view.RxView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseElderlyActivity extends BaseActivity {
	private Activity context;
	private Dialog loading;

	@Bind(R.id.tv_title) TextView tvTitle;

	@Bind(R.id.tv_back_text) TextView tvBackText;
	@Bind(R.id.iv_back) ImageView ivBack;
	@Bind(R.id.ll_back) LinearLayout llBack;
	
	@Bind(R.id.tv_commit) TextView tvCommit;
	@Bind(R.id.gv_elderly) GridViewForScrollView gvElderly;
	private String userId, type, bedNo, execTime;
	private List<Map<String,String>> laundryList = new ArrayList<Map<String,String>>();
	private SimpleAdapter adapter;
	
	private StringBuffer sbElderlyNames = new StringBuffer(), sbElderlyIds = new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_elderly);
		
		context = this;
		
		initView();
		initData();
		
		getElderlyList();
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
		
		RxView.clicks(tvCommit)
			.subscribe(new Action1<Void>() {
				@Override
				public void call(Void aVoid) {
					sbElderlyNames.delete(0, sbElderlyNames.length());
					sbElderlyIds.delete(0, sbElderlyIds.length());
					for(int i=0; i<laundryList.size(); i++){
						if(laundryList.get(i).get("state").equals("1")){
							if(sbElderlyNames.length() == 0){
								sbElderlyNames.append(laundryList.get(i).get("name"));
								sbElderlyIds.append(laundryList.get(i).get("elderlyId"));
							}else{
								sbElderlyNames.append(",");
								sbElderlyNames.append(laundryList.get(i).get("name"));
								sbElderlyIds.append("#");
								sbElderlyIds.append(laundryList.get(i).get("elderlyId"));
							}
						}
					}
					Intent intent = new Intent();
					intent.putExtra("names", sbElderlyNames.toString());
					intent.putExtra("ids", sbElderlyIds.toString());
					setResult(1, intent);
					finish();
				}
			});
	}
	
	private void initData(){
		userId = getIntent().getStringExtra("userId");
		type = getIntent().getStringExtra("type");
		if(type.equals("1")){
			bedNo  = getIntent().getStringExtra("bedNo");
			execTime  = getIntent().getStringExtra("execTime");
		}
	}
	
	private void getElderlyList(){
		if (Network.checkNet(context)) {
			getData();
		}else{
			Toast.makeText(context, R.string.need_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void getData(){
		loading = LoadingDialog.loadingFind(context);
		loading.show();

		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("bedNo", bedNo);
		map.put("execTime", execTime);

		Subscriber<LeaveSrarchElderlyResponse> subscriber = new Subscriber<LeaveSrarchElderlyResponse>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError");
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				Log.e(TAG, e.getLocalizedMessage());
			}

			@Override
			public void onNext(LeaveSrarchElderlyResponse taseList) {
				if (loading != null) {
					loading.dismiss();
					loading = null;
				}
				if (taseList != null) {
					if (taseList.isSuccess()) {
						dealData(taseList.getITEMS());
					} else {
						Log.e(TAG, "fail - " + taseList.getMessage());
					}
				} else {
					Log.e(TAG, "fail - null");
				}
			}
		};
		HttpMethodImp.getInstance().getElderlyInfo(subscriber, map);
	}
	
	private void dealData(List<LeaveElderlyEntity> lists){
		for(int i=0; i<lists.size(); i++){
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", lists.get(i).getName());
			map.put("elderlyId", lists.get(i).getElderlyId());
			map.put("state", "0");
			laundryList.add(map);
		}
		adapter = new SimpleAdapter(context, laundryList, R.layout.sannitation_item, new String[] {}, new int[] {}){
			@Override
			public View getView(final int position, View convertView,ViewGroup parent){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.sannitation_item, null);
				TextView tvLaundry = (TextView)convertView.findViewById(R.id.tv_sannitation);
				tvLaundry.setText(laundryList.get(position).get("name"));
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
		gvElderly.setAdapter(adapter);
		
		gvElderly.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				sbElderlyNames.delete(0, sbElderlyNames.length());
				sbElderlyIds.delete(0, sbElderlyIds.length());
				if(laundryList.size() > 0){
					if(StringUtils.isNotEmpty(laundryList.get(position).get("state"))){
						if(laundryList.get(position).get("state").equals("0")){
							laundryList.get(position).put("state", "1");
//							if(sbElderlyIds.length() ==0){
//								sbElderlyNames.append(laundryList.get(position).get("name"));
//								sbElderlyIds.append(laundryList.get(position).get("elderlyId"));
//							}else{
//								sbElderlyNames.append(",");
//								sbElderlyNames.append(laundryList.get(position).get("name"));
//								sbElderlyIds.append("#");
//								sbElderlyIds.append(laundryList.get(position).get("elderlyId"));
//							}
						}else{
							laundryList.get(position).put("state", "0");
						}
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}
	
}
