package com.healthmanage.ylis.model;

public class TastPersonEntity {
	private String bedId;
	private String bedNo;
	private String elderlyId;
	private String elderlyName;
	private String state;
	private String ishous;// 48小时状态 0-需要48小时报告 1-不需要
	private String elderlySta;// 0-试入住1-已入住2-已离院 3-请假外出

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getElderlyId() {
		return elderlyId;
	}

	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getElderlyName() {
		return elderlyName;
	}

	public void setElderlyName(String elderlyName) {
		this.elderlyName = elderlyName;
	}

	public String getIshous() {
		return ishous;
	}

	public void setIshous(String ishour) {
		this.ishous = ishour;
	}

	public String getElderlySta() {
		return elderlySta;
	}

	public void setElderlySta(String elderlySta) {
		this.elderlySta = elderlySta;
	}

}
