package com.healthmanage.ylis.deviceentity;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientInfo implements Parcelable{
	private String userId;
	private String name;
	private String age;
	private String gender;
	private String userPhoto;
	private String brithday;
	private String mobilephone;
	private String groupId;
	private String imgPath;
	private String cardNo;
	private String isFinish;
	private String diaeId;
	
	public PatientInfo(){
		
	}
	public PatientInfo(String id, String username,String userage,String usergengder,
			String userPhotos,String userbrithday,String usermobilephone,
			String userGroupId,String imagPath,String idcard, String finish, String diae){
		userId = id;
		name = username;
		age = userage;
		gender = usergengder;
		userPhoto = userPhotos;
		brithday = userbrithday;
		mobilephone = usermobilephone;
		groupId = userGroupId;
		imgPath = imagPath;
		cardNo = idcard;
		isFinish = finish;
		diaeId = diae;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(userId);
		out.writeString(name);
		out.writeString(age);
		out.writeString(gender);
		out.writeString(userPhoto);
		out.writeString(brithday);
		out.writeString(mobilephone);
		out.writeString(groupId);
		out.writeString(imgPath);
		out.writeString(cardNo);
		out.writeString(isFinish);
		out.writeString(diaeId);
	}
	
	public PatientInfo(Parcel in){
		userId = in.readString();
		name = in.readString();
		age = in.readString();
		gender = in.readString();
		userPhoto = in.readString();
		brithday = in.readString();
		mobilephone = in.readString();
		groupId = in.readString();
		imgPath = in.readString();
		cardNo = in.readString();
		isFinish = in.readString();
		diaeId = in.readString();
	}
	
	public static final Parcelable.Creator<PatientInfo> CREATOR = new Creator<PatientInfo>(){
		  @Override
	        public PatientInfo[] newArray(int size)
	        {
	            return new PatientInfo[size];
	        }
	        
	        @Override
	        public PatientInfo createFromParcel(Parcel in)
	        {
	            return new PatientInfo(in);
	        }
	};
	
	public String getDiaeId() {
		return diaeId;
	}
	public void setDiaeId(String diaeId) {
		this.diaeId = diaeId;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String userName) {
		this.name = userName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gengder) {
		this.gender = gengder;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String idCard) {
		this.cardNo = idCard;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	
	
}
