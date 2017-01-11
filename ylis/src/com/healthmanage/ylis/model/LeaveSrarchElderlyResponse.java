package com.healthmanage.ylis.model;

import java.util.List;

public class LeaveSrarchElderlyResponse extends BaseResponseEntity{
	private List<LeaveElderlyEntity> ITEMS;

	public List<LeaveElderlyEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<LeaveElderlyEntity> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
