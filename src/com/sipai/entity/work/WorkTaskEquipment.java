package com.sipai.entity.work;


import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.equipment.EquipmentCard;
@Component
public class WorkTaskEquipment extends SQLAdapter {
    private String id;

    private String equipmentid;

    private String workstationid;

    private String taskid;
    
    private String processrealdetailid;

    private String insuser;

    private String insdt;
    
    private EquipmentCard equipment;
    
    private boolean _checked;
    
	public boolean is_checked() {
		return _checked;
	}

	public void set_checked(boolean _checked) {
		this._checked = _checked;
	}

	public EquipmentCard getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentCard equipment) {
		this.equipment = equipment;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getWorkstationid() {
        return workstationid;
    }

    public void setWorkstationid(String workstationid) {
        this.workstationid = workstationid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getProcessrealdetailid() {
		return processrealdetailid;
	}

	public void setProcessrealdetailid(String processrealdetailid) {
		this.processrealdetailid = processrealdetailid;
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
}