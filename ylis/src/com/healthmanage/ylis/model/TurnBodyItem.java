package com.healthmanage.ylis.model;

public class TurnBodyItem {
	private String id;//编号
	private String elderlyId;//老人编号
	private String fssj;//翻身时间
	private String wowei;//卧位
	private String pfqk;//皮肤情况
	private String lrfy;//处理结果
	private String execTime;//班次
	private String clfs;//处理方式
	private String remark;//备注
	private String status;//状态 0-正常 1-异常
	private String name;//老人姓名
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getElderlyId() {
		return elderlyId;
	}
	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}
	public String getFssj() {
		return fssj;
	}
	public void setFssj(String fssj) {
		this.fssj = fssj;
	}
	public String getWowei() {
		return wowei;
	}
	public void setWowei(String wowei) {
		this.wowei = wowei;
	}
	public String getPfqk() {
		return pfqk;
	}
	public void setPfqk(String pfqk) {
		this.pfqk = pfqk;
	}
	public String getLrfy() {
		return lrfy;
	}
	public void setLrfy(String lrfy) {
		this.lrfy = lrfy;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClfs() {
		return clfs;
	}
	public void setClfs(String clfs) {
		this.clfs = clfs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
