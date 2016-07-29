package com.sipai.entity.user;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class Dept extends SQLAdapter{
    private String id;

    private String name;

    private String pid;

    private String roleid;

    private String offtel;

    private String telout;

    private String telin;

    private String fax;

    private String office;

    private String logopic;

    private String taskid;

    private Date insdt;

    private String insuser;

    private Integer version;

    private Integer morder;

    private String sname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getOfftel() {
        return offtel;
    }

    public void setOfftel(String offtel) {
        this.offtel = offtel;
    }

    public String getTelout() {
        return telout;
    }

    public void setTelout(String telout) {
        this.telout = telout;
    }

    public String getTelin() {
        return telin;
    }

    public void setTelin(String telin) {
        this.telin = telin;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Date getInsdt() {
        return insdt;
    }

    public void setInsdt(Date insdt) {
        this.insdt = insdt;
    }

    public String getInsuser() {
        return insuser;
    }

    public void setInsuser(String insuser) {
        this.insuser = insuser;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getMorder() {
        return morder;
    }

    public void setMorder(Integer morder) {
        this.morder = morder;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}