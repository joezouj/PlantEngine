package com.sipai.entity.material;


import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class MaterialShort extends SQLAdapter{
    private String id;

    private String materialid;

    private String productid;

    private String wftaskid;

    private String workstationserial;

    private String insuser;

    private String insdt;

    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getWftaskid() {
        return wftaskid;
    }

    public void setWftaskid(String wftaskid) {
        this.wftaskid = wftaskid;
    }

    public String getWorkstationserial() {
        return workstationserial;
    }

    public void setWorkstationserial(String workstationserial) {
        this.workstationserial = workstationserial;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}