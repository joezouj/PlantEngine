package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;

public class DeliverDetail extends SQLAdapter{
    private String id;

    private String pid;

    private String materialid;

    private Double planamount;

    private Double deliveramount;

    private String boxnumber;

    private String boxname;

    private Integer deliverst;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private MaterialInfo materialInfo;

    public MaterialInfo getMaterialInfo() {
		return materialInfo;
	}

	public void setMaterialInfo(MaterialInfo materialInfo) {
		this.materialInfo = materialInfo;
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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public Double getPlanamount() {
        return planamount;
    }

    public void setPlanamount(Double planamount) {
        this.planamount = planamount;
    }

    public Double getDeliveramount() {
        return deliveramount;
    }

    public void setDeliveramount(Double deliveramount) {
        this.deliveramount = deliveramount;
    }

    public String getBoxnumber() {
        return boxnumber;
    }

    public void setBoxnumber(String boxnumber) {
        this.boxnumber = boxnumber;
    }

    public String getBoxname() {
        return boxname;
    }

    public void setBoxname(String boxname) {
        this.boxname = boxname;
    }

    public Integer getDeliverst() {
        return deliverst;
    }

    public void setDeliverst(Integer deliverst) {
        this.deliverst = deliverst;
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
}