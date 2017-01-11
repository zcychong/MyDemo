package com.healthmanage.ylis.deviceentity;

public class DeviceData {
	public String id;
	public String userId;
	public String isTest;
	public String time;
	public String elderlyId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIsTest() {
		return isTest;
	}
	public void setIsTest(String isTest) {
		this.isTest = isTest;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getElderlyId() {
		return elderlyId;
	}

	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}
}
