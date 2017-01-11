package com.healthmanage.ylis.model;

public class PeopleItemModelTwo {
	private String detiId;
	private String bedId;
	private String bedNo;
//	private String bedinfNo;
	private String elderlyId;
	private String elderlyName;
	private String wczt;
	private String elderlySta;// // 0-试入住1-已入住2-已离院 3-请假外出

	public String getDetiId() {
		return detiId;
	}

	public void setDetiId(String detiId) {
		this.detiId = detiId;
	}

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

//	public String getBedinfNo() {
//		return bedinfNo;
//	}
//
//	public void setBedinfNo(String bedNo) {
//		this.bedinfNo = bedNo;
//	}

	public String getElderlyId() {
		return elderlyId;
	}

	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}

	public String getElderlyName() {
		return elderlyName;
	}

	public void setElderlyName(String elderlyName) {
		this.elderlyName = elderlyName;
	}

	public String getElderlySta() {
		return elderlySta;
	}

	public void setElderlySta(String elderlySta) {
		this.elderlySta = elderlySta;
	}

	public String getWczt() {
		return wczt;
	}

	public void setWczt(String wczt) {
		this.wczt = wczt;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
}
