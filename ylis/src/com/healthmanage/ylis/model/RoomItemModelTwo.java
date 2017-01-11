package com.healthmanage.ylis.model;

import java.util.List;

public class RoomItemModelTwo {
	private String roomId;
	private String roomNo;
	private List<PeopleItemModelTwo> beds;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roominfNo) {
		this.roomNo = roominfNo;
	}

	public List<PeopleItemModelTwo> getBeds() {
		return beds;
	}

	public void setBeds(List<PeopleItemModelTwo> beds) {
		this.beds = beds;
	}

}
