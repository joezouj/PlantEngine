package com.sipai.entity.material;

import com.sipai.entity.base.SQLAdapter;

public class CutterInfo extends SQLAdapter{
    private String id;

    private String cuttercode;

    private String cuttername;

    private String typeid;

    private String positionid;

    private Double life;

    private Double length;

    private Double ply;

    private Double width;

    private String producer;

    private String status;

    private String remark;

    private String insuser;

    private String insdt;

    private String modify;

    private String modifydt;
    
    private CutterType cutterType;
    
    private CutterPosition cutterPosition;  
    
    private String _typename;
    
    private String _positionname;
    
    public String get_typename() {
		return _typename;
	}

	public void set_typename(String _typename) {
		this._typename = _typename;
	}

	public String get_positionname() {
		return _positionname;
	}

	public void set_positionname(String _positionname) {
		this._positionname = _positionname;
	}

	public CutterType getCutterType() {
		return cutterType;
	}

	public void setCutterType(CutterType cutterType) {
		this.cutterType = cutterType;
	}

	public CutterPosition getCutterPosition() {
		return cutterPosition;
	}

	public void setCutterPosition(CutterPosition cutterPosition) {
		this.cutterPosition = cutterPosition;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuttercode() {
        return cuttercode;
    }

    public void setCuttercode(String cuttercode) {
        this.cuttercode = cuttercode;
    }

    public String getCuttername() {
        return cuttername;
    }

    public void setCuttername(String cuttername) {
        this.cuttername = cuttername;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getPositionid() {
        return positionid;
    }

    public void setPositionid(String positionid) {
        this.positionid = positionid;
    }

    public Double getLife() {
        return life;
    }

    public void setLife(Double life) {
        this.life = life;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getPly() {
        return ply;
    }

    public void setPly(Double ply) {
        this.ply = ply;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public String getModifydt() {
        return modifydt;
    }

    public void setModifydt(String modifydt) {
        this.modifydt = modifydt;
    }
}