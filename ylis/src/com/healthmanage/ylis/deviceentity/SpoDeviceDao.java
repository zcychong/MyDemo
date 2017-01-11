package com.healthmanage.ylis.deviceentity;

public class SpoDeviceDao extends DeviceData{
	private String spoValue;
	private String pluseRate;
	public String getUserId() {
		return super.userId;
	}
	public void setUserId(String userId) {
		super.userId = userId;
	}
	public String getSpoValue() {
		return spoValue;
	}
	public void setSpoValue(String spoValue) {
		this.spoValue = spoValue;
	}
	public String getPluseRate() {
		return pluseRate;
	}
	public void setPluseRate(String pluseRate) {
		this.pluseRate = pluseRate;
	}
	public String getIsTest() {
		return super.isTest;
	}
	public void setIsTest(String isTest) {
		super.isTest = isTest;
	}
	public String getTime() {
		return super.time;
	}
	public void setTime(String time) {
		super.time = time;
	}
	
	
}
