package com.sipai.entity.msg;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class EmppSendUser extends SQLAdapter{
    private String id;

    private String emppsendid;

    private String senduserid;

    private Date senddate;

    private String recuserid;

    private String mobile;

    private Date backdate;

    private String insertuserid;

    private Date insertdate;

    private String updateuserid;

    private Date updatedate;

    private String msgid;
    

    private String recusername;

    private String sendtitle;

   
    public String getRecusername() {
        return recusername;
    }

    public void setRecusername(String recusername) {
        this.recusername = recusername;
    }

    public String getSendtitle() {
        return sendtitle;
    }

    public void setSendtitle(String sendtitle) {
        this.sendtitle = sendtitle;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmppsendid() {
        return emppsendid;
    }

    public void setEmppsendid(String emppsendid) {
        this.emppsendid = emppsendid;
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

    public String getRecuserid() {
        return recuserid;
    }

    public void setRecuserid(String recuserid) {
        this.recuserid = recuserid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBackdate() {
        return backdate;
    }

    public void setBackdate(Date backdate) {
        this.backdate = backdate;
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

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}