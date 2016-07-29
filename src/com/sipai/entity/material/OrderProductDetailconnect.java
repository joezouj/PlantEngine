package com.sipai.entity.material;

import java.util.Date;

import com.sipai.entity.base.SQLAdapter;

public class OrderProductDetailconnect extends SQLAdapter{
    private String id;

    private String insdt;

    private String insuser;

    private String planid;

    private String productdetailid;

    private String workorderid;

    private String processrealid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsdt() {
        return insdt;
    }

    public void setInsdt(String insdt) {
        this.insdt = insdt;
    }

    public String getInsuser() {
        return insuser;
    }

    public void setInsuser(String insuser) {
        this.insuser = insuser;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getProductdetailid() {
        return productdetailid;
    }

    public void setProductdetailid(String productdetailid) {
        this.productdetailid = productdetailid;
    }

    public String getWorkorderid() {
        return workorderid;
    }

    public void setWorkorderid(String workorderid) {
        this.workorderid = workorderid;
    }

    public String getProcessrealid() {
        return processrealid;
    }

    public void setProcessrealid(String processrealid) {
        this.processrealid = processrealid;
    }
}