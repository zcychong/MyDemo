package com.healthmanage.ylis.model;

import java.util.List;

public class GetTaskListModelTwoLResponse extends BaseResponseEntity {
	private List<TaskItemModelTwo> ITEMS;

	public List<TaskItemModelTwo> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TaskItemModelTwo> iTEMS) {
		ITEMS = iTEMS;
	}

}
