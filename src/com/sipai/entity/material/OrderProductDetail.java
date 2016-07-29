package com.sipai.entity.material;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.work.WorkOrder;

public class OrderProductDetail extends SQLAdapter{
    private String id;

    private String insuser;

    private String insdt;

    private String pid;

    private String productuid;
    
    private String productionorderno;
    
    private String workorderid;
    
    private String processrealid;
    
    private String status;
    
    private String finishflag_wfo;      
    private WorkOrder workorder; 
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductuid() {
        return productuid;
    }

    public void setProductuid(String productuid) {
        this.productuid = productuid;
    }

	public String getProductionorderno() {
		return productionorderno;
	}

	public void setProductionorderno(String productionorderno) {
		this.productionorderno = productionorderno;
	}

	public String getWorkorderid() {
		return workorderid;
	}

	public void setWorkorderid(String workorderid) {
		this.workorderid = workorderid;
	}

	public String getProcessrealid() {
		return processrealid;
	}

	public void setProcessrealid(String processrealid) {
		this.processrealid = processrealid;
	}

	public String getFinishflag_wfo() {
		return finishflag_wfo;
	}

	public void setFinishflag_wfo(String finishflag_wfo) {
		this.finishflag_wfo = finishflag_wfo;
	}

	public WorkOrder getWorkorder() {
		return workorder;
	}

	public void setWorkorder(WorkOrder workorder) {
		this.workorder = workorder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}