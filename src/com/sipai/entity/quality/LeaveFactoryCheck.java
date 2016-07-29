package com.sipai.entity.quality;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class LeaveFactoryCheck extends SQLAdapter{
    private String id;

    private String insuser;

    private String insdt;

    private String equipmentname;

    private String ratedvoltage;

    private String ratedcurrent;

    private String installationtype;

    private String releasemodel;

    private String appendix;

    private String poweroperationv;

    private String shuntreleasev;

    private String closingemv;

    private String undervreleasev;

    private String remarkoverall;

    private String remarktable;

    private String inspectdate;

    private String inspector;
    
    private String leavefactoryno;
    
    private String phasea;
    
    private String phaseb;
    
    private String phasec;

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


    public String getEquipmentname() {
        return equipmentname;
    }

    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }

    public String getRatedvoltage() {
        return ratedvoltage;
    }

    public void setRatedvoltage(String ratedvoltage) {
        this.ratedvoltage = ratedvoltage;
    }

    public String getRatedcurrent() {
        return ratedcurrent;
    }

    public void setRatedcurrent(String ratedcurrent) {
        this.ratedcurrent = ratedcurrent;
    }

    public String getInstallationtype() {
        return installationtype;
    }

    public void setInstallationtype(String installationtype) {
        this.installationtype = installationtype;
    }

    public String getReleasemodel() {
        return releasemodel;
    }

    public void setReleasemodel(String releasemodel) {
        this.releasemodel = releasemodel;
    }

    public String getAppendix() {
        return appendix;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix;
    }

    public String getPoweroperationv() {
        return poweroperationv;
    }

    public void setPoweroperationv(String poweroperationv) {
        this.poweroperationv = poweroperationv;
    }

    public String getShuntreleasev() {
        return shuntreleasev;
    }

    public void setShuntreleasev(String shuntreleasev) {
        this.shuntreleasev = shuntreleasev;
    }

    public String getClosingemv() {
        return closingemv;
    }

    public void setClosingemv(String closingemv) {
        this.closingemv = closingemv;
    }

    public String getUndervreleasev() {
        return undervreleasev;
    }

    public void setUndervreleasev(String undervreleasev) {
        this.undervreleasev = undervreleasev;
    }

    public String getRemarkoverall() {
        return remarkoverall;
    }

    public void setRemarkoverall(String remarkoverall) {
        this.remarkoverall = remarkoverall;
    }

    public String getRemarktable() {
        return remarktable;
    }

    public void setRemarktable(String remarktable) {
        this.remarktable = remarktable;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

	public String getInsdt() {
		return insdt;
	}

	public void setInsdt(String insdt) {
		this.insdt = insdt;
	}

	public String getInspectdate() {
		return inspectdate;
	}

	public void setInspectdate(String inspectdate) {
		this.inspectdate = inspectdate;
	}

	public String getLeavefactoryno() {
		return leavefactoryno;
	}

	public void setLeavefactoryno(String leavefactoryno) {
		this.leavefactoryno = leavefactoryno;
	}

	public String getPhasea() {
		return phasea;
	}

	public void setPhasea(String phasea) {
		this.phasea = phasea;
	}

	public String getPhaseb() {
		return phaseb;
	}

	public void setPhaseb(String phaseb) {
		this.phaseb = phaseb;
	}

	public String getPhasec() {
		return phasec;
	}

	public void setPhasec(String phasec) {
		this.phasec = phasec;
	}
}