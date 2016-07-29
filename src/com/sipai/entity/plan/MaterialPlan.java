package com.sipai.entity.plan;

import com.sipai.entity.base.SQLAdapter;

public class MaterialPlan extends SQLAdapter {
	
	private String id;

    private String name;

    private String plandt;

    private String workstationid;

    private String workstationname;
    
    private String workstationserial;
    
    private String materialid;

    private String materialcode;
    
    private String materialname;

    private Double amount;
    
    private String unit;

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

	public String getPlandt() {
		return plandt;
	}

	public void setPlandt(String plandt) {
		this.plandt = plandt;
	}

	public String getWorkstationid() {
		return workstationid;
	}

	public void setWorkstationid(String workstationid) {
		this.workstationid = workstationid;
	}

	public String getWorkstationname() {
		return workstationname;
	}

	public void setWorkstationname(String workstationname) {
		this.workstationname = workstationname;
	}

	public String getWorkstationserial() {
		return workstationserial;
	}

	public void setWorkstationserial(String workstationserial) {
		this.workstationserial = workstationserial;
	}

	public String getMaterialid() {
		return materialid;
	}

	public void setMaterialid(String materialid) {
		this.materialid = materialid;
	}

	public String getMaterialcode() {
		return materialcode;
	}

	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}

	public String getMaterialname() {
		return materialname;
	}

	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
