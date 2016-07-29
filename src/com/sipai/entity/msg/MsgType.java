package com.sipai.entity.msg;

import java.util.Date;
import java.util.List;

import com.sipai.entity.base.SQLAdapter;

public class MsgType extends SQLAdapter{
    private String id;

    private String name;

    private String insuser;

    private Date insdt;  

    private String pid;

    private String remark;

    private String status;
    
    private String sendway;
    
    private List<String> _roleid;
    private List<String> _msguserid;
    private List<String> _smsuserid;

    private List<MsgRole> role;//接收人,一对多
    private List<Msguser> msguser;//接收人,一对多
    private List<Smsuser> smsuser;//接收人,一对多
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsuser() {
        return insuser;
    }

    public void setInsuser(String insuser) {
        this.insuser = insuser;
    }

    public Date getInsdt() {
        return insdt;
    }

    public void setInsdt(Date insdt) {
        this.insdt = insdt;
    }   

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public List<MsgRole> getRole() {
		return role;
	}

	public void setRole(List<MsgRole> role) {
		this.role = role;
	}

	public List<Msguser> getMsguser() {
		return msguser;
	}

	public void setMsguser(List<Msguser> msguser) {
		this.msguser = msguser;
	}

	public List<Smsuser> getSmsuser() {
		return smsuser;
	}

	public void setSmsuser(List<Smsuser> smsuser) {
		this.smsuser = smsuser;
	}

	public String getSendway() {
		return sendway;
	}

	public void setSendway(String sendway) {
		this.sendway = sendway;
	}

	public List<String> get_roleid() {
		return _roleid;
	}

	public void set_roleid(List<String> _roleid) {
		this._roleid = _roleid;
	}

	public List<String> get_msguserid() {
		return _msguserid;
	}

	public void set_msguserid(List<String> _msguserid) {
		this._msguserid = _msguserid;
	}

	public List<String> get_smsuserid() {
		return _smsuserid;
	}

	public void set_smsuserid(List<String> _smsuserid) {
		this._smsuserid = _smsuserid;
	}

	
}