package com.sipai.entity.process;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.work.Line;

@Component
public class Real extends SQLAdapter{
    private String id;

    private String processid;
    
    private Processes process;

    private String name;

    private String productid;
    
    private MaterialInfo product;
    
    private String lineid;
    
    private String drawing;

    private String bomid;

    private String description;

    private String insuser;

    private String insdt;

    private String updateuser;

    private String updatedt;

    private String st;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public MaterialInfo getProduct() {
		return product;
	}

	public void setProduct(MaterialInfo product) {
		this.product = product;
	}

	public Processes getProcess() {
		return process;
	}

	public void setProcess(Processes process) {
		this.process = process;
	}

	public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getBomid() {
        return bomid;
    }

    public void setBomid(String bomid) {
        this.bomid = bomid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public String getUpdatedt() {
        return updatedt;
    }

    public void setUpdatedt(String updatedt) {
        this.updatedt = updatedt;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }
}