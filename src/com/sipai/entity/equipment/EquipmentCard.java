package com.sipai.entity.equipment;

import java.util.List;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.msg.MsgRecv;

public class EquipmentCard extends SQLAdapter{
    private String id;

    private String insuser;

    private String insdt;

    private String equipmentcardid;

    private String equipmentclassid;
    
    private String equipmentclassname;

    private String equipmentmodel;

    private String areaid;

    private String currentmanageflag;
    
    private EquipmentClass equipmentclass;
    private GeographyArea geographyarea;
    private String equipmentname;

    private String remark;

    public String getEquipmentname() {
        return equipmentname;
    }

    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
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

    public String getEquipmentcardid() {
        return equipmentcardid;
    }

    public void setEquipmentcardid(String equipmentcardid) {
        this.equipmentcardid = equipmentcardid;
    }

    public String getEquipmentclassid() {
        return equipmentclassid;
    }

    public void setEquipmentclassid(String equipmentclassid) {
        this.equipmentclassid = equipmentclassid;
    }

    public String getEquipmentclassname() {
		return equipmentclassname;
	}

	public void setEquipmentclassname(String equipmentclassname) {
		this.equipmentclassname = equipmentclassname;
	}

	public String getEquipmentmodel() {
        return equipmentmodel;
    }

    public void setEquipmentmodel(String equipmentmodel) {
        this.equipmentmodel = equipmentmodel;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getCurrentmanageflag() {
        return currentmanageflag;
    }

    public void setCurrentmanageflag(String currentmanageflag) {
        this.currentmanageflag = currentmanageflag;
    }

	public EquipmentClass getEquipmentclass() {
		return equipmentclass;
	}

	public void setEquipmentclass(EquipmentClass equipmentclass) {
		this.equipmentclass = equipmentclass;
	}

	public GeographyArea getGeographyarea() {
		return geographyarea;
	}

	public void setGeographyarea(GeographyArea geographyarea) {
		this.geographyarea = geographyarea;
	}




}