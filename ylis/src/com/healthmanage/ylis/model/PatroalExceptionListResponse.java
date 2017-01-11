package com.healthmanage.ylis.model;

import java.util.List;

public class PatroalExceptionListResponse extends BaseResponseEntity{
	private List<PatroalExceptionItem> ITEMS;

	public List<PatroalExceptionItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<PatroalExceptionItem> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
