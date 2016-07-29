package com.sipai.entity.material;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class MaterialBox extends SQLAdapter{
    private String id;

    private String boxnumber;

    private String name;

    private String workstationid;

    private String typeid;

    private String status;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoxnumber() {
        return boxnumber;
    }

    public void setBoxnumber(String boxnumber) {
        this.boxnumber = boxnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkstationid() {
        return workstationid;
    }

    public void setWorkstationid(String workstationid) {
        this.workstationid = workstationid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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