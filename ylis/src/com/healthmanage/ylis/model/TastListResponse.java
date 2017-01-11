package com.healthmanage.ylis.model;

import java.util.List;

public class TastListResponse extends BaseResponseEntity {
	private List<TastRoomEntity> ITEMS;
	private String orgName;

	public List<TastRoomEntity> getITEMS() {
		return ITEMS;
	}

	public void setITEMS(List<TastRoomEntity> iTEMS) {
		ITEMS = iTEMS;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
