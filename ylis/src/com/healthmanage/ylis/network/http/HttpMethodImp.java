package com.healthmanage.ylis.network.http;

import java.util.Map;

import com.healthmanage.ylis.model.AcceptConnectResponse;
import com.healthmanage.ylis.model.BaseResponseEntity;
import com.healthmanage.ylis.model.CheckVersionResponse;
import com.healthmanage.ylis.model.DoseHistoryResponse;
import com.healthmanage.ylis.model.DoseListResponse;
import com.healthmanage.ylis.model.DrugListResponse;
import com.healthmanage.ylis.model.GerAllUserResponse;
import com.healthmanage.ylis.model.GetFrequencyResponse;
import com.healthmanage.ylis.model.GetLaundryListResponse;
import com.healthmanage.ylis.model.GetMaybeTaskResponse;
import com.healthmanage.ylis.model.GetRoomListModelTwoResponse;
import com.healthmanage.ylis.model.GetSannitationInfoResponse;
import com.healthmanage.ylis.model.GetTaskListModelTwoLResponse;
import com.healthmanage.ylis.model.GetTurnBodyResponse;
import com.healthmanage.ylis.model.HourRecordResponse;
import com.healthmanage.ylis.model.LeaveSrarchElderlyResponse;
import com.healthmanage.ylis.model.LoginResponse;
import com.healthmanage.ylis.model.NibpHistroyResponse;
import com.healthmanage.ylis.model.PatroalExceptionListResponse;
import com.healthmanage.ylis.model.PatroalHistroyResponse;
import com.healthmanage.ylis.model.PulmHistroyResponse;
import com.healthmanage.ylis.model.StaffInfoResponse;
import com.healthmanage.ylis.model.TaskConnectNewResponse;
import com.healthmanage.ylis.model.TaskConnectResponse;
import com.healthmanage.ylis.model.TaskDetailResponse;
import com.healthmanage.ylis.model.TastListResponse;
//import com.squareup.okhttp.OkHttpClient;


import com.healthmanage.ylis.model.TempHistroyResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.FieldMap;
import retrofit.http.Query;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpMethodImp {
	private static final String BASE_URL = "http://10.10.3.120:8080/ylis/";
	// private static final String BASE_URL = "http://192.168.0.117:8080/ylis/";
	private static final int DEFAULT_TIMEOUT = 5;

	private Retrofit retrofit;

	private HttpMethod httpMethod;

	// 构造方法私有
	private HttpMethodImp() {

//		OkHttpClient client = new OkHttpClient();

		retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build();

		httpMethod = retrofit.create(HttpMethod.class);

	}

	// 在访问HttpMethods时创建单例
	private static class SingletonHolder {
		private static final HttpMethodImp INSTANCE = new HttpMethodImp();
	}

	// 获取单例
	public static HttpMethodImp getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * @方法描述：用户登录
	 * @param subscriber
	 * @param map
	 */
	public void userLogin(Subscriber<LoginResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.userLogin(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：检查是否有新版本
	 * @param subscriber
	 * @param map
	 */
	public void checkVersionInfo(Subscriber<CheckVersionResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.checkVersionInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * 下载新版本apk
	 * @param client
	 * @param apkUrl
	 * @return
	 */
	public Call<ResponseBody> newApkDownload(OkHttpClient client,
			@Query("apkUrl")String apkUrl) {
		Retrofit.Builder downloadRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
		HttpMethod downloadHttpMethod = downloadRetrofit.client(client)
		        .build().create(HttpMethod.class);
		return downloadHttpMethod.newApkDownload(apkUrl);
	}

	/**
	 * @方法描述：获取班次信息
	 * @param subscriber
	 * @param map
	 */
	public void getFrequencyInfo(Subscriber<GetFrequencyResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getFrequencyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取交接信息
	 * @param subscriber
	 * @param map
	 */
	public void getTaskConnectInfo(Subscriber<TaskConnectResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskConnectInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：不交接登录
	 * @param subscriber
	 * @param map
	 */
	public void getTaskConnectInfoNew(
			Subscriber<TaskConnectNewResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskConnectInfoNew(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：接受交接信息
	 * @param subscriber
	 * @param map
	 */
	public void acceptTaskConnectInfo(
			Subscriber<AcceptConnectResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.acceptTaskConnect(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：拒绝交接信息
	 * @param subscriber
	 * @param map
	 */
	public void refuseTaskConnectInfo(
			Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.refuseTaskConnect(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：举报交接信息
	 * @param subscriber
	 * @param map
	 */
	public void reportTaskConnectInfo(
			Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskConnectInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取本次任务信息
	 * @param subscriber
	 * @param map
	 */
	public void getTestListInfo(Subscriber<TastListResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskListInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取每个老人的详细任务
	 * @param subscriber
	 * @param map
	 */
	public void getTestDetailInfo(Subscriber<TaskDetailResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskDetailInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：完成每个老人的详细任务
	 * @param subscriber
	 * @param map
	 */
	public void updateTestDetailInfo(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.updateTaskDetailInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取护工的详细信息
	 * @param subscriber
	 * @param map
	 */
	public void getStaffInfo(Subscriber<StaffInfoResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getStaffInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：提交任务交接单
	 * @param subscriber
	 * @param map
	 */
	public void updateTaskConnect(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.updateTaskConnect(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取服药记录
	 * @param subscriber
	 * @param map
	 */
	public void getDoseHistroy(Subscriber<DoseHistoryResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getDoseHistroy(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取48小时观察记录信息
	 * @param subscriber
	 * @param map
	 */
	public void getHourRecord(Subscriber<HourRecordResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getHourRecord(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：提交48小时观察记录信息
	 * @param subscriber
	 * @param map
	 */
	public void updateHourRecord(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.updateHourRecord(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取任务列表 model2
	 * @param subscriber
	 * @param map
	 */
	public void getTaskListModelTwo(
			Subscriber<GetTaskListModelTwoLResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTaskListModelTwo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取任务房间床位老人信息 model2
	 * @param subscriber
	 * @param map
	 */
	public void getRoomListModelTwo(
			Subscriber<GetRoomListModelTwoResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getRoomListModelTwo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：老人请假
	 * @param subscriber
	 * @param map
	 */
	public void postLeave(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.leave(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：老人请假回来 销假
	 * @param subscriber
	 * @param map
	 */
	public void resumptionLeave(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.resumptionLeave(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：查看遗留未完成任务
	 * @param subscriber
	 * @param map
	 */
	public void getLeavlTaskList(Subscriber<TastListResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getLeavlTaskList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：修改密码
	 * @param subscriber
	 * @param map
	 */
	public void changePwd(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.changePwd(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取48小时老人列表
	 * @param subscriber
	 * @param map
	 */
	public void get48HourselderlyList(Subscriber<GetRoomListModelTwoResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.get48HourselderlyList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：请假 获取老人信息
	 * @param subscriber
	 * @param map
	 */
	public void getLeaveElerlyInfo(Subscriber<LeaveSrarchElderlyResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getLeaveElerlyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	
	
	/**
	 * @方法描述：获取环境卫生信息
	 * @param subscriber
	 * @param map
	 */
	public void getSannatationInfo(Subscriber<GetSannitationInfoResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getSannitationInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：保存环境卫生信息
	 * @param subscriber
	 * @param map
	 */
	public void saveSannitationInfo(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.saveSannitationInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取翻身记录列表
	 * @param subscriber
	 * @param map
	 */
	public void getTurnBodyList(Subscriber<GetTurnBodyResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getTurnBodyList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：保存翻身记录
	 * @param subscriber
	 * @param map
	 */
	public void saveTurnBodyInfo(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.saveTurnBodyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取洗衣服务记录
	 * @param subscriber
	 * @param map
	 */
	public void getLaundryListInfo(Subscriber<GetLaundryListResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getLaundryListInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：保洗衣服务记录
	 * @param subscriber
	 * @param map
	 */
	public void saveLaundryInfo(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.saveLaundryInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：添加一条巡视异常情况
	 * @param subscriber
	 * @param map
	 */
	public void savePatroalExceptionItem(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.savePatroalExceptionItem(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：添加一条巡视正常情况
	 * @param subscriber
	 * @param map
	 */
	public void savePatroalNormalItem(Subscriber<BaseResponseEntity> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.savePatroalNormalItem(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：查询护工护理的老人
	 * @param subscriber
	 * @param map
	 */
	public void getElderlyInfo(Subscriber<LeaveSrarchElderlyResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getElderlyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：查询备选任务列表
	 * @param subscriber
	 * @param map
	 */
	public void getMaybeTaskList(Subscriber<GetMaybeTaskResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getMaybeTaskList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：查询巡视记录历史信息
	 * @param subscriber
	 * @param map
	 */
	public void getPatroalHistroyList(Subscriber<PatroalHistroyResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getPatroalHistroyList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：查询巡视记录 异常记录详细信息
	 * @param subscriber
	 * @param map
	 */
	public void getPatroalExceptionList(Subscriber<PatroalExceptionListResponse> subscriber,
			@FieldMap Map<String, String> map) {
		httpMethod.getPatroalExceptionList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：查询需要完成该项任务的老人信息列表(备选)
	 * @param subscriber
	 * @param map
	 */
	public void getMaybeTaskElderlyList(Subscriber<GetRoomListModelTwoResponse> subscriber,
										@FieldMap Map<String, String> map) {
		httpMethod.getMaybeTaskElderlyList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：完成备选任务
	 * @param subscriber
	 * @param map
	 */
	public void updateMaybeTask(Subscriber<BaseResponseEntity> subscriber,
										@FieldMap Map<String, String> map) {
		httpMethod.updateMaybeTask(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取所有的护工信息
	 * @param subscriber
	 * @param map
	 */
	public void getAllUserList(Subscriber<GerAllUserResponse> subscriber,
								@FieldMap Map<String, String> map) {
		httpMethod.getAllUserList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：上传老人特殊情况信息
	 * @param subscriber
	 * @param map
	 */
	public void updateElderlyExceptionInfo(Subscriber<BaseResponseEntity> subscriber,
							   @FieldMap Map<String, String> map) {
		httpMethod.updateElderlyExceptionInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：本区域公共设施设备报修情况
	 * @param subscriber
	 * @param map
	 */
	public void updateAreaFcilitiesException(Subscriber<BaseResponseEntity> subscriber,
										   @FieldMap Map<String, String> map) {
		httpMethod.updateAreaFcilitiesException(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取药品列表
	 * @param subscriber
	 * @param map
	 */
	public void getDrugList(Subscriber<DrugListResponse> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.getDrugList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：上传血压信息
	 * @param subscriber
	 * @param map
	 */
	public void updateNibpInfo(Subscriber<BaseResponseEntity> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.updateNibpInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：上传肺活量信息
	 * @param subscriber
	 * @param map
	 */
	public void updatePulmInfo(Subscriber<BaseResponseEntity> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.updatePulmInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：上传耳温信息
	 * @param subscriber
	 * @param map
	 */
	public void updateTempInfo(Subscriber<BaseResponseEntity> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.updateTempInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取血压历史信息
	 * @param subscriber
	 * @param map
	 */
	public void getNibpHistroyInfo(Subscriber<NibpHistroyResponse> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.getNibpHistroyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取肺活量历史信息
	 * @param subscriber
	 * @param map
	 */
	public void getPulmHistroyInfo(Subscriber<PulmHistroyResponse> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.getPulmHistroyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
	
	/**
	 * @方法描述：获取耳温历史信息
	 * @param subscriber
	 * @param map
	 */
	public void getTempHistroyInfo(Subscriber<TempHistroyResponse> subscriber,
											 @FieldMap Map<String, String> map) {
		httpMethod.getTempHistroyInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：药品管理-添加药品
	 * @param subscriber
	 * @param map
	 */
	public void updateDrugsInfo(Subscriber<BaseResponseEntity> subscriber,
								@FieldMap Map<String, String> map) {
		httpMethod.updateDrugsInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：发药管理
	 * @param subscriber
	 * @param map
	 */
	public void sendFrugsInfo(Subscriber<BaseResponseEntity> subscriber,
								@FieldMap Map<String, String> map) {
		httpMethod.sendFrugsInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取有某项套餐的老人列表
	 * @param subscriber
	 * @param map
	 */
	public void getSomeElderlyList(Subscriber<GetRoomListModelTwoResponse> subscriber,
							  @FieldMap Map<String, String> map) {
		httpMethod.getSomeElderlyList(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：获取有某项套餐的老人列表
	 * @param subscriber
	 * @param map
	 */
	public void getSomeElderlyList1(Subscriber<TastListResponse> subscriber,
								   @FieldMap Map<String, String> map) {
		httpMethod.getSomeElderlyList1(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：查询护工护理老人是否已经发药
	 * @param subscriber
	 * @param map
	 */
	public void getElderlySendDrugInfo(Subscriber<TastListResponse> subscriber,
									@FieldMap Map<String, String> map) {
		httpMethod.getElderlySendDrugInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：查询老人今天吃药信息
	 * @param subscriber
	 * @param map
	 */
	public void getElderlyDoseInfo(Subscriber<DoseListResponse> subscriber,
									   @FieldMap Map<String, String> map) {
		httpMethod.getElderlyDoseInfo(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：护工执行辅助服药
	 * @param subscriber
	 * @param map
	 */
	public void dose(Subscriber<BaseResponseEntity> subscriber,
								   @FieldMap Map<String, String> map) {
		httpMethod.dose(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}

	/**
	 * @方法描述：删除药品
	 * @param subscriber
	 * @param map
	 */
	public void deleteDrug(Subscriber<BaseResponseEntity> subscriber,
					 @FieldMap Map<String, String> map) {
		httpMethod.deleteDrug(map).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
	}
}
