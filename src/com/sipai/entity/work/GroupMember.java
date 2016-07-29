package com.sipai.entity.work;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class GroupMember extends SQLAdapter{
    private String id;

    private String groupid;

    private String userid;
    
    private String username;

    private String usertype;

    private String insuser;

    private String insdt;

    private String remark;
    private String _workstationid;
    private String _workstationname;
    private String _checkflag;

    public String get_checkflag() {
		return _checkflag;
	}

	public void set_checkflag(String _checkflag) {
		this._checkflag = _checkflag;
	}

	public String getWorkstationid() {
		return _workstationid;
	}

	public void setWorkstationid(String workstationid) {
		this._workstationid = workstationid;
	}

	public String getWorkstationname() {
		return _workstationname;
	}

	public void setWorkstationname(String workstationname) {
		this._workstationname = workstationname;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}