package com.healthmanage.ylis.model;

public class DoseItem {
	private String itemId;
	private String medicineName;
	private String time;
	private String medicineInit;
	private String peopleInfo;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMedicineInit() {
		return medicineInit;
	}

	public void setMedicineInit(String medicineInit) {
		this.medicineInit = medicineInit;
	}

	public String getPeopleInfo() {
		return peopleInfo;
	}

	public void setPeopleInfo(String peopleInfo) {
		this.peopleInfo = peopleInfo;
	}

}
