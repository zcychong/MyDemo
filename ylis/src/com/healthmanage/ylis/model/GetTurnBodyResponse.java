package com.healthmanage.ylis.model;

import java.util.List;

public class GetTurnBodyResponse extends BaseResponseEntity{
	private List<TurnBodyItem> ITEMS;

	public List<TurnBodyItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TurnBodyItem> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
