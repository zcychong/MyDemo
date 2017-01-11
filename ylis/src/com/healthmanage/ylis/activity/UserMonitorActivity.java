package com.healthmanage.ylis.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Subscriber;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.contec.jar.cmssxt.CmssxtDataJar;

import com.healthmanage.ylis.R;
import com.healthmanage.ylis.adapter.AlertnateDisplayAdapter;
import com.healthmanage.ylis.adapter.BCDataAdapter;
import com.healthmanage.ylis.bluetooth.BluetoothManager;
//import cn.healthmanage.yhylis.common.AlertnateDisplayAdapter;
//import cn.healthmanage.yhylis.common.HttpMethod;
import com.healthmanage.ylis.common.LoadingDialog;
import com.healthmanage.ylis.common.Network;
//import com.healthmanage.ylis.common.ServerURL;
import com.healthmanage.ylis.common.SharedPraferenceUtils;
import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.db.DBManager;
import com.healthmanage.ylis.deviceentity.BCData;
import com.healthmanage.ylis.deviceentity.PatientInfo;
import com.healthmanage.ylis.deviceentity.BcDeviceDao;
import com.healthmanage.ylis.deviceentity.BgDeviceDao;
import com.healthmanage.ylis.deviceentity.DeviceDao;
import com.healthmanage.ylis.deviceentity.NibpDeviceDao;
import com.healthmanage.ylis.deviceentity.PulmDeviceDao;
import com.healthmanage.ylis.deviceentity.SpoDeviceDao;
import com.healthmanage.ylis.deviceentity.TempDeviceDao;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.GetRoomListModelTwoResponse;
import com.healthmanage.ylis.model.NibpEntity;
import com.healthmanage.ylis.model.NibpHistroyResponse;
import com.healthmanage.ylis.model.PulmEntity;
import com.healthmanage.ylis.model.PulmHistroyResponse;
import com.healthmanage.ylis.model.TempEntity;
import com.healthmanage.ylis.model.TempHistroyResponse;
import com.healthmanage.ylis.network.http.HttpMethodImp;
import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.jar.BC401.BC401_Data;
import com.contec.jar.contec08a.DeviceCommand;
import com.contec.jar.contec08a.DevicePackManager;

public class UserMonitorActivity extends Activity implements
		View.OnClickListener {
	private final String TAG = getClass().getSimpleName();
	private Activity context;

	// 每个设备按钮
	private LinearLayout llBloodPressure;
	private LinearLayout llVitealCapacity;
	private LinearLayout llBgo;
	private LinearLayout llBc;
	private LinearLayout llPulm;
	private LinearLayout llEet;

	private TextView tvState;
	private TextView tvName;
	private TextView tvRoomNo;
	private TextView tvBedNo;
//	private TextView tvAge;
//	private TextView tvGender;
	private TextView tvMobielNumber;
	private TextView tvIdCard;
	private BluetoothSocket socket;
	private SoundPool soundpool;
	private PatientInfo patientInfo;

	private BluetoothManager bluetoothManager;
	private String bloodPressureName = "NIBP";// 血压设备名称
	private String vitealCapacityName = "SpO";// 血氧设备名称
	private String bgoName = "BG";// 血糖仪设备名称
	private String bcName = "BC";// 尿液分析仪
	private String pulmName = "PULM";// 肺活量
	private String eetName = "TEMP";// 耳温枪
	private DevicePackManager devicePackManager = new DevicePackManager();
	private ReadThread readThread;

	private byte[] comd_date = new byte[100];
	private byte[] handshakesave;
	private byte[] byte_date;
	private int position = -1;
	private static int STYLEMEDIAN_BLOOD = 0;
	private List<byte[]> deviceData;
	private int handShakeType = 0;// 0 准备校验时间握手 1获取数据握手 2 删除数据握手

	private TextView goTextView;// 显示数据的表头type_list
	private TextView oneTextView;// 表头1
	private TextView twoTextView;// 表头2
	private TextView threeTextView;// 表头3
	private TextView fourTextView;// 表头4
	private LinearLayout titileLayout;// 表头的LinearLayout
	private RelativeLayout rlUserInfo;// 有用户信息的头
	private RelativeLayout llTestInfo;// 测试用户的头
	private EditText etPhoneNumber;
	private LinearLayout llChangeUserInfo, llDeleteUser;
	private LinearLayout llNIBPHint, llBGHint, llPulmHint;
	private ImageView ivPhoneType;

	private String userId;
	private String groupId;// 分组id
	private boolean isTest = false; // 是否是体验用户
	private boolean startFlag = false; // 体验用户输入手机后 置为true，可进行监听
	private String strPhoneNumber = "";

	private String patientId = "0";
	private String itemConValue;// 低压
	private StringBuffer inputDate = new StringBuffer();// 时间
	private String itemValue;// 高压
	private String prVal;// 脉率

	private String PatientNo;
	private String deviceName = "NIBP"; // 当前选择的设备名

	private Boolean flag = false;
	private Dialog loadingDialog;
	private boolean isUpdate = false;

	// 每个设备等待进度条
	private ProgressBar pbBibp;
	private ProgressBar pbSpo;
	private ProgressBar pbBgo;
	private ProgressBar pbPulm;
	private ProgressBar pbBc;
	private ProgressBar pbEet;
	// 每个设备的完成图标
	private ImageView ivNIBPFinish;
	private ImageView ivSoPFinish;
	private ImageView ivBGbpFinish;
	private ImageView ivPULMbpFinish;
	private ImageView ivBCbpFinish;
	private ImageView ivTEMPFinish;

	// 连接设备读取数据线程
	private AcceptThread acceptThread;

	private boolean bloodPressureUpdataState = false;// 血压数据是否上传
	private boolean vitealCapacityUpdataState = false;// 血氧数据是否上传
	private boolean bgoUpdataState = false;// 血糖数据是否上传
	private boolean pulmUpdataState = false;// 肺活量数据是否上传
	private boolean bcUpdataState = false;// 肺活量数据是否上传
	private boolean eetUpdataState = false;// 耳温数据是否上传

	// 获得的数据
	private NibpDeviceDao nibpDevice;
	private SpoDeviceDao spoDeviceDao;
	private BgDeviceDao bgDeviceDao;
	private PulmDeviceDao pulmDeviceDao;
	private BcDeviceDao bcDeviceDao;
	private TempDeviceDao tempDeviceDao;
	
	private boolean isNoDate = false;// 设备是否没有数据标记
	private boolean closeActivity = true;
	// 血氧
	private com.contec.cms50dj_jar.DeviceCommand spoDrive;
	private com.contec.cms50dj_jar.DevicePackManager spoDevice = new com.contec.cms50dj_jar.DevicePackManager();;
	private byte[] comd_spo = new byte[100];
	private static int STYLEMEDIAN_SPO = 0;// 血氧的标识
	private List<Map<String, String>> list;// 数据集
	private AlertnateDisplayAdapter adapter;
	private String spo = "";// 血氧的值
	private ListView listSheet;
	private String pluseRate = "";// 脉率
	private StringBuffer spodata ;// 血氧测量时间
	DeviceData50DJ_Jar spoData;
	// 血糖
	private cn.com.contec.jar.cmssxt.DeviceCommand bgoCommand;
	private cn.com.contec.jar.cmssxt.DevicePackManager bgoPackManager = new cn.com.contec.jar.cmssxt.DevicePackManager();
	private CmssxtDataJar cmssxt;
	private String bgoTime = "";
	private double bgoData = 0;

	// 尿液分析仪
	private com.contec.jar.BC401.DevicePackManager bcPackManager = new com.contec.jar.BC401.DevicePackManager();
	private BCDataAdapter bcAdapter = null;
	private List<BCData> listBcData = new ArrayList<BCData>();
	
	// 肺活量仪
	private cn.com.contec.jar.sp10w.DevicePackManager pulmPackManager = new cn.com.contec.jar.sp10w.DevicePackManager();
	private cn.com.contec.jar.sp10w.DeviceDataJar deviceDataJar;
	private String pulmTime;
	private String FVC;
	private String FEV1;
	private String PEF;
	// 耳温枪
	private cn.com.contec.jar.eartemperture.DeviceCommand eetCommand;
	private cn.com.contec.jar.eartemperture.DevicePackManager eetPackManager = new cn.com.contec.jar.eartemperture.DevicePackManager();
	private String eetData;// 耳温数据
	private String eetOldTime;
	private String eetTime;
	// 连接状态 0-未连接 1-正在校验时间 2-校验时间完成,读取数据
	private int isConnect = 0;

	private BluetoothServerSocket msSocket = null;

	private MAcceptThread mat = null;
	private DBManager dbManager;
	private String name, roomNo, bedNo, elderlyId, usrOrg;
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_monitor);
		LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
		llBack.setVisibility(View.VISIBLE);
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		TextView tvTitle = (TextView)findViewById(R.id.tv_title);
		tvTitle.setText("日常监测");


		context = this;

		groupId = getIntent().getStringExtra("group_id");
		Bundle bendle = (getIntent().getBundleExtra("user_bundle"));
        userId = getIntent().getStringExtra("userId");
		elderlyId = getIntent().getStringExtra("elderlyId");
        name = getIntent().getStringExtra("elderlyName");
		usrOrg = getIntent().getStringExtra("usrOrg");
		roomNo = getIntent().getStringExtra("roomNo");
		bedNo =getIntent().getStringExtra("bedNo");
//        sex=getIntent().getStringExtra("sex");
//		if (bendle != null) {
//			patientInfo = bendle.getParcelable("userInfo");
//		} else {
//			isTest = true;
//		}
		initView();

//		if (bendle != null) {
//			rlUserInfo.setVisibility(View.VISIBLE);
//			llTestInfo.setVisibility(View.GONE);
//		} else {
//			rlUserInfo.setVisibility(View.GONE);
//			llTestInfo.setVisibility(View.VISIBLE);
//		}
		initUI();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		context.registerReceiver(receiver, intentFilter);

		if(checkBluetooth()){
			if (!isTest) {
				speakOut(8);
				tvState.setText("请开始测量");
			} else {
				tvState.setText("请先输入手机号再进行测量");
			}
			mat = new MAcceptThread();
			mat.start();
			if (!isTest) {
				getBloodPressureHestory();
			}
		}else{
			
		}
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	/**
	 * 初始化视图
	 **/
	private void initUI() {
		listSheet = (ListView) this.findViewById(R.id.ll_hestory);
		listSheet.setVerticalScrollBarEnabled(false);
		// addMoreTextView =(TextView)findViewById(R.id.listview_addmorenurse);
		// 初始化数据
		final String[] from = new String[] { "time", "oneValue", "twoValue",
				"threeValue" , "exceptionStatus"};
		final int[] to = new int[] { R.id.tv_time, R.id.tv_h_pressure,
				R.id.tv_l_pressure, R.id.three_value };
		adapter = new AlertnateDisplayAdapter(this, getSimpleData(),
				R.layout.blood_pressure_history_item, from, to) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater
							.from(UserMonitorActivity.this)
							.inflate(R.layout.blood_pressure_history_item, null);
				}
			
				return super.getView(position, convertView, parent);
			}
		};
		listSheet.setAdapter(adapter);

		listSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// 类型决定着跳转去向
			}
		});

	}

	private void initView() {

		initSound();
		tvName = (TextView) context.findViewById(R.id.user_name);
		tvRoomNo = (TextView) context.findViewById(R.id.tv_room_no);
		tvBedNo = (TextView) context.findViewById(R.id.tv_bed_no);

		if(StringUtils.isNotEmpty(roomNo)){
			tvRoomNo.setText(roomNo);
		}else{
			tvRoomNo.setText("");
		}

		if(StringUtils.isNotEmpty(bedNo)){
			tvBedNo.setText(bedNo);
		}else{
			tvBedNo.setText("");
		}
		tvMobielNumber = (TextView) context.findViewById(R.id.user_phone);
		tvIdCard = (TextView) context.findViewById(R.id.user_id_card);

			tvName.setText(name);
//			tvAge.setText(patientInfo.getAge());
//			if(sex.equals("1")){
//				tvGender.setText("男");
//			}else if(sex.equals("2")){
//				tvGender.setText("女");
//			}else{
//				tvGender.setText("未知");
//			}
//			tvMobielNumber.setText(patientInfo.getMobilephone());
//			tvIdCard.setText(StringUtils.dismissIdCard(patientInfo.getCardNo()));

		etPhoneNumber = (EditText) context.findViewById(R.id.et_phone_number);
		rlUserInfo = (RelativeLayout) context.findViewById(R.id.rl_user_info);
		llTestInfo = (RelativeLayout) context.findViewById(R.id.rl_test_info);

		llChangeUserInfo = (LinearLayout) context
				.findViewById(R.id.ll_change_user_info);
		llDeleteUser = (LinearLayout) context.findViewById(R.id.ll_delete_user);
		ivPhoneType = (ImageView) context.findViewById(R.id.iv_phone_type);

		pbBibp = (ProgressBar) context.findViewById(R.id.pb_nibp);
		pbSpo = (ProgressBar) context.findViewById(R.id.pb_spo);
		pbBgo = (ProgressBar) context.findViewById(R.id.pb_bgo);
		pbPulm = (ProgressBar) context.findViewById(R.id.pb_pulm);
		pbBc = (ProgressBar) context.findViewById(R.id.pb_bc);
		pbEet = (ProgressBar) context.findViewById(R.id.pb_eet);

		ivNIBPFinish = (ImageView) context.findViewById(R.id.iv_nibp_finish);
		ivSoPFinish = (ImageView) context.findViewById(R.id.iv_spo_finish);
		ivBGbpFinish = (ImageView) context.findViewById(R.id.iv_bgo_finish);
		ivPULMbpFinish = (ImageView) context.findViewById(R.id.iv_pulm_finish);
		ivBCbpFinish = (ImageView) context.findViewById(R.id.iv_bc_finish);
		ivTEMPFinish = (ImageView) context.findViewById(R.id.iv_eet_finish);

		llNIBPHint = (LinearLayout) context.findViewById(R.id.ll_nibp_info);
		llBGHint = (LinearLayout) context.findViewById(R.id.ll_bg_info_hint);
		llPulmHint = (LinearLayout) context.findViewById(R.id.ll_pulm_hint);

		llNIBPHint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, NibpInfoActivity.class));
			}
		});

//		llChangeUserInfo.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,
//						ChangeUserInfoActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putParcelable("patient_info", patientInfo);
//				intent.putExtra("bundle", bundle);
//				startActivityForResult(intent, 0);
//			}
//		});
//		llDeleteUser.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				DeleteUserHint();
//			}
//		});

		tvState = (TextView) context.findViewById(R.id.tv_state);
		llBloodPressure = (LinearLayout) context
				.findViewById(R.id.ll_blood_pressure);
		llVitealCapacity = (LinearLayout) context
				.findViewById(R.id.ll_viteal_capacity);
		llBgo = (LinearLayout) context.findViewById(R.id.ll_bgo);
		llPulm = (LinearLayout) context.findViewById(R.id.ll_pulm);
		llBc = (LinearLayout) context.findViewById(R.id.ll_bc);
		llEet = (LinearLayout) context.findViewById(R.id.ll_eet);

		llBloodPressure.setOnClickListener(this);
		llVitealCapacity.setOnClickListener(this);
		llBgo.setOnClickListener(this);
		llPulm.setOnClickListener(this);
		llBc.setOnClickListener(this);
		llEet.setOnClickListener(this);

		goTextView = (TextView) context.findViewById(R.id.type_list);// 显示数据的表头type_list
		oneTextView = (TextView) context.findViewById(R.id.one_text);// 表头1
		twoTextView = (TextView) context.findViewById(R.id.two_text);// 表头2
		threeTextView = (TextView) context.findViewById(R.id.three_text);// 表头3
		fourTextView = (TextView) context.findViewById(R.id.four_text);// 表头4
		titileLayout = (LinearLayout) context.findViewById(R.id.vise_lay);// 表头的LinearLayout

		etPhoneNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null) {
					if (s.length() == 11) {
						if (StringUtils.isMobileNO(s.toString())) {
							ivPhoneType.setVisibility(View.VISIBLE);
							strPhoneNumber = etPhoneNumber.getText().toString();
							// 手机号码验证正确
							speakOut(8);
							tvState.setText("请开始测量");
							startFlag = true;
							return;
						}
					}
				}
				ivPhoneType.setVisibility(View.INVISIBLE);
			}
		});
	}

	/**
	 * 加载音频文件
	 */
	private void initSound() {
		soundpool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
		soundpool.load(this, R.raw.bgo_begin_measure, 1);
		soundpool.load(this, R.raw.begin_connect, 1);
		soundpool.load(this, R.raw.device_no_data, 1);
		soundpool.load(this, R.raw.connecting, 1);
		soundpool.load(this, R.raw.updata_error, 1);
		soundpool.load(this, R.raw.bp_finish_spo, 1);
		soundpool.load(this, R.raw.connect_error, 1);
		soundpool.load(this, R.raw.begin_measure, 1);

		soundpool.load(this, R.raw.nibp_measure_finish, 1);
		soundpool.load(this, R.raw.spo_measure_finish, 1);
		soundpool.load(this, R.raw.bg_measure_finish, 1);
		soundpool.load(this, R.raw.pulm_measure_finish, 1);
		soundpool.load(this, R.raw.bc_measure_finish, 1);

		soundpool.load(this, R.raw.find_nibp, 1);
		soundpool.load(this, R.raw.find_spo, 1);
		soundpool.load(this, R.raw.find_bg, 1);
		soundpool.load(this, R.raw.find_pulm, 1);
		soundpool.load(this, R.raw.find_bc, 1);
		soundpool.load(this, R.raw.find_eet, 1);
		soundpool.load(this, R.raw.eet_measure_finish, 1);

		soundpool.load(this, R.raw.device_error, 1);//21
		soundpool.load(this, R.raw.input_phone_number, 1);//22
		soundpool.load(this, R.raw.phone_type_error, 1);//23
	}

	@Override
	public void onClick(View view) {
		// flag = true;
		if (!checkBluetooth()) {
			return;
		}
		strPhoneNumber = etPhoneNumber.getText().toString();
		if (isTest) {
			if (StringUtils.isNotEmpty(strPhoneNumber)) {
				if (!StringUtils.isMobileNO(strPhoneNumber)) {
					speakOut(23);
					Toast.makeText(context, "手机号格式不正确!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			} else {
				speakOut(22);
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		clearHint();
		isNoDate = false;
		isUpdate = false;
		switch (view.getId()) {
		case R.id.ll_blood_pressure:
			// tvState.setText("正在查找血压计...");
			// if (deviceName.equals(bloodPressureName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbBibp, llBloodPressure);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			clearProgressState(null, null);
			deviceName = bloodPressureName;
			// isConnect = 1;
			// }
			// bloodPressureUpdataState = false;
			// speakOut(14);
			if (!isTest) {
				getBloodPressureHestory();
			}
			llNIBPHint.setVisibility(View.VISIBLE);
			// initGetBloodPressure();
			break;
		case R.id.ll_viteal_capacity:
			// tvState.setText("正在查找血氧仪...");
			// if (deviceName.equals(vitealCapacityName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbSpo, llVitealCapacity);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			deviceName = vitealCapacityName;
			// isConnect = 1;
			clearProgressState(null, null);
			// }
			if (!isTest) {
				getBloodPressureHestory();
			}
			// speakOut(15);
			// initGetVitealCapacity();
			break;
		case R.id.ll_bgo:
			// tvState.setText("正在查找血糖仪...");
			// if (deviceName.equals(bgoName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbBgo, llBgo);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			deviceName = bgoName;
			// isConnect = 1;
			clearProgressState(null, null);
			// }
			// speakOut(16);
			if (!isTest) {
				getBloodPressureHestory();
			}
			llBGHint.setVisibility(View.VISIBLE);
			// initBgo();
			// speakOut(8);
			break;
		case R.id.ll_pulm:
			// tvState.setText("正在查找肺活量计...");
			// if (deviceName.equals(pulmName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbPulm, llPulm);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			deviceName = pulmName;
			// isConnect = 1;
			clearProgressState(null, null);

			// }
			// speakOut(17);
			// pulmUpdataState = false;
			llPulmHint.setVisibility(View.VISIBLE);
			if (!isTest) {
				getBloodPressureHestory();
			}
			llPulmHint.setVisibility(View.VISIBLE);
			// initGetPulm();
			break;
		case R.id.ll_bc:
			// tvState.setText("正在查找尿液分析仪...");
			// if (deviceName.equals(bcName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbBc, llBc);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			deviceName = bcName;
			// isConnect = 1;
			clearProgressState(null, null);
			// }
			if (!isTest) {
				getBloodPressureHestory();
			}
			// speakOut(18);
			// initGetBC();
			break;
		case R.id.ll_eet:
			// tvState.setText("正在查找耳温枪...");
			// if (deviceName.equals(eetName)) {
			// if (isConnect == 0) {
			// isConnect = 1;
			// clearProgressState(pbEet, llEet);
			// } else if (isConnect == 1) {
			// speakOut(4);
			// return;
			// } else if (isConnect == 2) {
			// return;
			// }
			// } else {
			deviceName = eetName;
			// isConnect = 1;
			clearProgressState(null, null);
			// }
			if (!isTest) {
				getBloodPressureHestory();
			}
			// speakOut(19);
			// initGetEet();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入时直接读取血压数据
	 */
	private void initGetBloodPressure() {
		if (!checkBluetooth()) {
			return;
		} else {
			bloodPressureUpdataState = false;
			deviceName = bloodPressureName;
			link();
		}
	}

	/**
	 * 开始连接血氧设备
	 */
	private void initGetVitealCapacity() {
		if (!checkBluetooth()) {
			return;
		} else {
			vitealCapacityUpdataState = false;
			deviceName = vitealCapacityName;
			link();
		}
	}

	/**
	 * 开始连接血糖设备
	 */
	private void initBgo() {
		if (!checkBluetooth()) {
			return;
		} else {
			bgoUpdataState = false;
			deviceName = bgoName;
			link();
		}
	}

	/**
	 * 开始连接肺活量设备
	 */
	private void initGetPulm() {
		if (!checkBluetooth()) {
			return;
		} else {
			pulmUpdataState = false;
			deviceName = pulmName;
			link();
		}
	}

	/**
	 * 开始连接尿液分析仪
	 */
	private void initGetBC() {
		if (!checkBluetooth()) {
			return;
		} else {
			bcUpdataState = false;
			deviceName = bcName;
			link();
		}
	}

	/**
	 * 开始连接耳温枪
	 */
	private void initGetEet() {
		if (!checkBluetooth()) {
			return;
		} else {
			eetUpdataState = false;
			deviceName = eetName;
			link();
		}
	}

	/**
	 * 用户进入后开启后台监听，退出页面时销毁线程
	 * 
	 * 
	 * 
	 * @author lym
	 */
	public class MAcceptThread extends Thread {

		public MAcceptThread() {
			BluetoothServerSocket tmpbss = null;
			if (!checkBluetooth()) {
				// 完善 提醒 蓝牙不支持
				return;
			}
			try {
				tmpbss = bluetoothManager.getBluetoothAdapter()
						.listenUsingRfcommWithServiceRecord(
								bluetoothManager.getBluetoothAdapter()
										.getAddress(),
								UUID.fromString(StringUtils.MY_UUID));
				Log.e("服务端蓝牙 地址", "MAC ADDRESS:"
						+ bluetoothManager.getBluetoothAdapter().getAddress());
			} catch (IOException e) {
				Log.e("服务端", "mmServerSocket异常:: " + e.getMessage());

			}catch(Exception e){
				Log.e("服务端", "mmServerSocket异常:: " + e.getMessage());
			}
			msSocket = tmpbss;
		}

		public void run() {
			Log.e("服务端蓝牙监听", "线程开启");
			while (true) { 
				if (isTest) {
					if (!startFlag) {
						// Log.e("服务端蓝牙监听", "测试用户手机号没输入完");
						continue;
					}
				}
				try {
					if(msSocket == null){
						return;
					}
					socket = msSocket.accept();
					Log.e(TAG, "服务端建立蓝牙链接");
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("服务端建立蓝牙链接", "socket异常:: " + e.getMessage());
					// 完善 异常时提醒
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					if (null != socket.getRemoteDevice().getName()
							&& socket.getRemoteDevice().getName().length() > 0) {

						Log.e("服务端建立蓝牙链接", socket.getRemoteDevice().getName()
								+ "  connected success");
						deviceName = socket.getRemoteDevice().getName();
//						boolean isRegisteDevice = false;
//						List<DeviceDao> deviceList = getDBManager().getDeviceList();
//						for(DeviceDao device : deviceList){
//							if(device.getDeviceName().equals(deviceName)){
//								isRegisteDevice = true;
//								break;
//							}
//						}
//						if(!isRegisteDevice){
//							return;
//						}

						try {
							if (deviceName.contains(bloodPressureName)) {// 血压
								deviceName = bloodPressureName;
							} else if (deviceName.contains(vitealCapacityName)) {// 血氧
								deviceName = vitealCapacityName;
							} else if (deviceName.contains(bgoName)) {// 血糖
								deviceName = bgoName;
							} else if (deviceName.contains(pulmName)) {// 肺活量
								deviceName = pulmName;
							} else if (deviceName.contains(bcName)) {// 尿液
								deviceName = bcName;
							} else if (deviceName.contains(eetName)) {// 体温-耳温
								deviceName = eetName;
							}
							mHandler.sendEmptyMessage(82);
							flag = true;
							isConnect = 2;
							chooseDeciveType(deviceName);

						} catch (Exception e) {
							if (socket != null) {
								try {
									socket.close();
									socket = null;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							flag = true;
							e.printStackTrace();
						}
					}
				}
			}
			Log.e("服务端蓝牙监听", "线程结束");
		}

		public void cancel() {
			Log.e("服务端蓝牙监听", "线程关闭");
			try {
				if(msSocket != null){
					msSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public class AcceptThread extends Thread {
		public void run() {
			Log.e("蓝牙监听线程", "开启");
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket mmServerSocket = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				mmServerSocket = bluetoothManager.getBluetoothAdapter()
						.listenUsingRfcommWithServiceRecord(
								bluetoothManager.getBluetoothAdapter()
										.getAddress(),
								UUID.fromString(StringUtils.MY_UUID));
				Log.e("MAC ADDRESS", bluetoothManager.getBluetoothAdapter()
						.getAddress());
			} catch (IOException e) {
				Log.e("服务端建立蓝牙链接", "mmServerSocket异常:: " + e.getMessage());
			}

			// BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (!isUpdate) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e("服务端建立蓝牙链接", "socket异常:: " + e.getMessage());
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					if (null != socket.getRemoteDevice().getName()
							&& socket.getRemoteDevice().getName().length() > 0) {
						// Log.e(TAG, socket.getRemoteDevice().getName());
						// tvState.setText("连接设备成功!");
						Log.e("服务端建立蓝牙链接", socket.getRemoteDevice().getName());
						Log.e("服务端建立蓝牙链接", "success");
						try {
							// socket.connect();
							Log.e(TAG, "connectd");
							flag = true;
							chooseDeciveType(socket.getRemoteDevice().getName());

						} catch (Exception e) {
							// mHandler.sendEmptyMessage(37);
							if (socket != null) {
								try {
									socket.close();
									socket = null;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							flag = true;
							e.printStackTrace();
						}
					}
					try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			isUpdate = false;
			Log.e("蓝牙监听线程", "关闭");
		}
	}

	/**
	 * 前提条件是需要先添加设备，没有配对时先进行配对再连接 暂时给血糖和尿液分析仪使用
	 */
	private void link() {
		Set<BluetoothDevice> devices = (bluetoothManager.getBluetoothAdapter())
				.getBondedDevices();
		String name = null;
		Map<String, BluetoothDevice> dMap = new HashMap<String, BluetoothDevice>();
		for (BluetoothDevice device : devices) {
			dMap.put(device.getName(), device);
			if (device.getName().contains(deviceName)) {

				name = device.getName();
			}
			Log.e("device.getName()-----", device.getName());
		}
		BluetoothDevice temp = dMap.get(name);
		// 如果设备已经配对，直接提示开始测量，不再进行时间校验
		if (null != temp) {
			Log.e("已配对设备的dirvice", deviceName + "--可进行测量");
			chooseDecive(deviceName);
		} else {
			Log.e("未配对设备的dirvice", deviceName + "--linkDevice");
			linkDevice();
		}
	}

	private void checkAndLink() {
		Set<BluetoothDevice> devices = (bluetoothManager.getBluetoothAdapter())
				.getBondedDevices();
		String name = null;
		Map<String, BluetoothDevice> dMap = new HashMap<String, BluetoothDevice>();
		for (BluetoothDevice device : devices) {
			dMap.put(device.getName(), device);
			if (device.getName().contains(deviceName)) {

				name = device.getName();
			}
			Log.e("device.getName()222", device.getName());
		}
		BluetoothDevice temp = dMap.get(name);
		if (null != temp) {
			Log.e("设备的dirvice", deviceName + "----connect");
			connect(temp);
		} else {
			Log.e("设备的dirvice", deviceName + "----linkDevice");
			linkDevice();
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e(TAG, "msg.what = " + msg.what);
			switch (msg.what) {
			case 0:// 准备校验时间握手
				Log.e("response", "准备校验时间握手成功!");
				getCorrectTimeNotice();
				break;
			case 1:
				Log.e("response", "准备校验时间成功!");
				getCorrectTime();
				break;
			case 2:
				Log.e("response", "校验时间成功!");
				handShakeType = 1;
				if (isConnect == 1) {
					// 删除数据
					handShakeType = 2;
					handShake();
					return;
				} else if (isConnect == 2) {
					handShake();
				}
				break;
			case 3:
				Log.e("response", "获取数据握手成功!");
				getBloodPressure();
				break;
			case 4:
				Log.e("response", "获取数据成功!");
				if (null == deviceData) {
					Log.e("deviceData", "deviceData = null");
					break;
				}
				if (deviceData.size() <= 0) {
					isNoDate = true;
					Log.e("deviceData", "deviceData.deviceData = null");
					// hintNoData();
					// bloodPressureUpdataState = false;
					flag = false;
					handShakeType = 2;
					handShake();
					break;
				}
				isNoDate = false;
				resolveData(deviceData.get(deviceData.size() - 1));
				break;
			case 5:
				Log.e("response", "删除数据握手成功!");
				deleteDate();
				break;
			case 6:
				if (isConnect == 2) {
					if (!isNoDate) {
						Log.e("response", "血压删除数据成功!");
						tvState.setText("血压测量完成!");
						speakOut(9);
						// updateFinish();
						// 获得血压数据线程关闭
						bloodPressureUpdataState = true;
						isConnect = 0;
						flag = false;

						clearProgressState(null, null);

						llNIBPHint.setVisibility(View.VISIBLE);
						getBloodPressureHestory();
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
					}
				} else if (isConnect == 1) {
					speakOut(8);
					tvState.setText("血压计已连接,请开始测量");
					isConnect = 2;
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// getBloodPressureDataThread = new
					// GetBloodPressureDataThread();
					// getBloodPressureDataThread.start();
					acceptThread = new AcceptThread();
					acceptThread.start();

				}
				flag = false;
				handShakeType = 0;
				break;
			case 7:
				Log.e("response", "删除数据失败!");
				speakOut(5);
				flag = false;
				break;
			case 10:// 发送到服务器
				upDataBloodPressure();
				break;
			case 11:
				try {
					BaseResponseEntity baseResponseEntity = (BaseResponseEntity) msg.obj;
					if (baseResponseEntity.isSuccess()) {
						handShakeType = 2;
						handShake();
						inputDate.delete(0, inputDate.length());
					} else {
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
						setNibpToDb();
//						handShake();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 12:
				try {
					Log.e("用户", "当前设备名称:" + deviceName);
//					JSONObject jsonObject = (JSONObject) msg.obj;
					BaseResponseEntity base = (BaseResponseEntity)msg.obj;
//					 Log.e("查询的数据", jsonObject + "数据哇");
					if (base.isSuccess()) {
						Log.e(TAG, "success");
						goTextView.setVisibility(View.VISIBLE);
						titileLayout.setVisibility(View.VISIBLE);
						list.clear();
						listBcData.clear();
						oneTextView.setVisibility(View.VISIBLE);
						twoTextView.setVisibility(View.VISIBLE);
						threeTextView.setVisibility(View.VISIBLE);
						fourTextView.setVisibility(View.VISIBLE);
//						JSONArray jsonObjs = jsonObject.getJSONArray("ITEMS");
						// titileLayout
						if (deviceName.contains("NIBP")) {
							NibpHistroyResponse nibpHisRes = (NibpHistroyResponse)msg.obj;
							goTextView.setText("血压数据");
							oneTextView.setText("检测时间");
							twoTextView.setText("收缩压(mmHg)");
							threeTextView.setText("舒张压(mmHg)");
							fourTextView.setText("脉率(次/分)");
							for (int i = 0; i < nibpHisRes.getITEMS().size(); i++) {
								Map<String, String> map = new HashMap<String, String>();
//								JSONObject jsonObj = (JSONObject) jsonObjs
//										.opt(i);
								NibpEntity item = nibpHisRes.getITEMS().get(i);
								map.put("time", item.getInputDate());// "time",
								map.put("oneValue", item.getSbpVal());
								map.put("twoValue", item.getDbpVal());
								map.put("threeValue", item.getPrVal());
								map.put("exceptionStatus", item.getPrVal());
								list.add(map);
//								if (isTest) {
//									break;
//								}
							}
							listSheet.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} else if (deviceName.contains("SpO")) {// 血氧
							goTextView.setText("血氧数据");
							oneTextView.setText("检测时间");
							twoTextView.setText("血氧饱和度(%SpO2)");
							threeTextView.setText("脉率(PRbpm)");
							fourTextView.setVisibility(View.GONE);
//							for (int i = 0; i < jsonObjs.length(); i++) {
//								Map<String, String> map = new HashMap<String, String>();
//
//								JSONObject jsonObj = (JSONObject) jsonObjs
//										.opt(i);
//								map.put("time", jsonObj.getString("inputdate")
//										+ " " + jsonObj.getString("inputtime"));// "time",
//																				// "oneValue","twoValue"};
//								map.put("oneValue", jsonObj.getString("spo"));
//								map.put("twoValue",
//										jsonObj.getString("pulseRate"));
//								map.put("threeValue", "");
//								list.add(map);
//								if (isTest) {
//									break;
//								}
//							}
							listSheet.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} else if (deviceName.contains("BG")) {// 血糖
							goTextView.setText("血糖数据");
							oneTextView.setText("检测时间");
							twoTextView.setText("血糖(mmoL/L)");
							threeTextView.setVisibility(View.GONE);
							fourTextView.setVisibility(View.GONE);
							// Log.e("血糖用户",
							// "isTest:"+isTest+",血糖数据jsonObjs.length()："+jsonObjs.length());
//							for (int i = 0; i < jsonObjs.length(); i++) {
//								Map<String, String> map = new HashMap<String, String>();
//
//								JSONObject jsonObj = (JSONObject) jsonObjs
//										.opt(i);
//								Log.e("血糖用户", "血糖数据Value：" + jsonObj.toString());
//
//								map.put("time", jsonObj.getString("inputdate")
//										+ " " + jsonObj.getString("inputtime"));// "time",
//
//								// "oneValue","twoValue"};
//								map.put("oneValue", jsonObj.getString("bgval"));
//								map.put("twoValue", "");
//								map.put("threeValue", "");
//								list.add(map);
//								if (isTest) {
//									Log.e("血糖测试用户",
//											"血糖数据："
//													+ jsonObj
//															.getString("bgval"));
//									break;
//								}
//							}
							listSheet.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} else if (deviceName.contains("PULM")) {// 肺活量
							goTextView.setText("肺活量数据");
							oneTextView.setText("检测时间");
							twoTextView.setText("FVC");
							threeTextView.setText("FEV1");
							fourTextView.setText("PEF");
							PulmHistroyResponse pulmHisRes =  (PulmHistroyResponse)msg.obj;
							
							for (int i = 0; i < pulmHisRes.getITEMS().size(); i++) {
								Map<String, String> map = new HashMap<String, String>();

								PulmEntity item = pulmHisRes.getITEMS().get(i);
								map.put("time", item.getInputDate());// "time",
																				// "oneValue","twoValue"};
								map.put("oneValue", item.getFvcVal());
								map.put("twoValue", item.getFevVal());
								map.put("threeValue",item.getPefVal());
								list.add(map);
							}
							listSheet.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} else if (deviceName.contains("BC")) {
							goTextView.setText("尿液分析数据");
							titileLayout.setVisibility(View.GONE);
//							for (int i = 0; i < jsonObjs.length(); i++) {
//								// Map<String, String> map = new HashMap<String,
//								// String>();
//								JSONObject jsonObj = (JSONObject) jsonObjs
//										.opt(i);
//								BCData badata = new BCData();
//								badata.setBilVal(jsonObj.getString("bilVal"));
//								badata.setBldVal(jsonObj.getString("bldVal"));
//								badata.setGluVal(jsonObj.getString("gluVal"));
//								badata.setKetVal(jsonObj.getString("ketVal"));
//								badata.setLeuVal(jsonObj.getString("leuVal"));
//								badata.setNitVal(jsonObj.getString("nitVal"));
//								badata.setPhVal(jsonObj.getString("phVal"));
//								badata.setProVal(jsonObj.getString("proVal"));
//								badata.setSgVal(jsonObj.getString("sgVal"));
//								badata.setUroVal(jsonObj.getString("uroVal"));
//								badata.setVcVal(jsonObj.getString("vcVal"));
//								badata.setTime(jsonObj.getString("inputtime"));
//								badata.setDate(jsonObj.getString("inputdate"));
//								listBcData.add(badata);
//								if (isTest) {
//									break;
//								}
//							}

							if (bcAdapter == null) {
								Log.e("bcAdapter", "bcAdapter == null");
								bcAdapter = new BCDataAdapter(context,
										listBcData);
								listSheet.setAdapter(bcAdapter);
								bcAdapter.notifyDataSetChanged();
							} else {
								Log.e("bcAdapter", "bcAdapter != null");
								listSheet.setAdapter(bcAdapter);
								bcAdapter.setData(listBcData);
								bcAdapter.notifyDataSetChanged();
							}
						} else if (deviceName.contains(eetName)) {
							goTextView.setText("耳温数据");
							oneTextView.setText("检测时间");
							twoTextView.setText("耳温(℃)");
							threeTextView.setVisibility(View.GONE);
							fourTextView.setVisibility(View.GONE);
							TempHistroyResponse tempHisRes = (TempHistroyResponse)msg.obj;
							for (int i = 0; i < tempHisRes.getITEMS().size(); i++) {
								Map<String, String> map = new HashMap<String, String>();

								TempEntity item = tempHisRes.getITEMS().get(i);
								map.put("time", item.getInpuDate());
								map.put("oneValue",item.getTemperature());
								list.add(map);
//								Log.e("用户", "体温Value：" + jsonObj.toString());
								if (isTest) {
//									Log.e("测试用户", "体温Value：" + jsonObj.toString());
									break;
								}
							}
							listSheet.setAdapter(adapter);
							adapter.notifyDataSetChanged();

						}
						// for(int i=0; i<jsonObjs.length(); i++){
						// JSONObject jsonObj = (JSONObject)jsonObjs.opt(i);
						// map.put("time",
						// jsonObj.getString("inputdate")+"-"+jsonObj.getString("inputtime"));//"time",
						// "oneValue","twoValue"};
						// map.put("oneValue", jsonObj.getString("sbpval"));
						// map.put("twoValue", jsonObj.getString("dbpval"));
						// list.add(map);
						// }

					} else {
						Log.e(TAG, base.getMessage());
//						if ("NOVALUE".equals(jsonObject.getString("message"))) {
//							Log.e(TAG, "NOVALUE");
//							list.clear();
//							adapter.notifyDataSetChanged();
//							goTextView.setVisibility(View.GONE);
//							titileLayout.setVisibility(View.GONE);
//							Toast.makeText(context, "没有历史数据", Toast.LENGTH_LONG)
//									.show();
//						}
//						if ("EXCEPTION".equals(jsonObject.getString("message"))) {
//							list.clear();
//							adapter.notifyDataSetChanged();
//							goTextView.setVisibility(View.GONE);
//							titileLayout.setVisibility(View.GONE);
//							Toast.makeText(context, "没有历史数据", Toast.LENGTH_LONG)
//									.show();
//						}
					}
				} catch (Exception e) {
					Log.e(TAG, "Exception");
					e.printStackTrace();
				}
				break;
			case 13:// 发起查询数据请求

				break;
			// 血氧设备
			case 20:// 血氧 脉率 没有新数据
				isNoDate = true;
				askSpoDaySuccess();// 发送成功的指令
				break;
			case 21:
				Toast.makeText(context, "请求失败", Toast.LENGTH_LONG).show();
				break;
			case 22:// 上传数据
				upDataSpo();
				break;
			case 23:
				askSpo();
				break;
			case 24:
				askSpoSuccess();
				break;
			case 25:
				try {
					JSONObject jsonObject = (JSONObject) msg.obj;
					if (jsonObject.getBoolean("success")) {
						vitealCapacityUpdataState = true;
						isConnect = 0;
						speakOut(10);
						tvState.setText("血氧测量完成!");
						askSpoDaySuccess();// 发送成功的指令
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getBloodPressureHestory();
						clearProgressState(null, null);
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
					} else {
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
//						speakOut(5);
						setSpoDataToDb();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 26:// 发送校验时间的命令
				askSpoDate();
				break;
			case 27:// 发送校验计步器信息的命令 时间校验成功
				if (isConnect == 1) {
					speakOut(1);
					tvState.setText("血氧仪已连接,请开始测量");
					// askSpoDaySuccess();//发送成功的指令
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					isConnect = 2;
					// getVitealCapacityUpDataThread = new
					// GetVitealCapacityUpDataThread();
					// getVitealCapacityUpDataThread.start();
					acceptThread = new AcceptThread();
					acceptThread.start();
					return;
				} else if (isConnect == 2) {
					askSpoByke();
				}
				break;
			case 28:// 发送校验信息失败
				Toast.makeText(context, "检验信息失败!", Toast.LENGTH_LONG).show();
				break;
			case 29:// 上传失败
				Toast.makeText(context, "上传信息失败!", Toast.LENGTH_LONG).show();
				break;

			// 血糖设备
			case 30:// 时间设置成功 开始获取数据
				Log.e("BGO", "开始获取数据");
				if (isConnect == 1) {
					deleteBgoData();
					return;
				} else if (isConnect == 2) {
					getBgoData();
				}
				break;
			case 31:// 时间设置失败
				Log.e(TAG, "时间设置失败!");
				break;
			case 32:// 获取数据成功 解析 上传
				getBogDataSuccess();
				break;
			case 33:// 处理返回结果
				try {
					JSONObject jsonObject = (JSONObject) msg.obj;
					if (jsonObject.getBoolean("success")) {
						Log.e("update_state", "success");
						isNoDate = false;
						deleteBgoData();
					} else {
						Log.e("update_state", "error");
						setBGDataToDb();
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 34:// 删除数据成功
				Log.e("delete_state", "success");
				if (isConnect == 1) {
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					speakOut(8);
					tvState.setText("血糖仪已连接,请开始测量");
					// getBgoUpDataThread = new GetGboUpDataThread();
					// getBgoUpDataThread.start();

					isConnect = 2;
					acceptThread = new AcceptThread();
					acceptThread.start();

				} else if (isConnect == 2) {
					if (!isNoDate) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tvState.setText("血糖测量完成!");
						bgoUpdataState = true;
						isConnect = 0;
						clearProgressState(null, null);
						llBGHint.setVisibility(View.VISIBLE);
						getBloodPressureHestory();
						speakOut(11);
						flag = false;
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
					}
				}

				break;
			case 35:// 删除数据失败
				Log.e("delete_state", "error");
				flag = false;
				break;
			case 36:// 无数据
				// tvState.setText("设备没有数据,请先测量!");
				// speakOut(3);
				// flag = false;
				// isNoDate = true;
				// deleteBgoData();
				if (isConnect == 1) {
					isConnect = 2;
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					speakOut(8);
					tvState.setText("血糖仪已连接,请开始测量");
					// getBgoUpDataThread = new GetGboUpDataThread();
					// getBgoUpDataThread.start();
					acceptThread = new AcceptThread();
					acceptThread.start();
				}else{
					 tvState.setText("设备没有数据,请重试!");
					 clearProgressState(null, null);
				}
				break;
			case 37:// 连接失败
				// tvState.setText("设备连接失败,请重试!");
				break;
			case 38:
				tvState.setText("测量数据异常,请联系客服!");
				speakOut(21);
				if (null != socket) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				bgoUpdataState = true;
				isNoDate = false;
				isConnect = 0;
				clearProgressState(null, null);
				flag = false;
				isUpdate = true;
				break;
			// 肺活量计
			case 40:// 设置日期成功
				setPulmTime();
				break;
			case 41:// 设置日期失败
				Log.e("set_date", "error");
				break;
			case 42:// 设置时间成功
				if (isConnect == 1) {
					deletePulmData();
					speakOut(8);
					tvState.setText("设备已连接,请开始测量");
					return;
				} else if (isConnect == 2) {
					getPulmData();
				}
				break;
			case 43:// 设置时间失败
				Log.e("set_time", "error");
				break;
			case 44:// 获取数据成功 开始上传到服务器
				getPulmDataSuccess();
				break;
			case 45:// 获取数据失败
				Log.e("get_data", "error");
				break;
			case 46:// 数据上传服务器成功
				Log.e("46", "46");
				try {
					if (msg.obj == null) {
						isNoDate = true;
						deletePulmData();
						return;
					}
					BaseResponseEntity baseResponseEntity = (BaseResponseEntity) msg.obj;
					if (baseResponseEntity.isSuccess()) {
						Log.e("update_state", "success");
						isNoDate = false;
						deletePulmData();
					} else {
						Log.e("update_state", "error");
						setPulmDataToDb();
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 47:// 删除成功
				Log.e("删除肺活量数据", "成功");
				if (isConnect == 1) {

					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						socket = null;
					}
					// getPulmUpDataThread = new GetPulmUpDataThread();
					// getPulmUpDataThread.start();
					isConnect = 2;
					acceptThread = new AcceptThread();
					acceptThread.start();
				} else if (isConnect == 2) {
					if (!isNoDate) {
						tvState.setText("肺活量测量完成!");
						getBloodPressureHestory();
						clearProgressState(null, null);
						llPulmHint.setVisibility(View.VISIBLE);
						speakOut(12);
						pulmUpdataState = true;
						isConnect = 0;
						flag = false;
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
					}
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						socket = null;
					}
				}
				break;
			case 48:// 删除失败
				Log.e("删除肺活量数据", "失败");
				flag = false;
				break;
			// 尿液分析仪
			case 50:// 校验时间成功
				if (isConnect == 1) {
					deleteBcData();
					return;
				} else if (isConnect == 2) {
					getBcData();
				}
				break;
			case 51:// 得到数据
				getBcDataSuccess();
				break;
			case 52:// 没有数据
				// tvState.setText("设备没有数据,请先测量!");
				// speakOut(3);
				isNoDate = true;
				deleteBcData();
				break;
			case 53:
				try {
					JSONObject jsonObject = (JSONObject) msg.obj;
					if (jsonObject.getBoolean("success")) {
						Log.e("update_state", "success");
						isNoDate = false;
						deleteBcData();
					} else {
						Log.e("update_state", "error");
						setBcDataToDb();
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 54:
				if (isConnect == 1) {
					speakOut(8);
					tvState.setText("尿液分析仪已连接,请开始测量");
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// getBCUpDataThread = new GetBCUpDataThread();
					// getBCUpDataThread.start();
					isConnect = 2;
					acceptThread = new AcceptThread();
					acceptThread.start();
				} else if (isConnect == 2) {
					Log.e("delete_bc_state", "success");
					if (!isNoDate) {
						tvState.setText("尿液测量完成!");
						isConnect = 0;
						getBloodPressureHestory();
						clearProgressState(null, null);
						speakOut(13);
						bcUpdataState = true;
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
						if (null != socket) {
							try {
								socket.close();
								socket = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			// 删除用户结果
			case 60:
				Toast.makeText(context, "删除成功!", Toast.LENGTH_LONG).show();
				setResult(1);
				finish();
				break;
			case 61:
				Toast.makeText(context, "删除失败!", Toast.LENGTH_LONG).show();
				break;
			// 耳温枪
			case 70:// 连接验证成功
				eetVerifyTime();
				break;
			case 71:// 校验时间成功
				if (isConnect == 1) {
					eetGetDataNum();
					return;
				} else if (isConnect == 2) {
					eetGetDataNum();
				}
				break;
			case 72:// 校验时间失败
				break;
			case 73:// 读取数据成功 上传数据
				if (isConnect == 1) {
					if (eetPackManager.m_DeviceDatas != null) {
						if (eetPackManager.m_DeviceDatas.size() > 0) {
							Log.e("isConnect == 1  size ", "size() = "
									+ eetPackManager.m_DeviceDatas.size());
							int position = eetPackManager.m_DeviceDatas.size() - 1;
							eetOldTime = eetPackManager.m_DeviceDatas
									.get(position).m_saveDate;
							eetPackManager.m_DeviceDatas.clear();
							eetDeleteData();
						}
					}
				} else if (isConnect == 2) {
					if (eetPackManager.m_DeviceDatas.size() > 0) {
						int position = eetPackManager.m_DeviceDatas.size() - 1;
						Log.e("isConnect == 2  size ", "size() = "
								+ eetPackManager.m_DeviceDatas.size());
						Log.e("ett_time",
								eetPackManager.m_DeviceDatas.get(position).m_saveDate);
						Log.e("ett_data",
								eetPackManager.m_DeviceDatas.get(position).m_data
										+ "----");
						eetData = String.valueOf(eetPackManager.m_DeviceDatas
								.get(position).m_data);
						eetTime = eetPackManager.m_DeviceDatas.get(position).m_saveDate;
						eetPackManager.m_DeviceDatas.clear();
						if (!eetTime.equals(eetOldTime)) {
							upDateEetData();
						}
					} else {
						Log.e("ett_数量", "数量小于0");
					}
				}
				break;
			case 74:// 无数据
				isNoDate = true;
				eetDeleteData();
				break;
			case 75:// 数据上传成功
				try {
					if (msg.obj == null) {
						isNoDate = true;
//						eetDeleteData();
						return;
					}
					BaseResponseEntity baseResponseEntity = (BaseResponseEntity) msg.obj;
					if (baseResponseEntity.isSuccess()) {
						Log.e("update_state", "success");
						isNoDate = false;
						eetDeleteData();
					} else {
						Log.e("update_state", "error - " + baseResponseEntity.getMessage());
						setTempDataToDb();
//						Toast.makeText(context, "数据上传失败,请重试!",
//								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 76:// 删除数据成功
				if (isConnect == 1) {
					speakOut(8);
					tvState.setText("耳温枪已连接,请开始测量");
					isConnect = 2;
					if (null != socket) {
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// getEetUpDataThread = new GetEetUpDataThread();
					// getEetUpDataThread.start();
					acceptThread = new AcceptThread();
					acceptThread.start();
				} else if (isConnect == 2) {
					Log.e("delete_eet_state", "success");
					if (!isNoDate) {
						tvState.setText("耳温测量完成!");
						isConnect = 0;
						eetOldTime = eetTime;
						getBloodPressureHestory();
						clearProgressState(null, null);
						speakOut(20);
						eetUpdataState = true;
						isUpdate = true;
						updateFinish();
						setUpdateSuccess();
						try {
							socket.close();
							socket = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case 77:// 删除数据失败
				break;
			case 78:// 读取到一条数据
				if (isConnect == 1) {
					if (eetPackManager.m_DeviceDatas != null) {
						if (eetPackManager.m_DeviceDatas.size() > 0) {
							Log.e("isConnect == 1  size ", "size() = "
									+ eetPackManager.m_DeviceDatas.size());
							int position = eetPackManager.m_DeviceDatas.size() - 1;
							eetOldTime = eetPackManager.m_DeviceDatas
									.get(position).m_saveDate;
							eetPackManager.m_DeviceDatas.clear();
							eetDeleteData();
						}
					}
				} else if (isConnect == 2) {
					if (eetPackManager.m_DeviceDatas.size() > 0) {
						int position = eetPackManager.m_DeviceDatas.size() - 1;
						Log.e("isConnect == 2  size ", "size() = "
								+ eetPackManager.m_DeviceDatas.size());
						Log.e("ett_time",
								eetPackManager.m_DeviceDatas.get(0).m_saveDate);
						Log.e("ett_data",
								eetPackManager.m_DeviceDatas.get(0).m_data
										+ "----");
						eetData = String.valueOf(eetPackManager.m_DeviceDatas
								.get(0).m_data);
						eetTime = eetPackManager.m_DeviceDatas.get(position).m_saveDate;
						eetPackManager.m_DeviceDatas.clear();
						if (!eetTime.equals(eetOldTime)) {
							upDateEetData();
						}
					} else {
						Log.e("ett_数量", "数量小于0");
					}
				}
				break;
			case 79:
				eetGetData();
				break;
			// 血氧数据
			case 80:// 上传计步器信息成功
				askSpoDaySuccess();
				break;
			case 81:
				// 血氧完成 测试 肺活量
				vitealCapacityUpdataState = true;
				break;
			case 82: // 创建进度条

				if (deviceName.contains(bloodPressureName)) {// 血压
					clearProgressState(pbBibp, llBloodPressure);
				} else if (deviceName.contains(vitealCapacityName)) {// 血氧
					clearProgressState(pbSpo, llVitealCapacity);
				} else if (deviceName.contains(bgoName)) {// 血糖
					clearProgressState(pbBgo, llBgo);
				} else if (deviceName.contains(pulmName)) {// 肺活量
					clearProgressState(pbPulm, llPulm);
				} else if (deviceName.contains(bcName)) {// 尿液
					clearProgressState(pbBc, llBc);
				} else if (deviceName.contains(eetName)) {// 体温-耳温
					clearProgressState(pbEet, llEet);
				}

				break;
			case 99:
				checkAndLink();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 连接蓝牙设备
	 */
	private void linkDevice() {
		// tvState.setText("设备连接中...");
		bluetoothManager.getBluetoothAdapter().startDiscovery();
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

	/**
	 * 蓝牙设备监听
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e("action", action);
			int connectState;
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.e("device.getName()", "--" + device.getName() + "--");
				if (StringUtils.isNotEmpty(device.getName())) {
					if (device.getName().contains(deviceName)) {
						bluetoothManager.getBluetoothAdapter()
								.cancelDiscovery();
						connectState = device.getBondState();
						Log.e("connectState", String.valueOf(connectState));
						switch (connectState) {
						case BluetoothDevice.BOND_NONE:
							try {
								Log.e("action", "开始配对!");
								Method createBondMethod = BluetoothDevice.class
										.getMethod("createBond");
								createBondMethod.invoke(device);
							} catch (Exception e) {
								Log.e("action", "配对失败!");
								e.printStackTrace();
							}
							break;
						case BluetoothDevice.BOND_BONDED:
							connect(device);
							break;
						}
					}
				}

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.e("ACTION_BOND_STATE_CHANGED", device.getName());
				if (device.getName().contains(deviceName)) {
					connectState = device.getBondState();
					Log.e("配对成功，开始链接", String.valueOf(connectState));
					switch (connectState) {
					case BluetoothDevice.BOND_NONE:
						break;
					case BluetoothDevice.BOND_BONDING:
						break;
					case BluetoothDevice.BOND_BONDED:
						connect(device);
						break;
					}
				}
			}
		}
	};

	/**
	 * 连接
	 * 
	 * @param device
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	private void connect(BluetoothDevice device) {
		// final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		// UUID uuid = UUID.fromString(SPP_UUID);

		UUID uuid = null;
		// if (null != device && null != device.getUuids()) {
		// for (int i = 0; i < device.getUuids().length; i++) {
		// // Log.e("uuid", device.getUuids()[i].toString());
		// uuid = device.getUuids()[0].getUuid();
		// }
		// } else {
		// uuid = UUID.randomUUID();
		// }
		uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		// Log.e("uuid2", uuid.toString());
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket = null;
			Log.e(TAG, "socket is null");
		}
		try {
			socket = device.createRfcommSocketToServiceRecord(uuid);
			socket.connect();
			Log.e(TAG, "connectd");
			if (null != socket.getRemoteDevice().getName()
					&& socket.getRemoteDevice().getName().length() > 0) {
				// Log.e(TAG, socket.getRemoteDevice().getName());
				// tvState.setText("连接设备成功!");
				Log.e("connect", socket.getRemoteDevice().getName());
				Log.e("connect", "success");
				flag = true;
				chooseDeciveType(null);
			}

		} catch (Exception e) {
			// mHandler.sendEmptyMessage(37);
			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			flag = true;
			e.printStackTrace();
		}

	}

	/**
	 * 选择设备
	 * 
	 */
	private void chooseDecive(String name) {
		if (name == null) {
			if (deviceName.equals(bloodPressureName)) {
				handShake();
			} else if (deviceName.equals(vitealCapacityName)) {
				// 请求数据
				askSpoNum();
			} else if (deviceName.equals(bgoName)) {
				getDeciveNumber();
			} else if (deviceName.equals(pulmName)) {
				// setPulmTime();
				setPulmDate();
			} else if (deviceName.equals(bcName)) {
				setBcSyncTime();
			} else if (deviceName.equals(eetName)) {
				eetConfirm();
			}
		} else {

			if (name.contains(bloodPressureName)) {
				// handShake();
				// 血压计
				mHandler.sendEmptyMessage(6);
			} else if (name.contains(vitealCapacityName)) {
				// 请求数据
				// askSpoNum();
				// 血氧仪
				mHandler.sendEmptyMessage(27);
			} else if (name.contains(bgoName)) {
				Log.e("chooseDecive--", "chooseDecive bgoName：" + name);
				// 血糖
				mHandler.sendEmptyMessage(34);
			} else if (name.contains(pulmName)) {
				// setPulmTime();
				// setPulmDate();
				// 肺活量
				mHandler.sendEmptyMessage(34);
			} else if (name.contains(bcName)) {
				Log.e("chooseDecive--", "chooseDecive bcName：" + name);
				// 尿液分析仪
				mHandler.sendEmptyMessage(54);
				// setBcSyncTime();

			} else if (name.contains(eetName)) {
				// 耳温枪
				mHandler.sendEmptyMessage(76);
				// eetConfirm();
			}
		}
	}

	/**
	 * 选择设备类型
	 */
	private void chooseDeciveType(String name) {
		if (name == null) {
			if (deviceName.equals(bloodPressureName)) {
				handShake();
			} else if (deviceName.equals(vitealCapacityName)) {
				// 请求数据
				askSpoNum();
			} else if (deviceName.equals(bgoName)) {
				getDeciveNumber();
			} else if (deviceName.equals(pulmName)) {
				// setPulmTime();
				setPulmDate();
			} else if (deviceName.equals(bcName)) {
				setBcSyncTime();
			} else if (deviceName.equals(eetName)) {
				eetConfirm();
			}
		} else {
			if (name.contains(bloodPressureName)) {
				handShake();
			} else if (name.contains(vitealCapacityName)) {
				// 请求数据
				askSpoNum();
			} else if (name.contains(bgoName)) {
				getDeciveNumber();
			} else if (name.contains(pulmName)) {
				// setPulmTime();
				setPulmDate();
			} else if (name.contains(bcName)) {
				setBcSyncTime();
			} else if (name.contains(eetName)) {
				Log.e("chooseDecive--", "chooseDecive eetName：" + name);
				eetConfirm();
			}
		}

		readThread = new ReadThread();
		readThread.start();
	}

	/**
	 * 血压应答命令代码 此处设备收到应答命令后，不再给手机回复任何信息
	 */
	private void reqestData() {
		byte_date = DeviceCommand.REPLAY_CONTEC08A();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取血氧的设备号
	 */
	private void askSpoNum() {
		STYLEMEDIAN_SPO = 1;
		comd_spo = spoDrive.deviceConfirmCommand();
		sendMessageHandle(comd_spo);
	}

	/**
	 * 获取血氧校验时间命令
	 */
	private void askSpoDate() {
		STYLEMEDIAN_SPO = 1;
		comd_spo = spoDrive.correctionDateTime();
		sendMessageHandle(comd_spo);
	}

	/**
	 * 血氧的请求数据
	 */
	private void askSpo() {
		STYLEMEDIAN_SPO = 1;
		comd_spo = spoDrive.getDataFromDevice();
		sendMessageHandle(comd_spo);
	}

	/**
	 * 血氧的请求设置计步器信息
	 */
	private void askSpoByke() {
		STYLEMEDIAN_SPO = 1;
		comd_spo = spoDrive.setPedometerInfo("170", "65", 0, 24, 5000, 2, 1);
		sendMessageHandle(comd_spo);
	}

	/**
	 * 血氧仪发送计步器为天单位的请求
	 */
	private void askSpoDay() {
		byte_date = com.contec.cms50dj_jar.DeviceCommand
				.dayPedometerDataCommand();
		sendMessageHandle(byte_date);
	}

	/**
	 * 血氧仪数据上传成功
	 */
	private void askSpoSuccess() {
		byte_date = com.contec.cms50dj_jar.DeviceCommand
				.dataUploadSuccessCommand();
		sendMessageHandle(byte_date);
	}

	/**
	 * 血氧仪计步数据上传成功
	 */
	private void askSpoDaySuccess() {
		byte_date = com.contec.cms50dj_jar.DeviceCommand
				.dayPedometerDataSuccessCommand();
		sendMessageHandle(byte_date);
	}

	/**
	 * 握手
	 */
	private void handShake() {
		STYLEMEDIAN_BLOOD = 0;
		comd_date = DeviceCommand.REQUEST_HANDSHAKE();
		sendMessageHandle(comd_date);
	}

	/**
	 * 血压 获取准备校验时间
	 * 
	 */
	private void getCorrectTimeNotice() {
		STYLEMEDIAN_BLOOD = 3;
		byte_date = DeviceCommand.correct_time_notice;
		sendMessageHandle(byte_date);
	}

	/**
	 * 校验时间
	 * 
	 */
	private void getCorrectTime() {
		STYLEMEDIAN_BLOOD = 3;
		byte_date = DeviceCommand.Correct_Time();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取血压数据
	 */
	private void getBloodPressure() {
		STYLEMEDIAN_BLOOD = 1;
		byte_date = DeviceCommand.REQUEST_BLOOD_PRESSURE();
		sendMessageHandle(byte_date);
	}

	/**
	 * 删除血压数据
	 */
	private void deleteDate() {
		STYLEMEDIAN_BLOOD = 5;
		byte_date = DeviceCommand.DELETE_BP();
		sendMessageHandle(byte_date);
	}

	/**
	 * 血糖测试设备获取设备号
	 */
	private void getDeciveNumber() {
		byte_date = bgoCommand.command_ReadID();
		sendMessageHandle(byte_date);
	}

	/**
	 * 血糖测试设备设置时间
	 */
	private void setBgoTime(int receiveNum) {
		if (8 == receiveNum) {
			sendMessageHandle(bgoCommand.command_VerifyTime());
		} else if (9 == receiveNum) {
			sendMessageHandle(bgoCommand.command_VerifyTimeSS());
		}
	}

	/**
	 * 血糖测试设备获取数据
	 */
	private void getBgoData() {
		byte_date = bgoCommand.command_requestData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取血糖数据成功 开始上传
	 */
	private void getBogDataSuccess() {
		if (null != bgoPackManager.m_DeviceDatas
				&& bgoPackManager.m_DeviceDatas.size() > 0) {
			cmssxt = bgoPackManager.m_DeviceDatas
					.get(bgoPackManager.m_DeviceDatas.size() - 1);
			bgoTime = cmssxt.m_saveDate;
			bgoData = cmssxt.m_data;
			if (Network.checkNet(this)) {
				new Thread(new UpdataBGO()).start();
			} else {
				setBGDataToDb();
			}
		} else {
			Log.e("bgo", "no data");
		}
	}
	
	private void setBGDataToDb(){
		bgDeviceDao = new BgDeviceDao();
		bgDeviceDao.setBgValue(String.valueOf(bgoData));
		bgDeviceDao.setType("1");
		bgDeviceDao.setTime(bgoTime);
		if (isTest) {
			bgDeviceDao.setIsTest("1");
			if (null != etPhoneNumber.getText()) {
				if (StringUtils.isNotEmpty(etPhoneNumber.getText().toString())) {
					bgDeviceDao.setUserId(etPhoneNumber.getText().toString());
				}
			} else {
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			bgDeviceDao.setIsTest("0");
			bgDeviceDao.setUserId(userId);
		}
		//存储到数据库
		if(getDBManager().addBgDate(bgDeviceDao)){
			isNoDate = false;
			deleteBgoData();
			SharedPraferenceUtils.save(context, StringUtils.BG_NEW_DATA, "1");
			Log.e(TAG, "血糖数据存到数据库,标记");
		}
	}

	/**
	 * 删除血糖数据
	 */
	private void deleteBgoData() {
		byte_date = bgoCommand.command_delData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 校验尿液分析仪时间
	 */
	private void setBcSyncTime() {
		byte_date = com.contec.jar.BC401.DeviceCommand.Synchronous_Time();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取尿液分析仪数据
	 */
	private void getBcData() {
		byte_date = com.contec.jar.BC401.DeviceCommand.Request_AllData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取尿液分析仪数据成功 开始上传
	 */
	private void getBcDataSuccess() {
		if (Network.checkNet(this)) {
			new Thread(new UpdataBc()).start();
		} else {
//			Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
//					.show();
			setBcDataToDb();
		}
	}

	private void setBcDataToDb(){
		BC401_Data data = bcPackManager.mBc401_Data;
		int index = data.Structs.size() - 1;
		
		bcDeviceDao = new BcDeviceDao();
		bcDeviceDao.setUroVal(String.valueOf(data.Structs.get(index).URO));
		bcDeviceDao.setBilVal(String.valueOf(data.Structs.get(index).BIL));
		bcDeviceDao.setBldVal(String.valueOf(data.Structs.get(index).BLD));
		bcDeviceDao.setKetVal(String.valueOf(data.Structs.get(index).KET));
		bcDeviceDao.setGluVal(String.valueOf(data.Structs.get(index).GLU));
		bcDeviceDao.setProVal(String.valueOf(data.Structs.get(index).PRO));
		bcDeviceDao.setPhVal(String.valueOf(data.Structs.get(index).PH));
		bcDeviceDao.setNitVal(String.valueOf(data.Structs.get(index).NIT));
		bcDeviceDao.setLeuVal(String.valueOf(data.Structs.get(index).LEU));
		bcDeviceDao.setVcVal(String.valueOf(data.Structs.get(index).VC));
		bcDeviceDao.setSgVal(String.valueOf(data.Structs.get(index).SG));
		dealBcData(data, index);
		bcDeviceDao.setTime(inputDate.toString());
		if (isTest) {
			bcDeviceDao.setIsTest("1");
			if (null != etPhoneNumber.getText()) {
				if (StringUtils.isNotEmpty(etPhoneNumber.getText().toString())) {
					bcDeviceDao.setUserId(etPhoneNumber.getText().toString());
				}
			} else {
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			bcDeviceDao.setIsTest("0");
			bcDeviceDao.setUserId(userId);
		}
		//存储到数据库
		if(getDBManager().addBcDate(bcDeviceDao)){
			isNoDate = false;
			deleteBcData();
			SharedPraferenceUtils.save(context, StringUtils.BC_NEW_DATA, "1");
			Log.e(TAG, "肺活量数据存到数据库,标记");
		}
		
	}
	
	/**
	 * 删除尿液分析仪数据
	 */
	private void deleteBcData() {
		Log.e("delete", "删除尿液分析仪数据");
		byte_date = com.contec.jar.BC401.DeviceCommand.Delete_AllData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 肺活量计 校验日期
	 */
	private void setPulmDate() {
		Log.e("肺活量计 校验日期", "肺活量计 校验日期");
		byte_date = cn.com.contec.jar.sp10w.DeviceCommand.command_Date();
		sendMessageHandle(byte_date);
	}

	/**
	 * 肺活量计 校验时间
	 */
	private void setPulmTime() {
		Log.e("肺活量计 校验时间", "肺活量计 校验时间");
		byte_date = cn.com.contec.jar.sp10w.DeviceCommand.command_Time();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取肺活量计数据
	 */
	private void getPulmData() {
		byte_date = cn.com.contec.jar.sp10w.DeviceCommand.command_requestData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取肺活量计数据成功 开始上传
	 */
	private void getPulmDataSuccess() {
		if (null != pulmPackManager.mDeviceDataJarsList
				&& pulmPackManager.mDeviceDataJarsList.size() >= 0) {
			deviceDataJar = pulmPackManager.mDeviceDataJarsList
					.get(pulmPackManager.mDeviceDataJarsList.size() - 1);
			resolvePulmData();
			if (Network.checkNet(this)) {
				new Thread(new UpdataPulm()).start();
			} else {
//				Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
//						.show();
				setPulmDataToDb();
			}
		}
	}
	
	private void setPulmDataToDb(){
		pulmDeviceDao = new PulmDeviceDao();
		pulmDeviceDao.setFvcVal(FVC);
		pulmDeviceDao.setFevVal(FEV1);
		pulmDeviceDao.setPefVal(PEF);
		pulmDeviceDao.setTime(pulmTime);
			pulmDeviceDao.setIsTest("0");
			pulmDeviceDao.setUserId(userId);
			pulmDeviceDao.setElderlyId(elderlyId);
		//存储到数据库
		if(getDBManager().addPulmDate(pulmDeviceDao)){
			SharedPraferenceUtils.save(context, StringUtils.PULM_NEW_DATA, "1");
			isNoDate = false;
			deletePulmData();
			Log.e(TAG, "肺活量数据存到数据库,标记");
		}
	}

	/**
	 * 删除肺活量数据
	 */
	private void deletePulmData() {
		byte_date = cn.com.contec.jar.sp10w.DeviceCommand.command_delData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 耳温枪验证
	 */
	private void eetConfirm() {
		byte_date = eetCommand.commandConfirmEquipment();
		sendMessageHandle(byte_date);
	}

	/**
	 * 耳温枪校验时间
	 */
	private void eetVerifyTime() {
		byte_date = eetCommand.command_VerifyTime();
		sendMessageHandle(byte_date);
	}

	/**
	 * 耳温枪
	 */
	private void eetGetDataNum() {
		byte_date = eetCommand.command_queryDataNum();
		sendMessageHandle(byte_date);
	}

	/**
	 * 耳温枪读取数据
	 */
	private void eetGetData() {
		Log.e("eet_get_data", "get_data");
		byte_date = eetCommand.command_requestAllData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 耳温枪删除数据
	 */
	private void eetDeleteData() {
		Log.e("eet_delete_data", "delete_data");
		byte_date = eetCommand.command_delData();
		sendMessageHandle(byte_date);
	}

	/**
	 * 获取肺活量计数据成功 开始上传
	 */
	private void getEetDataSuccess() {
		if (Network.checkNet(this)) {
			new Thread(new UpdataPulm()).start();
		} else {
			Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 获取耳温枪数据成功 开始上传
	 */
	private void upDateEetData() {
		if (Network.checkNet(this)) {
			new Thread(new UpdataEet()).start();
		} else {
			setTempDataToDb();
//			Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
//					.show();
		}
	}
	
	private void setTempDataToDb(){
		tempDeviceDao = new TempDeviceDao();
		tempDeviceDao.setTemperature(eetData);
		tempDeviceDao.setTime(eetTime);
		tempDeviceDao.setElderlyId(elderlyId);
		if (isTest) {
			tempDeviceDao.setIsTest("1");
			if (null != etPhoneNumber.getText()) {
				if (StringUtils.isNotEmpty(etPhoneNumber.getText().toString())) {
					tempDeviceDao.setUserId(etPhoneNumber.getText().toString());
				}
			} else {
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			tempDeviceDao.setIsTest("0");
			tempDeviceDao.setUserId(userId);
		}
		//存储到数据库
		if(getDBManager().addTempDate(tempDeviceDao)){
			SharedPraferenceUtils.save(context, StringUtils.TEMP_NEW_DATA, "1");
			isNoDate = false;
			eetDeleteData();
			Log.e(TAG, "血氧数据存到数据库,标记");
		}
	}

	/**
	 * 通过蓝牙发送数据
	 * 
	 * @param date
	 */
	private void sendMessageHandle(byte[] date) {
		if (socket == null) {
			Toast.makeText(getApplicationContext(), "连接已断开", Toast.LENGTH_SHORT)
					.show();
			Log.e("error", "连接已断开");
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			os.write(date);
			os.flush();
			// Log.e("send", deviceName + date.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("error", "error");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "error");
		}
	}

	/**
	 * 通过蓝牙接受数据
	 */
	public class ReadThread extends Thread {
		@Override
		public void run() {
			Log.e("ReadThread", "ReadThread start");
			byte[] buffer = new byte[1024];
			int bytes;
			int length = 0;
			InputStream mmInStream = null;
			while (closeActivity) {
				if (flag) {
					if (null != socket) {
						// Log.e("socket", " != null");
						try {
							mmInStream = socket.getInputStream();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					flag = false;
					// Log.e("mmInStream",
					// " mmInStream = socket.getInputStream()");
				}
				try {
					if ((bytes = mmInStream.read(buffer)) > 0) {

						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						if (deviceName.equals(bloodPressureName)) {
							Log.e("STYLEMEDIAN=",
									String.valueOf(STYLEMEDIAN_BLOOD));
							byte byte_back = devicePackManager.arrangeMessage(
									buf_data, buf_data.length,
									STYLEMEDIAN_BLOOD);
							if (null != devicePackManager.mDeviceData) {
								deviceData = devicePackManager.mDeviceData.mData_blood;
							}
							Log.e("byte_back",
									String.valueOf(byte_back) + "------"
											+ "STYLEMEDIAN="
											+ String.valueOf(STYLEMEDIAN_BLOOD));
							switch (byte_back) {
							case (byte) 0x48:// 握手成功
								if (handShakeType == 0) {
									handshakesave = buf_data;
									Log.e("response", "准备校验握手_success");
									mHandler.sendEmptyMessage(0);
								} else if (handShakeType == 1) {
									Log.e("response", "获取数据握手handshake_success");
									mHandler.sendEmptyMessage(3);
								} else if (handShakeType == 2) {
									Log.e("response", "删除数据握手handshake_success");
									mHandler.sendEmptyMessage(5);
								}
								break;
							case (byte) 0x31:// 准备校验时间失败
								Log.e("response", "getTimeCode_error");
								break;
							case (byte) 0x30:// 准备校验时间成功
								Log.e("response", "getTimeCode_success");
								mHandler.sendEmptyMessage(1);
								break;
							case (byte) 0x40:// 校验时间成功
								Log.e("response", "checkTime_success");
								mHandler.sendEmptyMessage(2);
								break;
							case (byte) 0x41:// 校验时间失败
								Log.e("response", "checkTime_error");
								break;
							case (byte) 0x46:// 获取数据成功
								Log.e("response", "getData_success");
								mHandler.sendEmptyMessage(4);
								break;
							case (byte) 0x50:// 删除数据成功
								mHandler.sendEmptyMessage(6);
								break;
							case (byte) 0x51:// 删除数据失败
								mHandler.sendEmptyMessage(7);
								break;
							default:
								break;
							}
						} else if (deviceName.equals(vitealCapacityName)) {
							int byte_back = spoDevice.arrangeMessage(buf_data,
									buf_data.length);
							// Log.e("返回的case", byte_back + "是多少");
							switch (byte_back) {
							case 1:// 获取设备号
								mHandler.sendEmptyMessage(26);// 发送校验时间命令
								break;
							case 2:// 校时成功 发送设置计步器信息命令
								mHandler.sendEmptyMessage(27);
								break;
							case 3:// 校时失败
								mHandler.sendEmptyMessage(28);
								break;
							case 8:// 设置成功 可以发送请求血氧、脉率数据的命令
								mHandler.sendEmptyMessage(23);
								break;
							case 9:// 设置失败
									// 由于血氧、脉率数据和计步器没有关系，所以这里也可以发送请求血氧、脉率数据的命令
								mHandler.sendEmptyMessage(23);
								break;
							case 5:// 血氧、脉率数据全部接收完毕，
								spoData = spoDevice.getDeviceData50dj();
								mHandler.sendEmptyMessage(22);
								break;
							case 20:// 上一包数据接收完成的命令发送成功此时应该发送请求下一包的命令
								mHandler.sendEmptyMessage(23);
								break;
							case 6:// 一包数据接收完毕 ；发送数据上传成功的指令，（必须这样发）
									// spoData = spoDevice
									// .getDeviceData50dj();
								mHandler.sendEmptyMessage(24);
								break;
							case 7:// 请求失败
								mHandler.sendEmptyMessage(21);
								break;
							case 4:// 血氧、脉率无新数据
								mHandler.sendEmptyMessage(20);
								break;
							case 10:// 以天为单位 计步器数据 一包上传完成，发送上传完成的命令
								mHandler.sendEmptyMessage(80);
								break;
							case 11:// 以天为单位计步器数据上一包上传成功请求下一包数据
								mHandler.sendEmptyMessage(27);
								break;
							case 12:// 以天为单位的计步器数据全部上传完成
								mHandler.sendEmptyMessage(81);
								break;
							case 17:// 以天为单位 计步器无新数据
								mHandler.sendEmptyMessage(20);
								break;
							case 13:// 计步器以天为单位的数据上传失败。
								mHandler.sendEmptyMessage(21);
								break;
							default:
								break;
							}
						} else if (deviceName.equals(bgoName)) {
							int receiveNum = bgoPackManager.arrangeMessage(
									buf_data, buf_data.length);
							Log.e("BGO", String.valueOf(receiveNum));
							switch (receiveNum) {
							case 8:// 旧设备 发送不带秒的对时命令
								setBgoTime(receiveNum);
								break;
							case 9:// 新设备 发送带秒的对时命令
								setBgoTime(receiveNum);
								break;
							case 3:// 时间设置成功
								mHandler.sendEmptyMessage(30);
								break;
							case 4:// 时间设置失败
								mHandler.sendEmptyMessage(31);
								break;
							case 1:// 成功接受数据
								mHandler.sendEmptyMessage(32);
								break;
							case 2:// 结合艘数据失败
								mHandler.sendEmptyMessage(33);
								break;
							case 7:// 无数据
								mHandler.sendEmptyMessage(36);
								break;
							case 5:// 删除数据成功
								mHandler.sendEmptyMessage(34);
								break;
							case 6:// 删除数据失败
								mHandler.sendEmptyMessage(35);
								break;
							case 0:
								mHandler.sendEmptyMessage(38);
								break;
							default:
								break;
							}
						} else if (deviceName.equals(pulmName)) {
							int backNum = pulmPackManager.arrangeMessage(
									buf_data, buf_data.length);
							switch (backNum) {
							case 7:// 设置日期成功
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(40);
								break;
							case 8:// 设置日期失败
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(41);
								break;
							case 3:// 设置时间成功
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(42);
								break;
							case 4:// 设置时间失败
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(43);
								break;
							case 1:// 获取数据成功
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(44);
								break;
							case 2:// 无数据
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(46);
								break;
							case 5:// 删除成功
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(47);
								break;
							case 6:// 删除失败
								Log.e("plulm_num", String.valueOf(backNum));
								mHandler.sendEmptyMessage(48);
								break;
							default:
								break;
							}
						} else if (deviceName.equals(bcName)) {
							int backNum = bcPackManager.arrangeMessage(
									buf_data, buf_data.length);
							Log.e("BC", String.valueOf(backNum));
							switch (backNum) {
							case (byte) 0x02:// 校验时间成功
								mHandler.sendEmptyMessage(50);
								break;
							case (byte) 0x05:// 获取数据成功
								mHandler.sendEmptyMessage(51);
								break;
							case (byte) 0x08:// 无新数据
								mHandler.sendEmptyMessage(52);
								break;
							case (byte) 0x06:// 数据删除完毕
								mHandler.sendEmptyMessage(54);
								break;
							default:
								break;
							}
						} else if (deviceName.equals(eetName)) {
							int backNum = eetPackManager.arrangeMessage(
									buf_data, buf_data.length);
							Log.e("耳温枪 设备设备响应", String.valueOf(backNum));
							switch (backNum) {
							case 8:// 验证成功
								mHandler.sendEmptyMessage(70);
								break;
							case 4:// 校验时间失败
								mHandler.sendEmptyMessage(72);
								break;
							case 3:// 校验时间成功
								mHandler.sendEmptyMessage(71);
								break;
							case 1:// 数据读取成功
								mHandler.sendEmptyMessage(73);
								break;
							case 7:// 无数据
								mHandler.sendEmptyMessage(74);
								break;
							case 5:// 删除数据成功
								mHandler.sendEmptyMessage(76);
								break;
							case 6:// 删除数据失败
								mHandler.sendEmptyMessage(77);
								break;
							case 2:
								mHandler.sendEmptyMessage(78);
								break;
							case 9:// 获取数量成功
								mHandler.sendEmptyMessage(79);
								break;
							default:
								break;
							}
						}
					}
				} catch (IOException e) {
					Log.e("mmInStream", "error");
					try {
						mmInStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					flag = false;
					break;
				}
			}
			Log.e("ReadThread", "ReadThread  die");
		}
	}

	/**
	 * 上传血氧的数据
	 */
	private void upDataSpo() {
		if (Network.checkNet(this)) {
			new Thread(new UploadSpo()).start();
		} else {
//			Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
//					.show();
			setSpoDataToDb();
		}
	}
	
	/**
	 * 保存血氧数据到数据库
	 */
	private void setSpoDataToDb(){
		dealSpoData();
		spoDeviceDao = new SpoDeviceDao();
		spoDeviceDao.setSpoValue(spo);
		spoDeviceDao.setPluseRate(pluseRate);
		spoDeviceDao.setTime(spodata.toString());
		spoDeviceDao.setElderlyId(elderlyId);
		if (isTest) {
			spoDeviceDao.setIsTest("1");
			if (null != etPhoneNumber.getText()) {
				if (StringUtils.isNotEmpty(etPhoneNumber.getText().toString())) {
					spoDeviceDao.setUserId(etPhoneNumber.getText().toString());
				}
			} else {
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			spoDeviceDao.setIsTest("0");
			spoDeviceDao.setUserId(userId);
		}
		//存储到数据库
		if(getDBManager().addSpoDate(spoDeviceDao)){
			SharedPraferenceUtils.save(context, StringUtils.SPO_NEW_DATA, "1");
			vitealCapacityUpdataState = true;
			isConnect = 0;
			speakOut(10);
			tvState.setText("血氧测量完成!");
			askSpoDaySuccess();// 发送成功的指令
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getBloodPressureHestory();
			clearProgressState(null, null);
			isUpdate = true;
			updateFinish();
			setUpdateSuccess();
			
			Log.e(TAG, "血氧数据存到数据库,标记");
		}
	}

	/**
	 * 上传血氧数据
	 * 
	 * @author liww
	 */
	private class UploadSpo implements Runnable {
		@Override
		public void run() {
			boolean flag = true;
			try {
				dealSpoData();
				Map<String, String> map = new HashMap<String, String>();
			 	map.put("inputUsr", userId);
				map.put("elderlyId", elderlyId);
				map.put("itemValue", spo);
				map.put("inputDate", spodata.toString());
				map.put("itemConValue", pluseRate);

//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPLOAD_SPO_PRESSURE, map);
//				if (jsonObject != null) {
//					flag = false;
//					Message msg = new Message();
//					msg.what = 25;
//					msg.obj = jsonObject;
//					mHandler.sendMessage(msg);
//				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			} finally {
				if (flag)
					mHandler.sendEmptyMessage(29);
			}
		}
	}
	
	/**
	 * 处理血氧数据
	 */
	private void dealSpoData(){
		spoData = spoDevice.getDeviceData50dj();
		// for (int i = 0; i < spoData.getmSp02DataList().size(); i++) {
		byte[] dataSpo = spoData.getmSp02DataList().get(
				spoData.getmSp02DataList().size() - 1);
		spodata = new StringBuffer("20");
		spo = dataSpo[6] + "";
		pluseRate = dataSpo[7] + "";
		spodata.append(dataSpo[0]);// 年
		if (dataSpo[1] < (byte) 10) {
			spodata.append("0");
		}
		spodata.append(dataSpo[1]);// 月
		if (dataSpo[2] < (byte) 10) {
			spodata.append("0");
		}
		spodata.append(dataSpo[2]);// 日
		if (dataSpo[3] < (byte) 10) {
			spodata.append("0");
		}
		spodata.append(dataSpo[3]);// 时
		if (dataSpo[4] < (byte) 10) {
			spodata.append("0");
		}
		spodata.append(dataSpo[4]);// 分
		if (dataSpo[5] < (byte) 10) {
			spodata.append("0");
		}
		spodata.append(dataSpo[5]);// 秒
	}

	/**
	 * 上传血压数据
	 * 
	 * @author zcy
	 */
	private class UpdataBloodPressure implements Runnable {
		@Override
		public void run() {
			boolean flag = true;
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("elderlyId", elderlyId);
			 	map.put("inputUsr", userId);
				map.put("dbpVal", itemConValue);
				map.put("inputDate", inputDate.toString());
				map.put("sbpVal", itemValue);
				map.put("prVal", prVal);
				map.put("inputOrg", usrOrg);

//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPDATA_BLOOD_PRESSURE, map);
				
				Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "onError");
					}

					@Override
					public void onNext(BaseResponseEntity taseList) {
						Log.e(TAG, "onNext");
						if (taseList != null) {
							Message msg = new Message();
							msg.what = 11;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}

					}
				};
				HttpMethodImp.getInstance().updateNibpInfo(subscriber, map);
				
				
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			} finally {
				if (flag)
					mHandler.sendEmptyMessage(11);
			}
		}
	}

	/**
	 * 上传血糖数据
	 * 
	 * @author zcy
	 *
	 */
	private class UpdataBGO implements Runnable {
		@Override
		public void run() {
			// BC401_Data data = bcPackManager.mBc401_Data;
			// Log.e("Year", String.valueOf(data.Structs.get(0).Year));
			// Log.e("Month", String.valueOf(data.Structs.get(0).Month));
			// Log.e("Date", String.valueOf(data.Structs.get(0).Date));
			// Log.e("Hour", String.valueOf(data.Structs.get(0).Hour));
			// Log.e("Min", String.valueOf(data.Structs.get(0).Min));
			// Log.e("Sec", String.valueOf(data.Structs.get(0).Sec));
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("inputUsr", userId);
				map.put("elderlyId", elderlyId);
				map.put("inputDate", bgoTime);
				map.put("itemValue", String.valueOf(bgoData));
				map.put("itemConValue", "1");// 餐前餐后

//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPDATA_SPO_DATA, map);
//				if (jsonObject != null) {
//					Message msg = new Message();
//					msg.what = 33;
//					msg.obj = jsonObject;
//					mHandler.sendMessage(msg);
//				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	/**
	 * 上传尿液分析结果
	 */
	private class UpdataBc implements Runnable {
		@Override
		public void run() {
			// 得到尿液分析数据
			int index = 0;
			try {
				Map<String, String> map = new HashMap<String, String>();
			
				BC401_Data data = bcPackManager.mBc401_Data;
				index = data.Structs.size() - 1;
				dealBcData(data, index);


				 map.put("userId", userId);
				map.put("bldVal", String.valueOf(data.Structs.get(index).BLD));
				map.put("uroVal", String.valueOf(data.Structs.get(index).URO));
				map.put("bilVal", String.valueOf(data.Structs.get(index).BIL));
				map.put("ketVal", String.valueOf(data.Structs.get(index).KET));
				map.put("gluVal", String.valueOf(data.Structs.get(index).GLU));
				map.put("proVal", String.valueOf(data.Structs.get(index).PRO));
				map.put("phVal", String.valueOf(data.Structs.get(index).PH));
				map.put("nitVal", String.valueOf(data.Structs.get(index).NIT));
				map.put("leuVal", String.valueOf(data.Structs.get(index).LEU));
				map.put("vcVal", String.valueOf(data.Structs.get(index).VC));
				map.put("sgVal", String.valueOf(data.Structs.get(index).SG));
				map.put("inputDate", inputDate.toString());

//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPDATA_BC_DATA, map);
//				if (jsonObject != null) {
//					Message msg = new Message();
//					msg.what = 53;
//					msg.obj = jsonObject;
//					mHandler.sendMessage(msg);
//				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}
	
	private void dealBcData(BC401_Data data,int index){
		if (inputDate.length() != 0) {
			inputDate.delete(0, inputDate.length());
		}
		inputDate.append("20");
		inputDate.append(data.Structs.get(index).Year);
		if (data.Structs.get(index).Month < (byte) 10) {
			inputDate.append("0");
		}
		inputDate.append(data.Structs.get(index).Month);
		if (data.Structs.get(index).Date < (byte) 10) {
			inputDate.append("0");
		}
		inputDate.append(data.Structs.get(index).Date);
		if (data.Structs.get(index).Hour < (byte) 10) {
			inputDate.append("0");
		}
		inputDate.append(data.Structs.get(index).Hour);
		if (data.Structs.get(index).Min < (byte) 10) {
			inputDate.append("0");
		}
		inputDate.append(data.Structs.get(index).Min);
		if (data.Structs.get(index).Sec < (byte) 10) {
			inputDate.append("0");
		}
		inputDate.append(data.Structs.get(index).Sec);
	}

	/**
	 * 解析血压数据
	 */
	private void resolveData(byte[] buf) {
		if (null != buf && buf.length != 0) {
			itemConValue = String.valueOf(buf[2]);
			prVal = String.valueOf(buf[3] & 0x000000ff);
			Log.e("itemConValue -itemConValue", itemConValue);
			Log.e("hhhh", String.valueOf(buf[0]));
			Log.e("llll", String.valueOf(buf[1]));
			if (buf[0] == (byte) 0) {
				itemValue = String.valueOf(buf[1] & 0x000000ff);
				Log.e("进入if buf[1] = ", itemValue);
			} else {
				int a = (buf[0] & 0x000000ff) << 8 + buf[1] & 0x000000ff;
				itemValue = String.valueOf(a);
				Log.e("进入else buf[0] = ", String.valueOf(buf[0]));
			}
			Log.e("itemValue -itemValue", itemValue);
			if (inputDate.length() != 0) {
				inputDate.delete(0, inputDate.length());
			}
			inputDate.append("20");// 年前2位
			inputDate.append(buf[5]);// 年后2位
			if (buf[6] < (byte) 10) {
				inputDate.append("0");
			}
			inputDate.append(buf[6]);// 月
			if (buf[7] < (byte) 10) {
				inputDate.append("0");
			}
			inputDate.append(buf[7]);// 日
			if (buf[8] < (byte) 10) {
				inputDate.append("0");
			}
			inputDate.append(buf[8]);// 时
			if (buf[9] < (byte) 10) {
				inputDate.append("0");
			}
			inputDate.append(buf[9]);// 分
			if (buf[10] < (byte) 10) {
				inputDate.append("0");
			}
			inputDate.append(buf[10]);// 秒
			Log.e("inputDate", inputDate.toString());
			mHandler.sendEmptyMessage(10);
		}
	}

	/**
	 * 上传血压数据到服务器
	 */
	private void upDataBloodPressure() {
		if (Network.checkNet(this)) {
			new Thread(new UpdataBloodPressure()).start();
		} else {
			setNibpToDb();
		}
	}
	
	private void setNibpToDb(){
		nibpDevice = new NibpDeviceDao();
		nibpDevice.setHeighValue(itemValue);
		nibpDevice.setLowValue(itemConValue);
		nibpDevice.setHeartRateNumber(prVal);
		nibpDevice.setTime(inputDate.toString());
		if (isTest) {
			nibpDevice.setIsTest("1");
			if (null != etPhoneNumber.getText()) {
				if (StringUtils.isNotEmpty(etPhoneNumber.getText().toString())) {
					nibpDevice.setUserId(etPhoneNumber.getText().toString());
				}
			} else {
				Toast.makeText(context, "请先输入手机号!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			nibpDevice.setIsTest("0");
			nibpDevice.setUserId(userId);
		}
		//存储到数据库
		if(getDBManager().addNibpDate(nibpDevice)){
			handShakeType = 2;
			handShake();
			inputDate.delete(0, inputDate.length());
			SharedPraferenceUtils.save(context, StringUtils.NIBP_NEW_DATA, "1");
			Log.e(TAG, "血压数据存到数据库,标记");
		}
	}

	/**
	 * 查询指标数据
	 */
	private void getBloodPressureHestory() {
		if(Network.checkNet(context)){
			Log.e("查询指标数据", "start");
			try {
				if (Network.checkNet(this)) {
					new Thread(new GetBloodPressureHestory()).start();
				} else {
					Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {
				Log.e("查询指标数据", "Exception:" + e.getMessage());
				e.printStackTrace();
			}
		}else{
			goTextView.setVisibility(View.GONE);
			titileLayout.setVisibility(View.GONE);
			if(list != null){
				list.clear();
			}else{
				getSimpleData();
			}
			if(listBcData != null){
				listBcData.clear();
			}
			
			oneTextView.setVisibility(View.VISIBLE);
			twoTextView.setVisibility(View.VISIBLE);
			threeTextView.setVisibility(View.VISIBLE);
			fourTextView.setVisibility(View.VISIBLE);
			if(deviceName.contains(bloodPressureName)){
				if(nibpDevice != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("血压数据");
					oneTextView.setText("检测时间");
					twoTextView.setText("收缩压(mmHg)");
					threeTextView.setText("舒张压(mmHg)");
					fourTextView.setText("脉率(次/分)");
					Map<String, String> map = new HashMap<String, String>();
					map.put("time", nibpDevice.getTime().substring(0,4)
							+ "-" + nibpDevice.getTime().substring(4,6)
							+ "-" + nibpDevice.getTime().substring(6,8)
							+ " " + nibpDevice.getTime().substring(8,10)
							+ ":" + nibpDevice.getTime().substring(10,12)
							+ ":" + nibpDevice.getTime().substring(12,14)
							);
					map.put("oneValue", nibpDevice.getHeighValue());
					map.put("twoValue", nibpDevice.getLowValue());
					map.put("threeValue", nibpDevice.getHeartRateNumber());
					list.add(map);
					listSheet.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}else if(deviceName.contains(vitealCapacityName)){
				if(spoDeviceDao != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("血氧数据");
					oneTextView.setText("检测时间");
					twoTextView.setText("血氧饱和度(%SpO2)");
					threeTextView.setText("脉率(PRbpm)");
					fourTextView.setVisibility(View.GONE);
					Map<String, String> map = new HashMap<String, String>();
					map.put("time", spoDeviceDao.getTime().substring(0,4)
							+ "-" + spoDeviceDao.getTime().substring(4,6)
							+ "-" + spoDeviceDao.getTime().substring(6,8)
							+ " " + spoDeviceDao.getTime().substring(8,10)
							+ ":" + spoDeviceDao.getTime().substring(10,12)
							+ ":" + spoDeviceDao.getTime().substring(12,14)
							);
					map.put("oneValue", spoDeviceDao.getSpoValue());
					map.put("twoValue", spoDeviceDao.getPluseRate());
					map.put("threeValue", "");
					list.add(map);
					listSheet.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}else if(deviceName.contains(bgoName)){
				if(bgDeviceDao != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("血糖数据");
					oneTextView.setText("检测时间");
					twoTextView.setText("血糖(mmoL/L)");
					threeTextView.setVisibility(View.GONE);
					fourTextView.setVisibility(View.GONE);
					Map<String, String> map = new HashMap<String, String>();
					map.put("time",bgDeviceDao.getTime());

					map.put("oneValue", bgDeviceDao.getBgValue());
					map.put("twoValue", "");
					map.put("threeValue", "");
					list.add(map);
					listSheet.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}else if(deviceName.contains(pulmName)){
				if(pulmDeviceDao != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("肺活量数据");
					oneTextView.setText("检测时间");
					twoTextView.setText("FVC");
					threeTextView.setText("FEV1");
					fourTextView.setText("PEF");
					Map<String, String> map = new HashMap<String, String>();
					map.put("time", pulmDeviceDao.getTime().substring(0,4)
							 + "-"+ pulmDeviceDao.getTime().substring(4,6)
							 + "-"+ pulmDeviceDao.getTime().substring(6,8)
							 + " "+ pulmDeviceDao.getTime().substring(8,10)
							 + ":"+ pulmDeviceDao.getTime().substring(10,12)
							 + ":"+ pulmDeviceDao.getTime().substring(12,14));
					map.put("oneValue", pulmDeviceDao.getFvcVal());
					map.put("twoValue", pulmDeviceDao.getFevVal());
					map.put("threeValue", pulmDeviceDao.getPefVal());
					list.add(map);
					listSheet.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}else if(deviceName.contains(bcName)){
				if(bcDeviceDao != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("尿液分析数据");
					titileLayout.setVisibility(View.GONE);
					BCData badata = showBcData(bcDeviceDao);
					listBcData.add(badata);
					if (bcAdapter == null) {
						Log.e("bcAdapter", "bcAdapter == null");
						bcAdapter = new BCDataAdapter(context,
								listBcData);
						listSheet.setAdapter(bcAdapter);
						bcAdapter.notifyDataSetChanged();
					} else {
						Log.e("bcAdapter", "bcAdapter != null");
						listSheet.setAdapter(bcAdapter);
						bcAdapter.setData(listBcData);
						bcAdapter.notifyDataSetChanged();
					}
				}
			}else if(deviceName.contains(eetName)){
				if(tempDeviceDao != null){
					goTextView.setVisibility(View.VISIBLE);
					titileLayout.setVisibility(View.VISIBLE);
					goTextView.setText("耳温数据");
					oneTextView.setText("检测时间");
					twoTextView.setText("耳温(℃)");
					threeTextView.setVisibility(View.GONE);
					fourTextView.setVisibility(View.GONE);
					Map<String, String> map = new HashMap<String, String>();
					map.put("time", tempDeviceDao.getTime());
					map.put("oneValue",tempDeviceDao.getTemperature());
					list.add(map);
					Log.e("用户", "体温Value：" + tempDeviceDao.getTemperature());
					listSheet.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}
		}

	}
	
	static BCData showBcData(BcDeviceDao bcDeviceDao){
		BCData badata = new BCData();
		HashMap< String, String> mapData=new HashMap<String, String>();
		mapData.put("uro_0", "Norm");
		mapData.put("uro_1", "1+");
		mapData.put("uro_2", "2+");
		mapData.put("uro_3", ">=3+");
		mapData.put("bld_0", "-");
		mapData.put("bld_1", "+-");
		mapData.put("bld_2", "1+");
		mapData.put("bld_3", "2+");
		mapData.put("bld_4", "3+");
		mapData.put("bil_0", "-");
		mapData.put("bil_1", "1+");
		mapData.put("bil_2", "2+");
		mapData.put("bil_3", "3+");
		mapData.put("ket_0", "-");
		mapData.put("ket_1", "1+");
		mapData.put("ket_2", "2s+");
		mapData.put("ket_3", "3+");
		mapData.put("glu_0", "-");
		mapData.put("glu_1", "+-");
		mapData.put("glu_2", "1+");
		mapData.put("glu_3", "2+");
		mapData.put("glu_4", "3+");
		mapData.put("glu_5", "4+");
		mapData.put("pro_0", "-");
		mapData.put("pro_1", "+-");
		mapData.put("pro_2", "1+");
		mapData.put("pro_3", "2+");
		mapData.put("pro_4", ">=3+");
		mapData.put("ph_0", "5");
		mapData.put("ph_1", "6");
		mapData.put("ph_2", "7");
		mapData.put("ph_3", "8");
		mapData.put("ph_4", "9");
		mapData.put("nit_0", "-");
		mapData.put("nit_1", "1+");
		mapData.put("leu_0", "-");
		mapData.put("leu_1", "+-");
		mapData.put("leu_2", "1+");
		mapData.put("leu_3", "2+");
		mapData.put("leu_4", "3+");
		mapData.put("sg_0", "<=1.005");
		mapData.put("sg_1", "1.010");
		mapData.put("sg_2", "1.015");
		mapData.put("sg_3", "1.020");
		mapData.put("sg_4", "1.025");
		mapData.put("sg_5", ">=1.030");
		mapData.put("vc_0", "-");
		mapData.put("vc_1", "+-");
		mapData.put("vc_2", "1+");
		mapData.put("vc_3", "2+");
		mapData.put("vc_4", "3+");
		
		badata.setBilVal(mapData.get("bil_" + bcDeviceDao.getBilVal()));
		badata.setBldVal(mapData.get("bld_" + bcDeviceDao.getBldVal()));
		badata.setGluVal(mapData.get("glu_" + bcDeviceDao.getGluVal()));
		badata.setKetVal(mapData.get("ket_" + bcDeviceDao.getKetVal()));
		badata.setLeuVal(mapData.get("leu_" + bcDeviceDao.getLeuVal()));
		badata.setNitVal(mapData.get("nit_" + bcDeviceDao.getNitVal()));
		badata.setPhVal(mapData.get("ph_" + bcDeviceDao.getPhVal()));
		badata.setProVal(mapData.get("pro_" + bcDeviceDao.getProVal()));
		badata.setSgVal(mapData.get("sg_" + bcDeviceDao.getSgVal()));
		badata.setUroVal(mapData.get("uro_" + bcDeviceDao.getUroVal()));
		badata.setVcVal(mapData.get("vc_" + bcDeviceDao.getVcVal()));
		badata.setTime(bcDeviceDao.getTime().substring(8, 10)
				+ ":" + bcDeviceDao.getTime().substring(10, 12)
				+ ":" + bcDeviceDao.getTime().substring(12, 14));
		badata.setDate(bcDeviceDao.getTime().substring(0, 4)
				+ "-" + bcDeviceDao.getTime().substring(4, 6)
				+ "-" + bcDeviceDao.getTime().substring(6, 8));
		
		return badata;
	}

	/**
	 * 上传耳温枪数据
	 * 
	 * @author zcy
	 *
	 */
	private class UpdataEet implements Runnable {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("elderlyId", elderlyId);
				map.put("inputUsr", userId);
				Log.e(TAG, userId);
				map.put("inputDate", eetTime);
				map.put("temperature", eetData);
				map.put("inputOrg", usrOrg);
//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPDATE_EET_DATA, map);
				Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "onError");
					}

					@Override
					public void onNext(BaseResponseEntity taseList) {
						Log.e(TAG, "onNext");
						if (taseList != null) {
							Message msg = new Message();
							msg.what = 75;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}

					}
				};
				HttpMethodImp.getInstance().updateTempInfo(subscriber, map);
				
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	/**
	 * 上传肺活量数据
	 * 
	 * @author zcy
	 *
	 */
	private class UpdataPulm implements Runnable {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
//				resolvePulmData();
				map.put("elderlyId", elderlyId);
				map.put("inputUsr", userId);
				map.put("inputDate", pulmTime);
				map.put("fvcVal", FVC);
				map.put("fevVal", FEV1);
				map.put("pefVal", PEF);
				map.put("inputOrg", usrOrg);
//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.UPDATA_PULM_DATA, map);
				Subscriber<BaseResponseEntity> subscriber = new Subscriber<BaseResponseEntity>() {

					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "onError");
					}

					@Override
					public void onNext(BaseResponseEntity taseList) {
						Log.e(TAG, "onNext");
						if (taseList != null) {
							Message msg = new Message();
							msg.what = 46;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}

					}
				};
				HttpMethodImp.getInstance().updatePulmInfo(subscriber, map);
				
				
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	private void resolvePulmData() {
		StringBuffer time = new StringBuffer();
		time.append("20");
		time.append(deviceDataJar.mPatientInfo.mYear);
		if (deviceDataJar.mPatientInfo.mMonth < 10) {
			time.append("0");
		}
		time.append(deviceDataJar.mPatientInfo.mMonth);
		if (deviceDataJar.mPatientInfo.mDay < 10) {
			time.append("0");
		}
		time.append(deviceDataJar.mPatientInfo.mDay);
		if (deviceDataJar.mPatientInfo.mHour < 10) {
			time.append("0");
		}
		time.append(deviceDataJar.mPatientInfo.mHour);
		if (deviceDataJar.mPatientInfo.mMin < 10) {
			time.append("0");
		}
		time.append(deviceDataJar.mPatientInfo.mMin);
		if (deviceDataJar.mPatientInfo.mSecond < 10) {
			time.append("0");
		}
		time.append(deviceDataJar.mPatientInfo.mSecond);
		// Time t=new Time();
		// t.setToNow(); // 取得系统时间。
		// int year = t.year;
		// int month = t.month;
		// int date = t.monthDay;
		// int hour = t.hour; // 0-23
		// int minute = t.minute;
		// int second = t.second;
		// time.append(t.hour);
		// time.append(t.minute);
		// time.append(t.second);
		Log.e("pulm_time", time.toString());
		pulmTime = time.toString();
		FVC = String.valueOf(deviceDataJar.mParamInfo.mFVC);
		FEV1 = String.valueOf(deviceDataJar.mParamInfo.mFEV1);
		PEF = String.valueOf(deviceDataJar.mParamInfo.mPEF);

	}

	private class GetBloodPressureHestory implements Runnable {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				if (StringUtils.isNotEmpty(userId)) {
					map.put("elderlyId", elderlyId);
					Log.e("用户的id", userId);
				}

//				map.put("phone", strPhoneNumber);
//				Log.e("用户的phone", "isTest  strPhoneNumber:" + strPhoneNumber);
				map.put("pageSize", "10");
				map.put("pageIndex", "0");
				JSONObject jsonObject = null;
				// 血压
				if (deviceName.contains("NIBP")) {
//					jsonObject = HttpMethod.postData(
//							ServerURL.GET_BLOOD_PRESSURE_HESTORY, map);
					Subscriber<NibpHistroyResponse> subscriber = new Subscriber<NibpHistroyResponse>() {

						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError");
						}

						@Override
						public void onNext(NibpHistroyResponse taseList) {
							Log.e(TAG, "onNext");
							Message msg = new Message();
							msg.what = 12;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}
					};
					HttpMethodImp.getInstance().getNibpHistroyInfo(subscriber, map);
				} else if (deviceName.contains("SpO")) {// 血氧
//					jsonObject = HttpMethod.postData(ServerURL.GET_SPO_HESTORY,
//							map);
				} else if (deviceName.contains("BG")) {// 血糖
//					jsonObject = HttpMethod.postData(ServerURL.LOAD_BG_DATA,
//							map);
				} else if (deviceName.contains("PULM")) {// 肺活量
//					jsonObject = HttpMethod.postData(ServerURL.LOAD_DATA, map);
					Subscriber<PulmHistroyResponse> subscriber = new Subscriber<PulmHistroyResponse>() {

						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError");
						}

						@Override
						public void onNext(PulmHistroyResponse taseList) {
							Log.e(TAG, "onNext");
							Message msg = new Message();
							msg.what = 12;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}
					};
					HttpMethodImp.getInstance().getPulmHistroyInfo(subscriber, map);
				} else if (deviceName.contains("BC")) {// 尿液分析
//					jsonObject = HttpMethod.postData(ServerURL.LOAD_BC, map);
				} else if (deviceName.contains(eetName)) {
//					jsonObject = HttpMethod.postData(ServerURL.LOAD_EET, map);
					Subscriber<TempHistroyResponse> subscriber = new Subscriber<TempHistroyResponse>() {

						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError");
						}

						@Override
						public void onNext(TempHistroyResponse taseList) {
							Log.e(TAG, "onNext");
							Message msg = new Message();
							msg.what = 12;
							msg.obj = taseList;
							mHandler.sendMessage(msg);
						}
					};
					HttpMethodImp.getInstance().getTempHistroyInfo(subscriber, map);
				}
//				if (jsonObject != null) {
//					Message msg = new Message();
//					msg.what = 12;
//					msg.obj = jsonObject;
//					mHandler.sendMessage(msg);
//				} else {
//					Log.e("jsonObject", "jsonObject is null");
//				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, e.toString());
			}
		}
	}

	/**
	 * 删除用户
	 */
	private void DeleteUserHint() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("确定要删除该用户吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DeleteUser();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void DeleteUser() {
		if (Network.checkNet(this)) {
			if (loadingDialog == null) {
				loadingDialog = LoadingDialog.loadingSave(context);
				loadingDialog.show();
			}
			new Thread(new DeleteUserThread()).start();
		} else {
			Toast.makeText(this, R.string.need_connect, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 删除用户线程
	 * 
	 * @author zcy
	 *
	 */
	private class DeleteUserThread implements Runnable {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", userId);
				map.put("groupId", groupId);
				Log.e("userId", userId);
				Log.e("groupId", groupId);
//				JSONObject jsonObject = HttpMethod.postData(
//						ServerURL.DELETE_USER, map);
//				Log.e("jsonObject", jsonObject.toString());
//				if (loadingDialog != null) {
//					loadingDialog.dismiss();
//					loadingDialog = null;
//				}
//				if (jsonObject != null) {
//					if (jsonObject.getBoolean("success")) {
//						mHandler.sendEmptyMessage(60);
//					} else {
//						Message msg = new Message();
//						msg.what = 61;
//						Log.e("delete_error",
//								(String) jsonObject.get("message"));
//						msg.obj = jsonObject.get("message");
//						mHandler.sendMessage(msg);
//					}
//				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				mHandler.sendEmptyMessage(61);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		clearUpdateState();
		if (null != mat) {
			mat.cancel();
		}
	}

	/**
	 * 清空状态
	 */
	private void clearUpdateState() {
		Log.e("clearUpdateState", "clearUpdateState");
		closeActivity = false;
		bloodPressureUpdataState = true;
		vitealCapacityUpdataState = true;
		bgoUpdataState = true;
		pulmUpdataState = true;
		bcUpdataState = true;
	}

	/**
	 * 提示设备没有数据
	 */
	private void hintNoData() {
		tvState.setText("设备没有数据,请先测量!");
		speakOut(3);
		clearState();
	}

	/**
	 * 清理状态
	 */
	private void clearState() {
		handShakeType = 0;
		STYLEMEDIAN_BLOOD = 0;
	}

	/**
	 * 输出声音
	 * 
	 * @param i
	 */
	private void speakOut(int i) {
		soundpool.play(i, 1, 1, 0, 0, 1);
	}

	/**
	 * 数据上传完成
	 */
	private void updateFinish() {
		if(!isTest){
			Intent intent = new Intent();
			intent.putExtra("type", "1");
			intent.putExtra("PatientNo", PatientNo);
			intent.setAction("receive_nibp");
			sendBroadcast(intent);
		}
		
	}

	private List<Map<String, String>> getSimpleData() {
		if (null != list) {
			list.clear();
		} else {
			list = new ArrayList<Map<String, String>>();
		}
		return list;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "修改信息回显");
		switch (resultCode) {
		case 0:
			break;
		case 1:
			if (data != null) {
				Bundle bun = data.getBundleExtra("intent");
				patientInfo = bun.getParcelable("patientInfo");
//				tvName.setText(patientInfo.getName());
				tvMobielNumber.setText(patientInfo.getMobilephone());
				tvIdCard.setText(StringUtils.dismissIdCard(patientInfo
						.getCardNo()));

				setResult(1);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 测量相关提示
	 */
	private void clearHint() {
		llNIBPHint.setVisibility(View.GONE);
		llBGHint.setVisibility(View.GONE);
		llPulmHint.setVisibility(View.GONE);
	}

	/**
	 * 隐藏progress
	 */
	private void clearProgressState(ProgressBar pb, LinearLayout llCheck) {
		// Log.e("clearProgressStat---start", "clearProgressState start");
		try {
			clearHint();

			llBloodPressure.setEnabled(true);
			llVitealCapacity.setEnabled(true);
			llBgo.setEnabled(true);
			llBc.setEnabled(true);
			llPulm.setEnabled(true);
			llEet.setEnabled(true);

			pbBibp.setVisibility(View.GONE);
			pbSpo.setVisibility(View.GONE);
			pbBgo.setVisibility(View.GONE);
			pbPulm.setVisibility(View.GONE);
			pbBc.setVisibility(View.GONE);
			pbEet.setVisibility(View.GONE);

			if (pb != null) {
				pb.setVisibility(View.VISIBLE);
			}
			if (llCheck != null) {
				llCheck.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("clearProgressStat---Exception",
					"clearProgressState Exception:" + e.getMessage());
		}
		// Log.e("clearProgressStat---end", "clearProgressState end");
	}

	private void setUpdateSuccess() {
		if (deviceName.contains(bloodPressureName)) {
			ivNIBPFinish.setVisibility(View.VISIBLE);
		} else if (deviceName.contains(vitealCapacityName)) {
			ivSoPFinish.setVisibility(View.VISIBLE);
		} else if (deviceName.contains(bgoName)) {
			ivBGbpFinish.setVisibility(View.VISIBLE);
		} else if (deviceName.contains(pulmName)) {
			ivPULMbpFinish.setVisibility(View.VISIBLE);
		} else if (deviceName.contains(bcName)) {
			ivBCbpFinish.setVisibility(View.VISIBLE);
		} else if (deviceName.contains(eetName)) {
			ivTEMPFinish.setVisibility(View.VISIBLE);
		}
	}
	
	private DBManager getDBManager(){
		if(dbManager == null){
			dbManager = new DBManager(context);
		}
		return dbManager;
	}
}
