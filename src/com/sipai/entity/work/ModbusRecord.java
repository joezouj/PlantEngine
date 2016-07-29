package com.sipai.entity.work;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class ModbusRecord extends SQLAdapter{
    private String id;

    private String lastdate;

    private String record;

    private String insuser;

    private String insdt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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