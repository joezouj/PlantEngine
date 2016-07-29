package com.sipai.entity.plan;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class DailyPlanSummaryMaterial extends SQLAdapter{
    private String id;

    private String planid;

    private String workstationid;

    private String materialid;

    private Double amount;

    private Double revokeamount;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getWorkstationid() {
        return workstationid;
    }

    public void setWorkstationid(String workstationid) {
        this.workstationid = workstationid;
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRevokeamount() {
        return revokeamount;
    }

    public void setRevokeamount(Double revokeamount) {
        this.revokeamount = revokeamount;
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



    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

	public String getInsdt() {
		return insdt;
	}

	public void setInsdt(String insdt) {
		this.insdt = insdt;
	}

	public String getModifydt() {
		return modifydt;
	}

	public void setModifydt(String modifydt) {
		this.modifydt = modifydt;
	}


}