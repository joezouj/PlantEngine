package com.sipai.entity.material;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.document.Data;

public class MaterialInfo extends SQLAdapter{
    private String id;

    private String materialcode;

    private String materialname;

    private String figurenumberid;
    
    private Data figure;
    
    private String spec;
    
    private String unit;

    private String typeid;
    
    private String _typename;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private String status;
    
    private MaterialType materialtype;
    
    private String delivertype;

    public String getDelivertype() {
		return delivertype;
	}

	public void setDelivertype(String delivertype) {
		this.delivertype = delivertype;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String get_typename() {
		return _typename;
	}

	public void set_typename(String _typename) {
		this._typename = _typename;
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
    
	public String getFigurenumberid() {
		return figurenumberid;
	}

	public void setFigurenumberid(String figurenumberid) {
		this.figurenumberid = figurenumberid;
	}

	public Data getFigure() {
		return figure;
	}

	public void setFigure(Data figure) {
		this.figure = figure;
	}

	public MaterialType getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(MaterialType materialtype) {
		this.materialtype = materialtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}