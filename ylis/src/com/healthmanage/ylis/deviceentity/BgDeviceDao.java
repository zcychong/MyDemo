package com.healthmanage.ylis.deviceentity;

public class BgDeviceDao extends DeviceData{
	private String bgValue;
	private String type;
	public String getBgValue() {
		return bgValue;
	}
	public void setBgValue(String bgValue) {
		this.bgValue = bgValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
