package com.healthmanage.ylis.model;

import java.util.List;

public class TempHistroyResponse extends BaseResponseEntity{
	private List<TempEntity> ITEMS;

	public List<TempEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TempEntity> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
