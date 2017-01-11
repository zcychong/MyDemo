package com.healthmanage.ylis.model;

import java.util.List;

public class GetLaundryListResponse extends BaseResponseEntity{

	private List<LaundryItem> ITEMS;
	public List<LaundryItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<LaundryItem> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
