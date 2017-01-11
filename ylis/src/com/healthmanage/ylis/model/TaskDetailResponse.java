package com.healthmanage.ylis.model;

import java.util.List;

public class TaskDetailResponse extends BaseResponseEntity {

	private String roomName;
	private String time;
	private String bedName;
	private String peopleName;
	private String frequency;
	private List<TaskDetailEntity> ITEMS;

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBedName() {
		return bedName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public List<TaskDetailEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TaskDetailEntity> iTEMS) {
		ITEMS = iTEMS;
	}

}
