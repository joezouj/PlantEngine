package com.sipai.entity.plan;

import java.util.List;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.material.OrderProductDetail;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.work.Line;

public class DailyPlan extends SQLAdapter{
    private String id;

	private String stdt;

	private String eddt;

	private String productno;

	private String productname;

	private String lineid;
	
	private Line line;

	private String bomid;

	private String dwgid;

	private String insuser;

	private String insdt;

	private String remark;

	private String type;

	private String salesorderid;

	private String productionorderno;

	private String productquantity;

	private String finishedquantity;
	
	private String status;
	
	private String delflag;
	
	private String taskchangedstatus;
	
	private String processrealid;
	
	private String processrealname;
	
	private String pid;
	
	private String porder;
	
	private MaterialInfo product;
	private SalesOrderProduct salesorderproduct;
	private List<OrderProductDetail> orderproductdetail;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStdt() {
		return stdt;
	}

	public void setStdt(String stdt) {
		this.stdt = stdt;
	}

	public String getEddt() {
		return eddt;
	}

	public void setEddt(String eddt) {
		this.eddt = eddt;
	}

	public String getProductno() {
		return productno;
	}

	public void setProductno(String productno) {
		this.productno = productno;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getBomid() {
		return bomid;
	}

	public void setBomid(String bomid) {
		this.bomid = bomid;
	}

	public String getDwgid() {
		return dwgid;
	}

	public void setDwgid(String dwgid) {
		this.dwgid = dwgid;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSalesorderid() {
		return salesorderid;
	}

	public void setSalesorderid(String salesorderid) {
		this.salesorderid = salesorderid;
	}

	public String getProductionorderno() {
		return productionorderno;
	}

	public void setProductionorderno(String productionorderno) {
		this.productionorderno = productionorderno;
	}

	public String getProductquantity() {
		return productquantity;
	}

	public void setProductquantity(String productquantity) {
		this.productquantity = productquantity;
	}

	public String getFinishedquantity() {
		return finishedquantity;
	}

	public void setFinishedquantity(String finishedquantity) {
		this.finishedquantity = finishedquantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelflag() {
		return delflag;
	}

	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}

	public MaterialInfo getProduct() {
		return product;
	}

	public void setProduct(MaterialInfo product) {
		this.product = product;
	}

	public SalesOrderProduct getSalesorderproduct() {
		return salesorderproduct;
	}

	public void setSalesorderproduct(SalesOrderProduct salesorderproduct) {
		this.salesorderproduct = salesorderproduct;
	}

	public List<OrderProductDetail> getOrderproductdetail() {
		return orderproductdetail;
	}

	public void setOrderproductdetail(List<OrderProductDetail> orderproductdetail) {
		this.orderproductdetail = orderproductdetail;
	}

	public String getTaskchangedstatus() {
		return taskchangedstatus;
	}

	public void setTaskchangedstatus(String taskchangedstatus) {
		this.taskchangedstatus = taskchangedstatus;
	}

	public String getProcessrealid() {
		return processrealid;
	}

	public void setProcessrealid(String processrealid) {
		this.processrealid = processrealid;
	}

	public String getProcessrealname() {
		return processrealname;
	}

	public void setProcessrealname(String processrealname) {
		this.processrealname = processrealname;
	}


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public String getPorder() {
		return porder;
	}

	public void setPorder(String porder) {
		this.porder = porder;
	}
	
}