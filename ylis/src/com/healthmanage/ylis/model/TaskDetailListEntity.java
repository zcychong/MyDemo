package com.healthmanage.ylis.model;

import java.util.List;

public class TaskDetailListEntity {
	private String execTime;
	private List<TaskDetailEntity> taskList;
	
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public List<TaskDetailEntity> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<TaskDetailEntity> taskList) {
		this.taskList = taskList;
	}
	
}
