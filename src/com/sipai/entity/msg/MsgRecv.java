package com.sipai.entity.msg;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class MsgRecv extends SQLAdapter{
   
	private String id;

    private String masterid;

    private String unitid;

    private Date readtime;

    private String status;

    private String delflag;

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

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public Date getReadtime() {
		return readtime;
	}

	public void setReadtime(Date readtime) {
		this.readtime = readtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelflag() {
		return delflag;
	}

	public void setDelflag(String delflag) {
		this.delflag = delflag;
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

}