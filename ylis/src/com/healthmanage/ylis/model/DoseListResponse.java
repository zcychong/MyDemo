package com.healthmanage.ylis.model;

import java.util.List;

/**
 * Created by YHT on 2017/1/4.
 */

public class DoseListResponse extends BaseResponseEntity {
    private List<DoseEntity> ITEMS;

    public List<DoseEntity> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<DoseEntity> ITEMS) {
        this.ITEMS = ITEMS;
    }
}
