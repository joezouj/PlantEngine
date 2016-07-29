package com.sipai.entity.process;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;

@Component
public class RealDetailsMaterial extends SQLAdapter{
    private String id;

    private String pid;

    private String materialid;
    
    private MaterialInfo material;

    private Double amount;
    
    private String insuser;

    private String insdt;

    private String upduser;

    private String upddt;

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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public MaterialInfo getMaterial() {
		return material;
	}

	public void setMaterial(MaterialInfo material) {
		this.material = material;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

    public String getUpduser() {
        return upduser;
    }

    public void setUpduser(String upduser) {
        this.upduser = upduser;
    }

    public String getUpddt() {
        return upddt;
    }

    public void setUpddt(String upddt) {
        this.upddt = upddt;
    }
}