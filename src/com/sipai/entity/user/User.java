package com.sipai.entity.user;

import java.util.List;

import com.sipai.entity.base.SQLAdapter;

public class User extends SQLAdapter {
    private String id;

    private String name;

    private String password;

    private String active;

    private String caption;

    private String sex;

    private String officeroom;

    private String officephone;

    private String useremail;

    private String pid;
    
    private String _pname;
    
    private List<Job> jobs;
    
	private Integer lgnum;

    private Double totaltime;

    private Integer morder;  

    private String serial;

    private String cardid;
    
    private List<Role> roles;
    
    private String insuser;
    
    private String insdt;
    
    private String mobile;
    
    private String currentip;
    
    private String lastlogintime;

    
    public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public String getCurrentip() {
		return currentip;
	}

	public void setCurrentip(String currentip) {
		this.currentip = currentip;
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOfficeroom() {
        return officeroom;
    }

    public void setOfficeroom(String officeroom) {
        this.officeroom = officeroom;
    }

    public String getOfficephone() {
        return officephone;
    }

    public void setOfficephone(String officephone) {
        this.officephone = officephone;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getLgnum() {
        return lgnum;
    }

    public void setLgnum(Integer lgnum) {
        this.lgnum = lgnum;
    }

    public Double getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(Double totaltime) {
        this.totaltime = totaltime;
    }

    public Integer getMorder() {
        return morder;
    }

    public void setMorder(Integer morder) {
        this.morder = morder;
    }   

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }
    
    public String get_pname() {
		return _pname;
	}

	public void set_pname(String _pname) {
		this._pname = _pname;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}