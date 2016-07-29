package com.sipai.entity.work;

import com.sipai.entity.base.SQLAdapter;

public class UserWorkStation extends SQLAdapter{
    private String id;

	private String userid;

	private String workstationid;

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

	public String getWorkstationid() {
		return workstationid;
	}

	public void setWorkstationid(String workstationid) {
		this.workstationid = workstationid;
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