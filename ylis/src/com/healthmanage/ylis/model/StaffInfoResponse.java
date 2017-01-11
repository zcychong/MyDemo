package com.healthmanage.ylis.model;

public class StaffInfoResponse extends BaseResponseEntity {
	private String staffName;
	private String staffGender;
	private String staffNamber;
	private String careLeave;
	private String staffLocation;
	private String staffDomicile;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffGender() {
		return staffGender;
	}

	public void setStaffGender(String staffGender) {
		this.staffGender = staffGender;
	}

	public String getStaffNamber() {
		return staffNamber;
	}

	public void setStaffNamber(String staffNamber) {
		this.staffNamber = staffNamber;
	}

	public String getCareLeave() {
		return careLeave;
	}

	public void setCareLeave(String careLeave) {
		this.careLeave = careLeave;
	}

	public String getStaffLocation() {
		return staffLocation;
	}

	public void setStaffLocation(String staffLocation) {
		this.staffLocation = staffLocation;
	}

	public String getStaffDomicile() {
		return staffDomicile;
	}

	public void setStaffDomicile(String staffDomicile) {
		this.staffDomicile = staffDomicile;
	}

}
