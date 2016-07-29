package com.sipai.entity.msg;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class EmppAdmin extends SQLAdapter{
    private String id;

    private String emppname;

    private String host;

    private Integer port;

    private String accountid;

    private String accname;

    private String password;

    private String serviceid;

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

    public String getEmppname() {
        return emppname;
    }

    public void setEmppname(String emppname) {
        this.emppname = emppname;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getAccname() {
        return accname;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
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