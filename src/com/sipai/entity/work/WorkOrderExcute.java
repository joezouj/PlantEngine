package com.sipai.entity.work;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;

@Component
public class WorkOrderExcute extends SQLAdapter{
	private String id;

	private String wftaskid;

	private String insuser;

	private String insdt;

	private String updateuser;

	private String updatedt;

	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWftaskid() {
		return wftaskid;
	}

	public void setWftaskid(String wftaskid) {
		this.wftaskid = wftaskid;
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

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public String getUpdatedt() {
		return updatedt;
	}

	public void setUpdatedt(String updatedt) {
		this.updatedt = updatedt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}