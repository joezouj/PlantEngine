package com.sipai.entity.work;

import java.util.List;

import com.sipai.entity.base.SQLAdapter;

public class Group extends SQLAdapter{
    private String id;

    private String name;

    private String deptid;
    
    private String deptname;

    private String insuser;

    private String insdt;

    private String remark;
    
    private List<GroupMember> groupmembers;
    private String schedulingflag;//标记group是否被排班
    

	public String getSchedulingflag() {
		return schedulingflag;
	}

	public void setSchedulingflag(String schedulingflag) {
		this.schedulingflag = schedulingflag;
	}

	public List<GroupMember> getGroupmembers() {
		return groupmembers;
	}

	public void setGroupmembers(List<GroupMember> groupmembers) {
		this.groupmembers = groupmembers;
	}

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

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
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