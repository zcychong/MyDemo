package com.healthmanage.ylis.model;

import java.util.List;

public class TastRoomEntity {
	private String roomId;
	private String roomNo;
	private List<TastPersonEntity> beds;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public List<TastPersonEntity> getBeds() {
		return beds;
	}

	public void setBeds(List<TastPersonEntity> bed) {
		beds = bed;
	}

}
