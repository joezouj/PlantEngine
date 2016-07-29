package com.sipai.entity.work;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class ModbusFig extends SQLAdapter{
    private String id;

    private String ipsever;

    private String port;

    private String slaveid;

    private String order32;

    private String name;

    private String remarks;

    private String flag;

    private String insuser;

    private String insdt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpsever() {
        return ipsever;
    }

    public void setIpsever(String ipsever) {
        this.ipsever = ipsever;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSlaveid() {
        return slaveid;
    }

    public void setSlaveid(String slaveid) {
        this.slaveid = slaveid;
    }

    public String getOrder32() {
        return order32;
    }

    public void setOrder32(String order32) {
        this.order32 = order32;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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