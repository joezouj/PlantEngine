package com.sipai.entity.material;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Workstation;

public class MaterialRequisition extends SQLAdapter{
    private String id;

    private String applyuserid;

    private String workstationid;

    private String materialid;

    private String quantity;

    private String requestsenddate;

    private String orderno;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;

    private String workshopid;
    
    private String status;
    private String deliverquantity;
    private String deliverman;
    private String deliverdate;
    
    private User applyuser;
    private MaterialInfo materialinfo;
    private Workstation workstation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyuserid() {
        return applyuserid;
    }

    public void setApplyuserid(String applyuserid) {
        this.applyuserid = applyuserid;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
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

    public String getWorkshopid() {
        return workshopid;
    }

    public void setWorkshopid(String workshopid) {
        this.workshopid = workshopid;
    }

	public String getRequestsenddate() {
		return requestsenddate;
	}

	public void setRequestsenddate(String requestsenddate) {
		this.requestsenddate = requestsenddate;
	}

	public User getApplyuser() {
		return applyuser;
	}

	public void setApplyuser(User applyuser) {
		this.applyuser = applyuser;
	}

	public MaterialInfo getMaterialinfo() {
		return materialinfo;
	}

	public void setMaterialinfo(MaterialInfo materialinfo) {
		this.materialinfo = materialinfo;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliverquantity() {
		return deliverquantity;
	}

	public void setDeliverquantity(String deliverquantity) {
		this.deliverquantity = deliverquantity;
	}

	public String getDeliverman() {
		return deliverman;
	}

	public void setDeliverman(String deliverman) {
		this.deliverman = deliverman;
	}

	public String getDeliverdate() {
		return deliverdate;
	}

	public void setDeliverdate(String deliverdate) {
		this.deliverdate = deliverdate;
	}
}