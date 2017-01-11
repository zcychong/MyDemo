package com.healthmanage.ylis.model;

import java.util.List;

/**
 * Created by YHT on 2016/12/19.
 */

public class GerAllUserResponse extends BaseResponseEntity{
    private List<UserInfoEntity> ITEMS;

    public List<UserInfoEntity> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<UserInfoEntity> ITEMS) {
        this.ITEMS = ITEMS;
    }
}
