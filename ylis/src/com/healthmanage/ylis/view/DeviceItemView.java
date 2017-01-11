package com.healthmanage.ylis.view;


import com.healthmanage.ylis.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceItemView extends LinearLayout{
	private Context mContext;
	private TextView tvType, tvName;
	private ImageView ivDeviceType, ivDeleteDevice;
	private Button btnAddDevice;
	
	private int deviceType = -1;//设备类型  1-血压 2-血氧 3-血糖 4-肺活量 5-尿液分析 6-耳温枪
	private boolean isAdd = false;
	private String strDeviceName;
	public DeviceItemView(Context context, boolean add, int type, String name){
		super(context);
		mContext = context;
		deviceType = type;
		strDeviceName = name;
		isAdd = add;
		
		initView();
		initData();
	}
	
	private void initView(){
		LayoutInflater localinflater =  (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = localinflater.inflate(R.layout.device_item, this);
		
		tvType = (TextView)view.findViewById(R.id.tv_device_type);
		tvName = (TextView)view.findViewById(R.id.tv_device_name);
		
		ivDeviceType = (ImageView)view.findViewById(R.id.iv_decive);
		ivDeleteDevice = (ImageView)view.findViewById(R.id.iv_delete);
		btnAddDevice = (Button)view.findViewById(R.id.btn_add_device);
	}
	
	private void initData(){
		switch (deviceType) {
		case 1:
			tvType.setText(R.string.nibp_name);
			ivDeviceType.setBackgroundResource(R.drawable.blood_pressure);
			break;
		case 2:
			tvType.setText(R.string.spo_name);
			ivDeviceType.setBackgroundResource(R.drawable.spo);
			break;
		case 3:
			tvType.setText(R.string.bg_name);
			ivDeviceType.setBackgroundResource(R.drawable.bgo);
			break;
		case 4:
			tvType.setText(R.string.pulm_name);
			ivDeviceType.setBackgroundResource(R.drawable.viteal_capacity);
			break;
		case 5:
			tvType.setText(R.string.bc_device_name);
			ivDeviceType.setBackgroundResource(R.drawable.bc);
			break;
		case 6:
			tvType.setText(R.string.temp_name);
			ivDeviceType.setBackgroundResource(R.drawable.eet);
			break;
		default:
			break;
		}
		
		tvName.setText(strDeviceName);
		if(isAdd){
			btnAddDevice.setVisibility(View.VISIBLE);
			ivDeleteDevice.setVisibility(View.GONE);
		}else{
			btnAddDevice.setVisibility(View.GONE);
			ivDeleteDevice.setVisibility(View.VISIBLE);
		}
	}

	public ImageView getIvDeleteDevice() {
		return ivDeleteDevice;
	}

	public void setIvDeleteDevice(ImageView ivDeleteDevice) {
		this.ivDeleteDevice = ivDeleteDevice;
	}

	public Button getBtnAddDevice() {
		return btnAddDevice;
	}

	public void setBtnAddDevice(Button btnAddDevice) {
		this.btnAddDevice = btnAddDevice;
	}
	
	
}
