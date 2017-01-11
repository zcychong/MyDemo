package com.healthmanage.ylis.activity;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.bluetooth.BluetoothManager;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.db.DBManager;
import com.healthmanage.ylis.deviceentity.DeviceDao;
import com.healthmanage.ylis.view.DeviceItemView;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceManagerActivity extends BaseActivity {
	private Activity context;
	
	private Button btnSearch;
	private LinearLayout llBack;
	private TextView tvTitle;
	private LinearLayout llDeviceList, llSearch, llSearchDevice;
	private TextView tvSearchInfo;
	private ProgressBar pbSearch;
	
	private BluetoothManager bluetoothManager;
	private BluetoothServerSocket mmServerSocket;
	private DBManager dbManager = null;
	
	private String name = "";
	private int length = 0;
	private String[] arrDeviceList;// 所以设备类型
	private List<DeviceDao> deviceList = new ArrayList<DeviceDao>();
	private List<DeviceDao> findDeviceList = new ArrayList<DeviceDao>();
	private View view;
	
//	private Map<String, BluetoothDevice> dMap = new HashMap<String, BluetoothDevice>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_manager_layout);
		context = this;

		
		// 注册蓝牙监听广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(receiver, intentFilter);
		
		initView();
		initData();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
//		getDBManager().closeDb();
		unregisterReceiver(receiver);
	}
	
	private void initView(){

		llBack = (LinearLayout)findViewById(R.id.ll_back);
		llBack.setVisibility(View.VISIBLE);
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvTitle = (TextView) context.findViewById(R.id.tv_title);
		tvTitle.setText("设备管理");

		arrDeviceList = getResources().getStringArray(R.array.device_list);
		llDeviceList = (LinearLayout)context.findViewById(R.id.ll_device_list);
		llSearch = (LinearLayout)context.findViewById(R.id.ll_search);
		llSearchDevice = (LinearLayout)context.findViewById(R.id.ll_search_device);
		tvSearchInfo = (TextView)context.findViewById(R.id.tv_search_info);
		pbSearch = (ProgressBar)context.findViewById(R.id.pb_search);
		btnSearch = (Button)context.findViewById(R.id.btn_begin);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llSearch.setVisibility(View.VISIBLE);
				tvSearchInfo.setText("正在搜索设备");
				pbSearch.setVisibility(View.VISIBLE);
//				dMap.clear();
				if(findDeviceList != null){
					if(findDeviceList.size() > 0){
						findDeviceList.clear();
					}
				}
				if(llSearchDevice != null){
					llSearchDevice.removeAllViews();
				}
				findNewDevice();
			}
		});
	}
	
	private void initData(){
		if(llDeviceList != null){
			llDeviceList.removeAllViews();
		}
//		if(checkBluetooth()){
//			Set<BluetoothDevice> devices = (bluetoothManager.getBluetoothAdapter())
//					.getBondedDevices();
//			String name = null;
//			Map<String, BluetoothDevice> dMap = new HashMap<String, BluetoothDevice>();
		if(deviceList != null){
			if(deviceList.size() >0){
				deviceList.clear();
			}
		}
		if(getDBManager().getDeviceList() != null){
			if(getDBManager().getDeviceList().size() > 0){
				deviceList = getDBManager().getDeviceList();
			}
		}
		
//			for (BluetoothDevice device : devices) {
		Log.e("TAG", "初始化设备数据");
			for (final DeviceDao device : deviceList) {
//				dMap.put(device.getName(), device);
				for(int i=0; i<arrDeviceList.length; i++){
					if (device.getDeviceName().contains(arrDeviceList[i])) {
						final DeviceItemView temp = new DeviceItemView(context,false, i+1,device.getDeviceName());
						temp.getIvDeleteDevice().setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);

								builder.setTitle("提示");
								builder.setMessage("删除该设备?");
								builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										getDBManager().deleteDevice(device.getDeviceName());
										initData();
									}
								});
								builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								});
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						});
						llDeviceList.addView(temp);
						length ++;
						Log.e("device.getName()-----", device.getDeviceName() + "--add");
					}
				}
				Log.e("device.getName()-----", device.getDeviceName());
			}
//		}

	}
	
	
	/**
	 * 蓝牙设备监听
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			Log.e("action", action);
			int connectState;
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				final BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.e("device.getName()", "--" + device.getName() + "--");
				if (StringUtils.isNotEmpty(device.getName())) {
					for(int i=0; i<arrDeviceList.length; i++){
						if (device.getName().contains(arrDeviceList[i])) {
							Log.e("iii", String.valueOf(i));
							for(int j=0;j<deviceList.size();j++){
								Log.e("jjj", String.valueOf(j));
								if(!device.getName().equals(deviceList.get(j).getDeviceName())){
									connect(device,i);
									break;
								}
							}
							Log.e("deviceList.size()", String.valueOf(deviceList.size()));
							if(deviceList.size() == 0){
								connect(device,i);
							}
						}
					}
				}
			}else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				tvSearchInfo.setText("搜索完成");
				pbSearch.setVisibility(View.GONE);
			}
			else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//				Log.e("ACTION_BOND_STATE_CHANGED", device.getName());
				int type= 0;
				if (device.getName().contains(name)) {
					connectState = device.getBondState();
					if(connectState == BluetoothDevice.BOND_BONDED){
						for(int i=0; i<arrDeviceList.length; i++){
							if(device.getName().contains(arrDeviceList[i])){
								type = i + 1;
							}
						}
						Log.e("配对成功!", String.valueOf(connectState));

						llSearchDevice.removeView(view);
						getDBManager().addDevice(device.getName(), String.valueOf(type));
						tvSearchInfo.setText("添加完成");
						pbSearch.setVisibility(View.GONE);
						initData();
					}
					
				}
			}
		}
	};
	
	private void connect(final BluetoothDevice device, int i){
		bluetoothManager.getBluetoothAdapter()
				.cancelDiscovery();
		int connectState;
		connectState = device.getBondState();
		Log.e("connectState", String.valueOf(connectState));
		switch (connectState) {
		case BluetoothDevice.BOND_NONE:
			//找到匹配设备 保存
			DeviceDao tempDevice = new DeviceDao();
			tempDevice.setDeviceName(device.getName());
			tempDevice.setDeviceType(String.valueOf(i+1));
			findDeviceList.add(tempDevice);
			final DeviceItemView temp = new DeviceItemView(context,true,i+1,device.getName());
			temp.getBtnAddDevice().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tvSearchInfo.setText("正在添加");
					pbSearch.setVisibility(View.VISIBLE);
					view = temp;
					name = device.getName();
					try {
						Log.e("action", "开始配对!");
						Method createBondMethod = BluetoothDevice.class
								.getMethod("createBond");
						createBondMethod.invoke(device);
					} catch (Exception e) {
						Log.e("action", "配对失败!");
						e.printStackTrace();
					}
				}
			});
			llSearchDevice.addView(temp);
			break;
		case BluetoothDevice.BOND_BONDED:
			break;
		}
	}
	
	private DBManager getDBManager(){
		if(dbManager == null){
			dbManager = new DBManager(context);
		}
		return dbManager;
	}
	
	
	/**
	 * 检测蓝牙设备
	 */
	private boolean checkBluetooth() {
		if (null == bluetoothManager) {
			bluetoothManager = new BluetoothManager();
		}
		if (!BluetoothManager.isBluetoothSupported()) {
			Toast.makeText(context, "您的设备不支持蓝牙!", Toast.LENGTH_LONG).show();
			return false;
		} else {
			if (!BluetoothManager.turnOnBluetooth()) {
				Toast.makeText(context, "您的蓝牙未打开!", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}

	private void findNewDevice() {
		if(checkBluetooth()){
			bluetoothManager.getBluetoothAdapter().startDiscovery();
		}
	}
}
