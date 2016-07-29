package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;


public class DailyPlanSummary extends SQLAdapter{

	private String id;

    private String insuser;

    private String insdt;

    private String plandt;

    private String auditor;

    private String auditdate;

    private String status;
    
    private String modifier;

	private String modifydt;
    private String name;
    private String remark;
    private String _modifiername;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPlandt() {
        return plandt;
    }

    public void setPlandt(String plandt) {
        this.plandt = plandt;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditdate() {
        return auditdate;
    }

    public void setAuditdate(String auditdate) {
        this.auditdate = auditdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifydt() {
		return modifydt;
	}

	public void setModifydt(String modifydt) {
		this.modifydt = modifydt;
	}

	public String get_modifiername() {
		return _modifiername;
	}

	public void set_modifiername(String _modifiername) {
		this._modifiername = _modifiername;
	}
}