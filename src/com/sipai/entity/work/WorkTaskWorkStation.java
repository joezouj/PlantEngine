package com.sipai.entity.work;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.user.User;

import org.springframework.stereotype.Component;
@Component
public class WorkTaskWorkStation extends SQLAdapter{
    private String id;

	private String workstationid;

	private String workstationname;
	
	private String deptid;
	
	private String deptname;
	
	private String lineid;
	
	private String linename;

	private String processRealid;

	private String processRealdetailid;

	private String taskid;

	private String insuser;

	private String insdt;
	
	private String _realdetailid;
    
    private boolean _checked;//选中标志
    
    private String _processdetailname;//工序名称
    
    private String _processname;//工艺�?
    
    private Workstation  workstation;
    
    private String _taskstatue;//记录员工在该工位相应任务的执行情况，0为未开始，1为开始 2结束
    
    private User user;
    
    private String _taskstdt;
    
    private String _taskedt;
    
    private String workstationserial;
    
    private String wftaskname;//snaker中的taskname
    
	public String get_taskstdt() {
		return _taskstdt;
	}

	public void set_taskstdt(String _taskstdt) {
		this._taskstdt = _taskstdt;
	}

	public String get_taskedt() {
		return _taskedt;
	}

	public void set_taskedt(String _taskedt) {
		this._taskedt = _taskedt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkstationid() {
		return workstationid;
	}

	public void setWorkstationid(String workstationid) {
		this.workstationid = workstationid;
	}

	public String getWorkstationname() {
		return workstationname;
	}

	public void setWorkstationname(String workstationname) {
		this.workstationname = workstationname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
    
    public String get_taskstatue() {
		return _taskstatue;
	}

	public void set_taskstatue(String _taskstatue) {
		this._taskstatue = _taskstatue;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

	public String get_processdetailname() {
		return _processdetailname;
	}

	public void set_processdetailname(String _processdetailname) {
		this._processdetailname = _processdetailname;
	}

	public String get_processname() {
		return _processname;
	}

	public void set_processname(String _processname) {
		this._processname = _processname;
	}

	public boolean is_checked() {
		return _checked;
	}

	public void set_checked(boolean _checked) {
		this._checked = _checked;
	}

	public String get_realdetailid() {
		return _realdetailid;
	}

	public void set_realdetailid(String _realdetailid) {
		this._realdetailid = _realdetailid;
	}

	public String getWorkstationserial() {
		return workstationserial;
	}

	public void setWorkstationserial(String workstationserial) {
		this.workstationserial = workstationserial;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getWftaskname() {
		return wftaskname;
	}

	public void setWftaskname(String wftaskname) {
		this.wftaskname = wftaskname;
	}

}