package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.work.Workstation;

public class MaterialDeliver extends SQLAdapter{
    private String id;

    private String dailyplanid;

    private String workstationid;

    private String starttime;

    private String endtime;

    private String status;

    private String remark;
    
    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private DailyPlanSummary dailyPlanSummary;
    
    private Workstation workstation;

    public DailyPlanSummary getDailyPlanSummary() {
		return dailyPlanSummary;
	}

	public void setDailyPlanSummary(DailyPlanSummary dailyPlanSummary) {
		this.dailyPlanSummary = dailyPlanSummary;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDailyplanid() {
        return dailyplanid;
    }

    public void setDailyplanid(String dailyplanid) {
        this.dailyplanid = dailyplanid;
    }

    public String getWorkstationid() {
        return workstationid;
    }

    public void setWorkstationid(String workstationid) {
        this.workstationid = workstationid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

	public String getModify() {
		return modify;
	}

	public void setModify(String modify) {
		this.modify = modify;
	}

	public String getModifydt() {
		return modifydt;
	}

	public void setModifydt(String modifydt) {
		this.modifydt = modifydt;
	}
}