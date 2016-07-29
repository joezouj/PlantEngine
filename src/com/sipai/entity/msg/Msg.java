package com.sipai.entity.msg;

import java.util.Date;
import java.util.List;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.user.User;

public class Msg extends SQLAdapter{
    private String id;

    private String pid;

    private String suserid;

    private String sdt;

    private String url;

    private String readflag;

    private String content;

    private String backid;

    private String typeid;

    private String plansdt;

    private String plansdate;

    private String sendflag;

    private String delflag;

    private String alerturl;

    private String insuser;

    private String insdt;

    private String bizid;
    private String issms;
    private User susername;//发送人,一对一
    private MsgType typename;//一对一
    private List<MsgRecv> mrecv;//接收人,一对多
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

    public String getSuserid() {
        return suserid;
    }

    public void setSuserid(String suserid) {
        this.suserid = suserid;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReadflag() {
        return readflag;
    }

    public void setReadflag(String readflag) {
        this.readflag = readflag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackid() {
        return backid;
    }

    public void setBackid(String backid) {
        this.backid = backid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getPlansdt() {
        return plansdt;
    }

    public void setPlansdt(String plansdt) {
        this.plansdt = plansdt;
    }

    public String getPlansdate() {
        return plansdate;
    }

    public void setPlansdate(String plansdate) {
        this.plansdate = plansdate;
    }

    public String getSendflag() {
        return sendflag;
    }

    public void setSendflag(String sendflag) {
        this.sendflag = sendflag;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getAlerturl() {
        return alerturl;
    }

    public void setAlerturl(String alerturl) {
        this.alerturl = alerturl;
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

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

	public User getSusername() {
		return susername;
	}

	public void setSusername(User susername) {
		this.susername = susername;
	}

	public MsgType getTypename() {
		return typename;
	}

	public void setTypename(MsgType typename) {
		this.typename = typename;
	}

	public List<MsgRecv> getMrecv() {
		return mrecv;
	}

	public void setMrecv(List<MsgRecv> mrecv) {
		this.mrecv = mrecv;
	}

	public String getIssms() {
		return issms;
	}

	public void setIssms(String issms) {
		this.issms = issms;
	}


	

	
}