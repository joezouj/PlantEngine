package com.sipai.entity.msg;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class EmppSend extends SQLAdapter{
    private String id;

    private String emppadminid;

    private String senduserid;

    private Date senddate;

    private String insertuserid;

    private Date insertdate;

    private String updateuserid;

    private Date updatedate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmppadminid() {
        return emppadminid;
    }

    public void setEmppadminid(String emppadminid) {
        this.emppadminid = emppadminid;
    }

    public String getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(String senduserid) {
        this.senduserid = senduserid;
    }

    public Date getSenddate() {
        return senddate;
    }

    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }

    public String getInsertuserid() {
        return insertuserid;
    }

    public void setInsertuserid(String insertuserid) {
        this.insertuserid = insertuserid;
    }

    public Date getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(Date insertdate) {
        this.insertdate = insertdate;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public void setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
}