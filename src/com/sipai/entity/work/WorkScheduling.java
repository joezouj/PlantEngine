package com.sipai.entity.work;


import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.user.User;
@Component
public class WorkScheduling extends SQLAdapter{

	private String id;

	private String userid;

	private String workstationid;

	private String taskid;

	private String workstdt;

	private String workeddt;

	private String grouptypeid;

	private String insuser;

	private String insdt;

	private String remarks;
	
	private User user;
	private String _jobname;
	private String _groupname;
	private String _username;
	private String _grouptypename;
	private String _workstationname;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getWorkstdt() {
		return workstdt;
	}

	public void setWorkstdt(String workstdt) {
		this.workstdt = workstdt;
	}

	public String getWorkeddt() {
		return workeddt;
	}

	public void setWorkeddt(String workeddt) {
		this.workeddt = workeddt;
	}

	public String getGrouptypeid() {
		return grouptypeid;
	}

	public void setGrouptypeid(String grouptypeid) {
		this.grouptypeid = grouptypeid;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String get_workstationname() {
		return _workstationname;
	}

	public void set_workstationname(String _workstationname) {
		this._workstationname = _workstationname;
	}

	public String get_grouptypename() {
		return _grouptypename;
	}

	public void set_grouptypename(String _grouptypename) {
		this._grouptypename = _grouptypename;
	}

	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public String get_jobname() {
		return _jobname;
	}

	public void set_jobname(String _jobname) {
		this._jobname = _jobname;
	}

	public String get_groupname() {
		return _groupname;
	}

	public void set_groupname(String _groupname) {
		this._groupname = _groupname;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}