package com.healthmanage.ylis.db;

import java.util.ArrayList;
import java.util.List;

import com.healthmanage.ylis.common.StringUtils;
import com.healthmanage.ylis.deviceentity.BcDeviceDao;
import com.healthmanage.ylis.deviceentity.BgDeviceDao;
import com.healthmanage.ylis.deviceentity.DeviceDao;
import com.healthmanage.ylis.deviceentity.NibpDeviceDao;
import com.healthmanage.ylis.deviceentity.PulmDeviceDao;
import com.healthmanage.ylis.deviceentity.SpoDeviceDao;
import com.healthmanage.ylis.deviceentity.TempDeviceDao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class DBManager {
	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context){
		mContext = context;
		if(helper == null){
			helper = DBOpenHelper.getInstance(context);
		}
		try{
			db = helper.getWritableDatabase();
		}catch(SQLiteException e){
			db = helper.getReadableDatabase();
		}
	}
	
	public void closeDb(){
		Log.e(TAG, "db 已关闭");
		db.close();
	}
	
	/**
	 * 添加设备
	 * @param deviceName
	 * @param deviceType
	 * @return
	 */
	public boolean addDevice(String deviceName, String deviceType){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.DEVICE_TABLE_NAME + " (device_name, device_type) VALUES(?,?) ";
			db.execSQL(sql,new String[]{deviceName, deviceType});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取设备列表
	 * @return
	 */
	public List<DeviceDao> getDeviceList(){
		List<DeviceDao> deviceList = new ArrayList<DeviceDao>();
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.DEVICE_TABLE_NAME , null);
		while(c.moveToNext()){
			DeviceDao device = new DeviceDao();
			device.setDeviceName(c.getString(c.getColumnIndex("device_name")));
			device.setDeviceType(c.getString(c.getColumnIndex("device_type")));
			deviceList.add(device);
		}
		c.close();
		
		return deviceList;
	}
	
	/**
	 * 删除设备
	 * @param name
	 */
	public void deleteDevice(String name){
		db.delete(StringUtils.DEVICE_TABLE_NAME, "device_name = ? ", new String[]{ name});
	}
	
	/**
	 * 添加一个用户
	 * @param deviceName
	 * @param deviceType
	 * @return
	 */
//	public boolean addUser(PatientInfo patientInfo){
//		db.beginTransaction();
//		try{
//			String sql = "INSERT INTO " + StringUtils.USER_TABLE_NAME + " (user_id, card_no, group_id, brithday, mobile_phone,"
//					+ "user_name, user_photo, image_path, age, gender, is_finish, diaeId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
//			db.execSQL(sql,new String[]{patientInfo.getUserId(), patientInfo.getCardNo(), patientInfo.getGroupId(),
//					patientInfo.getBrithday(), patientInfo.getMobilephone(), patientInfo.getName(), patientInfo.getUserPhoto(),
//					patientInfo.getImgPath(), patientInfo.getAge(),patientInfo.getGender(),patientInfo.getIsFinish(),
//					patientInfo.getDiaeId()});
//			db.setTransactionSuccessful();
//		}catch(SQLException e){
//			db.endTransaction();
//			return false;
//		}
//		db.endTransaction();
//		return true;
//	}
	
//	public void addUserArray(List<PatientInfo> patientInfoList){
//		for(PatientInfo patientInfo : patientInfoList){
//			addUser(patientInfo);
//		}
//	}
	
//	public void saveAllUser(GetUserListResponse response){
//		for(GroupItems groupItem : response.getITEMS()){
//			if(saveGroupInfo(groupItem)){
//				for(PatientInfo userItem : groupItem.getUserInfoL()){
//					addUser(userItem);
//				}
//			}
//		}
//	}
//	
//	public boolean saveGroupInfo(GroupItems groupItem){
//		db.beginTransaction();
//		try{
//			String sql = "INSERT INTO " + StringUtils.GROUP_TABLE_NAME + " (group_id, group_name, memo, reserved, root_group_id "
//					+ " ) VALUES(?,?,?,?,?) ";
//			db.execSQL(sql,new String[]{groupItem.getGroupId(), groupItem.getGroupName(),groupItem.getMemo(),
//					 groupItem.getReserved(), groupItem.getRootGroupId()});
//			db.setTransactionSuccessful();
//		}catch(SQLException e){
//			db.endTransaction();
//			return false;
//		}
//		db.endTransaction();
//		return true;
//	}
	
	/**
	 * 获取用户列表
	 * @return
	 */
//	public List<PatientInfo> getUserList(){
//		List<PatientInfo> userList = new ArrayList<PatientInfo>();
//		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.USER_TABLE_NAME , null);
//		while(c.moveToNext()){
//			PatientInfo userInfo = new PatientInfo();
//			userInfo.setUserId(c.getString(c.getColumnIndex("user_id")));
//			userInfo.setCardNo(c.getString(c.getColumnIndex("card_no")));
//			userInfo.setGroupId(c.getString(c.getColumnIndex("group_id")));
//			userInfo.setBrithday(c.getString(c.getColumnIndex("brithday")));
//			userInfo.setMobilephone(c.getString(c.getColumnIndex("mobile_phone")));
//			userInfo.setName(c.getString(c.getColumnIndex("user_name")));
//			userInfo.setUserPhoto(c.getString(c.getColumnIndex("user_photo")));
//			userInfo.setImgPath(c.getString(c.getColumnIndex("image_path")));
//			userInfo.setAge(c.getString(c.getColumnIndex("age")));
//			userInfo.setGender(c.getString(c.getColumnIndex("gender")));
//			userInfo.setIsFinish(c.getString(c.getColumnIndex("is_finish")));
//			userList.add(userInfo);
//		}
//		c.close();
//		return userList;
//	}
	
//	/**
//	 * 获取用户列表
//	 * @return
//	 */
//	public GetUserListResponse getUserListResponse(){
//		GetUserListResponse response =  new GetUserListResponse();
//		if(getGroupList() != null){
//			response.setITEMS(getGroupList());
//			response.setSuccess(true);
//			if(response.getITEMS().size() > 0){
//				response.setRootGroupId(response.getITEMS().get(0).getRootGroupId());
//				for(GroupItems groupItem : response.getITEMS()){
//					List<PatientInfo> userList = new ArrayList<PatientInfo>();
//					Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.USER_TABLE_NAME + 
//							" where group_id = " + groupItem.getGroupId() , null);
//					while(c.moveToNext()){
//						PatientInfo userInfo = new PatientInfo();
//						userInfo.setUserId(c.getString(c.getColumnIndex("user_id")));
//						userInfo.setCardNo(c.getString(c.getColumnIndex("card_no")));
//						userInfo.setGroupId(c.getString(c.getColumnIndex("group_id")));
//						userInfo.setBrithday(c.getString(c.getColumnIndex("brithday")));
//						userInfo.setMobilephone(c.getString(c.getColumnIndex("mobile_phone")));
//						userInfo.setName(c.getString(c.getColumnIndex("user_name")));
//						userInfo.setUserPhoto(c.getString(c.getColumnIndex("user_photo")));
//						userInfo.setImgPath(c.getString(c.getColumnIndex("image_path")));
//						userInfo.setAge(c.getString(c.getColumnIndex("age")));
//						userInfo.setGender(c.getString(c.getColumnIndex("gender")));
//						userInfo.setIsFinish(c.getString(c.getColumnIndex("is_finish")));
//						userInfo.setDiaeId(c.getString(c.getColumnIndex("diaeId")));
//						userList.add(userInfo);
//					}
//					c.close();
//					groupItem.setUserInfoL(userList);
//				}
//			}else{
//				return null;
//			}
//		}else{
//			return null;
//		}
//		return response;
//	}
	
//	public List<GroupItems> getGroupList(){
//		List<GroupItems> groupList = new ArrayList<GroupItems>();
//		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.GROUP_TABLE_NAME , null);
//		while(c.moveToNext()){
//			GroupItems userInfo = new GroupItems();
//			userInfo.setGroupId(c.getString(c.getColumnIndex("group_id")));
//			userInfo.setGroupName(c.getString(c.getColumnIndex("group_name")));
//			userInfo.setMemo(c.getString(c.getColumnIndex("memo")));
//			userInfo.setReserved(c.getString(c.getColumnIndex("reserved")));
//			userInfo.setRootGroupId(c.getString(c.getColumnIndex("root_group_id")));
//			groupList.add(userInfo);
//		}
//		c.close();
//		return groupList;
//	}
	
	
	
	/**
	 * 删除全部用户
	 */
	public void deleteAllUser(){
		db.delete(StringUtils.USER_TABLE_NAME,null,null);
	}
	
	/**
	 * 删除全部分组
	 */
	public void deleteAllGroup(){
		db.delete(StringUtils.GROUP_TABLE_NAME,null,null);
	}
	
	/**
	 * 保存血压数据
	 * @param device
	 * @return
	 */
	public boolean addNibpDate(NibpDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.NIBP_TABLE_NAME + " (user_id, heigh_value, low_value, heart_rate_number,"
					+ "heart_rate_number, is_test,time) VALUES(?,?,?,?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(), device.getHeighValue(),device.getLowValue(),
					device.getHeartRateNumber(),device.getHeartRateNumber(),device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取血压数据列表
	 * @return
	 */
	public List<NibpDeviceDao> getNibpDataList(){
		List<NibpDeviceDao> nibpDataList= new ArrayList<NibpDeviceDao>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.NIBP_TABLE_NAME , null);
		while(c.moveToNext()){
			NibpDeviceDao device = new NibpDeviceDao();
			device.setId(c.getString(c.getColumnIndex("id")));
			device.setUserId(c.getString(c.getColumnIndex("user_id")));
			device.setHeighValue(c.getString(c.getColumnIndex("heigh_value")));
			device.setLowValue(c.getString(c.getColumnIndex("low_value")));
			device.setHeartRateNumber(c.getString(c.getColumnIndex("heart_rate_number")));
			device.setIsTest(c.getString(c.getColumnIndex("is_test")));
			device.setTime(c.getString(c.getColumnIndex("time")));
			nibpDataList.add(device);
		}
		c.close();
		
		return nibpDataList;
	}
	
	/**
	 * 删除血压数据
	 * @param id
	 */
	public void deleteNibpData(String id){
		db.delete(StringUtils.NIBP_TABLE_NAME, "id = ? ", new String[]{ id});
	}
	
	/**
	 * 保存血氧数据
	 * @param device
	 * @return
	 */
	public boolean addSpoDate(SpoDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.SPO_TABLE_NAME + " (user_id, spo_value, pluse_rate,"
					+ " is_test,time) VALUES(?,?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(), device.getSpoValue(),device.getPluseRate(),
					device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取血压数据列表
	 * @return
	 */
	public List<SpoDeviceDao> getSpoDataList(){
		List<SpoDeviceDao> spoDataList= new ArrayList<SpoDeviceDao>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.SPO_TABLE_NAME , null);
		while(c.moveToNext()){
			SpoDeviceDao device = new SpoDeviceDao();
			device.setId(c.getString(c.getColumnIndex("id")));
			device.setUserId(c.getString(c.getColumnIndex("user_id")));
			device.setSpoValue(c.getString(c.getColumnIndex("spo_value")));
			device.setPluseRate(c.getString(c.getColumnIndex("pluse_rate")));
			device.setIsTest(c.getString(c.getColumnIndex("is_test")));
			device.setTime(c.getString(c.getColumnIndex("time")));
			spoDataList.add(device);
		}
		c.close();
		
		return spoDataList;
	}
	
	/**
	 * 删除血氧数据
	 * @param id
	 */
	public void deleteSpoData(String id){
		db.delete(StringUtils.SPO_TABLE_NAME, "id = ? ", new String[]{ id});
	}
	
	/**
	 * 保存血糖数据
	 * @param device
	 * @return
	 */
	public boolean addBgDate(BgDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.BG_TABLE_NAME + " (user_id, bg_value, type,"
					+ " is_test,time) VALUES(?,?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(), device.getBgValue(),device.getType(),
					device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取血糖数据列表
	 * @return
	 */
	public List<BgDeviceDao> getBgDataList(){
		List<BgDeviceDao> bgDataList= new ArrayList<BgDeviceDao>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.BG_TABLE_NAME , null);
		while(c.moveToNext()){
			BgDeviceDao device = new BgDeviceDao();
			device.setId(c.getString(c.getColumnIndex("id")));
			device.setUserId(c.getString(c.getColumnIndex("user_id")));
			device.setBgValue(c.getString(c.getColumnIndex("bg_value")));
			device.setType(c.getString(c.getColumnIndex("type")));
			device.setIsTest(c.getString(c.getColumnIndex("is_test")));
			device.setTime(c.getString(c.getColumnIndex("time")));
			bgDataList.add(device);
		}
		c.close();
		
		return bgDataList;
	}
	
	/**
	 * 删除血氧数据
	 * @param id
	 */
	public void deleteBgData(String id){
		db.delete(StringUtils.BG_TABLE_NAME, "id = ? ", new String[]{ id});
	}
	
	/**
	 * 保存肺活量数据
	 * @param device
	 * @return
	 */
	public boolean addPulmDate(PulmDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.PULM_TABLE_NAME + " (user_id, fvcVal, fevVal, pefVal,"
					+ " is_test,time) VALUES(?,?,?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(), device.getFvcVal(),device.getFevVal(),device.getPefVal(),
					device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取肺活量数据列表
	 * @return
	 */
	public List<PulmDeviceDao> getPulmDataList(){
		List<PulmDeviceDao> pulmDataList= new ArrayList<PulmDeviceDao>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.PULM_TABLE_NAME , null);
		while(c.moveToNext()){
			PulmDeviceDao device = new PulmDeviceDao();
			device.setId(c.getString(c.getColumnIndex("id")));
			device.setUserId(c.getString(c.getColumnIndex("user_id")));
			device.setFvcVal(c.getString(c.getColumnIndex("fvcVal")));
			device.setFevVal(c.getString(c.getColumnIndex("fevVal")));
			device.setPefVal(c.getString(c.getColumnIndex("pefVal")));
			device.setIsTest(c.getString(c.getColumnIndex("is_test")));
			device.setTime(c.getString(c.getColumnIndex("time")));
			pulmDataList.add(device);
		}
		c.close();
		
		return pulmDataList;
	}
	
	/**
	 * 删除肺活量数据
	 * @param id
	 */
	public void deletePulmData(String id){
		db.delete(StringUtils.PULM_TABLE_NAME, "id = ? ", new String[]{ id});
	}
	
	/**
	 * 保存尿液分析数据
	 * @param device
	 * @return
	 */
	public boolean addBcDate(BcDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.BC_TABLE_NAME + " (user_id, uroVal, bldVal, bilVal, ketVal, "
					+ "gluVal, proVal, phVal, nitVal, leuVal, vcVal, sgVal,"
					+ " is_test,time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(), device.getUroVal(),device.getBldVal(),device.getBilVal(),
					device.getKetVal(),device.getGluVal(), device.getProVal(), device.getPhVal(), device.getNitVal(),
					device.getLeuVal(), device.getVcVal(), device.getSgVal(),device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取尿液分析数据列表
	 * @return
	 */
	public List<BcDeviceDao> getBcDataList(){
		List<BcDeviceDao> bcDataList= new ArrayList<BcDeviceDao>();
		try{
			Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.BC_TABLE_NAME , null);
			if(c == null){
				Log.e(TAG, "Cursor = null");
			}
			while(c.moveToNext()){
				BcDeviceDao device = new BcDeviceDao();
				device.setId(c.getString(c.getColumnIndex("id")));
				device.setUserId(c.getString(c.getColumnIndex("user_id")));
				device.setUroVal(c.getString(c.getColumnIndex("uroVal")));
				device.setBldVal(c.getString(c.getColumnIndex("bldVal")));
				device.setBilVal(c.getString(c.getColumnIndex("bilVal")));
				device.setKetVal(c.getString(c.getColumnIndex("ketVal")));
				device.setGluVal(c.getString(c.getColumnIndex("gluVal")));
				device.setProVal(c.getString(c.getColumnIndex("proVal")));
				device.setPhVal(c.getString(c.getColumnIndex("phVal")));
				device.setNitVal(c.getString(c.getColumnIndex("nitVal")));
				device.setLeuVal(c.getString(c.getColumnIndex("leuVal")));
				device.setVcVal(c.getString(c.getColumnIndex("vcVal")));
				device.setSgVal(c.getString(c.getColumnIndex("sgVal")));
				device.setIsTest(c.getString(c.getColumnIndex("is_test")));
				device.setTime(c.getString(c.getColumnIndex("time")));
				bcDataList.add(device);
			}
			c.close();
		}catch(SQLException e){
			e.printStackTrace();
			Log.e(TAG, "getBcDataList() SQLException");
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, "getBcDataList() Exception");
		}
		if(bcDataList == null){
			Log.e(TAG, "bcDataList = null");
		}
		return bcDataList;
	}
	
	/**
	 * 删除尿液分析数据
	 * @param id
	 */
	public void deleteBcData(String id){
		db.delete(StringUtils.BC_TABLE_NAME, "id = ? ", new String[]{ id});
	}
	
	/**
	 * 保存耳温数据
	 * @param device
	 * @return
	 */
	public boolean addTempDate(TempDeviceDao device){
		db.beginTransaction();
		try{
			String sql = "INSERT INTO " + StringUtils.TEMP_TABLE_NAME + " (user_id, temperature,"
					+ " is_test,time) VALUES(?,?,?,?) ";
			Log.e("sql", sql);
			db.execSQL(sql,new String[]{device.getUserId(),device.getTemperature(),
					device.getIsTest(),device.getTime()});
			db.setTransactionSuccessful();
		}catch(SQLException e){
			db.endTransaction();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 获取耳温数据列表
	 * @return
	 */
	public List<TempDeviceDao> getTempDataList(){
		List<TempDeviceDao> tempDataList= new ArrayList<TempDeviceDao>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + StringUtils.TEMP_TABLE_NAME , null);
		while(c.moveToNext()){
			TempDeviceDao device = new TempDeviceDao();
			device.setId(c.getString(c.getColumnIndex("id")));
			device.setUserId(c.getString(c.getColumnIndex("user_id")));
			device.setTemperature(c.getString(c.getColumnIndex("temperature")));
			device.setIsTest(c.getString(c.getColumnIndex("is_test")));
			device.setTime(c.getString(c.getColumnIndex("time")));
			tempDataList.add(device);
		}
		c.close();
		
		return tempDataList;
	}
	
	/**
	 * 删除尿液分析数据
	 * @param id
	 */
	public void deleteTempData(String id){
		db.delete(StringUtils.TEMP_TABLE_NAME, "id = ? ", new String[]{ id});
	}
}
