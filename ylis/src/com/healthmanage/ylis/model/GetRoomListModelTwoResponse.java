package com.healthmanage.ylis.model;

import java.util.List;

public class GetRoomListModelTwoResponse extends BaseResponseEntity {
	private List<RoomItemModelTwo> ITEMS;

	public List<RoomItemModelTwo> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<RoomItemModelTwo> iTEMS) {
		ITEMS = iTEMS;
	}

}
