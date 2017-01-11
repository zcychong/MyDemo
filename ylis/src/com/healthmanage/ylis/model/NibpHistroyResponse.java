package com.healthmanage.ylis.model;

import java.util.List;

public class NibpHistroyResponse extends BaseResponseEntity{
	private List<NibpEntity> ITEMS;

	public List<NibpEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<NibpEntity> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
