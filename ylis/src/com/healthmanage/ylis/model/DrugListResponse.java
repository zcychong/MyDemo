package com.healthmanage.ylis.model;

import java.util.List;

/**
 * Created by YHT on 2016/12/26.
 */

public class DrugListResponse extends BaseResponseEntity {
    private List<DrugResEntity> ITEMS;

    public List<DrugResEntity> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<DrugResEntity> ITEMS) {
        this.ITEMS = ITEMS;
    }
}
