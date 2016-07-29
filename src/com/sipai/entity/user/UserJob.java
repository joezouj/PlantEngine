package com.sipai.entity.user;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;

@Component
public class UserJob extends SQLAdapter{
    private String userid;

    private String jobid;

    private String unitid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }
}