package com.sipai.entity.material;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
@Component
public class SalesOrderProduct extends SQLAdapter{
    private String id;

	private String insuser;

	private String insdt;

	private String salesorderno;

	private String clientsid;

	private String productid;

	private String productnum;

	private String ordercreatedate;

	private String orderfinishdate;

	private String deliverydate;

	private String status;
	
    private List<OrderProductDetail> orderproductdetail;
    private MaterialInfo product;
    private Clients clients;
	
    private String clientname;


	public Clients getClients() {
		return clients;
	}

	public void setClients(Clients clients) {
		this.clients = clients;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSalesorderno() {
		return salesorderno;
	}

	public void setSalesorderno(String salesorderno) {
		this.salesorderno = salesorderno;
	}

	public String getClientsid() {
		return clientsid;
	}

	public void setClientsid(String clientsid) {
		this.clientsid = clientsid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getProductnum() {
		return productnum;
	}

	public void setProductnum(String productnum) {
		this.productnum = productnum;
	}

	public String getOrdercreatedate() {
		return ordercreatedate;
	}

	public void setOrdercreatedate(String ordercreatedate) {
		this.ordercreatedate = ordercreatedate;
	}

	public String getOrderfinishdate() {
		return orderfinishdate;
	}

	public void setOrderfinishdate(String orderfinishdate) {
		this.orderfinishdate = orderfinishdate;
	}

	public String getDeliverydate() {
		return deliverydate;
	}

	public void setDeliverydate(String deliverydate) {
		this.deliverydate = deliverydate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public List<OrderProductDetail> getOrderproductdetail() {
		return orderproductdetail;
	}

	public void setOrderproductdetail(List<OrderProductDetail> orderproductdetail) {
		this.orderproductdetail = orderproductdetail;
	}

	public MaterialInfo getProduct() {
		return product;
	}

	public void setProduct(MaterialInfo product) {
		this.product = product;
	}

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	
	

}