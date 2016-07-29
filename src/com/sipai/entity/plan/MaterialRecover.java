package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Workstation;

public class MaterialRecover extends SQLAdapter{
    private String id;

    private String dailyplanid;

    private String materialid;

    private String workstationid;

    private String recoverid;

    private String recovertime;

    private Double recovernum;

    private String memo;
    
    private DailyPlanSummary dailyPlanSummary;
    
    private MaterialInfo materialInfo;
    
    private Workstation workstation; 
    
    private User recover;

	public DailyPlanSummary getDailyPlanSummary() {
		return dailyPlanSummary;
	}

	public void setDailyPlanSummary(DailyPlanSummary dailyPlanSummary) {
		this.dailyPlanSummary = dailyPlanSummary;
	}

	public MaterialInfo getMaterialInfo() {
		return materialInfo;
	}

	public void setMaterialInfo(MaterialInfo materialInfo) {
		this.materialInfo = materialInfo;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

    public User getRecover() {
		return recover;
	}

	public void setRecover(User recover) {
		this.recover = recover;
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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public String getWorkstationid() {
        return workstationid;
    }

    public void setWorkstationid(String workstationid) {
        this.workstationid = workstationid;
    }

    public String getRecoverid() {
        return recoverid;
    }

    public void setRecoverid(String recoverid) {
        this.recoverid = recoverid;
    }

    public String getRecovertime() {
        return recovertime;
    }

    public void setRecovertime(String recovertime) {
        this.recovertime = recovertime;
    }

    public Double getRecovernum() {
        return recovernum;
    }

    public void setRecovernum(Double recovernum) {
        this.recovernum = recovernum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}