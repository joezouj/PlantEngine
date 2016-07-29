package com.sipai.dao.material;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.OrderProductDetailconnect;
@Repository
public class OrderProductDetailconnectDao extends CommDaoImpl<OrderProductDetailconnect>{
	public OrderProductDetailconnectDao() {
		super();
		this.setMappernamespace("material.OrderProductDetailconnectMapper");
	}	
}
