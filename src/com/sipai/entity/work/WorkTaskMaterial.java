package com.sipai.entity.work;


import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;
@Component
public class WorkTaskMaterial extends SQLAdapter{
    private String id;

	private String materialid;

	private String amount;

	private String workstationid;

	private String taskid;
	
	private String processrealdetailid;

	private String insuser;

	private String insdt;

	private String batchingflag;

	private String arrivalamount;

	private String batchingamount;

	private String buser;

	private String bdt;

	private MaterialInfo materialinfo;
	private Workstation workstation;
    private boolean _checked;
    private String _deptid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaterialid() {
		return materialid;
	}

	public void setMaterialid(String materialid) {
		this.materialid = materialid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getProcessrealdetailid() {
		return processrealdetailid;
	}

	public void setProcessrealdetailid(String processrealdetailid) {
		this.processrealdetailid = processrealdetailid;
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

	public String getBatchingflag() {
		return batchingflag;
	}

	public void setBatchingflag(String batchingflag) {
		this.batchingflag = batchingflag;
	}

	public String getArrivalamount() {
		return arrivalamount;
	}

	public void setArrivalamount(String arrivalamount) {
		this.arrivalamount = arrivalamount;
	}

	public String getBatchingamount() {
		return batchingamount;
	}

	public void setBatchingamount(String batchingamount) {
		this.batchingamount = batchingamount;
	}

	public String getBuser() {
		return buser;
	}

	public void setBuser(String buser) {
		this.buser = buser;
	}

	public String getBdt() {
		return bdt;
	}

	public void setBdt(String bdt) {
		this.bdt = bdt;
	}


	public String get_deptid() {
		return _deptid;
	}

	public void set_deptid(String _deptid) {
		this._deptid = _deptid;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

    
    public MaterialInfo getMaterialinfo() {
		return materialinfo;
	}

	public void setMaterialinfo(MaterialInfo materialinfo) {
		this.materialinfo = materialinfo;
	}

	public boolean is_checked() {
		return _checked;
	}

	public void set_checked(boolean _checked) {
		this._checked = _checked;
	}

}