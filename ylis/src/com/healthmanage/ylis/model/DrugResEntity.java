package com.healthmanage.ylis.model;

/**
 * Created by YHT on 2016/12/26.
 */

public class DrugResEntity {
    DrugResEntity(){
        status="0";
    }

    private String ypId;
    private String ypmc;
    private String ypzl;
    private String ypgg;
    private String ypdw;
    private String mcyydw;
    private String mcyl;
    private String yppc;

    private String syypsl;
    private String ypwz;
    private String elderlyId;
    private String syr;
    private String byyp;
    private String byypyl;

    private String inputUser;
    private String inputTime;
    private String usrOrg;
    private String type;

    private String status;//是否显示删除按钮 0-不显示 1-显示

    public String getYpId() {
        return ypId;
    }

    public void setYpId(String ypId) {
        this.ypId = ypId;
    }

    public String getYpmc() {
        return ypmc;
    }

    public void setYpmc(String ypmc) {
        this.ypmc = ypmc;
    }

    public String getYpzl() {
        return ypzl;
    }

    public void setYpzl(String ypzl) {
        this.ypzl = ypzl;
    }

    public String getYpgg() {
        return ypgg;
    }

    public void setYpgg(String ypgg) {
        this.ypgg = ypgg;
    }

    public String getYpdw() {
        return ypdw;
    }

    public void setYpdw(String ypdw) {
        this.ypdw = ypdw;
    }

    public String getMcyydw() {
        return mcyydw;
    }

    public void setMcyydw(String mcyydw) {
        this.mcyydw = mcyydw;
    }

    public String getMcyl() {
        return mcyl;
    }

    public void setMcyl(String mcyl) {
        this.mcyl = mcyl;
    }

    public String getYppc() {
        return yppc;
    }

    public void setYppc(String yppc) {
        this.yppc = yppc;
    }

    public String getSyypsl() {
        return syypsl;
    }

    public void setSyypsl(String syypsl) {
        this.syypsl = syypsl;
    }

    public String getYpwz() {
        return ypwz;
    }

    public void setYpwz(String ypwz) {
        this.ypwz = ypwz;
    }

    public String getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(String elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }

    public String getByyp() {
        return byyp;
    }

    public void setByyp(String byyp) {
        this.byyp = byyp;
    }

    public String getByypyl() {
        return byypyl;
    }

    public void setByypyl(String byypyl) {
        this.byypyl = byypyl;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getUsrOrg() {
        return usrOrg;
    }

    public void setUsrOrg(String usrOrg) {
        this.usrOrg = usrOrg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
