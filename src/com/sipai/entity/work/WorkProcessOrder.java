package com.sipai.entity.work;


import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.process.RealDetails;
@Component
public class WorkProcessOrder extends SQLAdapter{
    private String id;

	private String taskid;

	private String processRealid;

	private String processRealdetailid;

	private String insuser;

	private String insdt;

	private String updateuser;

	private String updatedt;

	private String status;
	
	private RealDetails realDetails;

	public RealDetails getRealDetails() {
		return realDetails;
	}

	public void setRealDetails(RealDetails realDetails) {
		this.realDetails = realDetails;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}