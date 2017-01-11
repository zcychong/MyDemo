package com.healthmanage.ylis.model;

import java.util.List;

public class PatroalHistroyResponse extends BaseResponseEntity{
	private List<PatroalHistroyItem> ITEMS;

	public List<PatroalHistroyItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<PatroalHistroyItem> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
