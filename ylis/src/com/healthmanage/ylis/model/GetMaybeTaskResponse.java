package com.healthmanage.ylis.model;

import java.util.List;

public class GetMaybeTaskResponse extends BaseResponseEntity{
	private List<TaskMaybeModelTwo> ITEMS;

	public List<TaskMaybeModelTwo> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TaskMaybeModelTwo> iTEMS) {
		ITEMS = iTEMS;
	}
	
	
}
