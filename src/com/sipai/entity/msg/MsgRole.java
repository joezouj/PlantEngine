package com.sipai.entity.msg;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class MsgRole extends SQLAdapter{
    private String id;

    private String masterid;

    private String roleid;

    private String insuser;

    private Date insdt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

  

    public String getInsuser() {
        return insuser;
    }

    public void setInsuser(String insuser) {
        this.insuser = insuser;
    }

    public Date getInsdt() {
        return insdt;
    }

    public void setInsdt(Date insdt) {
        this.insdt = insdt;
    }

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
}