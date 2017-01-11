package com.healthmanage.ylis.model;

import java.util.List;

public class TaskConnectResponse extends BaseResponseEntity {
	private List<TaskConnectDetailEntity> ITEMS;

	public List<TaskConnectDetailEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TaskConnectDetailEntity> iTEMS) {
		ITEMS = iTEMS;
	}

}
