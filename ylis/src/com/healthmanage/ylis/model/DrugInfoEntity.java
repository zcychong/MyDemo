package com.healthmanage.ylis.model;

/**
 * Created by YHT on 2016/12/23.
 */

public class DrugInfoEntity {
    private DrugEntity drug;
    private DrugEntity drugSpare;

    public DrugEntity getDrug() {
        return drug;
    }

    public void setDrug(DrugEntity drug) {
        this.drug = drug;
    }

    public DrugEntity getDrugSpare() {
        return drugSpare;
    }

    public void setDrugSpare(DrugEntity drugSpare) {
        this.drugSpare = drugSpare;
    }
}
