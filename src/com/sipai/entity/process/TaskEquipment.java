package com.sipai.entity.process;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.equipment.EquipmentCard;
@Component
public class TaskEquipment extends SQLAdapter{
    private String id;

    private String processid;

    private String taskname;

    private String equipmentid;

    private String status;

    private String memo;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private EquipmentCard equipmentCard;
    
    /***********equipment的属性************/
    private String equipmentcardid;//设备编号
    private String equipmentname;//设备名称
    private String equipmentmodel;//型号
    private String equipmentclass;//类型
    private String geographyarea;//位置
    private String currentmanageflag;//状态
    private String remark;//备注
    
    public EquipmentCard getEquipmentCard() {
		return equipmentCard;
	}

	public void setEquipmentCard(EquipmentCard equipmentCard) {
		this.equipmentCard = equipmentCard;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

	public String getEquipmentcardid() {
		return equipmentcardid;
	}

	public void setEquipmentcardid(String equipmentcardid) {
		this.equipmentcardid = equipmentcardid;
	}

	public String getEquipmentname() {
		return equipmentname;
	}

	public void setEquipmentname(String equipmentname) {
		this.equipmentname = equipmentname;
	}

	public String getEquipmentmodel() {
		return equipmentmodel;
	}

	public void setEquipmentmodel(String equipmentmodel) {
		this.equipmentmodel = equipmentmodel;
	}

	public String getEquipmentclass() {
		return equipmentclass;
	}

	public void setEquipmentclass(String equipmentclass) {
		this.equipmentclass = equipmentclass;
	}

	public String getGeographyarea() {
		return geographyarea;
	}

	public void setGeographyarea(String geographyarea) {
		this.geographyarea = geographyarea;
	}

	public String getCurrentmanageflag() {
		return currentmanageflag;
	}

	public void setCurrentmanageflag(String currentmanageflag) {
		this.currentmanageflag = currentmanageflag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}