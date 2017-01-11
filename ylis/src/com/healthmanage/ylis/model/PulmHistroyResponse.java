package com.healthmanage.ylis.model;

import java.util.List;

public class PulmHistroyResponse extends BaseResponseEntity{
	private List<PulmEntity> ITEMS;

	public List<PulmEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<PulmEntity> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
