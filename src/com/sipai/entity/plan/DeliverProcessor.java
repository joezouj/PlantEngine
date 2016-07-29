package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.user.User;


public class DeliverProcessor extends SQLAdapter{
    private String id;

    private String pid;

    private String processorid;

    private String insuser;

    private String insdt;
    
    private User processor;

    public User getProcessor() {
		return processor;
	}

	public void setProcessor(User processor) {
		this.processor = processor;
	}

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

    public String getProcessorid() {
        return processorid;
    }

    public void setProcessorid(String processorid) {
        this.processorid = processorid;
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