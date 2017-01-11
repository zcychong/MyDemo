package com.healthmanage.ylis.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YHT on 2016/12/23.
 */

public class DrugEntity implements Parcelable{
    private String drugId;
    private String drugName;
    private String drugTime;
    private String drugSumUnit;
    private String drugSum;
    private String drugGG;
    private String drugUseQuantity;
    private String drugUseQuantityUnit;
    private String drugFrequency;
    private String drugUsage;
    private int drugSpare;

    public DrugEntity(){

    }

    public DrugEntity(Parcel in) {
        drugId = in.readString();
        drugName = in.readString();
        drugTime = in.readString();
        drugSumUnit = in.readString();
        drugSum = in.readString();
        drugGG = in.readString();
        drugUseQuantity = in.readString();
        drugUseQuantityUnit = in.readString();
        drugFrequency = in.readString();
        drugUsage = in.readString();
        drugSpare= in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(drugId);
        out.writeString(drugName);
        out.writeString(drugTime);
        out.writeString(drugSumUnit);
        out.writeString(drugSum);
        out.writeString(drugGG);
        out.writeString(drugUseQuantity);
        out.writeString(drugUseQuantityUnit);
        out.writeString(drugFrequency);
        out.writeString(drugUsage);
        out.writeInt(drugSpare);
    }

    public static final Creator<DrugEntity> CREATOR = new Creator<DrugEntity>() {
        @Override
        public DrugEntity createFromParcel(Parcel in) {
            return new DrugEntity(in);
        }

        @Override
        public DrugEntity[] newArray(int size) {
            return new DrugEntity[size];
        }
    };

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugTime() {
        return drugTime;
    }

    public void setDrugTime(String drugTime) {
        this.drugTime = drugTime;
    }

    public String getDrugSumUnit() {
        return drugSumUnit;
    }

    public void setDrugSumUnit(String drugSumUnit) {
        this.drugSumUnit = drugSumUnit;
    }

    public String getDrugSum() {
        return drugSum;
    }

    public void setDrugSum(String drugSum) {
        this.drugSum = drugSum;
    }

    public String getDrugGG() {
        return drugGG;
    }

    public void setDrugGG(String drugGG) {
        this.drugGG = drugGG;
    }

    public String getDrugUseQuantity() {
        return drugUseQuantity;
    }

    public void setDrugUseQuantity(String drugUseQuantity) {
        this.drugUseQuantity = drugUseQuantity;
    }

    public String getDrugUseQuantityUnit() {
        return drugUseQuantityUnit;
    }

    public void setDrugUseQuantityUnit(String drugUseQuantityUnit) {
        this.drugUseQuantityUnit = drugUseQuantityUnit;
    }

    public String getDrugFrequency() {
        return drugFrequency;
    }

    public void setDrugFrequency(String drugFrequency) {
        this.drugFrequency = drugFrequency;
    }

    public String getDrugUsage() {
        return drugUsage;
    }

    public void setDrugUsage(String drugUsage) {
        this.drugUsage = drugUsage;
    }

    public int getDrugSpare() {
        return drugSpare;
    }

    public void setDrugSpare(int drugSpare) {
        this.drugSpare = drugSpare;
    }
}
