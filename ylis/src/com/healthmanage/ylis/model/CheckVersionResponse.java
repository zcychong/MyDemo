package com.healthmanage.ylis.model;

public class CheckVersionResponse extends BaseResponseEntity{
	private int versionCode;
	private String apkUrl;
	private String versionName;
	private String versionLog;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionLog() {
		return versionLog;
	}
	public void setVersionLog(String versionLog) {
		this.versionLog = versionLog;
	}
	
	
}
