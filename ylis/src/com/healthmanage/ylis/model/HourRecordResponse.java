package com.healthmanage.ylis.model;

import java.util.List;

public class HourRecordResponse extends BaseResponseEntity {
	private String mainId;
	private List<HourQuestionItem> ITEMS;

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public List<HourQuestionItem> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<HourQuestionItem> iTEMS) {
		ITEMS = iTEMS;
	}

}
