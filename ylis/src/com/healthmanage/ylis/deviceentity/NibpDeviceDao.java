package com.healthmanage.ylis.deviceentity;

public class NibpDeviceDao extends DeviceData{
	private String heighValue;
	private String lowValue;
	private String heartRateNumber;
	public String getUserId() {
		return super.userId;
	}
	public void setUserId(String userId) {
		super.userId = userId;
	}
	public String getHeighValue() {
		return heighValue;
	}
	public void setHeighValue(String heighValue) {
		this.heighValue = heighValue;
	}
	public String getLowValue() {
		return lowValue;
	}
	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
	}
	public String getHeartRateNumber() {
		return heartRateNumber;
	}
	public void setHeartRateNumber(String heartRateNumber) {
		this.heartRateNumber = heartRateNumber;
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

