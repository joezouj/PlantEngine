package com.sipai.entity.document;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class Doctype extends SQLAdapter{
    private String id;
	private String name;
	private String pid;
	private Integer level;
	private String insuser;
	private Date insdt;
	private String updateuser;
	private Date updatedt;


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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
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
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public Date getUpdatedt() {
		return updatedt;
	}
	public void setUpdatedt(Date updatedt) {
		this.updatedt = updatedt;
	}


	
	
}