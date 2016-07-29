package com.sipai.entity.user;

import com.sipai.entity.base.SQLAdapter;

public class UserTime extends SQLAdapter{
    private String id;

    private String userid;

    private String ip;

    private String sdate;

    private String edate;

    private Double logintime;
    
    private String insuser;
    
    private String insdt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public Double getLogintime() {
        return logintime;
    }

    public void setLogintime(Double logintime) {
        this.logintime = logintime;
    }

	public String getInsuser() {
		return insuser;
	}

	public void setInsuser(String insuser) {
		this.insuser = insuser;
	}

	public String getInsdt() {
		return insdt;
	}

	public void setInsdt(String insdt) {
		this.insdt = insdt;
	}
    
    
}