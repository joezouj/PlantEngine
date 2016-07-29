package com.sipai.entity.process;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.equipment.EquipmentCard;

@Component
public class RealDetailsEquipment extends SQLAdapter{
    private String id;

    private String pid;

    private String equipmentid;
    
    private EquipmentCard equipment;

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

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public EquipmentCard getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentCard equipment) {
		this.equipment = equipment;
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
}