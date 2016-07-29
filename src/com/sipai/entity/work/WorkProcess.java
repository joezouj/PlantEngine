package com.sipai.entity.work;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;

@Component
public class WorkProcess extends SQLAdapter{
    private String id;

	private String taskid;

	private String processRealid;

	private String processRealdetailid;

	private String workstationid;

	private String userid;

	private String stdt;

	private String edt;

	private String remarks;

	private String insuser;

	private String insdt;

	private String updateuser;

	private String updatedt;

	private String processorderid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getProcessRealid() {
		return processRealid;
	}

	public void setProcessRealid(String processRealid) {
		this.processRealid = processRealid;
	}

	public String getProcessRealdetailid() {
		return processRealdetailid;
	}

	public void setProcessRealdetailid(String processRealdetailid) {
		this.processRealdetailid = processRealdetailid;
	}

	public String getWorkstationid() {
		return workstationid;
	}

	public void setWorkstationid(String workstationid) {
		this.workstationid = workstationid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getStdt() {
		return stdt;
	}

	public void setStdt(String stdt) {
		this.stdt = stdt;
	}

	public String getEdt() {
		return edt;
	}

	public void setEdt(String edt) {
		this.edt = edt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getProcessorderid() {
		return processorderid;
	}

	public void setProcessorderid(String processorderid) {
		this.processorderid = processorderid;
	}
}