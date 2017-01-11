package com.healthmanage.ylis.model;

import java.util.List;

public class GetFrequencyResponse extends BaseResponseEntity {
	private List<FrequencyEntity> ITEMS;

	public List<FrequencyEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<FrequencyEntity> iTEMS) {
		ITEMS = iTEMS;
	}

}
