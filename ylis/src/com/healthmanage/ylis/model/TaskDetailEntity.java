package com.healthmanage.ylis.model;

public class TaskDetailEntity {
	private String detiId;// 编号
	private String itemName;
	private String execTime;
	private String wczt;// 0-未完成 1-已完成 2-选中
	private String isCollect;// 0-无二级任务 1-有二级任务
	private String questNoes;// 采集问卷编号
	private String elderlyId;// 老人编号

	public String getTaskId() {
		return detiId;
	}

	public void setTaskId(String taskId) {
		this.detiId = taskId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String taskName) {
		this.itemName = taskName;
	}

	public String getWczt() {
		return wczt;
	}

	public void setWczt(String wczt) {
		this.wczt = wczt;
	}

	public String getDetiId() {
		return detiId;
	}

	public void setDetiId(String detiId) {
		this.detiId = detiId;
	}

	public String getElderlyId() {
		return elderlyId;
	}

	public void setElderlyId(String elderlyId) {
		this.elderlyId = elderlyId;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getQuestNoes() {
		return questNoes;
	}

	public void setQuestNoes(String questNoes) {
		this.questNoes = questNoes;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	
}
