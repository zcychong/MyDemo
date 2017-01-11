package com.healthmanage.ylis.model;

/**
 * Created by ZCY on 2016/12/16.
 */

public class SimpleEntity {
    private String strName;
    private String state;

    public SimpleEntity(String name){
        strName = name;
        state = "0";
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
