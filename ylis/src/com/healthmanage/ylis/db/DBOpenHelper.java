package com.healthmanage.ylis.db;


import com.healthmanage.ylis.common.StringUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private final String TAG = getClass().getSimpleName();
	private static final String DB_NAME = "ecare_monitor_db"; 
	private static final int DB_VERSION = 2;
	private static DBOpenHelper instance;
	
	
	private final String CREATE_DEVICE_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.DEVICE_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, device_name VARCHAR, device_type VARCHAR) ";
	
	private final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.USER_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, card_no VARCHAR, group_id VARCHAR, brithday VARCHAR,"
			+ " mobile_phone VARCHAR, user_name VARCHAR, user_photo VARCHAR, image_path VARCHAR, age VARCHAR,"
			+ " gender VARCHAR, is_finish VARCHAR, diaeId VARCHAR) ";
	
	private final String CREATE_NIBP_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.NIBP_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, heigh_value VARCHAR, low_value VARCHAR, heart_rate_number VARCHAR,"
			+ " is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_SPO_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.SPO_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, spo_value VARCHAR, pluse_rate VARCHAR, "
			+ "is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_BG_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.BG_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, bg_value VARCHAR, type VARCHAR, "
			+ "is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_PULM_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.PULM_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, fvcVal VARCHAR, fevVal VARCHAR, "
			+ "pefVal VARCHAR, is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_BC_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.BC_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, uroVal VARCHAR, bldVal VARCHAR, "
			+ "bilVal VARCHAR, ketVal VARCHAR, gluVal VARCHAR, proVal VARCHAR, phVal VARCHAR,"
			+ "nitVal VARCHAR, leuVal VARCHAR, vcVal VARCHAR, sgVal VARCHAR,"
			+ "is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.TEMP_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, user_id VARCHAR, temperature VARCHAR, "
			+ "is_test VARCHAR, time VARCHAR) ";
	
	private final String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + StringUtils.GROUP_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY, group_id VARCHAR, group_name VARCHAR, "
			+ "memo VARCHAR, reserved VARCHAR, root_group_id VARCHAR) ";
	
	private final String ALTER_USER_TABL = " ALTER TABLE " + StringUtils.USER_TABLE_NAME + " ADD COLUMN "
			+ " diaeId VARCHAR ";
	
	
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static synchronized DBOpenHelper getInstance(Context context){
		if(instance == null){
			instance = new DBOpenHelper(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DEVICE_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_NIBP_TABLE);
		db.execSQL(CREATE_SPO_TABLE);
		db.execSQL(CREATE_BG_TABLE);
		db.execSQL(CREATE_PULM_TABLE);
		db.execSQL(CREATE_BC_TABLE);
		db.execSQL(CREATE_TEMP_TABLE);
		db.execSQL(CREATE_GROUP_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1 && newVersion == 2){
			Log.e("onUpgrade", "更新版本");
			db.execSQL(CREATE_GROUP_TABLE);
			db.execSQL(ALTER_USER_TABL);
		}
		
		
	}

}
