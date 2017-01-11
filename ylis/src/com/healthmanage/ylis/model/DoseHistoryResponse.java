package com.healthmanage.ylis.model;

import java.util.List;

public class DoseHistoryResponse extends BaseResponseEntity {
	private List<DoseItem> ITEMS;

	public List<DoseItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<DoseItem> iTEMS) {
		ITEMS = iTEMS;
	}

}
