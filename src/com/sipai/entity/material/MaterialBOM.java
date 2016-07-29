package com.sipai.entity.material;

import java.util.ArrayList;
import java.util.List;

import com.sipai.entity.base.SQLAdapter;

public class MaterialBOM extends SQLAdapter{
    private String id;

    private String pid;

    private Integer ordernumber;

    private String materialcode;

    private String materialname;

    private Double num;

    private Integer version;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private String unit;

    public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

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

    public Integer getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(Integer ordernumber) {
        this.ordernumber = ordernumber;
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

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
    
    private String _lvl;
    
	public String get_lvl() {
		return _lvl;
	}

	public void set_lvl(String _lvl) {
		this._lvl = _lvl;
	}
    
    
}