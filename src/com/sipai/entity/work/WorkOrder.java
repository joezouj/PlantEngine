package com.sipai.entity.work;


import java.util.List;

import com.sipai.entity.base.SQLAdapter;

public class WorkOrder extends SQLAdapter{
    private String id;

    private String wforderid;

    private String stdt;

    private String eddt;

    private String productno;

    private String productname;

    private String productformat;

    private String lineid;
    
    private String linename;

    private String bomid;

    private String dwgid;

    private String insuser;

    private String insdt;

    private String updateuser;

    private String updatedt;

    private String type;

    private String productionorderno;

    private String status;
    
    private String pstatus;

    private String salesorderno;

    private String productid;

    private String ordercreatedate;

    private String orderfinishdate;

    private String productuid;

    private String planid;
    
    private String processrealid;
    
    private String wftaskid;//当前的工序id
    private String wftaskname;//用于显示当前的工序名
    private String wftaskdisplay;//用于显示当前的工序显示名
    private String wftaskurl;//用于显示当前的工序url
    
    private List<WorkTaskWorkStation> workstationlist;//工位
    private List<Workstation> activeWorkstations;//当前工位
    private String username;//当前操作员
    
    private int _productnum; //用于记录产品数 注：只用于任务列表分级显示时统计产品数
    
    private List<WorkScheduling> workscheduling;


	public int get_productnum() {
		return _productnum;
	}

	public void set_productnum(int _productnum) {
		this._productnum = _productnum;
	}

	public List<WorkScheduling> getWorkscheduling() {
		return workscheduling;
	}

	public void setWorkscheduling(List<WorkScheduling> workscheduling) {
		this.workscheduling = workscheduling;
	}

	public String getPlanid() {
		return planid;
	}

	public void setPlanid(String planid) {
		this.planid = planid;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWforderid() {
        return wforderid;
    }

    public void setWforderid(String wforderid) {
        this.wforderid = wforderid;
    }

    public String getStdt() {
        return stdt;
    }

    public void setStdt(String stdt) {
        this.stdt = stdt;
    }

    public String getEddt() {
        return eddt;
    }

    public void setEddt(String eddt) {
        this.eddt = eddt;
    }

    public String getProductno() {
        return productno;
    }

    public void setProductno(String productno) {
        this.productno = productno;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductformat() {
        return productformat;
    }

    public void setProductformat(String productformat) {
        this.productformat = productformat;
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid;
    }

    public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getBomid() {
        return bomid;
    }

    public void setBomid(String bomid) {
        this.bomid = bomid;
    }

    public String getDwgid() {
        return dwgid;
    }

    public void setDwgid(String dwgid) {
        this.dwgid = dwgid;
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

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public String getUpdatedt() {
        return updatedt;
    }

    public void setUpdatedt(String updatedt) {
        this.updatedt = updatedt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductionorderno() {
        return productionorderno;
    }

    public void setProductionorderno(String productionorderno) {
        this.productionorderno = productionorderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalesorderno() {
        return salesorderno;
    }

    public void setSalesorderno(String salesorderno) {
        this.salesorderno = salesorderno;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getOrdercreatedate() {
        return ordercreatedate;
    }

    public void setOrdercreatedate(String ordercreatedate) {
        this.ordercreatedate = ordercreatedate;
    }

    public String getOrderfinishdate() {
        return orderfinishdate;
    }

    public void setOrderfinishdate(String orderfinishdate) {
        this.orderfinishdate = orderfinishdate;
    }

    public String getProductuid() {
        return productuid;
    }

    public void setProductuid(String productuid) {
        this.productuid = productuid;
    }

	public String getWftaskid() {
		return wftaskid;
	}

	public void setWftaskid(String wftaskid) {
		this.wftaskid = wftaskid;
	}

	public String getWftaskname() {
		return wftaskname;
	}

	public void setWftaskname(String wftaskname) {
		this.wftaskname = wftaskname;
	}

	public String getWftaskdisplay() {
		return wftaskdisplay;
	}

	public void setWftaskdisplay(String wftaskdisplay) {
		this.wftaskdisplay = wftaskdisplay;
	}

	public String getWftaskurl() {
		return wftaskurl;
	}

	public void setWftaskurl(String wftaskurl) {
		this.wftaskurl = wftaskurl;
	}

	public List<WorkTaskWorkStation> getWorkstationlist() {
		return workstationlist;
	}

	public void setWorkstationlist(List<WorkTaskWorkStation> workstationlist) {
		this.workstationlist = workstationlist;
	}

	public List<Workstation> getActiveWorkstations() {
		return activeWorkstations;
	}

	public void setActiveWorkstations(List<Workstation> activeWorkstations) {
		this.activeWorkstations = activeWorkstations;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProcessrealid() {
		return processrealid;
	}

	public void setProcessrealid(String processrealid) {
		this.processrealid = processrealid;
	}

	public String getPstatus() {
		return pstatus;
	}

	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}
	
}