package com.healthmanage.ylis.network.http;

import java.io.File;
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
import com.healthmanage.ylis.model.TempHistroyResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public interface HttpMethod {
	/**
	 * @方法描述：普通登陆请求
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/user/login")
	public Observable<LoginResponse> userLogin(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：检查是否有新版本
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/versionController/findMaxVcode")
	public Observable<CheckVersionResponse> checkVersionInfo(@FieldMap Map<String, String> map);

	/**
	 * 下载新的apk文件
	 * @param apkUrl
	 * @return
	 */
	@POST("v1/versionController/downloadApk")
    Call<ResponseBody> newApkDownload(@Query("apkUrl")String apkUrl);
	
	/**
	 * @方法描述：获取班次信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("user/login")
	public Observable<GetFrequencyResponse> getFrequencyInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：不交接
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/getJlyHljjjlMByInputIdNew")
	public Observable<TaskConnectNewResponse> getTaskConnectInfoNew(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取交接信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/getJlyHljjjlMByInputId")
	public Observable<TaskConnectResponse> getTaskConnectInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：接受交接
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/receiveJlyHljjjlM")
	public Observable<AcceptConnectResponse> acceptTaskConnect(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：拒绝交接
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/updateJlyHljjjlMStatus")
	public Observable<BaseResponseEntity> refuseTaskConnect(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：举报交接
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("user/login")
	public Observable<BaseResponseEntity> reportTaskConnect(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取护理任务/房间列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/loadElderlyBed")
	public Observable<TastListResponse> getTaskListInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取详细任务 model1
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPlan2/findJlyPlan2ListMByParam")
	public Observable<TaskDetailResponse> getTaskDetailInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：完成任务
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPlan2/updateJlyPlan2MByDetiIds")
	public Observable<BaseResponseEntity> updateTaskDetailInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取护工信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("user/login")
	public Observable<StaffInfoResponse> getStaffInfo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：提交交接单信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/addJlyHljjjlM")
	public Observable<BaseResponseEntity> updateTaskConnect(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取用药记录
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/addJlyHljjjlM")
	public Observable<DoseHistoryResponse> getDoseHistroy(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：查询48小时观察记录
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyQuestionnaireMain/findFehgzjlMain")
	public Observable<HourRecordResponse> getHourRecord(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：更新48小时观察记录
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyQuestionnaireMain/updateFehgjjlgdMain")
	public Observable<BaseResponseEntity> updateHourRecord(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：老人请假外出
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyAfl/addJlyAflM")
	public Observable<BaseResponseEntity> leave(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：老人回来销假
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyAfl/backJlyAflM")
	public Observable<BaseResponseEntity> resumptionLeave(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取任务列表 model2
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPlan2/findElderlyPlanListM")
	public Observable<GetTaskListModelTwoLResponse> getTaskListModelTwo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取房间床位老人信息 model2
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPlan2/findElderlyByBedNo")
	public Observable<GetRoomListModelTwoResponse> getRoomListModelTwo(
			@FieldMap Map<String, String> map);

	/**
	 * @方法描述：更新 下载新的apk
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("http://yiliantianxia.healthmanage.cn/android/NoticeAndroid_downloadApk.action?versionType=9")
	public Observable<File> getNewApk(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：查看遗留未完成任务
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/loadElderlyBedbyWCZT")
	public Observable<TastListResponse> getLeavlTaskList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：修改密码
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/user/simpleChangePwd")
	public Observable<BaseResponseEntity> changePwd(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取48小时老人列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/loadElderlyBed48")
	public Observable<GetRoomListModelTwoResponse> get48HourselderlyList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：请假 获取老人信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyElderly/findJlyElderlyListByNameM")
	public Observable<LeaveSrarchElderlyResponse> getLeaveElerlyInfo(@FieldMap Map<String, String> map);
	
	
	
	/**
	 * @方法描述：获取环境卫生情况
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/loadHjwsInfo")
	public Observable<GetSannitationInfoResponse> getSannitationInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：保存环境卫生情况
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/updateJlyHjwsBatch")
	public Observable<BaseResponseEntity> saveSannitationInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取翻身记录列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/loadFsjlList")
	public Observable<GetTurnBodyResponse> getTurnBodyList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：保存翻身记录 批量
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyFsjlBatch")
	public Observable<BaseResponseEntity> saveTurnBodyInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取洗衣服务记录
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/loadXyfwjlList")
	public Observable<GetLaundryListResponse> getLaundryListInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：保洗衣服务记录 批量
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyXyfwjlBatch")
	public Observable<BaseResponseEntity> saveLaundryInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：添加一条巡视异常情况
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyXsjlyc")
	public Observable<BaseResponseEntity> savePatroalExceptionItem(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：添加一条巡视正常情况
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyXsjl")
	public Observable<BaseResponseEntity> savePatroalNormalItem(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：查询护工护理的老人
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyElderlyController/loadElderlyInfo")
	public Observable<LeaveSrarchElderlyResponse> getElderlyInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：查询备选任务列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyTcItem/findElderlyPlanListByExecM")
	public Observable<GetMaybeTaskResponse> getMaybeTaskList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：查询巡视记录历史信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/loadJlyXsjl")
	public Observable<PatroalHistroyResponse> getPatroalHistroyList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：查询巡视记录 异常记录详细信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/findJlyXsjlycList")
	public Observable<PatroalExceptionListResponse> getPatroalExceptionList(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：查询需要完成该项任务的老人信息列表(备选)
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPlan2/findElderlyByGoodId")
	public Observable<GetRoomListModelTwoResponse> getMaybeTaskElderlyList(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：完成备选任务
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/addJlyPlanByBatch")
	public Observable<BaseResponseEntity> updateMaybeTask(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取所有护工信息 no-name
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/user/findUserALL")
	public Observable<GerAllUserResponse> getAllUserList(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：上传老人特殊情况信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/addJlyHljjjlExceInfo")
	public Observable<BaseResponseEntity> updateElderlyExceptionInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：本区域公共设施设备报修情况
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyBxjl")
	public Observable<BaseResponseEntity> updateAreaFcilitiesException(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：查询药品列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/findJlyYpgljlList")
	public Observable<DrugListResponse> getDrugList(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：上传血压信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyJkzbItemController/addJlyBpItem")
	public Observable<BaseResponseEntity> updateNibpInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：上传肺活量信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyJkzbItemController/addJlyPulm")
	public Observable<BaseResponseEntity> updatePulmInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：上传耳温信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyJkzbItemController/addJlyTempeerature")
	public Observable<BaseResponseEntity> updateTempInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取血压历史信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyBpItem/findJlyBpItemListM")
	public Observable<NibpHistroyResponse> getNibpHistroyInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取肺活量历史信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyPulm/findJlyPulmListM")
	public Observable<PulmHistroyResponse> getPulmHistroyInfo(@FieldMap Map<String, String> map);
	
	/**
	 * @方法描述：获取耳温历史信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("jlyTempeerature/findJlyTempeeratureListM")
	public Observable<TempHistroyResponse> getTempHistroyInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：药品管理-添加药品
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyYpgljl")
	public Observable<BaseResponseEntity> updateDrugsInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：发药管理
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/addJlyFyjl")
	public Observable<BaseResponseEntity> sendFrugsInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取有某项套餐的老人列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyElderlyController/findJlyElderlyByFy")
	public Observable<GetRoomListModelTwoResponse> getSomeElderlyList(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：获取有某项套餐的老人列表
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyElderlyController/findJlyElderlyByFy")
	public Observable<TastListResponse> getSomeElderlyList1(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：查询护工护理老人是否已经发药
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("lrglController/loadElderlyJlyFyjl")
	public Observable<TastListResponse> getElderlySendDrugInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：查询老人今天吃药信息
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/findJlyFyjlByElderly")
	public Observable<DoseListResponse> getElderlyDoseInfo(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：护工执行辅助服药
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController/updateJlyFyjl")
	public Observable<BaseResponseEntity> dose(@FieldMap Map<String, String> map);

	/**
	 * @方法描述：删除药品
	 * @param map
	 * @return
	 */
	@FormUrlEncoded
	@POST("v1/jlyTaskController//deleteLogicJlyYpgljlM")
	public Observable<BaseResponseEntity> deleteDrug(@FieldMap Map<String, String> map);
}
