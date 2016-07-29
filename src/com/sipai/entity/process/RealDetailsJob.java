package com.sipai.entity.process;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;

@Component
public class RealDetailsJob extends SQLAdapter{
    private String id;

    private String pid;

    private String unitid;
    
    private String unitname;
    
    private String jobid;

    private String jobname;
    
    private String insuser;

    private String insdt;

    private String upduser;

    private String upddt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
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

    public String getUpduser() {
        return upduser;
    }

    public void setUpduser(String upduser) {
        this.upduser = upduser;
    }

    public String getUpddt() {
        return upddt;
    }

    public void setUpddt(String upddt) {
        this.upddt = upddt;
    }

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
}