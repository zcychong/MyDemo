package com.healthmanage.ylis.model;

public class LaundryItem {
	private String id;//编号
	private String sxsj;//送洗时间
	private String sxlx;//送洗类型
	private String execTime;//班次
	private String remark;//备注
	private String userId;//执行人
	private String usrOrg;//-所属机构
	private String shiftId;//交接任务单号
	private String elderlyId;//老人id
	private String name;//老人名字
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSxsj() {
		return sxsj;
	}
	public void setSxsj(String sxsj) {
		this.sxsj = sxsj;
	}
	public String getSxlx() {
		return sxlx;
	}
	public void setSxlx(String sxlx) {
		this.sxlx = sxlx;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsrOrg() {
		return usrOrg;
	}
	public void setUsrOrg(String usrOrg) {
		this.usrOrg = usrOrg;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getElderlyId() {
		return elderlyId;
	}
	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
